package com.hms.features.dagger.module

import android.app.Application
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hms.features.R
import com.hms.features.database.HmsDatabase
import com.hms.features.database.entity.FeatureEntity
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
class RoomDatabaseModule(application: Application) {

    private var hmsApplication = application
    private lateinit var hmsDatabase: HmsDatabase


    private val databaseCallback = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Log.d("RoomDatabaseModule", "onCreate")
            CoroutineScope(Dispatchers.IO).launch {
                addSampleFeaturesToDatabase()
            }
        }
    }

    private fun addSampleFeaturesToDatabase() {
        var arrarList=hmsApplication.resources.getStringArray(R.array.account_kit)
        val fAccount = FeatureEntity(1,
            arrarList[0].toString(),
            arrarList[1].toString(),
            arrarList[2].toString(),
            arrarList[3].toString())

        arrarList=hmsApplication.resources.getStringArray(R.array.push_kit)
        val fPush = FeatureEntity(2,
            arrarList[0].toString(),
            arrarList[1].toString(),
            arrarList[2].toString(),
            arrarList[3].toString())

        arrarList=hmsApplication.resources.getStringArray(R.array.panorama_kit)
        val fPanorama = FeatureEntity(3,
            arrarList[0].toString(),
            arrarList[1].toString(),
            arrarList[2].toString(),
            arrarList[3].toString())




        arrarList=hmsApplication.resources.getStringArray(R.array.analytics_kit)
        val fAnalytics = FeatureEntity(4,
            arrarList[0].toString(),
            arrarList[1].toString(),
            arrarList[2].toString(),
            arrarList[3].toString())

        arrarList=hmsApplication.resources.getStringArray(R.array.ads_kit)
        val fAds = FeatureEntity(5,
            arrarList[0].toString(),
            arrarList[1].toString(),
            arrarList[2].toString(),
            arrarList[3].toString())



        arrarList=hmsApplication.resources.getStringArray(R.array.map_kit)
        val fMap = FeatureEntity(6,
            arrarList[0].toString(),
            arrarList[1].toString(),
            arrarList[2].toString(),
            arrarList[3].toString())



        arrarList=hmsApplication.resources.getStringArray(R.array.location_kit)
        val fLocation = FeatureEntity(7,
            arrarList[0].toString(),
            arrarList[1].toString(),
            arrarList[2].toString(),
            arrarList[3].toString())


        arrarList=hmsApplication.resources.getStringArray(R.array.ml_kit)
        val fMl = FeatureEntity(8,
            arrarList[0].toString(),
            arrarList[1].toString(),
            arrarList[2].toString(),
            arrarList[3].toString())

        arrarList=hmsApplication.resources.getStringArray(R.array.scan_kit)
        val fScan = FeatureEntity(9,
            arrarList[0].toString(),
            arrarList[1].toString(),
            arrarList[2].toString(),
            arrarList[3].toString())






        val featureDAO = hmsDatabase.getFeatureDAO()
        featureDAO.addFeature(fAccount)
        featureDAO.addFeature(fLocation)
        featureDAO.addFeature(fMap)
        featureDAO.addFeature(fAnalytics)
        featureDAO.addFeature(fAds)
        featureDAO.addFeature(fPush)
        featureDAO.addFeature(fMl)
        featureDAO.addFeature(fPanorama)
        featureDAO.addFeature(fScan)
    }

    @Singleton
    @Provides
    fun providesRoomDatabase(): HmsDatabase {
        hmsDatabase = Room.databaseBuilder(hmsApplication, HmsDatabase::class.java, "hms_database")
            .fallbackToDestructiveMigration()
            .addCallback(databaseCallback)
            .build()
        return hmsDatabase
    }



    @Singleton
    @Provides
    fun providesFeatureDAO(libraryDatabase: HmsDatabase) = libraryDatabase.getFeatureDAO()
}