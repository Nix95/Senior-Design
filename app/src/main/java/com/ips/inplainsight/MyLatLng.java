package com.ips.inplainsight;


import android.os.Parcel;
import android.os.Parcelable;

public class MyLatLng implements Parcelable{
    public double latitude;
    public double longitude;

    public MyLatLng(){ } // empty constructor

    protected MyLatLng(Parcel in){
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<MyLatLng> CREATOR = new Creator<MyLatLng>() {
        @Override
        public MyLatLng createFromParcel(Parcel in) {
            return new MyLatLng(in);
        }

        @Override
        public MyLatLng[] newArray(int i) {
            return new MyLatLng[i];
        }
    };

    public  MyLatLng(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel in, int i) {
        in.writeDouble(latitude);
        in.writeDouble(longitude);
    }
}
