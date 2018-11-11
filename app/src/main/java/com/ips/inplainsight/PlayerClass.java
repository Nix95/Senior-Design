package com.ips.inplainsight;
import android.location.Location;
import android.os.Bundle;
import android.Manifest;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class PlayerClass {
    public static String uID;
    public boolean eog = false;
    public static String userName;
    public long reactTime =  5000;
    private boolean canAD = true;
    private PlayerClass target;
    private PlayerClass assassin;
    private LatLng curLoc; //TODO update "my location" in game
    private boolean inInnerBounds;
    private boolean inOuterBounds;
    //Power-Ups

    public PlayerClass() {

    }

    public PlayerClass(String userName) {
        this.userName = userName;
    }

    private static final String TAG = "PlayerClass";

    public boolean getCanAD() {
        return canAD;
    }

    public void setCanAD_False() {
        canAD = false;
        Log.d(TAG, canAD + " : set to false");
        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                canAD = true;
                Log.d(TAG, canAD + " : time up state change");
            }
            }, 120000);
    }

    public PlayerClass getTarget() {
        return target;
    }

    public void setTarget(PlayerClass target) {
        this.target = target;
    }
    public PlayerClass getAssassin() {
        return assassin;
    }

    public void setAssassin(PlayerClass assassin) {
        this.assassin = assassin;
    }
    public LatLng getCurLoc() {
        return curLoc;
    }

    public void setCurLoc(LatLng curLoc) {
        this.curLoc = curLoc;
    }

    public boolean isInInnerBounds() {
        return inInnerBounds;
    }

    public void setInInnerBounds(boolean inInnerBounds) {
        this.inInnerBounds = inInnerBounds;
    }

    public boolean isInOuterBounds() {
        return inOuterBounds;
    }

    public void setInOuterBounds(boolean inOuterBounds) {
        this.inOuterBounds = inOuterBounds;
    }
}
