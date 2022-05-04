package com.example.gpstest2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DBrequest implements Runnable{
    CameraList cameraList;
    List<Camera> savedLocations;
    public DBrequest(CameraList lc, List<Camera> sl){
        cameraList = lc;
        savedLocations = sl;
    }
    @Override
    public void run() {
        savedLocations = cameraList.getMyLocations();
        downloadJSON();
    }

    private void downloadJSON() {
        try {
            loadIntoListView();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadIntoListView() throws JSONException {
        JSONArray jsonArray = new JSONArray("http://sawproject.altervista.org/php/cam_request.php");
        for (int i = 0;i< jsonArray.length();i++){
            JSONObject obj =jsonArray.getJSONObject(i);
            savedLocations.add( new Camera(obj.getInt("id"),obj.getDouble("lat"),obj.getDouble("lng"),CameraStatus.valueOf("LOW")));
        }
    }
}
