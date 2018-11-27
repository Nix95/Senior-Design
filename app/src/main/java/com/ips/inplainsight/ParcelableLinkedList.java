package com.ips.inplainsight;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.LinkedList;

public class ParcelableLinkedList<PlayerClass extends Parcelable> extends LinkedList implements Parcelable {

    private LinkedList<PlayerClass> linkedList = new LinkedList<>();
    

    public static final Creator<ParcelableLinkedList> CREATOR = new Creator<ParcelableLinkedList>() {
        @Override
        public ParcelableLinkedList createFromParcel(Parcel in) {
            return new ParcelableLinkedList(in);
        }

        @Override
        public ParcelableLinkedList[] newArray(int size) {
            return new ParcelableLinkedList[size];
        }
    };

    public ParcelableLinkedList(Parcel in) {
        // Read size of list
        int size = in.readInt();
        // Read the list
        linkedList = new LinkedList<PlayerClass>();
        for (int i = 0; i < size; i++) {
            linkedList.add((PlayerClass) in.readParcelable(ParcelableLinkedList.class.getClassLoader()));
        }

    }

    public ParcelableLinkedList(LinkedList<PlayerClass> players) {
        this.linkedList = linkedList;
    }

    LinkedList<PlayerClass> getLinkedList() {
        return linkedList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        // Write size of the list
        parcel.writeInt(linkedList.size());
        // Write the list
        for (PlayerClass entry : linkedList) {
            parcel.writeParcelable(entry, flags);
        }
    }
}
