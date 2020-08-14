package com.hms.accountkit

import android.accounts.Account
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import com.hms.accountkit.utils.AccountConst
import com.hms.accountkit.utils.AccountDetails
import com.hms.accountkit.utils.AccountUtils
import com.hms.availabletoalllbraries.BaseActivity
import com.hms.availabletoalllbraries.reflections.CallClassMethods
import com.hms.availabletoalllbraries.utils.Utils
import kotlinx.android.synthetic.main.account_demo_activity.*
import kotlin.reflect.KClass

class LoginDemoActivity: BaseActivity(true){

    lateinit var basicDemoBtn: AppCompatTextView
    lateinit var silentSignInBtn: AppCompatTextView
    lateinit var signOutBtn: AppCompatTextView
    lateinit var revokeBtn: AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       setContentView(R.layout.account_demo_activity)
        title=getString(R.string.account_kit_features)

        basicDemoBtn=findViewById(R.id.basic_agc)
        silentSignInBtn=findViewById(R.id.quick_sign_in)
        signOutBtn=findViewById(R.id.sign_out)
        revokeBtn=findViewById(R.id.sign_revoke)


        basicDemoBtn.setOnClickListener(View.OnClickListener {
           moveToSignIn(AccountConst.LOGIN_RESULT)
            })

        silentSignInBtn.setOnClickListener(View.OnClickListener {
            moveToSignIn(AccountConst.SILENT_RESULT)
        })

        signOutBtn.setOnClickListener(View.OnClickListener {
            moveToSignIn(AccountConst.LOGINOUT_RESULT)
        })

        revokeBtn.setOnClickListener(View.OnClickListener {
            moveToSignIn(AccountConst.REVOKE_RESULT)
        })



    }

    override fun onResume() {
        super.onResume()



    }

    fun moveToSignIn(requestValue:Int)
    {

        HBasicLoginActivity.newStartActivity(this,requestValue)

       /* var kotlinClass: KClass<*>?=null
        kotlinClass=CallClassMethods.getKotlinClass(AccountConst.HBasicLoginActivity_PATH)

        CallClassMethods.callCompanionFunction(kotlinClass!!,AccountConst.newStartActivity_METHOD)
            .call(CallClassMethods.getCompanionObjectInstance(kotlinClass!!), this,requestValue)*/

        /*var kotlinClass: KClass<*>?=null
        if(Utils.isHmsorGms(this)){
            kotlinClass=CallClassMethods.getKotlinClass(AccountConst.HBasicLoginActivity_PATH)

            CallClassMethods.callCompanionFunction(kotlinClass!!,AccountConst.newStartActivity_METHOD)
                .call(CallClassMethods.getCompanionObjectInstance(kotlinClass!!), this,requestValue)

        } else
        {
            kotlinClass=CallClassMethods.getKotlinClass(AccountConst.GBasicLoginActivity_PATH)

            CallClassMethods.callCompanionFunction(kotlinClass!!,AccountConst.newStartActivity_METHOD)
                .call(CallClassMethods.getCompanionObjectInstance(kotlinClass!!), this)
        }*/

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(resultCode)
        {

            AccountConst.LOGIN_RESULT->{
                var accountDetails= AccountUtils.accountDetails
                login_result.text= accountDetails?.name+ "\n"+accountDetails?.email

            }

            AccountConst.LOGIN_RESULT_ERROR->{
                var accountDetails= AccountUtils.accountDetails
                login_result.text=accountDetails?.errorMsg

            }

            AccountConst.SILENT_RESULT->{
                var accountDetails= AccountUtils.accountDetails
                login_result.text= accountDetails?.name+ "\n"+accountDetails?.email

            }

            AccountConst.SILENT_RESULT_ERROR->{
                var accountDetails= AccountUtils.accountDetails
                login_result.text=accountDetails?.errorMsg

            }

            AccountConst.LOGINOUT_RESULT->{
                var accountDetails= AccountUtils.accountDetails
                login_result.text=accountDetails?.errorMsg

            }

            AccountConst.LOGINOUT_RESULT_ERROR->{
                var accountDetails= AccountUtils.accountDetails
                login_result.text=accountDetails?.errorMsg

            }

            AccountConst.REVOKE_RESULT->{
                var accountDetails= AccountUtils.accountDetails
                login_result.text=accountDetails?.errorMsg

            }

            AccountConst.REVOKE_RESULT_ERROR->{
                var accountDetails= AccountUtils.accountDetails
                login_result.text=accountDetails?.errorMsg

            }

        }



    }




}