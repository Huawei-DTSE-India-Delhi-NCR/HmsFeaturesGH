package com.hms.locationkit.ai

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import com.hms.availabletoalllbraries.BaseActivity
import com.hms.locationkit.R
import com.hms.locationkit.geofence.GeofenceActivity
import com.huawei.hmf.tasks.OnFailureListener
import com.huawei.hmf.tasks.OnSuccessListener
import com.huawei.hms.location.ActivityConversionInfo
import com.huawei.hms.location.ActivityConversionRequest
import com.huawei.hms.location.ActivityIdentification
import com.huawei.hms.location.ActivityIdentificationService
import kotlinx.android.synthetic.main.location_identification.*


class LocationActivityService: BaseActivity(true) {

    companion object{

        fun newStartActivity(context: Context){
            context.startActivity(Intent(context, LocationActivityService::class.java))

        }

    }

    val TAG= LocationActivityService::class.java.simpleName

    lateinit var activityIdentificationService: ActivityIdentificationService

    lateinit var myLocationBroadcastReceiver: BroadcastReceiver

    //The way to get the PendingIntent and callback results can be customized. Here we use broadcast as an example.
    private var pendingIntent: PendingIntent? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.location_identification)
        title=getString(R.string.activity_identification)

        activityIdentificationService = ActivityIdentification.getService(this)
        pendingIntent = getPendingIntent();


        activityIdentificationService.createActivityIdentificationUpdates(5000, pendingIntent)
            .addOnSuccessListener(object : OnSuccessListener<Void?> {
                override fun onSuccess(aVoid: Void?) {
                    Log.i(TAG, "createActivityIdentificationUpdates onSuccess")
                }
            })
            .addOnFailureListener(object : OnFailureListener {
                override fun onFailure(e: Exception) {
                    Log.e(TAG,
                        "createActivityIdentificationUpdates onFailure:" + e.message
                    )
                }
            })



        val STILL = 103
        val activityConversionInfo1 =
            ActivityConversionInfo(STILL, ActivityConversionInfo.ENTER_ACTIVITY_CONVERSION)
        val activityConversionInfo2 =
            ActivityConversionInfo(STILL, ActivityConversionInfo.EXIT_ACTIVITY_CONVERSION)
        val activityConversionInfos: MutableList<ActivityConversionInfo> =
            ArrayList()
        activityConversionInfos.add(activityConversionInfo1)
        activityConversionInfos.add(activityConversionInfo2)
        val request = ActivityConversionRequest()
        request.activityConversions = activityConversionInfos

    }

    private fun getPendingIntent(): PendingIntent? {
        //The LocationBroadcastReceiver class is a custom class. For detailed implementation methods, please refer to the sample code.

        val filter = IntentFilter()
        /*filter.addAction("service.to.activity.transfer")
        updateUIReciver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                //UI update here
                if (intent != null) Toast.makeText(
                    context,
                    intent.getStringExtra("number").toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        registerReceiver(updateUIReciver, filter)*/

        filter.addAction(MyLocationBroadcastReceiver.ACTION_PROCESS_LOCATION)
        myLocationBroadcastReceiver=object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
              if (intent!=null){
                  if (intent.extras!=null)
                  {
                      if (intent.hasExtra("STR"))
                      {
                          showContent(intent.getStringExtra("STR"))
                      }
                  }
              }

            }
        }

        registerReceiver(myLocationBroadcastReceiver,filter)

        val intent = Intent(this, MyLocationBroadcastReceiver::class.java).apply {
            action =
                MyLocationBroadcastReceiver.ACTION_PROCESS_LOCATION
        }

        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    fun removeIdentification(){
        activityIdentificationService.deleteActivityIdentificationUpdates(pendingIntent)
            .addOnSuccessListener { Log.i(TAG, "deleteActivityIdentificationUpdates onSuccess") }
            .addOnFailureListener { e ->
                Log.e(
                    TAG,
                    "deleteActivityIdentificationUpdates onFailure:" + e.message
                )
            }
    }

    override fun onResume() {
        super.onResume()




    }

    override fun onDestroy() {
        removeIdentification()
        unregisterReceiver(myLocationBroadcastReceiver)
        super.onDestroy()
    }



    open fun showContent(activityStr: String)
    {
        status_details.text=activityStr
    }


}