package com.djohnsoftware.adsmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.djohnsoftware.adsmanager.ads.CallQureka;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                new CallQureka(MainActivity.this, new CallQureka.QurekaListener() {
                    @Override
                    public void next() {

                    }
                });
            }
        },3000);
    }
}