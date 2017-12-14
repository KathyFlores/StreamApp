package stream.com.streamapp.home;

/**
 * Created by WuYiQuan on 2017/12/13.
 */


import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.bumptech.glide.request.target.DrawableImageViewTarget;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import stream.com.streamapp.db.Assets;
import stream.com.streamapp.db.Bills;
import stream.com.streamapp.login;


public class UpdateData {
    /*
    ** server url
     */
    static final private String mUrl="http://47.95.245.4/query.php";

    /*
    ** Return Answer
     */




    /*
    ** Wrap Thread
     */
    private static class myThread extends Thread{
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


    public static void UploadBill() throws InterruptedException {
        List<Bills> bills;
        String query = "";
        //添加同步
        bills = DataSupport.where("user_id = ? and state = 1 ", String.valueOf(login.getUser_id())).find(Bills.class);
        for (int i = 0; i < bills.size(); i++) {
            query = "insert into Bills (id, user_id, amount, date, place, inOrOut, type, note, timeStamp ) values( " + bills.get(i).getId() + "," +
                    bills.get(i).getUser_id() + "," + bills.get(i).getAmount() + ", \"" + bills.get(i).getDate() + "\" ,\"" + bills.get(i).getPlace() + "\", \"" +
                    bills.get(i).getInOrOut() + "\",\"" + bills.get(i).getType() + "\", \"" + bills.get(i).getNote() + "\", \"" + bills.get(i).getTimeStamp() + "\" );";
            Log.d("query", query);
            ContentValues values = new ContentValues();
            values.put("state", 9);
            DataSupport.update(Bills.class, values, bills.get(i).getId());
            myThread tThread = new myThread(mUrl, query);
            tThread.start();
        }
        //修改同步
        bills = DataSupport.where("user_id = ? and state = 2 ", String.valueOf(login.getUser_id())).find(Bills.class);
        for (int i = 0; i < bills.size(); i++) {
            query = "UPDATE Bills set amount = " + bills.get(i).getAmount() + " ,date = '" + bills.get(i).getDate() + "' ,place = '" +
                    bills.get(i).getPlace() + "' ,inOrOut = '" + bills.get(i).getInOrOut() +"' ,type = '" + bills.get(i).getType() + "' ,note = '"
            + bills.get(i).getNote() + "' ,timeStamp = '" + bills.get(i).getTimeStamp() + "' WHERE id = " + bills.get(i).getId() + ";";
            Log.d("query1", query);
            ContentValues values = new ContentValues();
            values.put("state", 9);
            DataSupport.update(Bills.class, values, bills.get(i).getId());
            myThread tThread = new myThread(mUrl, query);
            tThread.start();
        }
        //删除同步
        bills = DataSupport.where("user_id = ? and state = 3 ", String.valueOf(login.getUser_id())).find(Bills.class);
        for (int i = 0; i < bills.size(); i++) {
            query = "DELETE FROM Bills where id = " + bills.get(i).getId() + ";";
            Log.d("query", query);
            DataSupport.delete(Bills.class, bills.get(i).getId());
            myThread tThread = new myThread(mUrl, query);
            tThread.start();
        }

        return;
    }

    public static void downloadBill() throws InterruptedException {

        return;
    }

    public static void UploadAssets() throws InterruptedException {
        List<Assets> assets;
        String query = "";
        assets = DataSupport.where("user_id = ? and state = 1 ", String.valueOf(login.getUser_id())).find(Assets.class);
        for (int i = 0; i < assets.size(); i++) {
            query = "insert into Assets (id, user_id, amount, date,inOrOut, type, timeStamp ) values( " + assets.get(i).getId() + "," +
                    assets.get(i).getUser_id() + "," + assets.get(i).getAmount() + ", \"" + assets.get(i).getDate() +  "\", \"" +
                    assets.get(i).getInOrOut() + "\",\"" + assets.get(i).getType() +  "\", \"" + assets.get(i).getTimeStamp() + "\" );";
            Log.d("query", query);
            ContentValues values = new ContentValues();
            values.put("state", 9);
            DataSupport.update(Assets.class, values, assets.get(i).getId());
            myThread tThread = new myThread(mUrl, query);
            tThread.start();
        }
        return;
    }
}
