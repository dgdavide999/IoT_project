package com.example.gpstest2;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.gpstest2.CamerasData.Camera;
import com.example.gpstest2.CamerasData.CameraList;
import com.example.gpstest2.CamerasData.Registrations.ShowTrafficInfo;
import com.example.gpstest2.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final float ZOOM = 12.0f;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Marker myMarker;
    private Map<Integer, Camera> savedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        CameraList cameraList = (CameraList)getApplicationContext();
        savedLocation = cameraList.getMyLocations();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        savedLocation.forEach((id,camera)->{
            LatLng latLng = camera.getPosition();
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position((latLng));
            try {
                Geocoder geocoder = new Geocoder(getApplicationContext());
                markerOptions.title(geocoder.getFromLocation(camera.getPosition().latitude, camera.getPosition().longitude, 1).get(0).getAddressLine(0));
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            myMarker = mMap.addMarker(markerOptions);
            /*zoom at user's position*/
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(you, ZOOM));
            MoveMarker mm = new MoveMarker(myMarker, getApplicationContext(),true);
            Log.i("MapsActivity","mm.execute");
            //mm.execute(getIntent().getLongExtra("requestInterval",30*1000));

        }
        //click on markers
        mMap.setOnMarkerClickListener(marker -> {
            if(marker.getSnippet()==null || marker.getSnippet().equals("you"))
                return false;
            Intent i = new Intent(getApplicationContext(), ShowTrafficInfo.class);
            i.putExtra("cameraID",marker.getSnippet());
            Geocoder geocoder = new Geocoder(getApplicationContext());
            LatLng pos = marker.getPosition();
            try {
                i.putExtra("address",geocoder.getFromLocation(pos.latitude, pos.longitude, 1).get(0).getAddressLine(0));
            } catch (IOException e) {}
            //TODO: far partire i (aspetta di avere le query "lastRegistration")
            return false;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("TAG","OnDestroy");
        //TODO: fermare i moveMaker (broadCast?)
        // usare put extra con un valore bool che se falso termina il ciclo?
    }
}