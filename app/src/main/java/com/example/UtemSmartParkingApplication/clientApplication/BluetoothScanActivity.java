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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.UtemSmartParkingApplication.R;

import org.json.JSONObject;

import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

public class BluetoothScanActivity extends AppCompatActivity  {


    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int REQUEST_READ_PHONE_STATE = 1;
    private ActivityResultLauncher<Intent> bluetoothEnabler;
    TextView mStatusBlueTv, mPairedTv,txtBluetooth;
    private ProgressBar pgbScan;

    private BluetoothAdapter mBlueAdapter=null;
    private BluetoothLeScanner scanner;
    private BeaconCallback beaconCallback;
    Button btnScan;

    private void showToast(String hello) {
        Toast.makeText(this, hello, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState2) {
        mStatusBlueTv = findViewById(R.id.status);
        mPairedTv = findViewById(R.id.paired);
        pgbScan = findViewById(R.id.pgbScan);
        txtBluetooth = findViewById(R.id.bluetooth);
        btnScan = findViewById(R.id.btnScan);

        super.onCreate(savedInstanceState2);
        setContentView(R.layout.parking_list);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        BluetoothManager btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

        // btnScan.setOnClickListener(this::scan);
        mBlueAdapter = btManager.getAdapter();
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!mBlueAdapter.isEnabled()) {

            //if bluetooth not open yet
            Toast.makeText(BluetoothScanActivity.this, "Turning on Bluetooth...",
                    Toast.LENGTH_SHORT).show();

            //intent to on BT
            //open bluetooth
            //bluetoothEnabler = registerForActivityResult(
                   // new ActivityResultContracts.StartActivityForResult(), this::enable);
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_ENABLE_BT);

            showToast("Making your device discoverable");
            Intent intent2 = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            startActivityForResult(intent2, REQUEST_DISCOVER_BT);

            ActivityCompat.requestPermissions(BluetoothScanActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

            mBlueAdapter = btManager.getAdapter();
        }


        scanner = mBlueAdapter.getBluetoothLeScanner();
        beaconCallback = new BeaconCallback();
        scan();
        }

    private void request(boolean isGranted) {
        BluetoothManager btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

       // bluetoothEnabler = registerForActivityResult(
             //   new ActivityResultContracts.StartActivityForResult(), this::enable);
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, REQUEST_ENABLE_BT);

        showToast("Making your device discoverable");
        Intent intent2 = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(intent2, REQUEST_DISCOVER_BT);

        ActivityCompat.requestPermissions(BluetoothScanActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);

        mBlueAdapter = btManager.getAdapter();
        scanner = mBlueAdapter.getBluetoothLeScanner();
        beaconCallback = new BeaconCallback();
       // btnScan.setEnabled(true);
        scan();

    }

    private void enable(ActivityResult activityResult)
    {
        if (activityResult.getResultCode() == RESULT_OK)
            scan();
    }
/*
    private void scan(View view)
    {


        if (!mBlueAdapter.isEnabled())
            bluetoothEnabler.launch(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
        else
            scan();
    }

 */

    private void scan()
    {
        new Handler().postDelayed(this::stop, 30000);
        scanner.startScan(beaconCallback);
    }

    private void stop()
    {
        Executors.newSingleThreadExecutor().execute(this::send);
        scanner.stopScan(beaconCallback);
     //   pgbScan.setVisibility(View.GONE);

    }

    private void send()
    {
        try
        {
            JSONObject request = new JSONObject();
            HttpsURLConnection connection = (HttpsURLConnection) new URL(
                    "https://utemsmartparking.tk/api/v1/" + txtBluetooth.getText()
                            + "/telemetry").openConnection();

            request.put("user", "Satrya Fajri Pratama");

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.getOutputStream().write(request.toString().getBytes());

            if (connection.getResponseCode() == 200)
                System.out.println("Success");
            else
                System.out.println("Fail");

            connection.disconnect();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    private class BeaconCallback extends ScanCallback
    {


        private int maxRssi = Integer.MIN_VALUE;

        @Override
        public void onScanResult(int callbackType, ScanResult result)
        {
            super.onScanResult(callbackType, result);

            int rssi = result.getRssi();

            synchronized (this)
            {
                if (rssi > maxRssi)
                {
                    maxRssi = rssi;

                    BluetoothDevice device = result.getDevice();
                    txtBluetooth.setText(device.getName());
                    Toast.makeText(BluetoothScanActivity.this, "HAHA", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Toast.makeText(BluetoothScanActivity.this, "GG.COM", Toast.LENGTH_SHORT).show();
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

    /*

	private ActivityResultLauncher<Intent> bluetoothEnabler;
	private BluetoothAdapter bluetoothAdapter;
	private BluetoothLeScanner bleScanner;
	private BeaconCallback beaconCallback;
	private ProgressBar pgbScan;
	private TextView txtDetail;
	private Button btnScan;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		bluetoothEnabler = registerForActivityResult(
				new ActivityResultContracts.StartActivityForResult(), this::enable);
		pgbScan = findViewById(R.id.pgbScan);
		txtDetail = findViewById(R.id.txtDetail);
		btnScan = findViewById(R.id.btnScan);

		btnScan.setOnClickListener(this::scan);
		registerForActivityResult(new ActivityResultContracts.RequestPermission(),
				this::request).launch(Manifest.permission.ACCESS_FINE_LOCATION);
	}

	private void request(boolean isGranted)
	{
		BluetoothManager bluetoothManager =
				(BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
		bluetoothAdapter = bluetoothManager.getAdapter();
		bleScanner = bluetoothAdapter.getBluetoothLeScanner();
		beaconCallback = new BeaconCallback();

		btnScan.setEnabled(true);
	}

	private void enable(ActivityResult activityResult)
	{
		if (activityResult.getResultCode() == RESULT_OK)
			scan();
	}

	private void scan(View view)
	{
		pgbScan.setVisibility(View.VISIBLE);
		btnScan.setEnabled(false);

		if (!bluetoothAdapter.isEnabled())
			bluetoothEnabler.launch(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
		else
			scan();
	}

	private void scan()
	{
		new Handler().postDelayed(this::stop, 30000);
		bleScanner.startScan(beaconCallback);
	}

	private void stop()
	{
		Executors.newSingleThreadExecutor().execute(this::send);
		bleScanner.stopScan(beaconCallback);
		pgbScan.setVisibility(View.GONE);
		btnScan.setEnabled(true);
	}

	private void send()
	{
		try
		{
			JSONObject request = new JSONObject();
			HttpsURLConnection connection = (HttpsURLConnection) new URL(
					"https://utemsmartparking.tk/api/v1/" + txtDetail.getText()
							+ "/telemetry").openConnection();

			request.put("user", "Satrya Fajri Pratama");

			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.getOutputStream().write(request.toString().getBytes());

			if (connection.getResponseCode() == 200)
				System.out.println("Success");
			else
				System.out.println("Fail");

			connection.disconnect();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private class BeaconCallback extends ScanCallback
	{
		private int maxRssi = Integer.MIN_VALUE;

		@Override
		public void onScanResult(int callbackType, ScanResult result)
		{
			int rssi = result.getRssi();

			synchronized (this)
			{
				if (rssi > maxRssi)
				{
					maxRssi = rssi;
					BluetoothDevice device = result.getDevice();

					txtDetail.setText(device.getName());
				}
			}
		}
	}
     */

}
