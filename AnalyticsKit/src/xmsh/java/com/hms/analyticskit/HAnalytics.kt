package com.hms.analyticskit

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.hms.availabletoalllbraries.SingletonHolder
import com.hms.availabletoalllbraries.utils.Utils
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.analytics.HiAnalyticsInstance
import com.huawei.hms.analytics.HiAnalyticsTools
import com.huawei.hms.analytics.type.HAEventType
import com.huawei.hms.analytics.type.HAParamType

class HAnalytics(){
    var hiAnalyticsInstance: HiAnalyticsInstance

    constructor(context: Context) : this() {
      cContext=context
    }


    companion object{
        lateinit var analyticsInstance : HAnalytics
        lateinit var cContext: Context
        fun initialise(context: Context) {
            cContext=context
            Log.d("HHH","GOT it HMS")
            analyticsInstance=SingletonHolder<HAnalytics, Context>(::HAnalytics).getInstance(context)
        }
    }

    init {
        Log.d("HHH","GOT it Constructor HMS")

        // Enable Analytics Kit Log
        HiAnalyticsTools.enableLog()
       hiAnalyticsInstance = HiAnalytics.getInstance(cContext)

        // Enable collection capability
       hiAnalyticsInstance.setAnalyticsEnabled(true)


       hiAnalyticsInstance.regHmsSvcEvent()
    }

    fun logEvent(eventName: String?, bundle: Bundle?) {
        hiAnalyticsInstance.onEvent(eventName, bundle)
        Utils.showMessage(eventName!!, cContext)

    }

    /**
     * Product Add to cart event
     */
    fun logPreDefinedEvent() {
        var bundle= Bundle()
        bundle.putString(HAParamType.PRODUCTID,"12121")
        bundle.putString(HAParamType.PRODUCTNAME,"Polo Shirt")
        bundle.putString(HAParamType.PRODUCTFEATURE,"Polo")
        bundle.putString(HAParamType.BRAND,"Polo KING")
        bundle.putString(HAParamType.CATEGORY,"Shirts")
        bundle.putString(HAParamType.STORENAME,"Hoyo")
        bundle.putString(HAParamType.QUANTITY,"1")
        bundle.putString(HAParamType.PRICE,"1231")
        bundle.putString(HAParamType.REVENUE,"12")
        bundle.putString(HAParamType.CURRNAME,"ABCD")
        bundle.putString(HAParamType.PLACEID,"19/06/2020")
        hiAnalyticsInstance.onEvent(HAEventType.ADDPRODUCT2CART, bundle)

        Utils.showMessage(
            HAParamType.PRODUCTID + ": " + 12121 + "\n" + HAParamType.PRODUCTNAME + ":" + "Polo Shirt"
                    + "\n" + HAParamType.PRODUCTNAME + ":" + "Polo Shirt"+"\n"+ HAParamType.PRODUCTFEATURE+":"+"Polo"
                    + "\n" + HAParamType.BRAND + ":" + "Polo Shirt"+"\n"+ HAParamType.CATEGORY+":"+"Shirts"
                    + "\n" + HAParamType.STORENAME + ":" + "Hoyo"+"\n"+ HAParamType.QUANTITY+":"+"1"
                    + "\n" + HAParamType.PRICE + ":" + "1231"+"\n"+ HAParamType.REVENUE+":"+"12"
                    + "\n" + HAParamType.CURRNAME + ":" + "ABCD"+"\n"+ HAParamType.PLACEID+":"+"19/06/2020",
                    cContext

        )

    }

    /**
     * User custom id
     */
    fun sendUserInfo(){
        Log.d("HHH","User Details sent")
        hiAnalyticsInstance.setUserProfile(HAParamType.USERGROUPID, "12133")
        Utils.showMessage(HAParamType.USERGROUPID+": "+12133,context = cContext )
    }






}