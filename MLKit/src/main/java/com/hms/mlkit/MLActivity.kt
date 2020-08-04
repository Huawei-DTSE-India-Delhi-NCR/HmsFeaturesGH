package com.hms.mlkit

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.hms.availabletoalllbraries.BaseActivity
import com.hms.availabletoalllbraries.utils.ItemOffsetDecoration
import com.hms.mlkit.utils.MLMainAdapter
import com.hms.mlkit.utils.MLObject
import kotlinx.android.synthetic.main.ml_main_layout.*

class MLActivity: BaseActivity(true),
    ActivityCompat.OnRequestPermissionsResultCallback {

    val TAG="MLActivity"
    private val PERMISSION_REQUESTS = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.ml_main_layout)

        rv_ml_list.layoutManager = GridLayoutManager(this, 2)
        rv_ml_list.addItemDecoration(ItemOffsetDecoration(this,getResources().getDimensionPixelSize(R.dimen.offsetItem)))

        rv_ml_list.adapter=MLMainAdapter(this,getDummyData())

      //  startActivity(Intent(this,StartTextActivity::class.java))
        if (!allPermissionsGranted()) {
            getRuntimePermissions()
        }

    }

    /**
     * Read the ApiKey field in the agconnect-services.json to obtain the API key of the application and set it.
     * For details about how to apply for the agconnect-services.json, see section https://developer.huawei.com/consumer/cn/doc/development/HMS-Guides/ml-add-agc.
     */



    fun getDummyData():ArrayList<MLObject>
    {

        val myMlData=ArrayList<MLObject>()

        var mlData: MLObject = MLObject(1,"Text")
        myMlData.apply {
            add(MLObject(0,"Text"))
            add(MLObject(1,"Speech & Language"))
            add(MLObject(2,"Vision"))
            add(MLObject(3,"Face body"))
        }

        return myMlData

    }

    fun getRequiredPermissions(): Array<String?> {
        return try {
            val info = this.packageManager
                .getPackageInfo(this.packageName, PackageManager.GET_PERMISSIONS)
            val ps = info.requestedPermissions
            if (ps != null && ps.size > 0) {
                ps
            } else {
                arrayOfNulls(0)
            }
        } catch (e: RuntimeException) {
            throw e
        } catch (e: Exception) {
            arrayOfNulls(0)
        }
    }

    private fun allPermissionsGranted(): Boolean {
        for (permission in getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission!!)) {
                return false
            }
        }
        return true
    }

    private fun getRuntimePermissions() {
        val allNeededPermissions: MutableList<String?> =
            java.util.ArrayList()
        for (permission in getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission!!)) {
                allNeededPermissions.add(permission)
            }
        }
        if (!allNeededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                allNeededPermissions.toTypedArray(),
                PERMISSION_REQUESTS
            )
        }
    }

    fun isPermissionGranted(
        context: Context,
        permission: String
    ): Boolean {
        if (ContextCompat.checkSelfPermission(context, permission)
            == PackageManager.PERMISSION_GRANTED
        ) {
            Log.i(TAG, "Permission granted: $permission")
            return true
        }
        Log.i(TAG, "Permission NOT granted: $permission")
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != PERMISSION_REQUESTS) {
            return
        }
        var isNeedShowDiag = false
        for (i in permissions.indices) {
            if (permissions[i] == Manifest.permission.CAMERA && grantResults[i] != PackageManager.PERMISSION_GRANTED
                || permissions[i] == Manifest.permission.READ_EXTERNAL_STORAGE && grantResults[i] != PackageManager.PERMISSION_GRANTED
                || permissions[i] == Manifest.permission.RECORD_AUDIO && grantResults[i] != PackageManager.PERMISSION_GRANTED
            ) {
                // If the camera or storage permissions are not authorized, need to pop up an authorization prompt box.
                isNeedShowDiag = true
            }
        }
        if (isNeedShowDiag && !ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CALL_PHONE
            )
        ) {
            val dialog =
                AlertDialog.Builder(this)
                    .setMessage(this.getString(R.string.camera_permission_rationale))
                    .setPositiveButton(
                        this.getString(R.string.settings)
                    ) { dialog, which ->
                        val intent =
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        // Open the corresponding setting interface according to the package name.
                        intent.data =
                            Uri.parse("package:" + this@MLActivity.getPackageName())
                        this@MLActivity.startActivityForResult(intent, 200)
                        this@MLActivity.startActivity(intent)
                    }
                    .setNegativeButton(
                        this.getString(R.string.cancel)
                    ) { dialog, which -> this@MLActivity.finish() }.create()
            dialog.show()
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200) {
            if (!allPermissionsGranted()) {
                getRuntimePermissions()
            }
        }
    }



}