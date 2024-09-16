package com.codehemu.admobads;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowMetrics;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

import java.util.UUID;

public class BannerAds {
    private static final String TAG = "Codehemu:BannerAds";
    public static void loadAds(final Activity activity, String ADMOB_BANNER_ADS_UNIT_ID, final FrameLayout frameLayout){
        final AdView adView = new AdView(activity);
        adView.setAdUnitId(ADMOB_BANNER_ADS_UNIT_ID);
        adView.setAdSize(getAdSize(activity, frameLayout));
        Bundle bundle = new Bundle();
        bundle.putString("collapsible_request_id", UUID.randomUUID().toString());
        AdRequest build = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, bundle).build();
        adView.setAdListener(new AdListener() { 
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.d(TAG, "loadBannerAd onAdFailedToLoad :" + loadAdError.getMessage());
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d(TAG, "loadBannerAd onAdLoaded");
                frameLayout.addView(adView);
            }
        });
        adView.loadAd(build);

    }

    private static AdSize getAdSize(Activity activity, FrameLayout frameLayout) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = activity.getWindowManager().getCurrentWindowMetrics();
            Rect bounds = windowMetrics.getBounds();

             float adWidthPixels = frameLayout.getWidth();

            // If the ad hasn't been laid out, default to the full screen width.
            if (adWidthPixels == 0f) {
                adWidthPixels = bounds.width();
            }

            float density = activity.getResources().getDisplayMetrics().density;
            int adWidth = (int) (adWidthPixels / density);

            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);

        }else {
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, 320);
        }

    }

}
