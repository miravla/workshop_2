package com.example.UtemSmartParkingApplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.UtemSmartParkingApplication.ThingsboardConnection.ThingsboardConnection;
import com.example.UtemSmartParkingApplication.clientApplication.ClientHomeActivity;
import com.example.UtemSmartParkingApplication.guardapplication.GuardHomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {


    private EditText txtEmail,txtPassword;
    private ProgressBar loginProgress;
    private int responsecode = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtEmail = findViewById(R.id.emailAddressEditText);
        txtPassword = findViewById(R.id.passwordEditText);
       // loginProgress = findViewById(R.id.loginProgress);
        Button btn = (Button) findViewById(R.id.loginButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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
                Executors.newSingleThreadExecutor().execute(this::test);

                //check database
                //if account available

                if(responsecode == 200) {
                    intent = new Intent(MainActivity.this, GuardHomeActivity.class);
                }
                else  {
                    Toast.makeText(MainActivity.this, "Not connect!"/*+code*/,
                            Toast.LENGTH_SHORT).show();
                    intent = new Intent(MainActivity.this, ClientHomeActivity.class);
                }
                startActivity(intent);


                Toast.makeText(MainActivity.this, "Not A Valid Account!",
                            Toast.LENGTH_SHORT).show();
                txtEmail.setEnabled(true);
                txtPassword.setEnabled(true);
                //loginProgress.setVisibility(View.INVISIBLE);

            }

            private void test() {

                try {
                    JSONObject request = new JSONObject();
                    //username>get username from xml//

                    request.put("username", txtEmail.getText().toString());
                    //password>get from xml
                    request.put("password", txtPassword.getText().toString());
                    HttpURLConnection connection = (HttpURLConnection)
                            new URL("https://utemsmartparking.tk:443/api/auth/login").openConnection();

                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    responsecode = connection.getResponseCode();
                    connection.getOutputStream().write(request.toString().getBytes());
                    txtEmail.setEnabled(true);
                    txtPassword.setEnabled(true);
                    connection.disconnect();
                   // System.out.println(connection.getResponseCode());


                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }



}