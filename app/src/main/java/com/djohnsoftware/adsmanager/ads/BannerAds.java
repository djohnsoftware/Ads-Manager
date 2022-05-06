package com.djohnsoftware.adsmanager.ads;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.bumptech.glide.Glide;
import com.djohnsoftware.adsmanager.R;
import com.djohnsoftware.adsmanager.retrofit.Livechat_Const;
import com.djohnsoftware.adsmanager.retrofit.SharedHelper;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.LoadAdError;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;

import pl.droidsonroids.gif.GifImageView;

public class BannerAds {

    private static int allCount = 0;
    static String isMediation="";

    public static void bannerAds(final Activity activity, final FrameLayout frameLayout) {

        allCount = 0;
        isMediation = SharedHelper.getKey(activity, Livechat_Const.Mediation);

        if (isMediation.equals("")) {
            Log.d("MyAds", "RandomAds: Banner " + AdsConstant.strNativeAds);
            if (AdsConstant.strNativeAds.equals("Admob")) {
                googleBannerAds(activity, frameLayout);
            } else if (AdsConstant.strNativeAds.equals("Facebook")) {
                FBbannerAds(activity, frameLayout);
            } else {
                AdxBannerAds(activity, frameLayout);
            }
        } else if (isMediation.equals("Mopub")) {
            mopubBannerAds(activity, frameLayout);
        } else if (isMediation.equals("Google")) {
            googleBannerAds(activity, frameLayout);
        } else if (isMediation.equals("AppLovin")) {
            appLovinBannerAds(activity, frameLayout);
        } else if (isMediation.equals("Unity")) {
            //googleBannerAds(activity, frameLayout);
        }
    }


    // TODO: 31-01-2022 Start Mopub banner ads
    private static void mopubBannerAds(Activity activity, FrameLayout frameLayout) {
        MoPubView moPubView = new MoPubView(activity);
        moPubView.setAdUnitId(SharedHelper.getKey(activity, Livechat_Const.ADMOB_BANNER_PUB_ID));
        moPubView.setAdSize(MoPubView.MoPubAdSize.HEIGHT_50);
        moPubView.loadAd();
        moPubView.setBannerAdListener(new MoPubView.BannerAdListener() {
            @Override
            public void onBannerLoaded(@NonNull MoPubView moPubView) {
                frameLayout.removeAllViews();
                frameLayout.addView(moPubView);
                Log.d("MyAds", "Mopub onBannerLoaded: ");
            }

            @Override
            public void onBannerFailed(MoPubView moPubView, MoPubErrorCode moPubErrorCode) {
              /*  Banner banner = new Banner(activity);
                frameLayout.removeAllViews();
                frameLayout.addView(banner);*/
                FBbannerAds(activity, frameLayout);
                Log.d("MyAds", "Mopub onBannerFailed: ");
            }

            @Override
            public void onBannerClicked(MoPubView moPubView) {

            }

            @Override
            public void onBannerExpanded(MoPubView moPubView) {

            }

            @Override
            public void onBannerCollapsed(MoPubView moPubView) {

            }
        });
    }
    // TODO: 31-01-2022 end mopub banner ads


    // TODO: 31-01-2022 Start google banner ads
    private static void googleBannerAds(Activity activity, FrameLayout frameLayout) {
        AdsConstant.strNativeAds = "Facebook";
        com.google.android.gms.ads.AdView mAdView = new com.google.android.gms.ads.AdView(activity);
        mAdView.setAdUnitId(SharedHelper.getKey(activity, Livechat_Const.ADMOB_BANNER_PUB_ID));

        AdRequest adRequest = new AdRequest.Builder().build();
        AdSize adsize = getAdSize(activity);
        mAdView.setAdSize(adsize);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.d("MyAds", "Googel Banner Fail=> Admob" + loadAdError);

                if(isMediation.isEmpty()) {
                    allCount++;
                    Log.d("MyAds", "Admob Qureka Banner Count display" + allCount);
                    if (allCount >= 3) {
                        qurekaAds(activity, frameLayout);
                    } else {
                        FBbannerAds(activity, frameLayout);
                    }
                }
                else {
                    FBbannerAds(activity, frameLayout);
                }
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d("MyAds", "Banner onAdLoaded: Admob ");
//                SplashActivity.strNativeAds="Facebook";
                frameLayout.removeAllViews();
                frameLayout.addView(mAdView);
            }
        });
        mAdView.loadAd(adRequest);
    }

    private static void AdxBannerAds(Activity activity, FrameLayout frameLayout) {
        AdsConstant.strNativeAds = "Admob";
        com.google.android.gms.ads.AdView mAdView = new com.google.android.gms.ads.AdView(activity);
        mAdView.setAdUnitId(SharedHelper.getKey(activity, Livechat_Const.ADX_BANNER_PUB_ID));

        AdRequest adRequest = new AdRequest.Builder().build();
        AdSize adsize = getAdSize(activity);
        mAdView.setAdSize(adsize);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.d("MyAds", "Googel Banner Fail=> Adx " + loadAdError);
                FBbannerAds(activity, frameLayout);
                allCount++;
                Log.d("MyAds", "Adx Qureka Banner Count display"+ allCount);
                if (allCount >= 3) {
                    qurekaAds(activity, frameLayout);
                } else {
                    googleBannerAds(activity, frameLayout);
                }
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
//                SplashActivity.strNativeAds = "Admob";
                Log.d("MyAds", "Banner onAdLoaded: Adx ");
                frameLayout.removeAllViews();
                frameLayout.addView(mAdView);
            }
        });
        mAdView.loadAd(adRequest);
    }

    private static AdSize getAdSize(Context activity) {

        WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);

    }
    // TODO: 31-01-2022 end google banner ads

    // TODO: 31-01-2022 start Fb banner ads
    private static void FBbannerAds(Activity activity, FrameLayout frameLayout) {
        AdsConstant.strNativeAds = "Adx";
        AdView adView = new AdView(activity, SharedHelper.getKey(activity, Livechat_Const.FB_BANNER_PUB_ID), com.facebook.ads.AdSize.BANNER_HEIGHT_50);

        com.facebook.ads.AdListener adListener = new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                Log.d("MyAds", "FB Banner failed" + adError.getErrorMessage());

                if(isMediation.isEmpty()) {
                    allCount++;
                    Log.d("MyAds", "FB Qureka Banner Count display" + allCount);
                    if (allCount >= 3) {
                        qurekaAds(activity, frameLayout);
                    } else {
                        AdxBannerAds(activity, frameLayout);
                    }
                }
                else {
                    qurekaAds(activity, frameLayout);
                }

            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.d("MyAds", "FB Banner Loaded");
//                SplashActivity.strNativeAds = "Adx";
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        };

        // Request an ad
        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
    }

    private static void qurekaAds(Activity activity, FrameLayout frameLayout) {
        try {
            View view = LayoutInflater.from(activity).inflate(R.layout.gifbanner_layout, null, false);

            GifImageView gifImageView = view.findViewById(R.id.gifImageView);
            if (SharedHelper.getBoolean(activity, Livechat_Const.ISADSBAN1Qureka)) {
                Glide.with(activity).load(SharedHelper.getKey(activity, Livechat_Const.Banner_CardAds1)).into(gifImageView);
                frameLayout.removeAllViews();
                frameLayout.addView(view);

                gifImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Livechat_Const.intentQuereka(activity);
                    }
                });
            } else {
                view.setVisibility(View.GONE);
                frameLayout.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // TODO: 31-01-2022 end Fb banner ads

    // TODO: 2/1/2022 Start Applovin banner ads
    private static void appLovinBannerAds(Activity activity, FrameLayout frameLayout) {
        MaxAdView adView = new MaxAdView(SharedHelper.getKey(activity, Livechat_Const.ADMOB_BANNER_PUB_ID), activity);
        // Stretch to the width of the screen for banners to be fully functional
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        // Banner height on phones and tablets is 50 and 90, respectively
        int heightPx = 90;
        adView.setLayoutParams(new FrameLayout.LayoutParams(width, heightPx));
        // Set background or background color for banners to be fully functional
        adView.setBackgroundColor(activity.getResources().getColor(R.color.white));
        frameLayout.addView(adView);
        adView.setListener(new MaxAdViewAdListener() {
            @Override
            public void onAdExpanded(MaxAd ad) {
                Log.d("MyAds", "AppLovinBanner onAdExpanded: ");
            }

            @Override
            public void onAdCollapsed(MaxAd ad) {
                Log.d("MyAds", "AppLovinBanner onAdCollapsed: ");
            }

            @Override
            public void onAdLoaded(MaxAd ad) {
                Log.d("MyAds", "AppLovinBanner onAdLoaded: ");
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {
                Log.d("MyAds", "AppLovinBanner onAdDisplayed: ");
            }

            @Override
            public void onAdHidden(MaxAd ad) {
                Log.d("MyAds", "AppLovinBanner onAdHidden: ");
            }

            @Override
            public void onAdClicked(MaxAd ad) {
                Log.d("MyAds", "AppLovinBanner onAdClicked: ");
            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                Log.d("MyAds", "AppLovinBanner onAdLoadFailed: " + error.getMessage());
                FBbannerAds(activity, frameLayout);
            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                Log.d("MyAds", "AppLovinBanner onAdDisplayFailed: " + error.getMessage());
            }
        });

        // Load the ad
        adView.loadAd();
    }

}
