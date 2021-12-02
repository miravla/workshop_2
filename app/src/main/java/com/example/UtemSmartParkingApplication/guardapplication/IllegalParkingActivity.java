package com.example.UtemSmartParkingApplication.guardapplication;;

import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.UtemSmartParkingApplication.MainActivity;
import com.example.UtemSmartParkingApplication.recyclerview.RecyclerViewAdapter;
import com.example.UtemSmartParkingApplication.R;
import java.util.ArrayList;
import java.util.List;

public class IllegalParkingActivity  extends AppCompatActivity {
   private List<ColorSpace.Model> mModelList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.illigal_parking);

        //RecycleView
        /*
        mRecyclerView = (RecyclerView) findViewById(R.id.illi);
        mAdapter = new RecyclerViewAdapter(getListData());
        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
        */

    }

  /*  private List<Model> getListData() {
        mModelList = new ArrayList<>();
        for (int i = 1; i <= 25; i++) {
            mModelList.add(new Model("TextView " + i));
        }
        return mModelList;
    }
*/
}