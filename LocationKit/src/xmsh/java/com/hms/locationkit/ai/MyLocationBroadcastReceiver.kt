package com.hms.locationkit.ai

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.huawei.hms.location.ActivityIdentificationResponse


class MyLocationBroadcastReceiver(): BroadcastReceiver() {


    companion object{
        val ACTION_PROCESS_LOCATION = "com.huawei.hms.location.ACTION_PROCESS_LOCATION"
        var currentValue : String?=null
    }


    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            val action = intent.action
            if (ACTION_PROCESS_LOCATION == action) {

                val activityIdentificationResponse = ActivityIdentificationResponse.getDataFromIntent(intent)

                val list = activityIdentificationResponse.activityIdentificationDatas

                if (list.size>0) {
                    val activityIdentificationData = list.get(0)
                    currentValue = activityIdentificationData.toString()
                        .split("{")[1].replace("}","")
                        .apply { split(",")[0] + "\n" + split(",")[1] }

                    val local = Intent()
                    local.action = ACTION_PROCESS_LOCATION
                    local.putExtra("STR",currentValue)
                    context!!.sendBroadcast(local)

                    Log.d("LIST", activityIdentificationData.toString())

                }
            }
        }
    }


}