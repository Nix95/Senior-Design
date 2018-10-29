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
    private int playersRemaining = 0;

    public LinkedList<PlayerClass> getPlayers() {
        return players;
    }

    public void addPlayer(PlayerClass player) { //adding puts player at end of list
        if(playersRemaining==0){
            players.add(player);
        }
        else if(playersRemaining==1){
            player.setTarget(players.peekFirst());
            player.setAssassin(players.peekFirst());
            players.peekFirst().setTarget(player);
            players.peekFirst().setAssassin(player);
            players.add(player);
        }
        else {
            player.setTarget(players.peekFirst()); //target is first player "circular"
            player.setAssassin(players.peekLast()); //assassin was previous last
            players.peekLast().setTarget(player);
            players.peekFirst().setAssassin(player);
            players.add(player); //adds to end of list
        }
        playersRemaining++;
    }

    public void eliminatePlayer(PlayerClass player) { //TODO have a "you lost" screen to splash before return to lobby.
        if(playersRemaining==2){
            //winner
            //TODO pick winner, tell loser they lost and kick from game.
            players.remove(player);
        }
        else {
            if(players.getFirst()==player){ //first player
                players.get(1).setAssassin(players.peekLast()); //last targeting second
                players.peekLast().setTarget(players.get(1)); //last targeting second
                players.remove(player);
            }
            else if(players.getLast()==player){ //last player
                players.peekFirst().setAssassin(players.get(players.size()-2)); //second to last player asaasinating first
                players.get(players.size()-2).setTarget(players.peekFirst());//first targeting new last
                players.remove(player);
            }
            else{ //any middle player
                int i = players.indexOf(player);
                players.get(i-1).setTarget(players.get(i+1));
                players.get(i+1).setAssassin(players.get(i-1));
                players.remove(player);
            }
        }
        playersRemaining--;
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
