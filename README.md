[![Version](https://img.shields.io/badge/version-1.0.1-green.svg)](https://shields.io/)
[![Open Source Love svg1](https://badges.frapsoft.com/os/v1/open-source.svg?v=103)](https://github.com/ellerbrock/open-source-badges/)
<img src="https://komarev.com/ghpvc/?username=codehemu&label=Visitors%20&color=0e75b6&style=flat" alt="codehemu" />
<a href="https://rzp.io/i/"> <img src="https://img.shields.io/badge/Donate-Razorpay-green" /></a>

# AdmobAds
 Implement admob ads in your android app.


# How to Implement Module
To get a Git project into your build:
> Step 1. download admobads folder and import module
<img src="https://raw.githubusercontent.com/hemucode/AdmobAds/main/images/import.png" width="100%"  alt="DEMO"/>

> Step 2. Add the dependency

Add it in your root app.gradle at the end of repositories: <br/>
```gradle
dependencies {
	...
	    implementation project(':admobads')
	...
}
```
<br/>

# How do I use Admob Interstitial Ads
Simple use cases will look something like this:
> Step 1. First load the interstitial ads <br/>
```java
AdmobAds.loadInterstitialAd(context, "ADMOB_INTERSTITIAL_ADS_ID");
```

> Step 2. Set the FullScreenContentCallback
```java
AdmobAds.showInterstitialAdIfAvailable(context , new OnShowAdCompleteListener() {
    @Override
    public void onAdClosed() {
       // Called when ad is dismissed.
    }
});

```
> Example. You can also set the state of the interstitial add a timer like if you want to switch from one activity to another activity after 5 seconds then you can call like this. add 
```xml
 ...
<!-- Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713 -->
    <meta-data
        android:name="com.google.android.gms.ads.APPLICATION_ID"
        android:value="ca-app-pub-3940256099942544~3347511713" />
</application>
```

```java
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //First load the interstitial ads
        AdmobAds.loadInterstitialAd(this, "ca-app-pub-3940256099942544/1033173712");

        //Timer 5 second like 5000
        int seconds = 5000;
        AdmobAds.showInterstitialAdIfAvailable(this , seconds,  new OnShowAdCompleteListener() {
            @Override
            public void onAdClosed() {
                // Called when ad is dismissed.
                Toast.makeText(MainActivity.this, "Ad Closed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
```

<br/>

# How do I use Admob Rewarded Ads
Simple use cases will look something like this:
> Step 1. First load the rewarded ads <br/>
```java
AdmobAds.loadRewardedAd(context, "ADMOB_REWARDED_ADS_UNIT_ID");

```

> Step 2. Set the FullScreenContentCallback
```java
AdmobAds.showRewardedAdIfAvailable(context, new OnRewardAdCompleteListener() {
    @Override
    public void onUserEarnedReward() {
        //The user earned the reward.
    }

    @Override
    public void onAdFailed(String error) {
    	//The rewarded ad wasn't ready yet."
    }
});
```

> Example. +5 Coin Reward. If you click on the reward button, you will get 5 coin, for that, first we will take a button in the layout, we will give the ID reward, then we will write

```java
public class MainActivity extends AppCompatActivity {
    int coin = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //First load the Rewarded ads
        AdmobAds.loadRewardedAd(this,"ca-app-pub-3940256099942544/5224354917");

        Button button = findViewById(R.id.rewarded);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdmobAds.showRewardedAdIfAvailable(MainActivity.this, 
                	new OnRewardAdCompleteListener() {
                    @Override
                    public void onUserEarnedReward() {
                        coin += 5;
                        Toast.makeText(MainActivity.this, 
                        	coin + " Coin", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAdFailed(String error) {

                    }
                });
            }
        });
    }
}
```
<br/>

# How do I use Admob Native Ads
Simple use cases will look something like this:
> Step 1. First, you need to include the template as part of your layout.
```xml
<androidx.constraintlayout.widget.ConstraintLayout 
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <!--  This is your template view -->
    <!-- this attribute determines which template is used. 
     The other option is @layout/small_template_view -->
    <com.codehemu.admobads.NativeTemplateView
        android:id="@+id/my_template"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:codehemu_template_type="@layout/small_template_view"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent" />

   ...
</androidx.constraintlayout.widget.ConstraintLayout>
```


> Step 2. load the native ads <br/>
```java
NativeTemplateView nativeTemplateView = findViewById(R.id.my_template);
AdmobAds.initialize(context);
AdmobAds.loadNativeAd(context,"ADMOB_NATIVE_ADS_ID", nativeTemplateView);
```

# Native Ads Template sizes
There are two templates: small and medium. They both use the TemplateView class and both have a fixed aspect ratio. They will scale to fill the width of their parent views. <br/>

> Small template.
```xml
  @layout/small_template_view
```
The small template is ideal for recycler views, or any time you need a long rectangular ad view. For instance you could use it for in-feed ads. <br/>

<img src="https://raw.githubusercontent.com/hemucode/AdmobAds/main/images/small_template.png" width="250"  alt="DEMO"/>

```xml
  @layout/no_rating_bar_small_template_view
```
The small template is ideal for recycler views with no rating bar. <br/>

<img src="https://raw.githubusercontent.com/hemucode/AdmobAds/main/images/no_rating_bar_small_template_view.jpg" width="250"  alt="DEMO"/>


> Medium template.
```xml
  @layout/medium_template_view
```
The medium template is meant to be a one-half to three-quarter page view but can also be used in feeds. It is good for landing pages or splash pages. <br/>

<img src="https://raw.githubusercontent.com/hemucode/AdmobAds/main/images/medium_template.png" width="250"  alt="DEMO"/>


```xml
  @layout/no_rating_bar_medium_template_view
```
The medium template is meant with no rating bar. <br/>

<img src="https://raw.githubusercontent.com/hemucode/AdmobAds/main/images/no_rating_bar_mediam_template_view.jpg" width="250"  alt="DEMO"/>

<br/>


# How do I use Admob open Ads
Simple use cases will look something like this:
> Step 1. Create a class like MyApplication.class <br/>
```java
AdmobAds.initialize(context, "TEST_DEVICE_HASHED_ID");
new AppOpenManager(context, "ADMOB_OPEN_ADS_ID");
```
> Step 2. Update menifest
```xml
<application 
	android:name=".MyApplication"
	...
>
```
> Example open ads MyApplication.class  <br/>
```java
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AdmobAds.initialize(this,"DD4FB2BBB90E8B51");
        new AppOpenManager(this,"ca-app-pub-3940256099942544/9257395921");
    }
}

```

<br/>

# How do I use Admob Banner Ads
Simple use cases will look something like this:
> Step 1. First, you need to include the frame as part of your layout.

```xml
<androidx.constraintlayout.widget.ConstraintLayout 
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <!--  This is your frame view -->
   <FrameLayout
    android:id="@+id/my_frame"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintStart_toStartOf="parent" />

   ...
</androidx.constraintlayout.widget.ConstraintLayout>
```


> Step 2. load the banner ads <br/>
```java
FrameLayout frameLayout = findViewById(R.id.my_frame);
AdmobAds.loadBannerAds(context, "ADMOB_BANNER_ADS_UNIT_ID", frameLayout);
```