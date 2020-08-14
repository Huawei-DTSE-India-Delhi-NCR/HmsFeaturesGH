package com.hms.features.view

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import com.hms.availabletoalllbraries.BaseActivity
import com.hms.features.R
import com.huawei.agconnect.applinking.AGConnectAppLinking


class DeeplinkTwoActivity: BaseActivity(true){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.deeplink_layout)

        val deepTv=findViewById<AppCompatTextView>(R.id.deep_link)
        deepTv.text="Deep Link 2"

        val intent = intent
        if (null != intent) {
            // Obtain data set in way 1.
     //       val name1 = intent.data!!.getQueryParameter("name")
     //       val age1 = intent.data!!.getQueryParameter("age")!!.toInt()
            // Obtain data set in way 2.
            val name2 = intent.getStringExtra("name")
            val age2 = intent.getIntExtra("age", -1)
            Toast.makeText(this, "name $name2,age $age2", Toast.LENGTH_SHORT).show()

            if(age2==-1)
            {
                AGConnectAppLinking.getInstance().getAppLinking(this, getIntent())
                    .addOnSuccessListener({ resolvedLinkData ->
                        var deepLink: Uri? = null
                        if (resolvedLinkData != null) {
                            deepLink = resolvedLinkData.getDeepLink()
                            Toast.makeText(this, deepLink.getQueryParameter("name"), Toast.LENGTH_SHORT).show()
                        }
                    }).addOnFailureListener({ e ->
                        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                    })
            }

        }

    }

}