package com.board.testboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.board.testboard.R;
import com.board.testboard.bean.WorkshopTableBean;

import java.util.List;

/**
 * [SMT车间看板ListView数据]
 */
public class ViewWorkshopTableAdapter extends BaseAdapter {

    private List<WorkshopTableBean> smtTotalBoardList;

    private Context context; // 上下文

    private LayoutInflater inflater = null; // 用来导入布局

    public ViewWorkshopTableAdapter(Context context, List<WorkshopTableBean> list){
        this.context = context;
        this.smtTotalBoardList = list;
    }

    @Override
    public int getCount() {
        return smtTotalBoardList.size(); //返回数组的长度
    }

    @Override
    public Object getItem(int i) {
        return smtTotalBoardList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        WorkshopTableBean workshopTableBean = this.smtTotalBoardList.get(i % this.smtTotalBoardList.size());
        //获取一个在数据集中指定索引的视图来显示数据
        MyViewHolder holder = null;
        //如果缓存convertView为空，则需要创建View
        if (view == null) {
            holder=new MyViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_list_view_workshop_table, null);
            holder.tv_number = view.findViewById(R.id.tv_number);
            holder.tv_line_no = view.findViewById(R.id.tv_line_no);
            holder.tv_part_no = view.findViewById(R.id.tv_part_no);
            holder.tv_wo = view.findViewById(R.id.tv_wo);
            holder.tv_wo_qty_plan = view.findViewById(R.id.tv_wo_qty_plan);
            holder.tv_finishing_rate =  view.findViewById(R.id.tv_finishing_rate);
            holder.tv_achieving_rate = view.findViewById(R.id.tv_achieving_rate);
//            holder.tv_qty_output = view.findViewById(R.id.tv_qty_output);
            holder.tv_workstatus_name = view.findViewById(R.id.tv_workstatus_name);
            holder.ll_bg = view.findViewById(R.id.ll_show);

            view.setTag(holder);
        } else {
            holder=(MyViewHolder)view.getTag();
        }
        //显示数据
        holder.tv_number.setText(workshopTableBean.getNumber());
        holder.tv_line_no.setText(workshopTableBean.getLineNo());
        holder.tv_part_no.setText(workshopTableBean.getProductNo());
        holder.tv_wo.setText(workshopTableBean.getWo());
        holder.tv_wo_qty_plan.setText(workshopTableBean.getWoPlanQty());
        holder.tv_finishing_rate.setText(workshopTableBean.getFinishingRate() + "%");
        holder.tv_achieving_rate.setText(workshopTableBean.getAchievingRate() + "%");
//        holder.tv_qty_output.setText(workshopTableBean.getQtyOutput());
        holder.tv_workstatus_name.setText(workshopTableBean.getWorkStatusName());
        if(i%2==0){
            holder.ll_bg.setBackgroundResource(R.color.total_board_item_single_bg);
        }else {
            holder.ll_bg.setBackgroundResource(R.color.total_board_item_double_bg);
        }
        return view;
    }

    public static class MyViewHolder {
        private TextView tv_number; //序号
        private TextView tv_line_no; //线别
        private TextView tv_part_no; //物料名称
        private TextView tv_wo; //制令单
        private TextView tv_wo_qty_plan; //制令单计划数量
        private TextView tv_finishing_rate;  //计划量
        private TextView tv_achieving_rate;  //完成率
        private TextView tv_qty_output;  //线体总产量
        private TextView tv_workstatus_name; //线别生产状态名称
        private LinearLayout ll_bg;
    }
}
