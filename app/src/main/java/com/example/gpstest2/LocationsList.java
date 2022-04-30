package com.example.gpstest2;

import android.app.Application;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;

public class LocationsList extends Application {
    //this class must be a singleton: it can be only one objet that implement this class at one time
    //i had to change the manifest name
    private static LocationsList singleton;

    private List<Location> myLocations;

    public List<Location> getMyLocations(){
        return myLocations;
    }

    public LocationsList getIstance(){
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        myLocations = new ArrayList<>();
    }
}
