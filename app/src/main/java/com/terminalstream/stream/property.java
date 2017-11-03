package com.terminalstream.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;

public class property extends AppCompatActivity {

    private TextView billBTN = null;
    private TextView propertyBTN = null;
    private TextView chartBTN = null;
    private TextView userBTN = null;
    private ImageView addBTN = null;
    private Context mContext;
    private static RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private List<Integer> iconList;
    private List<Integer> categoryList;
    private List<String> dataList;
    private myAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);
        initData();
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
    private void initData(){
        dataList =new ArrayList<String>();
        iconList = new ArrayList<Integer>(Arrays.asList(R.drawable.alipay,R.drawable.alipay,R.drawable.alipay));
        categoryList = new ArrayList<Integer>(Arrays.asList(R.string.alipay,R.string.alipay,R.string.alipay));
        for(int i=0;i<3;i++)
        {
            dataList.add("test"+i*100);

        }
    }

    private void initView(){
        billBTN = (TextView) findViewById(R.id.text_bill);
        mContext = getApplicationContext();
        Drawable drawable= ContextCompat.getDrawable(mContext,R.drawable.property_selected);
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        propertyBTN = (TextView) findViewById(R.id.text_property);
        propertyBTN.setCompoundDrawables(null,drawable,null,null);
        chartBTN = (TextView) findViewById(R.id.text_chart);

        userBTN = (TextView) findViewById(R.id.text_user);

        addBTN = (ImageView) findViewById(R.id.add);

        recyclerView=(RecyclerView)findViewById(R.id.property_recycler);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter = new myAdapter());
    }

    private void setListener(){
        billBTN.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent_bill = new Intent(property.this, bill.class);
                startActivity(intent_bill);
//                finish();
            }
        });

        propertyBTN.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent_property = new Intent(property.this, property.class);
                startActivity(intent_property);
//                finish();
            }
        });

        chartBTN.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent_chart = new Intent(property.this, chart.class) ;
                startActivity(intent_chart);
//                finish();
            }
        });

        userBTN.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent_chart = new Intent(property.this, user.class) ;
                startActivity(intent_chart);
//                finish();
            }
        });

        addBTN.setOnClickListener(new ImageView.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent_add = new Intent(property.this, add_bill.class) ;
                startActivity(intent_add);
            }
        });
    }

    class myAdapter extends RecyclerView.Adapter<myAdapter.myViewHolder> {
        @Override
        public myAdapter.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            myAdapter.myViewHolder holder = new myAdapter.myViewHolder(LayoutInflater.from(
                    property.this).inflate(R.layout.property_item_layout, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(myAdapter.myViewHolder holder, int position)
        {
            holder.data.setText(dataList.get(position));
            holder.category.setText(categoryList.get(position));
            holder.icon.setImageResource(iconList.get(position));
        }

        @Override
        public int getItemCount()
        {
            return dataList.size();
        }
        class myViewHolder extends ViewHolder{
            TextView category;
            TextView data;
            ImageView icon;
            public myViewHolder(View view){
                super (view);
                category = (TextView)view.findViewById(R.id.category);
                data = (TextView)view.findViewById(R.id.data);
                icon = (ImageView)view.findViewById(R.id.icon);
            }
        }
    }
}
