package com.board.testboard.ui.activity.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;


import com.board.testboard.R;
import com.board.testboard.app.MyApplication;
import com.board.testboard.bean.UserBean;
import com.board.testboard.database.DBUtil;
import com.board.testboard.http.CheckNetwork;
import com.board.testboard.presenter.OnRequestListener;
import com.board.testboard.ui.activity.base.BaseActivity;
import com.board.testboard.ui.activity.base.IpSetActivity;
import com.board.testboard.ui.activity.base.module.KanBanMCActivity;
import com.board.testboard.ui.view.update.UpdateManager;
import com.board.testboard.utils.AppUtil;
import com.board.testboard.utils.SharedPreferencesUtil;
import com.board.testboard.utils.ToastUtil;

import java.util.List;


/**
 * 主界面
 */
public class MainActivity extends BaseActivity {

    private Context context;
    private String sServerIp = "";
    private String kanbanMcNo = "";
    private final String TAG = MainActivity.this.getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = MainActivity.this;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initParams();
        check();//判断IP是否为空,判断是否为自动登陆
    }

    /**
     * 判断IP是否为空,判断是否为自动登陆
     */
    private void check(){
        /**
         * 判断IP是否为空
         */
        sServerIp = SharedPreferencesUtil.getValue(context, "sServerIp", "");
        Integer nServerPort = SharedPreferencesUtil.getValue(context, "sServerPort", 0);
        if (TextUtils.isEmpty(sServerIp)|| TextUtils.isEmpty(String.valueOf(nServerPort))) {
            ToastUtil.showShortToast(context, getString(R.string.please_set_ip));
            openActivity(context,IpSetActivity.class);
            return;
        }
        /**
         * 判断是否为自动登陆
         */
        DBUtil dbUtil = new DBUtil(context);
        UserBean user = dbUtil.getLatestUser();//获取已经登陆过的账户
        if(user != null){
            if(CheckNetwork.isWifiConnected(context)){
                checkKanBanMcCode();
            }else {
                openActivity(context, LoginActivity.class);
            }
        }else {
            openActivity(context, LoginActivity.class);
        }

    }


    /**
     * 判断设备代码是否为空
     */
    private void checkKanBanMcCode(){
        kanbanMcNo = SharedPreferencesUtil.getValue(context, "KANBAN_MC_NO", "");
        if(!TextUtils.isEmpty(kanbanMcNo)){
            updateKanBanMCCode(context,kanbanMcNo);
        }else {
            openActivity(context,KanBanMCActivity.class);
        }
    }
    //更新看板代码
    public void updateKanBanMCCode(Context mContext, final String sKanBanMCNo) {
        getKanBanMCCode(mContext, sKanBanMCNo, new OnRequestListener() {
            @Override
            public void onSuccess(List list) {
            }

            @Override
            public void onFail(String result) {
                ToastUtil.showShortToast(context,result);
                openActivity(context,KanBanMCActivity.class);
            }
        });
    }




    /**
     * 初始化参数
     */
    private void initParams(){
        sServerIp = SharedPreferencesUtil.getValue(context, "sServerIp", "");
        Integer nServerPort = SharedPreferencesUtil.getValue(context, "sServerPort", 0);
        if (TextUtils.isEmpty(sServerIp)|| TextUtils.isEmpty(String.valueOf(nServerPort))) {
            ToastUtil.showShortToast(context, getString(R.string.please_set_ip));
            Intent intent = new Intent(context, IpSetActivity.class);
            startActivity(intent);
            return;
        }

        //调用WebService接口赋值(后续新增APK只需要修改此处应对数据)
        String sFactoryNo = SharedPreferencesUtil.getValue(context, "sFactoryNo", "0"); //工厂代码：0
        String sLanguage = SharedPreferencesUtil.getValue(context, "sLanguage", "S"); //语言
        String sClientIp = AppUtil.getIp(); //IP地址

        String sServiceBese = "http://" + sServerIp + ":" + nServerPort; //IP+端口
        String sWeService = "/KANBAN01.WEBSERVICE/TimsKanbanService.asmx";
        String sUrl = sServiceBese + sWeService; //WebService地址
        String sApkName = "TIMS_KANBAN.apk"; //APK名称
        String sDownloadUrl = sServiceBese + "/KANBAN01.WEBSERVICE/UPDATE/" + sApkName; //APK下载地址
        saveValue(sFactoryNo,"0");
        saveValue(sLanguage,"S");
        saveValue(sClientIp,sClientIp);
        SharedPreferencesUtil.saveValue(context, "sServiceBase", sServiceBese); //IP+端口
        SharedPreferencesUtil.saveValue(context, "sWeService", sWeService);
        SharedPreferencesUtil.saveValue(context, "sUrl", sUrl); //WebService地址
        SharedPreferencesUtil.saveValue(context, "sDownloadUrl", sDownloadUrl); //APK下载地址
        String sUserNo = SharedPreferencesUtil.getValue(context, "sUserNo","" );
        String sUserName = SharedPreferencesUtil.getValue(context, "sUserName", "");
        String sCurrentUserNo = SharedPreferencesUtil.getValue(context, "sCurrentUserNo", "");//验证权限账号
        //系统全局变量赋值
        MyApplication.sFactoryNo = sFactoryNo;
        MyApplication.sLanguage = sLanguage;
        MyApplication.sClientIp = sClientIp;
        MyApplication.sUserNo = sUserNo;
        MyApplication.sUserName = sUserName;
        MyApplication.sCurrentUserNo = sCurrentUserNo;

        MyApplication.sServiceBese = sServiceBese; //IP+端口
        MyApplication.sUrl = sUrl; //WebService地址
        MyApplication.sDownloadUrl = sDownloadUrl; //APK下载地址
        MyApplication.sApkName = sApkName; //APK名称

        AutoUpdateManager();
    }
    /**
     * 如果为空，则设置初始值
     * @param key
     * @param values
     */
    private void saveValue(String key,String values){
        if(TextUtils.isEmpty(key)){
            SharedPreferencesUtil.saveValue(context, key, values);
        }
    }
    //检查app是否需要更新
    private void AutoUpdateManager() {
        UpdateManager mUpdateManager = new UpdateManager(context);
        mUpdateManager.checkUpdateInfo(context);//获取APP版本(本地与服务器)比对，是否需要更新
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
