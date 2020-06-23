package com.hms.locationkit.utils

import android.app.Activity
import android.content.IntentSender
import android.location.Location
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.huawei.hmf.tasks.OnFailureListener
import com.huawei.hmf.tasks.OnSuccessListener
import com.huawei.hms.common.ApiException
import com.huawei.hms.common.ResolvableApiException
import com.huawei.hms.location.*

class CusLocationProvider(var activity: Activity, locationUpdates: LocationUpdates): FusedLocationProviderClient(activity) {

    // the callback of the request
    var mLocationCallback: LocationCallback? = null
    var mLocationRequest: LocationRequest? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var settingsClient: SettingsClient? = null
    var locationUpdates: LocationUpdates

    init {

        this.locationUpdates=locationUpdates

        // create fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
        // create settingsClient
        settingsClient = LocationServices.getSettingsClient(activity)
        mLocationRequest = LocationRequest()
        // Set the interval for location updates, in milliseconds.
        mLocationRequest!!.interval = 10000
        // set the priority of the request
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        if (mLocationCallback==null) {
            mLocationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    if (locationResult != null) {
                        val locations: List<Location> =
                            locationResult.locations
                        if (!locations.isEmpty()) {
                            for (location in locations) {

                                //Processing logic of the Location object.
                                locationUpdates.updateLatLong(latitude = location.latitude, longitude = location.longitude)
                            }
                        }
                    }
                }

                override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                    if (locationAvailability != null) {
                        val flag = locationAvailability.isLocationAvailable
                        /*  LocationLog.i(
                              FragmentActivity.TAG,
                              "onLocationAvailability isLocationAvailable:$flag"
                          )*/
                    }
                }
            }
        }

    }



    fun continuousLocationUpdates() {
        try {
            val builder = LocationSettingsRequest.Builder()
            builder.addLocationRequest(mLocationRequest)
            val locationSettingsRequest = builder.build()
            // check devices settings before request location updates.
            settingsClient?.checkLocationSettings(locationSettingsRequest)
                ?.addOnSuccessListener(OnSuccessListener<LocationSettingsResponse?> {
                    //    Log.i(FragmentActivity.TAG, "check location settings success")
                    // request location updates
                    fusedLocationProviderClient
                        ?.requestLocationUpdates(
                            mLocationRequest,
                            mLocationCallback,
                            Looper.getMainLooper()
                        )
                        ?.addOnSuccessListener(OnSuccessListener<Void?> {
                            Toast.makeText(
                                activity,
                                "requestLocationUpdatesWithCallback onSuccess",
                                Toast.LENGTH_LONG
                            ).show()
                        })
                        ?.addOnFailureListener(OnFailureListener { e ->
                            Toast.makeText(
                                activity,
                                "requestLocationUpdatesWithCallback onFailure:" + e.message,
                                Toast.LENGTH_LONG
                            ).show()
                        })
                })
                ?.addOnFailureListener(OnFailureListener { e ->
                    Toast.makeText(
                        activity,
                        "checkLocationSetting onFailure:" + e.message,
                        Toast.LENGTH_LONG
                    ).show()
                    val statusCode: Int = (e as ApiException).getStatusCode()
                    when (statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            val rae: ResolvableApiException = e as ResolvableApiException
                            rae.startResolutionForResult(activity, 0)
                        } catch (sie: IntentSender.SendIntentException) {
                            Log.e(
                                "Location",
                                "PendingIntent unable to execute request."
                            )
                        }
                    }
                })
        } catch (e: Exception) {
            Log.e("Location", "requestLocationUpdatesWithCallback exception:" + e.message)
        }
    }

    fun stopContinuousLocationUpdates() {
        try {
            fusedLocationProviderClient!!.removeLocationUpdates(mLocationCallback)
                .addOnSuccessListener {
                    Toast.makeText(
                        activity,
                        "removeLocationUpdatesWithCallback onSuccess",
                        Toast.LENGTH_LONG
                    ).show()

                    //location_information.setText("")
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        activity,
                        "removeLocationUpdatesWithCallback onFailure:" + e.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
        } catch (e: java.lang.Exception) {
            Log.e(
                "Location",
                "removeLocationUpdatesWithCallback exception:" + e.message
            )
        }
    }

    fun obtainLastLocation() {
        fusedLocationProviderClient!!.lastLocation
            .addOnSuccessListener(OnSuccessListener { location ->
                if (location == null) {
                    return@OnSuccessListener
                }

                locationUpdates.updateLatLong(latitude = location.latitude, longitude = location.longitude)
            })
            .addOnFailureListener {
                //Exception handling logic.
            }
    }




}