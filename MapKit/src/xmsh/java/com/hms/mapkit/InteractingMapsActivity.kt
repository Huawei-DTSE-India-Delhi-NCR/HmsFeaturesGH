package com.hms.mapkit

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import com.hms.availabletoalllbraries.BaseActivity
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapView
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.model.CameraPosition
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.LatLngBounds


class InteractingMapsActivity :BaseActivity(true), OnMapReadyCallback {

    companion object{

        fun newStartActivity(context: Context){
            context.startActivity(Intent(context,InteractingMapsActivity::class.java))

        }

    }

    private val TAG = "InteractingMapsActivity"

    //HUAWEI map
    private var hMap: HuaweiMap? = null

    private var mMapView: MapView? = null

    private val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.hms_map_activity)
        title=getString(R.string.interactive_map)
        //get mapview instance
        mMapView = findViewById(R.id.mapView)

        if(hasPermissions(this)) {
            var mapViewBundle: Bundle? = null
            if (savedInstanceState != null) {
                mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
            }
            mMapView?.onCreate(mapViewBundle)
            //get map instance
            mMapView?.getMapAsync(this)
        }

    }



    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE])
    override fun onMapReady(map: HuaweiMap?) {
        //get map instance in a callback method
        Log.d(TAG, "onMapReady: ");
        hMap = map;


    }

    override fun onStart() {
        super.onStart()
        mMapView!!.onStart()
    }

    override fun onStop() {
        super.onStop()
        mMapView!!.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView!!.onDestroy()
    }

    override fun onPause() {
        mMapView!!.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        mMapView!!.onResume()
    }

    private fun hasPermissions(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i(TAG, "sdk >= 23 M")
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val strings = arrayOf<String>(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                ActivityCompat.requestPermissions(this, strings, 1)
            }
        }
        return true
    }

}