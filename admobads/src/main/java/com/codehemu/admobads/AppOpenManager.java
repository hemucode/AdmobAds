package com.codehemu.admobads;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;

import java.util.Date;

public class AppOpenManager implements Application.ActivityLifecycleCallbacks, DefaultLifecycleObserver {
    private static final String LOG_TAG = "AppOpenManager";
    @SuppressLint("StaticFieldLeak")
    private static Activity currentActivity;
    private final Application myApplication;
    private static String AD_UNIT_ID;
    private static AppOpenAd appOpenAd = null;
    private static long loadTime = 0;
    private static boolean isShowingAd = false;


    public AppOpenManager(Application myApplication, String ADMOB_OPEN_ADS_ID) {
        AD_UNIT_ID = ADMOB_OPEN_ADS_ID;
        this.myApplication = myApplication;
        this.myApplication.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onStart(owner);
        showAdIfAvailable();
        Log.d(LOG_TAG, "onStart");

    }

    public void fetchAd() {
        if (isAdAvailable()) {
            return;
        }
        AppOpenAd.AppOpenAdLoadCallback loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull AppOpenAd appOpenAd2) {
                AppOpenManager.appOpenAd = appOpenAd2;
                AppOpenManager.loadTime = new Date().getTime();
                Log.d(AppOpenManager.LOG_TAG, "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.d(AppOpenManager.LOG_TAG, "onAdFailedToLoad");
            }
        };
        AppOpenAd.load(myApplication, AD_UNIT_ID, getAdRequest(), loadCallback);
    }




    private static boolean isAdAvailable() {
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo();
    }

    private static AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    private static boolean wasLoadTimeLessThanNHoursAgo() {
        return new Date().getTime() - loadTime < 4L * 3600000;
    }

    public void showAdIfAvailable() {
        if (isShowingAd) {
            Log.d(LOG_TAG, "The app open ad is already showing.");
        } else if (!isAdAvailable()) {
            Log.d(LOG_TAG, "The app open ad is not ready yet.");
            //onShowAdCompleteListener.onShowAdComplete();
            fetchAd();
        } else if (!isShowingAd && isAdAvailable()) {
            Log.d(LOG_TAG, "Will show ad.");
            appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    AppOpenManager.appOpenAd = null;
                    AppOpenManager.isShowingAd = false;
                    fetchAd();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {

                }

                @Override
                public void onAdShowedFullScreenContent() {
                    AppOpenManager.isShowingAd = true;
                }
            });

            appOpenAd.show(currentActivity);

        } else {
            Log.d(LOG_TAG, "Can not show ad.");
            fetchAd();
        }
    }


    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        currentActivity = null;
    }
}
