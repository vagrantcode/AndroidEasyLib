package com.mw.androideasylib;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.mw.easylib.Common.DateUtil;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
         long res=   DateUtil.difTime(DateUtil.convertToDate("2020-09-01 13:00:00",DateUtil.DateStringFormat.DateTimeHorizontal.value()),DateUtil.convertToDate("2020-09-01 13:00:01",DateUtil.DateStringFormat.DateTimeHorizontal.value()));
            Log.i("sdfasdfa",res+"");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}