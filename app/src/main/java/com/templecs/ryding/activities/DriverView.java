package com.templecs.ryding.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.templecs.ryding.R;
import com.templecs.ryding.services.LocationUpdate;

public class DriverView extends AppCompatActivity {

    private String busnumber;
    private String direction;
    private String id;
    private double latitude;
    private double longitude;

    private TextView busIDText;
    private TextView latitudeText;
    private TextView longitudeText;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driverview);

        id = getIntent().getExtras().getString("busid");

        busIDText = (TextView) findViewById(R.id.busid);
        latitudeText = (TextView) findViewById(R.id.latitude);
        longitudeText = (TextView) findViewById(R.id.longitude);
        busIDText.setText("Bus ID: " + id);

        startService(id);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String status = intent.getStringExtra("Status");
                if(status == "false"){
                    Toast.makeText(getApplicationContext(), "Cannot find bus with ID " + id, Toast.LENGTH_SHORT).show();
                    finish();
                }
                double lat = intent.getDoubleExtra("Lat", 0);
                double lon = intent.getDoubleExtra("Lon", 0);
                latitudeText.setText("Latitude: " + String.valueOf(lat));
                longitudeText.setText("Longitude: " + String.valueOf(lon));
            }
        };
    }

    public void StopService(View view){
        stopService();
    }

    public void startService(String busID) {
        Intent intent = new Intent(getBaseContext(), LocationUpdate.class);
        intent.putExtra("BusID", busID);
        startService(intent);
    }

    public void stopService() {
        stopService(new Intent(getBaseContext(), LocationUpdate.class));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        stopService();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver), new IntentFilter("LocationService")
        );
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onStop();
    }
}