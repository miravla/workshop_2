package com.example.UtemSmartParkingApplication.clientApplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.UtemSmartParkingApplication.R;
import com.example.UtemSmartParkingApplication.guardapplication.EditProfileActivity;
import com.example.UtemSmartParkingApplication.guardapplication.GuardProfileActivity;

public class ClientProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState1) {
        super.onCreate(savedInstanceState1);
        setContentView(R.layout.profile);
        Button btn = (Button) findViewById(R.id.editProfileBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);

            }});
    }
}
