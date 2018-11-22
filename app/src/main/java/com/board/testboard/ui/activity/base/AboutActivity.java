package com.board.testboard.ui.activity.base;

import android.os.Bundle;
import android.widget.TextView;


import com.board.testboard.R;
import com.board.testboard.utils.AppUtil;

import butterknife.BindView;


/**
 * [设置-关于]
 */

public class AboutActivity extends TitlebarBaseActivity {

    @BindView(R.id.tvVersoinName)
    TextView tvVersoinName;
    @BindView(R.id.tvUrl)
    TextView tvUrl;
    @BindView(R.id.tvTel)
    TextView tvTel;
    @BindView(R.id.tvCopyright)
    TextView tvCopyright;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initTitleView() {
        super.initTitleView();
        mTitle.setTitleText(R.string.about);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvVersoinName.setText(getString(R.string.about_version) + AppUtil.getVersionName(this));
    }
}
