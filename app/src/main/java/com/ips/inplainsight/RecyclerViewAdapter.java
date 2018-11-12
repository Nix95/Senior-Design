package com.ips.inplainsight;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> gameNames;
    //private ArrayList<String> mImages = new ArrayList<>();
    //private Context mContext;

    public RecyclerViewAdapter(ArrayList<String> gameNames) {
        this.gameNames = gameNames;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        holder.gameName.setText(gameNames.get(position));

        /* onClickListener for message, probably dont need this
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + gameNames.get(position));

                Toast.makeText(mContext, gameNames.get(position), Toast.LENGTH_SHORT).show();
                }
        });*/
    }

    @Override
    public int getItemCount() {
        return gameNames.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView gameName;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            gameName = (TextView) itemView.findViewById(R.id.list_item_text);
            //parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }


}
