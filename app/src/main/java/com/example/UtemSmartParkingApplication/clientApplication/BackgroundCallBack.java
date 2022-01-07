package com.example.UtemSmartParkingApplication.clientApplication;

import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;

public class BackgroundCallBack extends ScanCallback {
    private BluetoothScanActivity activity;
    BackgroundCallBack (BluetoothScanActivity activity)
    {
        this.activity=activity;
    }

    @Override
    public void onScanResult(int callbackType, ScanResult result) {

        activity.setScanResult(result);
    }
}
