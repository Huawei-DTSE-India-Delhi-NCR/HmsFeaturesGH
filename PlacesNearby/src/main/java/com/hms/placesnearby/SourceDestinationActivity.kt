package com.hms.placesnearby

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.ColorFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import com.hms.availabletoalllbraries.BaseActivity
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.hms.location.FusedLocationProviderClient
import com.huawei.hms.location.LocationRequest
import com.huawei.hms.location.LocationServices
import com.huawei.hms.location.SettingsClient
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapView
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.model.*
import com.huawei.hms.mlplugin.asr.MLAsrCaptureActivity
import com.huawei.hms.mlplugin.asr.MLAsrCaptureConstants
import com.huawei.hms.mlsdk.common.MLApplication
import com.huawei.hms.site.api.model.Site
import com.huawei.hms.site.widget.SearchFilter
import com.huawei.hms.site.widget.SearchIntent
import kotlinx.android.synthetic.xmsh.source_des_activity.*


class SourceDestinationActivity :  BaseActivity(true), OnMapReadyCallback
{

    val PLACES_RES:Int=100001
    val TAG: String = "SourceDestinationActivity"
    //HUAWEI map
    private var hMap: HuaweiMap? = null
    private var mMapView: MapView? = null
    private val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var mLocationRequest: LocationRequest? = null
    lateinit var settingsClient: SettingsClient

    val searchIntent: SearchIntent= SearchIntent()

    val mLanguage: String = com.huawei.hms.mlsdk.asr.MLAsrConstants.LAN_EN_US
    val ML_ASR_CAPTURE_CODE = 2

    var locationVoiceText: String ? = null

    var sourceMicOn: Boolean=false
    var destinationMicOn: Boolean=false

    var sourceSite: Site?= null
    var destinationSite: Site? = null

    var micActiveColor: Int = 0
    var micInActiveColor: Int = 0

    var needToClearMap:Boolean=false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.source_des_activity)

        //get mapview instance
        mMapView = findViewById(R.id.mapView)

        micActiveColor=ResourcesCompat.getColor(resources,R.color.colorPrimary,null)
        micInActiveColor=ResourcesCompat.getColor(resources,R.color.black,null)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        settingsClient = LocationServices.getSettingsClient(this)

        if(hasPermissions(this)) {
            var mapViewBundle: Bundle? = null
            if (savedInstanceState != null) {
                mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
            }
            mMapView?.onCreate(mapViewBundle)
            //get map instance
            mMapView?.getMapAsync(this)
        }

        searchIntent.setApiKey(getString(R.string.api_key));
        initiateAsr()

    }

    fun initiateAsr(){
        val config =
            AGConnectServicesConfig.fromContext(application)
        MLApplication.getInstance().apiKey = config.getString("client/api_key")


    }

    fun sourceMicClicked(view:View)
    {
        img_s_mic.imageTintList= ColorStateList.valueOf(micActiveColor)
        sourceMicOn=true
        destinationMicOn=false
        asrListerner()
    }


    fun destinationMicClicked(view:View)
    {
        img_d_mic.imageTintList= ColorStateList.valueOf(micActiveColor)
        destinationMicOn=true
        sourceMicOn=false
        asrListerner()
    }



    fun asrListerner(){
        try {
            val intent: Intent = Intent(this, MLAsrCaptureActivity::class.java)
                .putExtra(MLAsrCaptureConstants.LANGUAGE, mLanguage)
                .putExtra(MLAsrCaptureConstants.FEATURE, MLAsrCaptureConstants.FEATURE_WORDFLUX)
            startActivityForResult(intent, ML_ASR_CAPTURE_CODE)
            overridePendingTransition(R.anim.mlkit_asr_popup_slide_show, 0)
        } catch (ex:Exception){
            Log.e("EROR",ex.message)
        }

    }


    fun sourceSearch(view:View)
    {
        sourceMicOn=true
//      startActivity(Intent(this,PlacesFragment::class.java))
        callSearchLocation()
    }


    fun destinationSearch(view:View)
    {
        destinationMicOn=true
//      startActivity(Intent(this,PlacesFragment::class.java))
        callSearchLocation()
    }



    fun callSearchLocation(){
        if(locationVoiceText!=null) {
            var searchFilter: SearchFilter = SearchFilter()
            searchFilter.query = locationVoiceText
            searchIntent.setSearchFilter(searchFilter)
        }
        val intent = searchIntent.getIntent(this)
        startActivityForResult(intent, SearchIntent.SEARCH_REQUEST_CODE)
    }

    override fun onMapReady(map: HuaweiMap?) {

        hMap = map?.apply {
            setMyLocationEnabled(true);// Enable the my-location overlay.
            getUiSettings()?.setMyLocationButtonEnabled(true);// Enable the my-location icon.
            isMyLocationEnabled = false
            moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(12.9698, 77.7500), 10f))
        };

        setUpMap()

    }

    fun setUpMap(){
        hMap?.apply {
            setMyLocationEnabled(true)
            getUiSettings()?.setMyLocationButtonEnabled(true)
            getUiSettings()?.setCompassEnabled(true)
            getUiSettings()?.setZoomControlsEnabled(true)
            getUiSettings()?.setMyLocationButtonEnabled(true)
            setWatermarkEnabled(true)
        }


    }

    /*
    Create Circle to current location
     */
    private fun addCircleToCurrentLocation() {
        hMap?.addCircle(
            CircleOptions()
                .center(LatLng(sourceSite?.location!!.lat, sourceSite?.location!!.lng))
                .radius(100.0)
                .strokeWidth(10f)
                .strokeColor(ResourcesCompat.getColor(resources,R.color.colorPrimaryDark,null))
                .fillColor(Color.argb(128, 245, 0, 87))
                .clickable(true)
        )
    }

    /*
    Create Marker when you click on map
     */

    private fun createMarker(site:Site) {
        var latLng= LatLng(site.location.lat,site.location.lng)
        val markerOptions = MarkerOptions()
            .position(latLng)
            .snippet("Address : " + site.name)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.flower))
        hMap?.addMarker(markerOptions)

        moveMapCameraToPosition(latLng)

      //  hMap?.setOnMarkerClickListener(this)
    }

    fun moveMapCameraToPosition(latLng: LatLng)
    {
        val cameraPosition = CameraPosition.Builder()
            .target(latLng) // Sets the center of the map to location user
            .zoom(15f) // Sets the zoom
            .bearing(90f) // Sets the orientation of the camera to east
            .tilt(40f) // Sets the tilt of the camera to 30 degrees
            .build() // Creates a CameraPosition from the builder
        hMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }


    override fun onStart() {
        super.onStart()
        mMapView!!.onStart()
    }

    override fun onStop() {
        super.onStop()
        mMapView!!.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView!!.onDestroy()
    }

    override fun onPause() {
        mMapView!!.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        mMapView!!.onResume()
    }


    private fun hasPermissions(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i(TAG, "sdk >= 23 M")
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val strings = arrayOf<String>(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.RECORD_AUDIO
                )
                ActivityCompat.requestPermissions(this, strings, 1)
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (SearchIntent.SEARCH_REQUEST_CODE == requestCode) {
            locationVoiceText=null
            if (SearchIntent.isSuccess(resultCode)) {
                val site: Site
                site = searchIntent.getSiteFromIntent(data)
                loadVoiceText(site)
            }
        }
        else if (requestCode == ML_ASR_CAPTURE_CODE) {
            var text:String?=null
            img_s_mic.imageTintList= ColorStateList.valueOf(micInActiveColor)
            img_d_mic.imageTintList= ColorStateList.valueOf(micInActiveColor)
            when (resultCode) {
                MLAsrCaptureConstants.ASR_SUCCESS -> if (data != null) {
                    val bundle = data.extras
                    if (bundle != null && bundle.containsKey(MLAsrCaptureConstants.ASR_RESULT)) {
                        text=bundle!!.getString(MLAsrCaptureConstants.ASR_RESULT)

                    }
                    if (text != null && "" != text) {
                        locationVoiceText=text
                        callSearchLocation()
                    }
                }
                MLAsrCaptureConstants.ASR_FAILURE -> if (data != null) {
                    val errorCode: Int
                    val bundle = data.extras
                    if (bundle != null && bundle.containsKey(MLAsrCaptureConstants.ASR_ERROR_CODE)) {
                        errorCode = bundle.getInt(MLAsrCaptureConstants.ASR_ERROR_CODE)
                        //     showFailedDialog(getPrompt(errorCode))
                        Toast.makeText(this,errorCode,Toast.LENGTH_LONG).show()
                        locationVoiceText=null

                    }
                }
                else -> {
                }
            }
        }

    }

    fun loadVoiceText(site: Site)
    {
        if(site==null)
        {
            sourceMicOn=false
            destinationMicOn=false
        }
        if(sourceMicOn)
        {
            sourceMicOn=false
            sourceSite=site
            source_text.text=site.formatAddress
        }else if(destinationMicOn)
        {
            destinationMicOn=false
            destinationSite=site
            des_text.text=site.formatAddress
        }

        if(needToClearMap)
        {
            createMarker(site)
        }
        drawPolyline()


    }

    fun drawPolyline()
    {

        if(sourceSite!=null && destinationSite!=null) {
            needToClearMap=true
            hMap?.clear()
            createMarker(sourceSite!!)
            createMarker(destinationSite!!)
            hMap?.addPolyline(PolylineOptions().apply {
                add(
                    LatLng(sourceSite?.location!!.lat, sourceSite?.location!!.lng),
                    LatLng(destinationSite?.location!!.lat, destinationSite?.location!!.lng)
                )
                    .width(5.0f)
                    .color.apply { micActiveColor }
                color(micActiveColor)
                visible(true)
            })




        }



    }





}