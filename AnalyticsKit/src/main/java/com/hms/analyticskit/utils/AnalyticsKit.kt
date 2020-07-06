package com.hms.analyticskit.utils

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.hms.availabletoalllbraries.SingletonHolder
import com.hms.availabletoalllbraries.reflections.CallClassMethods
import com.hms.availabletoalllbraries.utils.Utils
import java.lang.Exception
import kotlin.reflect.KClass

class AnalyticsKit(var context: Context) {

    private var kotlinClass: KClass<*>?=null
    private var kObject:Any?=null

    companion object{
        lateinit var analyticsInstance : AnalyticsKit

        fun initialise(context: Context) {
            Log.d("HHH","GOT it HMS")
            analyticsInstance= SingletonHolder<AnalyticsKit, Context>(::AnalyticsKit).getInstance(context)
        }
    }

    init {

        try {
            if(Utils.isHmsorGms(context))
            {
                kotlinClass = CallClassMethods.getKotlinClass(AnalyticsConst.HAnalytics_PATH)
            } else{
                kotlinClass = CallClassMethods.getKotlinClass(AnalyticsConst.GAnalytics_PATH)
            }

            CallClassMethods.callCompanionFunction(kotlinClass!!,AnalyticsConst.ANALYTICS_initialise_METHOD)
                .call(CallClassMethods.getCompanionObjectInstance(kotlinClass!!), context)

            kObject=CallClassMethods.getObjectInstance(kotlinClass!!)

        }catch (ex: Exception)
        {
            Log.e("ERROR_HMS_F",ex.toString())
        }
    }



    fun sendUserInfo(){
            CallClassMethods.callFunction(kotlinClass!!, "sendUserInfo").call(kObject)
       }

    fun logPreDefinedEvent(){
        CallClassMethods.callFunction(kotlinClass!!, "logPreDefinedEvent").call(kObject)
         }

    fun logEvent(eventName: String?, bundle: Bundle?) {
        CallClassMethods.callFunction(kotlinClass!!, "logEvent").call(kObject, eventName, bundle)
    }

}