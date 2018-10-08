package com.ips.inplainsight;
import android.location.Location;
import android.os.Bundle;
import android.Manifest;

public class PlayerClass {
    public static String userName;
    private PlayerClass target;
    private PlayerClass assassin;
    private Location curLoc;
    private boolean inInnerBounds;
    private boolean inOuterBounds;
    //Power-Ups

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
    public Location getCurLoc() {
        return curLoc;
    }

    public void setCurLoc(Location curLoc) {
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
