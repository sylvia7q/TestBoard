package com.board.testboard.ui.view.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button; 
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.board.testboard.R;
import com.board.testboard.utils.SystemMethod;


/**
 * [通用顶部标题类]
 */

public class TitleBarView extends RelativeLayout {
    private static final String TAG = "Topcee";
    private Context mContext;
    private Button btnLeft; //左边But
    private Button btnRight; //右边But
    private Button btn_titleLeft; //左边title
    private Button btn_titleRight; //右边title
    private TextView tv_center; //中间title
    private LinearLayout common_constact;

    public TitleBarView(Context context) {
        super(context);
        mContext=context;
        initView();
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        initView();
    }

    public Button getBtnLeft() {
        return btnLeft;
    }

    public Button getBtnRight() {
        return btnRight;
    }

    private void initView(){
        LayoutInflater.from(mContext).inflate(R.layout.view_common_title_bar, this);
        btnLeft=(Button) findViewById(R.id.title_btn_left);
        btnRight=(Button) findViewById(R.id.title_btn_right);
        btn_titleLeft=(Button) findViewById(R.id.constact_group);
        btn_titleRight=(Button) findViewById(R.id.constact_all);
        tv_center=(TextView) findViewById(R.id.title_txt);
        common_constact=(LinearLayout) findViewById(R.id.common_constact);

    }

    public void setCommonTitle(int LeftVisibility,int centerVisibility,int center1Visibilter,int rightVisibility){
        btnLeft.setVisibility(LeftVisibility);
        tv_center.setVisibility(centerVisibility);
        common_constact.setVisibility(center1Visibilter);
        btnRight.setVisibility(rightVisibility);
    }

    public void setBtnLeft(int icon,int txtRes){
        Drawable img=mContext.getResources().getDrawable(icon);
        int height= SystemMethod.dip2px(mContext, 25);
        int width=img.getIntrinsicWidth()*height/img.getIntrinsicHeight();
        img.setBounds(0, 0, width, height);
        btnLeft.setText(txtRes);
        btnLeft.setCompoundDrawables(img, null, null, null);
        btnLeft.setBackgroundResource(R.drawable.btn_title_selector);
    }

    public void setBtnLeft(int txtRes){
        btnLeft.setText(txtRes);
    }
    public void setBtnLeft(String txtRes){
        btnLeft.setText(txtRes);
    }
    public void setBtnLeftTextColor(int color){
        btnLeft.setTextColor(color);
    }

    //获取焦点-btnLeft
    public void setBtnLeftRequestFocus(){
        btnLeft.requestFocus();
    }

    public void setBtnRight(int icon,int txtRes) {
        if (txtRes != 0)
            btnRight.setText(txtRes);
        if (icon == 0) {
            btnRight.setHeight(40);
            btnRight.setWidth(80);
        } else {
            Drawable img = mContext.getResources().getDrawable(icon);
            int height = SystemMethod.dip2px(mContext, 25);
            int width = img.getIntrinsicWidth() * height / img.getIntrinsicHeight();
            img.setBounds(0, 0, width, height);
            btnRight.setCompoundDrawables(img, null, null, null);
        }

        btnRight.setBackgroundResource(R.drawable.btn_title_selector);
    }

    public void setBtnRight(int txtRes) {
        btnRight.setText(txtRes);
    }
    public void setBtnRightTextColor(int color) {
        btnRight.setTextColor(color);
    }

    //获取焦点-btnRight
    public void setBtnRightRequestFocus(){
        btnRight.requestFocus();
    }

    public void setTitleLeft(int resId){
        btn_titleLeft.setText(resId);
    }

    public void setTitleRight(int resId){
        btn_titleRight.setText(resId);
    }

    public void setTitleText(int txtRes){
        tv_center.setText(txtRes);
    }
    public void setTitleTextColor(int color){
        tv_center.setTextColor(color);
    }
    public void setTitleText(CharSequence txtRes){
        tv_center.setText(txtRes);
    }

    public void setBtnLeftOnclickListener(OnClickListener listener){
        btnLeft.setOnClickListener(listener);
    }

    public void setBtnRightOnclickListener(OnClickListener listener){
        btnRight.setOnClickListener(listener);
    }
}
