package com.board.testboard.ui.view.module;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.board.testboard.R;

import butterknife.BindView;

/**
 * 模板标题模块
 */
public class TitleView extends LinearLayout {

    private ImageView ivCustomerLogo;
    private TextView tvLine;

    private Context context;

    public TitleView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public TitleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }
    /**
     * 初始化标题
     */
    public void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_title, this);
        ivCustomerLogo = view.findViewById(R.id.view_title_iv_customer_logo);
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


}
