package stream.com.streamapp.sql;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import stream.com.streamapp.constant.regex;
import stream.com.streamapp.exception.internetError;


public class query {
    /*
    ** server url
     */
    static final private String mUrl="http://47.95.245.4/query.php";

    /*
    ** Return Answer
     */
    static private String mAnswer="";


    /*
    ** Call back Method
     */
    Handler mHandler = new Handler(Looper.getMainLooper())
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            mAnswer = data.getString("ans");
            Log.e("asd",mAnswer);
        }
    };
    /*
    ** Wrap Thread
     */
    private class myThread extends Thread{
        final private String url;
        final private String query;
        myThread(String url,String query)
        {
            this.url=url;
            this.query=query;
        }
        @Override
        public void run()
        {
            try{

                OkHttpClient mClient=new OkHttpClient();
                RequestBody body = new FormBody.Builder()
                        .add("content", query)
                        .build();
                Request tRequest = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                Call call = mClient.newCall(tRequest);
                Response tResponse =  call.execute();
                String ans= tResponse.body().string();
                Bundle data = new Bundle();
                data.putString("ans",ans);
                mAnswer=ans;
            }
            catch(IOException e)
            {
                Log.e("fff","1111");
                e.printStackTrace();
            }
            catch(Exception e)
            {
                Log.e("fff","*****");
                e.printStackTrace();
            }
        }
    }


    public String select(String tableName, String query) throws InterruptedException ,internetError{
        mAnswer = "";
        myThread tThread=new myThread(mUrl,query);
        tThread.start();
        tThread.join();
        if (mAnswer.equals(""))
        {
            throw new internetError("请检查网络连接");
        }
        return mAnswer;

    }
}
