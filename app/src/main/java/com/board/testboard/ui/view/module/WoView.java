package com.board.testboard.ui.view.module;

import android.content.Context; 
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.board.testboard.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 制令单模块
 */
public class WoView extends LinearLayout {

    private Context context;
    private LinearLayout view_wo_ll_bg;
    //wo信息
    private TextView txt_customer; //客户
    private TextView tv_customer; //客户
    private TextView txt_product_no; //产品号
    private TextView tv_product_no; //产品号
    private TextView txt_wo; //制令单
    private TextView tv_wo; //制令单
    private TextView txt_wo_plan_qty; //制令单数量
    private TextView tv_wo_plan_qty; //制令单数量
    private TextView txt_cur_shift_total_qty; //当前班次总产量
    private TextView tv_cur_shift_total_qty; //当前班次总产量
    private TextView txt_production_schedule; //生产进度
    private TextView tv_production_schedule; //生产进度
    private ProgressBar tv_progressbar; //进度条
    private TextView tv_progressbar_data; //进度条百分比


    public WoView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public WoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }


    private void init(){
        View view = LayoutInflater.from(context).inflate(R.layout.view_wo, this);

        view_wo_ll_bg = view.findViewById(R.id.view_wo_ll_bg);
        //wo信息
        txt_customer =  view.findViewById(R.id.txt_customer); //客户
        txt_product_no =  view.findViewById(R.id.txt_part_no); //产品号
        txt_wo =  view.findViewById(R.id.txt_wo); //制令单
        txt_wo_plan_qty =  view.findViewById(R.id.txt_wo_plan_qty); //制令单数量
        txt_cur_shift_total_qty =  view.findViewById(R.id.txt_current_shift_total_qty); //当前班次总产量
        txt_production_schedule =  view.findViewById(R.id.txt_production_schedule); //生产进度

        tv_customer =  view.findViewById(R.id.tv_customer); //客户
        tv_product_no =  view.findViewById(R.id.tv_part_no); //产品号
        tv_wo =  view.findViewById(R.id.tv_wo); //制令单
        tv_wo_plan_qty =  view.findViewById(R.id.tv_wo_plan_qty); //制令单数量
        tv_cur_shift_total_qty =  view.findViewById(R.id.tv_current_shift_total_qty); //当前班次总产量
        tv_production_schedule =  view.findViewById(R.id.tv_production_schedule); //生产进度
        tv_progressbar =  view.findViewById(R.id.tv_progressbar); //进度条
        tv_progressbar_data =  view.findViewById(R.id.tv_progressbar_data); //进度条百分比
    }
    public void setBackgroundResource(int resid){
        view_wo_ll_bg.setBackgroundResource(resid);
    }

    /**
     * 设置制令单信息颜色
     */
    public void setWoInfoTextColor(int color){
        txt_customer.setTextColor(color);
        txt_product_no.setTextColor(color);
        txt_wo.setTextColor(color);
        txt_wo_plan_qty.setTextColor(color);
        txt_cur_shift_total_qty.setTextColor(color);
        txt_production_schedule.setTextColor(color);

        tv_customer.setTextColor(color);
        tv_product_no.setTextColor(color);
        tv_wo.setTextColor(color);
        tv_wo_plan_qty.setTextColor(color);
        tv_cur_shift_total_qty.setTextColor(color);
        tv_production_schedule.setTextColor(color);
    }


    /**
     * 设置制令单信息背景框颜色
     */
    public void setWoInfoBgColor(int color){
        view_wo_ll_bg.setBackgroundColor(color);
    }
    /**
     * 设置客户
     * @param txt
     */
    public void setCustomText(String txt){
        tv_customer.setText(txt);
    }
    /**
     * 设置客户
     * @param txt
     */
    public void setCustomTitle(String txt){
        txt_customer.setText(txt);
    }
    /**
     * 设置客户
     * @param txt
     */
    public void setCustomText(int txt){
        tv_customer.setText(txt);
    }
    /**
     * 设置产品号
     * @param txt
     */
    public void setProductNoText(String txt){
        tv_product_no.setText(txt);
    }
    /**
     * 设置产品号
     * @param txt
     */
    public void setProductNoTitle(String txt){
        txt_product_no.setText(txt);
    }
    /**
     * 设置产品号
     * @param txt
     */
    public void setProductNoText(int txt){
        tv_product_no.setText(txt);
    }
    /**
     * 设置制令单
     * @param txt
     */
    public void setWoText(String txt){
        tv_wo.setText(txt);
    }
    /**
     * 设置制令单
     * @param txt
     */
    public void setWoTitle(String txt){
        txt_wo.setText(txt);
    }
    /**
     * 设置制令单
     * @param txt
     */
    public void setWoText(int txt){
        tv_wo.setText(txt);
    }

    /**
     * 设置制令单数量
     * @param txt
     */
    public void setWoPlanQtyText(String txt){
        tv_wo_plan_qty.setText(txt);
    }
    /**
     * 设置制令单数量
     * @param txt
     */
    public void setWoPlanQtyTitle(String txt){
        txt_wo_plan_qty.setText(txt);
    }
    /**
     * 设置制令单数量
     * @param txt
     */
    public void setWoPlanQtyText(int txt){
        tv_wo_plan_qty.setText(txt);
    }
    /**
     * 设置当前班次总产量
     * @param txt
     */
    public void setCurShiftTotalQtyText(String txt){
        tv_cur_shift_total_qty.setText(txt);
    }
    /**
     * 设置当前班次总产量
     * @param txt
     */
    public void setCurShiftTotalQtyTitle(String txt){
        txt_cur_shift_total_qty.setText(txt);
    }
    /**
     * 设置当前班次总产量
     * @param txt
     */
    public void setCurShiftTotalQtyText(int txt){
        tv_cur_shift_total_qty.setText(txt);
    }
    /**
     * 设置生产进度
     * @param txt
     */
    public void setProductionScheduleText(String txt){
        tv_production_schedule.setText(txt);
    }
    /**
     * 设置生产进度
     * @param txt
     */
    public void setProductionScheduleTitle(String txt){
        txt_production_schedule.setText(txt);
    }
    /**
     * 设置生产进度
     * @param txt
     */
    public void setProductionScheduleText(int txt){
        tv_production_schedule.setText(txt);
    }

    /**
     * 设置生产进度
     * @param progress
     */
    public void setProgress(int progress){
        tv_progressbar.setProgress(progress);
    }

    /**
     * 设置生产进度
     * @param progressBarData
     */
    public void setProgressBarData(String progressBarData){
        tv_progressbar_data.setText(progressBarData);
    }
    /**
     * 设置生产进度
     * @param progressBarData
     */
    public void setProgressBarData(int progressBarData){
        tv_progressbar_data.setText(progressBarData);
    }


}
