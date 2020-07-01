package com.hms.accountkit

import android.R.attr
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hmf.tasks.Task
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper
import com.huawei.hms.support.hwid.result.AuthHuaweiId
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService
import kotlinx.android.synthetic.xmsgh.login_activity.*


class HBasicLoginActivity: AppCompatActivity() {


    companion object{

        fun newStartActivity(context: Context){
            context.startActivity(Intent(context,HBasicLoginActivity::class.java))

        }

    }


    //a constant for detecting the login intent result
    private val RC_SIGN_IN = 234

    //Tag for the logs optional
    private val TAG = "simplifiedcoding"
    var mGoogleSignInClient: HuaweiIdAuthService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        supportActionBar?.title="HMS Basic Login"

        val gso =HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
            .setEmail()
            .setId()
            .setAccessToken()
            .setIdToken()
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
            //login success
            //get user message by parseAuthResultFromIntent
            val authHuaweiIdTask: Task<AuthHuaweiId> =
                HuaweiIdAuthManager.parseAuthResultFromIntent(data)
            if (authHuaweiIdTask.isSuccessful()) {
                val huaweiAccount: AuthHuaweiId = authHuaweiIdTask.getResult()
                Log.i(TAG, "signIn success " + huaweiAccount.displayName)

                status_details.text="Name: "+huaweiAccount.displayName + "\n"+
                                    "Email: "+huaweiAccount.email+ "\n"
                                   /* "Image Url: "+huaweiAccount.avatarUriString+ "\n"+
                                    "Id Token: "+huaweiAccount.idToken*/

            } else {
                Log.i(TAG,"signIn failed: " + (authHuaweiIdTask.getException() as ApiException).statusCode
                )
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