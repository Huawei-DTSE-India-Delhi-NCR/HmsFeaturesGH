package com.hms.locationkit

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import com.hms.availabletoalllbraries.BaseActivity
import com.hms.locationkit.ai.LocationActivityService
import com.hms.locationkit.basic.BasicLocationActivity
import com.hms.locationkit.geofence.GeofenceActivity

class LocationDemoActivity: BaseActivity(true) {

    val TAG = "LocationUpdatesCallback"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.location_demo_activity)

        checkPermissions()

    }

    fun basicLocationDetails(view: View)
    {
        startActivity(Intent(this,
            BasicLocationActivity::class.java))
    }

    fun activityLocationDetails(view: View)
    {
        startActivity(Intent(this,
            LocationActivityService::class.java))
    }

    fun GeofenceLocationDetails(view: View)
    {
        startActivity(Intent(this,
            GeofenceActivity::class.java))
    }

    fun checkPermissions(){

        val strings = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            "android.permission.ACCESS_BACKGROUND_LOCATION",
            Manifest.permission.ACTIVITY_RECOGNITION,
            "com.huawei.hms.location.ACTION_PROCESS_LOCATION"
        )

        // check location permisiion
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            Log.i(TAG, "sdk < 28 Q");
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, strings, 1);
            }
        } else {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                    "android.permission.ACCESS_BACKGROUND_LOCATION") != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, strings, 2);
            }
        }
    }

}
