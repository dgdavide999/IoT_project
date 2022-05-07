package com.example.gpstest2;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.gpstest2.databinding.ActivityMapsBinding;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final float ZOOM = 12.0f;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private List<Camera> savedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        CameraList cameraList = (CameraList)getApplicationContext();
        savedLocation = cameraList.getMyLocations();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        /* Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */
        LatLng lastLocationPlaced = null;

        for (Camera camera: savedLocation) {
            LatLng latLng = camera.getPosition();
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position((latLng));
            markerOptions.title("Lat: "+ camera.getPosition().latitude+" Long: "+camera.getPosition().longitude);
            mMap.addMarker(markerOptions);
            lastLocationPlaced = latLng;
        }
        //zoom at the last wayPoint added
        if(lastLocationPlaced!=null)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLocationPlaced, ZOOM));

        //click on markers
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                // lets count the number od times the pin is clicked.
                //TODO: aprire un activity con le informazioni del DB

                Integer clicks = (Integer) marker.getTag();
                if(clicks == null) clicks = 0;
                clicks++;
                marker.setTag(clicks);
                Toast.makeText(MapsActivity.this, "Marker "+ marker.getTitle() + " was clicked "+ marker.getTag() +" times", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }
}