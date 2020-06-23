package com.hms.pushkit

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.hms.aaid.HmsInstanceId


class BasicPushActivity: AppCompatActivity() {

    val TAG: String =BasicPushActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getToken()


    }


    private fun getToken() {
        object : Thread() {
            override fun run() {
                try {
                    val appId =
                        AGConnectServicesConfig.fromContext(this@BasicPushActivity)
                            .getString("client/app_id")
                    val pushtoken = HmsInstanceId.getInstance(this@BasicPushActivity).getToken(appId, "HCM")
                    if (!TextUtils.isEmpty(pushtoken)) {
                        Log.i(TAG, "get token:$pushtoken")
                        // showLog(pushtoken)
                    }
                } catch (e: Exception) {
                    Log.i(TAG, "getToken failed, $e")
                }
            }
        }.start()
    }

}