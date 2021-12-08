package com.example.UtemSmartParkingApplication.clientApplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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
