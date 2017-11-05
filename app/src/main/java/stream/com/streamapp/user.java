package stream.com.streamapp;

/**
 * Created by Alan on 2017/11/5.
 */


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class user extends AppCompatActivity {

    private TextView billBTN = null;
    private TextView propertyBTN = null;
    private TextView chartBTN = null;
    private TextView userBTN = null;
    private ImageView addBTN = null;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        initView();
        setListener();
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
//        System.out.println("onRestoreInstanceState");
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        System.out.println("onSaveInstanceState");
    }
    @Override
    protected void onPause() {
        super.onPause();
//        System.out.println("onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
//        System.out.println("onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
//        System.out.println("onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        System.out.println("onRestart");
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void initView(){
        billBTN = (TextView) findViewById(R.id.text_bill);
        mContext=getApplicationContext();
        propertyBTN = (TextView) findViewById(R.id.text_property);
        chartBTN = (TextView) findViewById(R.id.text_chart);
        userBTN = (TextView) findViewById(R.id.text_user);
        Drawable drawable= ContextCompat.getDrawable(mContext,R.drawable.user_selected);
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        userBTN.setCompoundDrawables(null,drawable,null,null);
        addBTN = (ImageView) findViewById(R.id.add);
    }
    private void setListener(){
        billBTN.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent_bill = new Intent(user.this, bill.class);
                startActivity(intent_bill);
                finish();
            }
        });

        propertyBTN.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent_property = new Intent(user.this, property.class);
                startActivity(intent_property);
                finish();
            }
        });

        chartBTN.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent_chart = new Intent(user.this, chart.class) ;
                startActivity(intent_chart);
                finish();
            }
        });

        userBTN.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent_chart = new Intent(user.this, user.class) ;
                startActivity(intent_chart);
                finish();
            }
        });

        addBTN.setOnClickListener(new ImageView.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent_add = new Intent(user.this, add_bill.class) ;
                startActivity(intent_add);
            }
        });
    }
}
