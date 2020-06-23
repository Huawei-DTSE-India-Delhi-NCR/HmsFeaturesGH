package com.hms.locationkit.geofence

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.util.Log
import com.huawei.hms.location.GeofenceData


class GeoFenceBroadcastReceiver : BroadcastReceiver(){

    val TAG=GeoFenceBroadcastReceiver::class.java.simpleName

    companion object{
        val ACTION_PROCESS_LOCATION =
            "com.huawei.hmssample.geofence.GeoFenceBroadcastReceiver.ACTION_PROCESS_LOCATION"
    }



    //When detecting a geofence trigger event, the system broadcast a notification to the user through PendingIntent.
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            val action = intent.action
            val sb = StringBuilder()
            val next = "\n"
            if (ACTION_PROCESS_LOCATION == action) {
                val geofenceData = GeofenceData.getDataFromIntent(intent)
                if (geofenceData != null) {
                    val errorCode = geofenceData.errorCode
                    val conversion = geofenceData.conversion
                    val list =
                        geofenceData.convertingGeofenceList
                    val mLocation: Location = geofenceData.convertingLocation
                    val status = geofenceData.isSuccess
                    sb.append("errorcode: $errorCode$next")
                    sb.append("conversion: $conversion$next")
                    for (i in list.indices) {
                        sb.append("geoFence id :" + list[i].uniqueId + next)
                    }
                    sb.append( "location is :" + mLocation.getLongitude()
                            .toString() + " " + mLocation.getLatitude().toString() + next
                    )
                    sb.append("is successful :$status")
                    Log.i(TAG, sb.toString())
                }
            }
        }
    }


}