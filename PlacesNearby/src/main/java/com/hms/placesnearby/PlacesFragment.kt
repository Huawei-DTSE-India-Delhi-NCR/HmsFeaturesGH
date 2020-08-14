package com.hms.placesnearby

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.hms.availabletoalllbraries.BaseActivity
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.hms.mlplugin.asr.MLAsrCaptureActivity
import com.huawei.hms.mlplugin.asr.MLAsrCaptureConstants
import com.huawei.hms.mlsdk.asr.MLAsrConstants
import com.huawei.hms.mlsdk.common.MLApplication
import com.huawei.hms.site.api.model.SearchStatus
import com.huawei.hms.site.api.model.Site
import com.huawei.hms.site.widget.SearchFragment
import com.huawei.hms.site.widget.SiteSelectionListener


class PlacesFragment:BaseActivity(true) {

    lateinit var searchFragment: SearchFragment

    val mLanguage: String = MLAsrConstants.LAN_EN_US
    val ML_ASR_CAPTURE_CODE = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.search_places_dialog)

        searchFragment=supportFragmentManager.findFragmentById(R.id.search_fragment) as SearchFragment

        searchFragment.setApiKey(getString(R.string.api_key))

        val config =
            AGConnectServicesConfig.fromContext(application)
        MLApplication.getInstance().apiKey = config.getString("client/api_key")

        setListener()
    }

    fun asrListerner(view:View){
        try {
            val intent: Intent = Intent(this, MLAsrCaptureActivity::class.java)
                .putExtra(MLAsrCaptureConstants.LANGUAGE, mLanguage)
                .putExtra(MLAsrCaptureConstants.FEATURE, MLAsrCaptureConstants.FEATURE_WORDFLUX)
            startActivityForResult(intent, ML_ASR_CAPTURE_CODE)
            overridePendingTransition(R.anim.mlkit_asr_popup_slide_show, 0)
        } catch (ex:Exception){
            Log.e("EROR",ex.message)
        }

    }

    fun setListener(){

        searchFragment.setOnSiteSelectedListener(object :SiteSelectionListener{

            override fun onSiteSelected(data: Site?) {
                Toast.makeText(getApplication(), data!!.getName(), Toast.LENGTH_LONG).show();
            }

            override fun onError(status: SearchStatus?) {
                Toast
                    .makeText(getApplication(), status!!.getErrorCode() + "\n" + status!!.getErrorMessage(),
                        Toast.LENGTH_LONG)
                    .show();
            }

        })

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var text: String? =null

        if (requestCode == ML_ASR_CAPTURE_CODE) {
            when (resultCode) {
                MLAsrCaptureConstants.ASR_SUCCESS -> if (data != null) {
                    val bundle = data.extras
                    if (bundle != null && bundle.containsKey(MLAsrCaptureConstants.ASR_RESULT)) {
                        text=bundle!!.getString(MLAsrCaptureConstants.ASR_RESULT)

                    }
                    if (text != null && "" != text) {
                        Toast.makeText(this,text,Toast.LENGTH_LONG).show()
                    }
                }
                MLAsrCaptureConstants.ASR_FAILURE -> if (data != null) {
                    val errorCode: Int
                    val bundle = data.extras
                    if (bundle != null && bundle.containsKey(MLAsrCaptureConstants.ASR_ERROR_CODE)) {
                        errorCode = bundle.getInt(MLAsrCaptureConstants.ASR_ERROR_CODE)
                   //     showFailedDialog(getPrompt(errorCode))
                        Toast.makeText(this,errorCode,Toast.LENGTH_LONG).show()
                    }
                }
                else -> {
                }
            }
        }

    }






}