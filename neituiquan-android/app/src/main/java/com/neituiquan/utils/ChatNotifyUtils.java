package com.neituiquan.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import com.neituiquan.App;
import com.neituiquan.work.MainActivity;
import com.neituiquan.work.R;


/**
 * Created by Augustine on 2018/7/13.
 * <p>
 * email:nice_ohoh@163.com
 *
 * 新消息通知震动
 */

public class ChatNotifyUtils {

    /**
     * 如果在活动中，仅震动
     */
    public static final int ACTIVITY = 113;//活动中

    /**
     * 不可见状态，仅震动
     */
    public static final int NO_VISIBLE = 332;//不可见

    /**1
     * 后台状态，声音 + 通知 + 震动
     */
    public static final int PAUSE = 319;//后台状态

    /**
     * 当前状态
     */
    public static int CURRENT_STATE = ACTIVITY;

    public static void chatNotify(Context context,String msg,String fromNickName,String fromHeadImg){
        switch (CURRENT_STATE){
            case ACTIVITY:
                playVibrator();
                break;
            case NO_VISIBLE:
                playSound();
                playVibrator();
                break;
            case PAUSE:
                createNotificationLayout(context,msg,fromNickName,fromHeadImg);
                break;
        }
    }


    private static void playSound(){
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(App.getAppInstance(), notification);
        r.play();
    }

    private static void playVibrator(){
        Vibrator vibrator = (Vibrator) App.getAppInstance().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);
    }


    private static void createNotificationLayout(Context context, String msg, String fromNickName, String fromHeadImg){
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setAutoCancel(false)
                .setContentText(msg)
                .setContentTitle(fromNickName)
                .setTicker(fromNickName + "发来一条消息")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent);
        manager.notify(5389741, builder.build());
    }
}
