package com.djohnsoftware.adsmanager.ads;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.djohnsoftware.adsmanager.R;
import com.djohnsoftware.adsmanager.retrofit.Livechat_Const;
import com.djohnsoftware.adsmanager.retrofit.SharedHelper;

import pl.droidsonroids.gif.GifImageView;

public class QurekaActivity extends AppCompatActivity {

    String imgURl;
    GifImageView qimg;
    TextView txtAds;
    CallQureka.QurekaListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_qureka);

        listener = (CallQureka.QurekaListener) getIntent().getSerializableExtra("QUREKA_LISTENER");

        imgURl = SharedHelper.getKey(getApplicationContext(), Livechat_Const.Image1);

        qimg = findViewById(R.id.qimg);
        txtAds = findViewById(R.id.txtAds);
        Glide.with(getApplicationContext()).load(imgURl).into(qimg);
        waitFor5sec();
        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                txtAds.setText(getString(R.string.adtxt) + " " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                txtAds.setVisibility(View.GONE);
            }

        }.start();
        qimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Livechat_Const.intentQuereka(QurekaActivity.this);
            }
        });

    }

    public void waitFor5sec() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                callHome();
            }
        }, 5000);
    }

    public void callHome() {

        if(listener!=null){
            listener.next();
        }
        QurekaActivity.this.finish();
    }

}