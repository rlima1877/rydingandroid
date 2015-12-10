package com.templecs.ryding.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import com.templecs.ryding.model.Bus;
import com.templecs.ryding.R;
import com.templecs.ryding.adapters.BusListAdapter;

public class PassengerActivity extends AppCompatActivity {
    private BusListAdapter busListAdapter;
    public ArrayList<Bus> busList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger);
        setupToolbar();

        Bundle bundle = getIntent().getBundleExtra("BusList");
        if(bundle != null){
            busList = bundle.getParcelableArrayList("BusList");
            initializeViews(busList);
        }

        if(savedInstanceState != null) {
            busList = savedInstanceState.getParcelableArrayList("bus_data");
            initializeViews(busList);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Ryding Passenger");
        setSupportActionBar(toolbar);
    }

    private void initializeViews(List<Bus> list){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.busList);
        busListAdapter = new BusListAdapter(this, list);
        recyclerView.setAdapter(busListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        bundle.putParcelableArrayList("bus_data", busList);
        super.onSaveInstanceState(bundle);
    }

    @Override
    protected void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        busList = bundle.getParcelableArrayList("bus_data");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu
        //getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}
