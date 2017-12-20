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
import stream.com.streamapp.home.UpdateData;

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
        boolean isIn=false;

        Pattern r = Pattern.compile("你有一笔[0-9|\\.]*");
        Matcher m = r.matcher(content);
        if(m.find())
        {

            amount=m.group(0).substring(4);
            //TODO
            try {
                UpdateData.addBills(Double.parseDouble(amount),"out","alipay");
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }

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

        Notification notification = sbn.getNotification();
        if (notification == null) {
            return;
        }
        PendingIntent pendingIntent = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Bundle extras = notification.extras;
            if (extras != null) {
                String title = extras.getString(Notification.EXTRA_TITLE, "");
                String content = extras.getString(Notification.EXTRA_TEXT, "");

                sendHandlerMessage(title,content);


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