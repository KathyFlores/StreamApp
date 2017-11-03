package com.terminalstream.stream;

/**
 * Created by KathyF on 2017/10/22.
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;

public class login extends AppCompatActivity implements View.OnClickListener {

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
        if(password.equals(""))//TODO:||don't pass
        {
            passwordET.setText(R.string.passwordError);
            return ;
        }
//      TODO:else pass
        Intent intent = new Intent(login.this,bill.class) ;    //切换Login Activity至User Activity
        startActivity(intent);
        finish();


    }
}
