package com.board.testboard.ui.activity.base.module;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.board.testboard.R;
import com.board.testboard.app.MyApplication;
import com.board.testboard.bean.LINE_DISPLAY_TIME;
import com.board.testboard.database.DBUtil;
import com.board.testboard.ui.activity.base.TitlebarBaseActivity;

import butterknife.BindView;

/**
 * 线别展示时长设置
 */
public class LineDisplayTimeSettingActivity extends TitlebarBaseActivity {


    @BindView(R.id.activity_line_display_time_et_display_time_set)
    EditText etDisplayTimeSet;

    private Context context;
    private String lineNo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = LineDisplayTimeSettingActivity.this;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_line_display_time_setting;
    }


    @Override
    protected void initTitleView() {
        super.initTitleView();
        mTitle.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.VISIBLE);
        mTitle.setBtnRight(0, R.string.save);
        mTitle.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LineDisplayTimeSettingActivity.this.finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        DBUtil dbUtil  = new DBUtil(context);
        lineNo = getIntent().getStringExtra("LINE_NO");
        mTitle.setTitleText(String.format(getString(R.string.line_display_time_set),lineNo));
        if(!TextUtils.isEmpty(lineNo)){
            LINE_DISPLAY_TIME lineDisplayTime = dbUtil.getDisPlayByLine(lineNo);
            if(lineDisplayTime!=null){
                etDisplayTimeSet.setText(lineDisplayTime.getDISPLAY_TIME()+ getString(R.string.display_second));
            }else {
                etDisplayTimeSet.setText(MyApplication.nLineDisplayTime + getString(R.string.display_second));
            }
        }
        mTitle.setBtnRightOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(etDisplayTimeSet.getText().toString().trim())){
                    Toast.makeText(context,getString(R.string.display_time_is_null),Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Integer.parseInt(etDisplayTimeSet.getText().toString().trim()) < MyApplication.nLineDisplayTime){
                    Toast.makeText(context,String.format(getString(R.string.display_time_reminder),MyApplication.nLineDisplayTime + ""),Toast.LENGTH_SHORT).show();
                    return;
                }
                DBUtil dbUtil  = new DBUtil(context);
                LINE_DISPLAY_TIME lineDisplayTime = new LINE_DISPLAY_TIME();
                lineDisplayTime.setLINE_NO(lineNo);
                lineDisplayTime.setDISPLAY_TIME(etDisplayTimeSet.getText().toString().trim());
                dbUtil.saveLINE_DISPLAY_TIME(lineDisplayTime);

                Toast.makeText(context,String.format(getString(R.string.display_time_save_success),lineNo),Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
