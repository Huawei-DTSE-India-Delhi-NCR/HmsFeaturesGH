package com.hms.features.view

import android.os.Bundle
import android.widget.TextView
import com.hms.availabletoalllbraries.BaseActivity
import com.hms.features.R

lateinit var texView: TextView

class AppLinkDemo: BaseActivity(true) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.applink)




    }

}