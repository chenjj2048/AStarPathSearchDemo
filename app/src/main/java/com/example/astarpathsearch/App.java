package com.example.astarpathsearch;

import android.app.Application;
import android.content.Context;

/**
 * Created by 彩笔怪盗基德 on 2016/2/15.
 * https://github.com/chenjj2048
 */
public class App extends Application {
    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
