package com.ips.inplainsight;
import android.location.Location;
import android.os.Bundle;
import android.Manifest;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class PlayerClass implements Parcelable {
    public static String uID = "";
    public static String userName = "";
    public long reactTime =  5000;
    private boolean canAD = true;
    private PlayerClass target;
    private PlayerClass assassin;
    private MyLatLng curLoc; //TODO update "my location" in game
    private boolean inInnerBounds = true;
    private boolean inOuterBounds = true;
    //Power-Ups

    public PlayerClass() {

    }
//    public PlayerClass(String userName) {
//        this.userName = userName;
//    }
    public PlayerClass(String uID){
        this.uID = uID;
    }

    private static final String TAG = "PlayerClass";

    protected PlayerClass(Parcel in) {
        uID = in.readString();
        userName = in.readString();
        reactTime = in.readLong();
        canAD = in.readByte() != 0;
        target = in.readParcelable(PlayerClass.class.getClassLoader());
        assassin = in.readParcelable(PlayerClass.class.getClassLoader());
        curLoc = in.readParcelable(MyLatLng.class.getClassLoader());
        inInnerBounds = in.readByte() != 0;
        inOuterBounds = in.readByte() != 0;
    }

    public static final Creator<PlayerClass> CREATOR = new Creator<PlayerClass>() {
        @Override
        public PlayerClass createFromParcel(Parcel in) {
            return new PlayerClass(in);
        }

        @Override
        public PlayerClass[] newArray(int size) {
            return new PlayerClass[size];
        }
    };

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
    public MyLatLng getCurLoc() {
        return curLoc;
    }

    public void setCurLoc(MyLatLng curLoc) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uID);
        dest.writeString(userName);
        dest.writeLong(reactTime);
        dest.writeByte((byte)(canAD?1:0));
        dest.writeParcelable(target, flags);
        dest.writeParcelable(assassin, flags);
        dest.writeParcelable(curLoc, flags);
        dest.writeByte((byte)(inInnerBounds?1:0));
        dest.writeByte((byte)(inOuterBounds?1:0));

    }
}
