package com.example.UtemSmartParkingApplication.clientApplication;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.loader.content.AsyncTaskLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class parkingloader extends AsyncTaskLoader<Bundle> {

    private final String token, accessToken;

    public parkingloader(@NonNull Context context, String token, String accessToken) {
        super(context);
        this.token = token;
        this.accessToken = accessToken;
    }

    @Override
    protected void onStartLoading()
    {
        forceLoad();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public Bundle loadInBackground() {
        Bundle response = null;

        try {
            JSONObject request = new JSONObject();
            String token = null;
            String access_Token = null;
            String tokenUrl = "http(s)://host:port/api/v1/" + access_Token + "/attributes";
            // username>get username from xml//

            request.put("username", username);


            //password>get from xml
            request.put("password", password);
            HttpsURLConnection connection = (HttpsURLConnection)
                    new URL(tokenUrl).openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Content-Type", "application/json");
            connection.getOutputStream().write(request.toString().getBytes());


            if (connection.getResponseCode() == 200)
            {
                response = new Bundle();
                JSONObject resp = new JSONObject(new BufferedReader(new InputStreamReader(connection.getInputStream())).lines().collect(Collectors.joining()));
                token = response.getString("token");

                response.putString("token", resp.getString("token"));
            }

            connection.disconnect();

            if (token != null) { //High chance Unneeded
                connection = (HttpsURLConnection) new URL("https://utemsmartparking.tk/api/tenant/devices?pageSize=1000&page=0").openConnection();

                connection.setRequestProperty("X-Authorization", "Bearer " + token);
                connection.setRequestProperty("Accept", "application/json");

                if (connection.getResponseCode() == 200) {
                    JSONObject resp = new JSONObject(new BufferedReader(new InputStreamReader(connection.getInputStream())).lines().collect(Collectors.joining()));
                    JSONArray data = resp.getJSONArray("data");
                    int length = data.length();

                    for (int i = 0; i < length; i++)
                        System.out.println(data.getJSONObject(i));
                }
            }
            connection.disconnect();
            //runOnUiThread(this::process);


        }catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
