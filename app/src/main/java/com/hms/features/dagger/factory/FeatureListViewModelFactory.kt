package com.hms.features.dagger.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hms.features.repository.FeatureRepository
import com.hms.features.viewmodel.FeatureListViewModel
import javax.inject.Inject

class FeatureListViewModelFactory @Inject constructor(private var featuresRepository: FeatureRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FeatureListViewModel(featuresRepository) as T
    }
}