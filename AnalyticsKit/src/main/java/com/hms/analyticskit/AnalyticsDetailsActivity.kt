package com.hms.analyticskit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.hms.analyticskit.utils.EventType
import com.hms.availabletoalllbraries.BaseActivity
import kotlinx.android.synthetic.main.analytics_details_activity.*

class AnalyticsDetailsActivity : BaseActivity(isBackRequired = true) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.analytics_details_activity)
        title="Analytics Events"

        an_recyclerview.layoutManager = LinearLayoutManager(this)
        //  an_recyclerview.adapter = AnalyticsAdapter()
    }


    fun getData(type: EventType){

        when (type) {
            EventType.AUTOMATIC -> {


            }

            EventType.PRE_DEFINED -> {


            }

            EventType.CUSTOM -> {


            }

        }


    }
}