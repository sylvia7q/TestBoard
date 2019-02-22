package com.topcee.kanban.ui.activity.pattem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;


import com.topcee.kanban.R;
import com.topcee.kanban.ui.activity.base.PermissionActivity;
import com.topcee.kanban.ui.view.base.TitleBarView;
import com.topcee.kanban.ui.view.module.BottomView;
import com.topcee.kanban.ui.view.module.MarqueeView;
import com.topcee.kanban.ui.view.module.TitleViewV3Line;
import com.topcee.kanban.utils.LogUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * DOOV模板基础类
 */
public abstract class BasePattemV3WorkshopV4LineActivity extends PermissionActivity {

    private final String TAG = BasePattemActivity.class.getSimpleName();
    protected TitleBarView menuTitle;
    protected TitleViewV3Line smtTitle;
    private LinearLayout llContent;
    protected BottomView bottom;
    protected LinearLayout llWholeBg;
    private View contentView;
    private Unbinder bind;
    protected String template_code[];//当前模板内码


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e(TAG,"BasePattemActivity--------onCreate");
        llWholeBg = findViewById(R.id.activity_template_ll_whole_bg_custom);
        menuTitle = findViewById(R.id.activity_template_menu_title_custom);
        smtTitle = findViewById(R.id.activity_template_title_custom);
        llContent = findViewById(R.id.activity_template_module_content_custom);
        bottom = findViewById(R.id.activity_template_bottom_custom);
        contentView = LayoutInflater.from(this).inflate(getModuleLayoutId(), null);
        llContent.addView(contentView);
        bind = ButterKnife.bind(this,contentView);
        initTitleView();
        initUI();
        template_code = getResources().getStringArray(R.array.template_code);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base_pattem_v3_workshop_v4_line;
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
     * 显示线别标题
     * @param txtRes
     */
    public void setTitleText(String txtRes){
        smtTitle.setTitleText(txtRes);
    }
    /**
     * 显示线别标题
     * @param txtRes
     */
    public void setTitleText(int txtRes){
        smtTitle.setTitleText(txtRes);
    }
    /**
     * 显示线别标题颜色
     * @param color
     */
    public void setTitleTextColor(int color){
        smtTitle.setTitleTextColor(color);
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

    protected void setCompanyVisibility(int visibilityCompany) {
        bottom.setCompanyVisibility(visibilityCompany);
    }
    //设置当前时间
    protected void setCurrentDate(String date) {
        bottom.setCurrentDate(date);
    }
    /**
     * 设置时间颜色
     * @param color
     */
    protected void setCurrentDateTextColor(int color){
        bottom.setCurrentDateTextColor(color);
    }
    protected void setCurrentDateTextSize(int size){
        bottom.setCurrentDateTextSize(size);
    }
    /**
     * 显示时间
     * @param currentDateVisibility
     */
    protected void setCurrentDateVisibility(int currentDateVisibility){
        bottom.setCurrentDateVisibility(currentDateVisibility);
    }
    //设置班次值
    protected void setShift(String shift) {
        bottom.setShift(shift);
    }
    //显示班次字体大小
    protected void setShiftSize(int size){
        bottom.setShiftSize(size);
    }
    //显示班次字体颜色
    protected void setShiftTextColor(int color){
        bottom.setShiftTextColor(color);
    }
    //显示班次
    public void setShiftVisibility(int shiftVisibility){
        bottom.setShiftVisibility(shiftVisibility);
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
     * 设置是否显示公告信息
     * @param visiblebulletin
     */
    public void setVisiblebulletin(int visiblebulletin){
        smtTitle.setVisiblebulletin(visiblebulletin);
    }
    /**
     * 显示滚动条背景颜色
     * @param color
     */
    public void setBulletinBgColor(int color){
        smtTitle.setBulletinBgColor(color);
    }
    /**
     * 设置公告信息
     * @param
     * @return
     */
    public MarqueeView getViewBulletin(){
        return  smtTitle.getViewBulletin();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
    }
}
