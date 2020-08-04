package com.hms.placesnearby

import android.os.Bundle
import android.view.View
import com.hms.availabletoalllbraries.BaseActivity
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapView
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.model.*
import kotlinx.android.synthetic.main.hospitals_pick_activity.*


class NearByHospitalActivity: BaseActivity(true), OnMapReadyCallback {

    val TAG = "MapActivity"
    private var mMapView: MapView? = null
    private var hmap: HuaweiMap? = null
    private var mMarker: Marker? = null
    private var hospitalTitle: ArrayList<String>? = null
    private var hospitalLat: ArrayList<String>? = null
    private var hospitalLong: ArrayList<String>? = null

    private val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
    private var mLatitude = 0.0
    private  var mLongitude:kotlin.Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.hospital_map_activity)

        hospitalLong = ArrayList()
        hospitalLat = ArrayList()
        hospitalTitle = ArrayList()
        getDataFromHospitalLocationActivity()

        mMapView = findViewById<View>(R.id.mapView) as MapView?
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }
        mMapView!!.onCreate(mapViewBundle)
        mMapView!!.getMapAsync(this)



    }

    private fun getDataFromHospitalLocationActivity() {
        mLatitude = intent.extras!!.getDouble("lat")
        mLongitude = intent.extras!!.getDouble("long")
        hospitalTitle = intent.getStringArrayListExtra("hospitalTitle")
        hospitalLat = intent.getStringArrayListExtra("hospitalLat")
        hospitalLong = intent.getStringArrayListExtra("hospitalLong")
    }

    /*
    Enable Ui Settings
     */
    private fun enableUiSettings() {
        hmap?.apply {
            setMyLocationEnabled(true)
            getUiSettings()?.setMyLocationButtonEnabled(true)
            getUiSettings()?.setCompassEnabled(true)
            getUiSettings()?.setZoomControlsEnabled(true)
            getUiSettings()?.setMyLocationButtonEnabled(true)
            setWatermarkEnabled(true)
        }
    }

    override fun onMapReady(huaweiMap: HuaweiMap?) {
        hmap = huaweiMap
        enableUiSettings()
        hmap!!.isMyLocationEnabled = false //Enabling Location

        hmap!!.mapType = HuaweiMap.MAP_TYPE_NORMAL
        hmap!!.setMaxZoomPreference(20f) //Zoom Preferences

        hmap!!.setMinZoomPreference(5f) //Zoom Preferences

        hmap!!.isTrafficEnabled=true


        //Drawing the nearest hospitals on the map
        for (i in hospitalTitle!!.indices) {
           /* val markerOptions = MarkerOptions()
                .position(LatLng(hospitalLat!![i].toDouble(), hospitalLong!![i].toDouble()))
                .title(hospitalTitle!![i])
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.flower))
            hmap?.addMarker(markerOptions)*/


            val options = MarkerOptions()
                .position(LatLng(hospitalLat!![i].toDouble(), hospitalLong!![i].toDouble()))
                .title(hospitalTitle!![i])
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.flower)) // An icon for hospitals
            mMarker = hmap!!.addMarker(options)
            mMarker!!.showInfoWindow()
        }
        hmap!!.setOnMarkerClickListener { false }

        val cameraPosition = CameraPosition.Builder()
            .target(mMarker!!.position) // Sets the center of the map to location user
            .zoom(20f) // Sets the zoom
            .bearing(90f) // Sets the orientation of the camera to east
            .tilt(40f) // Sets the tilt of the camera to 30 degrees
            .build() // Creates a CameraPosition from the builder
        hmap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

    }

}