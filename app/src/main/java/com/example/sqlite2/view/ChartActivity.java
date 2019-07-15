package com.example.sqlite2.view;

import android.app.Activity;
import android.os.Bundle;

import com.example.sqlite2.R;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sqlite2.database.DatabaseHelper;
import com.example.sqlite2.database.model.Note;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jared on 16/4/1. //大佬留名
 */
public class ChartActivity extends Activity {

    private final static String TAG = ChartActivity.class.getSimpleName();

    private LinearLayout chartLyt;
    private LineChart mLineChart;
    private PieChart mPieChart;
    private TextView words;
    Typeface mTf; // 自定义显示字体

    private ImageView getDataBtn;
    private ImageView back;

    private List<Integer> lists = new ArrayList<Integer>();

    private List<Note> notesList = new ArrayList<>();
    private DatabaseHelper db;
    public String str;

    private void setLists() {
        lists.clear();
        int number = db.getNotesCount();
        for (int i = 1; i < 10 && i < number; i++) {
            int value = ((int) (notesList.get(i).getWordnumber()));
            lists.add(value);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.parseColor("#455399"));
        setContentView(R.layout.act_chart);

        db = new DatabaseHelper(this);
        notesList.addAll(db.getAllNotesDatas());

        getDataBtn = findViewById(R.id.getData);
        getDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                words.setText(str);
            }
        });
        back = findViewById(R.id.analys_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        chartLyt = (LinearLayout) findViewById(R.id.chart);
//        mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Bold.ttf");
        words = findViewById(R.id.analys);

        drawTheChart();
        drawTheChartByMPAndroid();
        drawPieChart();//mood
        int wordnumber = 0;
        int mood = 0;
        for(int i = 0; i <notesList.size(); i++){
            wordnumber += notesList.get(i).getWordnumber();
            mood += notesList.get(i).getMood();
        }

        String mood_str;
        int mood_ave = (99+mood)/(50*notesList.size());
        switch (mood_ave){
            case 0: mood_str = "\n山重水复疑无路，柳暗花明又一村\n";break;
            case 1: mood_str = "\n不识庐山真面目，只缘身在此山中\n";break;
            case 2: mood_str = "\n儿大诗书女丝麻，公但读书煮春茶\n";break;
            case 3: mood_str = "\n春风得意马蹄疾, 一日看尽长安花\n";break;
            default:mood_str = "\n两个黄鹂鸣翠柳，一行白鹭上青天\n";break;
        }
        str = "    这些日子里,你写了"+notesList.size()+"篇日记，"
                + wordnumber +"个字记录你的点点滴滴，"
                +  mood_str + "量化的心情指数:"+(mood/notesList.size())+"是怎样的感觉呢?";


    }

    private void drawPieChart() {
        mPieChart = (PieChart) findViewById(R.id.spread_pie_chart);
        PieData mPieData = getPieData(8, 100);
        showPieChart(mPieChart, mPieData);
    }

    private void showPieChart(PieChart pieChart, PieData pieData) {
        pieChart.setHoleColorTransparent(true);

        pieChart.setHoleRadius(40f); //半径
        pieChart.setTransparentCircleRadius(50f); //半透明圈

        pieChart.setDescription("");

        pieChart.setDrawHoleEnabled(true);

        pieChart.setRotationAngle(90); //初始角度
        pieChart.setRotationEnabled(true); //可以手动旋转
        pieChart.setUsePercentValues(true); //显示百分比

        pieChart.setDrawCenterText(true); //饼状图中间可以添加文字
        pieChart.setCenterText("心情指数分布");
        pieChart.setCenterTextColor(Color.GRAY);
        pieChart.setCenterTextTypeface(mTf);

        pieChart.setData(pieData);

        Legend mLegend = pieChart.getLegend(); //设置比例图
        mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART); //坐右边显示
        mLegend.setXEntrySpace(10f);
        mLegend.setYEntrySpace(5f);
        mLegend.setTypeface(mTf);
        mLegend.setTextColor(Color.GRAY);

        pieChart.animateXY(1000, 1000);
    }

    //time here
    private PieData getPieData(int count, float range) {
        ArrayList<String> xValues = new ArrayList<String>(); //用来表示每个饼块上的内容
        String[]  content = new String[] {"-100~-75", "-75~-50", "-50~-25","-25~0", "0~25","25~50","50~75","75~100"};
        for (int i = 0; i < count; i++) {
            xValues.add("("+content[i]+")");
        }

        ArrayList<Entry> yValue = new ArrayList<Entry>(); //用来表示封装每个饼块的实际数据

        List<Float> qs = new ArrayList<Float>();

//        qs.add(14f); qs.add(14f);qs.add(34f);qs.add(38f);qs.add(78f);

        float[] counts = new float[count];
        int dbnumber= db.getNotesCount();
        for(int i = 0; i < dbnumber; i++){
            //Log.d("mood:"+notesList.get(i).getMood()+" i:"+i, "index:"+((notesList.get(i).getMood() + 99)/25));
            counts[(notesList.get(i).getMood() + 99) / 25]++;
        }

        for(int i = 0; i < count; i++){
            qs.add((100*(counts[i]/dbnumber)));
        }

        for (int i = 0; i < qs.size(); i++) {
            yValue.add(new Entry(qs.get(i), i));
        }

        PieDataSet pieDataSet = new PieDataSet(yValue, "心情指数统计");
        pieDataSet.setSliceSpace(0f);
        ArrayList<Integer> colors = new ArrayList<Integer>();
        //饼图颜色

        colors.add(Color.parseColor(("#0000FF")));
        colors.add(Color.rgb(205, 205, 205));
        colors.add(Color.rgb(114, 188, 223));
        colors.add(Color.parseColor(("#000000")));
        colors.add(Color.parseColor(("#FFFF00")));
        colors.add(Color.parseColor(("#912CEE")));
        colors.add(Color.parseColor(("#98FB98")));
        colors.add(Color.parseColor(("#EE0000")));
        pieDataSet.setColors(colors); //设置颜色
        pieDataSet.setValueTextSize(8f);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTypeface(mTf); //设置字体样式
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px); //选中态多出的长度
        PieData pieData = new PieData(xValues, pieDataSet);
        Log.d("test,",pieData.toString());
        return pieData;
    }

    private void drawTheChartByMPAndroid() {
        mLineChart = (LineChart) findViewById(R.id.spread_line_chart);
        LineData lineData = getLineData(30, 200);
        showChart(mLineChart, lineData, Color.rgb(137, 230, 81));
    }

    private void showChart(LineChart lineChart, LineData lineData, int color) {
        lineChart.setDrawBorders(false); //在折线图上添加边框
        lineChart.setDescription(""); //数据描述
        lineChart.setNoDataTextDescription("You need to provide data for the chart.");

        lineChart.setDrawGridBackground(false); //表格颜色
        lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); //表格的颜色，设置一个透明度

        lineChart.setTouchEnabled(true); //可点击

        lineChart.setDragEnabled(true);  //可拖拽
        lineChart.setScaleEnabled(true);  //可缩放

        lineChart.setPinchZoom(false);

        lineChart.setBackgroundColor(color); //设置背景颜色

        lineChart.setData(lineData);  //填充数据

        Legend mLegend = lineChart.getLegend(); //设置标示，就是那个一组y的value的

        mLegend.setForm(Legend.LegendForm.CIRCLE); //样式
        mLegend.setFormSize(6f); //字体
        mLegend.setTextColor(Color.WHITE); //颜色

        lineChart.setVisibleXRange(1, 7);   //x轴可显示的坐标范围
        XAxis xAxis = lineChart.getXAxis();  //x轴的标示
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x轴位置
        xAxis.setTextColor(Color.WHITE);    //字体的颜色
        xAxis.setTextSize(10f); //字体大小
        xAxis.setGridColor(Color.WHITE);//网格线颜色
        xAxis.setDrawGridLines(false); //不显示网格线
        xAxis.setTypeface(mTf);

        YAxis axisLeft = lineChart.getAxisLeft(); //y轴左边标示
        YAxis axisRight = lineChart.getAxisRight(); //y轴右边标示
        axisLeft.setTextColor(Color.WHITE); //字体颜色
        axisLeft.setTextSize(10f); //字体大小
        axisLeft.setAxisMaxValue(100f); //最大值
        axisLeft.setAxisMinValue(-100f); //最大值
        axisLeft.setLabelCount(8, true); //显示格数
        axisLeft.setGridColor(Color.WHITE); //网格线颜色
        axisLeft.setTypeface(mTf);

        axisRight.setDrawAxisLine(false);
        axisRight.setDrawGridLines(false);
        axisRight.setDrawLabels(false);

        lineChart.animateX(2500);  //立即执行动画
    }

    private LineData getLineData(int count, float range) {
        ArrayList<String> xValues = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            // x轴显示的数据，这里默认使用数字下标显示
            xValues.add("" + (i+1));
        }

        // y轴的数据
        int number = db.getNotesCount();
        ArrayList<Entry> yValues = new ArrayList<Entry>();
        for (int i = 0; i < count && i < number; i++) {
            float value = (int) notesList.get(i).getMood();
            yValues.add(new Entry(value, i));
        }
        // create a dataset and give it a type
        // y轴的数据集合
        LineDataSet lineDataSet = new LineDataSet(yValues, "心情指数变化");
        // mLineDataSet.setFillAlpha(110);
        // mLineDataSet.setFillColor(Color.RED);

        //用y轴的集合来设置参数
        lineDataSet.setLineWidth(1.75f); // 线宽
        lineDataSet.setCircleSize(3f);// 显示的圆形大小
        lineDataSet.setColor(Color.WHITE);// 显示颜色
        lineDataSet.setCircleColor(Color.WHITE);// 圆形的颜色
        lineDataSet.setHighLightColor(Color.WHITE); // 高亮的线的颜色
        lineDataSet.setHighlightEnabled(true);
        lineDataSet.setValueTextColor(Color.WHITE); //数值显示的颜色
        lineDataSet.setValueTextSize(8f);     //数值显示的大小
        lineDataSet.setValueTypeface(mTf);

        ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();
        lineDataSets.add(lineDataSet); // 添加数据集合

        //创建lineData
        LineData lineData = new LineData(xValues, lineDataSets);
        return lineData;
    }

    public void drawTheChart() {
        XYMultipleSeriesRenderer mRenderer = getXYMulSeriesRenderer();

        XYSeriesRenderer renderer = getXYSeriesRenderer();

        mRenderer.addSeriesRenderer(renderer);

        setLists();

        XYMultipleSeriesDataset dataset = getDataSet();

        GraphicalView chartView = ChartFactory.getLineChartView(this, dataset, mRenderer);

        chartLyt.addView(chartView, 0);
        //chartLyt.invalidate();
    }

    public XYSeriesRenderer getXYSeriesRenderer() {
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        //设置折线宽度
        renderer.setLineWidth(2);
        //设置折线颜色
        renderer.setColor(Color.GRAY);
        renderer.setDisplayBoundingPoints(true);
        //点的样式
        renderer.setPointStyle(PointStyle.CIRCLE);
        //设置点的大小
        renderer.setPointStrokeWidth(3);
        //设置数值显示的字体大小
        renderer.setChartValuesTextSize(30);
        //显示数值
        renderer.setDisplayChartValues(true);
        return renderer;
    }

    public XYMultipleSeriesDataset getDataSet() {
        XYMultipleSeriesDataset barDataset = new XYMultipleSeriesDataset();
        CategorySeries barSeries = new CategorySeries("2019年");

        for (int i = 0; i < lists.size(); i++) {
            barSeries.add(lists.get(i));
        }

        barDataset.addSeries(barSeries.toXYSeries());
        return barDataset;
    }

    public XYMultipleSeriesRenderer getXYMulSeriesRenderer() {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setMarginsColor(Color.argb(0x00, 0xF3, 0xF3, 0xF3));

        // 设置背景颜色
        renderer.setApplyBackgroundColor(true);
        renderer.setBackgroundColor(Color.WHITE);

        //设置Title的内容和大小
        renderer.setChartTitle("每篇字数统计");
        renderer.setChartTitleTextSize(50);

        //图表与四周的边距
        renderer.setMargins(new int[]{80, 80, 50, 50});

        //设置X,Y轴title的内容和大小
        renderer.setXTitle("最近日记字数/篇");
        renderer.setYTitle("字数");
        renderer.setAxisTitleTextSize(30);
        //renderer.setAxesColor(Color.WHITE);
        renderer.setLabelsColor(Color.BLACK);

        //图例文字的大小
        renderer.setLegendTextSize(20);

        // x、y轴上刻度颜色和大小
        renderer.setXLabelsColor(Color.BLACK);
        renderer.setYLabelsColor(0, Color.BLACK);
        renderer.setLabelsTextSize(20);
        renderer.setYLabelsPadding(30);

        // 设置X轴的最小数字和最大数字，由于我们的数据是从1开始，所以设置为0.5就可以在1之前让出一部分
        // 有兴趣的童鞋可以删除下面两行代码看一下效果
        renderer.setPanEnabled(false, false);

        //显示网格
        renderer.setShowGrid(true);

        //X,Y轴上的数字数量
        renderer.setXLabels(10);
        renderer.setYLabels(5);

        // 设置X轴的最小数字和最大数字
        renderer.setXAxisMin(1);
        renderer.setXAxisMax(10);
        // 设置Y轴的最小数字和最大数字
        renderer.setYAxisMin(0);
        renderer.setYAxisMax(500);

        // 设置渲染器显示缩放按钮
        renderer.setZoomButtonsVisible(true);
        // 设置渲染器允许放大缩小
        renderer.setZoomEnabled(true);
        // 消除锯齿
        renderer.setAntialiasing(true);

        // 刻度线与X轴坐标文字左侧对齐
        renderer.setXLabelsAlign(Paint.Align.LEFT);
        // Y轴与Y轴坐标文字左对齐
        renderer.setYLabelsAlign(Paint.Align.LEFT);

        // 允许左右拖动,但不允许上下拖动.
        renderer.setPanEnabled(true, false);

        return renderer;
    }

//    @Override
//    public void onClick(View view) {
//        super.onClick(view);
//        switch (view.getId()) {
//            case R.id.getData:
//                drawTheChart();
//                drawTheChartByMPAndroid();
//                drawPieChart();
//                break;
//            default:
//                break;
//        }
//    }
}