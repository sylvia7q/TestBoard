package com.topcee.kanban.ui.view.module;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.topcee.kanban.R;


/**
 * 模板标题模块
 */
public class TitleViewV3Line extends LinearLayout {

    private ImageView ivCustomerLogo;
    private MarqueeView viewBulletin;
    private TextView tvLine;

    private Context context;

    public TitleViewV3Line(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public TitleViewV3Line(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }
    /**
     * 初始化标题
     */
    public void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_title_v3_line, this);
        ivCustomerLogo = view.findViewById(R.id.view_title_iv_customer_logo);
        viewBulletin = view.findViewById(R.id.view_title_tv_bulletin_horizontal_scroll);
        tvLine = view.findViewById(R.id.view_title_tv_line);
    }

    /**
     * 显示线别标题
     * @param txtRes
     */
    public void setTitleText(String txtRes){
        tvLine.setText(txtRes);
    }
    /**
     * 显示线别标题
     * @param txtRes
     */
    public void setTitleText(int txtRes){
        tvLine.setText(txtRes);
    }
    /**
     * 显示线别标题颜色
     * @param color
     */
    public void setTitleTextColor(int color){
        tvLine.setTextColor(color);
    }
    /**
     * 设置公司logo
     * @param icon
     */
    public void setCompanyLogo(int icon){
        ivCustomerLogo.setBackgroundResource(icon);
    }

    /**
     * 设置是否显示公告信息
     * @param visiblebulletin
     */
    public void setVisiblebulletin(int visiblebulletin){
        viewBulletin.setVisibility(visiblebulletin);
    }
    /**
     * 显示滚动条背景颜色
     * @param color
     */
    public void setBulletinBgColor(int color){
        viewBulletin.setBackgroundColor(color);
    }
    /**
     * 设置公告信息
     * @param
     * @return
     */
    public MarqueeView getViewBulletin(){
        return  viewBulletin;
    }
}
