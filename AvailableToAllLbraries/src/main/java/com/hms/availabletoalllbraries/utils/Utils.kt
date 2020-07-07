package com.hms.availabletoalllbraries.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

class Utils {


    companion object{

        var ACCESS_TOKEN: String?=null


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


        fun showMessage(message: String, context: Context)
        {
            Toast.makeText(context,message,Toast.LENGTH_LONG).show()
        }




    }

}