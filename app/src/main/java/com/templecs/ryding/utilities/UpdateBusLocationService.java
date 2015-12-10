package com.templecs.ryding.utilities;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.templecs.ryding.network.GetInputStream;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Narith on 11/27/2015.
 */
public class UpdateBusLocationService implements Runnable {

    private String myurl;
    private String result;
    private GetInputStream getInputStream;

    public UpdateBusLocationService(String myurl) {
        this.myurl = myurl;
        this.getInputStream = new GetInputStream(null);
    }

    public String getResult() {
        return this.result;
    }

    @Override
    public void run() {
        try {
            result = loadJSONFromNetwork(myurl);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    //Loading JSON from inputstream then store in arraylist
    private String loadJSONFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        ReadJSON readJSON = new ReadJSON();
        String str = "";

        try {
            stream = getInputStream.downloadUrl(urlString);
            str = readJSON.readBusLocationUpdateJSON(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return str;
    }
}
