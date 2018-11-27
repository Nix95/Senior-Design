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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;


public class Lobby extends AppCompatActivity {

    private RecyclerView mGameList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    Intent currGame;
    PlayerClass player;

    private DatabaseReference mDatabase;
    private static final String TAG = "lobby";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        player = (PlayerClass)getIntent().getParcelableExtra("User");
        currGame = new Intent(this, MainGame.class);


        // The below lines are commented out. They are only needed to add Game objects to the firebase database
        //ArrayList<PlayerClass> players = new ArrayList<>();
        //Game game1 = new Game("1", new MyLatLng(29.663350, -82.378250), players); //construct initial game
//        Game game2 = new Game("2", new MyLatLng(29.663350, -82.378250), players); //construct initial game
//        Game game3 = new Game("3", new MyLatLng(29.663350, -82.378250), players); //construct initial game
//        //PlayerClass p = new PlayerClass("bob"); // construct dummy player
//        //game1.addPlayer(p); // add dummy player to game1
       //mDatabase = FirebaseDatabase.getInstance().getReference();
       //mDatabase.child("games").child("game1").setValue(game1);
//        mDatabase.child("games").child("game2").setValue(game2);
//        mDatabase.child("games").child("game3").setValue(game3);



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

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.d(TAG, "inside of onDataChange called.");

                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ArrayList<String> values = new ArrayList<String>(); //dataSnapshot.getValue();
                ArrayList<Game> gameList = new ArrayList<Game>();

                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    Log.d(TAG, "value being added"+String.valueOf(dsp.getKey()));
                    // add gameId to ArrayList
                    //Log.d(TAG, "ERROR: " + dsp.getValue(Game.class));
                    values.add(String.valueOf(dsp.child("gameId").getValue(String.class))); //add result into array list
                    gameList.add(dsp.getValue(Game.class));
                }

                mGameList.setAdapter(new RecyclerViewAdapter(values)); //display ArrayList in Recycler View

                Game gts = new Game();
                //gts = dataSnapshot.child("games/-LR3tyvnY6U3bcNB5byG").getValue(Game.class);
                //TODO user selected
               gts = gameList.get(0);
                Log.d(TAG, "game to pass "+gts.gameId);

                //Bundle extras = new Bundle();
                //extras.putParcelable("0", player);
                //extras.putParcelable("1", gts);
                //currGame.putExtras(extras);

                currGame.putExtra("currPlayer", player);
                currGame.putExtra("gts", gts);
                startActivity(currGame);
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

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }

    }

}

