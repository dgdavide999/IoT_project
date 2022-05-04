package com.example.gpstest2;

import com.google.android.gms.maps.model.LatLng;

public class Camera {
    private int id;

    public LatLng getPosition() {
        return position;
    }

    private LatLng position;
    private CameraStatus status;

    public Camera(int id,double lat, double lng, CameraStatus s){
        this.id = id;
        position = new LatLng(lat,lng);
        status = s;
    }
}
