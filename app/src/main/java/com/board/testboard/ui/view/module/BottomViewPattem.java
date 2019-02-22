package com.topcee.kanban.ui.view.module;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.topcee.kanban.R;
import com.topcee.kanban.ui.view.base.BottomBarView;

/**
 * 模板底部模块
 */
public class BottomViewPattem extends LinearLayout {

    private TextView tvShiftTime;
    private BottomBarView viewCompany;

    private Context context;

    public BottomViewPattem(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public BottomViewPattem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }
    /**
     * 初始化标题
     */
    public void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_bottom_pattem, this);
        tvShiftTime = view.findViewById(R.id.view_bottom_pattem_tv_shift_time_begin_end);
        viewCompany = view.findViewById(R.id.view_bottom_pattem_company);

    }

    /**
     * 显示班次时段
     * @param shiftTime
     */
    public void setShiftTime(String shiftTime){
        tvShiftTime.setText(shiftTime);
    }
    public void setShiftTimeSize(int size){
        tvShiftTime.setTextSize(size);
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
    public void setCompanyTextSize(int size){
        viewCompany.setCompanyTextSize(size);
    }

}
