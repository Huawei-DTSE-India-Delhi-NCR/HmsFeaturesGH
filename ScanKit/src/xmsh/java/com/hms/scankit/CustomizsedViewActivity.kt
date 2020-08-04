package com.hms.scankit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import com.hms.availabletoalllbraries.BaseActivity
import com.huawei.hms.hmsscankit.OnLightVisibleCallBack
import com.huawei.hms.hmsscankit.OnResultCallback
import com.huawei.hms.hmsscankit.RemoteView
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions
import java.io.IOException
import java.lang.Exception


class CustomizsedViewActivity: BaseActivity(true) {

    companion object{
        fun newStartActivity(context: Context){
            val intent = Intent(context, CustomizsedViewActivity::class.java)
            context.startActivity(intent)
        }
    }

    lateinit var frameLayout:FrameLayout
    private var remoteView: RemoteView? = null
    private var backBtn: ImageView? = null
    private var imgBtn: ImageView? = null
    private var flushBtn: ImageView? = null
    private var scanTv:AppCompatTextView?=null
    var mScreenWidth = 0
    var mScreenHeight = 0

    //Declare the key. It is used to obtain the value returned from Scan Kit.
    val SCAN_RESULT = "scanResult"
    val REQUEST_CODE_PHOTO = 0X1113

    //The width and height of scan_view_finder is both 240 dp.
    val SCAN_FRAME_SIZE = 300


    private val img =intArrayOf(R.drawable.flashlight_on, R.drawable.flashlight_off)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.custom_layout)
        // Bind the camera preview screen.
        frameLayout = findViewById<FrameLayout>(R.id.rim)
        scanTv=findViewById(R.id.scan_message)

        //1. Obtain the screen density to calculate the viewfinder's rectangle.
        val dm = resources.displayMetrics
        val density = dm.density
        //2. Obtain the screen size.
      //  mScreenWidth = resources.displayMetrics.widthPixels
      //  mScreenHeight = resources.displayMetrics.heightPixels

        val scanFrameSize = (SCAN_FRAME_SIZE * density)

        //3. Calculate the viewfinder's rectangle, which in the middle of the layout.
        //Set the scanning area. (Optional. Rect can be null. If no settings are specified, it will be located in the middle of the layout.)
        val rect = Rect()
        rect.left = (mScreenWidth / 2 - scanFrameSize / 2).toInt()
        rect.right = (mScreenWidth / 2 + scanFrameSize / 2).toInt()
        rect.top = (mScreenHeight / 2 - scanFrameSize / 2).toInt()
        rect.bottom = (mScreenHeight / 2 + scanFrameSize / 2).toInt()


        //Initialize the RemoteView instance, and set callback for the scanning result.
        remoteView =    RemoteView.Builder().setContext(this).setIsCustomView(true).setBoundingBox(rect).setFormat(HmsScan.ALL_SCAN_TYPE).build()
        // When the light is dim, this API is called back to display the flashlight switch.
        flushBtn = findViewById<ImageView>(R.id.flush_btn)
        remoteView?.setOnLightVisibleCallback(OnLightVisibleCallBack { visible ->
            if (visible) {
                flushBtn?.setVisibility(View.VISIBLE)
            }
        })
        // Subscribe to the scanning result callback event.
        remoteView?.setOnResultCallback(OnResultCallback { result -> //Check the result.
            if (result != null && result.size > 0 && result[0] != null && !TextUtils.isEmpty(
                    result[0].getOriginalValue()
                )
            ) {
                val intent = Intent()
                intent.putExtra(SCAN_RESULT,
                    result[0]
                )

                scanTv?.text=result[0].showResult


                //setResult(Activity.RESULT_OK, intent)
                //this.finish()
            }
        })
        // Load the customized view to the activity.
        remoteView?.onCreate(savedInstanceState)
        /*val params = FrameLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        frameLayout.addView(remoteView, params)*/
        frameLayout.addView(remoteView)
        // Set the back, photo scanning, and flashlight operations.
       // setBackOperation()
        setPictureScanOperation()
        setFlashOperation()

    }

    /**
     * Call the lifecycle management method of the remoteView activity.
     */
    private fun setPictureScanOperation() {
        imgBtn = findViewById(R.id.img_btn)
        imgBtn?.setOnClickListener(View.OnClickListener {
            val pickIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            this.startActivityForResult(
                pickIntent,REQUEST_CODE_PHOTO
            )
        })
    }

    private fun setFlashOperation() {
        flushBtn!!.setOnClickListener {
            if (remoteView!!.lightStatus) {
                remoteView!!.switchLight()
                flushBtn!!.setImageResource(img.get(1))
            } else {
                remoteView!!.switchLight()
                flushBtn!!.setImageResource(img.get(0))
            }
        }
    }



    /**
     * Call the lifecycle management method of the remoteView activity.
     */
    override fun onStart() {
        super.onStart()
        remoteView!!.onStart()
    }

    override fun onResume() {
        super.onResume()
        remoteView!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        remoteView!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        remoteView!!.onDestroy()
    }

    override fun onStop() {
        super.onStop()
        remoteView!!.onStop()
    }

    /**
     * Handle the return results from the album.
     */
    @RequiresApi(Build.VERSION_CODES.P)
    override protected fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_PHOTO) {
            try {
                val bitmap = when {
                    Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(this.contentResolver,data?.data )
                    else -> {
                        val source = ImageDecoder.createSource(this.contentResolver, data?.data!!)
                        ImageDecoder.decodeBitmap(source).copy(Bitmap.Config.RGBA_F16, true)
                    }
                }
                val hmsScans = ScanUtil.decodeWithBitmap(this@CustomizsedViewActivity,bitmap,HmsScanAnalyzerOptions.Creator().setPhotoMode(true).create())

                if (hmsScans != null && hmsScans.size > 0 && hmsScans[0] != null && !TextUtils.isEmpty(hmsScans[0]!!.getOriginalValue())) {
                   scanTv?.text=hmsScans[0].showResult
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


}