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

import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class RewardedAds {
    private static final String TAG = "Codehemu:RewardedAds";
    private static RewardedAd mRewardedAd = null;
    private static boolean isLoadingRewardedAd = false;
    private static boolean isShowingRewardedAd = false;

    public static void loadAds(final Activity activity, String ADMOB_REWARDED_ADS_ID) {
        if (ADMOB_REWARDED_ADS_ID.isEmpty()){
            Log.d(TAG, "RewardedAd unit ID not found...");
            return;
        }

        if (isLoadingRewardedAd || isLoadingRewardedAdAvailable()) {
            Log.d(TAG, "RewardedAd is Available, Dot set ad...");
            return;
        }
        isLoadingRewardedAd = true;
        RewardedAd.load(activity, ADMOB_REWARDED_ADS_ID, getAdRequest(), new RewardedAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.d(TAG, "RewardedAd Loading Error.. "+ loadAdError.getMessage());
                isLoadingRewardedAd = false;
            }

            @Override
            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                super.onAdLoaded(rewardedAd);
                mRewardedAd = rewardedAd;
                isLoadingRewardedAd = false;
                Log.d(TAG, "RewardedAd Loading Complete!");

            }
        });
    }

    private static boolean isLoadingRewardedAdAvailable() {
        return mRewardedAd != null;
    }

    private static AdRequest getAdRequest() {
        return new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, new Bundle()).build();
    }

    public static void showRewardedAdAd(Activity activity, OnRewardAdCompleteListener onRewardAdCompleteListener){
        if(isShowingRewardedAd){
            Log.d(TAG, "RewardedAd is already Showing!");
            return;
        }

        if (!isLoadingRewardedAdAvailable()){
            onRewardAdCompleteListener.onAdFailed("Rewarded Ad not Available");
            return;
        }
        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent();
                isShowingRewardedAd =false;
                mRewardedAd = null;
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                super.onAdFailedToShowFullScreenContent(adError);
                onRewardAdCompleteListener.onAdFailed(adError.getMessage());
                isShowingRewardedAd =false;
                mRewardedAd = null;
            }
        });

        isShowingRewardedAd = true;
        mRewardedAd.show(activity, rewardItem -> onRewardAdCompleteListener.onUserEarnedReward());

    }

}
