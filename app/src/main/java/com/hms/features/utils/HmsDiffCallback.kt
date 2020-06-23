package com.hms.features.utils

import androidx.recyclerview.widget.DiffUtil
import com.hms.features.database.entity.FeatureEntity

class HmsDiffCallback(private val oldFeaturesList: List<FeatureEntity>?, private val newFeaturesList: List<FeatureEntity>?) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldFeaturesList?.get(oldItemPosition)?.featureID == newFeaturesList?.get(newItemPosition)?.featureID
    }

    override fun getOldListSize(): Int {
        return oldFeaturesList?.size ?: 0
    }

    override fun getNewListSize(): Int {
        return newFeaturesList?.size ?: 0
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldFeaturesList?.get(oldItemPosition)?.equals(newFeaturesList?.get(newItemPosition))!!
    }
}