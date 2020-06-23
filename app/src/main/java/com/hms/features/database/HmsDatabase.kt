package com.hms.features.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hms.features.database.dao.HmsFeatureDAO
import com.hms.features.database.entity.FeatureEntity

@Database(entities = [FeatureEntity::class], version = 1, exportSchema = false)
abstract class HmsDatabase : RoomDatabase() {

    abstract fun getFeatureDAO(): HmsFeatureDAO
}