package com.hms.features

import android.app.Application
import com.hms.adskit.CommonAds
import com.hms.analyticskit.HAnalytics
import com.hms.analyticskit.utils.AnalyticsKit
import com.hms.features.dagger.component.DaggerHmsComponent
import com.hms.features.dagger.component.HmsComponent
import com.hms.features.dagger.module.RoomDatabaseModule

class HmsApplication : Application() {
    companion object {
        lateinit var instance: HmsApplication
    }
    lateinit var hmsComponent: HmsComponent
    lateinit var analyticsKit: AnalyticsKit

    override fun onCreate() {
        super.onCreate()
        instance = this

        hmsComponent = DaggerHmsComponent
            .builder()
            .roomDatabaseModule(RoomDatabaseModule(this))
            .build()

        initialiseLibraries()


    }


    private fun initialiseLibraries()
    {
        AnalyticsKit.initialise(this)
        CommonAds.initialise(this)
    }
}