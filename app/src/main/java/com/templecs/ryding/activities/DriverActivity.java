package com.templecs.ryding.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.templecs.ryding.R;

public class DriverActivity extends AppCompatActivity {

    private EditText busid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        busid = (EditText) findViewById(R.id.busid);
        setupToolbar();
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

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Ryding Driver");
        setSupportActionBar(toolbar);
    }

    public void OpenView(View view){
        String busID = busid.getText().toString();
        if(busID.length() > 0){
            try {
                int tempID = Integer.parseInt(busID);
                Intent intent = new Intent(this, DriverView.class);
                intent.putExtra("busid",String.valueOf(tempID));
                startActivity(intent);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter Bus ID", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Please enter Bus ID", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}