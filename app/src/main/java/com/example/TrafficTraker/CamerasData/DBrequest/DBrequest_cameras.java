package com.example.TrafficTraker.CamerasData.DBrequest;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.example.TrafficTraker.CamerasData.Camera;
import com.example.TrafficTraker.CamerasData.CameraList;
import com.example.TrafficTraker.CamerasData.CameraStatus;
import com.example.TrafficTraker.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ConnectException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBrequest_cameras implements Runnable{
    private final String TAG ="DBrequest_cameras";
    private final Activity activity;
    private final IDBrequest iDBrequest;
    CameraList cameraList;
    Map<Integer, Camera> savedLocations;

    public DBrequest_cameras(CameraList lc, IDBrequest iDBrequest ,Activity activity){
        cameraList = lc;
        this.iDBrequest = iDBrequest;
        this.activity = activity;
    }

    @Override
    public void run() {
        savedLocations = cameraList.getMyLocations();
        try {
            downloadJSON();
        } catch (ConnectException e) {
            activity.runOnUiThread(() -> Toast.makeText(activity.getApplicationContext(), R.string.NO_INTERNET,Toast.LENGTH_LONG).show());
            return;
        }
        activity.runOnUiThread(() -> iDBrequest.onDownoladDone("done"));
    }

    private void downloadJSON() throws ConnectException {
        try {
            loadIntoListView();
        } catch(ConnectException e){
            throw e;
        }
        catch(JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void loadIntoListView() throws JSONException, IOException {
        List<JSONObject> jsonList = readJsonFromUrl(activity.getString(R.string.CAMERA_REQUEST_IPADDRESS));
        if(jsonList==null)throw new ConnectException();
        for (JSONObject obj: jsonList) {
            savedLocations.put( obj.getInt("id"), new Camera(obj.getDouble("lat"),obj.getDouble("lng"), CameraStatus.valueOf(obj.getString("status"))));
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
            Log.e(TAG,"qualcosa ?? andato storto");
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
