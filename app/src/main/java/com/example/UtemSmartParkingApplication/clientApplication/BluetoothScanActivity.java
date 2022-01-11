package com.example.UtemSmartParkingApplication.clientApplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.example.UtemSmartParkingApplication.MainActivity;
import com.example.UtemSmartParkingApplication.R;

import org.json.JSONObject;

import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

public class BluetoothScanActivity extends AppCompatActivity {


    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int REQUEST_READ_PHONE_STATE = 1;
    private ActivityResultLauncher<Intent> bluetoothLauncher;
    TextView  mPairedTv, txtBluetooth;
    private ProgressBar pgbScan;
    private BluetoothAdapter mBlueAdapter;
    private BluetoothLeScanner scanner;
    private BeaconCallback beaconCallback;
    private String token;
    Button btnScan;


    @Override
    protected void onCreate(Bundle savedInstanceState2) {

        super.onCreate(savedInstanceState2);
        setContentView(R.layout.parking_list);

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

        mPairedTv = findViewById(R.id.status);
        pgbScan = findViewById(R.id.pgbScan);
        txtBluetooth = findViewById(R.id.bluetooth);
        btnScan = findViewById(R.id.btnScan);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        bluetoothLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), this::bluetoothResult);


        btnScan.setOnClickListener(this::bluetoothScan);

        //add permission
        registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                this::bluetoothRequest).launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void bluetoothRequest(boolean isGranted) {

        //get bluetooth service
        BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);

        //get bluetooth adapter
        mBlueAdapter = bluetoothManager.getAdapter();

        //get bluetooth scanner
        scanner = mBlueAdapter.getBluetoothLeScanner();

        //callback for the scan result
        beaconCallback = new BeaconCallback();


        btnScan.setEnabled(true);
    }

    private void bluetoothResult(ActivityResult activityResult) {
        //if bluetooth is open
        if (activityResult.getResultCode() == RESULT_OK)

            //start scan
            bluetoothScan();
    }

    private void bluetoothScan(View view) {
        //scanning progress bar visible
        pgbScan.setVisibility(View.VISIBLE);
        btnScan.setEnabled(true);

        //if no adapter
        if (!mBlueAdapter.isEnabled()) {

            //show message
            Toast.makeText(this, "Bluetooth must be opened to scan the device!", Toast.LENGTH_SHORT).show();

            //get permission again
            bluetoothLauncher.launch(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
        }
        else

            //if adapter get
        //start the scanning
            bluetoothScan();
    }

    private void bluetoothScan() {

       //scanning process

        //stop the scanning if the time passed 30 seconds ady
        new Handler().postDelayed(this::stopScan, 30000);
        scanner.startScan(beaconCallback);
    }

    private void stopScan() {

        //send data to thingsboard
        Executors.newSingleThreadExecutor().execute(this::sendThingsboard);

        //after sending data
        //stop the scanning process
        scanner.stopScan(beaconCallback);

        //process stop
        //progress bar stop
        pgbScan.setVisibility(View.GONE);

        //to enable user to scan again
        btnScan.setEnabled(true);
    }

    @SuppressLint("SetTextI18n")
    private void sendThingsboard() {
        try {
            //get token name from device
            String esp= txtBluetooth.getText().toString();

            //choose the part after ESP32-
            String sub = esp.substring(6);

            JSONObject request = new JSONObject();
            HttpsURLConnection connection = (HttpsURLConnection) new URL(
                    "https://utemsmartparking.tk/api/v1/" + sub
                            + "/telemetry").openConnection();

            request.put("token", token);

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.getOutputStream().write(request.toString().getBytes());

            //if successfully send
            if (connection.getResponseCode() == 200)
            {
                //show dialog
                AlertDialog alertDialog = new AlertDialog.Builder(BluetoothScanActivity.this).create();
                alertDialog.setTitle("Notification");
                alertDialog.setMessage("Your car is successfully registered in the system!");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        (dialog, which) -> dialog.dismiss());
                alertDialog.show();

                //Successfully sent to thingsboard
                mPairedTv.setText("Currently parked at");
                btnScan.setEnabled(false);
            }

            //if not send
            else
            {
                //show dialog
                AlertDialog alertDialog = new AlertDialog.Builder(BluetoothScanActivity.this).create();
                alertDialog.setTitle("Notification");
                alertDialog.setMessage("Registered failed! Please contact our officer for help!");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        (dialog, which) -> dialog.dismiss());
                alertDialog.show();
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class BeaconCallback extends ScanCallback {


        private int maxRssi = Integer.MIN_VALUE;

        @SuppressLint("SetTextI18n")
        @Override
        public void onScanResult(int callbackType, ScanResult result) {

            try {
                //get the rssi value from device scan
                int rssi = result.getRssi();

                    synchronized (this) {

                        //detect whether the device is ESP 32 or not
                        byte[] data = result.getScanRecord().getManufacturerSpecificData(76);

                        //if it is ESP 32
                        if (data != null) {


                            //get the strongest rssi value
                            if (rssi > maxRssi) {
                                maxRssi = rssi;

                                //get device
                                BluetoothDevice device = result.getDevice();

                                //get device name
                                txtBluetooth.setText(device.getName());

                                //set text field
                                mPairedTv.setText("Devices detected :");

                            }

                        } else
                            {
                            //not a ESP 32 device
                            mPairedTv.setText("NO ESP 32 device is around ");
                        }
                    }
            }
         catch (Exception e) {

            //no device is detected
            mPairedTv.setText("NO device is detected ");
        }

        }
        @Override
        public void onScanFailed(int errorCode) {

            //scan failed
            super.onScanFailed(errorCode);

            //send error message and code
            Toast.makeText(BluetoothScanActivity.this, "Scan failed with error: " + errorCode, Toast.LENGTH_SHORT).show();


        }
    }


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


