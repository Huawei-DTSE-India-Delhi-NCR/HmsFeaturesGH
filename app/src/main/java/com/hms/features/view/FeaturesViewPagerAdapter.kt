package com.hms.features.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.hms.features.R
import kotlinx.android.synthetic.main.feature_vp_item.view.*

class FeaturesViewPagerAdapter(context: Context,size: Int ) : RecyclerView.Adapter<FeaturesViewPagerAdapter.ViewPagerHolder>()   {

    var list: List<Int> = listOf(1,2,3)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerHolder {
        return ViewPagerHolder(parent)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewPagerHolder, position: Int) {
        holder.bind(list[position])
    }


    inner class ViewPagerHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView){

        constructor(parent: ViewGroup) :
                this(LayoutInflater.from(parent.context).inflate(R.layout.feature_vp_item, parent, false))
        fun bind(category: Int) {
            itemView.vp_img.setImageDrawable(ResourcesCompat.getDrawable(itemView.resources, R.mipmap.image_3,null))
        }
    }


}