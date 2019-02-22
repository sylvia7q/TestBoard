package com.topcee.kanban.ui.view.module;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.topcee.kanban.R;
import com.topcee.kanban.adapter.ViewWorkshopTableAdapter;
import com.topcee.kanban.bean.WorkshopTableBean;

import java.util.List;

/**
 * 备注：用于DOOV
 * 车间看板制令单产出信息表格模块
 */
public class WorkshopTableViewV3 extends LinearLayout {

    private TextView txtNumber;
    private TextView txtLineNo;
    private TextView txtPartNo;
    private TextView txtWo;
    private TextView txtWoPlanQty;
    private TextView txtFinishingRate;
    private TextView txtAchievingRate;
    private TextView txtQtyOutput;
    private TextView txtWorkstatusName;
    private ListView lvData;
    private ViewWorkshopTableAdapter adapter;

    private Context context;

    public WorkshopTableViewV3(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public WorkshopTableViewV3(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    /**
     * 初始化布局
     */
    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_workshop_table_v3, this);
        txtNumber = view.findViewById(R.id.view_workshop_table_txt_number);
        txtLineNo = view.findViewById(R.id.view_workshop_table_txt_line_no);
        txtPartNo = view.findViewById(R.id.view_workshop_table_txt_part_no);
        txtWo = view.findViewById(R.id.view_workshop_table_txt_wo);
        txtWoPlanQty = view.findViewById(R.id.view_workshop_table_txt_wo_plan_qty);
        txtFinishingRate = view.findViewById(R.id.view_workshop_table_txt_finishing_rate);
        txtAchievingRate = view.findViewById(R.id.view_workshop_table_txt_achieving_rate);
        txtQtyOutput = view.findViewById(R.id.view_workshop_table_txt_qty_output);
        txtWorkstatusName = view.findViewById(R.id.view_workshop_table_txt_workstatus_name);
        lvData = view.findViewById(R.id.view_workshop_table_lv_data);
    }
    /**
     * 设置制令单信息颜色
     */
    public void setWorkshopTableInfoTextColor(int color){
        txtNumber.setTextColor(color);
        txtLineNo.setTextColor(color);
        txtPartNo.setTextColor(color);
        txtWo.setTextColor(color);
        txtWoPlanQty.setTextColor(color);
        txtFinishingRate.setTextColor(color);

        txtAchievingRate.setTextColor(color);
        txtQtyOutput.setTextColor(color);
        txtWorkstatusName.setTextColor(color);
    }

    /**
     * 初始化数据
     * @param context
     * @param workshopTableBeans
     */
    public void initTableView(Context context, List<WorkshopTableBean> workshopTableBeans){
        adapter = new ViewWorkshopTableAdapter(context,workshopTableBeans);
        lvData.setAdapter(adapter);
    }

    /**
     * 序号
     * @param txt
     */
    public void setTxtNumber(String txt){
        txtNumber.setText(txt);
    }

    /**
     * 线别
     * @param txt
     */
    public void setTxtLineNo(String txt) {
        txtLineNo.setText(txt);
    }

    /**
     * 产品号
     * @param txt
     */
    public void setTxtPartNo(String txt) {
        txtPartNo.setText(txt);
    }

    /**
     * 制令单
     * @param txt
     */
    public void setTxtWo(String txt) {
        txtWo.setText(txt);
    }

    /**
     * 制令单数量
     * @param txt
     */
    public void setTxtWoPlanQty(String txt) {
        txtWoPlanQty.setText(txt);
    }

    /**
     * 完成率
     * @param txt
     */
    public void setTxtFinishingRate(String txt) {
        txtFinishingRate.setText(txt);
    }

    /**
     * 达成率
     * @param txt
     */
    public void setTxtAchievingRate(String txt) {
        txtAchievingRate.setText(txt);
    }

    /**
     * 生产状态
     * @param txt
     */
    public void setTxtWorkstatusName(String txt) {
        txtWorkstatusName.setText(txt);
    }
}
