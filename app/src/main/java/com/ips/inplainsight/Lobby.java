package com.ips.inplainsight;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import java.util.ArrayList;


public class Lobby extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
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

        /*private static final String TAG = "MainActivity";

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
        }*/

    }
}
