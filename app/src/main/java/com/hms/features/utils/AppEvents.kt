package com.hms.features.utils

import android.os.Bundle
import com.hms.analyticskit.HAnalytics

class AppEvents {

    companion object{

        val bundle=Bundle()

        fun sendEvent(eventName:String, eventsList: Map<String,String>)
        {
            for((key, value) in eventsList){

                bundle.putString("$key","$value")
            }
            HAnalytics.analyticsInstance.logEvent(eventName, bundle)
           // bundle.clear()
        }







    }




}