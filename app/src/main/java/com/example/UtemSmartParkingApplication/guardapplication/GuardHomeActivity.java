package com.example.UtemSmartParkingApplication.guardapplication;;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.widget.ImageView;

import com.example.UtemSmartParkingApplication.R;

public class GuardHomeActivity extends AppCompatActivity{


       @Override
    protected void onCreate(Bundle savedInstanceState2) {
        super.onCreate(savedInstanceState2);
        setContentView(R.layout.guard_home);
        ImageView illegal = findViewById(R.id.illegalImage);
        illegal.setOnClickListener(v -> {
            Intent intent=new Intent(GuardHomeActivity.this, IllegalParkingActivity.class);
            startActivity(intent);
        });
           ImageView profile = findViewById(R.id.profileImage);
           profile.setOnClickListener(v -> {
               Intent intent=new Intent(GuardHomeActivity.this, GuardProfileActivity.class);
               startActivity(intent);
           });
           ImageView checkOccupancy = findViewById(R.id.checkOccupancyImage);
           checkOccupancy.setOnClickListener(v -> {
               Intent intent=new Intent(GuardHomeActivity.this, CheckOccupancyActivity.class);
               startActivity(intent);
           });
    }

}
