package com.templecs.ryding.utilities;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.templecs.ryding.model.Bus;

public class BusLocationService implements Runnable {

    Activity activity;
    String myurl;
    Bus bus = new Bus();

    public BusLocationService(Activity activity, String myurl) {
        this.activity = activity;
        this.myurl = myurl;
    }

    public Bus getBus() {
        return bus;
    }
    @Override
    public void run() {
        if (hasInternetConnection()) {
            try {
                bus = loadJSONFromNetwork(myurl);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        } else {
            Toast toast = Toast.makeText(activity.getApplicationContext(),
                    "Cannot connect to the server", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    //Loading JSON from inputstream then store in arraylist
    private Bus loadJSONFromNetwork(String urlString) throws
            XmlPullParserException, IOException {
        InputStream stream = null;
        ReadJSON readJSON = new ReadJSON();
        Bus entry = new Bus();

        try {
            stream = downloadUrl(urlString);
            entry = readJSON.readBusLocationJSON(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        return entry;
    }

    //Creating inputstream from url
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        return conn.getInputStream();
    }

    //Checking if there is internet connection
    public boolean hasInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfo = cm.getAllNetworkInfo();
        if (networkInfo != null)
            for (int i = 0; i < networkInfo.length; i++)
                if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
        return false;
    }
}
