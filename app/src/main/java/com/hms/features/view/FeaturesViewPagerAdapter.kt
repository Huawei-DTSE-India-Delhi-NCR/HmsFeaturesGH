package com.hms.features.view

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.hms.features.R
import com.hms.landmarklocations.LandMarkActivity
import com.hms.placesnearby.MapsActivity
import com.hms.placesnearby.NearbyPlacesActivity
import com.hms.placesnearby.SourceDestinationActivity
import com.mlkit.sampletext.util.Constant
import kotlinx.android.synthetic.main.feature_vp_item.view.*

class FeaturesViewPagerAdapter(context: Context,size: Int ) : RecyclerView.Adapter<FeaturesViewPagerAdapter.ViewPagerHolder>()   {

    var list: List<Int> = listOf(1,2)

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
            if(category==1)
                itemView.vp_title.text="Find Places on map"
            else
                itemView.vp_title.text="Choose Image we'll find location on map"

            itemView.setOnClickListener(object: View.OnClickListener {
                override fun onClick(v: View?) {
                    if(category==1) {
                        v!!.context.startActivity(
                            Intent(
                                v.context,
                                SourceDestinationActivity::class.java
                            )
                        )
                    }
                    else
                    {


                      //  Toast.makeText(v!!.context,"Select image from gallery to get landmark",Toast.LENGTH_LONG).show()
                        v!!.context.startActivity(Intent(v!!.context, LandMarkActivity::class.java).apply {
                            putExtra(Constant.MODEL_TYPE, Constant.CLOUD_LANDMARK_DETECTION)
                        })
                    }
                }

            })

        }
    }


}