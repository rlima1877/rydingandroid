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
import java.util.ArrayList;

import com.templecs.ryding.model.BusStop;
import com.templecs.ryding.network.GetInputStream;

public class BusStopService implements Runnable {

    private Activity activity;
    private String myurl;
    private ArrayList<BusStop> busStopList = new ArrayList<BusStop>();
    private GetInputStream getInputStream;

    public BusStopService(Activity activity, String myurl) {
        this.activity = activity;
        this.myurl = myurl;
        this.getInputStream = new GetInputStream(activity);
    }

    //Returning arraylist containing stock objects
    public ArrayList<BusStop> getBusStopList() {
        return busStopList;
    }

    @Override
    public void run() {
        if (getInputStream.hasInternetConnection()) {
            try {
                busStopList = loadJSONFromNetwork(myurl);
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
    private ArrayList<BusStop> loadJSONFromNetwork(String urlString) throws
            XmlPullParserException, IOException {
        InputStream stream = null;
        ReadJSON readJSON = new ReadJSON();
        ArrayList<BusStop> entries = new ArrayList();

        try {
            stream = getInputStream.downloadUrl(urlString);
            entries = readJSON.readBusStopJSON(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        return entries;
    }
}
