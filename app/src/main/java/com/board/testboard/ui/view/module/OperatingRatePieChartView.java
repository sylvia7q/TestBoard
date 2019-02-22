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
 * @Description Android图表基类库
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @Copyright Copyright (c) 2014 XCL-Charts (www.xclcharts.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.0
 */
package com.topcee.kanban.ui.view.module;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.topcee.kanban.bean.OperatingRateBean;

import org.xclcharts.chart.PieChart;
import org.xclcharts.chart.PieData;
import org.xclcharts.event.click.ArcPosition;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.plot.PlotLegend;

import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName OperatingRatePieChartView
 * @Description  [(饼图)-设备运转率]
 * @author longq
 */
public class OperatingRatePieChartView extends BaseChartView implements Runnable {
	
	private String TAG = "PieChart01View";
	private PieChart chart = new PieChart();
	private ArrayList<PieData> chartData = new ArrayList<PieData>();
	private int mSelectedID = -1;
	private List<OperatingRateBean> rateBeans;
	private int color;
	private String sTitle = "设备运转率";
	private String sPlotTitle = "运转率";
	private String sRate = "";
	private int nRate = 0;


	public OperatingRatePieChartView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView();
	}
	
	public OperatingRatePieChartView(Context context, AttributeSet attrs){
        	super(context, attrs);
		initView();
	 }
	 public OperatingRatePieChartView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
		 initView();
	 }
	public void initView()
	{
		chartDataSet();
		chartRender();

		//綁定手势滑动事件
		this.bindTouch(this,chart);
		new Thread(this).start();
	}
	public void initView(List<OperatingRateBean> rateBeans,int color,String title,String plotTitle)
	{
			this.rateBeans = rateBeans;
			this.color = color;
			this.sTitle = title;
			this.sPlotTitle = plotTitle;
		 	chartDataSet();
			chartRender();
			
			//綁定手势滑动事件
			this.bindTouch(this,chart);
			new Thread(this).start();
	}

	//清除数组
	public void refreshChart(){
		chartData.clear();
		//数据源
		chart.setDataSource(null);
		chart.getPlotGrid().hideEvenRowBgColor();
		chart.getPlotGrid().hideOddRowBgColor();
		this.invalidate();
	}
	@Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {  
        super.onSizeChanged(w, h, oldw, oldh);  
       //图所占范围大小
        chart.setChartRange(w,h);
    }  	
	
	
	private void chartRender()
	{
		try {					
			
			//设置绘图区默认缩进px值
			int [] ltrb = getPieDefaultSpadding();
			chart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);
			
			//设置起始偏移角度(即第一个扇区从哪个角度开始绘制)
			//chart.setInitialAngle(90);	
			
			//标签显示(隐藏，显示在中间，显示在扇区外面)
			chart.setLabelStyle(XEnum.SliceLabelStyle.INSIDE);
			chart.getLabelPaint().setColor(Color.WHITE);
			
			//标题
//			chart.addSubtitle(sTitle);
			chart.getPlotTitle().getTitlePaint().setTextSize(20);
								
			//chart.setDataSource(chartData);
			
			//激活点击监听
//			chart.ActiveListenItemClick();
//			chart.showClikedFocus();
			
			//设置允许的平移模式
			//chart.enablePanMode();
			//chart.setPlotPanMode(XEnum.PanMode.HORIZONTAL);	
			
			//显示图例
			PlotLegend legend = chart.getPlotLegend();
			legend.show();
			legend.setType(XEnum.LegendType.ROW);
			legend.setHorizontalAlign(XEnum.HorizontalAlign.LEFT);
			legend.setVerticalAlign(XEnum.VerticalAlign.TOP);
			legend.hideBox();
						
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}
	}
	
	private void chartDataSet()
	{
		/*
		//设置图表数据源		
		chartData.add(new PieData("HP","20%",20,(int)Color.rgb(155, 187, 90)));
		chartData.add(new PieData("IBM","30%",30,(int)Color.rgb(191, 79, 75),false));
		chartData.add(new PieData("DELL","10%",10,(int)Color.rgb(242, 167, 69)));
		//将此比例块突出显示
		chartData.add(new PieData("EMC","40%",40,(int)Color.rgb(60, 173, 213),false));
		*/
		if(rateBeans!=null && rateBeans.size()>0){
			sRate = rateBeans.get(0).getRUN_RATE();
			if(TextUtils.isEmpty(sRate)||sRate.equals("0")){
				nRate = 0;
			}else {
				double dRate = Double.parseDouble(sRate);
				nRate = (int) Math.round(dRate * 100);
			}
		}else {
			nRate = 0;
		}

		chartData.add(new PieData(sTitle,nRate + "%" ,   nRate, Color.rgb(0, 145, 57)));
		if(nRate == 0){
			chartData.add(new PieData("",nRate + "%",   (100 - nRate), Color.GRAY));
		}else {
			chartData.add(new PieData("","",   (100 - nRate), Color.GRAY));
		}

		chart.getLabelPaint().setTextSize(20);
		chart.getLabelPaint().setTypeface(Typeface.DEFAULT_BOLD);//黑体字体类型
	}

	@Override
    public void render(Canvas canvas) {
        try{
            chart.render(canvas);
        } catch (Exception e){
        	Log.e(TAG, e.toString());
        }
    }


	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {          
         	chartAnimation();         	
         }
         catch(Exception e) {
             Thread.currentThread().interrupt();
         }  
	}
	
	private void chartAnimation()
	{
		  try {       
			 
			    chart.setDataSource(chartData);
			  	int count = 360 / 10;
			  	
	          	for(int i=1;i<count ;i++)
	          	{
	          		Thread.sleep(40);
	          		
	          		chart.setTotalAngle(10 * i);
	          		
	          		//激活点击监听
	    			if(count - 1 == i)
	    			{
	    				chart.setTotalAngle(360);
	    				
	    				chart.ActiveListenItemClick();
	    				//显示边框线，并设置其颜色
	    				chart.getArcBorderPaint().setColor(Color.YELLOW);
	    				chart.getArcBorderPaint().setStrokeWidth(3);
	    			}
	    			
	          		postInvalidate();            				          	          	
	          }
			  
          }
          catch(Exception e) {
              Thread.currentThread().interrupt();
          }       
		  
	}
	
	/*
	 * 另一种动画
	private void chartAnimation()
	{
		  try {       
			 
			  	float sum = 0.0f;
			  	int count = chartData.size();
	          	for(int i=0;i< count ;i++)
	          	{
	          		Thread.sleep(150);
	          	
	          		ArrayList<PieData> animationData = new ArrayList<PieData>();
	        
	          		sum = 0.0f;
	          			          		
	          		for(int j=0;j<=i;j++)
	          		{            			            			
	          			animationData.add(chartData.get(j));
	          			sum = (float) MathHelper.getInstance().add(
	          									sum , chartData.get(j).getPercentage());	          			
	          		}   		          		
	          			          			          				          				          	
	          		animationData.add(new PieData("","",  MathHelper.getInstance().sub(100.0f , sum),
	          											  Color.argb(1, 0, 0, 0)));		          		
	          		chart.setDataSource(animationData);
	          	
	          		//激活点击监听
	    			if(count - 1 == i)
	    			{
	    				chart.ActiveListenItemClick();
	    				//显示边框线，并设置其颜色
	    				chart.getArcBorderPaint().setColor(Color.YELLOW);
	    				chart.getArcBorderPaint().setStrokeWidth(3);
	    			}
	    			
	          		postInvalidate();            				          	          	
	          }
			  
          }
          catch(Exception e) {
              Thread.currentThread().interrupt();
          }       
		  
	}
	*/
		
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub		
		super.onTouchEvent(event);		
		if(event.getAction() == MotionEvent.ACTION_UP)
		{						
			if(chart.isPlotClickArea(event.getX(),event.getY()))
			{											
				triggerClick(event.getX(),event.getY());					
			}
		}
		return true;
	}
	

	//触发监听
	private void triggerClick(float x,float y)
	{	
		if(!chart.getListenItemClickStatus())return;
				
		ArcPosition record = chart.getPositionRecord(x,y);
		if( null == record) return;
		/*
		PieData pData = chartData.get(record.getDataID());		
		Toast.makeText(this.getContext(),								
				" key:" +  pData.getKey() +
				" Label:" + pData.getLabel() ,
				Toast.LENGTH_SHORT).show(); 
		*/
		
		//用于处理点击时弹开，再点时弹回的效果
		PieData pData = chartData.get(record.getDataID());
		if(record.getDataID() == mSelectedID ) 
		{
			boolean bStatus = chartData.get(mSelectedID).getSelected();
			chartData.get(mSelectedID).setSelected(!bStatus);
		}else{
			if(mSelectedID >= 0)
				chartData.get(mSelectedID).setSelected(false);
			pData.setSelected(true);
		}
		mSelectedID = record.getDataID();
		this.refreshChart();		
				
		/*
		boolean isInvaldate = true;		
		for(int i=0;i < chartData.size();i++)
		{	
			PieData cData = chartData.get(i);
			if(i == record.getDataID())
			{
				if(cData.getSelected()) 
				{
					isInvaldate = false;
					break;
				}else{
					cData.setSelected(true);	
				}
			}else
				cData.setSelected(false);			
		}
		if(isInvaldate)this.invalidate();
		*/
		
	}
	
	
}
