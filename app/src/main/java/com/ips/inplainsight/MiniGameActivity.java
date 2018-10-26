package com.ips.inplainsight;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;

import java.util.Random;

public class MiniGameActivity extends MainGame {
    long init;
    long react;
    Handler handler;
    View v;
    Intent i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mini_game);
        final Button qd = (Button) findViewById(R.id.button2);
        long d = (long)(Math.random()*6000) + 4;
        v = findViewById(R.id.button2);
        i = new Intent(MiniGameActivity.this, MainGame.class);

        intentChange = 1;
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                init = System.currentTimeMillis();
                v.setBackgroundColor(Color.GREEN);

                qd.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // Code here executes on main thread after user presses button
                        react = System.currentTimeMillis();
                        diff = react - init;
                        i.putExtra("diff", diff);
                        i.putExtra("toRun", 1);
                        setResult(Activity.RESULT_OK, i);
                        //startActivity(i);
                        finish();
                    }
                });
            }
        }, d); // <-- the "1000" is the delay time in miliseconds.
    }
}
