package com.ips.inplainsight;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.Tag;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.Manifest;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainGame extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback, com.google.android.gms.location.LocationListener {


    long diff;
    int AoD;
    Intent intent;
    int intentChange = 0;

    //temp objects
    PlayerClass curPlayer = new PlayerClass();
    PlayerClass targetPlayer = new PlayerClass();
    PlayerClass asPlayer = new PlayerClass();

    Game curGame = new Game();

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;

    private static final String TAG = "MainGame";

    // Gravity rotational data
    private float gravity[];
    // Magnetic rotational data
    private float magnetic[]; //for magnetic rotational data
    private float accels[] = new float[3];
    private float mags[] = new float[3];
    private float[] values = new float[3];

    // azimuth, pitch and roll
    private float azimuth;
    private float pitch;
    private float roll;
    SensorManager sManager;
    //LocationManager lm;
    //Location location;
    private Circle circleOuter;
    private Circle circleInner;
    Circle dummyPlayer;

    private LocationCallback mLocationCallback;

    private FusedLocationProviderClient mFusedLocationClient;
    //GoogleApiClient gac;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    Handler h = new Handler();
    int delay = 3000; //milliseconds
    Runnable runnable;
    TextView mTextView;
    TextView llTextView;

    //@Override
    //public void onPause() {
      //  super.onPause();
       // timerHandler.removeCallbacks(timerRunnable);
    //}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        sManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mTextView = findViewById(R.id.DirectionTextView);
        llTextView = findViewById(R.id.LatLongTextView);

        //temp objects
        asPlayer.userName = "asPlayer";
        curPlayer.userName = "curPlayer";
        targetPlayer.userName = "targetPlayer";
        curGame.addPlayer(asPlayer);
        curGame.addPlayer(curPlayer);
        curGame.addPlayer(targetPlayer);
        //Log.d(TAG, "Target: " + targetPlayer.getTarget());

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult){
                if(locationResult == null){
                    return;
                }
                for(Location location : locationResult.getLocations()){
                    //Update UI
                    curPlayer.setCurLoc(new LatLng(location.getLatitude(), location.getLongitude()));
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();
                    llTextView.setText("   lat:   " + latitude + "\n   long:   " + longitude);

                    //in bounds

                    if(distance(location.getLatitude(), circleOuter.getCenter().latitude, location.getLongitude(), circleOuter.getCenter().longitude) > circleOuter.getRadius()){
                        //player out of outer bounds, DQ
                        llTextView.setTextColor(Color.parseColor("#ff0000"));
                    }
                    else if(distance(location.getLatitude(), circleInner.getCenter().latitude, location.getLongitude(), circleInner.getCenter().longitude) > circleInner.getRadius()){
                        //Player out of inner bounds, gets damage
                        llTextView.setTextColor(Color.parseColor("#0000ff"));
                    }
                    else{
                        //reset view changes
                        llTextView.setTextColor(Color.parseColor("#000000"));
                    }

                    //Dummy player hot cold TODO add assassin
                    //TextView mTextView = findViewById(R.id.DirectionTextView);
                    if(distance(location.getLatitude(), targetPlayer.getCurLoc().latitude, location.getLongitude(), targetPlayer.getCurLoc().longitude) > 50){
                        //player out of outer bounds, DQ
                        mTextView.setTextColor(Color.parseColor("#0000ff"));
                    }
                    else if(distance(location.getLatitude(), targetPlayer.getCurLoc().latitude, location.getLongitude(), targetPlayer.getCurLoc().longitude) < 49 &&
                            distance(location.getLatitude(), targetPlayer.getCurLoc().latitude, location.getLongitude(), targetPlayer.getCurLoc().longitude) > 25){
                        //Player out of inner bounds, gets damage
                        mTextView.setTextColor(Color.parseColor("#ffa500"));
                    }
                    else if(distance(location.getLatitude(), targetPlayer.getCurLoc().latitude, location.getLongitude(), targetPlayer.getCurLoc().longitude) < 24 &&
                            distance(location.getLatitude(), targetPlayer.getCurLoc().latitude, location.getLongitude(), targetPlayer.getCurLoc().longitude) > 5){
                        mTextView.setTextColor(Color.parseColor("#ff0000"));
                    }
                    else if(distance(location.getLatitude(), targetPlayer.getCurLoc().latitude, location.getLongitude(), targetPlayer.getCurLoc().longitude) <= 5 ||
                            distance(location.getLatitude(), asPlayer.getCurLoc().latitude, location.getLongitude(), asPlayer.getCurLoc().longitude) <= 5){
                        //TODO go to mini game intent and handle elimination

                        if(distance(location.getLatitude(), targetPlayer.getCurLoc().latitude, location.getLongitude(), targetPlayer.getCurLoc().longitude) <= 5){
                            AoD = 0;
                        }
                        else if(distance(location.getLatitude(), asPlayer.getCurLoc().latitude, location.getLongitude(), asPlayer.getCurLoc().longitude) <= 5){
                            AoD = 1;
                        }
                            intent = new Intent(MainGame.this, MiniGameActivity.class);

                            if(intentChange == 0) {
                                //Log.d(TAG, intentChange + " : Swithcing intent");
                                intentChange = 1;
                                startActivityForResult(intent, 1);
                            }
                    }
                }
            }
        };

        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        Handler h = new Handler();

        switch(requestCode){
            case 1:
                diff = data.getLongExtra("diff", -1);
                intentChange = data.getIntExtra("toRun", 0);
                Log.d(TAG, diff + " : diff time");
                curPlayer.reactTime = diff;
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(AoD==0){ //Atacking, elim other or refresh if lose
                            if(diff<curPlayer.getTarget().reactTime){
                                curGame.eliminatePlayer(curPlayer.getTarget());
                                Log.d(TAG, "Taking out target. Pr: " + curGame.getPlayersRemaining());
                                curPlayer.reactTime = 5000; //reset your reaction time 
                                //TODO won screen
                            }
                            else{
                                //TODO refresh
                            }
                        }
                        else if(AoD==1){ //Defending, get away if win eliminate if lose
                            if(diff<curPlayer.getAssassin().reactTime){//
                                //refresh
                            }
                            else{
                                //TODO lose screen
                                curGame.eliminatePlayer(curPlayer);
                            }
                        }
                    }
                }, 5000); //ensures the other time is gotten or default lose after 5 second.

                break;
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {

        }
    }


    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();
        sManager.registerListener(mySensorEventListener, sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        sManager.registerListener(mySensorEventListener, sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_NORMAL);
        mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, null);
        h.postDelayed( runnable = new Runnable() {
            public void run() {
                double shrink = 0.7;
                double shrinkInv = 1 - shrink;
                double tempOuterRad = circleInner.getRadius();
                double tempInnerRad = circleInner.getRadius() * shrink;

                double r = circleInner.getRadius() * shrinkInv;
                circleInner.setRadius(tempInnerRad);
                circleOuter.setRadius(tempOuterRad);
                circleOuter.setCenter(circleInner.getCenter());
                circleInner.setCenter(computeOffset(circleInner.getCenter(), r, azimuth));
                //Log.d(TAG, x + " " + y);

                h.postDelayed(runnable, delay);
            }
        }, delay);
    }

    private SensorEventListener mySensorEventListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_MAGNETIC_FIELD:
                    mags = event.values.clone();
                    break;
                case Sensor.TYPE_ACCELEROMETER:
                    accels = event.values.clone();
                    break;
            }

            if (mags != null && accels != null) {
                gravity = new float[9];
                magnetic = new float[9];
                SensorManager.getRotationMatrix(gravity, magnetic, accels, mags);
                float[] outGravity = new float[9];
                SensorManager.remapCoordinateSystem(gravity, SensorManager.AXIS_X,SensorManager.AXIS_Z, outGravity);
                SensorManager.getOrientation(outGravity, values);

                azimuth = values[0] * 57.2957795f;
                pitch =values[1] * 57.2957795f;
                roll = values[2] * 57.2957795f;
                mags = null;
                accels = null;
                //mTextView = findViewById(R.id.DirectionTextView);
                mTextView.setText("\tAzimuth:   " + azimuth);
            }
        }
    };

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener((GoogleMap.OnMyLocationButtonClickListener) this);
        mMap.setOnMyLocationClickListener((GoogleMap.OnMyLocationClickListener) this);
        enableMyLocation();

        curGame.setSeedLoc(new LatLng(29.663350, -82.378250));

        circleInner = mMap.addCircle(new CircleOptions()
                .center(curGame.getSeedLoc())
                .radius(700) // In meters
                .strokeWidth(10)
                .strokeColor(Color.BLACK));
        circleOuter = mMap.addCircle(new CircleOptions()
                .center(curGame.getSeedLoc())
                .radius(1000) // In meters
                .strokeWidth(10)
                .strokeColor(Color.BLUE));

        targetPlayer.setCurLoc(new LatLng(29.6633, -82.3782));
        asPlayer.setCurLoc(new LatLng(29.6633, -82.3782));
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        distance = Math.pow(distance, 2);

        return Math.sqrt(distance);
    }

    public static LatLng computeOffset(LatLng from, double distance, double heading) {
        Random random = new Random();
        double pn = Math.round(Math.random()) * 2 - 1;
        distance /= 6371009.0D;  //earth_radius = 6371009 # in meters
        heading = Math.toRadians(heading);
        double fromLat = Math.toRadians(from.latitude);
        double fromLng = Math.toRadians(from.longitude);
        double cosDistance = Math.cos(distance*random.nextDouble()*pn);// * random.nextDouble();
        double sinDistance = Math.sin(distance*random.nextDouble()*pn);// * random.nextDouble();
        double sinFromLat = Math.sin(fromLat);
        double cosFromLat = Math.cos(fromLat);
        double sinLat = cosDistance * sinFromLat + sinDistance * cosFromLat * Math.cos(heading);
        double dLng = Math.atan2(sinDistance * cosFromLat * Math.sin(heading), cosDistance - sinFromLat * sinLat);
        return new LatLng(Math.toDegrees(Math.asin(sinLat)), Math.toDegrees(fromLng + dLng));
    }
}

