package com.hms.placesnearby

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Location
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import com.hms.availabletoalllbraries.BaseActivity
import com.huawei.hmf.tasks.OnFailureListener
import com.huawei.hmf.tasks.OnSuccessListener
import com.huawei.hms.common.ApiException
import com.huawei.hms.common.ResolvableApiException
import com.huawei.hms.location.*
import com.huawei.hms.site.api.SearchResultListener
import com.huawei.hms.site.api.SearchService
import com.huawei.hms.site.api.SearchServiceFactory
import com.huawei.hms.site.api.model.*
import java.net.URLEncoder


class NearbyPlacesActivity: BaseActivity(true) {

    var TAG = "HospitalLocationActivity"
    private var longtitude:Double = 12.9698
    private  var latitude:kotlin.Double = 77.7500
    private var distanceID: TextView? =null
    private var queryText: TextView? =null
    private  var yourLatitude:TextView? = null
    private  var yourLongtitude:TextView? = null
    private var searchHospitalImage: AppCompatButton? = null
    private var hospitalDetailsBtn: AppCompatButton?=null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var settingsClient: SettingsClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLocationCallback: LocationCallback? = null
    private var searchService: SearchService? = null
    private var hospitalTitle: ArrayList<String>? = null
    private var hospitalLat: ArrayList<String>? = null
    private var hospitalLong: ArrayList<String>? = null
    private var locationAvailability: LocationAvailability? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hospitals_pick_activity)
        hospitalTitle = ArrayList()
        hospitalLat = ArrayList()
        hospitalLong = ArrayList()
        distanceID = findViewById(R.id.distanceID)
        queryText = findViewById(R.id.queryString)
        yourLatitude = findViewById<TextView>(R.id.yourLatitude)
        yourLongtitude = findViewById<TextView>(R.id.yourLongtitude)
        checkPermissions()
        getLocationData()
        searchHospitalImage = findViewById(R.id.searchHospitalImage)
        hospitalDetailsBtn = findViewById(R.id.hospital_deails)

        searchHospitalImage!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {

                if (distanceID!!.getText().toString() == "") {
                    Toast.makeText(this@NearbyPlacesActivity,
                        "Please enter a value",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {



                    val desiredRadius = distanceID!!.getText().toString()
                        .toInt() * 1000 //Convert the value to the int
                    findHospitalsBySiteKit(desiredRadius,queryText!!.text.toString()) // Use Site Kit

                  //  sendHospitalData()
                    //showHospitalsInMap() // Show on Map Kit
                }
            }
        })

        hospitalDetailsBtn!!.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                sendHospitalData()
            }

        })



    }

    private fun checkPermissions() {
        //Check Permissions
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            Log.i("TAG", "sdk < 28 Q")
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) !== PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) !== PackageManager.PERMISSION_GRANTED
            ) {
                val strings = arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                ActivityCompat.requestPermissions(this, strings, 1)
            }
        } else {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    "android.permission.ACCESS_BACKGROUND_LOCATION"
                ) !== PackageManager.PERMISSION_GRANTED
            ) {
                val strings = arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    "android.permission.ACCESS_BACKGROUND_LOCATION"
                )
                ActivityCompat.requestPermissions(this, strings, 2)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            if (grantResults.size > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                Log.i(TAG, "onRequestPermissionsResult: apply LOCATION PERMISSION successful")
            } else {
                Log.i(TAG, "onRequestPermissionsResult: apply LOCATION PERMISSSION  failed")
            }
        }
        if (requestCode == 2) {
            if (grantResults.size > 2 && grantResults[2] == PackageManager.PERMISSION_GRANTED && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                Log.i(
                    TAG,
                    "onRequestPermissionsResult: apply ACCESS_BACKGROUND_LOCATION successful"
                )
            } else {
                Log.i(TAG, "onRequestPermissionsResult: apply ACCESS_BACKGROUND_LOCATION  failed")
            }
        }
    }



    private fun getLocationData() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this) //create a fusedLocationProviderClient
        settingsClient =
            LocationServices.getSettingsClient(this) //create a settingsClient
        mLocationRequest = LocationRequest()
        mLocationRequest!!.setInterval(3000) // set the interval for location updates, in milliseconds.
        mLocationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY) // set the priority of the request
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null) {
                    val locations: List<Location> = locationResult.getLocations()
                    if (!locations.isEmpty()) {
                        for (location in locations) {
                            longtitude = location.getLongitude() // Get your Latitude
                            latitude = location.getLatitude() // Get your Longitude
                            yourLatitude!!.setText(latitude.toString() + "") // Set your Latitude
                            yourLongtitude!!.setText(longtitude.toString() + "") // Set your Longitude
                        }
                        stopContinuousLocationUpdates()
                    }
                }
            }

            @SuppressLint("LongLogTag")
            override fun onLocationAvailability(locationAvailability: LocationAvailability?) {
                if (locationAvailability != null) {
                    val flag: Boolean = locationAvailability.isLocationAvailable()
                    Log.i(TAG, "onLocationAvailability isLocationAvailable:$flag")
                } else {
                    Log.i(TAG, "onLocationAvailability isLocationAvailable:" + "faulty")
                }
            }
        }
        requestLocationUpdatesWithCallback()
    }

    private fun requestLocationUpdatesWithCallback() {
        try {
            val builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
            builder.addLocationRequest(mLocationRequest)
            val locationSettingsRequest: LocationSettingsRequest = builder.build()
            locationAvailability = LocationAvailability()
            Log.i(TAG, "Location Availability: $locationAvailability")
            Log.i(TAG, "Location Status: " + locationAvailability!!.getLocationStatus())
            Log.i(TAG, "Wifi Status: " + locationAvailability!!.getWifiStatus())
            Log.i(TAG, "Is Location Available: " + locationAvailability!!.isLocationAvailable())

            //check devices settings before request location updates.
            settingsClient!!.checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(object : OnSuccessListener<LocationSettingsResponse?> {
                    override fun onSuccess(locationSettingsResponse: LocationSettingsResponse?) {
                        Log.i(TAG, "check location settings success")
                        //request location updates
                        fusedLocationProviderClient!!.requestLocationUpdates(
                            mLocationRequest,
                            mLocationCallback,
                            Looper.getMainLooper()
                        ).addOnSuccessListener(object : OnSuccessListener<Void?> {
                            @SuppressLint("LongLogTag")
                            override fun onSuccess(aVoid: Void?) {
                                Log.i(TAG, "requestLocationUpdatesWithCallback onSuccess")
                            }
                        })
                            .addOnFailureListener(object : OnFailureListener {
                                override fun onFailure(e: Exception) {
                                    Log.e(
                                        TAG,
                                        "requestLocationUpdatesWithCallback onFailure:" + e.message
                                    )
                                }
                            })
                    }
                })
                .addOnFailureListener(object : OnFailureListener {
                    @SuppressLint("LongLogTag")
                    override fun onFailure(e: Exception) {
                        Log.e(TAG, "checkLocationSetting onFailure:" + e.message)
                        val statusCode: Int = (e as ApiException).getStatusCode()
                        when (statusCode) {
                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                                val rae: ResolvableApiException = e as ResolvableApiException
                                rae.startResolutionForResult(this@NearbyPlacesActivity, 0)
                            } catch (sie: SendIntentException) {
                            }
                        }
                    }
                })
        } catch (e: Exception) {
            Log.e(TAG, "requestLocationUpdatesWithCallback exception:" + e.message)
        }
    }




    private fun findHospitalsBySiteKit(desiredRadius: Int, query: String) {
        searchHospitalImage!!.visibility=View.GONE
        searchService =SearchServiceFactory.create(this, URLEncoder.encode("CV7toecX0FS1JG9Yc9Xck3lZoCt9OyphFVPa9yhlBj5hJPzno5ajoiZGlXHQjAeHk6107nU1LB4LGXL9aPfcEMedgwDI", "utf-8")); //Create searchService Object
//        val location = Coordinate(latitude, longtitude) // User location
        val location = Coordinate(12.9698, 77.7500) // User location
        val nearbySearchRequest =
            NearbySearchRequest() // Create a nearbySearchRequest
        nearbySearchRequest.location = location // Set user location
       // nearbySearchRequest.query = "Hospital" // Set query hospital
        nearbySearchRequest.query = query // Set query hospital
        nearbySearchRequest.pageSize = 10
        nearbySearchRequest.pageIndex=1
        nearbySearchRequest.radius = desiredRadius
       // nearbySearchRequest.poiType = LocationType.BANK
        val resultListener: SearchResultListener<NearbySearchResponse?> =
            object : SearchResultListener<NearbySearchResponse?> {
                // Return search results upon a successful search.
                override fun onSearchResult(results: NearbySearchResponse?) {
                    val sites = results!!.sites
                    if (results == null || results.totalCount <= 0 || sites == null || sites.size <= 0) {
                        return
                    }
                    for (site in sites) {
                        hospitalTitle!!.add(site.name)
                        hospitalLat!!.add(
                            site.location.lat.toString() + ""
                        ) //Converting the values to String
                        hospitalLong!!.add(
                            site.location.lng.toString() + ""
                        ) //Converting the values to String
                    }
                    sendHospitalData()
                }

                // Return the result code and description upon a search exception.
                override fun onSearchError(status: SearchStatus) {
                    Log.i(
                        "TAG",
                        "Error : " + status.errorCode + " " + status.errorMessage
                    )
                    Toast.makeText(
                        this@NearbyPlacesActivity,
                        status.errorCode+" "+status.errorMessage,
                        Toast.LENGTH_LONG
                    ).show()
                    searchHospitalImage!!.visibility=View.VISIBLE
                }
            }
        searchService!!.nearbySearch(nearbySearchRequest, resultListener)
    }

    fun stopContinuousLocationUpdates() {
        try {
            fusedLocationProviderClient!!.removeLocationUpdates(mLocationCallback)
                .addOnSuccessListener {
                    /*Toast.makeText(
                        this@NearbyPlacesActivity,
                        "removeLocationUpdatesWithCallback onSuccess",
                        Toast.LENGTH_LONG
                    ).show()*/

                    //location_information.setText("")
                }
                .addOnFailureListener { e ->
                   /* Toast.makeText(
                        this@NearbyPlacesActivity,
                        "removeLocationUpdatesWithCallback onFailure:" + e.message,
                        Toast.LENGTH_LONG
                    ).show()*/
                }
        } catch (e: java.lang.Exception) {
            Log.e(
                "Location",
                "removeLocationUpdatesWithCallback exception:" + e.message
            )
        }
    }

    private fun sendHospitalData() {
        val intent = Intent(this@NearbyPlacesActivity, NearByHospitalActivity::class.java)
        intent.putExtra("locationClicked", false)
        intent.putExtra("lat", latitude)
        intent.putExtra("long", longtitude)
        intent.putStringArrayListExtra("hospitalTitle", hospitalTitle)
        intent.putStringArrayListExtra("hospitalLat", hospitalLat)
        intent.putStringArrayListExtra("hospitalLong", hospitalLong)
        startActivity(intent)
        //Clear lists for future searchs.
        hospitalTitle!!.clear()
        hospitalLat!!.clear()
        hospitalLong!!.clear()

        searchHospitalImage!!.visibility=View.VISIBLE
    }


}