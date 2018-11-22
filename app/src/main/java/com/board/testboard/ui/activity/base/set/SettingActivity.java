package com.board.testboard.ui.activity.base.set;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;


import com.board.testboard.R;
import com.board.testboard.bean.UserBean;
import com.board.testboard.database.DBUtil;
import com.board.testboard.ui.activity.base.AboutActivity;
import com.board.testboard.ui.activity.base.IpSetActivity;
import com.board.testboard.ui.activity.base.TitlebarBaseActivity;
import com.board.testboard.ui.activity.base.module.KanBanMCActivity;
import com.board.testboard.ui.activity.main.LoginActivity;
import com.board.testboard.utils.DateUtils;
import com.board.testboard.utils.DialogUtil;
import com.board.testboard.utils.SharedPreferencesUtil;

import butterknife.BindView;

/**
 * [设置]
 */

public class SettingActivity extends TitlebarBaseActivity {

    @BindView(R.id.rl_server_ip_set_layout)
    RelativeLayout rl_server_ip_set_layout; //IP设置

    @BindView(R.id.rl_board_mc_code_select_layout)
    RelativeLayout rl_board_mc_code_select_layout; //看板设备代码选择

    @BindView(R.id.rl_about_layout)
    RelativeLayout rl_about_layout; //关于

    @BindView(R.id.rl_switch_user_layout)//切换用户
            RelativeLayout rl_switch_user_layout;

    @BindView(R.id.rl_exit_layout)//退出
            RelativeLayout rl_exit_layout;

    @BindView(R.id.rl_cur_time_set)//当前时间设置
            RelativeLayout rl_cur_time_set;

    @BindView(R.id.cb_right_cur_time_set)
    CheckBox cb_right_cur_time_set;//当前时间设置

    private String getCurTimeByServer = "Y";

    private Context context;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = SettingActivity.this;

    }

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_setting;
    }


    //标题信息
    @Override
    protected void initTitleView() {
        super.initTitleView();
        mTitle.setTitleText(R.string.setting);
        mTitle.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setListenerView(); //监听事件
    }

    //监听事件
    private void setListenerView() {
        //IP设置
        rl_server_ip_set_layout.setVisibility(View.VISIBLE);
        rl_server_ip_set_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, IpSetActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        //看板代码选择
        rl_board_mc_code_select_layout.setVisibility(View.VISIBLE);
        rl_board_mc_code_select_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, KanBanMCActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        //关于
        rl_about_layout.setVisibility(View.VISIBLE);
        rl_about_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AboutActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        //切换账户
        rl_switch_user_layout.setVisibility(View.VISIBLE);
        rl_switch_user_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserInfo();
                Intent intent = new Intent(context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        //退出
        rl_exit_layout.setVisibility(View.VISIBLE);
        rl_exit_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sMsg = getString(R.string.are_you_sure_exit);
                dialog = DialogUtil.showDialog(context, getString(R.string.prompt), sMsg, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //退出
                        killAll();
                        dialog.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });
        //当前时间设置
        rl_cur_time_set.setVisibility(View.VISIBLE);
        String sGetCurTimeByServer = SharedPreferencesUtil.getValue(context,"getCurTimeByServer",getCurTimeByServer);
        if(sGetCurTimeByServer.equals("Y")){
            cb_right_cur_time_set.setChecked(true);
        }else {
            cb_right_cur_time_set.setChecked(false);
        }
        cb_right_cur_time_set.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    getCurTimeByServer = "Y";
                }else {
                    getCurTimeByServer = "N";
                }
            }
        });
    }

    /**
     * 更新用户信息
     */
    private void updateUserInfo() {
        DBUtil dbUtil = new DBUtil(context);
        UserBean user = dbUtil.getLatestUser();//获取已经登陆过的账户
        if (user != null) {
            user.setUserId(user.getUserId());
            user.setUserName(user.getUserName());
            user.setLoginTime(DateUtils.getTime());
            user.setCurAccount("N");
            user.setSavePwd(user.getSavePwd());
            user.setAutoLogin("N");
            user.setUserPwd("");
            dbUtil.saveUser(user);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferencesUtil.saveValue(context,"getCurTimeByServer",getCurTimeByServer);
        if (dialog != null) {
            dialog.dismiss();
        }
    }

}
