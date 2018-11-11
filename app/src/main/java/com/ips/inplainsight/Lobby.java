package com.ips.inplainsight;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


public class Lobby extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        /*
        // The below lines are commented out. They are only needed to add Game objects to the firebase database
        Game game1 = new Game("1", new LatLng(29.663350, -82.378250)); //construct initial game
        Game game2 = new Game("2", new LatLng(29.663350, -82.378250)); //construct initial game
        Game game3 = new Game("3", new LatLng(29.663350, -82.378250)); //construct initial game
        //PlayerClass p = new PlayerClass("bob");
        //newGame.addPlayer(p);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("game1").setValue(game1);
        mDatabase.child("game2").setValue(game2);
        mDatabase.child("game3").setValue(game3);
        */


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        //mAdapter = new RecyclerViewAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);


        /*
        private static final String TAG = "MainActivity";

        //vars
        private ArrayList<String> mNames = new ArrayList<>();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_lobby);
            Log.d(TAG, "onCreate: started.");

        }

        private void initRecyclerView(){
            Log.d(TAG, "initRecyclerView: init recyclerview.");
            RecyclerView recyclerView = findViewById(R.id.recyclerv_view);
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mNames, mImageUrls);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        */

    }
}
