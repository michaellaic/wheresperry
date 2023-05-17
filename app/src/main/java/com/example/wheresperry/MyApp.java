package com.example.wheresperry;

import android.app.Application;

import com.example.wheresperry.data.GameManager;
import com.example.wheresperry.init.MyGPS;
import com.example.wheresperry.init.MySP;
import com.example.wheresperry.init.MySignal;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        MySignal.init(this);
        MySP.init(this);
        MyGPS.init(this);
        GameManager.init();
    }
}
