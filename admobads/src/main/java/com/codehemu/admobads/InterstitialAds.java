package com.codehemu.admobads;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class InterstitialAds {
    private static final String TAG = "Codehemu:InterstitialAds";
    private static InterstitialAd mInterstitialAd = null;
    private static boolean isLoadingInterstitialAd = false;
    private static boolean isShowingInterstitialAd = false;

    private static boolean isInterstitialAdAvailable(){
        return mInterstitialAd != null;
    }

    public static void loadAds(final Activity activity, String ADMOB_INTERSTITIAL_ADS_ID) {
        if (ADMOB_INTERSTITIAL_ADS_ID.isEmpty()){
            Log.d(TAG, "InterstitialAd unit ID not found...");
            return;
        }

        if (isLoadingInterstitialAd || isInterstitialAdAvailable()) {
            Log.d(TAG, "InterstitialAd is Available, Dot set ad...");
            return;
        }

        isLoadingInterstitialAd = true;
        InterstitialAd.load(activity, ADMOB_INTERSTITIAL_ADS_ID, getAdRequest(), new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                mInterstitialAd = interstitialAd;
                isLoadingInterstitialAd = false;
                Log.d(TAG, "InterstitialAd Loading Complete!");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                isLoadingInterstitialAd = false;
                Log.d(TAG, "InterstitialAd Loading Error.. "+ loadAdError.getMessage());
            }
        });

    }

    private static AdRequest getAdRequest() {
        return new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, new Bundle()).build();
    }

    public static void showInterstitialAd(Activity activity, OnShowAdCompleteListener onShowAdCompleteListener){
        if(isShowingInterstitialAd){
            Log.d(TAG, "InterstitialAd is already Showing!");
            return;
        }

        if (!isInterstitialAdAvailable()){
            onShowAdCompleteListener.onAdClosed();
            return;
        }

        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent();
                isShowingInterstitialAd = false;
                onShowAdCompleteListener.onAdClosed();
                mInterstitialAd = null;
            }

            @Override
            public void onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent();
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                super.onAdFailedToShowFullScreenContent(adError);
                isShowingInterstitialAd = false;
                onShowAdCompleteListener.onAdClosed();
                mInterstitialAd = null;
            }
        });

        isShowingInterstitialAd = true;
        mInterstitialAd.show(activity);
    }


}
