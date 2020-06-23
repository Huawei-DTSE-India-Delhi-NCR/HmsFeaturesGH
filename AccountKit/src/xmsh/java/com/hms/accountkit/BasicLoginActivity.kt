package com.hms.accountkit

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService

class BasicLoginActivity: AppCompatActivity() {

    //a constant for detecting the login intent result
    private val RC_SIGN_IN = 234

    //Tag for the logs optional
    private val TAG = "simplifiedcoding"
    var mGoogleSignInClient: HuaweiIdAuthService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        val gso =
            HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setEmail()
                .createParams()
        mGoogleSignInClient = HuaweiIdAuthManager.getService(this, gso)
        findViewById<View>(R.id.sign_in_button).setOnClickListener { signIn() }
    }

    override fun onStart() {
        super.onStart()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task =
                HuaweiIdAuthManager.parseAuthResultFromIntent(data)
            try {
                val account =
                    task.getResultThrowException(
                        ApiException::class.java
                    )
                Toast.makeText(this, "user signed in", Toast.LENGTH_SHORT).show()
            } catch (e: ApiException) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


    //this method is called on click
    private fun signIn() {
        //getting the google signin intent
        val signInIntent = mGoogleSignInClient!!.signInIntent
        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

}