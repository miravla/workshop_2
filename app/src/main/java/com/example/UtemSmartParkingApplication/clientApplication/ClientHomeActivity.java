package com.example.UtemSmartParkingApplication.clientApplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.UtemSmartParkingApplication.R;


public class ClientHomeActivity extends AppCompatActivity {

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState2) {
        super.onCreate(savedInstanceState2);
        setContentView(R.layout.client_home);

        if (savedInstanceState2 == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                token = null;
            } else {
                token = extras.getString("token");
            }
        } else {
            token = (String) savedInstanceState2.getSerializable("token");
        }

        ImageView check = findViewById(R.id.CheckImage);
        check.setOnClickListener(v -> {
            Intent intent = new Intent(ClientHomeActivity.this, BluetoothScanActivity.class);
            intent.putExtra("token", token);
            startActivity(intent);
        });

        ImageView profile = findViewById(R.id.profileImage);
        profile.setOnClickListener(v -> {
            Intent intent = new Intent(ClientHomeActivity.this, ClientProfileActivity.class);
            startActivity(intent);
        });

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    // this event will enable the back
    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    }

