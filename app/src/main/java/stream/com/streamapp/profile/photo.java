package stream.com.streamapp.profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import stream.com.streamapp.login;
import android.content.Context;

/**
 * Created by Alan on 2017/12/13.
 */

public class photo {
    private static String path="";
    private static String savePath="";
    private static Boolean hasPhoto = new Boolean(false);
    private static final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();

    /*
     * if you want to know whether the user has photo, call this
     * @return the state
     */
    public static boolean getPhotoState()
    {
        synchronized (hasPhoto)
        {
            return hasPhoto;
        }

    }
    /*
     *  if you want to know the absolute path of the photo, call this
     *  @return the absolute path of the photo
     */
    public static String getPath()
    {
        return savePath;
    }

    /*
     *  if you want to flash photo, just call this
     *  @param  uid is the user's id
     */

    public static void download(int uid)
    {
        synchronized (hasPhoto)
        {
            hasPhoto=false;
            Thread t = new Thread(new Runnable() {
                private final String serverUrl="http://47.95.245.4:9999/getphoto/[%d]/";
                @Override
                public void run() {
                    String url=serverUrl.replace("[%d]",""+ login.getUser_id());
                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    Response response;
                    try {
                        response = client.newCall(request).execute();
                        InputStream inputStream = response.body().byteStream();//得到图片的流
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        File file = new File(path, "/"+login.getUser_id()+".jpg");
                        FileOutputStream fos = null;
                        try {
                            file.createNewFile();
                            fos = new FileOutputStream(file.getAbsolutePath());
                            if (fos != null) {
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                hasPhoto=true;
                                savePath=file.getAbsolutePath();
                                Log.e("fff",file.getAbsolutePath());
                                fos.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } catch (IOException e) {
                        Bundle re=new Bundle();
                        re.putString("Return","1");
                        Message msg=new Message();
                        msg.setData(re);
                        //mHandler.sendMessage(msg);
                        Log.d("ff", "upload IOException ", e);
                    }
                }
            });
            t.start();

        }
    }
    /*
     * don't call this
     */
    public static void setPath(String tpath)
    {
        path=tpath;
    }
}
