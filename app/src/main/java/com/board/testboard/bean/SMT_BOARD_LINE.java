package com.board.testboard.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 多线别
 */
@Table(name = "SMT_BOARD_LINE")
public class SMT_BOARD_LINE implements Serializable{

    @Id(column = "LINE_NO")
    private String LINE_NO;

    @Column(column = "LINE_NAME")
    private String LINE_NAME;

    @Column(column = "WORKSHOP_ID")
    private String WORKSHOP_ID;

    @Column(column = "WORKSHOP_NAME")
    private String WORKSHOP_NAME;

    @Column(column = "WORKSHOP_NO")
    private String WORKSHOP_NO;

    @Column(column = "STATION_NO")
    private String STATION_NO;

    @Column(column = "STATION_NAME")
    private String STATION_NAME;

    @Column(column = "DISPLAY_TIME")//每条线展示时长
    private String DISPLAY_TIME;

    @Column(column = "PARAM_ID")
    private String PARAM_ID;

    public String getLINE_NO() {
        return LINE_NO;
    }

    public void setLINE_NO(String LINE_NO) {
        this.LINE_NO = LINE_NO;
    }

    public String getLINE_NAME() {
        return LINE_NAME;
    }

    public void setLINE_NAME(String LINE_NAME) {
        this.LINE_NAME = LINE_NAME;
    }

    public String getWORKSHOP_ID() {
        return WORKSHOP_ID;
    }

    public void setWORKSHOP_ID(String WORKSHOP_ID) {
        this.WORKSHOP_ID = WORKSHOP_ID;
    }

    public String getWORKSHOP_NAME() {
        return WORKSHOP_NAME;
    }

    public void setWORKSHOP_NAME(String WORKSHOP_NAME) {
        this.WORKSHOP_NAME = WORKSHOP_NAME;
    }

    public String getWORKSHOP_NO() {
        return WORKSHOP_NO;
    }

    public void setWORKSHOP_NO(String WORKSHOP_NO) {
        this.WORKSHOP_NO = WORKSHOP_NO;
    }

    public String getSTATION_NO() {
        return STATION_NO;
    }

    public void setSTATION_NO(String STATION_NO) {
        this.STATION_NO = STATION_NO;
    }

    public String getSTATION_NAME() {
        return STATION_NAME;
    }

    public void setSTATION_NAME(String STATION_NAME) {
        this.STATION_NAME = STATION_NAME;
    }

    public String getDISPLAY_TIME() {
        return DISPLAY_TIME;
    }

    public void setDISPLAY_TIME(String DISPLAY_TIME) {
        this.DISPLAY_TIME = DISPLAY_TIME;
    }

    public String getPARAM_ID() {
        return PARAM_ID;
    }

    public void setPARAM_ID(String PARAM_ID) {
        this.PARAM_ID = PARAM_ID;
    }

    @Override
    public String toString() {
        return "SMT_BOARD_LINE{" +
                "LINE_NO='" + LINE_NO + '\'' +
                ", LINE_NAME='" + LINE_NAME + '\'' +
                ", WORKSHOP_ID='" + WORKSHOP_ID + '\'' +
                ", WORKSHOP_NAME='" + WORKSHOP_NAME + '\'' +
                ", WORKSHOP_NO='" + WORKSHOP_NO + '\'' +
                ", STATION_NO='" + STATION_NO + '\'' +
                ", STATION_NAME='" + STATION_NAME + '\'' +
                ", DISPLAY_TIME='" + DISPLAY_TIME + '\'' +
                ", PARAM_ID='" + PARAM_ID + '\'' +
                '}';
    }
}
