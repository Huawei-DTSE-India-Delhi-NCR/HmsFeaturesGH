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
        Toast.makeText(this,"User Info Event is sent",Toast.LENGTH_LONG).show()
    }


    fun preDefinedEvent(view:View)
    {
        analyticsInstance.logPreDefinedEvent()
        Toast.makeText(this,"Pre Defined Event is sent",Toast.LENGTH_LONG).show()
    }



    fun sendCustomEvent(view:View)
    {
        val bundle = Bundle()
        bundle.putString("ITEM_ID", "PRODUCT FLAVORS")
        bundle.putString("ITEM_NAME", "Android")
        bundle.putString("CONTENT_TYPE", "HMS")
        analyticsInstance.logEvent("SELECT_ITEM", bundle)

        Toast.makeText(this,"Custom Event is sent",Toast.LENGTH_LONG).show()
    }


}