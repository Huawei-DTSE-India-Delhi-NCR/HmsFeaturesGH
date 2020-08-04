package com.hms.crashservice

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.huawei.agconnect.crash.AGConnectCrash

class CrashServiceActivity: AppCompatActivity() {

    companion object{

        fun raiseCrash(context: Context)
        {
            AGConnectCrash.getInstance().testIt(context);
        }

        fun getCrash():AGConnectCrash
        {
            return AGConnectCrash.getInstance()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

}