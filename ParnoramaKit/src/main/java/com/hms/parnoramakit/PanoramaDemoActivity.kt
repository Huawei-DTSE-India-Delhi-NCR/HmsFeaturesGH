package com.hms.parnoramakit

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import com.hms.availabletoalllbraries.BaseActivity
import com.hms.availabletoalllbraries.reflections.CallClassMethods
import com.hms.parnoramakit.utils.PanoramaConst

class PanoramaDemoActivity: BaseActivity(isBackRequired = true) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.panorama_demo_activity)
        supportActionBar?.title="Panorama Feature"
        requestPermission()
    }

    fun loadImageGetDetails(view: View){

        CallClassMethods.moveToNewActivity(PanoramaConst.LoadImageGetInfoActivity_PATH, PanoramaConst.newStartActivity_METHOD, this)
    }

    fun loadImageWithoutType(view: View){
        CallClassMethods.moveToNewActivity(PanoramaConst.LoadImageGetInfoActivity_PATH, PanoramaConst.newStartActivity_METHOD2, this)

    }

    fun loadImageLocalInterface(view: View){
        CallClassMethods.moveToNewActivity(PanoramaConst.LoadImageGetInfoActivity_PATH, PanoramaConst.newStartActivity_METHOD3, this)

    }

    fun videoPanoramic(view: View){
        CallClassMethods.moveToNewActivity(PanoramaConst.PanoramicVideoActivity_PATH, PanoramaConst.newStartActivity_METHOD, this)

    }

    private fun requestPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
        } else {
            Log.i("TAG", "permission ok")
        }
    }

}