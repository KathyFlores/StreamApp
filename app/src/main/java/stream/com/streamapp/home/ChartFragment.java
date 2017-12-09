package stream.com.streamapp.home;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

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
        decideWhichToDraw();
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
    //TODO: 数据接入
    //需要填充 dataIn: 表示横坐标
    //需要填充 pointInX: 表示每一个点的x坐标  pointInY:表示每一个点的y坐标
    private void setData()
    {
        dateIn.clear();
        pointInX.clear();
        pointInY.clear();
        //TODO:  delete first
        for(int i=0;i<10;i++)
        {
            dateIn.add(i,Integer.toString(i));
        }
        for(int i=0;i<15;i++)
        {
            pointInX.add(i,(float)(i/15.0*10.0));
            pointInY.add(i,(float)i);
        }
        mPieData.clear();
        mlabels.clear();
        // TODO: delete first
        for(int i=0;i<3;i++)
        {
            mPieData.add((float)(i+1));
            mlabels.add("test"+i);
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
    private void decideWhichToDraw(){

        //-----   test -----
        setData();
        getData();
        drawLineChart();
        drawPieChart();
        //-----   test -----
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
