package com.topcee.kanban.ui.view.module;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.topcee.kanban.R;

/**
 * 员工信息模块
 */
public class EmployeeViewV2 extends LinearLayout {

    private Context context;
    //人员信息(图像、姓名、角色)
    private ImageView iv_employee_01, iv_employee_02;
    private TextView tv_employee_01, tv_employee_02;
    private TextView tv_role_01, tv_role_02;
    private LinearLayout view_employee_ll_bg;


    public EmployeeViewV2(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public EmployeeViewV2(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init(){
        View view = LayoutInflater.from(context).inflate(R.layout.view_employee_v2, this);
        //人员信息(图像、姓名、角色)
        view_employee_ll_bg =  view.findViewById(R.id.view_employee_ll_bg);
        iv_employee_01 =  view.findViewById(R.id.iv_employee_01);
        iv_employee_02 =  view.findViewById(R.id.iv_employee_02);
        tv_employee_01 =  view.findViewById(R.id.tv_employee_01);
        tv_employee_02 =  view.findViewById(R.id.tv_employee_02);
        tv_role_01 =  view.findViewById(R.id.tv_role_01);
        tv_role_02 =  view.findViewById(R.id.tv_role_02);
    }

    /**
     * 设置人员背景色
     * @param resid
     */
    public void setEmployeeBgColor(int resid){
        view_employee_ll_bg.setBackgroundColor(resid);
    }

    /**
     * 设置人员字体颜色
     * @param resid
     */
    public void setEmployeeColor(int resid){
        tv_employee_01.setTextColor(resid);
        tv_employee_02.setTextColor(resid);
    }
    /**
     * 设置岗位字体颜色
     * @param resid
     */
    public void setRoleColor(int resid){
        tv_role_01.setTextColor(resid);
        tv_role_02.setTextColor(resid);
    }
    /**
     * 设置人员头像
     */
    public void setEmplyee01(Bitmap bitmap){
        iv_employee_01.setImageBitmap(bitmap);
    }
    /**
     * 设置人员头像
     */
    public void setEmplyee02(Bitmap bitmap){
        iv_employee_02.setImageBitmap(bitmap);
    }

    public ImageView getIv_employee_01() {
        return iv_employee_01;
    }

    public ImageView getIv_employee_02() {
        return iv_employee_02;
    }

    public TextView getTv_employee_01() {
        return tv_employee_01;
    }

    public TextView getTv_employee_02() {
        return tv_employee_02;
    }

    public TextView getTv_role_01() {
        return tv_role_01;
    }

    public TextView getTv_role_02() {
        return tv_role_02;
    }


}
