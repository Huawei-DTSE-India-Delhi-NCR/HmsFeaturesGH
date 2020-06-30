package com.hms.locationkit.basic

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.hms.availabletoalllbraries.BaseActivity
import com.hms.locationkit.R
import com.hms.locationkit.ai.LocationActivityService
import com.hms.locationkit.utils.CusLocationProvider
import com.hms.locationkit.utils.LocationUpdates
import kotlinx.android.synthetic.main.basic_location_activity.*


class BasicLocationActivity: BaseActivity(true),
    LocationUpdates {

    companion object{

        fun newStartActivity(context: Context){
            context.startActivity(Intent(context, BasicLocationActivity::class.java))

        }

    }


   lateinit var latTv: AppCompatTextView
   lateinit var cusLocationProvider: CusLocationProvider


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.basic_location_activity)
        supportActionBar?.title="Location Updates"

        latTv=findViewById(R.id.location_txt_lat)

        cusLocationProvider=
            CusLocationProvider(this, this)
    }



    override fun updateLatLong(latitude: Double, longitude: Double) {
       // TODO("Not yet implemented")

        val lastLocation =" Longitude:${latitude} \n Latitude:${latitude} \n Accuracy:${latitude}"
        location_txt_lat.text = lastLocation
    }

    fun lastLocation(view:View)
    {
        cusLocationProvider.obtainLastLocation()
    }

    fun requestLocation(view:View)
    {
        cusLocationProvider.continuousLocationUpdates()
    }

    fun removeLocation(view:View)
    {
        cusLocationProvider.stopContinuousLocationUpdates()
    }

    fun clearLocationUpdates(view:View)
    {
        cusLocationProvider.stopContinuousLocationUpdates()
       location_txt_lat.text=""
    }




}