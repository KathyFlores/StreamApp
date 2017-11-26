package stream.com.streamapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

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
    private void setListener(){
        signupBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean success;
                //TODO:增加错误类型
                int errorType=-1;//0 用户名被占用
                String username = usernameET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();
                String phone = phoneET.getText().toString().trim();
                signup.setVisibility(View.GONE);
                progressView.startAnimation();
                progressView.setVisibility(View.VISIBLE);
                if(password.length()<8) {
                    success = false;
                    progressView.setVisibility(View.GONE);
                    progressView.stopAnimation();
                    signup.setVisibility(View.VISIBLE);
                    Toast.makeText(Signup.this,R.string.passwordTooShort,Toast.LENGTH_LONG).show();
                }
                else {
                    //TODO: send data to database

                    success=true;
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
                                //TODO: add error type
                        }
                    }
                }
            }
        });
    }
}
