package com.codehemu.admobads;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

public class NativeAds{
    private static final String TAG = "Codehemu:NativeAds";
    private static NativeAd mNativeAd = null;

    public static void loadAds(final Activity activity, String nativeAd_ID, NativeTemplateView nativeTemplateView){
        NativeAdView nativeAdView = nativeTemplateView.findViewById(R.id.native_ad_view);
        nativeAdView.setVisibility(GONE);

        if (mNativeAd != null) {
            populateNativeAdView(mNativeAd, nativeTemplateView);
        }

        AdLoader.Builder forNativeAd = new AdLoader.Builder(activity, nativeAd_ID).forNativeAd(nativeAd -> {
            if (activity.isDestroyed()) {
                return;
            }
            NativeAds.mNativeAd = nativeAd;
            NativeAds.populateNativeAdView(NativeAds.mNativeAd, nativeTemplateView);
        }).withAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d(TAG, "NativeAds Loading Complete!");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.d(TAG, "NativeAds Loading Error.. "+ loadAdError.getMessage());
            }
        });
        forNativeAd.build().loadAd(getAdRequest());
    }

    private static boolean adHasOnlyStore(NativeAd nativeAd) {
        String store = nativeAd.getStore();
        String advertiser = nativeAd.getAdvertiser();
        return !TextUtils.isEmpty(store) && TextUtils.isEmpty(advertiser);
    }

    private static void populateNativeAdView(NativeAd nativeAd, NativeTemplateView nativeTemplateView) {
        Log.d(TAG, nativeAd.getAdvertiser() + nativeAd.getPrice() + nativeAd.getStore());

        NativeAdView nativeAdView = nativeTemplateView.findViewById(R.id.native_ad_view);

        TextView primaryView = nativeTemplateView.findViewById(R.id.primary);
        TextView secondaryView = nativeTemplateView.findViewById(R.id.secondary);
        TextView tertiaryView = nativeTemplateView.findViewById(R.id.body);

        RatingBar ratingBar = nativeTemplateView.findViewById(R.id.rating_bar);
        if (ratingBar!=null){
            ratingBar.setEnabled(false);
        }

        Button callToActionView = nativeTemplateView.findViewById(R.id.cta);
        ImageView iconView = nativeTemplateView.findViewById(R.id.icon);
        MediaView mediaView = nativeTemplateView.findViewById(R.id.media_view);

        String store = nativeAd.getStore();
        String advertiser = nativeAd.getAdvertiser();
        String headline = nativeAd.getHeadline();
        String body = nativeAd.getBody();
        String cta = nativeAd.getCallToAction();
        Double starRating = nativeAd.getStarRating();
        NativeAd.Image icon = nativeAd.getIcon();

        String secondaryText;

        nativeAdView.setCallToActionView(callToActionView);
        nativeAdView.setVisibility(VISIBLE);

        nativeAdView.setHeadlineView(primaryView);
        nativeAdView.setMediaView(mediaView);
        secondaryView.setVisibility(VISIBLE);

        if (NativeAds.adHasOnlyStore(nativeAd)) {
            nativeAdView.setStoreView(secondaryView);
            secondaryText = store;
        } else if (!TextUtils.isEmpty(advertiser)) {
            nativeAdView.setAdvertiserView(secondaryView);
            secondaryText = advertiser;
        } else {
            secondaryText = "";
        }

        primaryView.setText(headline);
        callToActionView.setText(cta);


        if (starRating != null && starRating > 0) {
            secondaryView.setVisibility(GONE);
            if (ratingBar!= null){
                ratingBar.setVisibility(VISIBLE);
                ratingBar.setRating(starRating.floatValue());
            }
            nativeAdView.setStarRatingView(ratingBar);
        } else {
            secondaryView.setText(secondaryText);
            secondaryView.setVisibility(VISIBLE);
            if (ratingBar!= null) {
                ratingBar.setVisibility(GONE);
            }
        }

        if (icon != null) {
            iconView.setVisibility(VISIBLE);
            iconView.setImageDrawable(icon.getDrawable());
        } else {
            iconView.setVisibility(GONE);
        }

        if (tertiaryView != null) {
            tertiaryView.setText(body);
            nativeAdView.setBodyView(tertiaryView);
        }

        nativeAdView.setNativeAd(nativeAd);
    }

    private static AdRequest getAdRequest() {
        return new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, new Bundle()).build();
    }

}
