package com.board.testboard.ui.activity.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;


import com.board.testboard.R;
import com.board.testboard.app.MyApplication;
import com.board.testboard.bean.UserBean;
import com.board.testboard.database.DBUtil;
import com.board.testboard.http.GetData;
import com.board.testboard.presenter.OnRequestListener;
import com.board.testboard.ui.activity.base.IpSetActivity;
import com.board.testboard.ui.activity.base.PermissionActivity;
import com.board.testboard.ui.activity.base.module.KanBanMCActivity;
import com.board.testboard.ui.activity.base.set.SettingActivity;
import com.board.testboard.ui.view.base.BottomBarView;
import com.board.testboard.ui.view.update.UpdateManager;
import com.board.testboard.utils.AppUtil;
import com.board.testboard.utils.DateUtils;
import com.board.testboard.utils.DialogUtil;
import com.board.testboard.utils.SharedPreferencesUtil;
import com.board.testboard.utils.TextViewUtils;
import com.board.testboard.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录
 */
public class LoginActivity extends PermissionActivity {

    @BindView(R.id.main_activity_et_user_name)
    EditText etUserName;
    @BindView(R.id.main_activity_et_user_pwd)
    EditText etUserPwd;
    @BindView(R.id.main_activity_btn_login)
    Button btnLogin;
    @BindView(R.id.main_activity_btn_setting)
    Button btnSetting;
    @BindView(R.id.main_activity_bottom_bar)
    BottomBarView bottomBar;
    @BindView(R.id.main_activity_cb_save_pwd)
    CheckBox cbSavePwd;
    @BindView(R.id.main_activity_cb_auto_login)
    CheckBox cbAutoLogin;

    private final String TAG = LoginActivity.class.getSimpleName();
    @BindView(R.id.main_activity_tv_mc_code_name)
    TextView tvMcCodeName;
    @BindView(R.id.activity_template_cb_auto_run)
    CheckBox cbAutoRun;
    private Context context;
    private String sServerIp = "";
    private String sSavePwd = "";
    private String sAutoLogin = "";
    private Dialog dialog = null;
    private String sKanBanMCNo = "";

    private boolean isAutoRun = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        context = LoginActivity.this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    /**
     * 判断IP是否为空
     */
    private void checkIp() {
        sServerIp = SharedPreferencesUtil.getValue(context, "sServerIp", "");
        Integer nServerPort = SharedPreferencesUtil.getValue(context, "sServerPort", 0);
        if (TextUtils.isEmpty(sServerIp) || TextUtils.isEmpty(String.valueOf(nServerPort))) {
            ToastUtil.showShortToast(context, getString(R.string.please_set_ip));
            openActivity(context, IpSetActivity.class);
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        enterListener();
        initParams();
        checkIp();
        initUser();
    }

    @OnClick({R.id.main_activity_btn_login, R.id.main_activity_btn_setting, R.id.main_activity_cb_save_pwd, R.id.main_activity_cb_auto_login,R.id.activity_template_cb_auto_run})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.main_activity_btn_login:
                login();
                break;
            case R.id.main_activity_btn_setting:
                openActivity(context, SettingActivity.class);//进入设置界面
                break;
            case R.id.main_activity_cb_save_pwd:
                if (cbSavePwd.isChecked()) {
                    cbSavePwd.setChecked(true);
                    sSavePwd = "Y";
                } else {
                    cbSavePwd.setChecked(false);
                    sSavePwd = "N";
                    cbAutoLogin.setChecked(false);
                    sAutoLogin = "N";
                }
                break;
            case R.id.main_activity_cb_auto_login:
                if (cbAutoLogin.isChecked()) {
                    cbAutoLogin.setChecked(true);
                    sAutoLogin = "Y";
                    cbSavePwd.setChecked(true);
                    sSavePwd = "Y";
                } else {
                    cbAutoLogin.setChecked(false);
                    sAutoLogin = "N";
                }
                break;
            case R.id.activity_template_cb_auto_run:
                if (cbAutoRun.isChecked()) {
                    cbAutoRun.setChecked(true);
                    isAutoRun = true;
                } else {
                    cbAutoRun.setChecked(false);
                    isAutoRun = false;
                }
                SharedPreferencesUtil.saveValue(context, "isAutoRun", isAutoRun);
                break;
        }
    }

    //PDA Enter键监听
    public void enterListener() {
        etUserName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (etUserName.hasFocus() && keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    etUserPwd.requestFocus();
                    return true;
                } else {
                    return false;
                }

            }
        });

        etUserPwd.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (etUserPwd.hasFocus() && keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    btnLogin.requestFocus();
                    return true;
                } else {
                    return false;
                }

            }
        });
    }


    /**
     * 初始化用户
     */
    private void initUser() {
        DBUtil dbUtil = new DBUtil(context);
        UserBean user = dbUtil.getLatestUser();//获取已经登陆过的账户
        if (user != null) {
            etUserName.setText(user.getUserName());
            etUserName.requestFocus();
            etUserName.setSelection(0, etUserName.getText().length());
            etUserPwd.setText(user.getUserPwd());
            if (user.getSavePwd().equals("Y")) {
                cbSavePwd.setChecked(true);
            } else {
                cbSavePwd.setChecked(false);
            }
            if (user.getAutoLogin().equals("Y") && !TextUtils.isEmpty(user.getUserPwd())) {
                cbAutoLogin.setChecked(true);
            } else {
                cbAutoLogin.setChecked(false);
            }

        } else {
            List<UserBean> userBeans = dbUtil.getAllUser();
            if (userBeans != null && userBeans.size() >= 1) {
                UserBean latestUser = userBeans.get(0);
                etUserName.setText(latestUser.getUserName());
                etUserName.requestFocus();
                etUserName.setSelection(0, etUserName.getText().length());
                etUserPwd.setText(latestUser.getUserPwd());
                if (latestUser.getSavePwd().equals("Y")) {
                    cbSavePwd.setChecked(true);
                } else {
                    cbSavePwd.setChecked(false);
                }
                if (latestUser.getAutoLogin().equals("Y")) {
                    cbAutoLogin.setChecked(true);
                } else {
                    cbAutoLogin.setChecked(false);
                }
            } else {
                etUserName.setText("");
                etUserName.requestFocus();
                etUserPwd.setText("");
                cbAutoLogin.setChecked(false);
                cbSavePwd.setChecked(false);
            }
        }
    }


    /**
     * 登陆
     *
     * @return
     */
    private boolean login() {
        String sUserNo = etUserName.getText().toString();
        String sPassword = etUserPwd.getText().toString();
        if (sUserNo.equals("")) {
            ToastUtil.showShortToast(this, getString(R.string.prompt_user_no));
            etUserName.requestFocus();
            return false;
        }
        if (sPassword.equals("")) {
            ToastUtil.showShortToast(this, getString(R.string.prompt_user_pwd));
            etUserPwd.requestFocus();
            return false;
        }
        LoginAsyncTask loginAsyncTask = new LoginAsyncTask();
        loginAsyncTask.execute(sUserNo, sPassword);
        return true;
    }

    /**
     * 保存用户信息
     *
     * @param userNo
     * @param pwd
     */
    private void saveUserInfo(String userNo, String pwd) {
        UserBean user = new UserBean();
        user.setUserId(userNo);
        user.setUserName(userNo);
        user.setLoginTime(DateUtils.getTime());
        user.setCurAccount("Y");
        user.setSavePwd(sSavePwd);
        user.setAutoLogin(sAutoLogin);
        if (sSavePwd.equals("Y")) {
            user.setUserPwd(pwd);
        } else {
            user.setUserPwd("");
        }
        DBUtil dbUtil = new DBUtil(context);
        dbUtil.saveUser(user);
        SharedPreferencesUtil.saveValue(context, "sUserNo", userNo);
        SharedPreferencesUtil.saveValue(context, "sUserName", userNo);
        SharedPreferencesUtil.saveValue(context, "sCurrentUserNo", userNo);//验证权限账号
        MyApplication.sUserNo = userNo;
        MyApplication.sUserName = userNo;
        MyApplication.sCurrentUserNo = userNo;
    }

    /**
     * 登陆成功
     */
    private void loginSuccess() {
        checkKanBanMcCode();//判断设备代码是否为空
    }

    /**
     * 判断设备代码是否为空
     */
    private void checkKanBanMcCode() {
        sKanBanMCNo = SharedPreferencesUtil.getValue(context, "KANBAN_MC_NO", "");
        if (!TextUtils.isEmpty(sKanBanMCNo)) {//判断设备代码是否为空，如果为空，则重新选择，如果不为空，则重新刷新一下看板设备代码数据
            updateKanBanMCCode(context, sKanBanMCNo);
        } else {
            openActivity(context, KanBanMCActivity.class);
        }
    }


    //登陆-数据处理
    public class LoginAsyncTask extends AsyncTask<String, Void, String> {
        String sUserNo;
        String sPassword;

        @Override
        protected void onPreExecute() {
            TextViewUtils.setClickableShow(btnLogin, false);
            TextViewUtils.setClickableShow(btnSetting, false);
            if (dialog != null) {
                dialog.dismiss();
            }
            dialog = DialogUtil.showDialog(context, getString(R.string.logining), getString(R.string.loading_data), false, false);
        }

        @Override
        protected String doInBackground(String... params) {
            String methodname = "GetLoginUserInfo";
            JSONObject jsonObject = new JSONObject();
            sUserNo = params[0];
            sPassword = params[1];
            try {

                jsonObject.put("sMesUser", MyApplication.sMesUser);
                jsonObject.put("sFactoryNo", MyApplication.sFactoryNo);
                jsonObject.put("sLanguage", MyApplication.sLanguage);
                jsonObject.put("sUserNo", sUserNo);
                jsonObject.put("sClientIp", MyApplication.sClientIp);
                jsonObject.put("sPassWord", sPassword);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return GetData.getDataByJson(context, jsonObject.toString(), methodname);
        }

        @Override
        protected void onPostExecute(String string) {
            dialog.dismiss();
//            string = "[{\"status\": \"OK\",\"msg\": \"OK\"}]";
            TextViewUtils.setClickableShow(btnLogin, true);
            TextViewUtils.setClickableShow(btnSetting, true);
            if (string.equals("") || string.equals(null)) {
                ToastUtil.showShortToast(context, getString(R.string.interface_returns_failed));
                return;
            } else {
                try {
                    JSONArray jsonArray = new JSONArray(string);
                    JSONObject item = jsonArray.getJSONObject(0);
                    String sStatus = item.getString("status");
                    String sMsg = item.getString("msg");
                    if (sStatus.equalsIgnoreCase("OK")) {
                        saveUserInfo(sUserNo, sPassword);
                        loginSuccess(); //登录成功
                    } else if (sStatus.equalsIgnoreCase("ERROR")) {
                        ToastUtil.showShortToast(context, sMsg);
                    } else {
                        ToastUtil.showShortToast(context, sMsg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtil.showShortToast(context, e.getMessage().toString());
                }
            }
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
    private void initParams() {
        sServerIp = SharedPreferencesUtil.getValue(context, "sServerIp", "");
        Integer nServerPort = SharedPreferencesUtil.getValue(context, "sServerPort", 0);
        if (TextUtils.isEmpty(sServerIp) || TextUtils.isEmpty(String.valueOf(nServerPort))) {
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

        String mcName = SharedPreferencesUtil.getValue(context, "MC_NAME", "");
        tvMcCodeName.setText(mcName);
        isAutoRun = SharedPreferencesUtil.getValue(context, "isAutoRun", false);
        if (isAutoRun) {
            cbAutoRun.setChecked(true);
        } else {
            cbAutoRun.setChecked(false);
        }
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
    protected void onPause() {
        super.onPause();
        String mcName = SharedPreferencesUtil.getValue(context, "MC_NAME", "");
        tvMcCodeName.setText(mcName);
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
