package com.ips.inplainsight;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.LinkedList;

public class Game implements Parcelable {
    public String gameId;
    //private LinkedList<PlayerClass> players = new LinkedList<PlayerClass>();
    //private ParcelableLinkedList<PlayerClass> pplayers = new ParcelableLinkedList<PlayerClass>(players);// = new ParcelableLinkedList<>();// = new ParcelableLinkedList<PlayerClass>();
    private ArrayList<PlayerClass> players = new ArrayList<>();
    private MyLatLng seedLoc;

    public int durationSeconds;
    private int playersRemaining = 0;
    private static final String TAG = "GameClass";


    protected Game(Parcel in){
        gameId = in.readString();
        in.readTypedList(players,PlayerClass.CREATOR);
        //players = in.readParcelable(LinkedList<PlayerClass>.class.getClassLoader());
        //copyData = ((ParcelableLinkedList<data>)xy.getParcelable("list")).getLinkedList();
       //players = ((ParcelableLinkedList<PlayerClass>)in.readParcelable(ParcelableLinkedList.class.getClassLoader())).getLinkedList();
        //in.readTypedList(pplayers, PlayerClass.CREATOR);
        //players = pplayers.getLinkedList();
        //pplayers = in.readTypedList(pplayers, PlayerClass.CREATOR);
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


    public ArrayList<PlayerClass> getPlayers() {
        return players;
    }

    public void addPlayer(PlayerClass player) { //adding puts player at end of list
        //players = pplayers.getLinkedList();
        if(playersRemaining==0){
            players.add(player);
        }
        else if(playersRemaining==1){
            player.setTarget(players.get(0));
            player.setAssassin(players.get(0));
            players.get(0).setTarget(player);
            players.get(0).setAssassin(player);
            players.add(player);
        }
        else {
            player.setTarget(players.get(0)); //target is first player "circular"
            player.setAssassin(players.get(players.size()-1)); //assassin was previous last
            players.get(players.size()-1).setTarget(player);
            players.get(0).setAssassin(player);
            players.add(player); //adds to end of list
        }
        playersRemaining++;
        //pplayers = new ParcelableLinkedList<PlayerClass>(players);
    }

    public void eliminatePlayer(PlayerClass player) { //TODO have a "you lost" screen to splash before return to lobby.
        //players = pplayers.getLinkedList();
        if(playersRemaining==2){
            //winner
            //TODO pick winner, tell loser they lost and kick from game.
            players.remove(player);
        }
        else {
            if(players.get(0)==player){ //first player
                players.get(1).setAssassin(players.get(players.size()-1)); //last targeting second
                players.get(players.size()-1).setTarget(players.get(1)); //last targeting second
                players.remove(player);
            }

            else if(players.get(players.size()-1)==player){ //last player
                players.get(0).setAssassin(players.get(players.size()-2)); //second to last player asaasinating first
                players.get(players.size()-2).setTarget(players.get(0));//first targeting new last
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
        //pplayers = new ParcelableLinkedList<PlayerClass>(players);
    }

    public Game(String gameId, MyLatLng seedLoc, ArrayList<PlayerClass> players) {
        this.gameId = gameId;
        //this.players = players;
        this.seedLoc = seedLoc;
        this.players = players;
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
        in.writeTypedList(players);
        //in.writeParcelable((Parcelable) players, i);
        //xy.putParcelable("list", new ParcelableLinkedList<data>(storedData));
        //pplayers = new ParcelableLinkedList<PlayerClass>(players);
        //in.writeParcelable(new ParcelableLinkedList<PlayerClass>(players),i);
        //in.writeTypedList(pplayers);
        in.writeParcelable((Parcelable) seedLoc, i);
        in.writeInt(playersRemaining);
        in.writeInt(durationSeconds);
    }
}

