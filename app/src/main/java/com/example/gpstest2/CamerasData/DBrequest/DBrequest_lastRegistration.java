package com.example.gpstest2.CamerasData.DBrequest;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DBrequest_lastRegistration  extends AsyncTask {
    private  String ris;
    private final String id;
    private TextView tv;
    public DBrequest_lastRegistration(String s, String id, TextView tv){
        ris = s;
        this.id = id;
        tv = tv;

    }


    private void downloadJSON() {
        try {
            loadIntoListView();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void loadIntoListView() throws JSONException, IOException {
        Log.i("TAG", "URL = "+"https://sawproject.altervista.org/php/cam_status_request.php?id="+id);
        ris = readJsonFromUrl("https://sawproject.altervista.org/php/cam_status_request.php?id="+id);
    }

    public String  readJsonFromUrl(String link) {
        try (InputStream input = new URL(link).openStream()) {
            BufferedReader re = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
            String Text = Read(re);
            //TODO: vedere la sintassi e fare un parsing
            return Text;
        } catch (MalformedURLException e) {
            return null;
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
        Log.i("TAG","letto =" + str);
        return str.substring(2,str.length()-3);
    }

    @Override
    protected Object doInBackground(Object[] objects)  {
        downloadJSON();
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        tv.setText(ris);
    }
}
