package com.example.UtemSmartParkingApplication.ThingsboardConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ThingsboardConnection  {



    private void connection() {
        try {
            JSONObject request = new JSONObject();
            //username>get username from xml//

            request.put("username", "lohchunren99@hotmail.com");
            //password>get from xml
            request.put("password", "abc123");
            HttpURLConnection connection = (HttpURLConnection) new URL("https://utemsmartparking.tk:443/api/auth/login").openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.getOutputStream().write(request.toString().getBytes());

            System.out.println(connection.getResponseCode());

            connection.disconnect();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
