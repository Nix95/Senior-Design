package com.ips.inplainsight;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedList;

public class Game  {
    public String gameId;
    private LinkedList<PlayerClass> players = new LinkedList<PlayerClass>();
    private LatLng seedLoc;
    public int durationSeconds;
    private int playersRemaining = players.size();

    public LinkedList<PlayerClass> getPlayers() {
        return players;
    }

    public void addPlayer(PlayerClass player) { //adding puts player at end of list
        if(playersRemaining==0){
            this.players.add(player);
        }
        else if(playersRemaining==1){
            player.setTarget(players.peekFirst());
            player.setAssassin(players.peekFirst());
            this.players.peekFirst().setTarget(player);
            this.players.peekFirst().setAssassin(player);
            this.players.add(player);
        }
        else {
            player.setTarget(players.peekFirst()); //target is first player "circular"
            player.setAssassin(players.peekLast()); //assassin was previous last
            this.players.peekLast().setTarget(player);
            this.players.peekFirst().setAssassin(player);
            this.players.add(player); //adds to end of list
        }
    }

    public void eliminatePlayer(PlayerClass player) {
        if(playersRemaining==2){
            //winner
        }
        else {
            if(this.players.getFirst()==player){ //first player
                this.players.get(1).setAssassin(this.players.peekLast()); //last targeting second
                this.players.peekLast().setTarget(this.players.get(1)); //last targeting second
                this.players.remove(player);
            }
            else if(this.players.getLast()==player){ //last player
                this.players.peekFirst().setAssassin(this.players.get(this.players.size()-2)); //second to last player asaasinating first
                this.players.get(this.players.size()-2).setTarget(this.players.peekFirst());//first targeting new last
                this.players.remove(player);
            }
            else{ //any middle player
                int i = this.players.indexOf(player);
                this.players.get(i-1).setTarget(this.players.get(i+1));
                this.players.get(i+1).setAssassin(this.players.get(i-1));
                this.players.remove(player);
            }
        }
        this.players.remove(player);
    }

    public LatLng getSeedLoc() {
        return seedLoc;
    }

    public void setSeedLoc(LatLng seedLoc) {
        this.seedLoc = seedLoc;
    }

    public int getPlayersRemaining() {
        return playersRemaining;
    }

    public void setPlayersRemaining(int playersRemaining) {
        this.playersRemaining = playersRemaining;
    }
}
