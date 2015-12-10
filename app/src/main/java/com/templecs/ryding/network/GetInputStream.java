package com.templecs.ryding.network;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Narith on 12/9/2015.
 */
public class GetInputStream {
    private Activity activity;

    public GetInputStream(Activity activity){
        this.activity = activity;
    }
    //Creating inputstream from url
    public InputStream downloadUrl(String urlString) throws IOException {
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
