package stream.com.streamapp.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import stream.com.streamapp.constant.regex;
import stream.com.streamapp.handlefunction.smsHandler;

import static android.provider.Telephony.Sms.Intents.SMS_RECEIVED_ACTION;

/**
 * Created by Alan on 2017/12/6.
 */

public class smsRecevier extends BroadcastReceiver{
    public smsHandler mSmsHandler;
    private Handler mHandler;

    private void sendHandlerMessage(String sender,String content)
    {
        mSmsHandler=new smsHandler();
        mHandler=mSmsHandler.getHandler();
        String account="";
        String amount="";
        boolean isIn=false;
        boolean isValid=false;
        if(sender.equals("95533"))//建设银行
        {
            Pattern r = Pattern.compile("您尾号[0-9]*");
            Matcher m = r.matcher(content);
            if (m.find())
            {
                account=m.group(0).substring(3);
                if (account.length()!=4)
                {
                    return;
                }
                else{
                    if(content.contains("支出"))
                    {
                        isIn=false;
                    }
                    else if(content.contains("收入")||content.contains("存入"))
                    {
                        isIn=true;
                    }
                    else{
                        return;
                    }
                    r=Pattern.compile("人民币\\d*\\.\\d*");
                    m=r.matcher(content);
                    if(m.find())
                    {
                        amount=m.group(0).substring(3);
                        //System.out.println("acount:"+account+",amount:"+amount+",sender:"+sender);
                    }
                    else{
                        return;
                        //System.out.println("acount:"+account+",amount:"+amount+",sender:"+sender);
                    }
                    //amount=m.group(0).substring(3);
                    Log.e("sms","acount:"+account+",amount:"+amount+",sender:"+sender);
                    // TO DO


                }
            }
        }


    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(SMS_RECEIVED_ACTION))
        {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            for (Object pdu : pdus) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                String sender = smsMessage.getDisplayOriginatingAddress();
                if(sender.equals("95533"))
                {
                    //短信内容
                    String content = smsMessage.getDisplayMessageBody();
                    long date = smsMessage.getTimestampMillis();
                    Date tiemDate = new Date(date);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time = simpleDateFormat.format(tiemDate);

                    Log.e("fff","Time:"+time+",sender:"+sender+",content:"+content);

                    //mMessageListener.handleSms(sms);
                    sendHandlerMessage(sender,content);
                }
                abortBroadcast();
            }
        }
    }
}
