package com.templecs.ryding.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;
import com.templecs.ryding.R;
import com.templecs.ryding.model.Bus;


public class  MainActivity extends AppCompatActivity {

    private Button driverButton;
    private Button passengerButton;
    public ArrayList<Bus> busList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getBundleExtra("BusList");
        if(bundle != null){
            busList = bundle.getParcelableArrayList("BusList");
        }

        if(savedInstanceState != null) {
            busList = savedInstanceState.getParcelableArrayList("bus_data");
        }

        driverButton = (Button) findViewById(R.id.buttonDriver);
        driverButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), LoginActivity.class));
            }
        });

        passengerButton = (Button) findViewById(R.id.buttonPassenger);
        passengerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PassengerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("BusList", busList);
                intent.putExtra("BusList", bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


}
