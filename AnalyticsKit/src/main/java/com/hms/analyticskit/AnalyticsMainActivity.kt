package com.hms.analyticskit

import android.app.Application
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.hms.analyticskit.utils.AnalyticsKit
import com.hms.availabletoalllbraries.BaseActivity

class AnalyticsMainActivity : BaseActivity(true) {

    lateinit var analyticsInstance: AnalyticsKit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.analytics_main_activity)
        supportActionBar?.title="Analytics Events"

        analyticsInstance= AnalyticsKit.analyticsInstance
    }


    fun userEvents(view:View)
    {
        analyticsInstance.sendUserInfo()
    }


    fun preDefinedEvent(view:View)
    {
        analyticsInstance.logPreDefinedEvent()
    }



    fun sendCustomEvent(view:View)
    {
        val bundle = Bundle()
        bundle.putString("ITEM_ID", "PRODUCT FLAVORS")
        bundle.putString("ITEM_NAME", "Android")
        bundle.putString("CONTENT_TYPE", "HMS")
        analyticsInstance.logEvent("SELECT_ITEM", bundle)

    }


}