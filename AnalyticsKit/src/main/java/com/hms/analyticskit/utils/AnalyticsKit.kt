package com.hms.analyticskit.utils

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.hms.analyticskit.GAnalytics
import com.hms.analyticskit.HAnalytics
import com.hms.availabletoalllbraries.SingletonHolder
import com.hms.availabletoalllbraries.reflections.CallClassMethods
import com.hms.availabletoalllbraries.utils.Utils
import java.lang.Exception
import java.lang.reflect.Constructor
import kotlin.reflect.KClass
import kotlin.reflect.*

class AnalyticsKit(var context: Context) {

        var isHmsAvailability: Boolean = true
        var hAnalyticsClass: Class<Any>?=null
        var gAnalyticsClass: Class<Any>?=null
        var kotlinClass: KClass<*>?=null

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
                // HAnalytics.initialise(context)
                kotlinClass = CallClassMethods.getKotlinClass(AnalyticsConst.HAnalytics_PATH)
            } else{
                kotlinClass = CallClassMethods.getKotlinClass(AnalyticsConst.GAnalytics_PATH)
                isHmsAvailability=false
            }

            CallClassMethods.callCompanionFunction(kotlinClass!!,AnalyticsConst.ANALYTICS_initialise_METHOD)
                .call(CallClassMethods.getCompanionObjectInstance(kotlinClass!!), context)



        }catch (ex: Exception)
        {
            Log.e("CCC",ex.toString())
        }

     //   kotlinClass = CallClassMethods.getKotlinClass(AnalyticsConst.HAnalytics_PATH)
     //   CallClassMethods.callConstructorFunction(kotlinClass!!).call(kotlinClass!!.objectInstance,context)
    }



    fun sendUserInfo(){


        if(isHmsAvailability)
            hAnalyticsClass?.javaClass?.getMethod("sendUserInfo")
     //   HAnalytics.analyticsInstance.sendUserInfo()
        else
            GAnalytics.analyticsInstance.sendUserInfo()
    }

    fun logPreDefinedEvent(){
        if(isHmsAvailability)
            hAnalyticsClass?.javaClass?.getMethod("logPreDefinedEvent")
            //HAnalytics.analyticsInstance.logPreDefinedEvent()
        else
            GAnalytics.analyticsInstance.logPreDefinedEvent()

    }

    fun logEvent(eventName: String?, bundle: Bundle?){
        val paramTypes = arrayOf(
            String::class.java,
            Bundle::class.java
        )
        if(isHmsAvailability) {
           // hAnalyticsClass?.javaClass?.getMethod("logEvent", *paramTypes)?.invoke(hiAnalyticsObj,eventName,bundle)
        }
            //HAnalytics.analyticsInstance.logEvent(eventName,bundle)
        else
            GAnalytics.analyticsInstance.logEvent(eventName,bundle)

    }



}