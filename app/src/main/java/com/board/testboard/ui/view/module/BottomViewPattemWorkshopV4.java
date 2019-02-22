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
public class BottomViewPattemWorkshopV4 extends LinearLayout {

    private TextView tvShiftTime;
    private BottomBarView viewCompany;
    private TextView tvPage;
    private Context context;

    public BottomViewPattemWorkshopV4(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public BottomViewPattemWorkshopV4(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }
    /**
     * 初始化标题
     */
    public void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_bottom_pattem_workshop_v4, this);
        tvShiftTime = view.findViewById(R.id.view_bottom_pattem_v4_tv_shift_time_begin_end);
        viewCompany = view.findViewById(R.id.view_bottom_pattem_v4_company);
        tvPage = view.findViewById(R.id.view_bottom_pattem_v4_tv_show_paging_data);

    }

    /**
     * 设置页数
     * @param txtRes
     */
    public void setPageText(String txtRes){
        tvPage.setText(txtRes);
    }
    /**
     * 设置页数颜色
     * @param color
     */
    public void setPageColor(int color){
        tvPage.setTextColor(color);
    }
    public void setPageTextSize(int size){
        tvPage.setTextSize(size);
    }
    /**
     * 显示页数
     * @param PageVisibility
     */
    public void setPageVisibility(int PageVisibility){
        tvPage.setVisibility(PageVisibility);
    }

    /**
     * 显示班次时段
     * @param shiftTime
     */
    public void setShiftTime(String shiftTime){
        tvShiftTime.setText(shiftTime);
    }

    /**
     * 设置页数字体大小
     * @param size
     */
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
