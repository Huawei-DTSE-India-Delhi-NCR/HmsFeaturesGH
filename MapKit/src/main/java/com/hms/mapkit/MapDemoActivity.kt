package com.hms.mapkit

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.hms.availabletoalllbraries.BaseActivity

class MapDemoActivity: BaseActivity(true) {

    lateinit var basicDemoBtn: AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.map_demo_activity)

        basicDemoBtn=findViewById(R.id.basic_map)

        basicDemoBtn.setOnClickListener(View.OnClickListener {

            startActivity(Intent(this,MapActivity::class.java))

        })

    }

    fun interactive(view:View)
    {
        startActivity(Intent(this, InteractingMapsActivity::class.java))

    }

}