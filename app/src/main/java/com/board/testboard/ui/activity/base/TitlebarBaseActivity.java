package com.board.testboard.ui.activity.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;


import com.board.testboard.R;
import com.board.testboard.ui.activity.base.set.SettingActivity;
import com.board.testboard.ui.view.base.TitleBarView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 标题栏基类
 */
public abstract class TitlebarBaseActivity extends PermissionActivity {

    protected TitleBarView mTitle;
    private LinearLayout llContent;

    private Unbinder bind;
    private View contentView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = findViewById(R.id.activity_title);
        llContent = findViewById(R.id.ll_content);
        contentView = LayoutInflater.from(this).inflate(getContentLayoutId(), null);
        llContent.addView(contentView);
        bind = ButterKnife.bind(this,contentView);
        initTitleView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_titlebar_base;
    }
    protected abstract int getContentLayoutId();

    protected void initTitleView(){
        setBtnLeft();
        setBtnLeftOnclickListener();
        setCommonTitle();
    }

    protected void setBtnLeft(){
        mTitle.setBtnLeft(R.drawable.icon_back, R.string.btn_back);
    }
    protected void setBtnLeftOnclickListener(){
        mTitle.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void setCommonTitle(){
        mTitle.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE);
    }
    /**
     * 返回
     */
    protected void back(){
        openActivity(this, SettingActivity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
    }

}
