package com.hms.features.repository

import androidx.lifecycle.LiveData
import com.hms.features.database.HmsDatabase
import com.hms.features.database.dao.HmsFeatureDAO
import com.hms.features.database.entity.FeatureEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FeatureRepository(hmsDatabase: HmsDatabase) {

    private var featuresDAO: HmsFeatureDAO = hmsDatabase.getFeatureDAO()

    fun getFeatures(): LiveData<List<FeatureEntity>> {
        return featuresDAO.getAllFeatures()
    }

    fun insertFeature(feature: FeatureEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            featuresDAO.addFeature(feature)
        }
    }


    fun updateFeature(feature: FeatureEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            featuresDAO.updateFeature(feature)
        }
    }
}