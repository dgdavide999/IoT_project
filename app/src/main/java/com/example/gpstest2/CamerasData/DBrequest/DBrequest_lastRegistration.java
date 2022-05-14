package com.example.gpstest2.CamerasData.DBrequest;

import com.example.gpstest2.CamerasData.Camera;
import com.example.gpstest2.CamerasData.CameraStatus;
import com.example.gpstest2.CamerasData.Registrations.Scorrimento;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DBrequest_lastRegistration  implements Runnable{
    private String ris;
    public DBrequest_lastRegistration(String s){
        ris = s;
    }

    @Override
    public void run() {
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
        String jsonList = readJsonFromUrl("https://sawproject.altervista.org/php/registration_request.php");
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
        //  re.read() return -1 when there is end of buffer , data or end of file.
        return str.substring(2,str.length()-3);
        //toglo le quadre prima e ultima graffa e il -1
    }
}
