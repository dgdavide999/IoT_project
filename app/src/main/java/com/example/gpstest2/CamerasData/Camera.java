package com.example.gpstest2.CamerasData;

import com.google.android.gms.maps.model.LatLng;

public class Camera {

    public LatLng getPosition() {
        return position;
    }

    private final LatLng position;
    private final CameraStatus status;


    public Camera(double lat, double lng, CameraStatus s){
        position = new LatLng(lat,lng);
        status = s;
    }

    public CameraStatus getStatus() {
        return status;
    }
}
