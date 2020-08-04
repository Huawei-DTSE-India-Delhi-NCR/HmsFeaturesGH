package com.hms.features.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.hms.availabletoalllbraries.utils.ItemOffsetDecoration
import com.hms.features.HmsApplication
import com.hms.features.R
import com.hms.features.dagger.factory.FeatureListViewModelFactory
import com.hms.features.database.entity.FeatureEntity
import com.hms.features.databinding.ActivityFeatureListBinding
import com.hms.features.viewmodel.FeatureListViewModel
import kotlinx.android.synthetic.main.activity_feature_list.*
import javax.inject.Inject


class FeaturesListActivity : AppCompatActivity() {

    companion object {
        private val TAG = FeaturesListActivity::class.java.simpleName
    }

    private var vpPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            updateCircleMarker(binding, position)
        }
    }

    private lateinit var binding: ActivityFeatureListBinding
    private lateinit var featureListViewModel: FeatureListViewModel
    private lateinit var clickHandlers: ClickHandlers
    private var featuresAdapter:FeaturesAdapter? = null

    @Inject
    lateinit var featuresListViewModelFactory: FeatureListViewModelFactory

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injectDagger()

        createViewModel()

        setBinding()

        observeViewModel()



    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)


    }

    private fun createViewModel() {
        featureListViewModel = ViewModelProviders.of(this, featuresListViewModelFactory)[FeatureListViewModel::class.java]
        clickHandlers = ClickHandlers()
    }

    private fun injectDagger() {
        HmsApplication.instance.hmsComponent.inject(this)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun observeViewModel() {
        featureListViewModel.isLoading.value = true
        featureListViewModel.getFeaturesList().observe(this, Observer {
            if (!isDestroyed) {
                if(it!=null) {
                    showFeaturesList(it)
                }
            }
        })
    }



    private fun setBinding() {
        binding = ActivityFeatureListBinding.inflate(layoutInflater)
        binding.viewModel = featureListViewModel
        binding.lifecycleOwner = this
        binding.clickHandlers = clickHandlers
        setContentView(binding.root)

        binding.vpBanners.adapter=FeaturesViewPagerAdapter(this,2)
        binding.vpBanners.registerOnPageChangeCallback(vpPageChangeCallback)

    }

    /**
     *  Handles the click listener
     */
    inner class ClickHandlers {
        fun onFABClicked(view: View) {

        }


    }

    private fun updateFeaturesList() {

            featureListViewModel.getFeaturesList().observe(this, Observer { list ->
                if (!isDestroyed) {
                    if (list.isNotEmpty()) {
                        showFeaturesList(list)
                    } else
                        showFeaturesList(listOf())
                }
            })
    }

    private fun showFeaturesList(featuresList: List<FeatureEntity>) {
        if (featuresAdapter == null) {
            rv_features_list.layoutManager = GridLayoutManager(this, 2)
            rv_features_list.addItemDecoration(ItemOffsetDecoration(this,getResources().getDimensionPixelSize(R.dimen.offsetItem)))
            featuresAdapter = FeaturesAdapter(featuresList)
            rv_features_list.adapter = featuresAdapter
        } else {

            featuresAdapter?.updateFeaturesList(featuresList)

        }
    }



    override fun onResume() {
        super.onResume()
        setUpdatedFeaturesList()
    }

    override fun onDestroy() {
        super.onDestroy()


    }


    private fun setUpdatedFeaturesList() {
        Handler().postDelayed({
            if (!isDestroyed) {
            }
        }, 100)
    }

    private fun updateCircleMarker(binding: ActivityFeatureListBinding, position: Int) {
        when (position) {
            0 -> {
                binding.vpFirstCircle.background = getDrawable(R.drawable.bg_green_circle)
                binding.vpSecondCircle.background = getDrawable(R.drawable.bg_grey_circle)
            //    binding.vpThirdCircle.background = getDrawable(R.drawable.bg_grey_circle)
            }
            1 -> {
                binding.vpFirstCircle.background = getDrawable(R.drawable.bg_grey_circle)
                binding.vpSecondCircle.background = getDrawable(R.drawable.bg_green_circle)
              //  binding.vpThirdCircle.background = getDrawable(R.drawable.bg_grey_circle)
            }
            /*2 -> {
                binding.vpFirstCircle.background = getDrawable(R.drawable.bg_grey_circle)
                binding.vpSecondCircle.background = getDrawable(R.drawable.bg_grey_circle)
                binding.vpThirdCircle.background = getDrawable(R.drawable.bg_green_circle)
            }*/
        }
    }
}
