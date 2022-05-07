package com.example.gpstest2;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DBrequest implements Runnable{
    CameraList cameraList;
    List<Camera> savedLocations;
    public DBrequest(CameraList lc){
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
            savedLocations.add( new Camera(obj.getInt("id"),obj.getDouble("lat"),obj.getDouble("lng"),CameraStatus.valueOf("ON")));
        }
    }

    public List<JSONObject> readJsonFromUrl(String link) {
        try (InputStream input = new URL(link).openStream()) {
            List<JSONObject> l = new ArrayList<>();
            BufferedReader re = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
            String Text = Read(re);
            for (String jobj : Text.split("\\},\\{")) {
                JSONObject json = new JSONObject("{" + jobj + "}");    //Creating A JSON
                l.add(json);
            }
            return l;
        } catch (Exception e) {
            return null;
        }
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
