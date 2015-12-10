package com.templecs.ryding.activities;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import com.templecs.ryding.R;
import com.templecs.ryding.model.Bus;
import com.templecs.ryding.model.BusStop;
import com.templecs.ryding.utilities.BusLocationService;
import com.templecs.ryding.utilities.BusStopService;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int currentBusID;
    private int currentBusNumber;
    private Bus currentBus;
    private List<BusStop> currentBusStopsList;
    private Marker marker;
    private Polyline polyline;
    private List<LatLng> points;
    //flag for which shuttleService to draw.
    private String shuttleService;
    private String urlCurrentLoc;
    private String urlCurrentBusStopsInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        urlCurrentLoc = getResources().getString(R.string.GetBusLocation);
        urlCurrentBusStopsInfo = getResources().getString(R.string.GetBusRoute);

        //grab which route to draw for a particular case
        shuttleService = "owlloop";

        //grab current bus id and number from previous screen..
        Bundle bundle = getIntent().getExtras();
        currentBusID = bundle.getInt("current_bus_id");
        currentBusNumber = bundle.getInt("current_bus_number");

        //set urls for pull functions based on bus id
        urlCurrentLoc += currentBusID;
        urlCurrentBusStopsInfo += currentBusID;

        //grabs most up to date location of bus to initialize map.
        pullCurrentLocation();

        //draws toolbar
        //setupToolbar();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Route Map");
        setSupportActionBar(toolbar);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);

        LatLng geoLoc = new LatLng(currentBus.getGeoLat(), currentBus.getGeoLong());
        marker =  mMap.addMarker(new MarkerOptions()
                .position(geoLoc).title("Bus : " + currentBusNumber)
                .snippet("ETA to closest stop : (TO BE IMPLEMENTED)")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_icon_32)));
        marker.showInfoWindow();

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));

        //draw map based on a particular shuttleService (scalable)
        if(points == null) {

            switch(shuttleService){
                case "owlloop":
                    drawOwlLoop();
                    break;
                case "mha":
                    break;
                case "whatever":
                default:
                    Log.d("BUG", "shuttleService variable is null or not valid");
                    break;
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu
        getMenuInflater().inflate(R.menu.maps_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //grab id to work with buttons
        int id = item.getItemId();

        //when clicking refresh button this will trigger..
        if(id == R.id.refreshIcon){

            //grabs newest location
            pullCurrentLocation();

            LatLng newLoc = new LatLng(currentBus.getGeoLat(), currentBus.getGeoLong());

            //set new position of bus and move camera
            marker.setPosition(newLoc);
            marker.showInfoWindow();
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));

        }

        return super.onOptionsItemSelected(item);
    }


    //draws route for owlLoop shuttle service
    public void drawOwlLoop(){

        pullBusRouteStops();

        points = new ArrayList<LatLng>();

        for(int i = 0; i < currentBusStopsList.size(); i++){
            points.add(new LatLng(currentBusStopsList.get(i).getGeoLat(), currentBusStopsList.get(i).getGeoLong()));

            mMap.addMarker(new MarkerOptions()
                    .position(points.get(i)).title(currentBusStopsList.get(i).getName())
                    .snippet("Random info can be added here..."));
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_icon_32)));
            marker.showInfoWindow();
        }
        drawRoute();
    }


    public void drawRoute(){
        PolylineOptions polylineOptions;

        if(polyline == null){
            polylineOptions = new PolylineOptions();

            for(int i = 0; i < points.size() ; i++ ){
                polylineOptions.add(points.get(i));
            }
            // Connecting last point to first point
            polylineOptions.add(points.get(0));

            polylineOptions.color(Color.BLUE);
            polylineOptions.width(4);
            polyline = mMap.addPolyline(polylineOptions);
        }
    }

    public void pullCurrentLocation(){
        BusLocationService readBusLocation = new BusLocationService(this, urlCurrentLoc);
        Thread threat = new Thread(readBusLocation);
        threat.start();
        try{
            threat.join();
        }catch(InterruptedException ie){
            ie.printStackTrace();
        }
        currentBus = new Bus();
        currentBus = readBusLocation.getBus();

    }
    public void pullBusRouteStops(){
        BusStopService readBusStopJSON = new BusStopService(this, urlCurrentBusStopsInfo);
        Thread threat = new Thread(readBusStopJSON);
        threat.start();
        try{
            threat.join();
        }catch(InterruptedException ie){
            ie.printStackTrace();
        }
        currentBusStopsList = new ArrayList<BusStop>();
        currentBusStopsList = readBusStopJSON.getBusStopList();
    }
}