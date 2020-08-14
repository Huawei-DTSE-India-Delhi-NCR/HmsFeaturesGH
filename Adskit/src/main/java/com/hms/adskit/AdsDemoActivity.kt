package com.hms.adskit

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.hms.availabletoalllbraries.BaseActivity

class AdsDemoActivity: BaseActivity(true) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.ads_demo_activity)
        supportActionBar?.title=getString(R.string.ads_kit_features)

    }


    fun bannerAdsActivity(view:View)
    {
        startActivity(Intent(this,BannerActivity::class.java))

    }

    fun splashAdsActivity(view:View)
    {
        startActivity(Intent(this,SplashActivity::class.java))

    }

    fun rewardAdsActivity(view:View)
    {
        startActivity(Intent(this,RewardActivity::class.java))

    }


    fun nativeAdsActivity(view:View)
    {
        startActivity(Intent(this,NativeActivity::class.java))

    }


    fun interstitialAdsActivity(view:View)
    {
        startActivity(Intent(this,InterstitialActivity::class.java))

    }





}