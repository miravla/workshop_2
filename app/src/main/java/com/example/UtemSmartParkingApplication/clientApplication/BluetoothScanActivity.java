package com.example.UtemSmartParkingApplication.clientApplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.UtemSmartParkingApplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class BluetoothScanActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener {
    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;
    private JSONObject bluetooth;
    private BluetoothAdapter mBlueAdapter;
    private BluetoothLeScanner scanner;
    private BackgroundCallBack backgroundCallBack;
    private String id,token,name;
    private boolean enabled,interactive;
    private long last;
    TextView txtBluetooth;
    private void showToast(String hello) {
        Toast.makeText(this, hello, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState2) {
        super.onCreate(savedInstanceState2);
        setContentView(R.layout.parking_list);

        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //check if bluetooth is available or not
        if (mBlueAdapter == null) {
            // mStatusBlueTv.setText("Bluetooth is not available");
            Toast.makeText(BluetoothScanActivity.this, "Bluetooth is not available",
                    Toast.LENGTH_SHORT).show();
        }


        if (!mBlueAdapter.isEnabled()) {

            //if bluetooth not open yet
            Toast.makeText(BluetoothScanActivity.this, "Turning on Bluetooth...",
                    Toast.LENGTH_SHORT).show();

            //intent to on BT
            //open bluetooth
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_ENABLE_BT);

            showToast("Making your device discoverable");
            Intent intent2 = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            startActivityForResult(intent2, REQUEST_DISCOVER_BT);

        }

        else
        {
            scanner= mBlueAdapter.getBluetoothLeScanner();
            scanner.startScan(backgroundCallBack);
        }


        backgroundCallBack=new BackgroundCallBack(this);
        try {
            Intent intent=getIntent();
            bluetooth=new JSONObject(Objects.requireNonNull(intent.getStringExtra("bluetooth")));
            token=intent.getStringExtra("token");
            name=intent.getStringExtra("name");
            interactive="ACTIVE_ACK".equals(bluetooth.getString("status"));

            ByteBuffer byteB = ByteBuffer.wrap(new byte[16]);
            UUID uuid=UUID.fromString(bluetooth.getJSONObject("originator").getString("id"));
            byteB.putLong(uuid.getMostSignificantBits());
            byteB.putLong(uuid.getLeastSignificantBits());

            id= Arrays.toString(byteB.array());
            id=id.substring(1,id.length()-1);


        }
        catch(JSONException a)
        {
            a.printStackTrace();
        }

        if(interactive) {
            setContentView(R.layout.parking_list);
            txtBluetooth = findViewById(R.id.bluetooth);
            if (savedInstanceState2 !=null)
            {
                txtBluetooth.setText(savedInstanceState2.getString("name"));
            }

        }


        }

    /*@Override
    public void onRequestPermissionResult(int requestCode, @NonNull String[]permission, @NonNull int[]grantResult)
    {

    }

     */

    void setScanResult(ScanResult result)
    {

        ScanRecord record=result.getScanRecord();
        long time=System.currentTimeMillis();

        boolean found=false;
        if(record!=null)
        {
            int rssi=result.getRssi();
            byte[] data=record.getManufacturerSpecificData(76);
            String UUID=new String(data);
            String name=result.getDevice().getName();
            if(data!=null&&data.length>16)
            {

                found=Arrays.toString(data).contains(id);

            }
        }
        if(interactive)
        {
            if(found){
                last=time;
                enabled=true;
            }
            else
            {
                if(time-last>60000)
                {
                    enabled=false;

                }
            }


        }
        else
        {
            if(found)
            {
                scanner.stopScan(backgroundCallBack);
            }
        }
    }



    @Override
    public void onClick(DialogInterface dialog, int which)
    {
            if(mBlueAdapter.isEnabled()) {
                while (mBlueAdapter.getState() != BluetoothAdapter.STATE_ON) {
                    try {
                        Thread.sleep(600000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                scanner = mBlueAdapter.getBluetoothLeScanner();
                scanner.startScan(backgroundCallBack);
            }
            else
                Toast.makeText(this, "Unable to open Bluetooth...Please check your permission!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

    }
}
