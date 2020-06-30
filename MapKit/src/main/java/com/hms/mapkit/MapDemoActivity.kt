package com.hms.mapkit

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.hms.availabletoalllbraries.BaseActivity
import com.hms.availabletoalllbraries.reflections.CallClassMethods
import com.hms.mapkit.routemap.RouteMapActivity
import com.hms.mapkit.utils.MapsConst

class MapDemoActivity: BaseActivity(true) {

    lateinit var basicDemoBtn: AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.map_demo_activity)

        basicDemoBtn=findViewById(R.id.basic_map)

        basicDemoBtn.setOnClickListener(View.OnClickListener {

//            startActivity(Intent(this,MapActivity::class.java))
            CallClassMethods.moveToNewActivity(MapsConst.MapActivity_PATH, MapsConst.newStartActivity_METHOD, this)
        })

    }

    fun interactive(view:View)
    {
       // startActivity(Intent(this, InteractingMapsActivity::class.java))
        CallClassMethods.moveToNewActivity(MapsConst.InteractingMapsActivity_PATH, MapsConst.newStartActivity_METHOD, this)

    }

    fun routeMap(view:View)
    {
        // startActivity(Intent(this, RouteMapActivity::class.java))
        CallClassMethods.moveToNewActivity(MapsConst.RouteMapActivity_PATH, MapsConst.newStartActivity_METHOD, this)

    }

}