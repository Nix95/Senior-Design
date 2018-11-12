package com.ips.inplainsight;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


public class Lobby extends AppCompatActivity {

    private RecyclerView mGameList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    Intent currGame;

    private DatabaseReference mDatabase;
    private static final String TAG = "lobby";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        PlayerClass player = (PlayerClass)getIntent().getParcelableExtra("User");
        currGame = new Intent(this, MainGame.class);

        /*
        // The below lines are commented out. They are only needed to add Game objects to the firebase database
        Game game1 = new Game("1", new LatLng(29.663350, -82.378250)); //construct initial game
        Game game2 = new Game("2", new LatLng(29.663350, -82.378250)); //construct initial game
        Game game3 = new Game("3", new LatLng(29.663350, -82.378250)); //construct initial game
        //PlayerClass p = new PlayerClass("bob");
        //game1.addPlayer(p);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("games").push().setValue(game1);
        mDatabase.child("games").push().setValue(game2);
        mDatabase.push().child("games").setValue(game3);
        */

        mGameList = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mGameList.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mGameList.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        //mAdapter = new RecyclerViewAdapter(myDataset);
        mGameList.setAdapter(mAdapter);

        new GetDataFromFirebase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        // Read from the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("games");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.d(TAG, "inside of onDataChange called.");

                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ArrayList<String> values = new ArrayList<String>(); //dataSnapshot.getValue();

                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    Log.d(TAG, "value being added"+String.valueOf(dsp.getKey()));
                    // add gameId to ArrayList
                    values.add(String.valueOf(dsp.child("gameId").getValue(String.class))); //add result into array list
                }

                mGameList.setAdapter(new RecyclerViewAdapter(values)); //display ArrayList in Recycler View
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                System.out.println("Failed to read value." + error.toException());
            }
        });

    }

    private class GetDataFromFirebase extends AsyncTask<Void,Void,Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return false;
        }

        */
        currGame.putExtra("currPlayer", player);
        //TODO PASS GAME
        startActivity(currGame);


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }

    }

}

