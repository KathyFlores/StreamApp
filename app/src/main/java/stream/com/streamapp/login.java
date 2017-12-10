package stream.com.streamapp;

/**
 * Created by Alan on 2017/11/5.
 */

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import stream.com.streamapp.constant.regex;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.mob.MobSDK;
import com.mob.tools.MobUIShell;

import org.json.JSONException;
import org.json.JSONObject;

import stream.com.streamapp.home.BasicActivity;
import stream.com.streamapp.sql.query;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class LoginAsyncTask extends AsyncTask<Object,Object,Object>
{
    private static ProgressDialog sDialog;
    String userName,passwd;
    private final WeakReference<Activity> mActivity;
    boolean result = true;
    private int user_id;

    LoginAsyncTask(login activity, String userName,String passwd)
    {
        this.userName=userName;
        this.passwd=passwd;
        mActivity=new WeakReference<Activity>(activity);
    }

    @Override
    protected void onPreExecute()
    {

    }
    @Override
    protected Object doInBackground(Object... objects) {
        query tQuery = new query();
        String sqlQuery="select passwd from user where name = \"" + userName + "\";";
        try {
            String rePd=tQuery.select("user",sqlQuery);
            Pattern r = Pattern.compile(regex.passwdPattern);
            Matcher m = r.matcher(rePd);
            if(m.find())
            {

                String res=m.group(0).replaceAll("(passwd|<br>)","");
                if(res.equals(passwd))
                {
                    sqlQuery="select id from user where name = \"" + userName + "\";";
                    rePd=tQuery.select("user",sqlQuery);
                    //<br>id<br>1<br>
                    r=Pattern.compile(regex.idPattern);
                    m=r.matcher(rePd);
                    if(m.find())
                    {
                        res=m.group(0).replaceAll("(id|<br>)","");
                        user_id=Integer.parseInt(res);
                        result=true;
                    }
                    else{
                        result=false;
                    }
                }
                else{
                    result = false;
                }
            }
            else{
                result = false;
            }
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        return result;
    }
    @Override
    protected void onPostExecute(Object result)
    {
        if((boolean)result)
        {
            ((login)mActivity.get()).jump(user_id);
        }
        else{
            ((login)mActivity.get()).passwdError();
        }
    }
}





public class login extends AppCompatActivity {

    boolean finish = false;
    boolean ok = false;
    String mFilePath="/temp.jpeg";

    public static int getUser_id() {
        return user_id;
    }

    public static void setUser_id(int user_id) {
        login.user_id = user_id;
    }

    static int user_id = 0;
    RelativeLayout loginBTN = null;
    EditText usernameET = null;
    EditText passwordET = null;
    TextView loginPromptET = null;
    CircularProgressView progressView = null;
    TextView loginTXT = null;
    TextView signUpBTN=null;
    TextView forgetPasswdBTN=null;
    ImageView faceBTN = null;
    private String appKey="22cd1a36f1a40";
    private String privateKey="cfcd48435cfa42c9b518f511d1c471f0";
    String phone="11";
    private final OkHttpClient client = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MobSDK.init(this,appKey,privateKey);
        initView();
        setListener();



        if (Build.VERSION.SDK_INT >= 23) {
            int readPhone = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
            int receiveSms = checkSelfPermission(Manifest.permission.RECEIVE_SMS);
            int readSms = checkSelfPermission(Manifest.permission.READ_SMS);
            int readContacts = checkSelfPermission(Manifest.permission.READ_CONTACTS);
            int readSdcard = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            int hasNotification=checkSelfPermission(Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE);

            int requestCode = 0;
            ArrayList<String> permissions = new ArrayList<String>();
            if (readPhone != PackageManager.PERMISSION_GRANTED) {
                requestCode |= 1 << 0;
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (receiveSms != PackageManager.PERMISSION_GRANTED) {
                requestCode |= 1 << 1;
                permissions.add(Manifest.permission.RECEIVE_SMS);
            }
            if (readSms != PackageManager.PERMISSION_GRANTED) {
                requestCode |= 1 << 2;
                permissions.add(Manifest.permission.READ_SMS);
            }
            if (readContacts != PackageManager.PERMISSION_GRANTED) {
                requestCode |= 1 << 3;
                permissions.add(Manifest.permission.READ_CONTACTS);
            }
            if (readSdcard != PackageManager.PERMISSION_GRANTED) {
                requestCode |= 1 << 4;
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (requestCode > 0) {
                String[] permission = new String[permissions.size()];
                this.requestPermissions(permissions.toArray(permission), requestCode);
            }
            if(hasNotification!=PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
                startActivity(intent);
            }
        }
    }
    private void initView(){
        loginBTN = (RelativeLayout) findViewById(R.id.loginButton);

        usernameET = (EditText) findViewById(R.id.username);
        passwordET = (EditText) findViewById(R.id.password);
        loginPromptET = (TextView) findViewById(R.id.login_prompt);
        progressView = (CircularProgressView) findViewById(R.id.loading);
        loginTXT = (TextView) findViewById(R.id.loginTXT);
        signUpBTN=(TextView)findViewById(R.id.signUp);
        forgetPasswdBTN =(TextView)findViewById(R.id.forgetPassword);
        faceBTN=(ImageView)findViewById(R.id.face);
    }
    private void setListener(){
        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginTXT.setVisibility(View.GONE);
                progressView.startAnimation();
                progressView.setVisibility(View.VISIBLE);
                loginPromptET.setVisibility(View.GONE);
                String username = usernameET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();
                if (username.equals("")) {
                    loginPromptET.setText(R.string.usernameError);
                    loginPromptET.setVisibility(View.VISIBLE);
                    progressView.setVisibility(View.GONE);
                    progressView.stopAnimation();
                    loginTXT.setVisibility(View.VISIBLE);
                    return;
                }
                LoginAsyncTask myTask = new LoginAsyncTask(login.this, username, password);
                myTask.execute();
            }
        });
        faceBTN.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                loginPromptET.setVisibility(View.GONE);
                String username = usernameET.getText().toString().trim();
                if(username.equals("")){
                    loginPromptET.setText(R.string.usernameError);
                    loginPromptET.setVisibility(View.VISIBLE);
                    //return;
                }
                else{

                    openCamera();
                }
            }
        });
        signUpBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this,Signup.class) ;    //切换Login Activity至User Activity
                startActivity(intent);

            }
        });
        forgetPasswdBTN.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                RegisterPage registerPage = new RegisterPage();
                registerPage.setRegisterCallback(new EventHandler() {
                    public void afterEvent(int event, int result, Object data) {
                        // 解析注册结果
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            @SuppressWarnings("unchecked")
                            HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
                            String country = (String) phoneMap.get("country");
                            final String cphone = (String) phoneMap.get("phone");
                            if(event==SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE)
                            {
                                View newView = LayoutInflater.from(login.this).inflate(R.layout.new_password,null);
                                final EditText newpassword = (EditText)newView.findViewById(R.id.newpassword);
                                final AlertDialog.Builder newbuilder = new AlertDialog.Builder(login.this);
                                newbuilder.setView(newView);
                                newbuilder.setNegativeButton("取消",null);
                                newbuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String newPassword = newpassword.getText().toString().trim();
                                        if(newPassword.length()<8)
                                        {
                                            Toast.makeText(login.this,R.string.passwordTooShort,Toast.LENGTH_LONG).show();
                                            newbuilder.setCancelable(false);
                                        }
                                        else{

                                            String updateSql="update user set passwd = \""+newPassword+"\" where phone = \""+cphone+"\";";
                                         //   Log.e("fff","sql:"+updateSql);
                                            query tQuery = new query();
                                            try {
                                                tQuery.select("user",updateSql);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }




                                    }
                                });
                                newbuilder.create().show();




                            }
                        }

                    }
                });

                registerPage.show(login.this);
            }
        });
    }

    public void jump(int user_id)
    {
        loginPromptET.setVisibility(View.GONE);
        progressView.setVisibility(View.GONE);
        progressView.stopAnimation();
        loginTXT.setText(R.string.loginSuccess);
        loginTXT.setVisibility(View.VISIBLE);
        //TODO:user_id
        setUser_id(user_id);
        Log.e("fff","userid:"+getUser_id());


        Intent intent = new Intent(login.this,BasicActivity.class) ;    //切换Login Activity至User Activity
        startActivity(intent);
        finish();
    }
    protected void passwdError()
    {
        loginPromptET.setText(R.string.passwordError);
        loginPromptET.setVisibility(View.VISIBLE);
        progressView.setVisibility(View.GONE);
        progressView.stopAnimation();
        loginTXT.setVisibility(View.VISIBLE);
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpeg",
                storageDir
        );
        mFilePath = image.getAbsolutePath();
        return image;
    }
    /*
     *  调用系统相机 拍摄完成后用回调函数解决后续逻辑问题
     */
    private void openCamera()
    {
        Intent id=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (id.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e("wrong file", ex.getMessage(), ex);
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "stream.com.streamapp.fileprovider", photoFile);
                id.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                id.putExtra("android.intent.extras.CAMERA_FACING_FRONT", 1);
                id.putExtra("android.intent.extras.CAMERA_FACING", 1);
//                id.putExtra("crop", "true");
//                id.putExtra("aspectX", 1);
//                id.putExtra("aspectY", 1);
//                id.putExtra("outputX", 100);
//                id.putExtra("outputY", 100);
//                id.putExtra("scale", true);
                startActivityForResult(id, 1);
            }
        }
    }
    private void checkFace(File file)
    {
        Log.e("fff","resultfunc4");
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"), file);


        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
///                .addPart(
//                        Headers.of("Content-Disposition", "form-data; name=\"file\"; filename=\"" + fileName + "\""),
//                        RequestBody.create(MEDIA_TYPE_PNG, file))
//                .addPart(
//                        Headers.of("Content-Disposition", "form-data; name=\"imagetype\""),
//                        RequestBody.create(null, imageType))
//                .addPart(
//                        Headers.of("Content-Disposition", "form-data; name=\"userphone\""),
//                        RequestBody.create(null, userPhone))

                .addFormDataPart("file", "1", fileBody)
                .addFormDataPart("id", "1")//Integer.toString(getUser_id()))
                .addFormDataPart("isInsert", "0")
                .build();

        Request request = new Request.Builder()
                .url("http://10.214.129.171:9999/upload")
                .post(requestBody)
                .build();

        Response response;
        try {
            response = client.newCall(request).execute();
            String jsonString = response.body().string();
            Log.d("fff"," upload jsonString ="+jsonString);

            JSONObject jsonObject = new JSONObject(jsonString);
            int errorCode = jsonObject.getInt("errorCode");
            if(errorCode == 0){

                //Log.d(TAG," upload data ="+jsonObject.getString("data"));
                //return jsonObject.getString("data");
            }

        } catch (IOException e) {
            Log.d("ff","upload IOException ",e);
        }catch (JSONException e){
            Log.d("fff","upload JSONException ",e);
        }
        return;
    }


    /*
     * 拍摄完成后的回调函数
     */
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        Log.e("fff","resultfunc1");
        if (resultCode==RESULT_OK)
        {
            Log.e("fff","resultfunc2");

            if(requestCode==1)
            {
                Log.e("fff","resultfunc3");
                FileInputStream fis = null;
                try {
                    final File ff=new File(mFilePath);

                    Thread tThread=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            checkFace(ff);
                        }
                    });
                    tThread.start();
//                    fis = new FileInputStream(mFilePath);
//                    Bitmap bitmap = BitmapFactory.decodeStream(fis);
//                    Matrix m=new Matrix();
//                    //m.postRotate(90);
//                    WindowManager wm = this.getWindowManager();
//                    int width = wm.getDefaultDisplay().getWidth();
//                    int height = wm.getDefaultDisplay().getHeight();
//                    int picwidth= (int) (width);
//                    int picheight= (int) (height*0.4);
//                    Bitmap xbitmap=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),m,true);
//                    Bitmap cropped = ThumbnailUtils.extractThumbnail(xbitmap, picwidth, picheight);



                    //mPic.setImageBitmap(cropped);
                } finally {
                    //fis.close();// 关闭流
                }
            }
        }
    }

//    public void ShowTip(String strMsg)
//    {
//        Log.e("fff",strMsg);
//    }
}
