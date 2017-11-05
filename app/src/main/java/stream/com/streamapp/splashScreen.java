package stream.com.streamapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;

public class splashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //to avoid the time spent on setContentView
        //setContentView(R.layout.splashscreen);
        if((getIntent().getFlags()&Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)!=0) {
            finish();
            return;
        }
        new Handler().postDelayed(new Runnable(){
            public void run(){
                Intent i = new Intent(splashScreen.this, login.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
