package com.example.UtemSmartParkingApplication;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class test extends AppCompatActivity {
    //private final BroadcastReceiver FoundReceiver = null;
    protected ArrayList<BluetoothDevice> foundDevices = new ArrayList<BluetoothDevice>();
    private ListView foundDevicesListView;
    private ArrayAdapter<BluetoothDevice> btArrayAdapter;

    /**
     * Called when the activity is first created.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parking_list);
        final BluetoothAdapter myBlueToothAdapter = BluetoothAdapter.getDefaultAdapter();

        final Button scanbluetooth = (Button) findViewById(R.id.pairedDevices);
        final ListView foundDevicesListView = (ListView) findViewById(R.id.LotList);

        btArrayAdapter = new ArrayAdapter<BluetoothDevice>(this,
                android.R.layout.simple_list_item_1, foundDevices);

        foundDevicesListView.setAdapter(btArrayAdapter);

        //Turn on Bluetooth
        if (myBlueToothAdapter == null)
            Toast.makeText(test.this, "Your device doesnt support Bluetooth", Toast.LENGTH_LONG).show();
        else if (!myBlueToothAdapter.isEnabled()) {
            Intent BtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(BtIntent, 0);
            Toast.makeText(test.this, "Turning on Bluetooth", Toast.LENGTH_LONG).show();
        }

        // Quick permission check
        int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
        permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
        if (permissionCheck != 0) {

            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
        }


        scanbluetooth.setOnClickListener(v -> {
            btArrayAdapter.clear();

            myBlueToothAdapter.startDiscovery();

            Toast.makeText(test.this, "Scanning Devices", Toast.LENGTH_LONG).show();

        });

        registerReceiver(FoundReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        IntentFilter filter = new IntentFilter(
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(FoundReceiver, filter);

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(FoundReceiver);
    }


    private final BroadcastReceiver FoundReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            // When discovery finds a new device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (!foundDevices.contains(device)) {
                    foundDevices.add(device);
                    Toast.makeText(test.this, "name: " + device.getName() + " " + device.getAddress(), Toast.LENGTH_LONG).show();
                    btArrayAdapter.notifyDataSetChanged();
                }

            }

            // When discovery cycle finished
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (foundDevices == null || foundDevices.isEmpty()) {
                    Toast.makeText(test.this, "No Devices", Toast.LENGTH_LONG).show();
                }
            }

        }
    };


}

