package com.zhiyangstudio.commonlib.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by zhiyang on 2016/6/14.
 */
public class NotificationUtils {

    NotificationManager manager;
    NotificationCompat.Builder builder;

    int notificationId = -1;

    Context context;

    public NotificationUtils(Context context, int notificationId) {
        this.context = context;
        this.notificationId = notificationId;

        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(context);
    }

    public static boolean isDarkNotificationTheme(Context context) {
        return !isSimilarColor(Color.BLACK, getNotificationColor(context));
    }

    private static boolean isSimilarColor(int baseColor, int color) {
        int simpleBaseColor = baseColor | 0xff000000;
        int simpleColor = color | 0xff000000;
        int baseRed = Color.red(simpleBaseColor) - Color.red(simpleColor);
        int baseGreen = Color.green(simpleBaseColor) - Color.green(simpleColor);
        int baseBlue = Color.blue(simpleBaseColor) - Color.blue(simpleColor);
        double value = Math.sqrt(baseRed * baseRed + baseGreen * baseGreen + baseBlue * baseBlue);
        if (value < 180.0) {
            return true;
        }
        return false;
    }

    /**
     * 获取通知栏颜色
     */
    public static int getNotificationColor(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Notification notification = builder.build();
        int layoutId = notification.contentView.getLayoutId();
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(layoutId, null,
                false);
        if (viewGroup.findViewById(android.R.id.title) != null) {
            return ((TextView) viewGroup.findViewById(android.R.id.title)).getCurrentTextColor();
        }
        return findColor(viewGroup);
    }

    private static int findColor(ViewGroup viewGroupSource) {
        int color = Color.TRANSPARENT;
        LinkedList<ViewGroup> viewGroups = new LinkedList<>();
        viewGroups.add(viewGroupSource);
        while (viewGroups.size() > 0) {
            ViewGroup viewGroup1 = viewGroups.getFirst();
            for (int i = 0; i < viewGroup1.getChildCount(); i++) {
                if (viewGroup1.getChildAt(i) instanceof ViewGroup) {
                    viewGroups.add((ViewGroup) viewGroup1.getChildAt(i));
                } else if (viewGroup1.getChildAt(i) instanceof TextView) {
                    if (((TextView) viewGroup1.getChildAt(i)).getCurrentTextColor() != -1) {
                        color = ((TextView) viewGroup1.getChildAt(i)).getCurrentTextColor();
                    }
                }
            }
            viewGroups.remove(viewGroup1);
        }
        return color;
    }

    /**
     * 通知消息展示
     * 支持8.0版本
     */
    public static void notifyMsg(Context context, String title, String content, int largeIcon,
                                 int smallIcon, Class<? extends Activity> targetClas) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context
                .NOTIFICATION_SERVICE);
        Notification notification = null;

        Intent intent = new Intent(context, targetClas);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap largetIcon = BitmapFactory.decodeResource(context.getResources(), largeIcon);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            //获取通知实例
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            //为实例设置属性
            builder.setSmallIcon(smallIcon);//设置小图标
            // (必须设置，若LargeIcon没设置，默认也是它，而且通知栏没下拉的时候显示也是它)
            builder.setLargeIcon(largetIcon);
            builder.setDefaults(Notification.DEFAULT_ALL);
            //设置大图标
            builder.setContentTitle(title);//设置标题内容
            builder.setContentText(content);//设置正文内容
            builder.setTicker(content);//设置滚动内容
            builder.setAutoCancel(true);//点击后消失
            builder.setOngoing(true);//是否一直显示（设置了这个方法后，不能通过左右滑动通知让通知栏消失，只有通过点击通知才能让通知消失）
            // builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);//设置优先级，级别越高，显示的位置越高
            //设置通知的点击事件（一般用来跳转到真正要显示的页面内）
            builder.setContentIntent(pendingIntent);
            notification = builder.build();
        } else {
            // TODO: 2018/5/30 8.0的通知
            String channelId = "1";
            String channelName = "Channel1";
            NotificationChannel channel = new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true); //是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.GREEN); //小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            manager.createNotificationChannel(channel);

            Notification.Builder builder = new Notification.Builder(context, channelId);
            //为实例设置属性
            builder.setSmallIcon(smallIcon);//设置小图标
            // (必须设置，若LargeIcon没设置，默认也是它，而且通知栏没下拉的时候显示也是它)
            builder.setLargeIcon(largetIcon);
            builder.setDefaults(Notification.DEFAULT_ALL);
            //设置大图标
            builder.setContentTitle(title);//设置标题内容
            builder.setContentText(content);//设置正文内容
            builder.setTicker(content);//设置滚动内容
            builder.setAutoCancel(true);//点击后消失
            builder.setOngoing(true);//是否一直显示（设置了这个方法后，不能通过左右滑动通知让通知栏消失，只有通过点击通知才能让通知消失）
            // builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);//设置优先级，级别越高，显示的位置越高
            //设置通知的点击事件（一般用来跳转到真正要显示的页面内）
            builder.setNumber(10); //久按桌面图标时允许的此条通知的数量
            builder.setContentIntent(pendingIntent);
            notification = builder.build();
        }
        //发送通知
        manager.notify(1, notification);
    }

    /**
     * 清除通知，支持8.0
     */
    public static void clearNotify(Context context, int notifyId) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context
                .NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            manager.cancel(notifyId);
        } else {
            manager.deleteNotificationChannel(notifyId + "");
        }
    }

    /**
     * 单行文本使用
     */
    public void sendSingleLineNotification(String ticker, String title, String content, int
            smallIcon, PendingIntent intent, boolean sound, boolean vibrate, boolean lights, int
                                                   largetIcon) {
        sample(ticker, title, content, smallIcon, intent, sound, vibrate, lights, largetIcon);
        Notification notification = builder.build();
        send(notification);
    }

    public void sample(String ticker, String title, String content, int smallIcon, PendingIntent
            intent, boolean sound, boolean vibrate, boolean lights, int largeIcon) {
        //状态栏文字
        builder.setTicker(ticker);
        //通知栏标题
        builder.setContentTitle(title);
        //通知栏内容
        builder.setContentText(content);
        //触发的intent
        builder.setContentIntent(intent);
        //这边设置颜色，可以给5.0及以上版本smallIcon设置背景色
        builder.setColor(Color.RED);
        //小图标
        builder.setSmallIcon(smallIcon);
        //大图标(这边同时设置了小图标跟大图标，在5.0及以上版本通知栏里面的样式会有所不同)
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeIcon));
        //设置该条通知时间
        builder.setWhen(System.currentTimeMillis());
        //设置为true，点击该条通知会自动删除，false时只能通过滑动来删除
        builder.setAutoCancel(true);
        //设置优先级，级别高的排在前面
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        int defaults = 0;
        if (sound) {
            defaults |= Notification.DEFAULT_SOUND;
        }
        if (vibrate) {
            defaults |= Notification.DEFAULT_VIBRATE;
        }
        if (lights) {
            defaults |= Notification.DEFAULT_LIGHTS;
        }
        //设置声音、闪光、震动
        builder.setDefaults(defaults);
        //设置是否为一个正在进行中的通知，这一类型的通知将无法删除
        builder.setOngoing(true);
    }

    private void send(Notification notification) {
        manager.notify(notificationId, notification);
    }

    /**
     * 多行文本使用
     */
    public void sendMoreLineNotification(String ticker, String title, String content, int
            smallIcon, PendingIntent intent, boolean sound, boolean vibrate, boolean lights, int
                                                 largeIcon) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            Toast.makeText(context, "您的手机低于Android 4.1.2，不支持！！", Toast.LENGTH_SHORT).show();
            return;
        }
        sample(ticker, title, content, smallIcon, intent, sound, vibrate, lights, largeIcon);
        Notification notification = new NotificationCompat.BigTextStyle(builder).bigText(content)
                .build();
        send(notification);
    }

    /**
     * 大图模式
     */
    public void sendBigPicNotification(String ticker, String title, String content, int
            smallIcon, PendingIntent intent, boolean sound, boolean vibrate, boolean lights, int
                                               ic_launcher, int bigicon) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            Toast.makeText(context, "您的手机低于Android 4.1.2，不支持！！", Toast.LENGTH_SHORT).show();
            return;
        }
        sample(ticker, title, content, smallIcon, intent, sound, vibrate, lights, bigicon);
        //大图
        Bitmap bigPicture = BitmapFactory.decodeResource(context.getResources(), ic_launcher);
        //图标
        Bitmap bigLargeIcon = BitmapFactory.decodeResource(context.getResources(), bigicon);
        Notification notification = new NotificationCompat.BigPictureStyle(builder).bigLargeIcon
                (bigLargeIcon).bigPicture(bigPicture).build();
        send(notification);
    }

    /**
     * 自定义通知视图
     */
    public void sendCustomerNotification(String ticker, String title, String content, int
            smallIcon, PendingIntent intent, RemoteViews contentView, RemoteViews bigContentView,
                                         boolean sound, boolean vibrate, boolean lights, int
                                                 largeIcon) {
        sample(ticker, title, content, smallIcon, intent, sound, vibrate, lights, largeIcon);
        Notification notification = builder.build();
        //在api大于等于16的情况下，如果视图超过一定范围，可以转变成bigContentView
        notification.bigContentView = bigContentView;
        notification.contentView = contentView;
        send(notification);
    }

    /**
     * 列表型通知
     */
    public void sendListNotification(String ticker, String title, String content, int smallIcon,
                                     PendingIntent intent, ArrayList<String> conntents, boolean
                                             sound, boolean vibrate, boolean lights, int
                                             largeIcon) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            Toast.makeText(context, "您的手机低于Android 4.1.2，不支持！！", Toast.LENGTH_SHORT).show();
            return;
        }
        sample(ticker, title, content, smallIcon, intent, sound, vibrate, lights, largeIcon);
        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle(builder);
        for (String conntent : conntents) {
            style.addLine(conntent);
        }
        style.setSummaryText(conntents.size() + "条消息");
        style.setBigContentTitle(title);
        Notification notification = style.build();
        send(notification);
    }

    /**
     * 双折叠双按钮通知
     */
    public void sendActionNotification(String ticker, String title, String content, int
            smallIcon, PendingIntent intent,
                                       int leftIcon, String leftText, PendingIntent leftPI,
                                       int rightIcon, String rightText, PendingIntent rightPI,
                                       boolean sound, boolean vibrate, boolean lights, int largeIcon) {
        sample(ticker, title, content, smallIcon, intent, sound, vibrate, lights, largeIcon);
        builder.addAction(leftIcon, leftText, leftPI);
        builder.addAction(rightIcon, rightText, rightPI);
        Notification notification = builder.build();
        send(notification);
    }

    /**
     * 带进度条的通知栏
     */
    public void sendProgressNotification(String ticker, String title, String content, int
            smallIcon, PendingIntent intent, boolean sound, boolean vibrate, boolean lights, int
                                                 largetIcon) {
        sample(ticker, title, content, smallIcon, intent, sound, vibrate, lights, largetIcon);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 100; i += 10) {
                    builder.setProgress(100, i, false);
                    send(builder.build());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //下载完成
                builder.setContentText("下载完成").setProgress(0, 0, false);
                send(builder.build());
            }
        }).start();
    }
}
