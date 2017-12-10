package stream.com.streamapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.lang.ref.WeakReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import stream.com.streamapp.constant.regex;
import stream.com.streamapp.sql.query;

class SignupAsyncTask extends AsyncTask<Object,Object,Object>
{
    private static ProgressDialog sDialog;
    String userName,passwd,phoneNum;
    private final WeakReference<Activity> mActivity;
    boolean result = true;
    private int errorType=-1;

    SignupAsyncTask(Signup activity, String userName,String passwd,String phoneNum)
    {
        this.userName=userName;
        this.passwd=passwd;
        this.phoneNum=phoneNum;
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
        String sqlQuery1="insert into user values(null,\""+userName+"\",\""+passwd+"\",\""+phoneNum+"\");";
        Log.e("fff","sqlins:"+sqlQuery1);
        String sqlQuery2="select passwd from user where name = \"" + userName + "\";";
        try {
            String rePd=tQuery.select("user",sqlQuery2);
            Pattern r = Pattern.compile(regex.passwdPattern);
            Matcher m = r.matcher(rePd);
            if(m.find())
            {
                String replaceBefore=m.group(0);
                String res=m.group(0).replaceAll("(passwd|<br>)","");
                Log.e("fff","qqq:"+replaceBefore+","+res);
                Log.e("shina",m.group(0));
                if(!res.equals(""))
                {
                    result = false;
                    errorType=0;
                    return result;
                }
                else{
                    ;
                }
            }
            Log.e("ppp","999");

            tQuery.select("user",sqlQuery1);
            rePd=tQuery.select("user",sqlQuery2);
            r = Pattern.compile(regex.passwdPattern);
            m = r.matcher(rePd);
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
                    errorType=1;
                }
            }
            else{
                result = false;
                errorType=1;
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
            ((Signup)mActivity.get()).jump();
        }
        else{
            ((Signup)mActivity.get()).SignupError(0);
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

public class Signup extends AppCompatActivity {
    RelativeLayout signupBTN = null;
    EditText usernameET = null;
    EditText passwordET = null;
    EditText phoneET = null;
    CircularProgressView progressView = null;
    TextView signup = null;
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initView();
        setListener();
    }
    private void initView(){
        signupBTN = (RelativeLayout)findViewById(R.id.signupButton);
        signup = (TextView)findViewById(R.id.signUpTXT);
        usernameET = (EditText) findViewById(R.id.username);
        passwordET = (EditText) findViewById(R.id.password);
        progressView = (CircularProgressView) findViewById(R.id.loading);
        phoneET = (EditText) findViewById(R.id.phone);

    }
    public boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile(regex.phonePattern); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }
    protected void jump()
    {
        progressView.setVisibility(View.GONE);
        progressView.stopAnimation();
        signup.setText(R.string.signupSuccess);
        signup.setVisibility(View.VISIBLE);
        Intent intent = new Intent(Signup.this,login.class);
        startActivity(intent);
        finish();
    }
    protected void SignupError(int errorType)
    {
        progressView.setVisibility(View.GONE);
        progressView.stopAnimation();
        signup.setVisibility(View.VISIBLE);
        switch(errorType)
        {
            case 0://username taken
                Toast.makeText(Signup.this,R.string.usernameTaken,Toast.LENGTH_LONG).show();
                break;

            case 1:
                Toast.makeText(Signup.this,R.string.usernameTaken,Toast.LENGTH_LONG).show();
        }
    }
    private void setListener(){
        signupBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean success;

                int errorType=-1;//0 用户名被占用
                String username = usernameET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();
                String phone = phoneET.getText().toString().trim();
                signup.setVisibility(View.GONE);
                progressView.startAnimation();
                progressView.setVisibility(View.VISIBLE);
                if(!isMobile(phone))
                {
                    success = false;
                    progressView.setVisibility(View.GONE);
                    progressView.stopAnimation();
                    signup.setVisibility(View.VISIBLE);
                    Toast.makeText(Signup.this,R.string.phoneError,Toast.LENGTH_LONG).show();
                }
                else if(password.length()<8) {
                    success = false;
                    progressView.setVisibility(View.GONE);
                    progressView.stopAnimation();
                    signup.setVisibility(View.VISIBLE);
                    Toast.makeText(Signup.this,R.string.passwordTooShort,Toast.LENGTH_LONG).show();
                }
                else {

                    SignupAsyncTask myTask = new SignupAsyncTask(Signup.this, username, password,phone);
                    myTask.execute();


                    /*success=true;
                    if(success)
                    {
                        progressView.setVisibility(View.GONE);
                        progressView.stopAnimation();
                        signup.setText(R.string.signupSuccess);
                        signup.setVisibility(View.VISIBLE);
                        Intent intent = new Intent(Signup.this,login.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        progressView.setVisibility(View.GONE);
                        progressView.stopAnimation();
                        signup.setVisibility(View.VISIBLE);
                        switch(errorType)
                        {
                            case 0://username taken
                                Toast.makeText(Signup.this,R.string.usernameTaken,Toast.LENGTH_LONG).show();

                        }
                    }*/
                }
            }
        });
    }
}
