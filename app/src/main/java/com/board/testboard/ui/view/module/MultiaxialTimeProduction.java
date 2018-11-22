package com.board.testboard.ui.view.module;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import com.board.testboard.app.MyApplication;
import com.board.testboard.bean.PeriodOutputBean;

import org.xclcharts.chart.BarChart;
import org.xclcharts.chart.BarData;
import org.xclcharts.chart.CustomLineData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.common.IFormatterDoubleCallBack;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.axis.CategoryAxis;
import org.xclcharts.renderer.bar.Bar;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * [双-柱形图(竖向)-时段产量]
 */

public class MultiaxialTimeProduction extends BaseChartView{
    private String TAG = "Topcee";
    BarChart chart = new BarChart();
    //标签轴
    List<String> chartLabels = new LinkedList<String>();
    List<BarData> chartDataset = new LinkedList<BarData>();

    //定制线/分界线(标准时产)
    private List<CustomLineData> mCustomLineDataset = new LinkedList<CustomLineData>();

    private List<PeriodOutputBean> periodOutputList = null;
    private Integer[] color = {MyApplication.nDefaultChartHourColor1,MyApplication.nDefaultChartHourColor2};
    String sQtyHoursoutput = ""; //标准时产
    String sChartHourTitle = ""; //标题
    String[] sChartHourLabel; //标题

    public MultiaxialTimeProduction(Context context) {
        super(context);
    }

    public MultiaxialTimeProduction(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiaxialTimeProduction(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //用于初始化
    public void initView(List<PeriodOutputBean> periodOutputList, String sQtyHoursoutput, Integer[] color,String sChartHourTitle,String sChartHourLabel[]) {
        this.periodOutputList = periodOutputList;
        this.sQtyHoursoutput = sQtyHoursoutput;
        this.color = color;
        this.sChartHourTitle = sChartHourTitle;
        this.sChartHourLabel = sChartHourLabel;
        //设置X轴的标签
        chartLabels();
        //绘制柱形图
        chartDataSet();
        //画轴
        chartRender();
        //定制线/分界线
//        chartCustomLines(sQtyHoursoutput);

        //綁定手势滑动事件
//		this.bindTouch(this,chart);

    }
    //用于初始化
    public void initView(List<PeriodOutputBean> periodOutputList,String sQtyHoursoutput) {
        this.periodOutputList = periodOutputList;
        this.sQtyHoursoutput = sQtyHoursoutput;
        //设置X轴的标签
        chartLabels();
        //绘制柱形图
        chartDataSet();
        //画轴
        chartRender();
        //定制线/分界线
//        chartCustomLines(sQtyHoursoutput);

        //綁定手势滑动事件
//		this.bindTouch(this,chart);

    }

    //清除数组
    public void refreshChart(){
        chartLabels.clear();
        chartDataset.clear();
//        mCustomLineDataset.clear();
        //数据源
        chart.setCategories(null);
        chart.setDataSource(null);
        chart.setCustomLines(null);
        chart.getPlotGrid().hideEvenRowBgColor();
        chart.getPlotGrid().hideOddRowBgColor();
        this.invalidate();
    }

    //设置X轴的标签
    private void chartLabels() {

        for (int i = 0; i < periodOutputList.size(); i++) {
            String sCountingPointName = periodOutputList.get(i).getPeriodName(); //时段名称
            if (!TextUtils.isEmpty(sCountingPointName)) {
                chartLabels.add(sCountingPointName);
            }
        }
        if(periodOutputList.get(0).getPeriodName().equals("time0")||periodOutputList.get(0).getPeriodName().equals("time1")){
            chartLabels.clear();
            chartLabels.add("");
            chartLabels.add("");
        }
    }

    //绘制柱形图
    private void chartDataSet() {

        //标签对应的柱形数据集
        List<Double> dataSeries1= new LinkedList<Double>();
        List<Double> dataSeries2= new LinkedList<Double>();
        //依数据值确定对应的柱形颜色.
        List<Integer> dataColorA = new LinkedList<Integer>();
        List<Integer> dataColorB = new LinkedList<Integer>();
        if(periodOutputList!=null && periodOutputList.size()>=1){
            for (int i = 0; i < periodOutputList.size(); i++) {
                String sInputQty = periodOutputList.get(i).getInPutQty().toString().trim();
                String sOutputQty = periodOutputList.get(i).getOutPutQty().toString().trim();
                double dMissRate1 = 0.0;
                double dMissRate2 = 0.0;
                if(!TextUtils.isEmpty(sInputQty)){
                    dMissRate1 = Double.parseDouble(sInputQty); //投入数
                }
                if(!TextUtils.isEmpty(sOutputQty)){
                    dMissRate2 = Double.parseDouble(sOutputQty); //产出数
                }
                dataSeries1.add(dMissRate1);
                dataSeries2.add(dMissRate2);
                dataColorA.add(color[0]); //默认RED
                dataColorB.add(color[1]); //默认RED
            }
        }
        //此地的颜色为Key值颜色及柱形的默认颜色
        chartDataset.add(new BarData(sChartHourLabel[0],dataSeries1,dataColorA,color[0]));
        chartDataset.add( new BarData(sChartHourLabel[1],dataSeries2,dataColorB,color[1]));
    }

    //画轴
    private void chartRender() {
        try {
            //设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
            int [] ltrb = getBarLnDefaultSpadding();
            chart.setPadding(ltrb[0], ltrb[1], ltrb[2], DensityUtil.dip2px(getContext(), 25));

            chart.setChartDirection(XEnum.Direction.VERTICAL);
            //标题
            //chart.setTitle("测试工站"); //主标题
            chart.addSubtitle(sChartHourTitle); //子标题
            chart.setTitleAlign(XEnum.HorizontalAlign.CENTER);
            chart.getPlotTitle().getTitlePaint().setTextSize(20);

            //Y轴标题
            //chart.getAxisTitle().setLeftTitle("单位(%)");

            //指隔多少个轴刻度(即细刻度)后为主刻度
            chart.getDataAxis().setDetailModeSteps(1);

            //柱形图轴相关
            renderBarAxis();

            //定制柱形顶上的标签格式
            renderBar();

            //网格背景
//            chart.getPlotGrid().showHorizontalLines();
            chart.getPlotGrid().showEvenRowBgColor();
            chart.getPlotGrid().showOddRowBgColor();

            //隐蔽图例
            chart.getPlotLegend().show();

            //柱形和标签居中方式
            chart.setBarCenterStyle(XEnum.BarCenterStyle.TICKMARKS);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e(TAG, e.toString());
        }
    }

    //柱形图轴相关
    private void renderBarAxis() {
        //标签轴
        chart.setCategories(chartLabels);
        //数据轴
        chart.setDataSource(chartDataset);
        //定制线/分界线(标准时产)
//        chart.setCustomLines(mCustomLineDataset);

        //最大值
        double max = upToInteger(); //向上取整
        double steps = Math.ceil(max/5); //数据轴步长

        //坐标系
        chart.getDataAxis().setAxisMax(max); //设置数据轴最大值
        chart.getDataAxis().setAxisMin(0); //设置数据轴最小值,默认为0
        chart.getDataAxis().setAxisSteps(steps); //设置数据轴步长

        //定制数据轴上的标签格式
        chart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack(){

            @Override
            public String textFormatter(String value) {
                // TODO Auto-generated method stub

                double label=Double.parseDouble(value);
                DecimalFormat df=new DecimalFormat("#0");
                return df.format(label).toString();
            }

        });

        //定制标签轴标签的标签格式
        CategoryAxis categoryAxis = chart.getCategoryAxis();
        categoryAxis.setTickLabelRotateAngle(-15f);
        categoryAxis.getTickLabelPaint().setTextSize(14);
        categoryAxis.getTickLabelPaint().setTextAlign(Paint.Align.CENTER);
        categoryAxis.setLabelFormatter(new IFormatterTextCallBack(){

            @Override
            public String textFormatter(String value) {
                // TODO Auto-generated method stub
                //String tmp = "c-["+value+"]";
                return value;
            }

        });

        chart.getPlotGrid().hideEvenRowBgColor();
        chart.getPlotGrid().hideHorizontalLines();
        chart.getPlotGrid().hideOddRowBgColor();
        chart.getPlotGrid().hideVerticalLines();

        //柱形和标签居中方式
        chart.setBarCenterStyle(XEnum.BarCenterStyle.TICKMARKS);
        //chart.getCategoryAxis().hideAxisLabels();

    }

    //定制柱形顶上的标签格式
    private void renderBar(){

        Bar bar = chart.getBar();
        bar.setItemLabelVisible(true);
        bar.setItemLabelRotateAngle(-15); //柱形顶上标签旋转
        bar.getItemLabelPaint().setFakeBoldText(true);
        bar.getItemLabelPaint().setTextSize(12);

        chart.setItemLabelFormatter(new IFormatterDoubleCallBack() {
            @Override
            public String doubleFormatter(Double value) {
                // TODO Auto-generated method stub
                // DecimalFormat df=new DecimalFormat("#0.00");
                DecimalFormat df=new DecimalFormat("#0");
                return df.format(value).toString();
            }});
    }

    //定制线/分界线
    private void chartCustomLines(String sQtyHoursoutput){
        if(sQtyHoursoutput.equals("")){
            sQtyHoursoutput = "0";
        }
        Double dQtyHoursoutput = Double.parseDouble(sQtyHoursoutput);

        CustomLineData line1 = new CustomLineData("标准时产",dQtyHoursoutput,Color.rgb(53, 169, 239),5);
        line1.setCustomLineCap(XEnum.DotStyle.TRIANGLE);
		line1.setLabelOffset(15);
		line1.getLineLabelPaint().setColor(Color.rgb(216, 44, 41));
		line1.setLineStyle(XEnum.LineStyle.DASH);
		mCustomLineDataset.add(line1);
    }

    //向上取整
    private double upToInteger(){
        //获取ArrayList中的最大值
        int num = (int) Math.ceil(arrayListMax()); //不小于他的最小整数
        int a = 1;
        while(num >= 10){
            num = num / 10;
            a++;
        }
        num = num + 2;
        StringBuffer buffer = new StringBuffer();
        buffer.append(num);
        for (int i = 0; i < a - 1; i++) {
            buffer.append("0");
        }
        double end;
        try {
            end = Double.parseDouble(buffer.toString());
        }catch (Exception e){
            end = arrayListMax();
        }
        return end;
    }

    //获取ArrayList中的最大值
    public double arrayListMax() {
        try {
            //取投入数最大值
            double maxInPutQty = 0.0; //最大值
            int maxInPutQtyCount = periodOutputList.size();
            if (maxInPutQtyCount >= 1) {
                String sInPutQty = periodOutputList.get(0).getInPutQty().toString();
                double max = 0.0;
                if(!TextUtils.isEmpty(sInPutQty)){
                    max = Double.parseDouble(sInPutQty);
                }
                for (int i = 0; i < maxInPutQtyCount; i++) {
                    String tempInPutQty = periodOutputList.get(i).getInPutQty().toString();
                    double temp = 0.0;
                    if(!TextUtils.isEmpty(tempInPutQty)){
                        temp = Double.parseDouble(tempInPutQty);
                    }
                    if (temp > max) {
                        max = temp;
                    }
                }
                maxInPutQty = max;
            }
            //取产出数最大值
            double maxOutPutQty = 0.0; //最大值
            int maxOutPutQtyCount = periodOutputList.size();
            if (maxOutPutQtyCount >= 1) {
                String sOutPutQty = periodOutputList.get(0).getOutPutQty().toString();
                double max = 0.0;
                if(!TextUtils.isEmpty(sOutPutQty)){
                    max = Double.parseDouble(sOutPutQty);
                }
                for (int i = 0; i < maxOutPutQtyCount; i++) {
                    String tempOutPutQty = periodOutputList.get(i).getOutPutQty().toString();
                    double temp = 0.0;
                    if(!TextUtils.isEmpty(tempOutPutQty)){
                        temp = Double.parseDouble(tempOutPutQty);
                    }
                    if (temp > max) {
                        max = temp;
                    }
                }
                maxOutPutQty = max;
            }
            if(maxInPutQty > maxOutPutQty){
                return maxInPutQty;
            }else {
                return maxOutPutQty;
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //图所占范围大小
        chart.setChartRange(w,h);
    }

    @Override
    public void render(Canvas canvas) {
        try{
            chart.render(canvas);
        } catch (Exception e){
            Log.e(TAG, e.toString());
        }
    }

}
