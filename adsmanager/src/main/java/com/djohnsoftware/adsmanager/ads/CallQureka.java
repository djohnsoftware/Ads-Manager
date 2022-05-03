package com.djohnsoftware.adsmanager.ads;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;

import java.io.Serializable;

public class CallQureka {

    public interface QurekaListener extends Serializable {
        void next();
    }

    private QurekaListener listener;
    private Activity activity;

    public CallQureka(Activity activity, QurekaListener listener) {
        this.listener = listener;
        this.activity = activity;

        Intent intent = new Intent(activity, QurekaActivity.class);
        intent.putExtra("QUREKA_LISTENER", this.listener);

        activity.startActivity(intent);
    }
}
