package com.topcee.kanban.ui.view.module;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.topcee.kanban.R;

/**
 * 模板标题模块
 */
public class TitleViewPattem extends LinearLayout {

    private TextView tvShift;//班次
    private TextView tvLine;//线别
    private TextView tvDate;//时间

    private Context context;

    public TitleViewPattem(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public TitleViewPattem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }
    /**
     * 初始化标题
     */
    public void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_title_pattem, this);
        tvShift = view.findViewById(R.id.view_title_pattem_tv_shift_no);
        tvLine = view.findViewById(R.id.view_title_pattem_tv_line);
        tvDate = view.findViewById(R.id.view_title_pattem_tv_date);
    }

    /**
     * 显示线别标题
     * @param txtRes
     */
    public void setTitleText(String txtRes){
        tvLine.setText(txtRes);
    }
    /**
     * 显示线别标题
     * @param txtRes
     */
    public void setTitleText(int txtRes){
        tvLine.setText(txtRes);
    }

    /**
     * 设置字体大小
     * @param txtSize
     */
    public void setTitleTextSize(int txtSize){
        tvLine.setTextSize(txtSize);
    }
    /**
     * 显示线别标题颜色
     * @param color
     */
    public void setTitleTextColor(int color){
        tvLine.setTextColor(color);
    }
    /**
     * 显示时间
     * @param txtRes
     */
    public void setCurrentDate(String txtRes){
        tvDate.setText(txtRes);
    }
    /**
     * 设置时间颜色
     * @param color
     */
    public void setCurrentDateTextColor(int color){
        tvDate.setTextColor(color);
    }
    public void setCurrentDateTextSize(int size){
        tvDate.setTextSize(size);
    }
    /**
     * 显示时间
     * @param currentDateVisibility
     */
    public void setCurrentDateVisibility(int currentDateVisibility){
        tvDate.setVisibility(currentDateVisibility);
    }
    /**
     * 显示班次
     * @param shift
     */
    public void setShift(String shift){
        tvShift.setText(shift);
    }
    public void setShiftSize(int size){
        tvShift.setTextSize(size);
    }
    /**
     * 设置班次颜色
     * @param color
     */
    public void setShiftTextColor(int color){
        tvShift.setTextColor(color);
    }
    /**
     * 显示班次
     * @param shiftVisibility
     */
    public void setShiftVisibility(int shiftVisibility){
        tvShift.setVisibility(shiftVisibility);
    }
}
