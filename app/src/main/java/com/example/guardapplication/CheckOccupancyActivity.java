package com.example.guardapplication;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class CheckOccupancyActivity extends AppCompatActivity {

    private ProgressBar loginProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState1) {
        super.onCreate(savedInstanceState1);
        setContentView(R.layout.check_occupancy);


        loginProgress=findViewById(R.id.loginProgress);
    }

}
