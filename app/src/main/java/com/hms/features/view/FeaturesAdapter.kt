package com.hms.features.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hms.features.BR
import com.hms.features.R
import com.hms.features.database.entity.FeatureEntity
import com.hms.features.databinding.FeatureCardItemBinding
import com.hms.features.utils.HmsDiffCallback

class FeaturesAdapter(private var featuresList: List<FeatureEntity>?) : RecyclerView.Adapter<FeaturesAdapter.FeatureViewHolder>() {

    fun updateFeaturesList(newBooksList: List<FeatureEntity>?) {
        val diffResult = DiffUtil.calculateDiff(HmsDiffCallback(featuresList, newBooksList), false)
        featuresList = newBooksList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        return FeatureViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.feature_card_item, parent, false))
    }

    override fun getItemCount(): Int {
        return featuresList?.size ?: 0
    }

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {
        holder.dataBinding.setVariable(BR.feature, featuresList?.get(position))
    }

    class FeatureViewHolder(binding: FeatureCardItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var dataBinding = binding

    }
}