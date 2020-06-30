package com.hms.analyticskit

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.hms.availabletoalllbraries.SingletonHolder

class GAnalytics(var context: Context){

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    companion object{
        lateinit var analyticsInstance : HAnalytics

        fun initialise(context: Context) {
            Log.d("HHH","GOT it GMS")
            analyticsInstance= SingletonHolder<HAnalytics, Context>(::HAnalytics).getInstance(context)
        }
    }

    init {
        firebaseAnalytics= FirebaseAnalytics.getInstance(context)

        // Enable collection capability
        firebaseAnalytics.setAnalyticsCollectionEnabled(true)


    }

    fun logEvent(eventName: String?, bundle: Bundle?) {
        firebaseAnalytics.logEvent(eventName!!, bundle)

    }

    /**
     * Product Add to cart event
     */
    fun logPreDefinedEvent() {
        var bundle= Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID,"12121")
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"Polo Shirt")
        bundle.putString(FirebaseAnalytics.Param.ITEM_BRAND,"Polo")
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY,"Polo KING")
        bundle.putString(FirebaseAnalytics.Param.QUANTITY,"1")
        bundle.putString(FirebaseAnalytics.Param.PRICE,"1231")
        bundle.putString(FirebaseAnalytics.Param.ITEM_VARIANT,"ABCD")

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_CART, bundle)

    }

    /**
     * User custom id
     */
    fun sendUserInfo(){
        firebaseAnalytics.setUserProperty(FirebaseAnalytics.UserProperty.SIGN_UP_METHOD, "12133")
    }


}