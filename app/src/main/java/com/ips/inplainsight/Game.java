package com.ips.inplainsight;

import android.location.Location;

public class Game {
    public String gameId;
    private PlayerClass[] players = new PlayerClass[12];
    private Location seedLoc;
    public int durationSeconds;
    private int playersRemaining;

    public PlayerClass[] getPlayers() {
        return players;
    }

    public void setPlayers(PlayerClass[] players) {
        this.players = players;
    }

    public Location getSeedLoc() {
        return seedLoc;
    }

    public void setSeedLoc(Location seedLoc) {
        this.seedLoc = seedLoc;
    }

    public int getPlayersRemaining() {
        return playersRemaining;
    }

    public void setPlayersRemaining(int playersRemaining) {
        this.playersRemaining = playersRemaining;
    }
}
