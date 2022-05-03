package com.djohnsoftware.adsmanager.ads;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.djohnsoftware.adsmanager.R;
import com.djohnsoftware.adsmanager.retrofit.Livechat_Const;
import com.djohnsoftware.adsmanager.retrofit.SharedHelper;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;

import java.util.Random;


public class CommonInterstitial {

    private static int adCount = 1;

    private static InterstitialAd Google_Interstitial;
    public static Activity activityAll;
    static Dialog mProgreess;
    private static com.facebook.ads.InterstitialAd interstitialAd;

    // TODO: 2/18/2022 Adx MyAds By Jaysukh
    private static String[] rndArray1 = {"Admob", "Facebook", "Adx"};//All
    private static String[] rndArray2 = {"Facebook", "Adx"}; //admob
    private static String[] rndArray3 = {"Admob", "Adx"};  //facebook
    private static String[] rndArray4 = {"Facebook", "Admob"};//Adx
    private static String strAdsShow = "", isMediation = "";
    private static int allCount = 0;

    private static void init(Context context) {
        Log.d("Loading_dialog", "load");

        mProgreess = new Dialog(context);
        mProgreess.setContentView(LayoutInflater.from(context).inflate(R.layout.ad_load_dialog, (ViewGroup) null));
        mProgreess.setCancelable(false);
        mProgreess.show();
        mProgreess.getWindow().setBackgroundDrawable(new ColorDrawable(0));


    }

    public static void showLoadingAds(boolean show) {
        try {
            if (show) {
                if (mProgreess.isShowing()) {
                    mProgreess.dismiss();
                }
                mProgreess.show();
            } else {
                if (mProgreess.isShowing()) {
                    mProgreess.dismiss();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void LoadInterstitialFromSplash(Activity context, Intent intent) {

        allCount = 0;
        activityAll = context;
        init(context);
        showLoadingAds(true);

        isMediation = SharedHelper.getKey(context, Livechat_Const.Mediation);

        switch (isMediation) {
            case "":
                Random rnd = new Random();
                int rndInt = rnd.nextInt(rndArray1.length);
                strAdsShow = rndArray1[rndInt];
                Log.d("RandomAds", " RandomAds: " + strAdsShow);
                if (strAdsShow.equals("Admob")) {
                    Google_Interstitial(context, intent, true);
                } else if (strAdsShow.equals("Facebook")) {
                    FbIntertialLoadAds(context, intent, true);
                } else {
                    AdxInterstial(context, intent, true);
                }
                break;
            case "Mopub":
                showMopubInterstitial(context, intent, true);
                break;
            case "Google":
                Google_Interstitial(context, intent, true);
                break;
            case "AppLovin":
                AppLovinInter(context, intent, true);
                break;
            case "Unity":
                //showMopubInterstitial(context, intent, true);
                break;
        }
    }

    public static void LoadInterstitial(Activity context, Intent intent) {

        allCount = 0;
        activityAll = context;
        String count = SharedHelper.getKey(context, Livechat_Const.ADCOUNT);
        int adShowCount;

        Log.d("AdCount", "ShowCount" + count + " Counter" + adCount);

        if (count.equalsIgnoreCase("")) {
            adShowCount = 2;
        } else {
            adShowCount = Integer.parseInt(count);
        }
        //Log.d("AdCount", "ShowCount" + adShowCount + " Counter" + adCount);

        if (adCount >= adShowCount) {
            init(context);
            showLoadingAds(true);
            isMediation = SharedHelper.getKey(context, Livechat_Const.Mediation);
            switch (isMediation) {
                case "":
                    Log.d("RandomAds", " RandomAds: " + strAdsShow);
                    if (strAdsShow.equals("Admob")) {
                        Google_Interstitial(context, intent, false);
                    } else if (strAdsShow.equals("Facebook")) {
                        FbIntertialLoadAds(context, intent, false);
                    } else {
                        AdxInterstial(context, intent, false);
                    }
                    break;
                case "Mopub":
                    showMopubInterstitial(context, intent, false);
                    break;
                case "Google":
                    Google_Interstitial(context, intent, false);
                    break;
                case "AppLovin":
                    AppLovinInter(context, intent, false);
                    break;
                case "Unity":
                    //showMopubInterstitial(context, intent, false);
                    break;
            }
        } else {
            //context.startActivity(intent);
            startActivity(context, intent,0);
            adCount++;
        }
    }

    public static void LoadInterstitialWithFinish(Activity context) {

        Log.e(TAG, "LoadInterstitialWithFinish: "+context.getClass().getSimpleName() );

        allCount = 0;
        activityAll = context;
        String count = SharedHelper.getKey(context, Livechat_Const.ADCOUNT);
        int adShowCount;

        Log.d("AdCount", "ShowCount" + count + " Counter" + adCount);

        if (count.equalsIgnoreCase("")) {
            adShowCount = 2;
        } else {
            adShowCount = Integer.parseInt(count);
        }
        //Log.d("AdCount", "ShowCount" + adShowCount + " Counter" + adCount);
        if (adCount >= adShowCount) {

            init(context);
            showLoadingAds(true);
            isMediation = SharedHelper.getKey(context, Livechat_Const.Mediation);

            switch (isMediation) {
                case "":
                    Log.d("RandomAds", " RandomAds: " + strAdsShow);
                    if (strAdsShow.equals("Admob")) {
                        Google_Interstitial(context, null, true);
                    } else if (strAdsShow.equals("Facebook")) {
                        FbIntertialLoadAds(context, null, true);
                    } else {
                        AdxInterstial(context, null, true);
                    }
                    break;
                case "Mopub":
                    showMopubInterstitial(context, null, true);
                    break;
                case "Google":
                    Google_Interstitial(context, null, true);
                    break;
                case "AppLovin":
                    AppLovinInter(context, null, true);
                    break;
                case "Unity":
                    //showMopubInterstitial(context, null, true);
                    break;
            }
        } else {
            adCount++;
            context.finish();
        }
    }

    public static void LoadInterstitialWithFinish(Activity context, Intent intent) {

        Log.e(TAG, "LoadInterstitialWithFinish: "+context.getClass().getSimpleName() );

        allCount = 0;
        activityAll = context;
        String count = SharedHelper.getKey(context, Livechat_Const.ADCOUNT);
        int adShowCount;

        Log.d("AdCount", "ShowCount" + count + " Counter" + adCount);

        if (count.equalsIgnoreCase("")) {
            adShowCount = 2;
        } else {
            adShowCount = Integer.parseInt(count);
        }
        //Log.d("AdCount", "ShowCount" + adShowCount + " Counter" + adCount);
        if (adCount >= adShowCount) {

            init(context);
            showLoadingAds(true);
            isMediation = SharedHelper.getKey(context, Livechat_Const.Mediation);

            switch (isMediation) {
                case "":
                    Log.d("RandomAds", " RandomAds: " + strAdsShow);
                    if (strAdsShow.equals("Admob")) {
                        Google_Interstitial(context, intent, true);
                    } else if (strAdsShow.equals("Facebook")) {
                        FbIntertialLoadAds(context, intent, true);
                    } else {
                        AdxInterstial(context, intent, true);
                    }
                    break;
                case "Mopub":
                    showMopubInterstitial(context, intent, true);
                    break;
                case "Google":
                    Google_Interstitial(context, intent, true);
                    break;
                case "AppLovin":
                    AppLovinInter(context, intent, true);
                    break;
                case "Unity":
                    //showMopubInterstitial(context, null, true);
                    break;
            }
        } else {
            adCount++;
            if(intent!=null)context.startActivity(intent);
            context.finish();
        }
    }

    public static void showMopubInterstitial(final Activity activity, Intent intent, boolean finish) {

        MoPubInterstitial mInterstitial = new MoPubInterstitial(activity, SharedHelper.getKey(activity, Livechat_Const.ADMOB_INTRESTITIAL_AD_PUB_ID1));
        mInterstitial.setInterstitialAdListener(new MoPubInterstitial.InterstitialAdListener() {
            @Override
            public void onInterstitialLoaded(MoPubInterstitial moPubInterstitial) {
                showLoadingAds(false);
                if (mInterstitial.isReady()) {
                    mInterstitial.show();
                }


                Log.e("Mopub", "onInterstitialLoaded:");
            }

            @Override
            public void onInterstitialFailed(MoPubInterstitial moPubInterstitial, MoPubErrorCode moPubErrorCode) {

                Log.e("Mopub", "onInterstitialFailed: " + moPubErrorCode.toString());
                //AMStartAppLoadAds(activity, intent, finish);
                FbIntertialLoadAds(activity, intent, finish);
            }

            @Override
            public void onInterstitialShown(MoPubInterstitial moPubInterstitial) {

            }

            @Override
            public void onInterstitialClicked(MoPubInterstitial moPubInterstitial) {

            }

            @Override
            public void onInterstitialDismissed(MoPubInterstitial moPubInterstitial) {

                if (intent != null) {
                    //activity.startActivity(intent);
                    startActivity(activity, intent,0);
                }

                if (finish) {
                    activity.finish();
                    adCount = 1;
                } else {
                    adCount = 1;
                }
            }
        });
        mInterstitial.load();

        Log.e("Mopub", "CommonInterstitial: Loaded...!");
    }

    private static void Google_Interstitial(final Activity context, Intent intent, boolean finish) {

        strAdsShow = "Facebook";

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(context, SharedHelper.getKey(context, Livechat_Const.ADMOB_INTRESTITIAL_AD_PUB_ID1), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                        Google_Interstitial = interstitialAd;
                        Log.d("MyAds", "onAdLoaded");

                        Log.d("MyAds", "Google Interstitial ad is loaded and ready to be displayed!");
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                showLoadingAds(false);
                                Google_Interstitial.show(context);

                                Google_Interstitial.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        super.onAdDismissedFullScreenContent();

                                        if (intent != null) {
                                            //context.startActivity(intent);
                                            startActivity(context, intent,0);
                                        }

                                        if (finish) {
                                            context.finish();
                                            adCount = 1;
                                        } else {
                                            adCount = 1;
                                        }

                                    }
                                });

                            }
                        }, 1000);

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                        Log.i(TAG, loadAdError.getMessage());
                        Google_Interstitial = null;
                        Log.d("MyAds", "GoogleInterAd onAdFailedToLoad:--" + loadAdError.getMessage());

                        allCount++;
                        Log.d("MyAds", "Qureka Inter Count "+ allCount);
                        if (allCount >= 3) {
                            if (SharedHelper.getBoolean(context, Livechat_Const.ISADSQUREKA)) {

                                if (intent != null) {
                                    //context.startActivity(intent);
                                    startActivity(context, intent,0);
                                }

                                if (finish) {
                                    context.finish();
                                }

                                Livechat_Const.intentQuereka(context);

                                adCount = 1;
                            } else {
                                if (intent != null) {
                                    //context.startActivity(intent);
                                    startActivity(context, intent,0);
                                }

                                if (finish) {
                                    context.finish();
                                    adCount = 1;
                                } else {
                                    adCount = 1;
                                }
                            }
                        } else {
                            FbIntertialLoadAds(context, intent, finish);
                        }
                    }
                });

    }


    public static void AppLovinInter(Activity activity, Intent intent, boolean finish) {
        MaxInterstitialAd interstitialAd = new MaxInterstitialAd(SharedHelper.getKey(activity, Livechat_Const.ADMOB_INTRESTITIAL_AD_PUB_ID1), activity);
        interstitialAd.setListener(new MaxAdListener() {
            @Override
            public void onAdLoaded(MaxAd ad) {
                Log.d("AppLovinInter", "onAdLoaded: ");
                showLoadingAds(false);
                if (interstitialAd.isReady()) {
                    Log.d("AppLovinInter", "isReady: ");
                    interstitialAd.showAd();
                }
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {
                Log.d("AppLovinInter", "onAdDisplayed: ");
            }

            @Override
            public void onAdHidden(MaxAd ad) {
                Log.d("AppLovinInter", "onAdHidden: ");
                if (intent != null) {
                    //activity.startActivity(intent);
                    startActivity(activity, intent,0);
                }

                if (finish) {
                    activity.finish();
                    adCount = 1;
                } else {
                    adCount = 1;
                }
            }

            @Override
            public void onAdClicked(MaxAd ad) {
                Log.d("AppLovinInter", "onAdClicked: ");
            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                Log.d("AppLovinInter", "onAdLoadFailed: " + error.getMessage());
                FbIntertialLoadAds(activity, intent, finish);

            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                Log.d("AppLovinInter", "onAdDisplayFailed: " + error.getMessage());
                showLoadingAds(false);
                if (intent != null) {
                    //activity.startActivity(intent);
                    startActivity(activity, intent,0);
                }

                if (finish) {
                    activity.finish();
                    adCount = 1;
                } else {
                    adCount = 1;
                }
            }
        });

        // Load the first ad
        interstitialAd.loadAd();
    }


    private static void FbIntertialLoadAds(Activity activity, Intent intent, boolean finish) {

        strAdsShow = "Adx";

        interstitialAd = new com.facebook.ads.InterstitialAd(activity, SharedHelper.getKey(activity, Livechat_Const.FB_INTRESTITIAL_AD_PUB_ID1));
        // Create listeners for the Interstitial Ad
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {
                // Interstitial ad displayed callback
                Log.d("MyAds", "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                // Interstitial dismissed callback
                Log.d("MyAds", "Interstitial ad dismissed.");
                if (intent != null) {
                    //activity.startActivity(intent);
                    startActivity(activity, intent,0);
                }

                if (finish) {
                    activity.finish();
                    adCount = 1;
                } else {
                    adCount = 1;
                }
            }

            @Override
            public void onError(com.facebook.ads.Ad ad, AdError adError) {
                // Ad error callback
                showLoadingAds(false);
                Log.d("MyAds", " FB Interstitial ad failed to load: " + adError.getErrorMessage());
                // TODO: 31-01-2022 Qureka inter ads

                if (isMediation.equals("")) {
                    allCount++;
                    Log.d("MyAds", "Qureka Inter Count "+ allCount);
                    if (allCount >= 3) {
                        if (SharedHelper.getBoolean(activity, Livechat_Const.ISADSQUREKA)) {

                            if (intent != null) {
                                //activity.startActivity(intent);
                                startActivity(activity, intent,0);
                            }

                            if (finish) {
                                activity.finish();
                            }

                            Livechat_Const.intentQuereka(activity);

                            adCount = 1;
                        } else {
                            if (intent != null) {
                                //activity.startActivity(intent);
                                startActivity(activity, intent,0);
                            }

                            if (finish) {
                                activity.finish();
                                adCount = 1;
                            } else {
                                adCount = 1;
                            }
                        }
                    } else {
                        AdxInterstial(activity, intent, finish);
                    }
                    //}
                } else {
                    if (SharedHelper.getBoolean(activity, Livechat_Const.ISADSQUREKA)) {

                        if (intent != null) {
                            //activity.startActivity(intent);
                            startActivity(activity, intent,0);
                        }

                        if (finish) {
                            activity.finish();
                        }

                        Livechat_Const.intentQuereka(activity);

                        adCount = 1;
                    } else {
                        if (intent != null) {
                            //activity.startActivity(intent);
                            startActivity(activity, intent,0);
                        }

                        if (finish) {
                            activity.finish();
                            adCount = 1;
                        } else {
                            adCount = 1;
                        }
                    }
                }
            }

            @Override
            public void onAdLoaded(com.facebook.ads.Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d("MyAds", "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                showLoadingAds(false);
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(com.facebook.ads.Ad ad) {
                // Ad clicked callback
                Log.d("MyAds", "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(com.facebook.ads.Ad ad) {
                // Ad impression logged callback
                Log.d("MyAds", "Interstitial ad impression logged!");
            }
        };

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());
    }

    // TODO: 2/18/2022 Start adx ads
    private static void AdxInterstial(Activity context, Intent intent, boolean finish) {

        strAdsShow = "Admob";

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(context, SharedHelper.getKey(context, Livechat_Const.ADX_INTRESTITIAL_AD_PUB_ID1), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                        Google_Interstitial = interstitialAd;
                        Log.d("MyAds", "Adx onAdLoaded");
                        Log.d("MyAds", "Adx Interstitial ad is loaded and ready to be displayed!");
                        showLoadingAds(false);
                        Google_Interstitial.show(context);
                        Google_Interstitial.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();

                                if (intent != null) {
                                    //context.startActivity(intent);
                                    startActivity(context, intent,0);
                                }

                                if (finish) {
                                    context.finish();
                                    adCount = 1;
                                } else {
                                    adCount = 1;
                                }

                            }
                        });

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                        Log.i(TAG, loadAdError.getMessage());
                        Google_Interstitial = null;
                        Log.d("MyAds", "Adx inter onAdFailedToLoad:--" + loadAdError.getMessage());

                      /*  Random rnd = new Random();
                        int rndInt = rnd.nextInt(rndArray4.length);
                        String strRnd = rndArray4[rndInt];*/
                        if (isMediation.equals("")) {
                           /* Log.d("RandomAds", " RandomAds: Second " + strRnd);
                            if (strRnd.equals("Admob")) {*/
                            allCount++;
                            Log.d("MyAds", "Qureka Inter Count "+ allCount);
                            if (allCount >= 3) {
                                if (SharedHelper.getBoolean(context, Livechat_Const.ISADSQUREKA)) {
                                    if (intent != null) {
                                        //context.startActivity(intent);
                                        startActivity(context, intent,0);
                                    }

                                    if (finish) {
                                        context.finish();
                                    }

                                    Livechat_Const.intentQuereka(context);

                                    adCount = 1;
                                } else {
                                    if (intent != null) {
                                        //context.startActivity(intent);
                                        startActivity(context, intent,0);
                                    }

                                    if (finish) {
                                        context.finish();
                                        adCount = 1;
                                    } else {
                                        adCount = 1;
                                    }
                                }
                            } else {
                                Google_Interstitial(context, intent, finish);
                            }
                          /*  } else {
                                FbIntertialLoadAds(context, intent, finish);
                            }*/
                        } else {
                            FbIntertialLoadAds(context, intent, finish);
                        }
                    }
                });
    }
    // TODO: 2/18/2022 End adx ads

    private static void startActivity(Activity context, Intent intent, int requestCode){

        if(intent!=null){
            context.startActivityForResult(intent, 1);
        }
    }
}
