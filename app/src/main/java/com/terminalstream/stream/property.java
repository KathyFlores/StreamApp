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

public class property extends AppCompatActivity implements View.OnClickListener {

    TextView billBTN = null;
    TextView propertyBTN = null;
    TextView chartBTN = null;
    TextView userBTN = null;
    ImageView addBTN = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.property);
        billBTN = (TextView) findViewById(R.id.text_bill);
        billBTN.setOnClickListener(this);
        Context mcontext=getApplicationContext();
        Drawable drawable= ContextCompat.getDrawable(mcontext,R.drawable.property_selected);
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        propertyBTN = (TextView) findViewById(R.id.text_property);
        propertyBTN.setOnClickListener(this);
        propertyBTN.setCompoundDrawables(null,drawable,null,null);
        chartBTN = (TextView) findViewById(R.id.text_chart);
        chartBTN.setOnClickListener(this);
        userBTN = (TextView) findViewById(R.id.text_user);
        userBTN.setOnClickListener(this);
        addBTN = (ImageView) findViewById(R.id.add);
        addBTN.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.text_bill:
                Intent intent_bill = new Intent(property.this, bill.class);
                startActivity(intent_bill);
                finish();
                break;
            case R.id.text_property:
                Intent intent_property = new Intent(property.this, property.class) ;    //切换Login Activity至User Activity
                startActivity(intent_property);
                finish();
                break;
            case R.id.add:
                Intent intent_add = new Intent(property.this, add_bill.class) ;    //切换Login Activity至User Activity
                startActivity(intent_add);

                break;
            case R.id.text_chart:
                Intent intent_chart = new Intent(property.this, chart.class) ;    //切换Login Activity至User Activity
                startActivity(intent_chart);
                finish();
                break;
            case R.id.text_user:
                Intent intent_user = new Intent(property.this, user.class) ;    //切换Login Activity至User Activity
                startActivity(intent_user);
                finish();
                break;
        }

    }
}
