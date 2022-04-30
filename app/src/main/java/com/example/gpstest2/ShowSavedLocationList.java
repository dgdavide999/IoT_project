package com.example.gpstest2;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class ShowSavedLocationList extends AppCompatActivity {

    private ListView lst_savedLocations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_saved_location_list);

        LocationsList locationsList = (LocationsList)getApplicationContext();
        List<Location> savedLocations = locationsList.getMyLocations();

        lst_savedLocations = findViewById(R.id.lst_wayPoint);
        lst_savedLocations.setAdapter(new ArrayAdapter<Location>(this, android.R.layout.simple_list_item_1, savedLocations));
    }
}