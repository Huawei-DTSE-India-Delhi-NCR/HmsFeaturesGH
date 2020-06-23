package com.hms.features.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hms.features.database.entity.FeatureEntity

@Dao
interface HmsFeatureDAO {
    @Insert
    fun addFeature(feature: FeatureEntity) : Long

    @Update
    fun updateFeature(feature: FeatureEntity)

    @Delete
    fun deleteFeature(feature: FeatureEntity?)

    @Query("SELECT * FROM features")
    fun getAllFeatures() : LiveData<List<FeatureEntity>>


}