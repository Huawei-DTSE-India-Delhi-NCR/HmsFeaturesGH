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
        BasicPushActivity.newStartActivity(this,NOTIFICATION_TYPE.NORMAL)
//        moveToPushNewActivity(PushConst.basePushActivity_PATH, PushConst.newStartActivity_METHOD, this,NOTIFICATION_TYPE.NORMAL)
    }

    fun sendImagePushNotification(view:View)
    {
        BasicPushActivity.newStartActivity(this,NOTIFICATION_TYPE.IMAGE)
//        moveToPushNewActivity(PushConst.basePushActivity_PATH, PushConst.newStartActivity_METHOD, this,NOTIFICATION_TYPE.IMAGE)
    }

    fun sendSoundPushNotification(view:View)
    {
        BasicPushActivity.newStartActivity(this,NOTIFICATION_TYPE.SOUND)
//        moveToPushNewActivity(PushConst.basePushActivity_PATH, PushConst.newStartActivity_METHOD, this,NOTIFICATION_TYPE.SOUND)
    }

    fun sendLargePushNotification(view:View)
    {
        BasicPushActivity.newStartActivity(this,NOTIFICATION_TYPE.LARGE)
//        moveToPushNewActivity(PushConst.basePushActivity_PATH, PushConst.newStartActivity_METHOD, this,NOTIFICATION_TYPE.LARGE)
    }

    fun sendBadgePushNotification(view:View)
    {
        BasicPushActivity.newStartActivity(this,NOTIFICATION_TYPE.BANDGE)
//        moveToPushNewActivity(PushConst.basePushActivity_PATH, PushConst.newStartActivity_METHOD, this,NOTIFICATION_TYPE.BANDGE)
    }

    fun sendInboxPushNotification(view:View)
    {
        BasicPushActivity.newStartActivity(this,NOTIFICATION_TYPE.INBOX_STYLE)
//        moveToPushNewActivity(PushConst.basePushActivity_PATH, PushConst.newStartActivity_METHOD, this,NOTIFICATION_TYPE.INBOX_STYLE)
    }

    fun sendHidePushNotification(view:View)
    {
        BasicPushActivity.newStartActivity(this,NOTIFICATION_TYPE.HIDING_LOCK_SCREEN)
//        moveToPushNewActivity(PushConst.basePushActivity_PATH, PushConst.newStartActivity_METHOD, this,NOTIFICATION_TYPE.HIDING_LOCK_SCREEN)
    }

    fun sendButtonsPushNotification(view:View)
    {
        BasicPushActivity.newStartActivity(this,NOTIFICATION_TYPE.WITH_BUTTONS)
//        moveToPushNewActivity(PushConst.basePushActivity_PATH, PushConst.newStartActivity_METHOD, this,NOTIFICATION_TYPE.WITH_BUTTONS)
    }

    fun sendSilentPushNotification(view:View)
    {
        BasicPushActivity.newStartActivity(this,NOTIFICATION_TYPE.SILENT_PUSH)
//        moveToPushNewActivity(PushConst.basePushActivity_PATH, PushConst.newStartActivity_METHOD, this,NOTIFICATION_TYPE.SILENT_PUSH)
    }

    fun sendDeeplinkPushNotification(view:View)
    {
        BasicPushActivity.newStartActivity(this,NOTIFICATION_TYPE.DEEP_LINK)
//        moveToPushNewActivity(PushConst.basePushActivity_PATH, PushConst.newStartActivity_METHOD, this,NOTIFICATION_TYPE.DEEP_LINK)
    }

    fun sendDeeplinkWebPushNotification(view:View)
    {
        BasicPushActivity.newStartActivity(this,NOTIFICATION_TYPE.DEEP_LINK_WEB)
//        moveToPushNewActivity(PushConst.basePushActivity_PATH, PushConst.newStartActivity_METHOD, this,NOTIFICATION_TYPE.DEEP_LINK_WEB)
    }

    fun moveToPushNewActivity(pathName: String, methodName: String, context: Context, notificationType: NOTIFICATION_TYPE)
    {

        var kotlinClass: KClass<*>? = null
        kotlinClass = CallClassMethods.getKotlinClass(pathName)
        CallClassMethods.callCompanionFunction(kotlinClass!!, methodName)
            .call(
                CallClassMethods.getCompanionObjectInstance(kotlinClass!!),
                context,
                notificationType
            )

        /*if(Utils.isHmsorGms(this)) {
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
        }*/

    }






}