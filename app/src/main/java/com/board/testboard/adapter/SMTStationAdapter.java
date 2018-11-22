package com.board.testboard.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.board.testboard.R;
import com.board.testboard.bean.Station;

import java.util.List;

public class SMTStationAdapter extends BaseAdapter {

    private Context context;
    private List<Station> list;
    public OnItemClickListener onItemClickListener;
    public interface OnItemClickListener {
        void select(int id, boolean isChecked);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public SMTStationAdapter(Context context, List<Station> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final int index = position;
        ViewHolder holder = null;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_list_line_xmultiple_choice,null);
            holder = new ViewHolder();
            holder.ll_item = view.findViewById(R.id.ll_item);
            holder.cbStationName = view.findViewById(R.id.item_list_cb_line);
            holder.ivStationEdit = view.findViewById(R.id.item_list_cb_line_edit);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        holder.ivStationEdit.setVisibility(View.GONE);
        Station station  = list.get(position);
        if(station!=null){
            holder.cbStationName.setText(station.getSTATION_NAME());
            if(!TextUtils.isEmpty(station.getSELECTED())){
                if(station.getSELECTED().equals("Y")){
                    holder.cbStationName.setChecked(true);
                }else {
                    holder.cbStationName.setChecked(false);
                }
            }else {
                holder.cbStationName.setChecked(false);
            }

            holder.ll_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener!=null){
                        onItemClickListener.select(index,v.isClickable());
                    }
                    Log.e("long","onItemClickListener.select(index,isChecked);");
                }
            });
            holder.cbStationName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(onItemClickListener!=null){
                        onItemClickListener.select(index,isChecked);
                    }
                    Log.e("long","onItemClickListener.select(index,isChecked);");
                }
            });

        }

        return view;
    }
    static final class ViewHolder{
        private LinearLayout ll_item;
        private CheckBox cbStationName;
        private ImageView ivStationEdit;

    }
}
