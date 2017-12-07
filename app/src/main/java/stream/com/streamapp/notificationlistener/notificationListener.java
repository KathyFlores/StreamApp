package stream.com.streamapp.notificationlistener;

import android.app.Notification;
import android.app.PendingIntent;
import android.media.MediaCodec;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import stream.com.streamapp.handlefunction.notificationHandler;

/**
 * Created by Alan on 2017/12/7.
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class notificationListener extends NotificationListenerService {
    public notificationHandler mHandleShare;
    public Handler mHandler;


    private void sendHandlerMessage(String title, String content)
    {
        mHandleShare= new notificationHandler();
        mHandler=mHandleShare.getHandler();

        String amount="";
        boolean isIn=false;//这里只能做支出

        Pattern r = Pattern.compile("你有一笔[0-9|\\.]*");
        Matcher m = r.matcher(content);
        if(m.find())
        {
            amount=m.group(0).substring(4);
            //TODO

        }
        else{
            return;
        }

    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.e("SevenNLS","Notification posted"+sbn.getPackageName());
        if (! "com.eg.android.AlipayGphone".equals(sbn.getPackageName()))
        {
            return;
        }
        //com.eg.android.AlipayGphone

//        if (!"com.tencent.mm".equals(sbn.getPackageName())) {
//            return;
//        }
        Notification notification = sbn.getNotification();
        if (notification == null) {
            return;
        }
        PendingIntent pendingIntent = null;
        // 当 API > 18 时，使用 extras 获取通知的详细信息
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Bundle extras = notification.extras;
            if (extras != null) {
                // 获取通知标题
                String title = extras.getString(Notification.EXTRA_TITLE, "");
                // 获取通知内容
                String content = extras.getString(Notification.EXTRA_TEXT, "");
                Log.e("SevenNLS","1 title:"+title+" ,content:"+content);

                sendHandlerMessage(title,content);
//                if (!TextUtils.isEmpty(content) && content.contains("[微信红包]")) {
//                    pendingIntent = notification.contentIntent;
//                }


            }
        } else {
            // 当 API = 18 时，利用反射获取内容字段

//            List<String> textList = getText(notification);
//            if (textList != null && textList.size() > 0) {
//                for (String text : textList) {
//                    if (!TextUtils.isEmpty(text) && text.contains("[微信红包]")) {
//                        pendingIntent = notification.contentIntent;
//                        break;
//                    }
//                }
//            }
        }
        // 发送 pendingIntent 以此打开微信
//        try {
//            if (pendingIntent != null) {
//                pendingIntent.send();
//            }
//        } catch (PendingIntent.CanceledException e) {
//            e.printStackTrace();
//        }


    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.e("SevenNLS","Notification removed");
    }
    @Override
    public void onListenerConnected()
    {
        Log.e("SevenNLS","connected");
    }
}