package com.board.testboard.ui.activity.base.module;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;


import com.board.testboard.R;
import com.board.testboard.adapter.KanBanMCAdapter;
import com.board.testboard.app.MyApplication;
import com.board.testboard.bean.KANBAN_MC;
import com.board.testboard.presenter.OnRequestListener;
import com.board.testboard.ui.activity.base.TitlebarBaseActivity;
import com.board.testboard.utils.SharedPreferencesUtil;
import com.board.testboard.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;

/**
 * 获取设备代码
 */
public class KanBanMCActivity extends TitlebarBaseActivity {

    @BindView(R.id.view_base_lv_data)
    ListView lvData;

    private Context context;
    private Dialog dialog = null;
    private KanBanMCAdapter adapter;
    private List<KANBAN_MC> codeBeanList;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = KanBanMCActivity.this;
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_base_setting;
    }

    /**
     * 初始化标题
     */
    @Override
    public void initTitleView() {
        super.initTitleView();
        mTitle.setTitleText(R.string.board_mc_code_select_set);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getKanBanMCCode(context, "", new OnRequestListener() {
            @Override
            public void onSuccess(List list) {
                codeBeanList = list;
                adapter = new KanBanMCAdapter(context, codeBeanList);
                lvData.setAdapter(adapter);
                lvData.setDividerHeight(0);
            }

            @Override
            public void onFail(String result) {
                ToastUtil.showShortToast(context,result);
            }
        });
        lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String bmId = codeBeanList.get(position).getBM_ID();
                String kanbanMcNo = codeBeanList.get(position).getKANBAN_MC_NO();
                String mcName = codeBeanList.get(position).getMC_NAME();
                String factoryNo = codeBeanList.get(position).getFACTORY_NO();
                String hearTableTime = codeBeanList.get(position).getHEARTBEAT_TIME();
                String refreshTime = codeBeanList.get(position).getREFRESH_TIME();
                String sortid = codeBeanList.get(position).getSORTID();
                String status = codeBeanList.get(position).getSTATUS();
                String note = codeBeanList.get(position).getNOTE();
                String displayLanguage = codeBeanList.get(position).getDISPLAY_LANGUAGE();
                String createUserId = codeBeanList.get(position).getCREATE_USERID();
                String createDate = codeBeanList.get(position).getCREATE_DATE();
                String createIp = codeBeanList.get(position).getCREATE_IP();
                String updateUserId = codeBeanList.get(position).getUPDATE_USERID();
                String updateDate = codeBeanList.get(position).getUPDATE_DATE();
                String updateIp = codeBeanList.get(position).getUPDATE_IP();
                MyApplication.sKanBanMCNo = kanbanMcNo;
                //保存数据至本地文件中
                SharedPreferencesUtil.saveValue(getBaseContext(), "BM_ID", bmId);
                SharedPreferencesUtil.saveValue(getBaseContext(), "KANBAN_MC_NO", kanbanMcNo);
                SharedPreferencesUtil.saveValue(getBaseContext(), "FACTORY_NO", factoryNo);
                SharedPreferencesUtil.saveValue(getBaseContext(), "MC_NAME", mcName);
                SharedPreferencesUtil.saveValue(getBaseContext(), "HEARTBEAT_TIME", hearTableTime);
                SharedPreferencesUtil.saveValue(getBaseContext(), "REFRESH_TIME", refreshTime);
                SharedPreferencesUtil.saveValue(getBaseContext(), "SORTID", sortid);
                SharedPreferencesUtil.saveValue(getBaseContext(), "STATUS", status);
                SharedPreferencesUtil.saveValue(getBaseContext(), "NOTE", note);
                SharedPreferencesUtil.saveValue(getBaseContext(), "DISPLAY_LANGUAGE", displayLanguage);
                SharedPreferencesUtil.saveValue(getBaseContext(), "CREATE_USERID", createUserId);
                SharedPreferencesUtil.saveValue(getBaseContext(), "CREATE_DATE", createDate);
                SharedPreferencesUtil.saveValue(getBaseContext(), "CREATE_IP", createIp);
                SharedPreferencesUtil.saveValue(getBaseContext(), "UPDATE_USERID", updateUserId);
                SharedPreferencesUtil.saveValue(getBaseContext(), "UPDATE_DATE", updateDate);
                SharedPreferencesUtil.saveValue(getBaseContext(), "UPDATE_IP", updateIp);
                ToastUtil.showShortToast(context, getString(R.string.select_the_kanban_mc_code) + " [" + kanbanMcNo + "]!");
                MyApplication.isBack = true;
                MyApplication.sLanguage = displayLanguage;
                SharedPreferencesUtil.saveValue(context, "sLanguage", MyApplication.sLanguage); //语言
                for (int i = 0; i < lvData.getCount(); i++) {
                    RadioButton button = lvData.getChildAt(i).findViewById(R.id.item_list_kan_ban_code_select_name_rbutton);
                    button.setChecked(false);
                }
                RadioButton button = lvData.getChildAt(position).findViewById(R.id.item_list_kan_ban_code_select_name_rbutton);
                button.setChecked(true);
                finish();
            }
        });
        mTitle.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (codeBeanList != null && codeBeanList.size() >= 1) {
                    String sKanBanMCNo = SharedPreferencesUtil.getValue(getBaseContext(), "KANBAN_MC_NO", "");
                    if (TextUtils.isEmpty(sKanBanMCNo)) {
                        ToastUtil.showShortToast(context, getString(R.string.no_selected_kanban_mc_code));
                        return;
                    }
                    MyApplication.isBack = true;
                    finish();
                }else {
                    back();
                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
