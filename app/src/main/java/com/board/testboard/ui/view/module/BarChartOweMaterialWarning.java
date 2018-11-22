package com.board.testboard.ui.view.module;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;


import com.board.testboard.app.MyApplication;
import com.board.testboard.bean.WarnBean;

import org.xclcharts.chart.BarChart;
import org.xclcharts.chart.BarData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.common.IFormatterDoubleCallBack;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.axis.CategoryAxis;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * [单-柱形图(竖向)-欠料预警]
 */

public class BarChartOweMaterialWarning extends BaseChartView{
    private String TAG = "Topcee";
    private BarChart chart = new BarChart();
    //标签轴
    List<String> chartLabels = new LinkedList<String>();
    List<BarData> BarDataSet = new LinkedList<BarData>();

    //Table抛料率
    private List<WarnBean> oweMaterialWarningList = null;
    private String sChartWarnTitle = "";
    private String sChartWarnLabel = "";
    private Integer color = MyApplication.nDefaultChartWarnColor;

    public BarChartOweMaterialWarning(Context context) {
        super(context);
    }

    public BarChartOweMaterialWarning(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BarChartOweMaterialWarning(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    //用于初始化
    public void initView(List<WarnBean> oweMaterialWarningList) {
        this.oweMaterialWarningList = oweMaterialWarningList;
        //设置X轴的标签
        chartLabels();
        //绘制柱形图
        chartDataSet();
        //画轴
        chartRender();
    }
    //用于初始化
    public void initView(List<WarnBean> oweMaterialWarningList,Integer color,String sChartWarnTitle,String sChartWarnLabel) {
        this.oweMaterialWarningList = oweMaterialWarningList;
        this.color = color;
        this.sChartWarnTitle = sChartWarnTitle;
        this.sChartWarnLabel = sChartWarnLabel;
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
        BarDataSet.clear();
        //数据源
        chart.setCategories(null);
        chart.setDataSource(null);
        this.invalidate();
    }

    //设置X轴的标签
    private void chartLabels() {
        for (int i = 0; i < oweMaterialWarningList.size(); i++) {
            // 设备代码[站位]
            String sMachineSeqFid = "[" + oweMaterialWarningList.get(i).getMACHINE_SEQ() + "]" + oweMaterialWarningList.get(i).getFID() ;
            if (!TextUtils.isEmpty(sMachineSeqFid)) {
                chartLabels.add(sMachineSeqFid);
            }
        }
        if(oweMaterialWarningList.get(0).getMACHINE_SEQ().equals("warn0")||oweMaterialWarningList.get(0).getMACHINE_SEQ().equals("warn1")){
            chartLabels.clear();
            chartLabels.add("");
            chartLabels.add("");
        }
    }

    //绘制柱形图
    private void chartDataSet() {
        //标签1对应的柱形数据集
        List<Double> dataSeriesA = new LinkedList<Double>();

        //依数据值确定对应的柱形颜色.
        List<Integer> dataColorA = new LinkedList<Integer>();
        if(oweMaterialWarningList!=null && oweMaterialWarningList.size()>=1){
            for (int i = 0; i < oweMaterialWarningList.size(); i++) {
                String sNeedPercent = oweMaterialWarningList.get(i).getNEED_PERCENT().toString();
                double dMissRate = 0.0;
                if(!TextUtils.isEmpty(sNeedPercent)){
                     dMissRate = Double.parseDouble(sNeedPercent); //可用百分比
                }
                dataSeriesA.add(dMissRate);
                dataColorA.add(color);

            }
        }

        //此地的颜色为Key值颜色及柱形的默认颜色
        //BarDataSet.add(new BarData("可用百分比", dataSeriesA, dataColorA, Color.rgb(255, 215, 0)));
        BarDataSet.add(new BarData(sChartWarnLabel, dataSeriesA, dataColorA, color));
    }

    //画轴
    private void chartRender() {
        try {

            //设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
            int [] ltrb = getBarLnDefaultSpadding();

            //设置绘图区与整个图表范围的缩收间距
            chart.setPadding(ltrb[0], ltrb[1], ltrb[2], DensityUtil.dip2px(getContext(), 25));

            //显示边框
            //chart.showRoundBorder();

            //设置图的显示方向,即横向还是竖向显示柱形
            chart.setChartDirection(XEnum.Direction.VERTICAL);

            //数据源
            chart.setCategories(chartLabels);
            chart.setDataSource(BarDataSet);

            //物料可用百分比最大值
            //最大值
            double max = upToInteger(); //向上取整
            double steps = Math.ceil(max/5); //数据轴步长

            //坐标系
            chart.getDataAxis().setAxisMax(max); //设置数据轴最大值
            chart.getDataAxis().setAxisMin(0); //设置数据轴最小值,默认为0
            chart.getDataAxis().setAxisSteps(steps); //设置数据轴步长

            //指定数据X轴标签旋转-15度显示
            chart.getCategoryAxis().setTickLabelRotateAngle(-15f);
            Paint labelPaint = chart.getCategoryAxis().getTickLabelPaint();
            labelPaint.setTextAlign(Paint.Align.RIGHT);
            labelPaint.setColor(Color.rgb(0, 75, 106));

            //标题
            //chart.setTitle("欠料预警"); //主标题
            chart.addSubtitle(sChartWarnTitle); //子标题
            chart.setTitleAlign(XEnum.HorizontalAlign.CENTER);
            chart.getPlotTitle().getTitlePaint().setTextSize(20);
            chart.setTitleVerticalAlign(XEnum.VerticalAlign.MIDDLE);

            //Y轴标题
            //chart.getAxisTitle().setLeftTitle("可用百分比(%)");

            //指隔多少个轴刻度(即细刻度)后为主刻度
            chart.getDataAxis().setDetailModeSteps(1);

            //背景网格
            chart.getPlotGrid().showEvenRowBgColor();
            chart.getPlotGrid().showOddRowBgColor();

            //定义数据轴标签显示格式
            chart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack(){

                @Override
                public String textFormatter(String value) {
                    // TODO Auto-generated method stub

                    DecimalFormat df=new DecimalFormat("#0");
                    Double tmp = Double.parseDouble(value);
                    String label = df.format(tmp).toString();
                    return label;
                }

            });

            //定制标签轴标签的标签格式
            CategoryAxis categoryAxis = chart.getCategoryAxis();
            categoryAxis.setTickLabelRotateAngle(-15f);
            categoryAxis.getTickLabelPaint().setTextSize(14);
            categoryAxis.getTickLabelPaint().setColor(Color.rgb(0,0,0));
            categoryAxis.getTickLabelPaint().setTextAlign(Paint.Align.CENTER);

            //定义标签轴标签显示格式
            chart.getCategoryAxis().setLabelFormatter(new IFormatterTextCallBack(){

                @Override
                public String textFormatter(String value) {
                    // TODO Auto-generated method stub
                    String label = value;
                    return label;
                }

            });

            //定义柱形上标签显示格式
            chart.getBar().setItemLabelVisible(true);
            chart.setItemLabelFormatter(new IFormatterDoubleCallBack() {
                @Override
                public String doubleFormatter(Double value) {
                    // TODO Auto-generated method stub
                    DecimalFormat df=new DecimalFormat("#0");
                    String label = df.format(value).toString() + "%";
                    return label;
                }});

            //定义柱形上标签显示颜色
            chart.getBar().getItemLabelPaint().setTextSize(12);
            chart.getBar().getItemLabelPaint().setColor(Color.rgb(0, 0, 0));
            chart.getBar().getItemLabelPaint().setTypeface(Typeface.DEFAULT_BOLD);

            //让柱子间没空白
            chart.getBar().setBarInnerMargin(0.5f); //可尝试0.1或0.5各有啥效果噢

            //禁用平移模式
            chart.disablePanMode();

            //提高性能
            chart.disableHighPrecision();

            //柱形和标签居中方式
            chart.setBarCenterStyle(XEnum.BarCenterStyle.TICKMARKS);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e(TAG, e.toString());
        }
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
            double maxNeedPercent = 0.0; //最大值
            int needPercentCount = oweMaterialWarningList.size();
            if (needPercentCount >= 1) {
                String sNeedPercent = oweMaterialWarningList.get(0).getNEED_PERCENT().toString();
                double max = 0.0;
                if(!TextUtils.isEmpty(sNeedPercent)){
                    max = Double.parseDouble(sNeedPercent);
                }
                for (int i = 0; i < needPercentCount; i++) {
                    String tempNeedPercent = oweMaterialWarningList.get(i).getNEED_PERCENT().toString();
                    double temp = 0.0;
                    if(!TextUtils.isEmpty(tempNeedPercent)){
                        temp = Double.parseDouble(tempNeedPercent);
                    }
                    if (temp > max) {
                        max = temp;
                    }
                }
                maxNeedPercent = max;
            }
            return maxNeedPercent;
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
            //chart.setChartRange(this.getMeasuredWidth(), this.getMeasuredHeight());
            chart.render(canvas);
        } catch (Exception e){
            Log.e(TAG, e.toString());
        }
    }
}
