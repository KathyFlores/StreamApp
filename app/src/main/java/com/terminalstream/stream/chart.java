package com.terminalstream.stream;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class chart extends AppCompatActivity {

    private TextView billBTN = null;
    private TextView propertyBTN = null;
    private TextView chartBTN = null;
    private TextView userBTN = null;
    private ImageView addBTN = null;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

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
    private void initView(){
        billBTN = (TextView) findViewById(R.id.text_bill);
        mContext = getApplicationContext();
        propertyBTN = (TextView) findViewById(R.id.text_property);
        chartBTN = (TextView) findViewById(R.id.text_chart);
        Drawable drawable= ContextCompat.getDrawable(mContext,R.drawable.chart_selected);
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        chartBTN.setCompoundDrawables(null,drawable,null,null);
        userBTN = (TextView) findViewById(R.id.text_user);
        addBTN = (ImageView) findViewById(R.id.add);

    }
    private void setListener(){
        billBTN.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent_bill = new Intent(chart.this, bill.class);
                startActivity(intent_bill);
//                finish();
            }
        });

        propertyBTN.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent_property = new Intent(chart.this, property.class);
                startActivity(intent_property);
//                finish();
            }
        });

        chartBTN.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent_chart = new Intent(chart.this, chart.class) ;
                startActivity(intent_chart);
//                finish();
            }
        });

        userBTN.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent_chart = new Intent(chart.this, user.class) ;
                startActivity(intent_chart);
//                finish();
            }
        });

        addBTN.setOnClickListener(new ImageView.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent_add = new Intent(chart.this, add_bill.class) ;
                startActivity(intent_add);
            }
        });
    }

}
