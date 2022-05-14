package com.example.gpstest2.CamerasData;

import android.app.Application;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CameraList extends Application {
    //this class must be a singleton: it can be only one objet that implement this class at one time
    //i had to change the manifest name
    private static CameraList singleton;

    private Map<Integer, Camera> myLocations;

    public Map<Integer, Camera> getMyLocations(){
        return myLocations;
    }

    public CameraList getIstance(){
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        myLocations = new ConcurrentHashMap<>();
    }
}
