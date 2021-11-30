package com.example.guardapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    private EditText txtEmail,txtPassword;
    private ProgressBar loginProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtEmail=findViewById(R.id.emailAddressEditText);
        txtPassword=findViewById(R.id.passwordEditText);
        loginProgress=findViewById(R.id.loginProgress);
    }

    public void login(View view)
    {
        loginProgress.setVisibility(View.VISIBLE);
        Intent intent=null;
        //set disable
        txtEmail.setEnabled(false);
        txtPassword.setEnabled(false);
        view.setEnabled(false);

        //get email and password
        String email=txtEmail.getText().toString();
        String password=txtPassword.getText().toString();

        //check database
        //if account available

        intent=new Intent(getApplicationContext(),GuardHomeActivity.class);
        startActivity(intent);

    }

}