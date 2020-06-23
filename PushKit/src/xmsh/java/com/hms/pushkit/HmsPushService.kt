package com.hms.pushkit

import android.util.Log
import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage
import java.util.*

class HmsPushService: HmsMessageService() {

    private val TAG = "PushDemoLog"

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        Log.i(TAG, "receive token:$token");

    }

    override fun onMessageReceived(message: RemoteMessage?) {
        super.onMessageReceived(message)

        if (message != null) {
            Log.i(TAG, "getCollapseKey: " + message.getCollapseKey()
                    + "\n getData: " + message.getData()
                    + "\n getFrom: " + message.getFrom()
                    + "\n getTo: " + message.getTo()
                    + "\n getMessageId: " + message.getMessageId()
                    + "\n getSendTime: " + message.getSentTime()
                    + "\n getMessageType: " + message.getMessageType()
                    + "\n getTtl: " + message.getTtl())
        };

        var notification: RemoteMessage.Notification? = message?.getNotification();
        if (notification != null) {
            Log.i(TAG, "\n getImageUrl: " + notification.getImageUrl()
                    + "\n getTitle: " + notification.getTitle()
                    + "\n getTitleLocalizationKey: " + notification.getTitleLocalizationKey()
                    + "\n getTitleLocalizationArgs: " + Arrays.toString(notification.getTitleLocalizationArgs())
                    + "\n getBody: " + notification.getBody()
                    + "\n getBodyLocalizationKey: " + notification.getBodyLocalizationKey()
                    + "\n getBodyLocalizationArgs: " + Arrays.toString(notification.getBodyLocalizationArgs())
                    + "\n getIcon: " + notification.getIcon()
                    + "\n getSound: " + notification.getSound()
                    + "\n getTag: " + notification.getTag()
                    + "\n getColor: " + notification.getColor()
                    + "\n getClickAction: " + notification.getClickAction()
                    + "\n getChannelId: " + notification.getChannelId()
                    + "\n getLink: " + notification.getLink()
                    + "\n getNotifyId: " + notification.getNotifyId());
        }
    }

    }




