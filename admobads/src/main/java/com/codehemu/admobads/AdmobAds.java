package com.codehemu.admobads;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;

import java.util.Collections;

public class AdmobAds {

    public static void initialize(Context context) {
        // Initialize the Google Mobile Ads SDK on a background thread.
        MobileAds.initialize(context);
    }

    public static void initialize(Context context, String TEST_DEVICE_HASHED_ID) {
        MobileAds.setRequestConfiguration(new RequestConfiguration.Builder().setTestDeviceIds(Collections.singletonList(TEST_DEVICE_HASHED_ID)).build());
        // Initialize the Google Mobile Ads SDK on a background thread.
        MobileAds.initialize(context);
    }

    public static void loadRewardedAd(final Activity activity, String ADMOB_REWARDED_ADS_UNIT_ID){
        RewardedAds.loadAds(activity,ADMOB_REWARDED_ADS_UNIT_ID);
    }

    public static void showRewardedAdIfAvailable(Activity activity, OnRewardAdCompleteListener onRewardAdCompleteListener){
        RewardedAds.showRewardedAdAd(activity,onRewardAdCompleteListener);
    }

    public static void loadInterstitialAd(final Activity activity, String ADMOB_INTERSTITIAL_ADS_ID){
        InterstitialAds.loadAds(activity,ADMOB_INTERSTITIAL_ADS_ID);
    }

    public static void showInterstitialAdIfAvailable(Activity activity, OnShowAdCompleteListener onShowAdCompleteListener){
        InterstitialAds.showInterstitialAd(activity, onShowAdCompleteListener);
    }

    public static void showInterstitialAdIfAvailable(Activity activity, Integer countDownSecond, OnShowAdCompleteListener onShowAdCompleteListener){
        CountDownTimer countDownTimer = new CountDownTimer(countDownSecond ,1000) {
            @Override
            public void onTick(long l) {}
            @Override
            public void onFinish() {
                InterstitialAds.showInterstitialAd(activity, onShowAdCompleteListener);
            }
        };
        countDownTimer.start();
    }

    public static void loadNativeAd(final Activity activity, String ADMOB_NATIVE_ADS_UNIT_ID, NativeTemplateView nativeTemplateView){
        NativeAds.loadAds(activity,ADMOB_NATIVE_ADS_UNIT_ID,nativeTemplateView);
    }

    public static void loadBannerAds(final Activity activity, String ADMOB_BANNER_ADS_UNIT_ID, final FrameLayout frameLayout){
       BannerAds.loadAds(activity,ADMOB_BANNER_ADS_UNIT_ID,frameLayout);
    }

}
