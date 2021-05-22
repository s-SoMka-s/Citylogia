package com.solution.citylogia;

import android.app.Application;
import android.content.Context;

// TODO Rewrite this shit with Hilt!!!

public class StaticContextFactory extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        StaticContextFactory.context = getApplicationContext();
    }

    public static Context getContext() {
         return StaticContextFactory.context;
    }
}
