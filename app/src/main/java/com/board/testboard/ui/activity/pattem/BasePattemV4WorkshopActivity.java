package com.board.testboard.ui.activity.pattem;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.topcee.kanban.R;
import com.topcee.kanban.app.MyApplication;
import com.topcee.kanban.bean.MessageAttrBean;
import com.topcee.kanban.http.GetData;
import com.topcee.kanban.presenter.OnRequestListener;
import com.topcee.kanban.ui.activity.base.PermissionActivity;
import com.topcee.kanban.ui.view.base.TitleBarView;
import com.topcee.kanban.ui.view.module.BottomViewPattemWorkshopV4;
import com.topcee.kanban.ui.view.module.MarqueeFeedAlarmView;
import com.topcee.kanban.ui.view.module.MarqueeView;
import com.topcee.kanban.ui.view.module.TitleViewPattem;
import com.topcee.kanban.utils.GsonUtils;
import com.topcee.kanban.utils.LogUtil;
import com.topcee.kanban.utils.ToastUtil;

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
public abstract class BasePattemV4WorkshopActivity extends PermissionActivity {

    private final String TAG = BasePattemV4WorkshopActivity.class.getSimpleName();
    protected TitleBarView menuTitle;
    protected TitleViewPattem smtTitle;
    private LinearLayout llContent;
    protected BottomViewPattemWorkshopV4 bottom;
    protected LinearLayout llWholeBg;
    private MarqueeView viewBulletin;
    private MarqueeFeedAlarmView viewWarnScroll;

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
        viewWarnScroll = findViewById(R.id.view_bottom_view_warn_horizontal_scroll);
        contentView = LayoutInflater.from(this).inflate(getModuleLayoutId(), null);
        llContent.addView(contentView);
        bind = ButterKnife.bind(this,contentView);
        initUI();
        template_code = getResources().getStringArray(R.array.template_code);
        viewWarnScroll.setVisibility(View.GONE);
        viewBulletin.setVisibility(View.GONE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base_pattem_v4_workshop;
    }

    protected abstract int getModuleLayoutId();

    /**
     * 用于初始化UI至少要初始化toolbar，如果还需要别的方法可以自己加
     */
    protected abstract void initUI();

    public void onLongClick(View.OnLongClickListener listener){
        llWholeBg.setOnLongClickListener(listener);
    }
    /**
     * 显示线别标题
     * @param txtRes
     */
    protected void setTitleText(String txtRes){
        smtTitle.setTitleText(txtRes);
    }
    /**
     * 显示线别标题
     * @param txtRes
     */
    protected void setTitleText(int txtRes){
        smtTitle.setTitleText(txtRes);
    }

    /**
     * 设置字体大小
     * @param txtSize
     */
    protected void setTitleTextSize(int txtSize){
        smtTitle.setTitleTextSize(txtSize);
    }
    /**
     * 显示线别标题颜色
     * @param color
     */
    protected void setTitleTextColor(int color){
        smtTitle.setTitleTextColor(color);
    }
    //设置当前时间
    protected void setCurrentDate(String date) {
        smtTitle.setCurrentDate(date);
    }
    /**
     * 设置时间颜色
     * @param color
     */
    protected void setCurrentDateTextColor(int color){
        smtTitle.setCurrentDateTextColor(color);
    }
    protected void setCurrentDateTextSize(int size){
        smtTitle.setCurrentDateTextSize(size);
    }
    /**
     * 显示时间
     * @param currentDateVisibility
     */
    protected void setCurrentDateVisibility(int currentDateVisibility){
        smtTitle.setCurrentDateVisibility(currentDateVisibility);
    }
    //设置班次值
    protected void setShift(String shift) {
        smtTitle.setShift(shift);
    }
    //显示班次字体大小
    protected void setShiftSize(int size){
        smtTitle.setShiftSize(size);
    }
    //显示班次字体颜色
    protected void setShiftTextColor(int color){
        smtTitle.setShiftTextColor(color);
    }
    //显示班次
    public void setShiftVisibility(int shiftVisibility){
        smtTitle.setShiftVisibility(shiftVisibility);
    }
    //设置时段
    protected void setShiftTime(String shiftTime) {
        bottom.setShiftTime(shiftTime);
    }
    //设置时段字体大小
    protected void setShiftTimeSize(int size){
        bottom.setShiftTimeSize(size);
    }

    /**
     * 设置班次时段颜色
     * @param color
     */
    protected void setShiftTimeTextColor(int color){
        bottom.setShiftTimeTextColor(color);
    }
    /**
     * 显示班次
     * @param shiftTimeVisibility
     */
    protected void setShiftTimeVisibility(int shiftTimeVisibility){
        bottom.setShiftTimeVisibility(shiftTimeVisibility);
    }
    protected void setCompanyVisibility(int visibilityCompany) {
        bottom.setCompanyVisibility(visibilityCompany);
    }
    /**
     * 设置公司信息
     * @param companyText
     */
    protected void setCompanyText(String companyText){
        bottom.setCompanyText(companyText);
    }
    /**
     * 设置公司信息颜色
     * @param color
     */
    protected void setCompanyTextColor(int color){
        bottom.setCompanyTextColor(color);
    }
    protected void setCompanyTextSize(int size){
        bottom.setCompanyTextSize(size);
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
    /**
     * 设置是否显示报警信息
     * @param visiblebulletin
     */
    protected void setVisibleViewFeedAlarm(int visiblebulletin){
        viewWarnScroll.setVisibility(visiblebulletin);
    }

    /**
     * 设置页数
     * @param txtRes
     */
    protected void setPageText(String txtRes){
        bottom.setPageText(txtRes);
    }
    /**
     * 设置页数颜色
     * @param color
     */
    protected void setPageColor(int color){
        bottom.setPageColor(color);
    }
    protected void setPageTextSize(int size){
        bottom.setPageTextSize(size);
    }
    /**
     * 显示页数
     * @param PageVisibility
     */
    protected void setPageVisibility(int PageVisibility){
        bottom.setPageVisibility(PageVisibility);
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
            return GetData.getDataByJson(jsonObject.toString(), methodname);

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
