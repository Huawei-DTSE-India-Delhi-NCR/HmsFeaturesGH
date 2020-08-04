package com.hms.adskit

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import com.hms.adskit.R.*
import com.hms.availabletoalllbraries.BaseActivity
import com.huawei.hms.ads.AdParam
import com.huawei.hms.ads.AudioFocusType
import com.huawei.hms.ads.splash.SplashView
import com.huawei.hms.ads.splash.SplashView.SplashAdLoadListener


class SplashAdsActivity : BaseActivity(true) {

    var splashView: SplashView? = null
    private val AD_ID = "testd7c5cewoj6"
    private var hasPaused = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(layout.splash_activity);
        title="Splash"
        splashView = findViewById(R.id.splash_ad_view);
        loadAd();
    }

    fun loadAd()
    {

        val adParam = AdParam.Builder().build()
        val splashAdLoadListener: SplashAdLoadListener = object : SplashAdLoadListener() {
            override fun onAdLoaded() {
                // Called when an ad is loaded successfully.
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                // Called when an ad failed to be loaded. The app home screen is then displayed.
                goToHomeScreenPage()
            }

            override fun onAdDismissed() {
                // Called when the display of an ad is complete. The app home screen is then displayed.
                goToHomeScreenPage()
            }
        }
        val orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        // Obtain SplashView.
        val splashView = findViewById<SplashView>(id.splash_ad_view)
        // A default image if the ad is not loaded properly ...
        splashView.setSloganResId(mipmap.logo1)
        // Set a logo image.
        splashView.setLogoResId(mipmap.logo1)
        // Set logo description.
        splashView.setMediaNameResId(string.ad_id_reward)
        // Set the audio focus preemption policy for a video splash ad.
        splashView.setAudioFocusType(AudioFocusType.NOT_GAIN_AUDIO_FOCUS_WHEN_MUTE)
        // Load the ad. AD_ID indicates the ad slot ID.
        splashView.load(AD_ID, orientation, adParam, splashAdLoadListener)


    }

    private fun goToHomeScreenPage() {
        if (!hasPaused) {
            hasPaused = true
            startActivity(Intent(this, BannerAdsActivity::class.java))
            finish()
        }
    }

    override fun onStop() {
        hasPaused = true
        super.onStop()
    }

    override fun onRestart() {
        super.onRestart()
        hasPaused = false
        goToHomeScreenPage()
    }

    override fun onDestroy() {
        super.onDestroy()
    }



}