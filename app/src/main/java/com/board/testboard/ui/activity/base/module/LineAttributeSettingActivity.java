package com.board.testboard.ui.activity.base.module;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;


import com.board.testboard.R;
import com.board.testboard.ui.activity.base.TitlebarBaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 每条线的属性设置
 */
public class LineAttributeSettingActivity extends TitlebarBaseActivity {

    @BindView(R.id.rl_smt_station_set_layout)
    RelativeLayout rl_smt_station_set_layout;

    @BindView(R.id.rl_display_time_set)
    RelativeLayout rl_display_time_set;

    private Context context;
    private String sLineNo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = LineAttributeSettingActivity.this;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_smtline_attribute_setting;
    }

    @Override
    protected void initTitleView() {
        super.initTitleView();

    }
    @Override
    protected void onResume() {
        super.onResume();
        sLineNo = getIntent().getStringExtra("LINE_NO");
        mTitle.setTitleText(String.format(getString(R.string.line_attribute_set),sLineNo));
    }

    @OnClick({R.id.rl_smt_station_set_layout, R.id.rl_display_time_set})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_smt_station_set_layout:
                openActivity(context,LineStationSettingActivity.class,"LINE_NO",sLineNo);
                break;
            case R.id.rl_display_time_set:
                openActivity(context,LineDisplayTimeSettingActivity.class,"LINE_NO",sLineNo);
                break;
        }
    }
}
