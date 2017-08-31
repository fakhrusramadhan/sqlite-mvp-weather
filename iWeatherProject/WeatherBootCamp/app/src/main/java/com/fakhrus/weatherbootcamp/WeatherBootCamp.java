package com.fakhrus.weatherbootcamp;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by Fakhrus on 8/21/17.
 */

public class WeatherBootCamp extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        MultiDex.install(context);
        super.onCreate();
    }

    public static synchronized Context getContext() {
        return context;
    }
}
