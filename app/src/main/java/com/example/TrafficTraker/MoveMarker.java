package com.example.TrafficTraker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;

public class MoveMarker implements Runnable {
    String TAG = "MoveMarker";
    private final IDynamicMaps dynamicMaps;
    private final Activity activity;
    private final Context context;
    private final long requestInterval;
    private Location newLocation;

    public MoveMarker(long requestInterval, Context context, IDynamicMaps dynamicMaps, Activity activity){
        this.requestInterval = requestInterval;
        this.dynamicMaps = dynamicMaps;
        this.activity = activity;
        this.context = context;

    }

    @Override
    public void run() {
        Log.i(TAG,"doInBack");
        try {
            Thread.sleep(requestInterval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        updateGPS();

        Log.i(TAG,"fineeee");
    }

    @SuppressLint("MissingPermission")
    private void updateGPS(){
        Log.i(TAG,"updateGPS");
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context,Manifest.permission.INTERNET)==PackageManager.PERMISSION_GRANTED){

                LocationRequest locationRequest = LocationRequest.create();

                fusedLocationProviderClient.getCurrentLocation(locationRequest.getPriority(), new CancellationToken() {

                    @NonNull
                    @Override
                    public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                        return null;
                    }
                    @Override
                    public boolean isCancellationRequested() {
                        return false;
                    }
                }).addOnCompleteListener(task -> {
                    newLocation = task.getResult();
                    LatLng res;
                    if (newLocation != null) {
                        Log.i(TAG, "updateGPS:  location trovata");
                        res =  new LatLng(newLocation.getLatitude(), newLocation.getLongitude());
                    } else {
                        Log.i(TAG, "updateGPS:  location = null");
                        res =  null;
                    }
                    activity.runOnUiThread(() -> dynamicMaps.update(res));
                });
        }
    }
}
