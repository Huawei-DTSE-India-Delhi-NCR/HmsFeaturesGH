package com.hms.features.dagger.module

import com.hms.features.database.HmsDatabase
import com.hms.features.repository.FeatureRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class HmsModule {

    @Singleton
    @Provides
    fun providesFeaturesRepository(hmsDatabase: HmsDatabase): FeatureRepository {
        return FeatureRepository(hmsDatabase)
    }


}