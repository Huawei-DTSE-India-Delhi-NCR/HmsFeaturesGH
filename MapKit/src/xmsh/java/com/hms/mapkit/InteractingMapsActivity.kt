package com.hms.mapkit

import android.Manifest
import android.content.Context
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

    private val TAG = "InteractingMapsActivity"

    //HUAWEI map
    private var hMap: HuaweiMap? = null

    private var mMapView: MapView? = null

    private val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"

    // Method 1: Increase the camera zoom level by 1 and retain other attribute settings.
   // var cameraUpdate = CameraUpdateFactory.zoomIn()

    // Method 2: Decrease the camera zoom level by 1 and retain other attribute settings.
  //  var cameraUpdate1 = CameraUpdateFactory.zoomOut()

   /* // Method 3: Set the camera zoom level to a specified value and retain other attribute settings.
    var zoom : Float= 8.0f
    var cameraUpdate2 = CameraUpdateFactory.zoomTo(zoom)

    // Method 4: Increase or decrease the camera zoom level by a specified value.
    var amount = 2.0f
    var cameraUpdate3 = CameraUpdateFactory.zoomBy(amount)

    // Method 5: Move the camera to the specified center point and increase or decrease the camera zoom level by a specified value.
    // Set the position of a point on the screen.
    var point: Point = Point(31, 118)
    var amount = 2.0f
    var cameraUpdate4 = CameraUpdateFactory.zoomBy(amount, point)

    // Method 6: Set the latitude and longitude of the camera and retain other attribute settings.
    var latLng1 = LatLng(31.5, 118.9)
    var cameraUpdate5 = CameraUpdateFactory.newLatLng(latLng1)*/

    // Method 7: Set the visible region and padding.
    var padding = 100
    var latLng1 = LatLng(12.9698, 77.7500)
    var latLng2 = LatLng(12.9569, 77.7011)
    var latLngBounds = LatLngBounds(latLng1, latLng2)
   // var cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBounds, padding)
    var zoom = 0.0f

   /* // Method 8: Set the center point and zoom level of the camera.
    var zoom = 0.0f
    var latLng2 = LatLng(32.5, 119.9)
    var cameraUpdate7 = CameraUpdateFactory.newLatLngZoom(latLng2, zoom)*/

    // Method 9: Scroll the camera by specified pixels.
    var x = 100.0f
    var y = 100.0f
  //  var cameraUpdate8 = CameraUpdateFactory.scrollBy(x, y)

    // Method 10: Specify the camera position.
    // Set the tilt.
    var tilt = 2.2f

    // Set the bearing.
    var bearing = 31.5f
    var cameraPosition = CameraPosition(latLng1, zoom, tilt, bearing)
    var cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.hms_map_activity)
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

        // Move the map camera in animation mode.
        hMap?.animateCamera(cameraUpdate);
/*// Move the map camera in animation mode and set the API to be called back when the animation stops.
        hMap?.animateCamera(cameraUpdate, object:HuaweiMap.CancelableCallback{
            override fun onFinish() {

            }

            override fun onCancel() {

            }
        } );
// Move the map camera in animation mode, and set the animation duration and API to be called back when the animation stops.
        hMap?.animateCamera(cameraUpdate, 6, object:HuaweiMap.CancelableCallback{
            override fun onFinish() {

            }

            override fun onCancel() {

            }
        } );*/
// Move the map camera in non-animation mode.
        hMap?.moveCamera(cameraUpdate);

        // Set the preferred minimum zoom level.
        hMap?.setMinZoomPreference(zoom);
// Set the preferred maximum zoom level.
        hMap?.setMaxZoomPreference(1f);
// Reset the maximum and minimum zoom levels.
        hMap?.resetMinMaxZoomPreference();

        // Specify whether to enable the zoom controls.
        hMap?.getUiSettings()?.setZoomControlsEnabled(false);

        // Specify whether to enable the compass.
        hMap?.getUiSettings()?.setCompassEnabled(false);

        // Enable the my-location layer.
        hMap?.setMyLocationEnabled(true);
// Enable the function of displaying the my-location icon.
        hMap?.getUiSettings()?.setMyLocationButtonEnabled(true);

        // Specify whether to enable the scroll gestures.
        hMap?.getUiSettings()?.setScrollGesturesEnabled(true);

        // Specify whether to enable the rotate gestures.
        hMap?.getUiSettings()?.setRotateGesturesEnabled(true);

        hMap!!.setOnMyLocationButtonClickListener {
            Toast.makeText(
                applicationContext,
                "MyLocation button clicked",
                Toast.LENGTH_SHORT
            ).show()
            false
        }

        hMap!!.setOnInfoWindowClickListener { marker ->
            Toast.makeText(
                applicationContext,
                "onInfoWindowClick:" + marker.title,
                Toast.LENGTH_SHORT
            ).show()
        }

        hMap!!.setOnMarkerClickListener { marker ->
            Toast.makeText(
                applicationContext,
                "onMarkerClick:" + marker.title,
                Toast.LENGTH_SHORT
            ).show()
            false
        }
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