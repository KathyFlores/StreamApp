package stream.com.streamapp.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import stream.com.streamapp.R;

/**
 * Created by KathyF on 2017/11/26.
 */

public class ChartFragment extends Fragment {
    private TextView income;
    private TextView expense;
    private TextView day;
    private TextView month;
    private TextView year;
    private int InOrOut = 0;//0:支出, 1:收入
    private int scale = 0;//0:日， 1:月, 2:年
    private View view;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chart, null);
        initView();
        setListener();
        decideWhichToDraw();
        return view;
    }
    private void initView(){
        income=view.findViewById(R.id.income);
        expense=view.findViewById(R.id.expense);
        day=view.findViewById(R.id.day);
        month=view.findViewById(R.id.month);
        year=view.findViewById(R.id.year);
    }
    private void setListener(){
        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InOrOut=1;
            }
        });
        expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InOrOut=0;
            }
        });
        day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scale = 0;
            }
        });
        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scale = 1;
            }
        });
        year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scale = 2;
            }
        });
    }
    //TODO:完善chart绘制
    private void decideWhichToDraw(){
        switch (InOrOut){
            case 0://支出
                if(scale==0){//日支出，绘制pie chart与line chart

                }
                else if(scale==1){//月支出，绘制pie chart与line chart

                }
                else if(scale==2){//年支出，绘制pie chart

                }
                break;
            case 1://收入
                if(scale==0){

                }
                else if(scale==1){

                }
                else if(scale==2){

                }
                break;
        }
    }
}
