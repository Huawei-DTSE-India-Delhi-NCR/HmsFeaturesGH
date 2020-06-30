package com.hms.accountkit

import android.accounts.Account
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import com.hms.accountkit.utils.AccountConst
import com.hms.availabletoalllbraries.reflections.CallClassMethods
import com.hms.availabletoalllbraries.utils.Utils
import kotlin.reflect.KClass

class LoginDemoActivity: AppCompatActivity(){

    lateinit var basicDemoBtn: AppCompatTextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       setContentView(R.layout.account_demo_activity)
        title=getString(R.string.account_kit_features)

        basicDemoBtn=findViewById(R.id.basic_agc)

        basicDemoBtn.setOnClickListener(View.OnClickListener {

//           Utils.startFlavorActivity(this,"com.hms.accountkit","BasicLoginActivity")

            var currentIntent: Intent
            var kotlinClass: KClass<*>?=null
            if(Utils.isHmsorGms(this)){
                kotlinClass=CallClassMethods.getKotlinClass(AccountConst.HBasicLoginActivity_PATH)

            } else
            {
                kotlinClass=CallClassMethods.getKotlinClass(AccountConst.GBasicLoginActivity_PATH)
            }
            CallClassMethods.callCompanionFunction(kotlinClass!!,AccountConst.newStartActivity_METHOD)
                .call(CallClassMethods.getCompanionObjectInstance(kotlinClass!!), this)
            })

    }




}