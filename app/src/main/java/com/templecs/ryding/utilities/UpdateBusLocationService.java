package com.templecs.ryding.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Narith on 11/27/2015.
 */
public class UpdateBusLocationService implements Runnable {

    Context context;
    String myurl;
    String result;

    public UpdateBusLocationService(Context context, String myurl) {
        this.myurl = myurl;
        this.context = context;
    }

    public String getResult() {
        return this.result;
    }

    @Override
    public void run() {
        if (hasInternetConnection()) {
            try {
                result = loadJSONFromNetwork(myurl);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(context, "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    //Loading JSON from inputstream then store in arraylist
    private String loadJSONFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        ReadJSON readJSON = new ReadJSON();
        String str = "";

        try {
            stream = downloadUrl(urlString);
            str = readJSON.readBusLocationUpdateJSON(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        return str;
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
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfo = cm.getAllNetworkInfo();
        if (networkInfo != null)
            for (int i = 0; i < networkInfo.length; i++)
                if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
        return false;
    }
}
