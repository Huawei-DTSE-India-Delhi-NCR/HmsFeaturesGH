package com.hms.features.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hms.accountkit.LoginDemoActivity
import com.hms.adskit.AdsDemoActivity
import com.hms.analyticskit.AnalyticsMainActivity
import com.hms.features.database.entity.FeatureEntity
import com.hms.features.databinding.FeatureDetailsActivityBinding
import com.hms.features.utils.AppEvents
import com.hms.features.utils.CONSTANTS
import com.hms.features.utils.Utils
import com.hms.locationkit.LocationDemoActivity
import com.hms.mapkit.MapDemoActivity
import com.hms.pushkit.PushDemoActivity

class FeatureDetailsActivity: AppCompatActivity() {

    private lateinit var binding: FeatureDetailsActivityBinding
    var featureEntity: FeatureEntity? = null
    private lateinit var clickHandlers: ClickHandlers

    companion object {

        fun newIntent(context: Context, featureEn: FeatureEntity) {

            val mapsList=HashMap<String,String>()
            mapsList[CONSTANTS.KIT_NAME_ATR] = featureEn.featureType.toString()
            mapsList[CONSTANTS.CLICKED_TIME_ATR] = Utils.getTime()

            AppEvents.sendEvent(CONSTANTS.CLICK_ON_KIT_EVENT, mapsList)
            context.startActivity(Intent(context, FeatureDetailsActivity::class.java).apply {
                putExtra("feature", featureEn)
            })
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getIntentExtras()
        setBinding()
    }

    private fun getIntentExtras() {
          if(intent?.extras != null && intent?.extras!!.containsKey("feature"))
            featureEntity = intent?.getParcelableExtra("feature") as? FeatureEntity
    }

    private fun setBinding() {
        binding = FeatureDetailsActivityBinding.inflate(layoutInflater)
        val resId = resources.getIdentifier(featureEntity?.featureType+"_features", "string", packageName)
        title = resources.getString(resId)

        binding.clickHandlers = ClickHandlers()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.lifecycleOwner = this
        setContentView(binding.root)
        binding.setFeature(featureEntity)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /**
     *  Handles the click listener
     */
    inner class ClickHandlers {
        fun onDemoClicked(view: View, type: String) {

                when(type)
                {
                    "map_kit"->{
                        startActivity(Intent(this@FeatureDetailsActivity,MapDemoActivity::class.java))

                    }

                    "analytics_kit"->{
                        startActivity(Intent(this@FeatureDetailsActivity,AnalyticsMainActivity::class.java))

                    }

                    "account_kit"->{
                        startActivity(Intent(this@FeatureDetailsActivity,LoginDemoActivity::class.java))

                    }

                    "location_kit"->{
                        startActivity(Intent(this@FeatureDetailsActivity,LocationDemoActivity::class.java))

                    }

                    "push_kit"->{
                        startActivity(Intent(this@FeatureDetailsActivity,PushDemoActivity::class.java))

                    }

                    "ads_kit"->{
                        startActivity(Intent(this@FeatureDetailsActivity,AdsDemoActivity::class.java))

                    }

                }

        }


    }



}