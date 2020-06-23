package com.hms.features

import android.app.Application
import android.content.Context
import com.hms.analyticskit.CommonAnalytics
import com.hms.features.dagger.component.DaggerHmsComponent
import com.hms.features.dagger.component.HmsComponent
import com.hms.features.dagger.module.RoomDatabaseModule

class HmsApplication : Application() {
    companion object {
        lateinit var instance: HmsApplication
    }
    lateinit var hmsComponent: HmsComponent

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
        CommonAnalytics.initialise(context = this)
    }
}