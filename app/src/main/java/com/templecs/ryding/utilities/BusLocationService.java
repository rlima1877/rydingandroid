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
import com.templecs.ryding.network.GetInputStream;

public class BusLocationService implements Runnable {

    private Activity activity;
    private String myurl;
    private Bus bus = new Bus();
    private GetInputStream getInputStream;

    public BusLocationService(Activity activity, String myurl) {
        this.activity = activity;
        this.myurl = myurl;
        this.getInputStream = new GetInputStream(activity);

    }

    public Bus getBus() {
        return bus;
    }
    @Override
    public void run() {
        if (getInputStream.hasInternetConnection()) {
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
            stream = getInputStream.downloadUrl(urlString);
            entry = readJSON.readBusLocationJSON(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        return entry;
    }
}
