package com.kmetabus.baidu;


import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

// ca-app-pub-7684178701700291/6497969208
public class MainActivity extends AppCompatActivity  {
    private InterstitialAd mInterstitialAd;
    private WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_main);
        MobileAds.initialize(this, new   OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-9238000306830122/4712259560");

        // Create an ad request.
        AdRequest adRequest = new AdRequest.Builder().build();

        // Start loading the ad.
        adView.loadAd(adRequest);

        myWebView = (WebView) findViewById(R.id.webview);
        // WebView에 WebChromeClient를 설정합니다.
        myWebView.setWebChromeClient(new WebChromeClient() {
            // JavaScript의 alert() 함수를 처리하는 메소드를 재정의(override)합니다.
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                // 여기에서 alert 메시지를 처리할 수 있습니다.
                // 예를 들어, AlertDialog를 표시할 수 있습니다:
                new AlertDialog.Builder(view.getContext())
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> result.confirm())
                        .setCancelable(false)
                        .create()
                        .show();

                // 자체 처리했으므로 true를 반환합니다.
                return true;
            }
        });

        myWebView.loadUrl("https://www.baidu.com/");
        myWebView.getSettings().setJavaScriptEnabled(true);

        InterstitialAd.load(this,"ca-app-pub-9238000306830122/4712259560", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                }); // end
        Button loadAdButton = findViewById(R.id.load_ad_button);
        loadAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(MainActivity.this);
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }
            }
        });

    }



}
