package com.djohnsoftware.adsmanager.ads;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder;
import com.bumptech.glide.Glide;
import com.djohnsoftware.adsmanager.R;
import com.djohnsoftware.adsmanager.retrofit.Livechat_Const;
import com.djohnsoftware.adsmanager.retrofit.SharedHelper;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.mopub.nativeads.AdapterHelper;
import com.mopub.nativeads.FacebookAdRenderer;
import com.mopub.nativeads.MoPubNative;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.nativeads.RequestParameters;
import com.mopub.nativeads.ViewBinder;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;


public class MedNativeAdClass extends FrameLayout {

    private TextView ad_call_to_action;
    private NativeAd GoogleNativeBig;
    private MaxAd nativeMaxAd;
    private int allCount;
    private String isMediation;

    public MedNativeAdClass(@NonNull Context context) {
        super(context);

        loadNativeBannerAds(context, this);
    }

    public MedNativeAdClass(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        loadNativeBannerAds(context, this);
    }

    public MedNativeAdClass(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        loadNativeBannerAds(context, this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MedNativeAdClass(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        loadNativeBannerAds(context, this);
    }

    private void loadNativeBannerAds(Context context, FrameLayout frameLayout){

        allCount = 0;
        isMediation = SharedHelper.getKey(context, Livechat_Const.Mediation);

        if (isMediation.equals("")) {
            Log.d("MyAds", "RandomAds: Banner " + AdsConstant.strNativeAds);
            if (AdsConstant.strNativeAds.equals("Admob")) {
                load_Medium_NativeAds(context, frameLayout);
            } else if (AdsConstant.strNativeAds.equals("Facebook")) {
                FbNativeBanner(context, frameLayout);
            } else {
                loadMediumAdxAds(context, frameLayout);
            }
        } else if (isMediation.equals("Mopub")) {
            mopubNativeBanerAds(context, frameLayout);
        } else if (isMediation.equals("Google")) {
            load_Medium_NativeAds(context, frameLayout);
        } else if (isMediation.equals("AppLovin")) {
            AppLovinNative(context, frameLayout);
        } else if (isMediation.equals("Unity")) {
            //unityads(context, this);
        }
    }

    private void mopubNativeBanerAds(final Context activity, final FrameLayout frameLayout) {

        ViewBinder viewBinder = new ViewBinder.Builder(R.layout.native_ad_list_item)
                .titleId(R.id.native_title)
                .textId(R.id.native_text)
                .mainImageId(R.id.native_main_image)
                .iconImageId(R.id.native_icon_image)
                .callToActionId(R.id.native_cta)
                .sponsoredTextId(R.id.native_sponsored_text_view)
                .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                .build();

        FacebookAdRenderer facebookAdRenderer = new FacebookAdRenderer(
                new FacebookAdRenderer.FacebookViewBinder.Builder(R.layout.fb_nativebanr_ad_layout)
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
                FbNativeBanner(activity, frameLayout);

            }
        });
        moPubNative.registerAdRenderer(adRenderer);
        moPubNative.registerAdRenderer(facebookAdRenderer);
        moPubNative.makeRequest();
        //bindAdapter(activity, viewBinder, frameLayout);
    }

    private void bindAdapter(Context activity, ViewBinder viewBinder, FrameLayout frameLayout) {
        MoPubStaticNativeAdRenderer staticAdRender = new MoPubStaticNativeAdRenderer(viewBinder);

        MoPubNative classThatRegistersAdRenderers = new MoPubNative(activity, SharedHelper.getKey(activity, Livechat_Const.ADMOB_NATIVE_PUB_ID1), new MoPubNative.MoPubNativeNetworkListener() {
            @Override
            public void onNativeLoad(com.mopub.nativeads.NativeAd nativeAd) {

                Log.d("Mopub", "onNativeLoad: ");

                //View v = nativeAd.createAdView(MainActivity.this,null);

                AdapterHelper ah = new AdapterHelper(activity, 0, 3);
                View v = ah.getAdView(null, null, nativeAd, viewBinder);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                frameLayout.addView(v, params);
            }

            @Override
            public void onNativeFail(NativeErrorCode nativeErrorCode) {

                Log.d("Mopub", "onNativeFail: " + nativeErrorCode);
                Log.d("Mopub", "onNativeFail: " + nativeErrorCode.getIntCode());
                Log.d("Mopub", "onNativeFail: " + nativeErrorCode.toString());
              /*  try {
                    final StartAppNativeAd startAppNativeAd = new StartAppNativeAd(activity);
                    startAppNativeAd.loadAd(new NativeAdPreferences().setAdsNumber(2).setAutoBitmapDownload(true).setPrimaryImageSize(2), new AdEventListener() {
                        @Override
                        public void onReceiveAd(com.startapp.sdk.adsbase.Ad ad) {
                            ArrayList nativeAds = startAppNativeAd.getNativeAds();
                            final NativeAdDetails nativeAdDetails = nativeAds.size() > 0 ? (NativeAdDetails) nativeAds.get(0) : null;
                            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.startapp_large_native_ads, frameLayout, false);
                            frameLayout.addView(linearLayout);
                            if (nativeAds != null) {
                                ImageView imageView = (ImageView) linearLayout.findViewById(R.id.imgFreeApp);
                                TextView textView = (TextView) linearLayout.findViewById(R.id.txtFreeApp);
                                TextView textView2 = (TextView) linearLayout.findViewById(R.id.txtDesc);
                                LinearLayout linearLayout2 = (LinearLayout) linearLayout.findViewById(R.id.StartAppNativeAdd);
                                RatingBar ratingBar = (RatingBar) linearLayout.findViewById(R.id.ratingBar);
                                imageView.setEnabled(true);
                                textView.setEnabled(true);
                                imageView.setImageBitmap(nativeAdDetails.getImageBitmap());
                                textView.setText(nativeAdDetails.getTitle());
                                textView2.setText(nativeAdDetails.getDescription());
                                ratingBar.setRating(nativeAdDetails.getRating());
                                linearLayout2.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View view) {
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("market://details?id=");
                                        sb.append(nativeAdDetails.getPackacgeName());
                                        activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(sb.toString())));
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailedToReceiveAd(com.startapp.sdk.adsbase.Ad ad) {
                            Log.d("Mopub", "Start app native : " + nativeErrorCode.toString());
                        }

                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                FbNativeBanner(activity, frameLayout);

            }
        });

        EnumSet<RequestParameters.NativeAdAsset> assetsSet = EnumSet.of(
                RequestParameters.NativeAdAsset.TITLE,
                RequestParameters.NativeAdAsset.TEXT,
                RequestParameters.NativeAdAsset.CALL_TO_ACTION_TEXT,
                RequestParameters.NativeAdAsset.MAIN_IMAGE,
                RequestParameters.NativeAdAsset.ICON_IMAGE,
                RequestParameters.NativeAdAsset.STAR_RATING);

        Location exampleLocation = new Location("example_location");
        exampleLocation.setLatitude(23.1);
        exampleLocation.setLongitude(42.1);
        exampleLocation.setAccuracy(100);
        final String keywords = "";

        RequestParameters mRequestParameters = new RequestParameters.Builder()
                .location(exampleLocation)
                .keywords(keywords)
                .desiredAssets(assetsSet)
                .build();

        classThatRegistersAdRenderers.makeRequest(mRequestParameters);
        classThatRegistersAdRenderers.registerAdRenderer(staticAdRender);
    }

    private void load_Medium_NativeAds(final Context activity, FrameLayout viewGroup) {

        AdsConstant.strNativeAds = "Facebook";

        Log.e("FBADS", "load_Medium_NativeAds: " );

        AdLoader adLoader = new AdLoader.Builder(activity, SharedHelper.getKey(activity, Livechat_Const.ADMOB_NATIVE_PUB_ID1))
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(@NonNull NativeAd unifiedNativeAd) {
                        Log.d("FBADS", "Googele ad is loaded and ready to be displayed!");
                        if (GoogleNativeBig != null) {
                            GoogleNativeBig.destroy();
                        }
                        GoogleNativeBig = unifiedNativeAd;
                        View inflate = LayoutInflater.from(activity).inflate(R.layout.ad_medium, (ViewGroup) null);
                        ShowNative_small(activity, GoogleNativeBig, (NativeAdView) inflate.findViewById(R.id.ad_view));
                        viewGroup.removeAllViews();
                        viewGroup.addView(inflate);
                    }
                }).withAdListener(
                        new AdListener() {
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                super.onAdFailedToLoad(loadAdError);
                                Log.d("FBADS", "Google Big NAtive Failed to Load " + loadAdError);

                                if(isMediation.isEmpty()) {

                                    allCount++;

                                    if (allCount >= 3) {
                                        qurekaAds(activity, viewGroup);
                                    } else {
                                        FbNativeBanner(activity, viewGroup);
                                    }
                                }
                                else {
                                    FbNativeBanner(activity, viewGroup);
                                }

                            }
                        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void loadMediumAdxAds(final Context activity, final FrameLayout viewGroup) {

        AdsConstant.strNativeAds = "Admob";

        Log.e("FBADS", "load_Medium_NativeAds: " );

        AdLoader adLoader = new AdLoader.Builder(activity, SharedHelper.getKey(activity, Livechat_Const.ADX_NATIVE_PUB_ID1))
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(@NonNull NativeAd unifiedNativeAd) {
                        Log.d("FBADS", "Googele ad is loaded and ready to be displayed!");
                        if (GoogleNativeBig != null) {
                            GoogleNativeBig.destroy();
                        }
                        GoogleNativeBig = unifiedNativeAd;
                        View inflate = LayoutInflater.from(activity).inflate(R.layout.ad_medium, (ViewGroup) null);
                        ShowNative_small(activity, GoogleNativeBig, (NativeAdView) inflate.findViewById(R.id.ad_view));
                        viewGroup.removeAllViews();
                        viewGroup.addView(inflate);
                    }
                }).withAdListener(
                        new AdListener() {
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                super.onAdFailedToLoad(loadAdError);
                                Log.d("FBADS", "Google Big NAtive Failed to Load " + loadAdError);

                                if(isMediation.isEmpty()) {
                                    allCount++;

                                    if (allCount >= 3) {
                                        qurekaAds(activity, viewGroup);
                                    } else {
                                        load_Medium_NativeAds(activity, viewGroup);
                                    }
                                }
                                else {
                                    FbNativeBanner(activity, viewGroup);
                                }
                            }
                        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void FbNativeBanner(Context activity, FrameLayout frameLayout) {

        AdsConstant.strNativeAds = "Adx";

        NativeBannerAd nativeBannerAd = new NativeBannerAd(activity, SharedHelper.getKey(activity, Livechat_Const.FB_NATIVE_BANNER_PUB_ID1));
        NativeAdListener nativeAdListener = new NativeAdListener() {

            @Override
            public void onMediaDownloaded(com.facebook.ads.Ad ad) {
                // Native ad finished downloading all assets
                Log.e("FBADS", "Native ad finished downloading all assets.");
            }

            @Override
            public void onError(com.facebook.ads.Ad ad, AdError adError) {
                // Native ad failed to load
                Log.e("FBADS", "Native ad failed to load: " + adError.getErrorMessage());

                if(isMediation.isEmpty()) {

                    allCount++;

                    if (allCount >= 3) {
                        qurekaAds(activity, frameLayout);
                    } else {
                        loadMediumAdxAds(activity, frameLayout);
                    }
                }
                else {
                    qurekaAds(activity, frameLayout);
                }

            }

            @Override
            public void onAdLoaded(com.facebook.ads.Ad ad) {
                // Native ad is loaded and ready to be displayed
                Log.d("FBADS", "Native ad is loaded and ready to be displayed!");
                if (nativeBannerAd == null || nativeBannerAd != ad) {
                    return;
                }
                // Inflate Native Banner Ad into Container
                //View render = NativeBannerAdView.render(activity, nativeBannerAd, NativeBannerAdView.Type.HEIGHT_120);
                //frameLayout.addView(render);
                // TODO: 1/31/2022 native banner layout
                inflateAd1(nativeBannerAd, frameLayout, activity);
            }

            @Override
            public void onAdClicked(com.facebook.ads.Ad ad) {
                // Native ad clicked
                Log.d("FBADS", "Native ad clicked!");
            }

            @Override
            public void onLoggingImpression(com.facebook.ads.Ad ad) {
                // Native ad impression
                Log.d("FBADS", "Native ad impression logged!");
            }
        };
        // load the ad
        nativeBannerAd.loadAd(
                nativeBannerAd.buildLoadAdConfig()
                        .withAdListener(nativeAdListener)
                        .build());
    }

    private static void inflateAd1(NativeBannerAd nativeBannerAd, FrameLayout frameLayout, Context activity) {
        // Unregister last ad
        nativeBannerAd.unregisterView();

        // Add the Ad view into the ad container.
        //nativeAdLayout = findViewById(R.id.native_banner_ad_container);
        NativeAdLayout nativeAdLayout = new NativeAdLayout(activity);
        LayoutInflater inflater = LayoutInflater.from(activity);
        // Inflate the Ad view.  The layout referenced is the one you created in the last step.
        View adView = inflater.inflate(R.layout.fb_native_banner_layout, null, false);
        frameLayout.addView(adView);

        // Add the AdChoices icon
        RelativeLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(activity, nativeBannerAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        com.facebook.ads.MediaView nativeAdIconView = adView.findViewById(R.id.native_icon_view);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdCallToAction.setText(nativeBannerAd.getAdCallToAction());
        nativeAdCallToAction.setVisibility(
                nativeBannerAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdTitle.setText(nativeBannerAd.getAdvertiserName());
        nativeAdSocialContext.setText(nativeBannerAd.getAdSocialContext());
        sponsoredLabel.setText(nativeBannerAd.getSponsoredTranslation());

        // Register the Title and CTA button to listen for clicks.
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);
        nativeBannerAd.registerViewForInteraction(adView, nativeAdIconView, clickableViews);
    }

    public static void ShowNative_small(Context activity, NativeAd unifiedNativeAd, NativeAdView unifiedNativeAdView) {
        MediaView mediaView = (MediaView) unifiedNativeAdView.findViewById(R.id.ad_media);
        ImageView imageView2 = (ImageView) unifiedNativeAdView.findViewById(R.id.imageView_icon);
        imageView2.setVisibility(View.GONE);
        mediaView.setVisibility(View.VISIBLE);
        unifiedNativeAdView.setMediaView(mediaView);
        if (unifiedNativeAdView.getMediaView() == null) {
            mediaView.setMediaContent(unifiedNativeAd.getMediaContent());
            try {
                Glide.with(activity).load(unifiedNativeAd.getImages().get(0).getDrawable()).into(imageView2);
            } catch (Exception unused) {
            }
            unifiedNativeAdView.setImageView(imageView2);
            imageView2.setVisibility(View.VISIBLE);
            mediaView.setVisibility(View.GONE);
        }
        TextView textView = (TextView) unifiedNativeAdView.findViewById(R.id.ad_notification_view);
        unifiedNativeAdView.setHeadlineView((TextView) unifiedNativeAdView.findViewById(R.id.ad_headline));
        unifiedNativeAdView.setBodyView((TextView) unifiedNativeAdView.findViewById(R.id.ad_body));
        unifiedNativeAdView.setCallToActionView((TextView) unifiedNativeAdView.findViewById(R.id.ad_call_to_action));
        ((TextView) unifiedNativeAdView.getHeadlineView()).setText(unifiedNativeAd.getHeadline());
        ((TextView) unifiedNativeAdView.getBodyView()).setText(unifiedNativeAd.getBody());
        ((TextView) unifiedNativeAdView.getCallToActionView()).setText(unifiedNativeAd.getCallToAction());
        unifiedNativeAdView.setNativeAd(unifiedNativeAd);
    }

    private void AppLovinNative(Context activity, FrameLayout frameLayout) {
//        MaxNativeAdLoader nativeAdLoader = new MaxNativeAdLoader("YOUR_AD_UNIT_ID", activity);
        MaxNativeAdLoader nativeAdLoader = new MaxNativeAdLoader(SharedHelper.getKey(activity, Livechat_Const.ADMOB_NATIVE_PUB_ID1), activity);
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

                // Add ad view to view.
                frameLayout.removeAllViews();
                frameLayout.addView(nativeAdView);
            }

            @Override
            public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                // We recommend retrying with exponentially higher delays up to a maximum delay
                Log.d("AppLovin", "onNativeAdLoadFailed: ");

                FbNativeBanner(activity, frameLayout);
            }

            @Override
            public void onNativeAdClicked(final MaxAd ad) {
                // Optional click callback
            }
        });

        nativeAdLoader.loadAd(createNativeAdView(activity));
    }

    private MaxNativeAdView createNativeAdView(Context activity) {
        MaxNativeAdViewBinder binder = new MaxNativeAdViewBinder.Builder(R.layout.applovin_native_banner)
                .setTitleTextViewId(R.id.title_text_view)
                .setBodyTextViewId(R.id.body_text_view)
                .setAdvertiserTextViewId(R.id.advertiser_textView)
                .setIconImageViewId(R.id.icon_image_view)
                .setMediaContentViewGroupId(R.id.media_view_container)
                .setOptionsContentViewGroupId(R.id.ad_options_view)
                .setCallToActionButtonId(R.id.cta_button)
                .build();

        return new MaxNativeAdView(binder, activity);
    }

    private void qurekaAds(Context activity, FrameLayout frameLayout){

        View view = LayoutInflater.from(activity).inflate(R.layout.gif_layout1, frameLayout, false);

        GifImageView gifImageView = view.findViewById(R.id.gifImageView);
        try {
            if (SharedHelper.getBoolean(activity, Livechat_Const.ISADSNativeBanQureka)) {
                Glide.with(activity).load(SharedHelper.getKey(activity, Livechat_Const.Square_CardAds1)).into(gifImageView);
                frameLayout.removeAllViews();
                frameLayout.addView(view);

                gifImageView.setOnClickListener(new OnClickListener() {
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

}
