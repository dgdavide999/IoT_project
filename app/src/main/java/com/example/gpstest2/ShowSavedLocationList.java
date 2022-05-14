package com.example.gpstest2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.gpstest2.CamerasData.Camera;
import com.example.gpstest2.CamerasData.CameraList;

import java.util.ArrayList;
import java.util.Map;

public class
ShowSavedLocationList extends AppCompatActivity {

    private ListView lst_savedLocations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_saved_location_list);

        CameraList cameraList = (CameraList)getApplicationContext();
        Map<Integer, Camera> savedLocations = cameraList.getMyLocations();

        lst_savedLocations = findViewById(R.id.lst_wayPoint);
        lst_savedLocations.setAdapter(new ArrayAdapter<Camera>(this, android.R.layout.simple_list_item_1, new ArrayList(cameraList.getMyLocations().values())));
    }
}