package com.board.testboard.ui.view.module;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;


import com.board.testboard.app.MyApplication;
import com.board.testboard.bean.LineNoProductionBean;

import org.xclcharts.chart.BarChart;
import org.xclcharts.chart.BarData;
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
 * [双-柱形图(竖向)-线别产能]
 */

public class MultiaxialLineNoProduction extends BaseChartView{

    BarChart chart = new BarChart();
    //标签轴
    List<String> chartLabels = new LinkedList<String>();
    List<BarData> chartDataset = new LinkedList<BarData>();

    private List<LineNoProductionBean> lineNoProductionList = null;
    private Integer[] color = {MyApplication.nDefaultChartLineProductColor1, MyApplication.nDefaultChartLineProductColor2};
    private String sChartLineNoProductionTitle;
    private String sChartLineNoProductionLabel[];

    public MultiaxialLineNoProduction(Context context) {
        super(context);
    }

    public MultiaxialLineNoProduction(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiaxialLineNoProduction(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //用于初始化
    public void initView(List<LineNoProductionBean> lineNoProductionList) {
        this.lineNoProductionList = lineNoProductionList;
        //设置X轴的标签
        chartLabels();
        //绘制柱形图
        chartDataSet();
        //画轴
        chartRender();
    }
    //用于初始化
    public void initView(List<LineNoProductionBean> lineNoProductionList, Integer[] color,String sChartLineNoProductionTitle,String sChartLineNoProductionLabel[]) {
        this.lineNoProductionList = lineNoProductionList;
        this.color = color;
        this.sChartLineNoProductionTitle = sChartLineNoProductionTitle;
        this.sChartLineNoProductionLabel = sChartLineNoProductionLabel;
        //设置X轴的标签
        chartLabels();
        //绘制柱形图
        chartDataSet();
        //画轴
        chartRender();
    }
    //清除数组
    public void refreshChart(){
        chartLabels.clear();
        chartDataset.clear();
        //数据源
        chart.setCategories(null);
        chart.setDataSource(null);
        this.invalidate();
    }

    //设置X轴的标签
    private void chartLabels() {
        for (int i = 0; i < lineNoProductionList.size(); i++) {
            String sCountingPointName = lineNoProductionList.get(i).getsLineNo(); //线别名称
            if (!TextUtils.isEmpty(sCountingPointName)) {
                chartLabels.add(sCountingPointName);
            }
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
        if(lineNoProductionList!=null && lineNoProductionList.size()>=1){
            for (int i = 0; i < lineNoProductionList.size(); i++) {
                String sDlQtyInput = lineNoProductionList.get(i).getSdlQtyInput().toString();
                String sDlQtyOutput = lineNoProductionList.get(i).getSdlQtyOutput().toString();
                double dMissRate1 = 0.0;
                double dMissRate2 = 0.0;
                if(!TextUtils.isEmpty(sDlQtyInput)){
                    dMissRate1 = Double.parseDouble(sDlQtyInput); //投入数
                }
                if(!TextUtils.isEmpty(sDlQtyOutput)){
                    dMissRate2 = Double.parseDouble(sDlQtyOutput); //产出数
                }
                dataSeries1.add(dMissRate1);
                dataSeries2.add(dMissRate2);
                dataColorA.add(color[0]); //默认RED
                dataColorB.add(color[1]); //默认RED
            }
        }
        //此地的颜色为Key值颜色及柱形的默认颜色
        BarData BarDataA = new BarData(sChartLineNoProductionLabel[0],dataSeries1,dataColorA, color[0]);
        BarData BarDataB = new BarData(sChartLineNoProductionLabel[1],dataSeries2,dataColorB,color[1]);
        chartDataset.add(BarDataA);
        chartDataset.add(BarDataB);
    }

    //画轴
    private void chartRender() {
        try {
            //设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
            int [] ltrb = getBarLnDefaultSpadding();
            chart.setPadding(ltrb[0], ltrb[1], ltrb[2], DensityUtil.dip2px(getContext(), 23));

            chart.setChartDirection(XEnum.Direction.VERTICAL);
            //标题
            //chart.setTitle("线别产能"); //主标题
            chart.addSubtitle(sChartLineNoProductionTitle); //子标题
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

        }
    }

    //柱形图轴相关
    private void renderBarAxis() {
        //标签轴
        chart.setCategories(chartLabels);
        //数据轴
        chart.setDataSource(chartDataset);

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
        categoryAxis.setTickLabelRotateAngle(0f);
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
        chart.getCategoryAxis().showAxisLabels();

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
            int maxInPutQtyCount = lineNoProductionList.size();
            if (maxInPutQtyCount >= 1) {
                String sDlQtyInput = lineNoProductionList.get(0).getSdlQtyInput().toString();
                double max = 0.0;
                if(!TextUtils.isEmpty(sDlQtyInput)){
                    max = Double.parseDouble(sDlQtyInput);
                }

                for (int i = 0; i < maxInPutQtyCount; i++) {
                    String sTempDlQtyInput = lineNoProductionList.get(i).getSdlQtyInput().toString();
                    double temp = 0.0;
                    if(!TextUtils.isEmpty(sTempDlQtyInput)){
                        temp = Double.parseDouble(sTempDlQtyInput);
                    }
                    if (temp > max) {
                        max = temp;
                    }
                }
                maxInPutQty = max;
            }
            //取产出数最大值
            double maxOutPutQty = 0.0; //最大值
            int maxOutPutQtyCount = lineNoProductionList.size();
            if (maxOutPutQtyCount >= 1) {
                String sDlQtyOutput = lineNoProductionList.get(0).getSdlQtyOutput().toString();
                double max = 0.0;
                if(!TextUtils.isEmpty(sDlQtyOutput)){
                    max = Double.parseDouble(sDlQtyOutput);
                }
                for (int i = 0; i < maxOutPutQtyCount; i++) {
                    String sTempDlQtyOutput = lineNoProductionList.get(i).getSdlQtyOutput().toString();
                    double temp = 0.0;
                    if(!TextUtils.isEmpty(sTempDlQtyOutput)){
                        temp = Double.parseDouble(sTempDlQtyOutput);
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
        }
    }
}
