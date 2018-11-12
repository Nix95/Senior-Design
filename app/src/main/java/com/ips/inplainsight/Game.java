package com.ips.inplainsight;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.LinkedList;

public class Game implements Parcelable {
    public String gameId;
    private LinkedList<PlayerClass> players = new LinkedList<PlayerClass>();

    private MyLatLng seedLoc;

    public int durationSeconds;
    private int playersRemaining = 0;
    private static final String TAG = "GameClass";


    protected Game(Parcel in){
        gameId = in.readString();
        //players = in.readParcelable(LinkedList<PlayerClass>.class.getClassLoader());
        in.readTypedList(players, PlayerClass.CREATOR);
        seedLoc = in.readParcelable(MyLatLng.class.getClassLoader());
        playersRemaining = in.readInt();
        durationSeconds = in.readInt();
    }
    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int i) {
            return new Game[i];
        }
    };


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

    public Game(String gameId, MyLatLng seedLoc) {
        this.gameId = gameId;
        //this.players = players;
        this.seedLoc = seedLoc;
        //this.playersRemaining = playersRemaining;
    }

    public Game() {
    }


    public MyLatLng getSeedLoc() {
        return seedLoc;
    }

    public void setSeedLoc(MyLatLng seedLoc) {
        this.seedLoc = seedLoc;
    }

    public int getPlayersRemaining() {
        return playersRemaining;
    }

    public void setPlayersRemaining(int playersRemaining) {
        this.playersRemaining = playersRemaining;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel in, int i) {
        in.writeString(gameId);
        //in.writeParcelable((Parcelable) players, i);
        in.writeTypedList(players);
        in.writeParcelable((Parcelable) seedLoc, i);
        in.writeInt(playersRemaining);
        in.writeInt(durationSeconds);
    }
}

