package com.example.UtemSmartParkingApplication.clientApplication;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class BLEsearchLoader extends AsyncTaskLoader<Bundle> {


    public BLEsearchLoader(@NonNull Context context) { //declare any values that need to be carry over
        super(context);
    }

    @Override
    protected void onStartLoading()
    {
        forceLoad();
    }

    @Nullable
    @Override
    public Bundle loadInBackground() {
        //main function put in here
        return null;
    }
}
