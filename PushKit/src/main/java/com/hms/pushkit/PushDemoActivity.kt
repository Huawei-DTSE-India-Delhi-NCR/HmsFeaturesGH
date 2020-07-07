package com.hms.pushkit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.hms.availabletoalllbraries.BaseActivity
import com.hms.availabletoalllbraries.reflections.CallClassMethods
import com.hms.availabletoalllbraries.utils.Utils
import com.hms.parnoramakit.utils.PushConst
import com.hms.pushkit.utils.NOTIFICATION_TYPE
import kotlin.reflect.KClass

class PushDemoActivity:BaseActivity(true) {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.push_demo_activity)
        title=getString(R.string.push_kit_features)

    }


    fun sendPushNotification(view:View)
    {
        moveToPushNewActivity(PushConst.basePushActivity_PATH, PushConst.newStartActivity_METHOD, this,NOTIFICATION_TYPE.NORMAL)
    }

    fun sendImagePushNotification(view:View)
    {
        moveToPushNewActivity(PushConst.basePushActivity_PATH, PushConst.newStartActivity_METHOD, this,NOTIFICATION_TYPE.IMAGE)
    }

    fun sendSoundPushNotification(view:View)
    {
        moveToPushNewActivity(PushConst.basePushActivity_PATH, PushConst.newStartActivity_METHOD, this,NOTIFICATION_TYPE.SOUND)
    }

    fun sendLargePushNotification(view:View)
    {
        moveToPushNewActivity(PushConst.basePushActivity_PATH, PushConst.newStartActivity_METHOD, this,NOTIFICATION_TYPE.LARGE)
    }

    fun sendBadgePushNotification(view:View)
    {
        moveToPushNewActivity(PushConst.basePushActivity_PATH, PushConst.newStartActivity_METHOD, this,NOTIFICATION_TYPE.BANDGE)
    }


    fun moveToPushNewActivity(pathName: String, methodName: String, context: Context, notificationType: NOTIFICATION_TYPE)
    {
        if(Utils.isHmsorGms(this)) {
            var kotlinClass: KClass<*>? = null
            kotlinClass = CallClassMethods.getKotlinClass(pathName)
            CallClassMethods.callCompanionFunction(kotlinClass!!, methodName)
                .call(
                    CallClassMethods.getCompanionObjectInstance(kotlinClass!!),
                    context,
                    notificationType
                )
        }else{
            Toast.makeText(this,"GMS is not available",Toast.LENGTH_LONG).show()
        }

    }






}