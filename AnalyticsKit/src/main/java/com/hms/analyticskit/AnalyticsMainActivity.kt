package com.hms.analyticskit

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hms.analyticskit.CommonAnalytics.Companion.analyticsInstance
import com.hms.availabletoalllbraries.BaseActivity
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.analytics.type.HAEventType

class AnalyticsMainActivity : BaseActivity(true) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.analytics_main_activity)

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