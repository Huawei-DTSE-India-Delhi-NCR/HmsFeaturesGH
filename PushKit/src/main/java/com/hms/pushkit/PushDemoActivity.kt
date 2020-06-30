package com.hms.pushkit

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.hms.availabletoalllbraries.BaseActivity

class PushDemoActivity:BaseActivity(true) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.push_demo_activity)
        title=getString(R.string.push_kit_features)

    }


    fun sendPushNotification(view:View)
    {

        startActivity(Intent(this,BasicPushActivity::class.java))


    }






}