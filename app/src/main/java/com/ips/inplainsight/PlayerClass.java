package com.ips.inplainsight;
import android.location.Location;
import android.os.Bundle;
import android.Manifest;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class PlayerClass {
    public static String userName;
    public long reactTime;
    private PlayerClass target;
    private PlayerClass assassin;
    private LatLng curLoc;
    private boolean inInnerBounds;
    private boolean inOuterBounds;
    //Power-Ups

    public PlayerClass() {

    }

    public PlayerClass(String userName) {
        this.userName = userName;
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
