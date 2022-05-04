package com.example.gpstest2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int DEFAULT_UPDATE_INTERVAL = 30;
    private static final int FAST_UPDATE_INTERVAL = 5;
    private static final int PERMISSION_CODE = 3;

    private TextView tv_lat, tv_lon, tv_altitude, tv_accuracy, tv_speed, tv_sensor, tv_updates, tv_address, tv_waypointsCounts;
    private Switch sw_locationupdates, sw_gps;
    private Button  btt_newWaypoint, btt_showWaypointList, btt_showMap, btt_startDBrequest;

    // Location request is a config file for all settings related to FusedLocationProviderClient
    private LocationRequest locationRequest;
    private LocationManager locationManager;
    // Google's API for location services. The majority of the app function using this class
    // remember to set implementation in gradle Module
    private FusedLocationProviderClient fusedLocationProviderClient;

    private LocationCallback locationCallback;

    private Location currentLocation;

    List<Camera> savedLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TextWives
        tv_lat = findViewById(R.id.tv_lat);
        tv_lon = findViewById(R.id.tv_lon);
        tv_altitude = findViewById(R.id.tv_altitude);
        tv_accuracy = findViewById(R.id.tv_accuracy);
        tv_speed = findViewById(R.id.tv_speed);
        tv_sensor = findViewById(R.id.tv_sensor);
        tv_updates = findViewById(R.id.tv_updates);
        tv_address = findViewById(R.id.tv_address);
        tv_waypointsCounts = findViewById(R.id.tv_waypoints);
        //Switches
        sw_gps = findViewById(R.id.sw_gps);
        sw_locationupdates = findViewById(R.id.sw_locationsupdates);
        //Buttons
        btt_newWaypoint= findViewById(R.id.btt_newWayPoint);
        btt_showWaypointList = findViewById(R.id.btt_showWaypointList);
        btt_showMap = findViewById(R.id.btt_showMap);
        btt_startDBrequest = findViewById(R.id.btt_startDBrequest);
        //locationManager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //òocationRequest
        // set all properties of LocationRequest
        locationRequest = LocationRequest.create();
        // how often does the default location check occur
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);
        // how often does the default location check occur when set to the most frequent update
        locationRequest.setFastestInterval(1000 * FAST_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //locationCallback
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // save the location
                currentLocation = locationResult.getLastLocation();
            }
        };
        //fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        //onClickListeners
        sw_gps.setOnClickListener(view -> {
            if (sw_gps.isChecked()) {
                //most accurate -- use GPS
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                tv_sensor.setText(R.string.SET_PRIORITY_HIGH_ACCURACY);
            } else {
                locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                tv_sensor.setText(R.string.SET_BALANCED_POWER_ACCURACY);
            }
        });
        updateGPS();


        sw_locationupdates.setOnClickListener(view -> {
            if (sw_locationupdates.isChecked()) {
                //turn on location traking
                startLocationUpdates();
            } else {
                //turn off tracking(){
                stopLocationUpdates();
            }
        });

        btt_newWaypoint.setOnClickListener(view -> {
            // get the gps location

            // add the new location to the global list
            CameraList cameraList = (CameraList)getApplicationContext();
            savedLocations = cameraList.getMyLocations();
            //savedLocations.add(currentLocation);
        });

        btt_showWaypointList.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, ShowSavedLocationList.class);
            startActivity(i);
        });

        btt_showMap.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this,MapsActivity.class);
            startActivity(i);
        });

        btt_startDBrequest.setOnClickListener(view -> new Thread(new DBrequest((CameraList)getApplicationContext(),savedLocations)).start());
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        tv_updates.setText(R.string.UPDATE_ON);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, R.string.PERMISSION_NOT_GRANTED, Toast.LENGTH_SHORT).show();
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        updateGPS();
    }

    private void stopLocationUpdates() {
        tv_updates.setText(R.string.UPDATE_OFF);
        tv_lat.setText(R.string.UPDATE_OFF);
        tv_lon.setText(R.string.UPDATE_OFF);
        tv_altitude.setText(R.string.UPDATE_OFF);
        tv_accuracy.setText(R.string.UPDATE_OFF);
        tv_speed.setText(R.string.UPDATE_OFF);
        tv_address.setText(R.string.UPDATE_OFF);

        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG + "onRequest","grantResult.size =" + grantResults.length);
        Log.i(TAG + "onRequest","Permission Granted =" + PackageManager.PERMISSION_GRANTED);
        Log.i(TAG + "onRequest","grantResult[0] =" + grantResults[0] + "     permission[0] = "+ permissions[0]);
        Log.i(TAG + "onRequest","grantResult[1] =" + grantResults[1]);
        Log.i(TAG + "onRequest","grantResult.[2] =" + grantResults[2]);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG + "onRequest", "permissions granted");
                updateGPS();
            } else {
                Toast.makeText(this, R.string.PERMISSION_NOT_GRANTED, Toast.LENGTH_SHORT).show();
                finish();// call onDestroy
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void updateGPS(){
        Log.i(TAG,"updateGPS");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.INTERNET)==PackageManager.PERMISSION_GRANTED){
                Log.i(TAG+": onCreate","user provided the permissions");

                if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
                        Location location = task.getResult();
                        if (location != null)
                            currentLocation = location;
                        else {
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                        }
                    });
                    //null solo all'avvio
                    if (currentLocation != null)
                        updateUI(currentLocation);
                }
                else{
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
        }
        else{
            Log.i(TAG,"permission not granted yet");
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, PERMISSION_CODE);
        }

    }

    @SuppressLint("SetTextI18n")
    private void updateUI(Location location) {
        Log.i(TAG,"updateUi");
        // update all of the text view objects whit a new location

        tv_lat.setText(String.valueOf(location.getLatitude()));
        tv_lon.setText(String.valueOf(location.getLongitude()));
        tv_accuracy.setText(String.valueOf(location.getAccuracy()));

        if(location.hasAltitude())
            tv_altitude.setText(String.valueOf(location.getAltitude()));
        else
            tv_altitude.setText(R.string.RESOURCE_NOT_AVAILABLE);
        if(location.hasSpeed())
            tv_speed.setText(String.valueOf(location.getSpeed()));
        else
            tv_speed.setText(R.string.RESOURCE_NOT_AVAILABLE);

        Geocoder geocoder = new Geocoder(MainActivity.this);
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            tv_address.setText(addresses.get(0).getAddressLine(0));
        }catch (Exception e) {
            tv_address.setText("unable to get street address");
        }

        CameraList cameraList = (CameraList) getApplicationContext();
        savedLocations = cameraList.getMyLocations();
        tv_waypointsCounts.setText(Integer.toString(savedLocations.size()));
        //show the numbeer of waypoints
    }

}

//https://www.youtube.com/watch?v=UKUb7C3b0us
//https://youtu.be/_xUcYfbtfsI?t=3421