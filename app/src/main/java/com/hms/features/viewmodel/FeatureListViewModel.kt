package com.hms.features.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hms.features.database.entity.FeatureEntity
import com.hms.features.repository.FeatureRepository

class FeatureListViewModel(private var featuresRepository: FeatureRepository) : ViewModel() {

    var isLoading = MutableLiveData<Boolean>()

    fun getFeaturesList() : LiveData<List<FeatureEntity>> {
        return featuresRepository.getFeatures()
    }


}