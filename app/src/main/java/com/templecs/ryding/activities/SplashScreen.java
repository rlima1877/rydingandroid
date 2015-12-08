package com.templecs.ryding.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.templecs.ryding.R;
import com.templecs.ryding.model.Bus;
import com.templecs.ryding.utilities.ReadJSON;

public class SplashScreen extends AppCompatActivity {

    public static final String url = "http://templecs.com/bus/getallbuses";
    private Thread timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        GetBusInformation task = new GetBusInformation();
        task.execute(this);

        timer = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        timer.start();
    }

    private class GetBusInformation extends AsyncTask<Activity, Void, ArrayList<Bus>> {
        @Override
        protected ArrayList<Bus> doInBackground(Activity...activities) {
            try{
                ArrayList<Bus> busList = loadJSONFromNetwork(url);
                return busList;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return null;
        }

        //Loading JSON from inputstream then store in arraylist
        private ArrayList<Bus> loadJSONFromNetwork(String urlString) throws XmlPullParserException, IOException {
            InputStream stream = null;
            ReadJSON readJSON = new ReadJSON();
            ArrayList<Bus> entries = new ArrayList();
            try {
                stream = downloadUrl(urlString);
                entries = readJSON.readBusJSON(stream);
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
            return entries;
        }

        //Creating inputstream from url
        private InputStream downloadUrl(String urlString) throws IOException {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(50000 /* milliseconds */);
            conn.setConnectTimeout(50000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            return conn.getInputStream();
        }

        @Override
        protected void onPostExecute(ArrayList<Bus> busList) {
            Intent intent = new Intent(SplashScreen.this,MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("BusList", busList);
            intent.putExtra("BusList", bundle);
            try{
                if(timer!=null)
                    timer.join();
            }catch (Exception e){
                e.printStackTrace();
            }
            startActivity(intent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}