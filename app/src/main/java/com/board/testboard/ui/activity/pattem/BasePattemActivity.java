package com.board.testboard.ui.activity.pattem;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;


import com.board.testboard.R;
import com.board.testboard.app.MyApplication;
import com.board.testboard.bean.MessageAttrBean;
import com.board.testboard.http.GetData;
import com.board.testboard.presenter.OnRequestListener;
import com.board.testboard.ui.activity.base.PermissionActivity;
import com.board.testboard.ui.view.base.TitleBarView;
import com.board.testboard.ui.view.module.BottomView;
import com.board.testboard.ui.view.module.MarqueeView;
import com.board.testboard.ui.view.module.TitleView;
import com.board.testboard.utils.GsonUtils;
import com.board.testboard.utils.LogUtil;
import com.board.testboard.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 模板基础类
 */
public abstract class BasePattemActivity extends PermissionActivity {

    private final String TAG = BasePattemActivity.class.getSimpleName();
    protected TitleBarView menuTitle;
    protected TitleView smtTitle;
    private LinearLayout llContent;
    protected BottomView bottom;
    protected LinearLayout llWholeBg;
    private MarqueeView viewBulletin;

    private View contentView;
    private Unbinder bind;


    protected String template_code[];//当前模板内码
    private GetKanBanMsgAttrTask getKanBanMsgAttrTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e(TAG,"BasePattemActivity--------onCreate");
        llWholeBg = findViewById(R.id.activity_template_ll_whole_bg);
        menuTitle = findViewById(R.id.activity_template_menu_title);
        smtTitle = findViewById(R.id.activity_template_title);
        llContent = findViewById(R.id.activity_template_module_content);
        bottom = findViewById(R.id.activity_template_bottom);
        viewBulletin = findViewById(R.id.view_bottom_tv_bulletin_horizontal_scroll);
        contentView = LayoutInflater.from(this).inflate(getModuleLayoutId(), null);
        llContent.addView(contentView);
        bind = ButterKnife.bind(this,contentView);
        initTitleView();
        initUI();
        template_code = getResources().getStringArray(R.array.template_code);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base_pattem;
    }

    protected abstract int getModuleLayoutId();

    /**
     * 用于初始化UI至少要初始化toolbar，如果还需要别的方法可以自己加
     */
    protected abstract void initUI();

    /**
     * 初始化标题
     */
    protected void initTitleView() {
        smtTitle.setCompanyLogo(R.mipmap.doov_logo);
    }
    public void onLongClick(View.OnLongClickListener listener){
        llWholeBg.setOnLongClickListener(listener);
    }
    /**
     * 初始化底部信息
     */
    protected BottomView initBottomView(int visibilityRefreshDate, int visibilityCompany) {
        bottom.setCurrentDateVisibility(visibilityRefreshDate);//最后刷新时间或者当前时间
        setCompanyVisibility(visibilityCompany);
        return bottom;
    }

    protected void setDate(String date) {
        bottom.setCurrentDate(date);
    }

    protected void setShift(String shift) {
        bottom.setShift(shift);
    }

    protected void setShiftTime(String shiftTime) {
        bottom.setShiftTime(shiftTime);
    }

    protected void setCompanyVisibility(int visibilityCompany) {
        bottom.setCompanyVisibility(visibilityCompany);
    }
    protected void setTemplateWholeBg(int color) {
        llWholeBg.setBackgroundColor(color);
    }

    public LinearLayout getLlWholeBg() {
        return llWholeBg;
    }

    /**
     * 根据参数觉得决定是否显示UI
     *
     * @param params
     * @return
     */
    protected boolean showUI(String params) {
        if (params.equals("Y")) {
            return true;
        } else if (params.equals("N")) {
            return false;
        } else {
            return false;
        }
    }


    /**
     * 设置是否显示公告信息
     * @param visiblebulletin
     */
    protected void setVisiblebulletin(int visiblebulletin){
        viewBulletin.setVisibility(visiblebulletin);
    }
    /**
     * 显示滚动条背景颜色
     * @param color
     */
    protected void setBulletinBgColor(int color){
        viewBulletin.setBackgroundColor(color);
    }
    /**
     * 设置公告信息
     * @param
     * @return
     */
    protected MarqueeView getViewBulletin(){
        return  viewBulletin;
    }
    //获取看板代码
    public void getKanBanMsgAttr(Context context, String sLanguage, OnRequestListener listener) {
        getKanBanMsgAttrTask = new GetKanBanMsgAttrTask(context,sLanguage,listener);
        getKanBanMsgAttrTask.execute();
    }

    //获取看板设备代码-数据处理
    public class GetKanBanMsgAttrTask extends AsyncTask<String, Void, String> {

        private Context context;
        private String sLanguage;
        private OnRequestListener listener;
        private List<MessageAttrBean> msgList;

        public GetKanBanMsgAttrTask(Context context, String sLanguage, OnRequestListener listener) {
            this.context = context;
            this.sLanguage = sLanguage;
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            if(isCancelled()){
                return null;
            }
            String methodname = "GetMessageRGB";
            JSONObject jsonObject = new JSONObject();
            try {

                jsonObject.put("sMesUser", MyApplication.sMesUser);
                jsonObject.put("sFactoryNo", MyApplication.sFactoryNo);
                jsonObject.put("sLanguage", sLanguage);
                jsonObject.put("sUserNo", MyApplication.sUserNo);
                jsonObject.put("sClientIp", MyApplication.sClientIp);
            } catch (JSONException ex) {
                ex.printStackTrace();
                listener.onFail(ex.getMessage());
            }
            return GetData.getDataByJson(context,jsonObject.toString(), methodname);

        }

        @Override
        protected void onPostExecute(String json) {
            if(isCancelled()){
                return ;
            }
            if (null == json || json.equals("")) {
                ToastUtil.showShortToast(context,getString(R.string.interface_returns_failed));
                return;
            }
            try {
                JSONArray jsonArray = new JSONArray(json);
                JSONObject item = jsonArray.getJSONObject(0);
                String sStatus = item.getString("status");
                String sMsg = item.getString("msg");
                msgList = new ArrayList<>();
                if (sStatus.equalsIgnoreCase("OK")) {
                    String sData = item.getString("data");
                    JSONArray itemData = new JSONArray(sData);
                    msgList = GsonUtils.getObjects(itemData.toString(),MessageAttrBean.class);
                } else {
                    listener.onFail(sMsg);
                }
                if (msgList != null && msgList.size()>=1) {
                    listener.onSuccess(msgList);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                listener.onFail(e.getMessage());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTask(getKanBanMsgAttrTask);
        if (bind != null) {
            bind.unbind();
        }

    }

}
