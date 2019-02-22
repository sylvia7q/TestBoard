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
import com.topcee.kanban.adapter.ViewWorkshopTableV2Adapter;
import com.topcee.kanban.bean.WorkshopTableBean;

import java.util.List;

/**
 * 备注：用于TCL
 * 车间看板制令单产出信息表格模块
 */
public class WorkshopTableViewV2 extends LinearLayout {

    private TextView txtNumber;
    private TextView txtLineNo;
    private TextView txtCustom;
    private TextView txtWo;
    private TextView txtBatch;
    private TextView txtCommissioningNumber;
    private TextView txtAoiGoodRate;
    private TextView txtProductionState;
    private ListView lvData;
    private ViewWorkshopTableV2Adapter adapter;

    private Context context;

    public WorkshopTableViewV2(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public WorkshopTableViewV2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    /**
     * 初始化布局
     */
    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_workshop_table_v2, this);
        txtNumber = view.findViewById(R.id.view_workshop_table_txt_number);
        txtLineNo = view.findViewById(R.id.view_workshop_table_txt_line_no);
        txtCustom = view.findViewById(R.id.view_workshop_table_txt_customer);
        txtWo = view.findViewById(R.id.view_workshop_table_txt_wo);
        txtBatch = view.findViewById(R.id.view_workshop_table_txt_batch);
        txtCommissioningNumber = view.findViewById(R.id.view_workshop_table_txt_commissioning_number);
        txtAoiGoodRate = view.findViewById(R.id.view_workshop_table_txt_aoi_good_rate);
        txtProductionState = view.findViewById(R.id.view_workshop_table_txt_production_state);
        lvData = view.findViewById(R.id.view_workshop_table_lv_data);
    }
    /**
     * 设置制令单信息颜色
     */
    public void setWorkshopTableInfoTextColor(int color){
        txtNumber.setTextColor(color);
        txtLineNo.setTextColor(color);
        txtCustom.setTextColor(color);
        txtWo.setTextColor(color);
        txtBatch.setTextColor(color);
        txtCommissioningNumber.setTextColor(color);

        txtAoiGoodRate.setTextColor(color);
        txtProductionState.setTextColor(color);
    }

    /**
     * 初始化数据
     * @param context
     * @param workshopTableBeans
     */
    public void initTableView(Context context, List<WorkshopTableBean> workshopTableBeans){
        adapter = new ViewWorkshopTableV2Adapter(context,workshopTableBeans);
        lvData.setAdapter(adapter);
    }
    public void refresh(){
        adapter.notifyDataSetChanged();
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
        txtCustom.setText(txt);
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
    public void setTxtBatch(String txt) {
        txtBatch.setText(txt);
    }

    /**
     * 完成率
     * @param txt
     */
    public void setTxtCommissioningNumber(String txt) {
        txtCommissioningNumber.setText(txt);
    }

    /**
     * 达成率
     * @param txt
     */
    public void setTxtAoiGoodRate(String txt) {
        txtAoiGoodRate.setText(txt);
    }
}
