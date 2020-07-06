package com.hms.parnoramakit

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.huawei.hms.panorama.PanoramaInterface
import com.huawei.hms.support.api.client.ResultCallback

open class ResultCallbackImpl(mContext: Context):
    ResultCallback<PanoramaInterface.ImageInfoResult?> {

    val contextV = mContext

    override fun onResult(result: PanoramaInterface.ImageInfoResult?) {
        if (result == null) {
            Log.e("TAG", "ImageInfoResult is null")
            return
        }
        // if result is ok,start Activity
        if (result.status.isSuccess) {
            val intent = result.imageDisplayIntent
            ContextCompat.startActivity(contextV, intent, null)
            Toast.makeText(contextV,result.imageDisplayIntent.dataString?.toString(),Toast.LENGTH_LONG).show()
            // intent?.let { startActivity(,intent,null) } ?: Log.e("TAG", "unknown error, intent is null")
        } else {
            Log.e("TAG", "error status : " + result.status)
            Toast.makeText(contextV,"Allow permissions",Toast.LENGTH_LONG).show()
        }
    }
}