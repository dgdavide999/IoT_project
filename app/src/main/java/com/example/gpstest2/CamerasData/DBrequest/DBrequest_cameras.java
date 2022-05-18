package com.example.gpstest2.CamerasData.DBrequest;

import android.util.Log;

import com.example.gpstest2.CamerasData.Camera;
import com.example.gpstest2.CamerasData.CameraList;
import com.example.gpstest2.CamerasData.CameraStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBrequest_cameras implements Runnable{
    final String TAG ="DBrequest_cameras";
    CameraList cameraList;
    Map<Integer, Camera> savedLocations;
    public DBrequest_cameras(CameraList lc){
        cameraList = lc;
    }
    @Override
    public void run() {
        savedLocations = cameraList.getMyLocations();
        downloadJSON();
    }

    private void downloadJSON() {
        try {
            loadIntoListView();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void loadIntoListView() throws JSONException, IOException {
        List<JSONObject> jsonList = readJsonFromUrl("https://sawproject.altervista.org/php/cam_request.php");

        for (JSONObject obj: jsonList) {
            savedLocations.put( obj.getInt("id"), new Camera(obj.getDouble("lat"),obj.getDouble("lng"), CameraStatus.valueOf("ON")));
        }
    }

    public List<JSONObject> readJsonFromUrl(String link) {
        List<JSONObject> l = new ArrayList<>();
        try (InputStream input = new URL(link).openStream()) {
            Log.i(TAG,"dentro il try");
            BufferedReader re = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
            String Text = Read(re);
            Log.i(TAG,"text = "+Text);
            for (String jobj : Text.split("\\},\\{")) {
                JSONObject json = new JSONObject("{" + jobj + "}");    //Creating A JSON
                l.add(json);
            }
        } catch (Exception e) {
            Log.e(TAG,"qualcosa è andato storto");
            e.printStackTrace();
            return null;
        }return l;
    }

    public String Read(Reader re) throws IOException {
        StringBuilder str = new StringBuilder();
        int temp;
        do {
            temp = re.read();       //reading Charcter By Chracter.
            str.append((char) temp);
        } while (temp != -1);
        //  re.read() return -1 when there is end of buffer , data or end of file.
        return str.substring(2,str.length()-3);
        //toglo le quadre prima e ultima graffa e il -1

    }
}
