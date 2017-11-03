package com.terminalstream.stream;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class add_bill extends AppCompatActivity implements View.OnClickListener {
    ImageView backBTN = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbill);
        backBTN = (ImageView) findViewById(R.id.back_button);
        backBTN.setVisibility(View.VISIBLE);
        backBTN.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        finish();


    }
}
