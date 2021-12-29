package com.example.UtemSmartParkingApplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.UtemSmartParkingApplication.ThingsboardConnection.ThingsboardConnection;
import com.example.UtemSmartParkingApplication.clientApplication.ClientHomeActivity;
import com.example.UtemSmartParkingApplication.clientApplication.LoginLoader;
import com.example.UtemSmartParkingApplication.guardapplication.GuardHomeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Bundle> {


    private EditText txtEmail, txtPassword;
    private ProgressBar loginProgress;
    private LoaderManager loaderManager;
    private int responsecode;

    @NonNull
    @Override
    public Loader<Bundle> onCreateLoader(int id, @Nullable Bundle args) {


       /* Loader<Bundle> loader = null;
        if(id == 1)
            loader = new LoginLoader(this, txtEmail.getText().toString(), txtPassword.getText().toString());*/ //USED THIS IF MORE THAN ONE LOADER IS NEEDED

        return new LoginLoader(this, txtEmail.getText().toString(), txtPassword.getText().toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Bundle> loader, Bundle data) {

        loaderManager.destroyLoader(loader.getId());

        if(data != null)
        {
            Intent intent = null;
            String token = data.getString("token", null);

            if(token != null)
                intent = new Intent(MainActivity.this, ClientHomeActivity.class);
            else
                Toast.makeText(MainActivity.this, "Username/password is incorrect",
                        Toast.LENGTH_SHORT).show();

            intent.putExtra("token", token);
            startActivity(intent);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Bundle> loader) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loaderManager = LoaderManager.getInstance(this);
        txtEmail = findViewById(R.id.emailAddressEditText);
        txtPassword = findViewById(R.id.passwordEditText);
        // loginProgress = findViewById(R.id.loginProgress);
      //  Button btn = (Button) findViewById(R.id.loginButton);
      /*  btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loaderManager.initLoader(1,null,this);
                // loginProgress.setVisibility(View.VISIBLE);
                Intent intent = null;
                //set disable
                txtEmail.setEnabled(false);
                txtPassword.setEnabled(false);
                //view.setEnabled(false);

                ///REMIND
                /////get USERNAME NOT EMAIL
                //get email and password
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();
                Executors.newSingleThreadExecutor().execute(MainActivity.this::test);
                //check database
                //if account available

                //loginProgress.setVisibility(View.INVISIBLE);

            }
        })*/
        ;
    }

    public void login(View view)
    {
        loaderManager.initLoader(1,null,this);
    }

    private void process() {
        if (responsecode == 200) {
            Intent intent = new Intent(MainActivity.this, ClientHomeActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "Not connect!" + responsecode,
                    Toast.LENGTH_SHORT).show();
                   /* intent = new Intent(MainActivity.this, ClientHomeActivity.class);
                    startActivity(intent);*/
        }



                /*Toast.makeText(MainActivity.this, "Not A Valid Account!",
                            Toast.LENGTH_SHORT).show();*/
        txtEmail.setEnabled(true);
        txtPassword.setEnabled(true);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void test() {

        try {
            JSONObject request = new JSONObject();
            String token = null;
            // username>get username from xml//

            request.put("username", txtEmail.getText().toString());


            //password>get from xml
            request.put("password", txtPassword.getText().toString());
            HttpsURLConnection connection = (HttpsURLConnection)
                    new URL("https://utemsmartparking.tk/api/auth/login").openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Content-Type", "application/json");


            connection.getOutputStream().write(request.toString().getBytes());
            responsecode = connection.getResponseCode();

            if (responsecode == 200) {
                JSONObject response = new JSONObject(new BufferedReader(new InputStreamReader(connection.getInputStream())).lines().collect(Collectors.joining()));
                token = response.getString("token");
            }

            connection.disconnect();

            if (token != null) {
                connection = (HttpsURLConnection) new URL("https://utemsmartparking.tk/api/tenant/devices?pageSize=1000&page=0").openConnection();

                connection.setRequestProperty("X-Authorization", "Bearer " + token);
                connection.setRequestProperty("Accept", "application/json");

                if (connection.getResponseCode() == 200) {
                    JSONObject response = new JSONObject(new BufferedReader(new InputStreamReader(connection.getInputStream())).lines().collect(Collectors.joining()));
                    JSONArray data = response.getJSONArray("data");
                    int length = data.length();

                    for (int i = 0; i < length; i++)
                        System.out.println(data.getJSONObject(i));
                }
            }
            connection.disconnect();
            runOnUiThread(this::process);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}


