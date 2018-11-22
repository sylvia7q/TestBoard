package com.board.testboard.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;


import com.board.testboard.R;
import com.board.testboard.app.MyApplication;
import com.board.testboard.bean.KANBAN_MC;
import com.board.testboard.utils.SharedPreferencesUtil;

import java.util.List;

public class KanBanMCAdapter extends BaseAdapter {
    
    private Context context;
    private List<KANBAN_MC> list;

    public KanBanMCAdapter(Context context, List<KANBAN_MC> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_list_kan_ban_code_select,null);
            holder = new ViewHolder();
            holder.tvName = view.findViewById(R.id.item_list_kan_ban_code_select_chs);
            holder.rbSelect = view.findViewById(R.id.item_list_kan_ban_code_select_name_rbutton);
            holder.llSelect = view.findViewById(R.id.item_list_kan_ban_code_ll_select);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        KANBAN_MC codeBean = list.get(position);
        if(codeBean!=null){
            holder.tvName.setText(codeBean.getKANBAN_MC_NO());
            String sKanBanMCNo = SharedPreferencesUtil.getValue(context,"KANBAN_MC_NO","");
            if(!TextUtils.isEmpty(sKanBanMCNo)){
                if(codeBean.getKANBAN_MC_NO().equals(sKanBanMCNo)){
                    MyApplication.kanBanNoStates.put(index, true);
                    holder.rbSelect.setChecked(true);
                }else {
                    for (int i = 0; i < getCount(); i++) {
                        MyApplication.kanBanNoStates.put(i, false);
                    }
                    holder.rbSelect.setChecked(false);
                }

            }
            //上面是点击后设置状态，但是也是需要设置显示样式,通过判断状态设置显示的样式
            if (MyApplication.kanBanNoStates.get(position) == null || MyApplication.kanBanNoStates.get(position) == false) {  //true说明没有被选中
                holder.rbSelect.setChecked(false);
            } else {
                holder.rbSelect.setChecked(true);
            }
            notifyDataSetChanged();
        }

        return view;
    }
    static final class ViewHolder{
        private TextView tvName;
        private RadioButton rbSelect;
        private LinearLayout llSelect;

    }
}
