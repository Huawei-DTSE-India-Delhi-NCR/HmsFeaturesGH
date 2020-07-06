package com.hms.mapkit

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import com.hms.availabletoalllbraries.BaseActivity
import com.hms.mapkit.utils.Const
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapView
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.model.*
import java.io.IOException
import java.util.*


class InteractingMapsActivity :BaseActivity(true), OnMapReadyCallback, HuaweiMap.OnMapClickListener, HuaweiMap.OnMarkerClickListener {

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

    private val marker: Marker? = null

    private val latLng = LatLng(Const.LAT, Const.LONG)
    private var currLatLong:LatLng?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.hms_map_activity)
        supportActionBar?.title=getString(R.string.interactive_map)
        //get mapview instance
        mMapView = findViewById(R.id.mapView)

        currLatLong= LatLng(Const.LAT, Const.LONG)

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
        enableUiSettings();
        hMap?.apply {
            setMaxZoomPreference(15f);
            setMinZoomPreference(2f);
        }
        moveMapCameraToPosition()

        addCircleToCurrentLocation();

        hMap?.setOnMapClickListener(this);

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


    override fun onMapClick(latLong: LatLng?) {
        try {
            currLatLong=latLong
            createMarker(currLatLong!!)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
       // TODO("Not yet implemented")
        marker?.showInfoWindow();
        return true
    }


    /*
     Enable Ui Settings
      */
    private fun enableUiSettings() {
        hMap?.apply {
            setMyLocationEnabled(true)
            getUiSettings()?.setMyLocationButtonEnabled(true)
            getUiSettings()?.setCompassEnabled(true)
            getUiSettings()?.setZoomControlsEnabled(true)
            getUiSettings()?.setMyLocationButtonEnabled(true)
            setWatermarkEnabled(true)
        }
    }

    /*
     Create Circle to current location
      */
    private fun addCircleToCurrentLocation() {
        hMap?.addCircle(
            CircleOptions()
                .center(currLatLong)
                .radius(1000.0)
                .strokeWidth(10f)
                .strokeColor(ResourcesCompat.getColor(resources,R.color.colorPrimaryDark,null))
                .fillColor(Color.argb(128, 245, 0, 87))
                .clickable(true)
        )
    }

    /*
     Create Marker when you click on map
      */

    private fun createMarker(latLng: LatLng) {
        val markerOptions = MarkerOptions()
            .position(latLng)
            .snippet("Address : " + featchAddress(latLng))
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.flower))
        hMap?.addMarker(markerOptions)

        moveMapCameraToPosition()

        hMap?.setOnMarkerClickListener(this)
    }

    fun moveMapCameraToPosition()
    {
        val cameraPosition = CameraPosition.Builder()
            .target(currLatLong) // Sets the center of the map to location user
            .zoom(20f) // Sets the zoom
            .bearing(90f) // Sets the orientation of the camera to east
            .tilt(40f) // Sets the tilt of the camera to 30 degrees
            .build() // Creates a CameraPosition from the builder
        hMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    /*
     Convert from latlong to Address
      */
    private fun featchAddress(latLng: LatLng): String {
        val geocoder = Geocoder(this, Locale.ENGLISH)
        val addresses: List<Address> =
            geocoder.getFromLocation(latLng.latitude, latLng.latitude, 1)
            Toast.makeText(
                this, addresses[0]?.getLocality()?.toString() + ", "
                        + addresses[0]?.getAdminArea() + ", "
                        + addresses[0]?.getCountryName(), Toast.LENGTH_SHORT
            ).show()
            return (addresses[0]?.getLocality()?.toString() + ", "
                    + addresses[0]?.getAdminArea() + ", "
                    + addresses[0]?.getCountryName())

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