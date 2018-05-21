package com.zhiyangstudio.commonlib.utils;

import android.app.Notification;
import android.content.Context;

/**
 * Created by zhi yang on 2018/1/31.
 */

public class SampleNotificationUtils {
    public Notification create(Context context, String title, String msg, int smallIcon) {
        Notification notification = new Notification.Builder(context)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(msg)
                .setSmallIcon(smallIcon)
                .setWhen(System.currentTimeMillis())
                .build();
        return notification;
    }
}
