package stream.com.streamapp.home;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yalantis.phoenix.PullToRefreshView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import stream.com.streamapp.R;
import stream.com.streamapp.db.Assets;
import stream.com.streamapp.db.Bills;
import stream.com.streamapp.login;

/**
 * Created by KathyF on 2017/11/26.
 */

public class PropertyFragment extends Fragment {
    View view;
    private static RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private List<Integer> iconList;
    private List<Integer> categoryList;
    private List<String> dataList;
    private myAdapter mAdapter;
    private PullToRefreshView mPullToRefreshView;
    private boolean isRefreshing = false;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_property, null);
        initData();
        initView();
        return view;
    }
    private void initData(){
        dataList =new ArrayList<String>();

        iconList = new ArrayList<Integer>(Arrays.asList(R.drawable.alipay,R.drawable.nongye,R.drawable.jianshe,R.drawable.wallet));
        categoryList = new ArrayList<Integer>(Arrays.asList(R.string.alipay,R.string.nongye,R.string.jianshe,R.string.wallet));
        String[] typeList = {"alipay", "nongye", "jianshe", "other"};
        for(int i=0;i<4;i++)
        {
            double sum = 0;
            sum += DataSupport.where("user_id = ? and inOrOut = ? and methods = ?", String.valueOf(login.getUser_id()),"in" ,typeList[i]).sum(Bills.class, "amount", double.class);
            sum -= DataSupport.where("user_id = ? and inOrOut = ? and methods = ?", String.valueOf(login.getUser_id()),"out" ,typeList[i]).sum(Bills.class, "amount", double.class);
            dataList.add("¥"+sum);
        }

    }

    private void initView(){
        recyclerView=view.findViewById(R.id.property_recycler);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter = new myAdapter());
        mAdapter.setOnItemClickListener(new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String[] typeList = {"支付宝", "农业银行", "建设银行", "钱包"};
                Toast.makeText(getActivity(),"您的"+typeList[position]+"账户共有"+dataList.get(position)+"元",Toast.LENGTH_SHORT).show();
                //Toast.makeText(getActivity(),"",Toast.LENGTH_SHORT).show();

            }
        });
        mPullToRefreshView=(PullToRefreshView)view.findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefreshing=true;
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
    class myAdapter extends RecyclerView.Adapter<myAdapter.myViewHolder> {
        private MyItemClickListener mItemClickListener;
        public void setOnItemClickListener(MyItemClickListener listener){
            this.mItemClickListener=listener;
        }
        @Override
        public myAdapter.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            myAdapter.myViewHolder holder = new myAdapter.myViewHolder(LayoutInflater.from(
                    getActivity()).inflate(R.layout.property_item_layout, parent,
                    false),mItemClickListener);
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
            public myViewHolder(View view,MyItemClickListener listener){
                super (view);
                category = (TextView)view.findViewById(R.id.category);
                data = (TextView)view.findViewById(R.id.data);
                icon = (ImageView)view.findViewById(R.id.icon);
                this.mListener = listener;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mListener != null){
                            mListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
            }
        }
    }
    class MyTask extends AsyncTask {
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
            super.onPostExecute(o);
            isRefreshing=false;
            mAdapter.notifyDataSetChanged();
        }
    }
}
