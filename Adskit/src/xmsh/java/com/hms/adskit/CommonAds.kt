package com.hms.adskit

import android.content.Context
import com.hms.availabletoalllbraries.SingletonHolder
import com.huawei.hms.ads.HwAds

class CommonAds(context: Context) {

    companion object{
        lateinit var commonAds: CommonAds

        fun initialise(context: Context) {
            commonAds= SingletonHolder<CommonAds, Context>(::CommonAds).getInstance(context)
        }
    }

    init {
        // Initialize the HUAWEI Ads SDK.
        HwAds.init(context)
    }

}