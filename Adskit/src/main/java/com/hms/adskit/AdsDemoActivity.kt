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
      //  startActivity(Intent(this,BannerAdsActivity::class.java))

    }


}