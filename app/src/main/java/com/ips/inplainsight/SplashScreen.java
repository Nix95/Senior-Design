package com.ips.inplainsight;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {

            // Using handler with postDelayed called runnable run method

            @Override
            public void run() {
                // start splash activity
                Intent i = new Intent(SplashScreen.this, Login.class);
                startActivity(i);

                // close splash activity
                finish();

            }

        }, 1*1000); // wait for x seconds


    }
}
