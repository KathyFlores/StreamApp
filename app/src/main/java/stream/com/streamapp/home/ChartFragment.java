package stream.com.streamapp.home;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;
import stream.com.streamapp.R;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import stream.com.streamapp.db.Bills;
import stream.com.streamapp.login;

/**
 * Created by KathyF on 2017/11/26.
 */

public class ChartFragment extends Fragment {
    private TextView income;
    private TextView expense;
    private TextView day;
    private TextView month;
    private TextView year;
    private String InOrOut = "out";//0:支出, 1:收入
    private int scale = 0;//0:日， 1:月, 2:年
    private View view;

    /*
     * @param mLineChartView is Liner graph
     */

    private LineChartView mLineChartView;

    //x-axis x轴的下标  WARNING!!    must be from old to new
    private ArrayList<String>dateIn=new ArrayList<String>();
    //point value
    private ArrayList<Float>pointInX=new ArrayList<Float>();
    private ArrayList<Float>pointInY=new ArrayList<Float>();

    private ArrayList<PointValue>mPointValues=new ArrayList<>();
    private ArrayList<AxisValue>mAxisXValues=new ArrayList<>();

    /*
     *
     */
    private PieChartView mPieChartView;
    private PieChartData mPieChartData;
    ArrayList<SliceValue> mValues = new ArrayList<>();
    private boolean hasLabels = true;//是否在薄片上显示label
    private boolean hasLabelsOutside = false;//是否在薄片外显示label
    private boolean hasCenterCircle = false;//是否中间掏空一个圈
    private boolean isExploded = false;//薄片是否分离

    //** you need to fill
    private ArrayList<String>mlabels=new ArrayList<>();
    private ArrayList<Float>mPieData=new ArrayList<>();




    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chart, null);
        initView();
        setListener();
        Draw();
        return view;
    }
    private void initView(){
        income=view.findViewById(R.id.income);
        expense=view.findViewById(R.id.expense);
        day=view.findViewById(R.id.day);
        month=view.findViewById(R.id.month);
        year=view.findViewById(R.id.year);
        mLineChartView=view.findViewById(R.id.line_chart);
        mPieChartView=view.findViewById(R.id.pie_chart);
    }
    private void setListener(){
        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InOrOut="in";
                Draw();
            }
        });
        expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InOrOut="out";
                Draw();
            }
        });
        day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scale = 0;
                Draw();
            }
        });
        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scale = 1;
                Draw();
            }
        });
        year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scale = 2;
                Draw();
            }
        });
    }
    //TODO: 数据接入
    //需要填充 dataIn: 表示横坐标
    //需要填充 pointInX: 表示每一个点的x坐标  pointInY:表示每一个点的y坐标
    private void setData(String InOrOut, int scale)
    {
        dateIn.clear();
        pointInX.clear();
        pointInY.clear();
        Calendar calendar = Calendar.getInstance();
        Date d=new Date();
        calendar.setTime(d);
        for(int i=0;i<5;i++)
        {
            String text;
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            switch (scale){
                case 0://日
                    text = sf.format(calendar.getTime());
                    calendar.add(Calendar.DATE, -1);
                    dateIn.add(i,text);
                    break;
                case 1://月
                    text = sf.format(calendar.getTime());
                    calendar.add(Calendar.MONTH, -1);
                    dateIn.add(i,text.substring(0,7));
                    break;
                case 2://年
                    text = sf.format(calendar.getTime());
                    calendar.add(Calendar.YEAR, -1);
                    dateIn.add(i,text.substring(0,4));
                    break;
                default:break;
            }
        }

        d=new Date();
        calendar.setTime(d);

        for(int i=0;i<5;i++)
        {
            pointInX.add(i,(float)i);
            double sum = 0;
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            if (scale == 0) {
                calendar.add(Calendar.DATE, 1);//后一天日期
                String day1 = sf.format(calendar.getTime());
                calendar.add(Calendar.DATE, -1);//前一天日期
                String day2 = sf.format(calendar.getTime());
                sum = DataSupport.where("user_id = ? and date > ? and date < ? and inOrOut = ?", String.valueOf(login.getUser_id()), day2, day1, InOrOut).sum(Bills.class, "amount", double.class);
                calendar.add(Calendar.DATE, -1);
            }
            if (scale == 1) {
                calendar.add(Calendar.MONTH, 1);//后一个月日期
                String month1 = sf.format(calendar.getTime());
                calendar.add(Calendar.MONTH, -1);//前一个月日期
                String month2 = sf.format(calendar.getTime());
                sum = DataSupport.where("user_id = ? and date > ? and date < ? and inOrOut = ?", String.valueOf(login.getUser_id()), month2.substring(0, 7), month1.substring(0, 7), InOrOut).sum(Bills.class, "amount", double.class);
                calendar.add(Calendar.MONTH, -1);
            }
            if (scale == 2 ) {
                calendar.add(Calendar.YEAR, 1);//后一年日期
                String year1 = sf.format(calendar.getTime());
                calendar.add(Calendar.YEAR, -1);//前一年日期
                String year2 = sf.format(calendar.getTime());
                sum = DataSupport.where("user_id = ? and date > ? and date < ? and inOrOut = ?", String.valueOf(login.getUser_id()), year2.substring(0, 4), year1.substring(0, 4), InOrOut).sum(Bills.class, "amount", double.class);
                calendar.add(Calendar.YEAR, -1);
            }
            pointInY.add(i, (float)sum);
        }
        Collections.reverse(dateIn);
        Collections.reverse(pointInY);

        mPieData.clear();
        mlabels.clear();
       String[] labelsOut = {"meal","transportation","shopping",
               "daily","clothes","vegetables","fruit","snack",
               "book","study", "house", "investment", "social",
               "amusement", "makeup", "call", "sport", "travel",
               "medicine", "office", "digit", "gift", "repair",
               "wine", "redpacket", "other"};
        String[] labelsIn = {"salary", "redpacket", "parttime", "other"};
        Log.d("labelsIn", String.valueOf(labelsIn.length));
        Log.d("scale",String.valueOf(scale));
        Log.d("inorout",InOrOut);
        if (InOrOut.equals("in")) {

            for (int i = 0; i < labelsIn.length; i++) {
                double sum = 0;
                String day1, day2;
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                if (scale == 0) {//近七天以来
                    d = new Date();
                    calendar.setTime(d);
                    calendar.add(calendar.DATE, 1);
                    day1 = sf.format(calendar.getTime());
                    calendar.add(calendar.DATE, -7);
                    day2 = sf.format(calendar.getTime());
                    sum = DataSupport.where("user_id = ? and date > ? and date < ? and inOrOut = ? and type = ?", String.valueOf(login.getUser_id()), day2, day1, InOrOut, labelsIn[i]).sum(Bills.class, "amount", double.class);
                    Log.d("date1", day1);
                    Log.d("date2", day2);
                    Log.d("sum", String.valueOf(sum));
                }
                if (scale == 1) {//近三月以来
                    d = new Date();
                    calendar.setTime(d);
                    calendar.add(calendar.MONTH, 1);
                    day1 = sf.format(calendar.getTime());
                    calendar.add(calendar.MONTH, -3);
                    day2 = sf.format(calendar.getTime());
                    sum = DataSupport.where("user_id = ? and date > ? and date < ? and inOrOut = ? and type = ?", String.valueOf(login.getUser_id()), day2, day1, InOrOut, labelsIn[i]).sum(Bills.class, "amount", double.class);

                }
                if (scale == 2) {//近一年来
                    d = new Date();
                    calendar.setTime(d);
                    calendar.add(calendar.YEAR, 1);
                    day1 = sf.format(calendar.getTime());
                    calendar.add(calendar.YEAR, -2);
                    day2 = sf.format(calendar.getTime());
                    sum = DataSupport.where("user_id = ? and date > ? and date < ? and inOrOut = ? and type = ?", String.valueOf(login.getUser_id()), day2, day1, InOrOut, labelsIn[i]).sum(Bills.class, "amount", double.class);
                }
                Log.d("pi", labelsIn[i] + String.valueOf(sum));
                if(sum!=0) {
                    mPieData.add((float) sum);
                    mlabels.add(labelsIn[i]);
                }
            }
        }
        if (InOrOut.equals("out")) {
            for (int i = 0; i < labelsOut.length; i++) {
                double sum = 0;
                String day1, day2;
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                if (scale == 0) {//近七天以来
                    d = new Date();
                    calendar.setTime(d);
                    calendar.add(calendar.DATE, 1);
                    day1 = sf.format(calendar.getTime());
                    calendar.add(calendar.DATE, -7);
                    day2 = sf.format(calendar.getTime());
                    sum = DataSupport.where("user_id = ? and date > ? and date < ? and inOrOut = ? and type = ?", String.valueOf(login.getUser_id()), day2, day1, InOrOut, labelsOut[i]).sum(Bills.class, "amount", double.class);
                }
                if (scale == 1) {//近三月以来
                    d = new Date();
                    calendar.setTime(d);
                    calendar.add(calendar.MONTH, 1);
                    day1 = sf.format(calendar.getTime());
                    calendar.add(calendar.MONTH, -3);
                    day2 = sf.format(calendar.getTime());
                    sum = DataSupport.where("user_id = ? and date > ? and date < ? and inOrOut = ? and type = ?", String.valueOf(login.getUser_id()), day2, day1, InOrOut, labelsOut[i]).sum(Bills.class, "amount", double.class);
                }
                if (scale == 2) {//近一年来
                    d = new Date();
                    calendar.setTime(d);
                    calendar.add(calendar.YEAR, 1);
                    day1 = sf.format(calendar.getTime());
                    calendar.add(calendar.YEAR, -2);
                    day2 = sf.format(calendar.getTime());
                    sum = DataSupport.where("user_id = ? and date > ? and date < ? and inOrOut = ? and type = ?", String.valueOf(login.getUser_id()), day2, day1, InOrOut, labelsOut[i]).sum(Bills.class, "amount", double.class);
                }
                mPieData.add((float) sum);
                mlabels.add(labelsOut[i]);
            }
        }
    }

    private void getData()
    {
        mAxisXValues.clear();
        for (int i=0;i<dateIn.size();i++)
        {
            mAxisXValues.add(new AxisValue(i).setLabel(dateIn.get(i)));
        }
        mPointValues.clear();
        for (int i=0;i<pointInX.size();i++)
        {
            mPointValues.add(new PointValue(pointInX.get(i),pointInY.get(i)));
        }

        mValues.clear();
        for (int i=0;i<mlabels.size();i++)
        {
            SliceValue tmp=new SliceValue(mPieData.get(i),ChartUtils.pickColor());
            tmp.setLabel(mlabels.get(i));
            mValues.add(tmp);

        }

        mPieChartData = new PieChartData(mValues);
        mPieChartData.setHasLabels(hasLabels);
        mPieChartData.setHasLabelsOnlyForSelected(false);
        mPieChartData.setHasLabelsOutside(hasLabelsOutside);
        mPieChartData.setHasCenterCircle(hasCenterCircle);

        if (isExploded)
        {
            mPieChartData.setSlicesSpacing(24);//设置分离距离
        }



    }
    //TODO:完善chart绘制-- select 数据之后调用setData函数填充数据
    private void Draw(){

        //-----   test -----
        setData(InOrOut, scale);
        getData();
        drawLineChart();
        drawPieChart();
        //-----   test -----
        //switch (InOrOut){
        //    case "out"://支出
        //        if(scale==0){//日支出，绘制pie chart与line chart

        //        }
        //        else if(scale==1){//月支出，绘制pie chart与line chart

         //       }
         //       else if(scale==2){//年支出，绘制pie chart

         //       }
        //        break;
        //    case "in"://收入
         //       if(scale==0){

         //       }
         //       else if(scale==1){

         //       }
         //       else if(scale==2){

         //       }
          //      break;
        //}
    }

    private void drawPieChart()
    {
        mPieChartView.setPieChartData(mPieChartData);
        mPieChartView.setCircleFillRatio(0.9f);//设置放大缩小范围
    }

    private void drawLineChart()
    {
        Line line = new Line(mPointValues).setColor(Color.parseColor("#FFCD41"));  //折线的颜色
        ArrayList<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
        line.setCubic(false);//曲线是否平滑
//	    line.setStrokeWidth(3);//线条的粗细，默认是3
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
//		line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X轴下面坐标轴字体是斜的显示还是直的，true是斜的显示
//	    axisX.setTextColor(Color.WHITE);  //设置字体颜色
        axisX.setTextColor(Color.parseColor("#D6D6D9"));//灰色

//	    axisX.setName("未来几天的天气");  //表格名称
        axisX.setTextSize(11);//设置字体大小
        axisX.setMaxLabelChars(7); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
//	    data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线


        Axis axisY = new Axis();  //Y轴
        axisY.setName("");//y轴标注
        axisY.setTextSize(11);//设置字体大小
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边
        //设置行为属性，支持缩放、滑动以及平移
        mLineChartView.setInteractive(true);
        mLineChartView.setZoomType(ZoomType.HORIZONTAL);  //缩放类型，水平
        mLineChartView.setMaxZoom((float) 3);//缩放比例
        mLineChartView.setLineChartData(data);
        mLineChartView.setVisibility(View.VISIBLE);
        mLineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        mLineChartView.setContainerScrollEnabled(true, ContainerScrollType.VERTICAL);


        Viewport v = new Viewport(mLineChartView.getMaximumViewport());
        v.bottom = 0;
        v.top = 5;
        //固定Y轴的范围,如果没有这个,Y轴的范围会根据数据的最大值和最小值决定,这不是我想要的
        //mLineChartView.setMaximumViewport(v);

        //这2个属性的设置一定要在lineChart.setMaximumViewport(v);这个方法之后,不然显示的坐标数据是不能左右滑动查看更多数据的
        v.left = 10 - 7;
        v.right = 10 - 1;
        mLineChartView.setCurrentViewport(v);
    }

}
