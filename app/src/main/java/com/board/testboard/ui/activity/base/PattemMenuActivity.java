package com.board.testboard.ui.activity.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


import com.board.testboard.R;
import com.board.testboard.adapter.KanBanUserPattemAdapter;
import com.board.testboard.bean.KANBAN_MC_PATTEM;
import com.board.testboard.bean.KANBAN_USER_PATTEM;
import com.board.testboard.database.DBUtil;
import com.board.testboard.presenter.OnResultListener;
import com.board.testboard.ui.activity.base.set.SettingActivity;
import com.board.testboard.utils.LogUtil;
import com.board.testboard.utils.SharedPreferencesUtil;
import com.board.testboard.utils.ToastUtil;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 模板菜单
 */
public class PattemMenuActivity extends TitlebarBaseActivity {

    @BindView(R.id.activity_template_lv_template_data)
    ListView lvTemplateData;
    @BindView(R.id.activity_template_btn_show_template)
    Button btnShowTemplate;

    private  final String TAG = PattemMenuActivity.this.getClass().getName();
    private Context context;
    private String sKanBanMCNo = "";
    private Dialog dialog = null;
    private List<KANBAN_USER_PATTEM> userPattemList;
    private List<HashMap<String, String>> listUserTemplateName;


    private List<KANBAN_MC_PATTEM> mcPattemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = PattemMenuActivity.this;
    }


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_pattem_menu;
    }

    @Override
    protected void initTitleView() {
        super.initTitleView();
        mTitle.setCommonTitle(View.GONE, View.VISIBLE, View.GONE, View.VISIBLE);
        mTitle.setTitleText(R.string.menu_setting);
        mTitle.setBtnRight(R.drawable.icon_gear,0);
        mTitle.setBtnRightOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(context, SettingActivity.class);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        sKanBanMCNo = SharedPreferencesUtil.getValue(getBaseContext(), "KANBAN_MC_NO", "");
        //获取用户设备列表
        getKanBanUserPattem();
        //自动模式
        btnShowTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBUtil dbUtil = new DBUtil(context);
                List<KANBAN_MC_PATTEM> showMcPattems = dbUtil.getAllMcPattem();//每次进入自动模式都打开第一个需要展示的模板
                if(showMcPattems!=null && showMcPattems.size()>=1){
                    KANBAN_MC_PATTEM mcPattems = showMcPattems.get(0);
                    String className = getClassName(getTemplateActivity(),mcPattems.getINTERNAL_CODE());
                    LogUtil.e(TAG,"className = " + className);
                    openActivity(context,className,"isAutoPattem","Y");
                }
//                if(!TextUtils.isEmpty(PATTEM_NO)){//如果存有当前展示模板的名字，则继续展示该模板，如果没有，则按展示模板数据顺序展示模板
//                    KANBAN_MC_PATTEM kanbanMcPattem = dbUtil.getMcPattemByNo(PATTEM_NO);
//                    String className = getClassName(getTemplateActivity(),kanbanMcPattem.getINTERNAL_CODE());
//                    openActivity(context,className);
//                }else {
//                    List<KANBAN_MC_PATTEM> showMcPattems = dbUtil.getAllMcPattem();
//                    if(showMcPattems!=null && showMcPattems.size()>=1){
//                        KANBAN_MC_PATTEM mcPattems = showMcPattems.get(0);
//                        String className = getClassName(getTemplateActivity(),mcPattems.getINTERNAL_CODE());
//                        LogUtil.e(TAG,"className = " + className);
//                        openActivity(context,className);
//                    }
//                }

            }
        });


        //手动模式
        lvTemplateData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                KANBAN_USER_PATTEM userPattems = userPattemList.get(position);
                String className = getClassName(getTemplateActivity(),userPattems.getINTERNAL_CODE());
                if(!TextUtils.isEmpty(className)){
                    openActivity(context,className,"isAutoPattem","N");
                }
            }
        });
    }

    //获取用户模板信息
    public void getKanBanUserPattem() {
        getKanBanUserPattem(context, sKanBanMCNo, new OnResultListener() {
            @Override
            public void onSuccess() {
                DBUtil dbUtil = new DBUtil(context);
                userPattemList = dbUtil.getAllUserPattem();
                if(userPattemList!=null && userPattemList.size()>=1){
                    KanBanUserPattemAdapter adapter = new KanBanUserPattemAdapter(context,userPattemList);
                    lvTemplateData.setAdapter(adapter);
                    lvTemplateData.setDividerHeight(0);
                }
            }

            @Override
            public void onFail(String result) {
                ToastUtil.showShortToast(context,result);
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
}
