package com.hms.features.dagger.component

import com.hms.features.dagger.module.HmsModule
import com.hms.features.dagger.module.RoomDatabaseModule
import com.hms.features.view.FeaturesListActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RoomDatabaseModule::class, HmsModule::class])
interface HmsComponent {
    fun inject(featuresListActivity: FeaturesListActivity)
}