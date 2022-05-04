package com.example.gpstest2;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class CameraList extends Application {
    //this class must be a singleton: it can be only one objet that implement this class at one time
    //i had to change the manifest name
    private static CameraList singleton;

    private List<Camera> myLocations;

    public List<Camera> getMyLocations(){
        return myLocations;
    }

    public CameraList getIstance(){
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        myLocations = new ArrayList<>();
    }
}
