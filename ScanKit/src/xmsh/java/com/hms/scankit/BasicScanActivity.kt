package com.hms.scankit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.hms.availabletoalllbraries.BaseActivity
import com.hms.scankit.utils.Permissions
import com.hms.scankit.utils.Permissions.Companion.CAMERA_REQ_CODE
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions


// Scan Kit allows for 13 barcode formats
class BasicScanActivity: BaseActivity(true) {

    companion object{
        fun newStartActivity(context: Context){
            val intent = Intent(context, BasicScanActivity::class.java)
            context.startActivity(intent)
        }
    }


    val REQUEST_CODE_SCAN_ONE:Int=1112
    lateinit var resultTv:AppCompatTextView

    // Many types of code scanning formats are available
    val options:HmsScanAnalyzerOptions = HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.ALL_SCAN_TYPE).create()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.basic_layout)

        resultTv=findViewById(R.id.scan_message)



        Permissions.callCameraPermissions(this)

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_REQ_CODE && grantResults.size == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            ScanUtil.startScan(this, REQUEST_CODE_SCAN_ONE, options);
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode !== Activity.RESULT_OK || data == null) {
            return
        }
        if (requestCode === REQUEST_CODE_SCAN_ONE) {
            val obj: HmsScan = data!!.getParcelableExtra(ScanUtil.RESULT)
            obj?.let {
               // showResult(it)
                resultTv.text=it.showResult
            }
        }
    }

    fun scanCode(view:View)
    {
        ScanUtil.startScan(this, REQUEST_CODE_SCAN_ONE, options);
    }

    /**
     *  Parsing the data result
     */
    fun parseResult(result: HmsScan) {
        if (result.getScanTypeForm() == HmsScan.SMS_FORM) {
            val smsContent = result.getSmsContent()
            val content = smsContent.getMsgContent()
            val phoneNumber = smsContent.getDestPhoneNumber()
        } else if (result.getScanTypeForm() == HmsScan.WIFI_CONNECT_INFO_FORM) {
            val wifiConnectionInfo = result.wiFiConnectionInfo
            val password = wifiConnectionInfo.getPassword()
            val ssidNumber = wifiConnectionInfo.getSsidNumber()
            val cipherMode = wifiConnectionInfo.getCipherMode()
        }
    }

}