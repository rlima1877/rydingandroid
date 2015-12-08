package com.templecs.ryding.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;
import com.templecs.ryding.utilities.UpdateBusLocationService;

/**
 * Created by Narith on 11/27/2015.
 */
public class LocationUpdate extends Service {

    private String id;
    private double latitude;
    private double longitude;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private String locationProvider;
    private LocalBroadcastManager broadcaster;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        broadcaster = LocalBroadcastManager.getInstance(this);
        Bundle bundle = intent.getExtras();
        if(bundle == null){
            Toast.makeText(this, "Bus ID or Direction Error", Toast.LENGTH_SHORT).show();
            return START_FLAG_RETRY;
        }
        else{
            id = bundle.getString("BusID");
            //locationProvider = LocationManager.GPS_PROVIDER;
            locationProvider = LocationManager.NETWORK_PROVIDER;
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = getLocationListener();
            locationManager.requestLocationUpdates(locationProvider, 10000, 0, locationListener);
            return START_NOT_STICKY;
        }
    }

    public void sendResult(double lat, double lon, String status) {
        Intent intent = new Intent("LocationService");
        intent.putExtra("Lat", lat);
        intent.putExtra("Lon", lon);
        intent.putExtra("Status", status);
        broadcaster.sendBroadcast(intent);
    }

    public LocationListener getLocationListener() {
        return new LocationListener() {
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                sendResult(latitude, longitude, null);
                String url = "http://templecs.com/bus/setbuslocation?id=" + id + "&lat=" + latitude + "&lon=" + longitude;

                UpdateBusLocationService updateBusLocationService = new UpdateBusLocationService(getApplicationContext(), url);
                Thread threat = new Thread(updateBusLocationService);
                threat.start();
                try{
                    threat.join();
                }catch(InterruptedException ie){
                    ie.printStackTrace();
                }
                if(updateBusLocationService.getResult() == "false"){
                    sendResult(0, 0, "false");
                }
                Toast.makeText(getApplicationContext(), updateBusLocationService.getResult(), Toast.LENGTH_SHORT).show();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(locationListener);
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}
