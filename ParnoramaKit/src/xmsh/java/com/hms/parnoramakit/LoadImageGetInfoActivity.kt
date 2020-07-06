package com.hms.parnoramakit

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.hms.availabletoalllbraries.BaseActivity
import com.huawei.hms.panorama.Panorama
import com.huawei.hms.panorama.PanoramaInterface

class LoadImageGetInfoActivity: BaseActivity(true), View.OnClickListener, View.OnTouchListener {

    companion object{

        fun newStartActivity(context: Context){
            val uri =
                Uri.parse("android.resource://"+context.packageName +"/" + R.raw.pano)
            Panorama.getInstance().loadImageInfoWithPermission(context, uri)
                .setResultCallback(ResultCallbackImpl(context))

        }

        fun newStartActivity2(context: Context){
            val uri = Uri.parse("android.resource://" + context.packageName + "/" + R.raw.pano2)
            Panorama.getInstance().loadImageInfoWithPermission(context, uri, PanoramaInterface.IMAGE_TYPE_SPHERICAL)
                .setResultCallback(ResultCallbackImpl(context))


        }

        fun newStartActivity3(context: Context){
            val uri =
                Uri.parse("android.resource://" + context.packageName + "/" + R.raw.pano)
            val intent = Intent(context, LoadImageGetInfoActivity::class.java)
            intent.data = uri
            intent.putExtra("PanoramaType", PanoramaInterface.IMAGE_TYPE_SPHERICAL)
            context.startActivity(intent)
        }

    }

    private val TAG = "LocalInterfaceActivity"
    private var mLocalInterface: PanoramaInterface.PanoramaLocalInterface? = null

    private var mImageButton: ImageButton? = null

    private var mChangeButtonCompass = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.local_interface)

        Log.d(TAG, "onCreate")
        val intent = intent
        val uri = intent.data
        val type = intent.getIntExtra("PanoramaType", PanoramaInterface.IMAGE_TYPE_SPHERICAL)
        callLocalApi(uri, type)

    }

    private fun callLocalApi(uri: Uri?, type: Int) {
        mLocalInterface = Panorama.getInstance().getLocalInstance(this)
        if (uri == null || mLocalInterface == null) {
            Log.e(TAG, "uri or local api is null")
            finish()
            return
        }
        if (mLocalInterface!!.init() == 0 && mLocalInterface!!.setImage(uri, type) == 0) {
            // getview and add to layout
            val view = mLocalInterface!!.view
            val layout = findViewById<RelativeLayout>(R.id.relativeLayout)
            layout.addView(view)

            // update MotionEvent to sdk
            view.setOnTouchListener(this)

            // change control mode
            mImageButton = findViewById(R.id.changeButton)
            mImageButton!!.bringToFront()
            mImageButton!!.setOnClickListener(this)
        } else {
            Log.e(TAG, "local api error")
            Toast.makeText(this, "Local init error!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onClick(v: View) {
        if (v.id == R.id.changeButton) {
            if (mChangeButtonCompass) {
                mImageButton!!.setImageDrawable(resources.getDrawable(R.drawable.ic_touch))
                mChangeButtonCompass = false
                mLocalInterface!!.setControlMode(PanoramaInterface.CONTROL_TYPE_TOUCH)
                // dynamic change image
                // Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pano);
                // mLocalInterface.setImage(uri, PanoramaApiExt.IMAGE_TYPE_SPHERICAL);
            } else {
                mImageButton!!.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.ic_compass,null))
                mChangeButtonCompass = true
                mLocalInterface!!.setControlMode(PanoramaInterface.CONTROL_TYPE_POSE)
                // dynamic change image
                // Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pano2);
                // mLocalInterface.setImage(uri, PanoramaApiExt.IMAGE_TYPE_SPHERICAL);
            }
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (mLocalInterface != null) {
            mLocalInterface!!.updateTouchEvent(event)
        }
        return true
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        if (mLocalInterface != null) {
            mLocalInterface!!.deInit()
        }
        super.onDestroy()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d(TAG, "onConfigurationChanged")
    }

}