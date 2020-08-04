package com.hms.features.view

import android.os.Bundle
import android.widget.Toast
import com.hms.availabletoalllbraries.BaseActivity


class DeeplinkActivity: BaseActivity(true){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        if (null != intent) {
            // Obtain data set in way 1.
     //       val name1 = intent.data!!.getQueryParameter("name")
     //       val age1 = intent.data!!.getQueryParameter("age")!!.toInt()
            // Obtain data set in way 2.
            val name2 = intent.getStringExtra("name")
            val age2 = intent.getIntExtra("age", -1)
            Toast.makeText(this, "name $name2,age $age2", Toast.LENGTH_SHORT).show()
        }

    }

}