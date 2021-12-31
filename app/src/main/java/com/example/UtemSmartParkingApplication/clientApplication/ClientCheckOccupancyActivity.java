package com.example.UtemSmartParkingApplication.clientApplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.companion.BluetoothDeviceFilter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.UtemSmartParkingApplication.R;
import com.example.UtemSmartParkingApplication.parking_list;
import com.example.UtemSmartParkingApplication.test;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.regex.Pattern;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ClientCheckOccupancyActivity  extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    ListView listView;
    TextView mStatusBlueTv, mPairedTv;
    Button onBtn, offBtn, discoverBtn, pairedButton;
    ScanResult scanresult;
    BluetoothAdapter mBlueAdapter;

    //toast message function
    private void showToast(String hello) {
        Toast.makeText(this, hello, Toast.LENGTH_SHORT).show();
    }


    BluetoothAdapter bluetoothAdapter;
    //private BluetoothLeScanner bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
    private boolean scanning;
    private Handler handler = new Handler();
    private LocationManager locationAdapter;
    private String provider;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
    private ScanCallback mLeScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

            BluetoothDevice newDevice = result.getDevice();

            int newRssi = result.getRssi();
            String device_name = newDevice.getName();
            String device_address = newDevice.getAddress();

        }
    };
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState2) {
        super.onCreate(savedInstanceState2);
        setContentView(R.layout.parking_list);

        mStatusBlueTv = findViewById(R.id.status);
        mPairedTv = findViewById(R.id.paired);
        onBtn = findViewById(R.id.BtnOn);
        offBtn = findViewById(R.id.BtnOff);
        discoverBtn = findViewById(R.id.discover);
        pairedButton = findViewById(R.id.pairedDevices);
        listView=(ListView)findViewById(R.id.LotList);
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        //adapter
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //check if bluetooth is available or not
        if (mBlueAdapter == null) {
            // mStatusBlueTv.setText("Bluetooth is not available");
            Toast.makeText(ClientCheckOccupancyActivity.this, "Bluetooth is not available",
                    Toast.LENGTH_SHORT).show();
        }


        if (!mBlueAdapter.isEnabled()) {
            Toast.makeText(ClientCheckOccupancyActivity.this, "Turning on Bluetooth...",
                    Toast.LENGTH_SHORT).show();

            //intent to on BT
            //open bluetooth
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_ENABLE_BT);

            showToast("Making your device discoverable");
            Intent intent2 = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            startActivityForResult(intent2,REQUEST_DISCOVER_BT);

            // Register for broadcasts when a device is discovered.
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(receiver, filter);

        }


        //TODO:SCan and get device automatically
        //Scan strongest signal
        //paired Button
       // pairedButton.setOnClickListener(v -> {

            Set<BluetoothDevice> devices = mBlueAdapter.getBondedDevices();
            String []deviceName=new String[devices.size()];
            String []UUID=new String[devices.size()];

            int index=0;
            if (devices.size() > 0) {

                mPairedTv.setText("Devices detected :");

                for (BluetoothDevice device : devices) {

                    deviceName[index] = device.getName();
                          int newRssi = scanresult.getRssi();

                        UUID[index] = String.valueOf(newRssi);

                        //device.createBond();
                        // device.setPairingConfirmation();
                        //String deviceHardwareAddress = device.getAddress(); // MAC address
                        index++;

                }
                //get specific name
                if(Arrays.asList(deviceName).contains("ESP32")) {
                ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,deviceName);

                    listView.setAdapter(arrayAdapter);
                }
                //listView.setOnClickListener(View view); {

               // };

            }

            else {
                //bluetooth is off so can't get paired devices
                mPairedTv.setText("No Devices is Detected!");
                showToast("No Devices detected. Please check your location permission and bluetooth status before using the application");
            }
       // });


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
    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action))
            {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();

            }
        }
    };
    //@Override
    protected void onDestroy() {
        super.onDestroy();

        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(receiver);
    }


}