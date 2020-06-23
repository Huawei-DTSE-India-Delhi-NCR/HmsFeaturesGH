package com.hms.accountkit

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView

class LoginDemoActivity: AppCompatActivity(){

    lateinit var basicDemoBtn: AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       setContentView(R.layout.account_demo_activity)

        basicDemoBtn=findViewById(R.id.basic_agc)

        basicDemoBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,BasicLoginActivity::class.java))
        })

    }




}