package stream.com.streamapp;

/**
 * Created by Alan on 2017/11/5.
 */

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
import stream.com.streamapp.constant.regex;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.mob.MobSDK;
import com.mob.tools.MobUIShell;

import stream.com.streamapp.home.BasicActivity;
import stream.com.streamapp.sql.query;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class LoginAsyncTask extends AsyncTask<Object,Object,Object>
{
    private static ProgressDialog sDialog;
    String userName,passwd;
    private final WeakReference<Activity> mActivity;
    boolean result = true;

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
            //    Log.e("shina",m.group(0));
                if(res.equals(passwd))
                {
                    result = true;
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
        //Log.e("fff","****"+result);
        return result;
    }
    @Override
    protected void onPostExecute(Object result)
    {
       // Log.e("yyy","finish");
        if((boolean)result)
        {
            ((login)mActivity.get()).jump();
        }
        else{
            ((login)mActivity.get()).passwdError();
        }
    }
}





public class login extends AppCompatActivity {

    boolean finish = false;
    boolean ok = false;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MobSDK.init(this,appKey,privateKey);
        //SMSSDK.initSDK(this,appKey,privateKey,true);
        initView();
        setListener();

        if (Build.VERSION.SDK_INT >= 23) {
            int readPhone = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
            int receiveSms = checkSelfPermission(Manifest.permission.RECEIVE_SMS);
            int readSms = checkSelfPermission(Manifest.permission.READ_SMS);
            int readContacts = checkSelfPermission(Manifest.permission.READ_CONTACTS);
            int readSdcard = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);

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
                return;
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
                    //TODO:调用相机，刷脸登录
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
                            //phone=cphone;
                            // 提交用户信息
                            //registerUser(country, phone);
                            if(event==SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE)
                            {
                         //       Log.e("fff","yanzhengwanle"+cphone);
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
                                            //TODO:修改密码
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
                //
                /*
                final AlertDialog.Builder builder = new AlertDialog.Builder(login.this);
                //builder.setMessage("hihi");
                View view = LayoutInflater.from(login.this).inflate(R.layout.forget_password,null);
                final EditText phone = (EditText)view.findViewById(R.id.phone);
                final EditText safecode =(EditText)view.findViewById(R.id.safecode);
                final Button sendBTN = (Button)view.findViewById(R.id.sendBTN);

                sendBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO:发送验证码：
                        String phoneInput = phone.getText().toString().trim();
                        Log.e("debug",phoneInput);
                    }
                });
                builder.setView(view);
                builder.setNegativeButton("取消",null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO:确认是否匹配
                        String safecodeInput = safecode.getText().toString().trim();
                        Log.e("debug",safecodeInput);
                        //如果匹配

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
                                    //TODO:修改密码
                                }




                            }
                        });
                        newbuilder.create().show();
                        //否则没有动作
                    }
                });

                //builder.show();
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                */
            }
        });
    }

    public void jump()
    {
        loginPromptET.setVisibility(View.GONE);
        progressView.setVisibility(View.GONE);
        progressView.stopAnimation();
        loginTXT.setText(R.string.loginSuccess);
        loginTXT.setVisibility(View.VISIBLE);
        //TODO:user_id
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

//    public void ShowTip(String strMsg)
//    {
//        Log.e("fff",strMsg);
//    }
}
