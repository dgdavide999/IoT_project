package com.example.TrafficTraker;

import com.google.android.gms.maps.model.LatLng;

public interface IDynamicMaps {
    void update(LatLng newPosition);
    void publishProgress();
}
