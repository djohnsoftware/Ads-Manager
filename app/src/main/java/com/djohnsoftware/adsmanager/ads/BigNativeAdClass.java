package com.djohnsoftware.adsmanager.ads;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.bumptech.glide.Glide;
import com.djohnsoftware.adsmanager.R;
import com.djohnsoftware.adsmanager.retrofit.Livechat_Const;
import com.djohnsoftware.adsmanager.retrofit.SharedHelper;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.mopub.nativeads.AdapterHelper;
import com.mopub.nativeads.FacebookAdRenderer;
import com.mopub.nativeads.MoPubNative;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.nativeads.ViewBinder;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;


public class BigNativeAdClass extends FrameLayout{

    public com.facebook.ads.NativeAd fNativeAd;
    public NativeAd nativeAd;
    private static MaxAd nativeMaxAd;
    private int allCount;
    private String isMediation;

    public BigNativeAdClass(@NonNull Context context) {
        super(context);

        loadBigNativeAds(context, this);
    }

    public BigNativeAdClass(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        loadBigNativeAds(context, this);
    }

    public BigNativeAdClass(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        loadBigNativeAds(context, this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BigNativeAdClass(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        loadBigNativeAds(context, this);
    }

    private void loadBigNativeAds(Context context, FrameLayout frameLayout){

        allCount = 0;
        isMediation = SharedHelper.getKey(context, Livechat_Const.Mediation);

        if (isMediation.equals("")) {
            Log.d("MyAds", "RandomAds: Banner " + AdsConstant.strNativeAds);
            if (AdsConstant.strNativeAds.equals("Admob")) {
                loadBig_NativeAds(context, frameLayout);
            } else if (AdsConstant.strNativeAds.equals("Facebook")) {
                FbBigNative(context, frameLayout);
            } else {
                loadBig_NativeAdxAds(context, frameLayout);
            }
        } else if (isMediation.equals("Mopub")) {
            mopubNativeAds(context, frameLayout);
        } else if (isMediation.equals("Google")) {
            loadBig_NativeAds(context, frameLayout);
        } else if (isMediation.equals("AppLovin")) {
            AppLovinNative(context, frameLayout);
        } else if (isMediation.equals("Unity")) {
            //unityads(context, this);
        }
    }

    // TODO: 04-02-2022 Start mopub native ads
    private void mopubNativeAds(Context activity, FrameLayout frameLayout) {
        ViewBinder viewBinder = new ViewBinder.Builder(R.layout.native_ad_mopub)
                .titleId(R.id.native_title)
                .textId(R.id.native_text)
                .mainImageId(R.id.native_main_image)
                .iconImageId(R.id.native_icon_image)
                .callToActionId(R.id.native_cta)
                .sponsoredTextId(R.id.native_sponsored_text_view)
                .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                .build();

        FacebookAdRenderer facebookAdRenderer = new FacebookAdRenderer(
                new FacebookAdRenderer.FacebookViewBinder.Builder(R.layout.fb_native_ad_layout)
                        .titleId(R.id.native_ad_title)
                        .textId(R.id.native_ad_body)
                        .mediaViewId(R.id.native_ad_media)
                        .adIconViewId(R.id.native_ad_icon)
                        .adChoicesRelativeLayoutId(R.id.ad_choices_container)
                        .advertiserNameId(R.id.native_ad_sponsored_label) // Bind either the titleId or advertiserNameId depending on the FB SDK version
                        .callToActionId(R.id.native_ad_call_to_action)
                        .build());

        MoPubStaticNativeAdRenderer adRenderer = new MoPubStaticNativeAdRenderer(viewBinder);


        MoPubNative moPubNative = new MoPubNative(activity, SharedHelper.getKey(activity, Livechat_Const.ADMOB_NATIVE_PUB_ID1), new MoPubNative.MoPubNativeNetworkListener() {
            @Override
            public void onNativeLoad(com.mopub.nativeads.NativeAd nativeAd) {
                Log.d("Mopub", "onNativeLoad: ");
                AdapterHelper ah = new AdapterHelper(activity, 0, 3);
                View v = ah.getAdView(null, null, nativeAd, viewBinder);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                frameLayout.removeAllViews();
                frameLayout.addView(v, params);
               /* viewGroup2.removeAllViews();
                viewGroup2.addView(nativeAd.createAdView(activity, viewGroup2));
                nativeAd.getMoPubAdRenderer().renderAdView(viewGroup2, nativeAd.getBaseNativeAd());*/

            }

            @Override
            public void onNativeFail(NativeErrorCode nativeErrorCode) {
                Log.d("Mopub", "onNativeFail: " + nativeErrorCode);
                Log.d("Mopub", "onNativeFail: " + nativeErrorCode.getIntCode());
                Log.d("Mopub", "onNativeFail: " + nativeErrorCode.toString());
                FbBigNative(activity, frameLayout);

            }
        });
        moPubNative.registerAdRenderer(adRenderer);
        moPubNative.registerAdRenderer(facebookAdRenderer);
        moPubNative.makeRequest();
    }

    // TODO: 04-02-2022 end mopub native ads

    private void loadBig_NativeAds(final Context activity, FrameLayout viewGroup2) {

        AdsConstant.strNativeAds = "Facebook";

        AdLoader adLoader = new AdLoader.Builder(activity, SharedHelper.getKey(activity, Livechat_Const.ADMOB_NATIVE_PUB_ID1))
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(@NonNull NativeAd unifiedNativeAd) {
                        Log.d("FBADS", "Googele ad is loaded and ready to be displayed...!");
                        if (nativeAd != null) {
                            nativeAd.destroy();
                        }
                        nativeAd = unifiedNativeAd;
                        NativeAdView unifiedNativeAdView = (NativeAdView) LayoutInflater.from(activity).inflate(R.layout.ad_regular_layout, null);
                        populateUnifiedNativeAdView(unifiedNativeAd, unifiedNativeAdView);
                        viewGroup2.removeAllViews();
                        viewGroup2.addView(unifiedNativeAdView);
                    }
                }).withAdListener(
                        new AdListener() {
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                super.onAdFailedToLoad(loadAdError);

                                if(isMediation.isEmpty()) {

                                    allCount++;

                                    if (allCount >= 3) {
                                        qurekaAds(activity, viewGroup2);
                                    } else {
                                        FbBigNative(activity, viewGroup2);
                                    }
                                }
                                else {
                                    FbBigNative(activity, viewGroup2);
                                }

                            }
                        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void loadBig_NativeAdxAds(final Context activity, FrameLayout viewGroup2) {

        AdsConstant.strNativeAds = "Admob";

        AdLoader adLoader = new AdLoader.Builder(activity, SharedHelper.getKey(activity, Livechat_Const.ADX_NATIVE_PUB_ID1))
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(@NonNull NativeAd unifiedNativeAd) {
                        Log.d("FBADS", "Googele ad is loaded and ready to be displayed!");
                        if (nativeAd != null) {
                            nativeAd.destroy();
                        }
                        nativeAd = unifiedNativeAd;
                        NativeAdView unifiedNativeAdView = (NativeAdView) LayoutInflater.from(activity).inflate(R.layout.ad_regular_layout, null);
                        populateUnifiedNativeAdView(unifiedNativeAd, unifiedNativeAdView);
                        viewGroup2.removeAllViews();
                        viewGroup2.addView(unifiedNativeAdView);
                    }
                }).withAdListener(
                        new AdListener() {
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                super.onAdFailedToLoad(loadAdError);
                                Log.d("FBADS","Google Big NAtive Failed to Load "+ loadAdError);

                                if(isMediation.isEmpty()) {
                                    allCount++;

                                    if (allCount >= 3) {
                                        qurekaAds(activity, viewGroup2);
                                    } else {
                                        loadBig_NativeAds(activity, viewGroup2);
                                    }
                                }
                                else {
                                    FbBigNative(activity, viewGroup2);
                                }

                            }
                        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    // TODO: 04/02/2022 Start Applovin native ads
    private void AppLovinNative(Context activity, FrameLayout viewGroup2) {
//        MaxNativeAdLoader nativeAdLoader = new MaxNativeAdLoader("YOUR_AD_UNIT_ID", activity);
        MaxNativeAdLoader nativeAdLoader = new MaxNativeAdLoader(SharedHelper.getKey(activity, Livechat_Const.ADMOB_NATIVE_PUB_ID1),  activity);
        nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
            @Override
            public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                // Clean up any pre-existing native ad to prevent memory leaks.

                Log.d("AppLovin", "onNativeAdLoaded: ");
                if (nativeMaxAd != null) {
                    nativeAdLoader.destroy(nativeMaxAd);
                }

                // Save ad for cleanup.
                nativeMaxAd = ad;

                ViewGroup.LayoutParams params = viewGroup2.getLayoutParams();
                params.height = 500;
                viewGroup2.setPadding(10,10,10,20);
                viewGroup2.setLayoutParams(params);
                viewGroup2.setBackgroundResource(R.drawable.ad_background);

                //nativeAdView.getCallToActionButton().setBackgroundColor(getResources().getColor(R.color.app_color));
                nativeAdView.getCallToActionButton().setBackgroundResource(R.drawable.bg_back);

                // Add ad view to view.
                viewGroup2.removeAllViews();
                viewGroup2.addView(nativeAdView);
            }

            @Override
            public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                // We recommend retrying with exponentially higher delays up to a maximum delay
                Log.d("AppLovin", "onNativeAdLoadFailed: ");
                FbBigNative(activity, viewGroup2);
            }

            @Override
            public void onNativeAdClicked(final MaxAd ad) {
                // Optional click callback
            }
        });

        nativeAdLoader.loadAd();
    }
    // TODO: 04/02/2022 end applovin native ads

    private void FbBigNative(Context activity, FrameLayout viewGroup2) {

        AdsConstant.strNativeAds = "Adx";

        fNativeAd = new com.facebook.ads.NativeAd((Context) activity, SharedHelper.getKey(activity, Livechat_Const.FB_NATIVE_PUB_ID1));
        NativeAdListener nativeAdListener = new NativeAdListener() {
            public void onAdClicked(Ad ad) {
                Log.d("FBADS", "BigNative ad clicked!");
            }

            public void onLoggingImpression(Ad ad) {
                Log.d("FBADS", "BigNative ad impression logged!");
            }

            public void onMediaDownloaded(Ad ad) {
                Log.e("FBADS", "BigNative ad finished downloading all assets.");
            }

            public void onError(Ad ad, AdError adError) {

                if(isMediation.isEmpty()) {

                    allCount++;

                    if (allCount >= 3) {
                        qurekaAds(activity, viewGroup2);
                    } else {
                        loadBig_NativeAdxAds(activity, viewGroup2);
                    }
                }
                else {
                    qurekaAds(activity, viewGroup2);
                }
            }

            public void onAdLoaded(Ad ad) {

                inflateAd(activity, fNativeAd, viewGroup2);

                Log.d("FBADS", "BigNative ad is loaded and ready to be displayed!");
                //viewGroup2.addView(com.facebook.ads.NativeAdView.render((Context) activity, fNativeAd, com.facebook.ads.NativeAdView.Type.HEIGHT_300));
            }
        };

        fNativeAd.loadAd(
                fNativeAd.buildLoadAdConfig()
                        .withAdListener(nativeAdListener)
                        .build());
    }

    public void populateUnifiedNativeAdView(NativeAd unifiedNativeAd, NativeAdView unifiedNativeAdView) {
        unifiedNativeAdView.setMediaView( unifiedNativeAdView.findViewById(R.id.ad_media));
        unifiedNativeAdView.setHeadlineView(unifiedNativeAdView.findViewById(R.id.ad_headline));
        unifiedNativeAdView.setBodyView(unifiedNativeAdView.findViewById(R.id.ad_body));
        unifiedNativeAdView.setIconView(unifiedNativeAdView.findViewById(R.id.ad_app_icon));
        unifiedNativeAdView.setPriceView(unifiedNativeAdView.findViewById(R.id.ad_price));
        unifiedNativeAdView.setStarRatingView(unifiedNativeAdView.findViewById(R.id.ad_stars));
        unifiedNativeAdView.setStoreView(unifiedNativeAdView.findViewById(R.id.ad_store));
        unifiedNativeAdView.setCallToActionView(unifiedNativeAdView.findViewById(R.id.ad_call_to_action));
        unifiedNativeAdView.setAdvertiserView(unifiedNativeAdView.findViewById(R.id.ad_advertiser));
        ((TextView) unifiedNativeAdView.getHeadlineView()).setText(unifiedNativeAd.getHeadline());
        if (unifiedNativeAd.getBody() == null) {
            unifiedNativeAdView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            unifiedNativeAdView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) unifiedNativeAdView.getBodyView()).setText(unifiedNativeAd.getBody());
        }
        if (unifiedNativeAd.getCallToAction() == null) {
            unifiedNativeAdView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            unifiedNativeAdView.getCallToActionView().setVisibility(View.VISIBLE);
            ((TextView) unifiedNativeAdView.getCallToActionView()).setText(unifiedNativeAd.getCallToAction());
        }
        if (unifiedNativeAd.getIcon() == null) {
            unifiedNativeAdView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) unifiedNativeAdView.getIconView()).setImageDrawable(unifiedNativeAd.getIcon().getDrawable());
            unifiedNativeAdView.getIconView().setVisibility(View.VISIBLE);
        }
        if (unifiedNativeAd.getPrice() == null) {
            unifiedNativeAdView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            unifiedNativeAdView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) unifiedNativeAdView.getPriceView()).setText(unifiedNativeAd.getPrice());
        }
        if (unifiedNativeAd.getStore() == null) {
            unifiedNativeAdView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            unifiedNativeAdView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) unifiedNativeAdView.getStoreView()).setText(unifiedNativeAd.getStore());
        }
        if (unifiedNativeAd.getStarRating() == null) {
            unifiedNativeAdView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) unifiedNativeAdView.getStarRatingView()).setRating(unifiedNativeAd.getStarRating().floatValue());
            unifiedNativeAdView.getStarRatingView().setVisibility(View.VISIBLE);
        }
        if (unifiedNativeAd.getAdvertiser() == null) {
            unifiedNativeAdView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) unifiedNativeAdView.getAdvertiserView()).setText(unifiedNativeAd.getAdvertiser());
            unifiedNativeAdView.getAdvertiserView().setVisibility(View.VISIBLE);
        }
        unifiedNativeAdView.setNativeAd(unifiedNativeAd);
        VideoController videoController = unifiedNativeAd.getMediaContent().getVideoController();
        if (videoController.hasVideoContent()) {
            videoController.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                public void onVideoEnd() {
                    super.onVideoEnd();
                }
            });
        }
    }

    private static void qurekaAds(Context activity, FrameLayout viewGroup2) {

        View view = LayoutInflater.from(activity).inflate(R.layout.adbignative_layout, viewGroup2, false);
        try {
            GifImageView gifImageView = view.findViewById(R.id.gifImageView1);
            if (SharedHelper.getBoolean(activity, Livechat_Const.ISADSNativeQureka)) {
                Glide.with(activity).load(SharedHelper.getKey(activity, Livechat_Const.Big_CardAds1)).into(gifImageView);
                viewGroup2.setBackgroundResource(R.drawable.bg_border);
                viewGroup2.removeAllViews();
                viewGroup2.addView(view);

                gifImageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Livechat_Const.intentQuereka(activity);
                    }
                });
            } else {
                view.setVisibility(View.GONE);
                viewGroup2.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void inflateAd(Context context, com.facebook.ads.NativeAd nativeAd, FrameLayout frameLayout) {

        NativeAdLayout nativeAdLayout = new NativeAdLayout(context);
        nativeAd.unregisterView();

        // Add the Ad view into the ad container.
        //View view = LayoutInflater.from(context).inflate(R.layout.native_ad_container,null,false);
        //nativeAdLayout = view.findViewById(R.id.native_ad_container);

        LinearLayout adView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.native_ad_layout_1, nativeAdLayout, false);
        frameLayout.addView(adView);

        // Add the AdOptionsView
        LinearLayout adChoicesContainer = findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(context, nativeAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        TextView nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
                adView, nativeAdMedia, nativeAdIcon, clickableViews);
    }

}