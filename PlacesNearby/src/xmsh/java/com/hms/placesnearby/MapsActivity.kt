package com.hms.placesnearby

import android.Manifest
import android.content.Context
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.hms.availabletoalllbraries.BaseActivity
import com.huawei.hmf.tasks.OnFailureListener
import com.huawei.hmf.tasks.OnSuccessListener
import com.huawei.hms.common.ApiException
import com.huawei.hms.common.ResolvableApiException
import com.huawei.hms.location.*
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapView
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.model.LatLng


class MapsActivity: BaseActivity(true), OnMapReadyCallback {

    private val TAG = "MapsActivity"
    //HUAWEI map
    private var hMap: HuaweiMap? = null
    private var mMapView: MapView? = null
    private val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var mLocationRequest: LocationRequest? = null
    lateinit var settingsClient: SettingsClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.map_activity)

        //get mapview instance
        mMapView = findViewById(R.id.mapView)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        settingsClient = LocationServices.getSettingsClient(this)

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

   /* fun checkLocationSettings(){
        val builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
        mLocationRequest = LocationRequest()
        builder.addLocationRequest(mLocationRequest)
        val locationSettingsRequest: LocationSettingsRequest = builder.build()
// Check the device location settings.
// Check the device location settings.
        settingsClient.checkLocationSettings(locationSettingsRequest)
            .addOnSuccessListener(object : OnSuccessListener<LocationSettingsResponse?> {
                override fun onSuccess(locationSettingsResponse: LocationSettingsResponse?) {
                    // Initiate location requests when the location settings meet the requirements.
                    fusedLocationProviderClient!!.requestLocationUpdates(
                            mLocationRequest,
                            mLocationCallback,
                            Looper.getMainLooper()
                        )
                        .addOnSuccessListener(object : OnSuccessListener<Void?>() {
                            override fun onSuccess(aVoid: Void?) {
                                // Processing when the API call is successful.
                            }
                        })
                }
            })
            .addOnFailureListener(object : OnFailureListener {
                override fun onFailure(e: Exception) {
                    // Device location settings do not meet the requirements.
                    val statusCode: Int = (e as ApiException).getStatusCode()
                    when (statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            val rae: ResolvableApiException = e as ResolvableApiException
                            // Call startResolutionForResult to display a pop-up asking the user to enable related permission.
                            rae.startResolutionForResult(this@MapsActivity, 0)
                        } catch (sie: SendIntentException) {
                            //...
                        }
                    }
                }
            })
    }*/

    override fun onMapReady(map: HuaweiMap?) {

        hMap = map?.apply {
            setMyLocationEnabled(true);// Enable the my-location overlay.
            getUiSettings()?.setMyLocationButtonEnabled(true);// Enable the my-location icon.
            isMyLocationEnabled = false
            moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(12.9698, 77.7500), 10f))
        };

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


}