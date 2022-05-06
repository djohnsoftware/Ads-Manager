package com.djohnsoftware.adsmanager.retrofit;

import android.content.Context;
import android.net.Uri;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import com.djohnsoftware.adsmanager.R;

public class Livechat_Const {
    public static String ADMOB_AppID = "admob_appid";
    public static String ADMOB_BANNER_PUB_ID = "admob_banner";
    public static String ADMOB_INTRESTITIAL_AD_PUB_ID1 = "admob_inter1";
    public static String ADMOB_INTRESTITIAL_AD_PUB_ID2 = "admob_inter2";
    public static String ADMOB_INTRESTITIAL_AD_PUB_ID3 = "admob_inter3";
    public static String ADMOB_NATIVE_PUB_ID1 = "admob_native";
    public static String APPOPEN_ID = "admob_openads";

    public static String FB_BANNER_PUB_ID = "fb_banner";
    public static String FB_INTRESTITIAL_AD_PUB_ID1 = "fb_inter1";
    public static String FB_INTRESTITIAL_AD_PUB_ID2 = "fb_inter2";
    public static String FB_NATIVE_PUB_ID1 = "fb_native";
    public static String FB_NATIVE_BANNER_PUB_ID1 = "fb_native_banner";

    public static String QUEREKA_URL = "quereka";
    public static String GAMEZOP_URL = "gamezop";
    public static String Image1 = "image1";
    public static String Banner_Card1 = "banner_card1";
    public static String Banner_Card2 = "banner_card2";
    public static String Banner_Card3 = "banner_card3";
    public static String Big_Card1 = "big_card1";
    public static String Square_Card1 = "square_card1";
    public static String BTN1 = "btn1";
    public static String BTN2 = "btn2";
    public static String BTN3 = "btn3";
    public static String AGORA_ID = "agora_id";
    public static String STARTAPP = "start_app";
    public static String ADCOUNT = "adcount";
    public static String CONTSTARTAPP = "209631156";

    public static String PRIVACY_POLICY = "privacypolicy";
    public static boolean isintertial_loaded = false;
    public static String local_video_url = "";
    public static String ISQUREKA = "qureka_splas_check";

    public static String ISBAN1Qureka = "qureka_banner1_check";
    public static String ISBAN2Qureka = "qureka_banner2_check";
    public static String ISBAN3Qureka = "qureka_banner3_check";
    public static String ISNativeQureka = "qureka_native_check";
    public static String ISNativeBanQureka = "qureka_native_banner_check";

    // TODO: 1/25/2022  By jaysukh
    public static String ISADSQUREKA = "qureka_splas_adds_check";
    public static String ISADSBAN1Qureka = "qureka_banner_adds1_check";
    public static String ISADSBAN2Qureka = "qureka_banner_adds2_check";
    public static String ISADSBAN3Qureka = "qureka_banner_adds3_check";
    public static String ISADSNativeQureka = "qureka_native_adds_check";
    public static String ISADSNativeBanQureka = "qureka_native_banner_adds_check";

    public static String ImageAds1 = "image_adds1";
    public static String Banner_CardAds1 = "bannercard_adds1";
    public static String Banner_CardAds2 = "bannercard_adds2";
    public static String Banner_CardAds3 = "bannercard_adds3";
    public static String Big_CardAds1 = "bigcard_adds_1";
    public static String Square_CardAds1 = "squarecard_adds_1";
    public static String Mediation = "mediation";

    // TODO: 2/26/2022 adx by jaysukh
    public static String ADX_AppID = "adx_app_id";
    public static String ADX_BANNER_PUB_ID = "adx_bannerid";
    public static String ADX_INTRESTITIAL_AD_PUB_ID1 = "adx_inter_1";
    public static String ADX_INTRESTITIAL_AD_PUB_ID2 = "adx_inter_2";
    public static String ADX_INTRESTITIAL_AD_PUB_ID3 = "adx_inter_3";
    public static String ADX_NATIVE_PUB_ID1 = "adx_native";
    public static String ADX_REWARD_PUB_ID1 = "adx_reword";
    public static String ADX_APPOPEN_ID = "adx_openads";

    public static void intentQuereka(Context activity) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(activity, R.color.purple_500));
        CustomTabsIntent build = builder.build();
        build.intent.setPackage("com.android.chrome");
        build.launchUrl(activity, Uri.parse(SharedHelper.getKey(activity, Livechat_Const.QUEREKA_URL)));

    }
 }
