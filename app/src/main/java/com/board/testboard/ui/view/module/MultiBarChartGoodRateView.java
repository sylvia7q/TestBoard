/**
 * Copyright 2014  XCL-Charts
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 	
 * @Project XCL-Charts 
 * @Description Android图表基类库演示
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.0
 */
package com.topcee.kanban.ui.view.module;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.topcee.kanban.app.MyApplication;
import com.topcee.kanban.bean.GoodRateBean1;
import com.topcee.kanban.bean.GoodRateBean2;
import com.topcee.kanban.ui.view.module.BaseChartView;

import org.xclcharts.chart.BarChart;
import org.xclcharts.chart.BarData;
import org.xclcharts.chart.LineChart;
import org.xclcharts.chart.LineData;
import org.xclcharts.chart.StackBarChart;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.common.IFormatterDoubleCallBack;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.axis.DataAxis;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;


/**
 * @ClassName MultiBarChartGoodRateView
 * @Description  工站时段产出
 * @author longq
 */

public class MultiBarChartGoodRateView extends BaseChartView {
	
	private String TAG = "MultiBarchart201View";
	private BarChart chart = new BarChart();
	private BarChart chart2 = new BarChart();
    private LineChart lnChart = new LineChart();
    private StackBarChart stackBarChart = new StackBarChart();

	//标签轴
	private List<String> chartLabels = new LinkedList<String>();
	private List<BarData> chartData = new LinkedList<BarData>();
	private List<BarData> chartData2 = new LinkedList<BarData>();
    private List<String> chartLabelsLn = new LinkedList<String>();
    private LinkedList<LineData> chartDatasetLn = new LinkedList<LineData>();

	private double dAxisMax = 0d;
	private int axisColor = Color.rgb(0, 0, 0);
    private String sLeftTitle = "";
    private String sRightTitle = "";
    private String sOkQtyPlotLegend = "";
    private String sOkRatePlotLegend = "";
    private List<GoodRateBean1> rateBean1List = null;
    private List<GoodRateBean2> rateBean2List = null;
    private Integer[] color = {MyApplication.nDefaultChartStation1OkRateColor, MyApplication.nDefaultChartStation1QtyColor, MyApplication.nDefaultChartStation2OkRateColor, MyApplication.nDefaultChartStation2QtyColor};
    private String sGoodRateTitle = ""; //标题

	public MultiBarChartGoodRateView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
//		initView();
	}
	
	public MultiBarChartGoodRateView(Context context, AttributeSet attrs){
        super(context, attrs);   
//        initView();
	 }
	 
	 public MultiBarChartGoodRateView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
//			initView();
	 }
    //清除数组
    public void refreshChart(){
        chartLabels.clear();
        chartData.clear();
        chartData2.clear();
        //数据源
        chart.setCategories(null);
        chart.setDataSource(null);
//		chart.setCustomLines(null);
        chartDatasetLn.clear();
        lnChart.setCategories(null);
        lnChart.setDataSource(null);
        chart.getPlotGrid().hideEvenRowBgColor();
        chart.getPlotGrid().hideOddRowBgColor();
        this.invalidate();
    }

    public void initView(String sLeftTitle, String sRightTitle, String sOkQtyPlotLegend, String sOkRatePlotLegend, List<GoodRateBean1> goodRateBean1List, List<GoodRateBean2> goodRateBean2List, Integer[] color, String sGoodRateTitle)
	 {
		 
		     this.sLeftTitle = sLeftTitle;
		     this.sRightTitle = sRightTitle;
		     this.sOkQtyPlotLegend = sOkQtyPlotLegend;
		     this.sOkRatePlotLegend = sOkRatePlotLegend;
		     this.rateBean1List = goodRateBean1List;
		     this.rateBean2List = goodRateBean2List;
		     this.color = color;
		     this.sGoodRateTitle = sGoodRateTitle;
             chartLabels();
             dAxisMax = upToInteger();
             chartDataSet1();
             chartDataSet2();
             chartRender1();
             chartRender2();
             chartLnRender();

			//綁定手势滑动事件
//			this.bindTouch(this,chart);
//			this.bindTouch(this,chart2);
			
	 }

    private void init(BarChart chart){
        //设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
        int [] ltrb = getBarLnDefaultSpadding();
        chart.setPadding(ltrb[0],ltrb[1], DensityUtil.dip2px(getContext(), 30),DensityUtil.dip2px(getContext(), 23));

        //最大值
        double steps = Math.ceil(dAxisMax/5); //数据轴步长

        //坐标系
        chart.getDataAxis().setAxisMax(dAxisMax); //设置数据轴最大值
        chart.getDataAxis().setAxisMin(0); //设置数据轴最小值,默认为0
        chart.getDataAxis().setAxisSteps(steps); //设置数据轴步长
        //X轴数据
        chart.setCategories(chartLabels);
        chart.getBar().setBarStyle(XEnum.BarStyle.FILL);
        //让柱子间没空白
        chart.getBar().setBarInnerMargin(0.f);
        //显示标签值
        chart.getBar().setItemLabelVisible(true);
        chart.getBar().getItemLabelPaint().setTextSize(12.0f);
        //柱形标签值显示在柱形里面
        chart.getBar().setItemLabelStyle(XEnum.ItemLabelStyle.INNER);
        //X轴Y轴标签值离轴的距离
        chart.getDataAxis().setTickLabelMargin(5);
//        chart.getCategoryAxis().setTickLabelMargin(5);
        //关于柱形的图例说明
        chart.getPlotLegend().getPaint().setTextSize(10);
        chart.getPlotLegend().getPaint().setColor(axisColor);
        chart.getPlotLegend().setLabelMargin(5.0f);

        //柱形和标签居中方式
        chart.setBarCenterStyle(XEnum.BarCenterStyle.SPACE);
    }
    private void chartRender1()
    {
        try {
            init(chart);
            //标题
            chart.addSubtitle(sGoodRateTitle);
            chart.getPlotTitle().getSubtitlePaint().setColor(axisColor);
            chart.getPlotTitle().getSubtitlePaint().setTextSize(20.0f);
            chart.getPlotTitle().setVerticalAlign(XEnum.VerticalAlign.MIDDLE);
            //数据源
            chart.setDataSource(chartData);

            //定义数据轴标签显示格式
            chart.getCategoryAxis().setTickLabelRotateAngle(-15f);
            chart.getCategoryAxis().getTickLabelPaint().setTextSize(14);
//            chart2.getCategoryAxis().setLabelLineFeed(XEnum.LabelLineFeed.ODD_EVEN);
            chart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack(){

                @Override
                public String textFormatter(String value) {
                    // TODO Auto-generated method stub
                    Double tmp = Double.parseDouble(value);
                    DecimalFormat df=new DecimalFormat("#0");
                    String label = df.format(tmp).toString();
                    return (label);
                }

            });
            //设定格式
            chart.setItemLabelFormatter(new IFormatterDoubleCallBack() {
                @Override
                public String doubleFormatter(Double value) {
                    // TODO Auto-generated method stub
                    DecimalFormat df=new DecimalFormat("#0");
                    String label = df.format(value).toString();
                    return label;
                }});
            //隐藏图例
            chart.getPlotLegend().show();
            chart.getDataAxis().hide();
            chart.getCategoryAxis().hide();

            //轴颜色
            chart.getDataAxis().getAxisPaint().setColor(axisColor);
            chart.getCategoryAxis().getAxisPaint().setColor(axisColor);
            chart.getDataAxis().getTickMarksPaint().setColor(axisColor);
            chart.getCategoryAxis().getTickMarksPaint().setColor(axisColor);
            chart.getDataAxis().getTickLabelPaint().setColor(axisColor);
            chart.getCategoryAxis().getTickLabelPaint().setColor(axisColor);
            //显示网格背景
            chart.getPlotGrid().showEvenRowBgColor();
            chart.getPlotGrid().showOddRowBgColor();


        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e(TAG, "chart--render1---" + e.toString());
        }
    }

    private void chartRender2()
    {
        try {
            init(chart2);
            chart2.setDataSource(chartData2);
            //隐藏轴
            chart2.getPlotLegend().hide();
            chart2.getDataAxis().show();
            chart2.getCategoryAxis().show();

            chart2.getDataAxis().setLabelFormatter(new IFormatterTextCallBack(){

                @Override
                public String textFormatter(String value) {
                    // TODO Auto-generated method stub
                    Double tmp = Double.parseDouble(value);
                    DecimalFormat df=new DecimalFormat("#0");
                    String label = df.format(tmp).toString();
                    return (label);
                }

            });
            //设定格式
            chart2.setItemLabelFormatter(new IFormatterDoubleCallBack() {
                @Override
                public String doubleFormatter(Double value) {
                    // TODO Auto-generated method stub
                    DecimalFormat df=new DecimalFormat("#0");
                    String label = df.format(value).toString();
                    return label;
                }});

            //定义数据轴标签显示格式
            chart2.getCategoryAxis().setTickLabelRotateAngle(-15f);
            chart2.getCategoryAxis().getTickLabelPaint().setTextSize(14);
            chart2.getDataAxis().setTickLabelMargin(1);
//            chart2.getCategoryAxis().getTickLabelPaint().setTextAlign(Paint.Align.LEFT);

            //轴标题
            chart2.getAxisTitle().setLeftTitle(sLeftTitle);
            chart2.getAxisTitle().setRightTitle(sRightTitle);
//            chart2.getAxisTitle().setLowerTitle("时段");
            chart2.getAxisTitle().getRightTitlePaint().setColor(Color.GRAY);
            chart2.getAxisTitle().getRightTitlePaint().setTextSize(10);
            chart2.getAxisTitle().getLeftTitlePaint().setColor(Color.GRAY);
            chart2.getAxisTitle().getLeftTitlePaint().setTextSize(10);
            chart2.getAxisTitle().getLowerTitlePaint().setColor(Color.GRAY);
            chart2.getAxisTitle().getLowerTitlePaint().setTextSize(10);

        } catch (Exception e) {
            // TODO Auto-generated catch block
        }
    }

    /**
     * 显示投入总数
     */
    private void chartDataSet1()
    {
        //标签对应的柱形数据集
        List<Double> dataSeriesA= new LinkedList<Double>();
        List<Double> dataSeriesB= new LinkedList<Double>();


        if(rateBean1List!=null && rateBean1List.size()>=1){
            for (int i = 0; i < rateBean1List.size(); i++) {
                String totalQty1 = rateBean1List.get(i).getStationNoOutPutTotalQty1();
                double dTotalQty1 = 0d;
                if(TextUtils.isEmpty(totalQty1)){
                    dTotalQty1 = 0d;
                }else {
                    dTotalQty1 = Double.parseDouble(totalQty1);
                }
                dataSeriesA.add(dTotalQty1);

            }
            //spi
            BarData BarDataA = new BarData(rateBean1List.get(0).getStationName1(),dataSeriesA, color[3]);
            chartData.add(BarDataA);
        }
        if(rateBean2List!=null && rateBean2List.size()>=1){
            for (int i = 0; i < rateBean2List.size(); i++) {
                String totalQty2 = rateBean2List.get(i).getStationNoOutPutTotalQty2();
                double dTotalQty2 = 0d;
                if(TextUtils.isEmpty(totalQty2)){
                    dTotalQty2 = 0d;
                }else {
                    dTotalQty2 = Double.parseDouble(totalQty2);
                }
                dataSeriesB.add(dTotalQty2);

            }
            //aoi
            BarData BarDataB = new BarData(rateBean2List.get(0).getStationName2(),dataSeriesB, color[1]);
            chartData.add(BarDataB);

        }


        //显示图例说明
        if(rateBean1List!=null && rateBean1List.size()>=1){
            //显示图例说明
            //spi良品
            List<Double> dataSeriesAA= new LinkedList<Double>();
            dataSeriesAA.add(0d);
            BarData barDataAA = new BarData(rateBean1List.get(0).getStationName1() + sOkQtyPlotLegend,dataSeriesAA, color[2]);
            chartData.add(barDataAA);
            if(rateBean2List!=null && rateBean2List.size()>=1){
                //aoi良品
                List<Double> dataSeriesBB= new LinkedList<Double>();
                dataSeriesBB.add(0d);
                BarData BarDataBB = new BarData(rateBean2List.get(0).getStationName2() + sOkQtyPlotLegend,dataSeriesBB,color[0]);
                chartData.add(BarDataBB);
            }
            //spi良品率
            List<Double> dataSeriesCC= new LinkedList<Double>();
            dataSeriesCC.add(0d);
            BarData BarDataCC = new BarData(rateBean1List.get(0).getStationName1() + sOkRatePlotLegend,dataSeriesCC,Color.rgb(255, 0, 0));
            chartData.add(BarDataCC);
            if(rateBean2List!=null && rateBean2List.size()>=1){
                //aoi良品率
                List<Double> dataSeriesDD= new LinkedList<Double>();
                dataSeriesDD.add(0d);
                BarData BarDataDD = new BarData(rateBean2List.get(0).getStationName2() + sOkRatePlotLegend ,dataSeriesDD,Color.rgb(199, 64, 219));
                chartData.add(BarDataDD);
            }
        }

    }

    /**
     * 显示良品数
     */
    private void chartDataSet2()
    {
        //标签对应的柱形数据集
        List<Double> dataSeriesA= new LinkedList<Double>();
        List<Double> dataSeriesB= new LinkedList<Double>();
        LinkedList<Double> okRate1= new LinkedList<Double>();
        LinkedList<Double> okRate2= new LinkedList<Double>();

        if(rateBean1List!=null && rateBean1List.size()>=1){
            for (int i = 0; i < rateBean1List.size(); i++) {
                String goodRateQty = rateBean1List.get(i).getStationNoOutPutOkQty1();
                String totalQty1 = rateBean1List.get(i).getStationNoOutPutTotalQty1();
                double dGoodRateQty1 = 0d;
                if(TextUtils.isEmpty(goodRateQty)){
                    dGoodRateQty1 = 0d;
                }else {
                    dGoodRateQty1 = Double.parseDouble(goodRateQty);
                }
                dataSeriesA.add(dGoodRateQty1);
                double okRate = 0d;
                if(TextUtils.isEmpty(goodRateQty)||TextUtils.isEmpty(totalQty1) || goodRateQty.equals("0")||totalQty1.equals("0")){
                    okRate = 0d;
                }else {
                    okRate = Double.parseDouble(goodRateQty)/Double.parseDouble(totalQty1);
                    okRate = Math.round(okRate * 100);
                }
                okRate1.add(okRate);
            }
            //spi良品
            BarData BarDataA = new BarData("",dataSeriesA, color[2]);//spi良品
            chartData2.add(BarDataA);

            //将标签与对应的数据集分别绑定
            LineData lineData1 = new LineData("",okRate1,Color.rgb(255, 0, 0));
            lineData1.setDotStyle(XEnum.DotStyle.HIDE);
            lineData1.setDotRadius(3);
            lineData1.getLinePaint().setStrokeWidth(3);
//            lineData1.getDotPaint().setStrokeWidth(1.0f);
            lineData1.getDotPaint().setColor(Color.rgb(255, 0, 0));
            lineData1.setLabelVisible(true);
            lineData1.setLineStyle(XEnum.LineStyle.SOLID);
//            lineData1.getDotLabelPaint().setTextAlign(Paint.Align.LEFT);
            lineData1.getDotLabelPaint().setTextSize(12);
            lineData1.getDotLabelPaint().setColor(Color.rgb(255, 0, 0));
            lineData1.getDotLabelPaint().setStrokeWidth(2);
            lineData1.getLabelOptions().setMargin(1.0f);
            lineData1.getLabelOptions().setOffsetY(5.0f);
            lineData1.getLabelOptions().setLabelBoxStyle(XEnum.LabelBoxStyle.TEXT);
            chartDatasetLn.add(lineData1);

        }
        if(rateBean2List!=null && rateBean2List.size()>=1){
            for (int i = 0; i < rateBean2List.size(); i++) {
                String goodRateQty = rateBean2List.get(i).getStationNoOutPutOkQty2();
                double dGoodRateQty2 = 0d;
                if(TextUtils.isEmpty(goodRateQty)){
                    dGoodRateQty2 = 0d;
                }else {
                    dGoodRateQty2 = Double.parseDouble(goodRateQty);
                }
                dataSeriesB.add(dGoodRateQty2);
                String totalQty2 = rateBean2List.get(i).getStationNoOutPutTotalQty2();
                double okRate = 0d;
                if(TextUtils.isEmpty(goodRateQty)||TextUtils.isEmpty(totalQty2) || goodRateQty.equals("0")||totalQty2.equals("0")){
                    okRate = 0d;
                }else {
                    okRate = Double.parseDouble(goodRateQty)/Double.parseDouble(totalQty2);
                    okRate = Math.round(okRate * 100);
                }
                okRate2.add(okRate);
            }
            //aoi良品
            BarData BarDataB = new BarData("",dataSeriesB, color[0]);//aoi良品
            chartData2.add(BarDataB);

            LineData lineData2 = new LineData("",okRate2,Color.rgb(199, 64, 219));
            lineData2.setDotStyle(XEnum.DotStyle.HIDE);
            lineData2.setDotRadius(3);

            lineData2.getLinePaint().setStrokeWidth(3);
            lineData2.setLabelVisible(true);
            lineData2.setLineStyle(XEnum.LineStyle.SOLID);
            lineData2.getDotLabelPaint().setTextSize(12);
            lineData2.getDotLabelPaint().setColor(Color.rgb(199, 64, 219));
            lineData2.getDotLabelPaint().setStrokeWidth(2);
            lineData2.getLabelOptions().setLabelBoxStyle(XEnum.LabelBoxStyle.TEXT);
//            lineData2.getLabelOptions().setCapBoxAngleHeight(1);
            lineData2.getLabelOptions().setMargin(1.0f);
            lineData2.getLabelOptions().setOffsetY(5.0f);
            chartDatasetLn.add(lineData2);
        }
    }

    //X轴数据
    private void chartLabels()
    {
        if(rateBean1List!=null && rateBean1List.size()>=1){
            if(rateBean1List.get(0).getPeriodName().equals("rate0")){
                chartLabels.clear();
                chartLabels.add("");
                return;
            }
            for (int i = 0; i < rateBean1List.size(); i++) {
                chartLabels.add(rateBean1List.get(i).getPeriodName());
            }
        }

    }
    private void chartLnRender()
    {
        try {
            //设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
            int [] ltrb = getBarLnDefaultSpadding();
            lnChart.setPadding(ltrb[0],ltrb[1], DensityUtil.dip2px(getContext(), 30),DensityUtil.dip2px(getContext(), 23));

            renderLnAxis();

            lnChart.getPlotLegend().show();
            lnChart.getPlotLegend().setType(XEnum.LegendType.COLUMN);
            lnChart.getPlotLegend().setVerticalAlign(XEnum.VerticalAlign.TOP);
            lnChart.getPlotLegend().setHorizontalAlign(XEnum.HorizontalAlign.LEFT);
            lnChart.getPlotLegend().hideBackground();
            lnChart.getPlotLegend().setVerticalAlign(XEnum.VerticalAlign.TOP);
            lnChart.getPlotLegend().setHorizontalAlign(XEnum.HorizontalAlign.RIGHT);
//            lnChart.setLineAxisIntersectVisible(true);//值为0时是否显示折线点
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e(TAG, "chartLnRender---" + e.toString());
        }
    }

    /**
     * 折线图轴相关
     */
    private void renderLnAxis()
    {
        //标签轴
        lnChart.setCategories(chartLabels);
        lnChart.getCategoryAxis().hide();

        //设定数据源
        lnChart.setDataSource(chartDatasetLn);
        //数据轴
        lnChart.setDataAxisLocation(XEnum.AxisLocation.RIGHT);
        DataAxis dataAxis = lnChart.getDataAxis();
        dataAxis.show();
        dataAxis.setAxisMax(120);
        dataAxis.setAxisMin(0);
        dataAxis.setAxisSteps(20);
        dataAxis.getAxisPaint().setColor(Color.rgb(255, 0, 0));
        dataAxis.getTickMarksPaint().setColor(Color.rgb(255, 0, 0));
        dataAxis.setTickLabelMargin(1);
        //定制数据轴上的标签格式
        lnChart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack(){

            @Override
            public String textFormatter(String value) {
                // TODO Auto-generated method stub

                double label=Double.parseDouble(value);
                DecimalFormat df=new DecimalFormat("#0");
                return df.format(label).toString();
            }

        });
        lnChart.getCategoryAxis().setLabelFormatter(new IFormatterTextCallBack(){

            @Override
            public String textFormatter(String value) {
                // TODO Auto-generated method stub

                double label=Double.parseDouble(value);
                DecimalFormat df=new DecimalFormat("#0");
                return df.format(label).toString();
            }

        });
        //允许线与轴交叉时，线会断开
        lnChart.setLineAxisIntersectVisible(false);

        //调整右轴显示风格
        lnChart.getDataAxis().setHorizontalTickAlign(Paint.Align.RIGHT);
        lnChart.getDataAxis().getTickLabelPaint().setTextAlign(Paint.Align.LEFT);

        lnChart.setXCoordFirstTickmarksBegin(true);
        //
        lnChart.setBarCenterStyle(XEnum.BarCenterStyle.SPACE);

    }

    //向上取整
    private double upToInteger(){
        //获取ArrayList中的最大值
        double end;
        double max = Math.ceil(arrayListMax()); //不小于他的最小整数
        int num = (int) max;
        int a = 1;
        if(num % 10 != 0){
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
            try {
                end = Double.parseDouble(buffer.toString());
            }catch (Exception e){
                end = arrayListMax();
            }
        }else {
            end = max;
            if(num == 0){
                end = 2;
            }
        }
        return end;
    }

    //获取ArrayList中的最大值
    public double arrayListMax() {
        try {
            //取投入数最大值
            double maxSPITotal = 0.0; //最大值
            int maxSPICount = rateBean1List.size();
            if (maxSPICount >= 1) {
                String sSPITotal = rateBean1List.get(0).getStationNoOutPutTotalQty1().toString();
                double max = 0.0;
                if(!TextUtils.isEmpty(sSPITotal)){
                    max = Double.parseDouble(sSPITotal);
                }
                for (int i = 0; i < maxSPICount; i++) {
                    String tempSPITotal = rateBean1List.get(i).getStationNoOutPutTotalQty1().toString();
                    double temp = 0.0;
                    if(!TextUtils.isEmpty(tempSPITotal)){
                        temp = Double.parseDouble(tempSPITotal);
                    }
                    if (temp > max) {
                        max = temp;
                    }
                }
                maxSPITotal = max;
            }
            //取产出数最大值
            double maxAOITotal = 0.0; //最大值
            int maxAOICount = rateBean2List.size();
            if (maxAOICount >= 1) {
                String sAOITotalQty = rateBean2List.get(0).getStationNoOutPutTotalQty2().toString();
                double max = 0.0;
                if(!TextUtils.isEmpty(sAOITotalQty)){
                    max = Double.parseDouble(sAOITotalQty);
                }
                for (int i = 0; i < maxAOICount; i++) {
                    String tempAOITotalQty = rateBean2List.get(i).getStationNoOutPutTotalQty2().toString();
                    double temp = 0.0;
                    if(!TextUtils.isEmpty(tempAOITotalQty)){
                        temp = Double.parseDouble(tempAOITotalQty);
                    }
                    if (temp > max) {
                        max = temp;
                    }
                }
                maxAOITotal = max;
            }
            if(maxSPITotal > maxAOITotal){
                return maxSPITotal;
            }else {
                return maxAOITotal;
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
        chart2.setChartRange(w,h);
        lnChart.setChartRange(w,h);
    }
    @Override
    public void render(Canvas canvas) {
        try{
            chart.render(canvas);
            chart2.render(canvas);
            lnChart.render(canvas);
        } catch (Exception e){
            Log.e(TAG, "render---" + e.toString());
        }
    }
    //重载掉，让其不能移动,实际应用时，可直接继承GraphicalView
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

}

