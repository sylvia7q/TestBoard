package com.board.testboard.ui.view.module;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.board.testboard.R;
import com.board.testboard.ui.view.base.BottomBarView;

/**
 * 模板底部模块
 */
public class BottomView extends LinearLayout {

    private TextView tvDate;
    private TextView tvShift;
    private TextView tvShiftTime;
    private BottomBarView viewCompany;


    private Context context;

    public BottomView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public BottomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }
    /**
     * 初始化标题
     */
    public void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_bottom, this);
        tvDate = view.findViewById(R.id.view_bottom_tv_date);
        tvShift = view.findViewById(R.id.view_bottom_tv_shift_no);
        tvShiftTime = view.findViewById(R.id.view_bottom_tv_shift_time_begin_end);
        viewCompany = view.findViewById(R.id.view_bottom_company);

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

    /**
     * 显示班次时段
     * @param shiftTime
     */
    public void setShiftTime(String shiftTime){
        tvShiftTime.setText(shiftTime);
    }
    /**
     * 设置班次时段颜色
     * @param color
     */
    public void setShiftTimeTextColor(int color){
        tvShiftTime.setTextColor(color);
    }
    /**
     * 显示班次
     * @param shiftTimeVisibility
     */
    public void setShiftTimeVisibility(int shiftTimeVisibility){
        tvShiftTime.setVisibility(shiftTimeVisibility);
    }


    /**
     * 显示公司标识
     * @param companyVisibility
     */
    public void setCompanyVisibility(int companyVisibility){
        viewCompany.setVisibility(companyVisibility);
    }
    /**
     * 设置公司信息
     * @param companyText
     */
    public void setCompanyText(String companyText){
        viewCompany.setCompanyText(companyText);
    }
    /**
     * 设置公司信息颜色
     * @param color
     */
    public void setCompanyTextColor(int color){
        viewCompany.setCompanyTextColor(color);
    }

}
