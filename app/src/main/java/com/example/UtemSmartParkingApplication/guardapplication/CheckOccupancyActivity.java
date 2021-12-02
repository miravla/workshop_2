package com.example.UtemSmartParkingApplication.guardapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.UtemSmartParkingApplication.MainActivity;
import com.example.UtemSmartParkingApplication.R;

import androidx.appcompat.app.AppCompatActivity;

public class CheckOccupancyActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState1) {
        super.onCreate(savedInstanceState1);
        setContentView(R.layout.check_occupancy);
        Button btn = (Button) findViewById(R.id.checkOccupiedBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckOccupancyActivity.this, IllegalParkingActivity.class);
                startActivity(intent);

            }});


        }
    }
