package stream.com.streamapp;

/**
 * Created by Alan on 2017/11/5.
 */

import stream.com.streamapp.home.UpdateData;
import stream.com.streamapp.profile.photo;
import stream.com.streamapp.exception.internetError;

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
import android.content.ComponentName;
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
import android.text.TextUtils;
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
import org.litepal.crud.DataSupport;

import stream.com.streamapp.home.BasicActivity;
import stream.com.streamapp.sql.query;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class LoginAsyncTask extends AsyncTask<Object,Object,Object>
{
    String userName,passwd;
    private final WeakReference<Activity> mActivity;
    int result = 1;
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
                        result=1;
                    }
                    else{
                        result=0;
                    }
                }
                else{
                    result = 0;
                }
            }
            else{
                result = 0;
            }
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        catch(internetError e)
        {
            result=2;
        }
        return result;
    }
    @Override
    protected void onPostExecute(Object result)
    {
        if((int)result==1)
        {
            Bundle re=new Bundle();
            re.putString("Return","0");
            re.putInt("ID",user_id);
            Message msg=new Message();
            msg.setData(re);
            ((login)mActivity.get()).mHandler.sendMessage(msg);
        }
        else if ((int)result==0){
            Bundle re=new Bundle();
            re.putString("Return","2");
            Message msg=new Message();
            msg.setData(re);
            ((login)mActivity.get()).mHandler.sendMessage(msg);
        }
        else if((int)result==2)
        {
            Bundle re=new Bundle();
            re.putString("Return","3");
            Message msg=new Message();
            msg.setData(re);
            ((login)mActivity.get()).mHandler.sendMessage(msg);
        }
    }
}
public class login extends AppCompatActivity {

    boolean finish = false;
    boolean ok = false;
    String mFilePath="/temp.jpeg";
    private static String basicDir="";
    private static int user_id = 0;
    private static String userName="";
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
    private final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS).build();


    /*
     * @param   mHandler:  call back function call this to send message to main thread
     * @return  0： 登陆成功
     *          1:  faceError
     *          2:  passwordError
     *
     */
    public Handler mHandler=new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            Bundle bd=msg.getData();
            String content=bd.getString("Return");
            if(content.equals("0"))
            {


                int uid=bd.getInt("ID");
                //TODO:user_id
                setUser_id(uid);
                try {
                    UpdateData.downloadBill();
//                    UpdateData.downloadAssets();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (internetError e)
                {
                    loginPromptET.setText(R.string.internetError);
                    loginPromptET.setVisibility(View.VISIBLE);
                    progressView.setVisibility(View.GONE);
                    progressView.stopAnimation();
                    loginTXT.setVisibility(View.VISIBLE);
                    return;
                }
                //更新数据库

                Log.e("fff","userid:"+getUser_id());

                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                photo.setPath(storageDir.getAbsolutePath());
                photo.download(getUser_id());
                loginPromptET.setVisibility(View.GONE);
                progressView.setVisibility(View.GONE);
                progressView.stopAnimation();
                loginTXT.setText(R.string.loginSuccess);
                loginTXT.setVisibility(View.VISIBLE);

                Intent intent = new Intent(login.this,BasicActivity.class) ;    //切换Login Activity至User Activity
                startActivity(intent);
                finish();
            }
            else if(content.equals("1"))//face error
            {
                loginPromptET.setText(R.string.faceError);
                loginPromptET.setVisibility(View.VISIBLE);
                progressView.setVisibility(View.GONE);
                progressView.stopAnimation();
                loginTXT.setVisibility(View.VISIBLE);
            }
            else if(content.equals("2"))//password error
            {
                loginPromptET.setText(R.string.passwordError);
                loginPromptET.setVisibility(View.VISIBLE);
                progressView.setVisibility(View.GONE);
                progressView.stopAnimation();
                loginTXT.setVisibility(View.VISIBLE);
            }
            else if(content.equals("3"))
            {
                loginPromptET.setText(R.string.internetError);
                loginPromptET.setVisibility(View.VISIBLE);
                progressView.setVisibility(View.GONE);
                progressView.stopAnimation();
                loginTXT.setVisibility(View.VISIBLE);
            }

        }
    };


    public static String getBasicDir() {
        return basicDir;
    }
    public static void setBasicDir(String basicDir) {
        login.basicDir = basicDir;
    }
    public static int getUser_id() {
        return user_id;
    }
    public static void setUser_id(int user_id) {
        login.user_id = user_id;
    }
    public static String getUserName(){return login.userName;}
    public static void setUserName(String userName){login.userName=userName;}

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

        }
        if(!isEnabled())
        {
            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            startActivity(intent);
        }
    }
    private boolean isEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
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
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        setBasicDir(storageDir.getAbsolutePath());
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
                setUserName(username);
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
                setUserName(username);

                if(username.equals("")){
                    loginPromptET.setText(R.string.usernameError);
                    loginPromptET.setVisibility(View.VISIBLE);
                    //return;
                }
                else{
                    int id= 0;
                    try {
                        id = getid(username);
                        if(id==-1)
                        {
                            Bundle re=new Bundle();
                            re.putString("Return","1");
                            Message msg=new Message();
                            msg.setData(re);
                            mHandler.sendMessage(msg);
                        }
                        else{
                            openCamera(id);
                        }
                    } catch (stream.com.streamapp.exception.internetError internetError) {
                        Bundle re=new Bundle();
                        re.putString("Return","3");
                        Message msg=new Message();
                        msg.setData(re);
                        mHandler.sendMessage(msg);
                    }

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
                                            query tQuery = new query();
                                            try {
                                                tQuery.select("user",updateSql);
                                                String sqlQuery="select passwd from user where phone = \""+cphone+"\";";
                                                String html=tQuery.select("user",sqlQuery);
                                                Pattern r = Pattern.compile(regex.passwdPattern);
                                                Matcher m = r.matcher(html);
                                                if(m.find())
                                                {
                                                    String res=m.group(0).replaceAll("(passwd|<br>)","");
                                                    if(!res.equals(newPassword))
                                                    {
                                                        Toast.makeText(login.this,R.string.internetError,Toast.LENGTH_LONG).show();
                                                        newbuilder.setCancelable(false);
                                                    }
                                                    else{
                                                        Toast.makeText(login.this,R.string.changeSuccess,Toast.LENGTH_LONG).show();
                                                    }
                                                }


                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }catch( internetError e)
                                            {
                                                loginPromptET.setText(R.string.internetError);
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
    private int getid(String userName) throws internetError {
        query tQuery = new query();
        String sqlQuery="select id from user where name = \"" + userName + "\";";
        String rePd= null;
        try {
            rePd = tQuery.select("user",sqlQuery);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //<br>id<br>1<br>
        Pattern r=Pattern.compile(regex.idPattern);
        Matcher m=r.matcher(rePd);
        if(m.find())
        {
            String res=m.group(0).replaceAll("(id|<br>)","");
            return Integer.parseInt(res);
        }
        else{
            return -1;
        }
    }
    /*
     *  调用系统相机 拍摄完成后用回调函数解决后续逻辑问题
     */
    private void openCamera(int uid)
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
                setUser_id(uid);
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
                .addFormDataPart("file", "1", fileBody)
                .addFormDataPart("id", ""+getUser_id())//Integer.toString(getUser_id()))
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
            Pattern r = Pattern.compile("<p>.*?</p>");
            Matcher m = r.matcher(jsonString);
            if(m.find())
            {
                double emb=Double.valueOf(m.group(0).replace("<p>","").replace("</p>",""));
                if (emb<0.88)
                {
                    Bundle re=new Bundle();
                    re.putString("Return","0");
                    re.putInt("ID",getUser_id());
                    Message msg=new Message();
                    msg.setData(re);
                    mHandler.sendMessage(msg);
                }
                else{
                    Bundle re=new Bundle();
                    re.putString("Return","1");
                    Message msg=new Message();
                    msg.setData(re);
                    mHandler.sendMessage(msg);
                }
            }

        } catch (IOException e) {
            Log.d("ff","upload IOException ",e);
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

                } finally {
                    //fis.close();// 关闭流
                }
            }
        }
    }

}
