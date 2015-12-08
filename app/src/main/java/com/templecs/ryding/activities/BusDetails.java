package com.templecs.ryding.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import com.templecs.ryding.R;
import com.templecs.ryding.adapters.BusDetailsAdapter;
import com.templecs.ryding.model.Bus;
import com.templecs.ryding.model.BusStop;
import com.templecs.ryding.utilities.TravelTimeService;

public class BusDetails extends AppCompatActivity {

    private BusDetailsAdapter busDetailsAdapter;
    private Bus bus;
    private ArrayList<BusStop> busGeos;
    private String url = "http://templecs.com/bus/gettraveltime?id=";
    private LinearLayout busStopContent;
    private LinearLayout loadingIcon;
    private boolean ready;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_details);
        setupToolbar();

        ready = false;
        busStopContent = (LinearLayout)findViewById(R.id.busStopContent);
        busStopContent.setVisibility(View.GONE);
        loadingIcon = (LinearLayout)findViewById(R.id.loadingIcon);
        loadingIcon.setVisibility(View.VISIBLE);

        if(getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            bus = (Bus)bundle.get("Bus");
            url += bus.getBusID();
            GetBusInformation task = new GetBusInformation();
            task.execute(this);
        }

        //user pressed back button, retrieving data from onSavedInstanceState
        if(savedInstanceState != null) {
            Log.d("DEBUGBUSDETAILS", "OnCreate being CALLED second if statem.... ");
            busGeos = savedInstanceState.getParcelableArrayList("bus_data");
            initializeViews(busGeos);
        }
    }

    private class GetBusInformation extends AsyncTask<Activity, Void, List<BusStop>> {
        @Override
        protected List<BusStop> doInBackground(Activity...activities) {
            TravelTimeService readBusStopJSON = new TravelTimeService(activities[0], url);
            Thread threat = new Thread(readBusStopJSON);
            threat.start();
            try{
                threat.join();
            }catch(InterruptedException ie){
                ie.printStackTrace();
            }
            busGeos = readBusStopJSON.getTravelTime();
            return busGeos;
        }

        @Override
        protected void onPostExecute(List<BusStop> busStopList) {
            initializeViews(busStopList);
            loadingIcon.setVisibility(View.GONE);
            busStopContent.setVisibility(View.VISIBLE);
            ready = true;
        }
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Bus Details");
        setSupportActionBar(toolbar);
    }

    private void initializeViews(List<BusStop> list){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listBusDetails);
        busDetailsAdapter = new BusDetailsAdapter(this, list);
        recyclerView.setAdapter(busDetailsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu
        getMenuInflater().inflate(R.menu.detail_screen_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //when user click on gps icon to get real time location of the bus
        if(id == R.id.locationIcon){
            if(ready){
                if(busGeos.size() != 0){
                    //Bellow code sends data to MapActivity.class
                    Intent intent = new Intent(this.getApplicationContext(), MapsActivity.class);
                    intent.putExtra("current_bus_id", busGeos.get(0).getBusID());
                    intent.putExtra("current_bus_number", busGeos.get(0).getBusNumber());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(this, "Bus route is not available to display", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this, "Still loading, please wait...", Toast.LENGTH_SHORT).show();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        Log.d("DEBUGBUSDETAILS", "onSaveInstanceStateCalled.... " + busGeos.get(0).getBusNumber());
        bundle.putParcelableArrayList("bus_data", (ArrayList<BusStop>) busGeos);
        bundle.putChar("random",'c');
        super.onSaveInstanceState(bundle);
    }

    @Override
    protected void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        Log.d("DEBUGBUSDETAILS", "ONRESTORE CALLED.... ");
        busGeos = bundle.getParcelableArrayList("bus_data");
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
