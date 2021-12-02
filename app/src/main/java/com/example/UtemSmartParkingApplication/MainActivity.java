package com.example.UtemSmartParkingApplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.UtemSmartParkingApplication.guardapplication.GuardHomeActivity;

public class MainActivity extends AppCompatActivity {

    private EditText txtEmail,txtPassword;
    private ProgressBar loginProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtEmail = findViewById(R.id.emailAddressEditText);
        txtPassword = findViewById(R.id.passwordEditText);
        loginProgress = findViewById(R.id.loginProgress);
        Button btn = (Button) findViewById(R.id.loginButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                loginProgress.setVisibility(View.VISIBLE);
                Intent intent = null;
                //set disable
                txtEmail.setEnabled(false);
                txtPassword.setEnabled(false);
                //view.setEnabled(false);

                //get email and password
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();

                //check database
                //if account available

                intent = new Intent(MainActivity.this, GuardHomeActivity.class);
                startActivity(intent);

            }
        });
    }

    public static class ClientMainActivity {
    }
}