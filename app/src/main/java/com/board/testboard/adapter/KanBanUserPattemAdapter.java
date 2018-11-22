package com.board.testboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;


import com.board.testboard.R;
import com.board.testboard.bean.KANBAN_USER_PATTEM;

import java.util.List;

public class KanBanUserPattemAdapter extends BaseAdapter {

    private Context context;
    private List<KANBAN_USER_PATTEM> userPattemList;

    public KanBanUserPattemAdapter(Context context, List<KANBAN_USER_PATTEM> userPattemList) {
        this.context = context;
        this.userPattemList = userPattemList;
    }

    @Override
    public int getCount() {
        return userPattemList.size();
    }

    @Override
    public Object getItem(int position) {
        return userPattemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder = null;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_list_kan_ban_code_select,null);
            holder = new ViewHolder();
            holder.tvName = view.findViewById(R.id.item_list_kan_ban_code_select_chs);
            holder.rbSelect = view.findViewById(R.id.item_list_kan_ban_code_select_name_rbutton);
            holder.llSelect = view.findViewById(R.id.item_list_kan_ban_code_ll_select);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        holder.rbSelect.setVisibility(View.GONE);
        KANBAN_USER_PATTEM userPattem = userPattemList.get(position);
        if(userPattem!=null){
            String pattemName = userPattemList.get(position).getPATTEM_NAME();
            holder.tvName.setText(pattemName);
        }

        return view;
    }
    static final class ViewHolder{
        private TextView tvName;
        private RadioButton rbSelect;
        private LinearLayout llSelect;

    }
}
