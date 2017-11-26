package stream.com.streamapp;

/**
 * Created by Alan on 2017/11/5.
 */

import stream.com.streamapp.constant.regex;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

import stream.com.streamapp.home.BasicActivity;
import stream.com.streamapp.sql.query;

import java.lang.ref.WeakReference;
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
//        if(null == sDialog)
//        {
//            sDialog = new ProgressDialog(mActivity.get());
//        }
//        sDialog.setTitle("Please wait...");
//        sDialog.setMessage("Logging...");
//        sDialog.setCancelable(false);
//        sDialog.show();

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
                Log.e("shina",m.group(0));
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
        Log.e("fff","****"+result);
        return result;
    }
    @Override
    protected void onPostExecute(Object result)
    {
        Log.e("yyy","finish");
        if((boolean)result)
        {
            ((login)mActivity.get()).jump();
        }
        else{
            ((login)mActivity.get()).passwdError();
        }
//        String strMsg;
//        if(result.equals(true))
//        {
//            strMsg = "success";
//        }
//        else
//            strMsg = "failed";
//        ((login)mActivity.get()).ShowTip(strMsg);
//        //取消进度条
//        sDialog.cancel();
    }
}




public class login extends AppCompatActivity {

    boolean finish = false;
    boolean ok = false;

    RelativeLayout loginBTN = null;
    EditText usernameET = null;
    EditText passwordET = null;
    TextView loginPromptET = null;
    CircularProgressView progressView = null;
    TextView loginTXT = null;
    TextView signUpBTN=null;
    TextView forgetPasswdBTN=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        setListener();
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

                    progressView.setVisibility(View.GONE);
                    progressView.stopAnimation();
                    loginTXT.setVisibility(View.VISIBLE);
                    return;
                }
                LoginAsyncTask myTask = new LoginAsyncTask(login.this, username, password);
                myTask.execute();
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
        Intent intent = new Intent(login.this,BasicActivity.class) ;    //切换Login Activity至User Activity
        startActivity(intent);
        finish();
    }
    protected void passwdError()
    {
        loginPromptET.setText(R.string.passwordError);

        progressView.setVisibility(View.GONE);
        progressView.stopAnimation();
        loginTXT.setVisibility(View.VISIBLE);
    }
//    public void ShowTip(String strMsg)
//    {
//        Log.e("fff",strMsg);
//    }
}
