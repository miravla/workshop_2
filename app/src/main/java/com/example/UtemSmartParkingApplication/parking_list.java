package com.example.UtemSmartParkingApplication;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Set;


public class parking_list extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;


    TextView mStatusBlueTv, mPairedTv;
    Button onBtn, offBtn, discoverBtn, pairedButton;

    BluetoothAdapter mBlueAdapter;

    //toast message function
    private void showToast(String hello){
        Toast.makeText(this,hello, Toast.LENGTH_SHORT).show();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parking_list);

        mStatusBlueTv   = findViewById(R.id.status);
        mPairedTv       = findViewById(R.id.paired);
        onBtn           = findViewById(R.id.BtnOn);
        offBtn          = findViewById(R.id.BtnOff);
        discoverBtn     = findViewById(R.id.discover);

        //adapter
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();


        //check if bluetooth is available or not
        if(mBlueAdapter == null) {
            mStatusBlueTv.setText("Bluetooth is not available");

        }
        else {
            mStatusBlueTv.setText("Bluetooth is available");

        }


        //on BT button
        onBtn.setOnClickListener(v -> {
            if(!mBlueAdapter.isEnabled()){
                showToast("Turning on Bluetooth...");
                //intent to on BT
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent , REQUEST_ENABLE_BT);
            }

            else
            {
                showToast("Bluetooth is already on");
            }
        });

        //discover BT btn
        discoverBtn.setOnClickListener(v -> {
            if(!mBlueAdapter.isDiscovering()){
                showToast("Making your device discoverable");
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                startActivityForResult(intent , REQUEST_DISCOVER_BT);
            }
        });

        //off BT button
        offBtn.setOnClickListener(v -> {
            if(mBlueAdapter.isEnabled()){
                mBlueAdapter.disable();
                showToast("Turning off Bluetooth");
            }
            else {
                showToast("Bluetooth is already off");
            }
        });

        //paired Button
        pairedButton.setOnClickListener(v -> {
            if(mBlueAdapter.isEnabled()){
                mPairedTv.setText("Paired Devices");
                Set<BluetoothDevice> devices = mBlueAdapter.getBondedDevices();
                for(BluetoothDevice device: devices){
                    mPairedTv.append("\nDevice : " + device.getName()+ "," + device);
                }
            }
            else{
                //bluetooth is off so can't get paired devices
                showToast("Turn on Bluetooth to get paired devices");
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if(resultCode == RESULT_OK){
                    //bluetooth is on
                    showToast("Bluetooth is on");
                }
                else{
                    //user denied to turn bluetooth on
                    showToast("could not turn on bluetooth");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
