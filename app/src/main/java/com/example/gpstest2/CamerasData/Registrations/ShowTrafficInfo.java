package com.example.gpstest2.CamerasData.Registrations;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.gpstest2.CamerasData.CameraStatus;
import com.example.gpstest2.CamerasData.DBrequest.DBrequest_lastRegistration;
import com.example.gpstest2.CamerasData.DBrequest.IDBrequest;
import com.example.gpstest2.R;

public class ShowTrafficInfo extends AppCompatActivity implements IDBrequest {
    TextView tv_address, tv_traffic, tv_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_traffic_info);
        tv_address = findViewById(R.id.tv_address);
        tv_traffic = findViewById(R.id.tv_traffic);
        tv_status = findViewById(R.id.tv_status);
        tv_address.setText(getIntent().getStringExtra("address"));
        String cameraStatus = getIntent().getStringExtra("cameraStatus");
        if(CameraStatus.valueOf(cameraStatus).equals(CameraStatus.ON)) {
            tv_status.setText(R.string.STATUS_ON_MESSAGE);
            new Thread(new DBrequest_lastRegistration(getIntent().getStringExtra("cameraID"), this, this)).start();
        }else {
            tv_status.setText(R.string.STATUS_OFF_MESSAGE);
            tv_traffic.setText(R.string.NO_TRAFFIC_DATA_AVAILABLE);
        }
    }

    @Override
    public void onDownoladDone(String res) {

        tv_traffic.setText(res);
    }
}