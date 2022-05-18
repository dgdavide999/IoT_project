package com.example.gpstest2.CamerasData.Registrations;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.example.gpstest2.CamerasData.DBrequest.DBrequest_lastRegistration;
import com.example.gpstest2.R;

public class ShowTrafficInfo extends AppCompatActivity {
    TextView tv_address, tv_traffic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_traffic_info2);
        tv_address = findViewById(R.id.tv_address);
        tv_traffic = findViewById(R.id.tv_traffic);
        tv_address.setText(getIntent().getStringExtra("address"));
        String ris = "";
       new DBrequest_lastRegistration(ris,getIntent().getStringExtra("cameraID"),tv_traffic).execute();
        tv_traffic.setText(ris);
    }
}