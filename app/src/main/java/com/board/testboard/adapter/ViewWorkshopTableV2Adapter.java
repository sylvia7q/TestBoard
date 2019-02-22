package com.board.testboard.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.topcee.kanban.R;
import com.topcee.kanban.bean.WorkshopTableBean;
import com.topcee.kanban.utils.AppUtil;
import com.topcee.kanban.utils.SharedPreferencesUtil;

import java.util.List;

/**
 * [SMT车间看板ListView数据]
 */
public class ViewWorkshopTableV2Adapter extends BaseAdapter {

    private List<WorkshopTableBean> items;
    private Context context;
    private int mScreenHeight;

    public ViewWorkshopTableV2Adapter(Context context, List<WorkshopTableBean> items){
        this.context = context;
        this.items = items;
    }

    public int getCount() {
        return items.size();//返回数组的长度
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        MyViewHolder holder = null;
        //如果缓存convertView为空，则需要创建View
        if (view == null) {
            holder = new MyViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_list_view_workshop_table_v2, null);
            holder.tv_number = view.findViewById(R.id.tv_number);
            holder.tv_line_no = view.findViewById(R.id.tv_line_no);
            holder.tv_customer_no = view.findViewById(R.id.tv_custom);
            holder.tv_wo = view.findViewById(R.id.tv_wo);
            holder.tv_batch = view.findViewById(R.id.tv_batch);//批量
            holder.tv_commissioning_number =  view.findViewById(R.id.tv_commissioning_number);//投产数
            holder.tv_aoi_rate = view.findViewById(R.id.tv_aoi_good_rate);//良率
            holder.tv_production_state = view.findViewById(R.id.tv_production_state);//生产状态
            holder.ll_list = view.findViewById(R.id.ll_list);
            //获取屏幕高度
//            DisplayMetrics dm = new DisplayMetrics();
//            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//            wm.getDefaultDisplay().getMetrics(dm);
//            mScreenHeight = dm.heightPixels;//屏幕高度
//            //设置Item高度
//            LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            //屏幕高度减去其它控件高度155，除以7就是每个Item的高度
//            linearParams.height = (mScreenHeight - 300)/7;//Item高度
//            holder.ll_list.setLayoutParams(linearParams);
            //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
            view.setTag(holder);
        }else{
            holder = (MyViewHolder) view.getTag();
        }

        WorkshopTableBean item = items.get(position);
        if(item!=null){
            //显示数据
            holder.tv_number.setText(item.getNumber());
            if(!TextUtils.isEmpty(item.getLineNo())){
                holder.tv_line_no.setText(item.getLineNo());
            }
            if(!TextUtils.isEmpty(item.getCustomerNo())){
                holder.tv_customer_no.setText(item.getCustomerNo());
            }

            //如果生产状态是空值，则设置为未生产,如果不为空，则取相应状态的背景颜色
            if(TextUtils.isEmpty(item.getWorkStatusNo())){
                holder.tv_production_state.setText(context.getString(R.string.smt_total_no_product));
                holder.tv_production_state.setTextColor(context.getResources().getColor(R.color.smt_other));
            }else {
                holder.tv_production_state.setText(item.getWorkStatusName());
                int color = AppUtil.parseColor(item.getRgb());
                holder.tv_production_state.setTextColor(color);

                //生产状态背景色
                if(TextUtils.isEmpty(item.getWo())){//如果制令单信息为空则不显示
                    holder.tv_wo.setText("");
                    holder.tv_batch.setText("");
                    holder.tv_commissioning_number.setText("");
                    holder.tv_aoi_rate.setText("");
                    holder.tv_production_state.setText(context.getString(R.string.smt_total_no_product));
                    holder.tv_production_state.setTextColor(context.getResources().getColor(R.color.smt_other));
                }else {
                    holder.tv_wo.setText(item.getWo());
                    if(!TextUtils.isEmpty(item.getWoPlanQty())){
                        holder.tv_batch.setText(item.getWoPlanQty());
                    }
                    if(!TextUtils.isEmpty(item.getQtyOutput())){
                        holder.tv_commissioning_number.setText(item.getQtyOutput());
                    }
                    if(TextUtils.isEmpty(item.getGoodRate())){
                        holder.tv_aoi_rate.setText("0%");
                    }else {
                        holder.tv_aoi_rate.setText(item.getGoodRate() + "%");
                        String sAoiRate = item.getGoodRate(); //AOI良率
                        double dAoiRate = Double.parseDouble(sAoiRate);
                        double dAoiRateSet = 0d;
                        int nFeedAlarmWarnValues = SharedPreferencesUtil.getValue(context, "nFeedAlarmWarnValues", 0);
                        dAoiRateSet = nFeedAlarmWarnValues;
                        //AOI良率背景色
                        if(dAoiRateSet != 0){
                            //变字体背景
                            if(dAoiRate < dAoiRateSet){
                                setTextColor(item.getGoodRate(),holder.tv_aoi_rate,context.getResources().getColor(R.color.red));
                            }
                        }
                    }
                }
            }

            if(position % 2 == 0){
                holder.ll_list.setBackgroundResource(R.color.total_board_item_single_bg);
            }else {
                holder.ll_list.setBackgroundResource(R.color.total_board_item_double_bg);
            }
        }

        return view;
    }
    private void setTextColor(String values, TextView textView, int resid){
        if(!TextUtils.isEmpty(values)){
            textView.setTextColor(resid);
        }
    }

    public static class MyViewHolder{
        private TextView tv_number;
        private TextView tv_line_no;
        private TextView tv_customer_no;
        private TextView tv_wo;
        private TextView tv_batch;
        private TextView tv_commissioning_number;
        private TextView tv_aoi_rate;
        private TextView tv_production_state;
        private LinearLayout ll_list;
    }
}
