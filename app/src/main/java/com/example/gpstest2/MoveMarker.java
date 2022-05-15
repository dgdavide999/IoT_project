package com.example.gpstest2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;

public class MoveMarker extends AsyncTask {
    String TAG = "MoveMarker";
    private Marker marker;
    private final Context context;
    private Boolean iCanReedGPS;

    public MoveMarker(Marker m,Context c, Boolean gpsVisibility){
        marker = m;
        context = c;
        iCanReedGPS = gpsVisibility;
    }
    @Override
    protected Object doInBackground(Object[] objects) {
        Log.i(TAG,"doInBack");
        updateGPS(context);
        return null;
    }

    @SuppressLint("MissingPermission")
    private void updateGPS(Context c){
        Log.i(TAG,"updateGPS");
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(c);

        if(ActivityCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(c, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(c,Manifest.permission.INTERNET)==PackageManager.PERMISSION_GRANTED){

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
                    Location location = task.getResult();
                    if (location != null) {
                        Log.i(TAG,"updateGPS:  location trovata");
                        marker.setPosition(new LatLng(location.getLatitude(),location.getLongitude()));
                    }

                });
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        new MoveMarker(marker,context,iCanReedGPS).execute();
        Log.i(TAG,"ho finitio");
    }
}
