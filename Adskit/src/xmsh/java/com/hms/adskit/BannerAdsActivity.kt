package com.hms.adskit

import android.os.Bundle
import android.widget.RelativeLayout
import com.hms.availabletoalllbraries.BaseActivity
import com.huawei.hms.ads.AdParam
import com.huawei.hms.ads.BannerAdSize
import com.huawei.hms.ads.HwAds
import com.huawei.hms.ads.banner.BannerView
import kotlinx.android.synthetic.xmsh.banner_layout.*


class BannerAdsActivity : BaseActivity(true) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.banner_layout)

        supportActionBar?.title="Banner Ads"


        // Obtain BannerView based on the configuration in layout/ad_fragment.xml.
        val adParam = AdParam.Builder().build()
        hw_banner_view.loadAd(adParam)

        // Call new BannerView(Context context) to create a BannerView class.
        val topBannerView = BannerView(this).apply {
            adId = getString(R.string.banner_ad_id)
            bannerAdSize = BannerAdSize.BANNER_SIZE_SMART
            loadAd(adParam)
        }

        root_view.addView(topBannerView)


    }



}