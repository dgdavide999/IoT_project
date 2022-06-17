package com.example.TrafficTraker.CamerasData.DBrequest;

import android.app.Activity;
import android.util.Log;

import com.example.TrafficTraker.R;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DBrequest_lastRegistration  implements Runnable{
    private final String id, TAG = "DBrequest_lastRegistration";
    private final IDBrequest idBrequest_lastRegistration;
    private final Activity activity;
    public DBrequest_lastRegistration(String id, IDBrequest idBrequest_lastRegistration, Activity activity){
        this.id = id;
        this.idBrequest_lastRegistration = idBrequest_lastRegistration;
        this.activity = activity;
    }
    @Override
    public void run() {
        String res = downloadJSON();
        Log.i(TAG,"ui thread");
        activity.runOnUiThread(() -> idBrequest_lastRegistration.onDownoladDone(res));
    }

    private String downloadJSON() {
        try {
            return loadIntoListView();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return "connessione fallita";
    }

    private String loadIntoListView() throws JSONException, IOException {
        return readJsonFromUrl(activity.getString(R.string.CAMERA_STATUS_REQUEST_IPADDRESS)+id);
    }

    public String  readJsonFromUrl(String link) {
        try (InputStream input = new URL(link).openStream()) {
            BufferedReader re = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
            String Text = Read(re);
            return Text.replace("\""," ");
        } catch (IOException e) {
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
        Log.i(TAG,"l messaggio =" + str.length());
        if(str.length()<=3)
            return "impossibile raggiungere il db";
        return str.substring(2,str.length()-3);
    }
}
