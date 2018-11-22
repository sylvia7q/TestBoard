package com.board.testboard.ui.view.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.board.testboard.R;


/**
 * [通用底部标题类]
 */

public class BottomBarView extends RelativeLayout {
    private static final String TAG = "Topcee";
    private Context mContext;
    private TextView tv_center;

    public BottomBarView(Context context){
        super(context);
        mContext = context;
        initView();
    }

    public BottomBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_common_bottom_bar, this);
        tv_center = (TextView) findViewById(R.id.title_txt);
    }

    public void setCompanyText(int resId) {
        tv_center.setText(resId);
    }
    public void setCompanyText(String resId) {
        tv_center.setText(resId);
    }
    public void setCompanyTextColor(int color) {
        tv_center.setTextColor(color);
    }
    public void setCompanyTextGravity(int gravity) {
        tv_center.setGravity(gravity);
    }
    public void setCompanyTextSize(float size) {
        tv_center.setTextSize(size);
    }
}
