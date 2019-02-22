package com.topcee.kanban.bean;

import android.graphics.Bitmap;

/**
 * 人员岗位信息  
 */
public class EmployeeNewBean {

    private String USER_NO; //员工号
    private String USER_NAME; //员工姓名
    private String EMPLOYEE_NO; //
    private String MAIL_ADDRESS; //邮件地址
    private String DEPT_NO; //部门
    private String DEPT_NAME; //部门名称
    private String DUTIES_NO; //岗位代码
    private String DUTY_NAME; //岗位名称
    private Bitmap PHOTO; //员工图片

    public String getUSER_NO() {
        return USER_NO;
    }

    public void setUSER_NO(String USER_NO) {
        this.USER_NO = USER_NO;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public String getEMPLOYEE_NO() {
        return EMPLOYEE_NO;
    }

    public void setEMPLOYEE_NO(String EMPLOYEE_NO) {
        this.EMPLOYEE_NO = EMPLOYEE_NO;
    }

    public String getMAIL_ADDRESS() {
        return MAIL_ADDRESS;
    }

    public void setMAIL_ADDRESS(String MAIL_ADDRESS) {
        this.MAIL_ADDRESS = MAIL_ADDRESS;
    }

    public String getDEPT_NO() {
        return DEPT_NO;
    }

    public void setDEPT_NO(String DEPT_NO) {
        this.DEPT_NO = DEPT_NO;
    }

    public String getDEPT_NAME() {
        return DEPT_NAME;
    }

    public void setDEPT_NAME(String DEPT_NAME) {
        this.DEPT_NAME = DEPT_NAME;
    }

    public String getDUTIES_NO() {
        return DUTIES_NO;
    }

    public void setDUTIES_NO(String DUTIES_NO) {
        this.DUTIES_NO = DUTIES_NO;
    }

    public String getDUTY_NAME() {
        return DUTY_NAME;
    }

    public void setDUTY_NAME(String DUTY_NAME) {
        this.DUTY_NAME = DUTY_NAME;
    }

    public Bitmap getPHOTO() {
        return PHOTO;
    }

    public void setPHOTO(Bitmap PHOTO) {
        this.PHOTO = PHOTO;
    }

    @Override
    public String toString() {
        return "EmployeeBean{" +
                "USER_NO='" + USER_NO + '\'' +
                ", USER_NAME='" + USER_NAME + '\'' +
                ", EMPLOYEE_NO='" + EMPLOYEE_NO + '\'' +
                ", MAIL_ADDRESS='" + MAIL_ADDRESS + '\'' +
                ", DEPT_NO='" + DEPT_NO + '\'' +
                ", DEPT_NAME='" + DEPT_NAME + '\'' +
                ", DUTIES_NO='" + DUTIES_NO + '\'' +
                ", DUTY_NAME='" + DUTY_NAME + '\'' +
                ", PHOTO=" + PHOTO +
                '}';
    }
}
