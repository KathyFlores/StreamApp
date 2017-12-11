package stream.com.streamapp.sql;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


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
                /*
                ** Request body
                 */
                RequestBody body = new FormBody.Builder()
                        .add("content", query)
                        .build();
                Log.e("fff","query:"+query);
//                Log.e("fff",body.toString());
                /*
                ** headers, url, body should be here
                 */
                Request tRequest = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                Call call = mClient.newCall(tRequest);
                Response tResponse =  call.execute();
//                Log.e("eee","***"+tResponse.toString());
                String ans= tResponse.body().string();
//                Log.e("eee",ans);

                //Message msg = new Message();
                Bundle data = new Bundle();
                data.putString("ans",ans);
//                Log.e("fff",ans);
                //mHandler.sendMessage(msg);
                mAnswer=ans;
                Log.e("fff",ans);
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


    public String select(String tableName, String query) throws InterruptedException {
        mAnswer = "";
        myThread tThread=new myThread(mUrl,query);
        tThread.start();
        while(mAnswer.equals(""))
        {
            Log.e("rrr","111");
        }
        Log.e("ppp",mAnswer);
        return mAnswer;

    }
}
