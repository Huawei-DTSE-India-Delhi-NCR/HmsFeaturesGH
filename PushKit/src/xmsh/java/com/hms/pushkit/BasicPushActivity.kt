package com.hms.pushkit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.style.CharacterStyle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.FragmentActivity
import com.android.volley.Header
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.hms.availabletoalllbraries.BaseActivity
import com.hms.availabletoalllbraries.reflections.CallClassMethods
import com.hms.availabletoalllbraries.utils.Utils
import com.hms.pushkit.utils.NOTIFICATION_TYPE
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.hms.aaid.HmsInstanceId
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import kotlin.reflect.KClass


class BasicPushActivity: BaseActivity(isBackRequired = true) {


    companion object{



        fun newStartActivity(context: Context, notificationType: NOTIFICATION_TYPE){
            val intent = Intent(context, BasicPushActivity::class.java)
            intent.putExtra("TYPE",notificationType.ordinal)
            context.startActivity(intent)
        }
    }

    val TAG: String =BasicPushActivity::class.java.simpleName

    lateinit var notificationType: NOTIFICATION_TYPE
    val jsonObject=JSONObject()
    var requestBody: String?=null
    var token: String?=null
    var textV:AppCompatTextView?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notification_layout)
        supportActionBar!!.title="Push Notifications"

        textV=findViewById(R.id.n_message)

        if(intent.extras!=null)
        {
            if(intent!!.extras!!.containsKey("TYPE"))
            {
                notificationType=NOTIFICATION_TYPE.values()[intent.getIntExtra("TYPE",0)]
            }
        }

        getToken()
    }

    fun getPushMessage()
    {

        try{
            val jsonArray=JSONArray()
            jsonArray.put(token)

            var notificationObj=JSONObject().apply {
                put("title","HMS Feature")
                put("body","Hello!!!! Hemanth here")
                //put("image","https://dummyimage.com/512x256/fff.png")
            }

            var clickAction=JSONObject().apply {
                put("type",1)
                put("intent", "#Intent;compo=com.rvr/.Activity;S.W=U;end")
//                put("intent","intent://com.hms.features/deeplink?#Intent;scheme=pushscheme;launchFlags=0x4000000;i.age=180;S.name=abc;end")

            }

            var androidNotification=JSONObject().apply {
                put("collapse_key",-1)
                put("title","HMS Feature")
                put("body","Hello world")
                put("click_action",clickAction)
            }

            var androidObj=JSONObject()

            var messageObj=JSONObject()
            messageObj.put("data","Hello Huawei")

            when(notificationType)
            {
                NOTIFICATION_TYPE.NORMAL->{

                  //  messageObj.put("notification",notificationObj)
                }

                NOTIFICATION_TYPE.IMAGE->{
                    androidNotification.apply {
                        put("body","Image notification")
                        put("image","https://huaweimobileservices.com/wp-content/uploads/2018/08/themes-icon.png")
                        put("click_action",clickAction)
                        put("default_sound",true)
                    }
                    androidObj.put("notification",androidNotification)
                    messageObj.put("android",androidObj)
                }

                NOTIFICATION_TYPE.SOUND->{
                    androidNotification.apply {
                        put("body","Sound notification")
                        put("sound","/raw/bongo_n")
                        put("default_sound",false)
                        put("importance","HIGH")
                        put("channel_id","RingRing")
                        put("click_action",JSONObject().apply {
                            put("type",1)
                            put("intent","intent://com.hms.features/deeplink?#Intent;scheme=deeplink2;launchFlags=0x4000000;i.age=180;S.name=abc;end")

                        })
                    }
                    androidObj.put("notification",androidNotification)
                    messageObj.put("android",androidObj)
                }

                NOTIFICATION_TYPE.LARGE->{
                    androidNotification.put("style",1)
                    androidNotification.put("big_title","Hemanth Cholla")
                    androidNotification.put("big_body","HMS features are developing using HMS kits")

                    androidObj.put("notification",androidNotification)
                    messageObj.put("android",androidObj)
                }

                NOTIFICATION_TYPE.BANDGE->{
                    androidNotification.put("body","Badge notification")
                    androidNotification.put("badge",JSONObject().apply { put("add_num",1 )
                                                                               put("class","com.hms.features")})
                    androidObj.put("notification",androidNotification)
                    messageObj.put("android",androidObj)
                }

                NOTIFICATION_TYPE.INBOX_STYLE->{

                    androidNotification.apply {

                        put("style",3)
                        put("big_title","HMS Core SDK 4.0.0 Feature Description")
                        put("inbox_content",JSONArray().apply { put("1. Added the function of displaying notification messages on the UI.")
                                                               put("2. Added the automatic initialization capability")
                                                               put("3. Added the function of sending messages to web apps through the WebPush agent.")
                                                                put("4. Added the function of sending messages to web apps through the WebPush agent.")})
                    }

                    androidObj.put("notification",androidNotification)
                    messageObj.put("android",androidObj)

                }

                NOTIFICATION_TYPE.WITH_BUTTONS->{
                    androidNotification.apply {

                        put("style",1)
                        put("big_title","HUAWEI Push Kit")
                        put("big_body","HUAWEI Push Kit is a messaging service provided by Huawei for developers. It establishes a messaging channel from the cloud to devices")
                        put("buttons",JSONArray().apply {put(JSONObject().apply { put("action_type",0)
                                                                                        put("name","Learn More")
                                                                                        put("intent","hmsdeeplink://com.hms.features:8080/details?param1=3")})
                                                                put(JSONObject().apply { put("action_type",3)
                                                                                         put("name","Ignore") })})
                                                                }
                    androidObj.put("notification",androidNotification)
                    messageObj.put("android",androidObj)

                }

                NOTIFICATION_TYPE.HIDING_LOCK_SCREEN->{
                    androidNotification.put("visibility","PRIVATE")
                    androidObj.put("notification",androidNotification)
                    messageObj.put("android",androidObj)

                }

                NOTIFICATION_TYPE.SILENT_PUSH->{
                    androidNotification.put("foreground_show",false)
                    androidObj.put("notification",androidNotification)
                    messageObj.put("android",androidObj)

                }

                NOTIFICATION_TYPE.DEEP_LINK->{
                    androidNotification.apply {
                        put("body","Deep link notification")
                        put("image","https://huaweimobileservices.com/wp-content/uploads/2018/08/themes-icon.png")
                        put("click_action",JSONObject().apply {
                            put("type",1)
                            put("intent","intent://com.hms.features/deeplink?#Intent;scheme=deeplink2;launchFlags=0x4000000;i.age=180;S.name=abc;end")

                        })
                    }

                    androidObj.put("notification",androidNotification)
                    messageObj.put("android",androidObj)
                }

                NOTIFICATION_TYPE.DEEP_LINK_WEB->{
                    androidNotification.apply {
                        put("body","Deep link web notification")
                        put("image","https://huaweimobileservices.com/wp-content/uploads/2018/08/themes-icon.png")
                        put("click_action",JSONObject().apply {
                            put("type",2)
                            put("url","https://developer.huawei.com/consumer/en/doc/development/HMS-3-References/push-server-send")

                        })
                    }

                    androidObj.put("notification",androidNotification)
                    messageObj.put("android",androidObj)

                }

            }

                messageObj.apply {
                  //  put("data","Notification")
                  //  put("notification",notificationObj)
                    put("token",jsonArray)

                }

            requestBody=JSONObject().apply {
                                            put("validate_only",false)
                                            put("message",messageObj)
                                            }.toString()


        } catch (jsonEx: JSONException)
        {
            Log.e("VOLLEY",jsonEx.message)
        }

    }

    fun callPushServiceApi()
    {
        getPushMessage()

        val queue= Volley.newRequestQueue(this)
        val url="https://push-api.cloud.huawei.com/v1/102422611/messages:send"

        val stringRequest=object :StringRequest(Request.Method.POST, url, Response.Listener<String> {
            Log.d("PUSH:",it.toString())
            Toast.makeText(this,it.toString(),Toast.LENGTH_LONG).show()
            textV!!.text=it.toString()
        }, Response.ErrorListener {
            Log.d("PUSH:",it.toString())
            if(Utils.ACCESS_TOKEN!=null) {
                //Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
                textV!!.text = it.toString()
            }else
            {
               // Toast.makeText(this, "Please login to Huawei account using Account Kit", Toast.LENGTH_LONG).show()
                textV!!.text = "Please login to Huawei account using Account Kit"
            }
        } ){

            override fun getHeaders(): MutableMap<String, String> {
                val maps=HashMap<String,String>().apply {
                    put("Content-Type","application/x-www-form-urlencoded")
                    put("Authorization",Utils.ACCESS_TOKEN!!)
                }


                return maps
            }

            override fun getBodyContentType(): String {


                return super.getBodyContentType()
            }

            override fun getBody(): ByteArray? {
           try {
                return requestBody!!.toByteArray(Charset.defaultCharset())
            } catch (uee: UnsupportedEncodingException) {
                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                return null;
            }

                return super.getBody()
            }

        }

        stringRequest.tag=1

        queue.add(stringRequest)

    }


    private fun getToken() {
        object : Thread() {
            override fun run() {
                try {
                    val appId =
                        AGConnectServicesConfig.fromContext(this@BasicPushActivity)
                            .getString("client/app_id")
                    val pushtoken = HmsInstanceId.getInstance(this@BasicPushActivity).getToken(appId, "HCM")
                    if (!TextUtils.isEmpty(pushtoken)) {
                        token=pushtoken
                        Log.i(TAG, "get token:$pushtoken")
                        // showLog(pushtoken)
                        callPushServiceApi()
                    }
                } catch (e: Exception) {
                    Log.i(TAG, "getToken failed, $e")
                }
            }
        }.start()
    }

}