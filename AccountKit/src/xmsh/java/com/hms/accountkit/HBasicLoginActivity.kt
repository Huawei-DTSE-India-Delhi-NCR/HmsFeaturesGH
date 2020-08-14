package com.hms.accountkit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import com.hms.accountkit.utils.AccountConst
import com.hms.accountkit.utils.AccountDetails
import com.hms.accountkit.utils.AccountUtils
import com.hms.availabletoalllbraries.BaseActivity
import com.hms.availabletoalllbraries.utils.Utils
import com.huawei.hmf.tasks.OnCompleteListener
import com.huawei.hmf.tasks.OnFailureListener
import com.huawei.hmf.tasks.OnSuccessListener
import com.huawei.hmf.tasks.Task
import com.huawei.hms.api.HuaweiApiClient
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper
import com.huawei.hms.support.hwid.result.AuthHuaweiId
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService


class HBasicLoginActivity: BaseActivity(true) {


    companion object{

        fun newStartActivity(context: Context, requestValue: Int){
            var intent=Intent(context,HBasicLoginActivity::class.java)
            intent.putExtra("REQUEST",requestValue)
            (context as BaseActivity).startActivityForResult(intent,AccountConst.LOGIN_RESULT)

        }

    }


    //a constant for detecting the login intent result
    private val RC_SIGN_IN = 234

    //Tag for the logs optional
    private val TAG = "HBasicLoginActivity"
    var mHuaweiSignInClient: HuaweiIdAuthService? = null
    var tvSuccess: AppCompatTextView?=null
    var tvNextBtn: AppCompatTextView?=null

    var silentSignInTask:Task<AuthHuaweiId>?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        supportActionBar?.title="HMS Basic Login"



        tvSuccess=findViewById<AppCompatTextView>(R.id.success_tv)
        tvNextBtn=findViewById<AppCompatTextView>(R.id.next_btn)

        val gso =HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
            .setEmail()
            .setId()
            .setAccessToken()
            .setIdToken()
            .createParams()

        mHuaweiSignInClient = HuaweiIdAuthManager.getService(this, gso)

        findViewById<View>(R.id.sign_in_button).setOnClickListener { signIn() }


        HuaweiApiClient.OnConnectionFailedListener {


        }

        if(intent.extras!=null){
            when(intent.extras?.getInt("REQUEST")){

                AccountConst.LOGIN_RESULT ->{
                    signIn()

                }

                AccountConst.LOGINOUT_RESULT ->{
                    signOut()
                }

                AccountConst.REVOKE_RESULT ->{
                    revokeAccount()

                }

                AccountConst.SILENT_RESULT ->{
                    setSilentSignIn()

                }





            }
        }

    }


    /**
     * Silent Signin
     */
    fun setSilentSignIn(){
        silentSignInTask= mHuaweiSignInClient?.silentSignIn();

        silentSignInTask?.addOnSuccessListener(OnSuccessListener {
            // Obtain the user's HUAWEI ID information.
            Log.i(TAG, "displayName:" + it.getDisplayName());

            AccountUtils.accountDetails= AccountDetails(true,it.displayName,it.email, it.avatarUriString,"")
            setResult(AccountConst.LOGIN_RESULT)
            finish()

        })

        silentSignInTask?.addOnFailureListener(OnFailureListener {
            // The sign-in failed. Try to sign in explicitly using getSignInIntent().

            // The sign-in failed. Try to sign in explicitly using getSignInIntent().
            if (it is ApiException) {
                val apiException =
                    it as ApiException
                Log.i(TAG, "sign failed status:" + apiException.statusCode)

                AccountUtils.accountDetails= AccountDetails(false,"","", "","sign failed status:" + apiException.statusCode)
                setResult(AccountConst.LOGIN_RESULT_ERROR)
                finish()
            }

        })

    }

    override fun onStart() {
        super.onStart()
    }

    fun nextBtnCalled(view: View){
        finish()
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

                Utils.ACCESS_TOKEN=huaweiAccount.accessToken

                findViewById<AppCompatTextView>(R.id.status_details).text="Name: "+huaweiAccount.displayName + "\n"+
                                    "Email: "+huaweiAccount.email+ "\n"
                                   "Image Url: "+huaweiAccount.avatarUriString
                                    /*  "Id Token: "+huaweiAccount.idToken*/
                tvSuccess?.visibility=View.VISIBLE
                tvNextBtn?.visibility=View.VISIBLE

                AccountUtils.accountDetails= AccountDetails(true,huaweiAccount.displayName,huaweiAccount.email, huaweiAccount.avatarUriString,"")
                setResult(AccountConst.LOGIN_RESULT)
                finish()

            } else {
                tvSuccess?.visibility=View.VISIBLE
                tvSuccess?.text="OOps...!!!! \n Login failed"
                Log.i(TAG,"signIn failed: " + (authHuaweiIdTask.getException() as ApiException).statusCode)
                AccountUtils.accountDetails= AccountDetails(false,"","", "","OOps...!!!! \n Login failed")
                setResult(AccountConst.LOGIN_RESULT_ERROR)
                finish()
            }
        }
    }


    //this method is called on click
    private fun signIn() {
        //getting the google signin intent
        val signInIntent = mHuaweiSignInClient!!.signInIntent
        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signOut() {
        val signOutTask: Task<Void> = mHuaweiSignInClient!!.signOut()
        signOutTask.addOnCompleteListener(object : OnCompleteListener<Void?> {
            override fun onComplete(task: Task<Void?>?) {
                AccountUtils.accountDetails= AccountDetails(false,"","", "","Signed out successfully")
                setResult(AccountConst.LOGINOUT_RESULT)
                finish()

                Toast.makeText(this@HBasicLoginActivity, "Signed out successfully", Toast.LENGTH_LONG)
                    .show()
                //clearInfo()
            }
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(e: Exception) {
                println("Exception $e")
                AccountUtils.accountDetails= AccountDetails(false,"","", "","Signed out successfully")
                setResult(AccountConst.LOGINOUT_RESULT_ERROR)
                finish()
            }
        })
    }


    fun revokeAccount(){

        mHuaweiSignInClient?.cancelAuthorization()

        // service indicates the HuaweiIdAuthService instance generated using the getService method during the sign-in authorization.
        mHuaweiSignInClient?.cancelAuthorization()?.addOnCompleteListener(OnCompleteListener<Void?> { task ->
            if (task.isSuccessful) {
                // Processing after a successful authorization revoking.
                AccountUtils.accountDetails= AccountDetails(false,"","", "","Successfully authorization revoking")
                setResult(AccountConst.REVOKE_RESULT)
                finish()
                Log.i(TAG, "onSuccess: ")
            } else {
                // Handle the exception.
                val exception = task.exception
                if (exception is ApiException) {
                    val statusCode = (exception as ApiException).statusCode
                    AccountUtils.accountDetails= AccountDetails(false,"","", "","Revoking  $statusCode")
                    Log.i(TAG, "onFailure: $statusCode")
                    setResult(AccountConst.REVOKE_RESULT_ERROR)
                    finish()
                }
            }
        })
    }



}