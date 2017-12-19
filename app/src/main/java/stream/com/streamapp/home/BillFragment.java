package stream.com.streamapp.home;


import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jzxiang.pickerview.TimePickerDialog;
import com.yalantis.phoenix.PullToRefreshView;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import stream.com.streamapp.R;
import stream.com.streamapp.db.Bills;
import stream.com.streamapp.login;
import static java.lang.String.valueOf;

/**
 * Created by KathyF on 2017/11/26.
 */

public class BillFragment extends Fragment {
    private static RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private final static int REQUEST_CODE = 1;
    private List<Integer> iconList;
    private List<Integer> categoryList;
    private List<String> dataList;
    private List<Bills> bills;
    private myAdapter mAdapter;
    private TextView incomeSum, expenseSum;
    private LinearLayout pickTime;
    double income,expense;
    private PullToRefreshView mPullToRefreshView;
    private TextView yearTV, monthTV;
    String mYear = "0";
    String mMonth = "0";
    TimePickerDialog timePickerDialog;
    View view;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragemnt_bill, null);
        if (mYear.equals("0"))
        {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String text = sf.format(new Date());
            mYear = text.substring(0,4);
            mMonth = text.substring(5,7);

        }
        initData();
        initView();
        incomeSum.setText(String.valueOf(income)+" 元");
        expenseSum.setText(String.valueOf(expense)+" 元");

        //setListener();
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 当otherActivity中返回数据的时候，会响应此方法
        // requestCode和resultCode必须与请求startActivityForResult()和返回setResult()的时候传入的值一致。
        if (requestCode == REQUEST_CODE && resultCode == BillDetail.RESULT_CODE) {
            Bundle bundle=data.getExtras();
            boolean changed = bundle.getBoolean("result");
            Log.e("changed",Boolean.toString(changed));
            if(changed)
                new MyTask().execute();
        }
    }
    private void initView(){
        incomeSum = (TextView)view.findViewById(R.id.incomeSum);
        expenseSum = (TextView)view.findViewById(R.id.expenseSum);
        yearTV = view.findViewById(R.id.yearTV);
        monthTV = view.findViewById(R.id.monthTV);

        yearTV.setText(mYear+"年");
        monthTV.setText(mMonth+"月");
        recyclerView=view.findViewById(R.id.bill_recycler);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter = new myAdapter());
        timePickerDialog = new TimePickerDialog.Builder()
                .setType(Type.YEAR_MONTH)
                .setThemeColor(R.color.colorPrimary)
                .setCallBack(new OnDateSetListener(){
                    @Override
                    public void onDateSet(TimePickerDialog timePickerDialog, long millseconds) {
                        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String text = sf.format(new Date(millseconds));
                        mYear = text.substring(0,4);
                        mMonth = text.substring(5,7);
                        //Log.d("year", mYear);
                        yearTV.setText(mYear+"年");
                        monthTV.setText(mMonth+"月");
                        initData();
                        new MyTask().execute();

                    }
                })
                .build();
        pickTime = view.findViewById(R.id.pickTime);
        pickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                timePickerDialog.show(getChildFragmentManager(),"选择日期");
            }
        });

        mAdapter.setOnItemClickListener(new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {

                Intent intent = new Intent(getContext(), BillDetail.class);
                int BillId = bills.get(postion).getId();
                intent.putExtra("BillId",BillId);

                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        mAdapter.setOnItemLongClickListener(new MyItemLongClickListener(){
            @Override
            public void onLongItemClick(View v, int position){
                final int pos = position;
                PopupMenu popupMenu = new PopupMenu(getContext(),v);
                popupMenu.getMenuInflater().inflate(R.menu.deletemenu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        int BillId = bills.get(pos).getId();
                        ContentValues values = new ContentValues();
                        values.put("state", 3);
                        DataSupport.update(Bills.class, values, BillId);
                        try {
                            UpdateData.UploadBill();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        new MyTask().execute();
                        Toast.makeText(getActivity(),"已删除",Toast.LENGTH_SHORT).show();

                        return true;
                    }
                });
                popupMenu.show();
            }
        });
        //TODO:add divider
        // recyclerView.addItemDecoration(new);
//        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
//        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.colorAccent));
//        swipeRefreshLayout.setProgressViewOffset(false,0, 50);

        mPullToRefreshView=(PullToRefreshView)view.findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new MyTask().execute();
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 2000);
            }
        });
    }

    private void initData(){

        String nextYear = mMonth.equals("12")? String.valueOf(Integer.valueOf(mYear)+1):mYear;
        //Log.d("nextyear", nextYear);
        String nextMonth = "";
        switch (mMonth){
            case "01": nextMonth="02";break;
            case "02": nextMonth="03";break;
            case "03": nextMonth="04";break;
            case "04": nextMonth="05";break;
            case "05": nextMonth="06";break;
            case "06": nextMonth="07";break;
            case "07": nextMonth="08";break;
            case "08": nextMonth="09";break;
            case "09": nextMonth="10";break;
            case "10": nextMonth="11";break;
            case "11": nextMonth="12";break;
            case "12": nextMonth="01";break;
            default:break;
        }
        //Log.d("nextmonth", nextMonth);
        income = DataSupport.where("user_id = ? and date >= ? and date < ? and inOrOut = ? and state <> 3 ", String.valueOf(login.getUser_id()), mYear+"-"+mMonth+"-01", nextYear+"-"+nextMonth+"-01" ,"in" ).sum(Bills.class, "amount", double.class);

        expense= DataSupport.where("user_id = ? and date >= ? and date < ? and inOrOut = ? and state <> 3 ", String.valueOf(login.getUser_id()), mYear+"-"+mMonth+"-01", nextYear+"-"+nextMonth+"-01" ,"out" ).sum(Bills.class, "amount", double.class);
        dataList =new ArrayList<String>();
        iconList = new ArrayList<Integer>();
        categoryList = new ArrayList<Integer>();
        //Log.d("date", mYear+"-"+mMonth+"-01");
        bills = DataSupport.where("user_id = ? and date >= ? and date < ? and state <> 3 ", String.valueOf(login.getUser_id()), mYear+"-"+mMonth+"-01", nextYear+"-"+nextMonth+"-01").order("date desc").limit(5).find(Bills.class);
        for (int i = 0; i < /*((bills.size()>5)?5:*/bills.size(); i++) {
            dataList.add( (bills.get(i).getInOrOut().equals("in") ? "+":"-") + String.valueOf(bills.get(i).getAmount()));
            switch(bills.get(i).getType())
            {
                case "meal":
                    categoryList.add(R.string.meal);
                    iconList.add(R.drawable.meal);
                    break;
                case "transportation":
                    categoryList.add(R.string.transportation);
                    iconList.add(R.drawable.transportation);
                    break;
                case "shopping":
                    categoryList.add(R.string.shopping);
                    iconList.add(R.drawable.shopping);
                    break;
                case "daily":
                    categoryList.add(R.string.daily);
                    iconList.add(R.drawable.daily);
                    break;
                case "clothes":
                    categoryList.add(R.string.clothes);
                    iconList.add(R.drawable.clothes);
                    break;
                case "vegetables":
                    categoryList.add(R.string.vegetable);
                    iconList.add(R.drawable.vegetable);
                    break;
                case "fruit":
                    categoryList.add(R.string.fruit);
                    iconList.add(R.drawable.fruit);
                    break;
                case "snack":
                    categoryList.add(R.string.snack);
                    iconList.add(R.drawable.snack);
                    break;
                case "book":
                    categoryList.add(R.string.book);
                    iconList.add(R.drawable.book);
                    break;
                case "study":
                    categoryList.add(R.string.study);
                    iconList.add(R.drawable.study);
                    break;
                case "house":
                    categoryList.add(R.string.house);
                    iconList.add(R.drawable.house);
                    break;
                case "investment":
                    categoryList.add(R.string.investment);
                    iconList.add(R.drawable.investment);
                    break;
                case "social":
                    categoryList.add(R.string.social);
                    iconList.add(R.drawable.social);
                    break;
                case "amusement":
                    categoryList.add(R.string.amusement);
                    iconList.add(R.drawable.amusement);
                    break;
                case "makeup":
                    categoryList.add(R.string.makeup);
                    iconList.add(R.drawable.makeup);
                    break;
                case "call":
                    categoryList.add(R.string.call);
                    iconList.add(R.drawable.call);
                    break;
                case "sport":
                    categoryList.add(R.string.sport);
                    iconList.add(R.drawable.sport);
                    break;
                case "travel":
                    categoryList.add(R.string.travel);
                    iconList.add(R.drawable.travel);
                    break;
                case "medicine":
                    categoryList.add(R.string.medicine);
                    iconList.add(R.drawable.medicine);
                    break;
                case "office":
                    categoryList.add(R.string.office);
                    iconList.add(R.drawable.office);
                    break;
                case "digit":
                    categoryList.add(R.string.digit);
                    iconList.add(R.drawable.digit);
                    break;
                case "gift":
                    categoryList.add(R.string.gift);
                    iconList.add(R.drawable.gift);
                    break;
                case "repair":
                    categoryList.add(R.string.repair);
                    iconList.add(R.drawable.repair);
                    break;
                case "wine":
                    categoryList.add(R.string.wine);
                    iconList.add(R.drawable.wine);
                    break;
                case "redpacket":
                    categoryList.add(R.string.redpacket);
                    iconList.add(R.drawable.redpacket);
                    break;
                case "other":
                    categoryList.add(R.string.other);
                    iconList.add(R.drawable.other);
                    break;
                case "parttime":
                    categoryList.add(R.string.parttime);
                    iconList.add(R.drawable.parttime);
                    break;
                case "salary":
                    categoryList.add(R.string.salary);
                    iconList.add(R.drawable.salary);
                    break;
            }


        }

    }
    class myAdapter extends RecyclerView.Adapter<myAdapter.myViewHolder> {
        private MyItemClickListener mItemClickListener;
        private MyItemLongClickListener myItemLongClickListener;
        public void setOnItemClickListener(MyItemClickListener listener){
            this.mItemClickListener=listener;
        }
        public void setOnItemLongClickListener(MyItemLongClickListener listener){
            this.myItemLongClickListener = listener;
        }
        @Override
        public myAdapter.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            myAdapter.myViewHolder holder = new myAdapter.myViewHolder(LayoutInflater.from(
                    getActivity()).inflate(R.layout.bill_item_layout, parent,
                    false),mItemClickListener,myItemLongClickListener);
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
        class myViewHolder extends RecyclerView.ViewHolder {
            TextView category;
            TextView data;
            ImageView icon;
            private MyItemClickListener mListener;
            private MyItemLongClickListener myItemLongClickListener;
            public myViewHolder(View view,MyItemClickListener listener,MyItemLongClickListener longClickListener){
                super (view);

                category = (TextView)view.findViewById(R.id.category);
                data = (TextView)view.findViewById(R.id.data);
                icon = (ImageView)view.findViewById(R.id.icon);
                this.mListener = listener;
                this.myItemLongClickListener=longClickListener;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mListener != null){
                            mListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if(myItemLongClickListener!=null){
                            myItemLongClickListener.onLongItemClick(v,getAdapterPosition());
                        }
                        return false;
                    }
                });

            }
        }
    }
    public class MyTask extends AsyncTask{
        @Override
        protected Object doInBackground(Object[] objects){
            initData();
            try {
                UpdateData.UploadBill();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Object o){
            expenseSum.setText(String.valueOf(expense)+" 元");
            incomeSum.setText(String.valueOf(income)+" 元");
            super.onPostExecute(o);

            mAdapter.notifyDataSetChanged();
        }
    }
}

