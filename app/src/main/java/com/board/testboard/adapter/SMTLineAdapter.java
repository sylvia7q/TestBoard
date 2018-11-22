package com.board.testboard.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;


import com.board.testboard.R;
import com.board.testboard.bean.SMT_LINE_SETTING;

import java.util.List;

public class SMTLineAdapter extends ArrayAdapter {

    private Context context;
    private List<SMT_LINE_SETTING> list;
    private int mResourceId;

    public SMTLineAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.context = context;
        this.list = objects;
        this.mResourceId = resource;
    }

    @Override
    public boolean hasStableIds() {
        return true;
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
        ViewHolder holder;
        if(view == null){
            view = LayoutInflater.from(context).inflate(mResourceId,null);
            holder = new ViewHolder();
            holder.cbLineName = view.findViewById(R.id.item_list_line);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        SMT_LINE_SETTING moreLine  = list.get(position);
        if(moreLine!=null){
            holder.cbLineName.setText(moreLine.getLINE_NAME());
        }

        return view;
    }
    static final class ViewHolder{
        private CheckedTextView cbLineName;

    }
}
