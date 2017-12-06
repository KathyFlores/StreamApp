package stream.com.streamapp.home;

import android.graphics.drawable.Drawable;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import stream.com.streamapp.R;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_property, null);
        initData();
        initView();
        return view;
    }
    private void initData(){
        dataList =new ArrayList<String>();
        iconList = new ArrayList<Integer>(Arrays.asList(R.drawable.alipay,R.drawable.nongye,R.drawable.jianshe));
        categoryList = new ArrayList<Integer>(Arrays.asList(R.string.alipay,R.string.nongye,R.string.jianshe));
        for(int i=0;i<3;i++)
        {
            dataList.add("¥"+(i+1)*1000);

        }
    }

    private void initView(){


        recyclerView=view.findViewById(R.id.property_recycler);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter = new myAdapter());
    }
    class myAdapter extends RecyclerView.Adapter<myAdapter.myViewHolder> {
        @Override
        public myAdapter.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            myAdapter.myViewHolder holder = new myAdapter.myViewHolder(LayoutInflater.from(
                    getActivity()).inflate(R.layout.property_item_layout, parent,
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
        class myViewHolder extends RecyclerView.ViewHolder {
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