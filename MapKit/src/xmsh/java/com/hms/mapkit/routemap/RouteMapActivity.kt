package com.hms.mapkit.routemap

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.hms.availabletoalllbraries.BaseActivity
import com.hms.mapkit.InteractingMapsActivity
import com.hms.mapkit.R
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.SupportMapFragment
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.MarkerOptions

class RouteMapActivity: BaseActivity(true) {

    companion object{

        fun newStartActivity(context: Context){
            context.startActivity(Intent(context, RouteMapActivity::class.java))

        }

    }

    private val TORONTO = LatLng(43.6532, -79.3832)
    private val MONTREAL = LatLng(45.5017, -73.5673)

    private lateinit var map: HuaweiMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.hms_map_activity)

        (supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment).getMapAsync {
            map = it
            setMarkers()
        }
    }


    private fun setMarkers() {

        val startMarker = MarkerOptions().position(TORONTO)
        map.addMarker(startMarker)


        val endMarker = MarkerOptions().position(MONTREAL)
        map.addMarker(endMarker)



        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(TORONTO, 5f)
        map.moveCamera(cameraUpdate)

    }

}