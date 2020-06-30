package com.hms.availabletoalllbraries.utils

import android.content.Context
import android.content.Intent
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

class Utils {


    companion object{


        fun isHmsorGms(context: Context): Boolean
        {
            if(GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context)== ConnectionResult.SUCCESS){
                return false
            }
            return true
        }

        /*fun startFlavorActivity(context: Context,intent: Intent,packageName:String, className:String){

            if(isHmsorGms(context))
            {
                intent.setClassName(packageName,".H"+className )
            }
            else{
                intent.setClassName(packageName,".G"+className)
            }
            context.startActivity(intent)
        }*/






    }

}