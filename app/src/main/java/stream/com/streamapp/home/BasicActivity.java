package stream.com.streamapp.home;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import stream.com.streamapp.R;
import stream.com.streamapp.addbill.add_bill;

public class BasicActivity extends AppCompatActivity {
    private TextView billBTN = null;
    private TextView propertyBTN = null;
    private TextView chartBTN = null;
    private TextView userBTN = null;
    private ImageView addBTN = null;
    private Context mContext;
    private List<Fragment> mlist;
    private ViewPager mViewPager;
    private MyFragmentPagerAdapter madapter;
    private Drawable drawable0, drawable1, drawable2, drawable3;
    private Drawable _drawable0, _drawable1, _drawable2, _drawable3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LitePal.initialize(this);//Init database.
        SQLiteDatabase db = LitePal.getDatabase();
        setContentView(R.layout.activity_basic);
        super.onCreate(savedInstanceState);

        InitView();

        setListener();
    }
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
    private void InitView()
    {
        mContext=getApplicationContext();
        drawable0= ContextCompat.getDrawable(mContext,R.drawable.bill);
        drawable1= ContextCompat.getDrawable(mContext,R.drawable.property);
        drawable2= ContextCompat.getDrawable(mContext,R.drawable.chart);
        drawable3= ContextCompat.getDrawable(mContext,R.drawable.user);
        _drawable0= ContextCompat.getDrawable(mContext,R.drawable.bill_selected);
        _drawable1= ContextCompat.getDrawable(mContext,R.drawable.property_selected);
        _drawable2= ContextCompat.getDrawable(mContext,R.drawable.chart_selected);
        _drawable3= ContextCompat.getDrawable(mContext,R.drawable.user_selected);
        drawable0.setBounds(0,0,drawable0.getMinimumWidth(),drawable0.getMinimumHeight());
        drawable1.setBounds(0,0,drawable1.getMinimumWidth(),drawable1.getMinimumHeight());
        drawable2.setBounds(0,0,drawable2.getMinimumWidth(),drawable2.getMinimumHeight());
        drawable3.setBounds(0,0,drawable3.getMinimumWidth(),drawable3.getMinimumHeight());
        _drawable0.setBounds(0,0,_drawable0.getMinimumWidth(),_drawable0.getMinimumHeight());
        _drawable1.setBounds(0,0,_drawable1.getMinimumWidth(),_drawable1.getMinimumHeight());
        _drawable2.setBounds(0,0,_drawable2.getMinimumWidth(),_drawable2.getMinimumHeight());
        _drawable3.setBounds(0,0,_drawable3.getMinimumWidth(),_drawable3.getMinimumHeight());
        billBTN = (TextView) findViewById(R.id.text_bill);
        billBTN.setCompoundDrawables(null,_drawable0,null,null);
        propertyBTN = (TextView) findViewById(R.id.text_property);
        chartBTN = (TextView) findViewById(R.id.text_chart);
        userBTN = (TextView) findViewById(R.id.text_user);
        addBTN = (ImageView) findViewById(R.id.add);
        mViewPager = (ViewPager) findViewById(R.id.myViewPager);

        mlist = new ArrayList<>();
        mlist.add(new BillFragment());
        mlist.add(new PropertyFragment());
        mlist.add(new ChartFragment());
        mlist.add(new UserFragment());
        madapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),mlist);
        mViewPager.setAdapter(madapter);
        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state == 2){
                    selected(mViewPager.getCurrentItem());
                }
            }
        });
        mViewPager.setCurrentItem(0);
    }
    private void setListener()
    {
        billBTN.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v){
                mViewPager.setCurrentItem(0);
                selected(0);
            }
        });

        propertyBTN.setOnClickListener(new TextView.OnClickListener(){
        @Override
        public void onClick(View v) {
            mViewPager.setCurrentItem(1);
            selected(1);
        }
        });

        chartBTN.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v){
                mViewPager.setCurrentItem(2);
                selected(2);
            }
        });

        userBTN.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v){
                mViewPager.setCurrentItem(3);
                selected(3);
            }
        });

        addBTN.setOnClickListener(new ImageView.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent_add = new Intent(BasicActivity.this, add_bill.class) ;
                startActivity(intent_add);
            }
        });
    }
    private void selected(int index)
    {
        switch(index)
        {
            case 0:
                billBTN.setCompoundDrawables(null,_drawable0,null,null);
                propertyBTN.setCompoundDrawables(null,drawable1,null,null);
                chartBTN.setCompoundDrawables(null,drawable2,null,null);
                userBTN.setCompoundDrawables(null,drawable3,null,null);

                break;
            case 1:
                billBTN.setCompoundDrawables(null,drawable0,null,null);
                propertyBTN.setCompoundDrawables(null,_drawable1,null,null);
                chartBTN.setCompoundDrawables(null,drawable2,null,null);
                userBTN.setCompoundDrawables(null,drawable3,null,null);
                break;
            case 2:
                billBTN.setCompoundDrawables(null,drawable0,null,null);
                propertyBTN.setCompoundDrawables(null,drawable1,null,null);
                chartBTN.setCompoundDrawables(null,_drawable2,null,null);
                userBTN.setCompoundDrawables(null,drawable3,null,null);
                break;
            case 3:
                billBTN.setCompoundDrawables(null,drawable0,null,null);
                propertyBTN.setCompoundDrawables(null,drawable1,null,null);
                chartBTN.setCompoundDrawables(null,drawable2,null,null);
                userBTN.setCompoundDrawables(null,_drawable3,null,null);
                break;
        }
    }
    public void handle(int request)
    {
        switch (request)
        {
            case (1):
            {
                Toast.makeText(this,"保存成功",Toast.LENGTH_LONG).show();
                break;
            }
            case (2):
            {
                Toast.makeText(this,"保存失败，请检查权限",Toast.LENGTH_LONG).show();
            }
        }
    }

}


