package com.example.gpstest2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.gpstest2.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final float ZOOM = 12.0f;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Map<Integer,Camera> savedLocation;

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
        savedLocation.forEach((id,camera)->{
            LatLng latLng = camera.getPosition();
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position((latLng));
            markerOptions.title("Lat: "+ camera.getPosition().latitude+" Long: "+camera.getPosition().longitude);
            markerOptions.snippet(""+id);
            mMap.addMarker(markerOptions);
        });
        double myLat = getIntent().getDoubleExtra("lat",-1);
        double myLng = getIntent().getDoubleExtra("lng",-1);
        if(myLat > 0 && myLng > 0) {
            LatLng you = new LatLng(myLat, myLng);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(you);
            markerOptions.title("you");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
            mMap.addMarker(markerOptions);
            /*zoom at user's position*/
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(you, ZOOM));
            //TODO: creare un thread che aggiorna questo marker ogni secondo
            //(prova a usare l'id del marker)
        }
        //click on markers
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                //ritornare false significa ok
                if(marker.getSnippet()==null)
                    return false;
                Integer cameraId = Integer.parseInt(marker.getSnippet());
                //TODO: chiedere al DB la registrazione più recente (dato l'id della camera) e stampare i dati
                return false;
            }
        });
    }
}