package com.hms.availabletoalllbraries

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity(var isBackRequired: Boolean) : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(isBackRequired) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}