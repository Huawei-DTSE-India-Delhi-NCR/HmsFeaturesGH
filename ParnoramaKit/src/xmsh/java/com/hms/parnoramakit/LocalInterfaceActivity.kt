package com.hms.parnoramakit

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.hms.availabletoalllbraries.BaseActivity
import com.huawei.hms.panorama.PanoramaInterface

class LocalInterfaceActivity: BaseActivity(isBackRequired = true) {

    fun newStartActivity(context: Context){
        val uri =
            Uri.parse("android.resource://" + context.packageName + "/" + R.raw.pano)
        val intent = Intent(context, LocalInterfaceActivity::class.java)
        intent.data = uri
        intent.putExtra("PanoramaType", PanoramaInterface.IMAGE_TYPE_SPHERICAL)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.local_interface)

    }

}