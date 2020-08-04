package com.hms.landmarklocations

import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.hms.landmarklocations.utils.LandmarkTransactor
import com.hms.placesnearby.NearByHospitalActivity
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.hms.mlsdk.common.MLApplication
import com.mlkit.sampletext.activity.dialog.AddPictureDialog
import com.mlkit.sampletext.transactor.ImageTransactor
import com.mlkit.sampletext.transactor.RemoteImageClassificationTransactor
import com.mlkit.sampletext.util.BitmapUtils
import com.mlkit.sampletext.util.Constant
import com.mlkit.sampletext.views.overlay.GraphicOverlay
import java.lang.ref.WeakReference

class LandMarkActivity:com.mlkit.sampletext.activity.BaseActivity(), View.OnClickListener {

    val TAG: String= "LandMarkActivity"

    private val KEY_IMAGE_URI = "KEY_IMAGE_URI"
    private val KEY_IMAGE_MAX_WIDTH = "KEY_IMAGE_MAX_WIDTH"
    private val KEY_IMAGE_MAX_HEIGHT = "KEY_IMAGE_MAX_HEIGHT"

    private val REQUEST_TAKE_PHOTO = 1
    private val REQUEST_SELECT_IMAGE = 2

    private val TIMEOUT = 20 * 1000
    private val DELAY_TIME = 600
    private var getImageButton: Button? = null
    private var preview: ImageView? = null
    private var title: TextView? = null
    private var graphicOverlay: GraphicOverlay? = null
    private var selectedMode =
        Constant.CLOUD_IMAGE_CLASSIFICATION

    var isLandScape = false

    private var imageUri: Uri? = null
    private var maxWidthOfImage: Int? = null
    private var maxHeightOfImage: Int? = null
    private var imageTransactor: ImageTransactor? = null

    private var imageBitmap: Bitmap? = null

    private var progressDialog: Dialog? = null

    private var addPictureDialog: AddPictureDialog? = null

    private val mHandler: Handler = MsgHandler(this@LandMarkActivity)

    interface GetLocationDetails{

        fun getLocationTitles(titlesList:ArrayList<String>)

        fun getLocationLat(latList:ArrayList<String>)

        fun getLocationLong(longList:ArrayList<String>)



    }

    private var landmarkTitle: ArrayList<String>? = null
    private var landmarkLat: ArrayList<String>? = null
    private var landmarkLong: ArrayList<String>? = null

    private class MsgHandler(mainActivity: LandMarkActivity) :
        Handler() {
        var mMainActivityWeakReference: WeakReference<LandMarkActivity>
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val mainActivity = mMainActivityWeakReference.get() ?: return
            Log.d("LandMarkActivity", "msg what :" + msg.what)
            if (msg.what == Constant.GET_DATA_SUCCESS) {
                mainActivity.handleGetDataSuccess()
            } else if (msg.what == Constant.GET_DATA_FAILED) {
                mainActivity.handleGetDataFailed()
            }
        }

        init {
            mMainActivityWeakReference =
                WeakReference(mainActivity)
        }
    }

    private fun handleGetDataSuccess() {
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
        mHandler.removeCallbacks(myRunnable)

        sendHospitalData()
    }

    private fun handleGetDataFailed() {
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
        mHandler.removeCallbacks(myRunnable)
        Toast.makeText(this, this.getString(R.string.get_data_failed), Toast.LENGTH_SHORT).show()
    }

    private val myRunnable = Runnable {
        if (this@LandMarkActivity.progressDialog != null) {
            this@LandMarkActivity.progressDialog!!.dismiss()
        }
        Toast.makeText(
            this@LandMarkActivity.getApplicationContext(),
            this@LandMarkActivity.getString(R.string.get_data_failed),
            Toast.LENGTH_SHORT
        ).show()
    }

    private val detectRunnable =
        Runnable { this@LandMarkActivity.loadImageAndSetTransactor() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = this.intent
        var type: String? = null
        try {
            selectedMode =
                intent.getStringExtra(Constant.MODEL_TYPE)
            type = intent.getStringExtra(Constant.ADD_PICTURE_TYPE)
        } catch (e: RuntimeException) {
            Log.e(TAG, "Get intent value failed: " + e.message)
        }
        if (savedInstanceState != null) {
            imageUri =
                savedInstanceState.getParcelable(KEY_IMAGE_URI)
            if (imageUri != null) {
                maxWidthOfImage =
                    savedInstanceState.getInt(KEY_IMAGE_MAX_WIDTH)
                maxHeightOfImage =
                    savedInstanceState.getInt(KEY_IMAGE_MAX_HEIGHT)
            }
        }
        this.setContentView(R.layout.hms_lank_mark_layout)
        supportActionBar?.title="Find Landmark Location"
        supportActionBar?.setHomeButtonEnabled(true)
        initTitle()
        findViewById<View>(R.id.back).setOnClickListener(this)
        preview = findViewById(R.id.still_preview)
        graphicOverlay = findViewById(R.id.still_overlay)
        getImageButton = findViewById(R.id.getImageButton)
        val config =AGConnectServicesConfig.fromContext(application)
        MLApplication.getInstance().setApiKey(config.getString("client/api_key"))
        getImageButton!!.setOnClickListener(this@LandMarkActivity)
        createImageTransactor()
        createDialog()
        isLandScape = this.resources
            .configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        this.setStatusBar()
        if (type == null) {
            selectLocalImage()
        } else if (type == Constant.TYPE_SELECT_IMAGE) {
            selectLocalImage()
        } else {
            startCamera()
        }
    }

    private fun initTitle() {
        title = findViewById(R.id.page_title)
        if (selectedMode == Constant.CLOUD_IMAGE_CLASSIFICATION) {
            title!!.setText(this.resources.getText(R.string.cloud_classification))
        } else if (selectedMode == Constant.CLOUD_LANDMARK_DETECTION) {
            title!!.setText(this.resources.getText(R.string.landmark_s))
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onClick(view: View) {
        if (view.id == R.id.getImageButton) {
            this.showDialog()
        }
    }

    private fun createDialog() {
        addPictureDialog = AddPictureDialog(this)
        val intent =
            Intent(this@LandMarkActivity, LandMarkActivity::class.java)
        intent.putExtra(
            Constant.MODEL_TYPE,
            Constant.CLOUD_IMAGE_CLASSIFICATION
        )
        addPictureDialog!!.setClickListener(object :
            AddPictureDialog.ClickListener {
            override fun takePicture() {
                this@LandMarkActivity.startCamera()
            }

            override fun selectImage() {
                this@LandMarkActivity.selectLocalImage()
            }

            override fun doExtend() {}
        })
    }

    private fun showDialog() {
        addPictureDialog!!.show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_IMAGE_URI, imageUri)
        if (maxWidthOfImage != null) {
            outState.putInt(KEY_IMAGE_MAX_WIDTH, maxWidthOfImage!!)
        }
        if (maxHeightOfImage != null) {
            outState.putInt(KEY_IMAGE_MAX_HEIGHT, maxHeightOfImage!!)
        }
    }

    private fun reloadAndDetectImage() {
        if (preview == null || maxHeightOfImage == null || (maxHeightOfImage == 0
                    && (preview!!.parent as View).height == 0)
        ) {
            mHandler.postDelayed(
                detectRunnable,
                DELAY_TIME.toLong()
            )
        } else {
            loadImageAndSetTransactor()
        }
    }

    private fun startCamera() {
        imageUri = null
        preview!!.setImageBitmap(null)
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(this.packageManager) != null) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "New Picture")
            values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera")
            imageUri = this.contentResolver
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            this.startActivityForResult(
                takePictureIntent,
                REQUEST_TAKE_PHOTO
            )
        }
    }

    private fun selectLocalImage() {
        val intent = Intent(Intent.ACTION_PICK, null)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        this.startActivityForResult(intent, REQUEST_SELECT_IMAGE)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            reloadAndDetectImage()
        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_CANCELED) {
            finish()
        } else if (requestCode == REQUEST_SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                imageUri = data.data
            }
            reloadAndDetectImage()
        } else if (requestCode == REQUEST_SELECT_IMAGE && resultCode == Activity.RESULT_CANCELED) {
            finish()
        }
    }

    private fun loadImageAndSetTransactor() {
        if (imageUri == null) {
            return
        }
        showLoadingDialog()
        graphicOverlay!!.clear()
        mHandler.postDelayed(myRunnable, TIMEOUT.toLong())
        imageBitmap = BitmapUtils.loadFromPath(
            this@LandMarkActivity,
            imageUri,
            getMaxWidthOfImage()!!,
            getMaxHeightOfImage()!!
        )
        preview!!.setImageBitmap(imageBitmap)
        if (imageBitmap != null) {
            imageTransactor!!.process(imageBitmap, graphicOverlay)
        }
    }

    private fun getMaxWidthOfImage(): Int? {
        if (maxWidthOfImage == null || maxWidthOfImage == 0) {
            if (isLandScape) {
                maxWidthOfImage = (preview!!.parent as View).height
            } else {
                maxWidthOfImage = (preview!!.parent as View).width
            }
        }
        return maxWidthOfImage
    }

    private fun getMaxHeightOfImage(): Int? {
        if (maxHeightOfImage == null || maxHeightOfImage == 0) {
            if (isLandScape) {
                maxHeightOfImage = (preview!!.parent as View).width
            } else {
                maxHeightOfImage = (preview!!.parent as View).height
            }
        }
        return maxHeightOfImage
    }

    private fun createImageTransactor() {
        when (selectedMode) {
            Constant.CLOUD_IMAGE_CLASSIFICATION -> imageTransactor =
                RemoteImageClassificationTransactor(this.applicationContext, mHandler)
            Constant.CLOUD_LANDMARK_DETECTION -> imageTransactor = LandmarkTransactor(mHandler, object : GetLocationDetails{
                override fun getLocationTitles(titlesList: ArrayList<String>) {
                        landmarkTitle=titlesList
                }

                override fun getLocationLat(latList: ArrayList<String>) {
                        landmarkLat=latList
                }

                override fun getLocationLong(longList: ArrayList<String>) {
                    landmarkLong=longList
                }
            })

            else -> throw IllegalStateException("Unknown selectedMode: " + selectedMode)
        }
        Log.d(TAG, imageTransactor!!::class.java.name)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (imageTransactor != null) {
            imageTransactor!!.stop()
            imageTransactor = null
        }
        imageUri = null
        if (progressDialog != null) {
            if (progressDialog!!.isShowing) {
                progressDialog!!.dismiss()
            }
            progressDialog = null
        }
    }

    private fun showLoadingDialog() {
        if (progressDialog == null) {
            progressDialog =
                Dialog(this, R.style.progress_dialog)
            progressDialog!!.setContentView(R.layout.dialog)
            progressDialog!!.setCancelable(false)
            progressDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            val msg = progressDialog!!.findViewById<TextView>(R.id.id_tv_loadingmsg)
            msg.text = this.getString(R.string.loading_data)
        }
        progressDialog!!.show()
    }

    private fun sendHospitalData() {
        val intent = Intent(this@LandMarkActivity, NearByHospitalActivity::class.java)
        intent.putExtra("locationClicked", false)
        intent.putExtra("lat", landmarkLat!!.get(0))
        intent.putExtra("long", landmarkLong!!.get(0))
        intent.putStringArrayListExtra("hospitalTitle", landmarkTitle)
        intent.putStringArrayListExtra("hospitalLat", landmarkLat)
        intent.putStringArrayListExtra("hospitalLong", landmarkLong)
        startActivity(intent)
        //Clear lists for future searchs.
        landmarkTitle!!.clear()
        landmarkLat!!.clear()
        landmarkLong!!.clear()
    }

}