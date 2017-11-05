package stream.com.streamapp;

/**
 * Created by Alan on 2017/11/5.
 */

import stream.com.streamapp.constant.regex;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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




public class login extends AppCompatActivity implements View.OnClickListener {

    boolean finish = false;
    boolean ok = false;

    Button loginBTN = null;
    EditText usernameET = null;
    EditText passwordET = null;
    TextView loginPromptET = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginBTN = (Button) findViewById(R.id.loginButton);
        loginBTN.setOnClickListener(this);
        usernameET = (EditText) findViewById(R.id.username);
        passwordET = (EditText) findViewById(R.id.password);
        loginPromptET = (TextView) findViewById(R.id.login_prompt);
    }
    @Override
    public void onClick(View v) {
        String username = usernameET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();
        if(username.equals(""))
        {
            loginPromptET.setText(R.string.usernameError);
            return ;
        }
        LoginAsyncTask myTask = new LoginAsyncTask(this,username,password);
        myTask.execute();

//      TODO:else pass




    }
    public void jump()
    {
        Intent intent = new Intent(login.this,bill.class) ;    //切换Login Activity至User Activity
        startActivity(intent);
        finish();
    }
    protected void passwdError()
    {
        loginPromptET.setText(R.string.passwordError);
    }
//    public void ShowTip(String strMsg)
//    {
//        Log.e("fff",strMsg);
//    }
}
