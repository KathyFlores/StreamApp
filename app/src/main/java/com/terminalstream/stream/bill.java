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
import android.view.KeyEvent;

public class bill extends AppCompatActivity {
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
//    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_bill);

        initData();
        initView();
        setListener();

    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        System.out.println("onRestoreInstanceState");
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("onSaveInstanceState");
    }
    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("onRestart");
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
        mContext=getApplicationContext();
        Drawable drawable=ContextCompat.getDrawable(mContext,R.drawable.bill_selected);
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        billBTN = (TextView) findViewById(R.id.text_bill);
        billBTN.setCompoundDrawables(null,drawable,null,null);
        propertyBTN = (TextView) findViewById(R.id.text_property);
        chartBTN = (TextView) findViewById(R.id.text_chart);
        userBTN = (TextView) findViewById(R.id.text_user);
        addBTN = (ImageView) findViewById(R.id.add);

        recyclerView=(RecyclerView)findViewById(R.id.bill_recycler);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter = new myAdapter());
        //TODO:add divider
        // recyclerView.addItemDecoration(new);
//        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
//        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.colorAccent));
//        swipeRefreshLayout.setProgressViewOffset(false,0, 50);
    }

    private void initData(){
        dataList =new ArrayList<String>();
        iconList = new ArrayList<Integer>(Arrays.asList(R.drawable.amusement,R.drawable.book,R.drawable.clothes));
        categoryList = new ArrayList<Integer>(Arrays.asList(R.string.amusement,R.string.book,R.string.clothes));
        for(int i=0;i<3;i++)
        {
            dataList.add("test"+i);

        }


    }
    private void setListener(){
        billBTN.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent_bill = new Intent(bill.this, bill.class);
                startActivity(intent_bill);
                finish();
            }
        });

        propertyBTN.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent_property = new Intent(bill.this, property.class);
                startActivity(intent_property);
                finish();
            }
        });

        chartBTN.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent_chart = new Intent(bill.this, chart.class) ;
                startActivity(intent_chart);
                finish();
            }
        });

        userBTN.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent_chart = new Intent(bill.this, user.class) ;
                startActivity(intent_chart);
                finish();
            }
        });

        addBTN.setOnClickListener(new ImageView.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent_add = new Intent(bill.this, add_bill.class) ;
                startActivity(intent_add);
            }
        });
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                swipeRefreshLayout.setRefreshing(true);
//
//            }
//        });
    }

    class myAdapter extends RecyclerView.Adapter<myAdapter.myViewHolder> {
        @Override
        public myAdapter.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            myAdapter.myViewHolder holder = new myAdapter.myViewHolder(LayoutInflater.from(
                    bill.this).inflate(R.layout.bill_item_layout, parent,
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
