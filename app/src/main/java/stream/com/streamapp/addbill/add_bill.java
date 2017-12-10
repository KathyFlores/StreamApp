package stream.com.streamapp.addbill;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import stream.com.streamapp.R;

public class add_bill extends AppCompatActivity{
    ImageView backBTN = null;
    TextView payBTN = null;
    TextView incomeBTN = null;
    private Fragment income, pay;
    private FragmentManager fm;
    private FragmentTransaction transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbill);
        initView();
        setListener();
    }
    private void initView()
    {
        backBTN = (ImageView) findViewById(R.id.back_button);
        backBTN.setVisibility(View.VISIBLE);
        payBTN = (TextView) findViewById(R.id.payBTN);
        incomeBTN = (TextView) findViewById(R.id.incomeBTN);
        income = new IncomeFragment();
        pay = new PayFragment();
        fm = getSupportFragmentManager();
        transaction = fm.beginTransaction();
        transaction.replace(R.id.itemContent,pay);
        transaction.commit();

    }
    private void setListener()
    {
        backBTN.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        payBTN.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v) {
                transaction = fm.beginTransaction();
                transaction.hide(income);
                transaction.replace(R.id.itemContent, pay);
                transaction.commit();
            }
        });
        incomeBTN.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v) {
                transaction = fm.beginTransaction();
                transaction.hide(pay);
                transaction.replace(R.id.itemContent, income);
                transaction.commit();

            }
        });
    }
}
