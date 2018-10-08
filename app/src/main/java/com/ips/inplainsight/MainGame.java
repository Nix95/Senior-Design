package com.ips.inplainsight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationListener;
import android.location.LocationManager;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainGame extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback, com.google.android.gms.location.LocationListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;

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

    private LocationCallback mLocationCallback;

    private FusedLocationProviderClient mFusedLocationClient;
    //GoogleApiClient gac;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    Handler h = new Handler();
    int delay = 10000; //milliseconds
    Runnable runnable;


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

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult){
                if(locationResult == null){
                    return;
                }
                for(Location location : locationResult.getLocations()){
                    //Update UI
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();
                    TextView llTextView = findViewById(R.id.LatLongTextView);
                    llTextView.setText("   lat:   " + latitude + "\n   long:   " + longitude);

                    //in bounds

                    double latdif = location.getLatitude() - circleInner.getCenter().latitude;
                    double longdif =  location.getLongitude() - circleInner.getCenter().longitude;
                    double latdifout =  location.getLatitude() - circleOuter.getCenter().latitude;
                    double longdifout =  location.getLongitude() - circleOuter.getCenter().longitude;
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
                }
            }
        };

        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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
                double shrink = 0.85;
                double Rad = 6378137;
                double shrinkInv = 1 - shrink;
                double tempOuterRad = circleInner.getRadius();
                double tempInnerRad = circleInner.getRadius() * shrink;

                Random random = new Random();
                double a = circleInner.getCenter().longitude;
                double b = circleInner.getCenter().latitude;
                double r = circleInner.getRadius() * shrink;
                double rlat = (r/Rad);
                double rlong = (r/(Rad*Math.cos(Math.PI * b/180)));

                double xMin = a - rlong;
                double xMax = a + rlong;
                double xRange = xMax - xMin;
                double x = xMin + random.nextDouble() * xRange;

                double yDelta = Math.sqrt(Math.pow(rlat,  2) - Math.pow((x - a), 2));
                double yMax = b + rlat;
                double yMin = b - rlat;
                double yRange = yMax - yMin;
                double y = yMin + random.nextDouble() * yRange;

//                double    y0 = b
//                        , x0 = a
//                        , u = Math.random()
//                        , v = Math.random()
//                        , w = r * Math.sqrt(u)
//                        , t = 2 * Math.PI * v
//                        , x = w * Math.cos(t)
//                        , y1 = w * Math.sin(t)
//                        , x1 = x / Math.cos(y0);

//                double newY = y0 + y1;
//                double newX = x0 + x1;

                circleInner.setRadius(tempInnerRad);
                circleOuter.setRadius(tempOuterRad);
                circleOuter.setCenter(circleInner.getCenter());
                LatLng newInner = new LatLng(x,y);
                circleInner.setCenter(newInner);

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
                TextView mTextView = findViewById(R.id.DirectionTextView);
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
        //TODO: Math and bounbs check
        circleInner = mMap.addCircle(new CircleOptions()
                .center(new LatLng(29.663350, -82.378250))
                .radius(700) // In meters
                .strokeWidth(10)
                .strokeColor(Color.BLACK));
        circleOuter = mMap.addCircle(new CircleOptions()
                .center(new LatLng(29.663350, -82.378250))
                .radius(1000) // In meters
                .strokeWidth(10)
                .strokeColor(Color.BLUE));
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
}

