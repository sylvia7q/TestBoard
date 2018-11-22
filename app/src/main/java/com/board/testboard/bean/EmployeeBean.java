package com.board.testboard.bean;

import android.graphics.Bitmap;

/**
 * 人员岗位信息  
 */
public class EmployeeBean {

    private String USER_NO; //员工号
    private String USER_NAME; //员工姓名
    private String USER_NAME_CH; //员工姓名
    private String USER_NAME_EN; //员工姓名
    private String DUTIES_NO; //岗位
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

    public String getUSER_NAME_CH() {
        return USER_NAME_CH;
    }

    public void setUSER_NAME_CH(String USER_NAME_CH) {
        this.USER_NAME_CH = USER_NAME_CH;
    }

    public String getUSER_NAME_EN() {
        return USER_NAME_EN;
    }

    public void setUSER_NAME_EN(String USER_NAME_EN) {
        this.USER_NAME_EN = USER_NAME_EN;
    }

    public String getDUTIES_NO() {
        return DUTIES_NO;
    }

    public void setDUTIES_NO(String DUTIES_NO) {
        this.DUTIES_NO = DUTIES_NO;
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
                ", USER_NAME_CH='" + USER_NAME_CH + '\'' +
                ", USER_NAME_EN='" + USER_NAME_EN + '\'' +
                ", DUTIES_NO='" + DUTIES_NO + '\'' +
                ", PHOTO='" + PHOTO + '\'' +
                '}';
    }
}
