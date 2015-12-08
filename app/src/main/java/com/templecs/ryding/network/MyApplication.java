package com.templecs.ryding.network;

import android.app.Application;
import android.content.Context;

/**
 * Created by rafaellima on 10/22/15.
 */
public class MyApplication extends Application {

    private static MyApplication sInstace;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstace = this;
    }

    public static MyApplication getInstance(){
        return sInstace;
    }

    public static Context getAppContext(){
        return sInstace.getApplicationContext();
    }
}