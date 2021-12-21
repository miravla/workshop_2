package com.example.UtemSmartParkingApplication.clientApplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.UtemSmartParkingApplication.R;
import com.example.UtemSmartParkingApplication.parking_list;

import java.util.Set;

public class ClientCheckOccupancyActivity  extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;


    TextView mStatusBlueTv, mPairedTv;
    Button onBtn, offBtn, discoverBtn, pairedButton;

    BluetoothAdapter mBlueAdapter;

    //toast message function
    private void showToast(String hello){
        Toast.makeText(this,hello, Toast.LENGTH_SHORT).show();
    }
    private BluetoothAdapter bluetoothAdapter;
    //private BluetoothLeScanner bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
    private boolean scanning;
    private Handler handler = new Handler();

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
    @Override
    protected void onCreate(Bundle savedInstanceState2) {
        super.onCreate(savedInstanceState2);
        setContentView(R.layout.parking_list);

        mStatusBlueTv   = findViewById(R.id.status);
        mPairedTv       = findViewById(R.id.paired);
        onBtn           = findViewById(R.id.BtnOn);
        offBtn          = findViewById(R.id.BtnOff);
        discoverBtn     = findViewById(R.id.discover);
        pairedButton    =  findViewById(R.id.pairedDevices);
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        //adapter
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();


        //check if bluetooth is available or not
        if(mBlueAdapter == null) {
            // mStatusBlueTv.setText("Bluetooth is not available");
            Toast.makeText(ClientCheckOccupancyActivity.this, "Bluetooth is not available",
                    Toast.LENGTH_SHORT).show();
        }


        if(!mBlueAdapter.isEnabled()){
            Toast.makeText(ClientCheckOccupancyActivity.this, "Turning on Bluetooth...",
                    Toast.LENGTH_SHORT).show();
            //showToast("Turning on Bluetooth...");
            //intent to on BT
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent , REQUEST_ENABLE_BT);
        }


        //TODO:BUTTON TAK MAU
        //on BT button
        onBtn.setOnClickListener(v -> {
            if(!mBlueAdapter.isEnabled()){
                showToast("Turning on Bluetooth...");
                //intent to on BT
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent , REQUEST_ENABLE_BT);
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
        //TODO:CANNOT YET
        offBtn.setOnClickListener(v -> {
            if(mBlueAdapter.isEnabled()){
                mBlueAdapter.disable();
                showToast("Turning off Bluetooth");
            }
            /*else {
                showToast("Bluetooth is already off");
            }*/
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
            /*else{
                //bluetooth is off so can't get paired devices
                showToast("Turn on Bluetooth to get paired devices");
            }*/
        });



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