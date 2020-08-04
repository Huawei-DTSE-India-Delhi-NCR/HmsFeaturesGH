package com.hms.locationkit

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import com.hms.availabletoalllbraries.BaseActivity
import com.hms.availabletoalllbraries.reflections.CallClassMethods
import com.hms.locationkit.utils.LocationConst

class LocationDemoActivity: BaseActivity(true) {

    val TAG = "LocationUpdatesCallback"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.location_demo_activity)
        supportActionBar?.title=getString(R.string.location_kit_features)

        checkPermissions()



    }

    fun basicLocationDetails(view: View)
    {
        CallClassMethods.moveToNewActivity(LocationConst.BasicLocationActivity_PATH, LocationConst.newStartActivity_METHOD, this)
    }

    fun activityLocationDetails(view: View)
    {
        CallClassMethods.moveToNewActivity(LocationConst.LocationActivityService_PATH, LocationConst.newStartActivity_METHOD, this)
    }

    fun GeofenceLocationDetails(view: View)
    {
        CallClassMethods.moveToNewActivity(LocationConst.GeofenceActivity_PATH, LocationConst.newStartActivity_METHOD, this)
    }



    private fun checkPermissions(){

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
