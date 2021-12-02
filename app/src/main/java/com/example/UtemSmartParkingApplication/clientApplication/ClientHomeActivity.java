package com.example.UtemSmartParkingApplication.clientApplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.UtemSmartParkingApplication.R;
import com.example.UtemSmartParkingApplication.guardapplication.GuardHomeActivity;
import com.example.UtemSmartParkingApplication.guardapplication.IllegalParkingActivity;

public class ClientHomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState2) {
        super.onCreate(savedInstanceState2);
        setContentView(R.layout.client_home);
        ImageView check = findViewById(R.id.CheckImage);
        check.setOnClickListener(v -> {
            Intent intent = new Intent(ClientHomeActivity.this, ClientCheckOccupancyActivity.class);
            startActivity(intent);
        });
        ImageView profile = findViewById(R.id.profileImage);
        profile.setOnClickListener(v -> {
            Intent intent = new Intent(ClientHomeActivity.this, ClientProfileActivity.class);
            startActivity(intent);
        });
    }
}
