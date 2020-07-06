package com.hms.parnoramakit

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.Surface
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.Toast
import com.hms.availabletoalllbraries.BaseActivity
import com.huawei.hms.panorama.Panorama
import com.huawei.hms.panorama.PanoramaInterface
import com.huawei.hms.panorama.PanoramaInterface.PanoramaLocalInterface
import java.io.IOException


class PanoramicVideoActivity: BaseActivity(isBackRequired = true), View.OnClickListener, View.OnTouchListener {

    companion object{
        fun newStartActivity(context: Context){
            val intent = Intent(context, PanoramicVideoActivity::class.java)
            context.startActivity(intent)
        }
    }



    private val LOG_TAG = "PanoramicVideoActivity"
    lateinit var mLocalInstance: PanoramaLocalInterface
    private var mImageButton: ImageButton? = null
    private var mChangeButtonCompass = false
    private var mPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.local_interface)
        val uri: Uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.sample)
        // Obtain the PanoramaLocalInterface object.
        mLocalInstance = Panorama.getInstance().getLocalInstance(this)
        // Check whether the initialization and image settings are successful.
        if (mLocalInstance.init() == 0) {
            val videoSurface: Surface =mLocalInstance.getSurface(PanoramaInterface.IMAGE_TYPE_SPHERICAL)
            if (videoSurface == null) {
                Log.e(LOG_TAG, "videoSurface is null")
                return
            }
            mPlayer = MediaPlayer()
            try {
                mPlayer!!.setDataSource(applicationContext, uri)
            } catch (e: IOException) {
                e.printStackTrace()
                return
            }
            mPlayer?.setLooping(true)
            mPlayer?.setSurface(videoSurface)
            try {
                mPlayer?.prepare()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "Media Player prepare exception")
            }
            mPlayer?.start()
            // Obtain the view and add it to the layout.
            val layout: RelativeLayout = findViewById(R.id.relativeLayout)
            val view = mLocalInstance?.getView()
            layout.addView(view)
            // Update the touch event to the SDK.
            view.setOnTouchListener(this)
            // Change the control mode.
            mImageButton = findViewById(R.id.changeButton)
            mImageButton?.bringToFront()
            mImageButton?.setOnClickListener(this)
        } else {
            Log.e(LOG_TAG, "local api error")
            Toast.makeText(this, "Local init error!", Toast.LENGTH_LONG).show()
        }

    }

    override fun onClick(v: View) {
        if (v.id == R.id.changeButton) {
            if (mChangeButtonCompass) {
                mImageButton!!.setImageDrawable(resources.getDrawable(R.drawable.ic_touch))
                mChangeButtonCompass = false
                mLocalInstance!!.setControlMode(PanoramaInterface.CONTROL_TYPE_TOUCH)
            } else {
                mImageButton!!.setImageDrawable(resources.getDrawable(R.drawable.ic_compass))
                mChangeButtonCompass = true
                mLocalInstance!!.setControlMode(PanoramaInterface.CONTROL_TYPE_POSE)
            }
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (mLocalInstance != null) {
            mLocalInstance!!.updateTouchEvent(event)
        }
        return true
    }

    override fun onDestroy() {
        if (mLocalInstance != null) {
            mLocalInstance!!.deInit()
        }
        super.onDestroy()
    }

}