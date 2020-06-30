package com.hms.locationkit.geofence

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.hms.availabletoalllbraries.BaseActivity
import com.hms.locationkit.LocationDemoActivity
import com.hms.locationkit.R
import com.hms.locationkit.utils.CusLocationProvider
import com.hms.locationkit.utils.LocationUpdates
import com.huawei.hmf.tasks.OnCompleteListener
import com.huawei.hmf.tasks.Task
import com.huawei.hms.location.Geofence
import com.huawei.hms.location.GeofenceRequest
import com.huawei.hms.location.GeofenceService
import com.huawei.hms.location.LocationServices
import kotlinx.android.synthetic.main.location_identification.*


class GeofenceActivity: BaseActivity(true)
{


    companion object{

        fun newStartActivity(context: Context){
            context.startActivity(Intent(context, GeofenceActivity::class.java))

        }

    }

    private var geofenceService: GeofenceService? = null
    private var idList: ArrayList<String>? = ArrayList()
    private var geofenceList: ArrayList<Geofence>? = ArrayList()
    private var TAG: String? = GeofenceActivity::class.java.simpleName

    //The way to get the PendingIntent and callback results can be customized. Here we use broadcast as an example.

    private var pendingIntent: PendingIntent? = null

    lateinit var cusLocationProvider: CusLocationProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.location_identification)
        title=getString(R.string.geo_fence)

        geofenceService = LocationServices.getGeofenceService(this)
        pendingIntent = getPendingIntent()

        cusLocationProvider= CusLocationProvider(this,
            object : LocationUpdates {
                override fun updateLatLong(latitude: Double, longitude: Double) {
                    addGeofenceToList(latitude, longitude)
                    requestGeoFenceWithNewIntent()
                }
            })

        status_title.text="GeoFence Details"

        cusLocationProvider.obtainLastLocation()

    }

    /**
     * Create new PendingIntent.
     */
    private fun getPendingIntent(): PendingIntent? {
//The GeoFenceBroadcastReceiver class is a custom static broadcast class. For detailed implementation methods, refer to the sample code.
        val intent = Intent(this, GeoFenceBroadcastReceiver::class.java)
        intent.action = GeoFenceBroadcastReceiver.ACTION_PROCESS_LOCATION
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    /**
     * Send the request to add a geofence.
     */
    fun addGeofenceToList(latitude: Double, longitude: Double){
        geofenceList!!.add(
            Geofence.Builder()
                .setUniqueId("mGeofence")
                .setValidContinueTime(10000) //Incoming latitude and longitude information, radius of circular geofence (unit: meter)
                .setRoundArea(
                    latitude,
                    longitude,
                     5.0f
                ) //Callback when entering or exiting the fence
                .setConversions(Geofence.ENTER_GEOFENCE_CONVERSION or Geofence.EXIT_GEOFENCE_CONVERSION)
                .build()
        )
        idList!!.add("mGeofence")
    }

    /**
     * Delete geofences using an ID list.
     */
    private fun getAddGeofenceRequest(): GeofenceRequest? {
        val builder = GeofenceRequest.Builder()
        //When the user is in the fence, the callback is triggered immediately after adding
        builder.setInitConversions(GeofenceRequest.ENTER_INIT_CONVERSION)
        builder.createGeofenceList(geofenceList)
        return builder.build()
    }

    fun requestGeoFenceWithNewIntent() {
        geofenceService!!.createGeofenceList(getAddGeofenceRequest(), pendingIntent)
            .addOnCompleteListener(object : OnCompleteListener<Void?> {
                override fun onComplete(task: Task<Void?>) {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "add geofence success！")
                    } else {
                        Log.w(TAG, "add geofence failed : " + task.getException().message)
                    }
                }
            })
    }

    /**
     * Delete geofences using an ID list.
     */
    fun removeWithID() {
        geofenceService!!.deleteGeofenceList(idList)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i(TAG, "delete geofence with ID success！")
                } else {
                    Log.w(TAG, "delete geofence with ID failed ")
                }
            }
    }


}