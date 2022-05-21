package com.example.gpstest2;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,IDynamicMaps {
    private final String TAG = "MapsActivity";
    public static final float ZOOM = 12.0f;
    private Boolean connection = true;
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
            new Thread(new MoveMarker(getIntent().getLongExtra("requestInterval",30*1000),getApplicationContext(),this,this)).start();

        }
        //click on markers
        mMap.setOnMarkerClickListener(marker -> {
            if(marker.getSnippet()==null || marker.getSnippet().equals("you"))
                return false;
            Intent i = new Intent(getApplicationContext(), ShowTrafficInfo.class);
            i.putExtra("cameraID",marker.getSnippet());
            i.putExtra("cameraStatus",savedLocation.get(Integer.valueOf(marker.getSnippet())).getStatus().toString());
            Geocoder geocoder = new Geocoder(getApplicationContext());
            LatLng pos = marker.getPosition();
            try {
                i.putExtra("address",geocoder.getFromLocation(pos.latitude, pos.longitude, 1).get(0).getAddressLine(0));
            } catch (IOException e) {
                e.printStackTrace();
            }
            startActivity(i);
            return false;
        });
    }


    @Override
    public void update(LatLng newPosition) {
        Log.i(TAG,"position update");
        if(newPosition==null) {
            if (connection)
                Toast.makeText(getApplicationContext(), "connessione gps interrotta", Toast.LENGTH_LONG).show();
            connection = false;
        }else {
            if(!connection)
                Toast.makeText(getApplicationContext(),"connessione gps ripristinata",Toast.LENGTH_LONG).show();
            connection = true;
            myMarker.setPosition(newPosition);
            new Thread(new MoveMarker(getIntent().getLongExtra("requestInterval", 5 * 1000), getApplicationContext(), this, this)).start();
        }
    }

    @Override
    public void publishProgress() {
    }
}