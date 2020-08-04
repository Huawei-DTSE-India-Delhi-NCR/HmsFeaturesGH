package com.hms.scankit

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.hms.availabletoalllbraries.BaseActivity
import com.hms.availabletoalllbraries.reflections.CallClassMethods
import com.hms.scankit.utils.ScanConst

class ScanKitDemoActivity: BaseActivity(true) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.scan_activity)
    }

    fun basicScanDemo(view: View)
    {
        //startActivity(Intent(this,BasicScanActivity::class.java))
        CallClassMethods.moveToNewActivity(ScanConst.baseScanActivity_PATH,ScanConst.newStartActivity_METHOD,this)
    }

    fun customScanDemo(view: View)
    {
        //startActivity(Intent(this,BasicScanActivity::class.java))
        CallClassMethods.moveToNewActivity(ScanConst.customScanActivity_PATH,ScanConst.newStartActivity_METHOD,this)
    }

    fun bitmapScanDemo(view: View)
    {
        //startActivity(Intent(this,BasicScanActivity::class.java))
        CallClassMethods.moveToNewActivity(ScanConst.customScanActivity_PATH,ScanConst.newStartActivity_METHOD,this)
    }

    fun multScanDemo(view: View)
    {
        //startActivity(Intent(this,BasicScanActivity::class.java))
      //  CallClassMethods.moveToNewActivity(ScanConst.customMultipleScanActivity_PATH,ScanConst.newStartActivity_METHOD,this)

        CallClassMethods.moveToNewJavaActivity(ScanConst.customMultipleScanActivity_PATH,ScanConst.newStartActivity_METHOD,this)
    }


    fun generateScanDemo(view: View)
    {
        //startActivity(Intent(this,BasicScanActivity::class.java))
        //  CallClassMethods.moveToNewActivity(ScanConst.customMultipleScanActivity_PATH,ScanConst.newStartActivity_METHOD,this)

        CallClassMethods.moveToNewJavaActivity(ScanConst.generateScanActivity_PATH,ScanConst.newStartActivity_METHOD,this)
    }

}