package com.example.gpstest2;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
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
        List<String> jsonList = readJsonFromUrl("http://sawproject.altervista.org/php/cam_request.php");
        for (String s: jsonList) {
            JSONObject obj = new JSONObject(jsonList.get(0));
            savedLocations.add( new Camera(obj.getInt("id"),obj.getDouble("lat"),obj.getDouble("lng"),CameraStatus.valueOf("LOW")));
        }
    }


    public static List<String> readJsonFromUrl(String url) throws IOException, JSONException {
        /*TODO: la struttura dati ricevuta ha il formato
         [{"campo1":"val1",'[altri campi]'},{'altre tuple...'}]
         */
        List<String> list= new LinkedList<String>();
        try (InputStream is = new URL(url).openStream()) {

            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            while (rd.read() != ']')
            {
                rd.skip(1);
                char c;
                String tupla= "";
                while((c = (char) rd.read())!='}'){
                    tupla+=c;
                }
                list.add(tupla+'}');
            }
            return list;
        }
    }
}
