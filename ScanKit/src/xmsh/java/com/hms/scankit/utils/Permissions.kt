package com.hms.scankit.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import androidx.core.app.ActivityCompat




class Permissions {

    companion object{

        val CAMERA_REQ_CODE:Int=111

        fun callCameraPermissions(activity: Activity){
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                CAMERA_REQ_CODE
            )
        }

    }

}