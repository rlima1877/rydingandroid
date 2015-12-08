package com.templecs.ryding.model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rafaellima on 10/21/15.
 */
public class Bus implements Parcelable {

    private int busID;
    private int busNumber;
    private String busRoute;
    private double geoLat;
    private double geoLong;

    public double getGeoLong() {
        return geoLong;
    }

    public void setGeoLong(double geoLong) {
        this.geoLong = geoLong;
    }

    public double getGeoLat() {
        return geoLat;
    }

    public void setGeoLat(double geoLat) {
        this.geoLat = geoLat;
    }

    public Bus(){
        //empty
    }

    public Bus(int busID, int busNumber, String busRoute){

        this.busID = busID;
        this.busNumber = busNumber;
        this.busRoute = busRoute;
    }

    public Bus(Parcel in) {
        this.busID = in.readInt();
        this.busNumber = in.readInt();
        this.busRoute = in.readString();
        this.geoLat = in.readDouble();
        this.geoLong = in.readDouble();
    }

    public int getBusID() {
        return busID;
    }

    public void setBusID(int busID) {
        this.busID = busID;
    }

    public int getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(int busNumber) {
        this.busNumber = busNumber;
    }

    public String getBusRoute() {
        return busRoute;
    }

    public void setBusRoute(String busRoute) {
        this.busRoute = busRoute;
    }


    @Override
    public String toString() {
        return "Bus{" +
                "busID=" + busID +
                ", busNumber=" + busNumber +
                ", busRoute='" + busRoute + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {


        dest.writeInt(busID);
        dest.writeInt(busNumber);
        dest.writeString(busRoute);
        dest.writeDouble(geoLat);
        dest.writeDouble(geoLong);

    }

    public static final Parcelable.Creator<Bus> CREATOR = new Parcelable.Creator<Bus>()
    {
        public Bus createFromParcel(Parcel in)
        {
            return new Bus(in);
        }
        public Bus[] newArray(int size)
        {
            return new Bus[size];
        }
    };
}