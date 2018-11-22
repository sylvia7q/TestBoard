package com.board.testboard.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 车间看板多线别
 */
@Table(name = "SMT_WORKSHOP_LINE_SETTING")
public class SMT_WORKSHOP_LINE_SETTING {

    @Id(column = "LINE_NO")
    private String LINE_NO;

    @Column(column = "LINE_ID")
    private String LINE_ID;

    @Column(column = "LINE_NAME")
    private String LINE_NAME;

    @Column(column = "NAME_CHT")
    private String NAME_CHT;

    @Column(column = "NAME_CHS")
    private String NAME_CHS;

    @Column(column = "NAME_EN")
    private String NAME_EN;

    @Column(column = "WORKSHOP_ID")
    private String WORKSHOP_ID;

    @Column(column = "WORKSHOP_NAME")
    private String WORKSHOP_NAME;

    @Column(column = "WORKSHOP_NO")
    private String WORKSHOP_NO;

    @Column(column = "LINE_TYPE_NAME")
    private String LINE_TYPE_NAME;

    @Column(column = "STATION_NAME_INPUT")
    private String STATION_NAME_INPUT;

    @Column(column = "STATION_NAME_OUTPUT")
    private String STATION_NAME_OUTPUT;

    @Column(column = "SORTID")
    private String SORTID;

    @Column(column = "TYPE")
    private String TYPE;

    @Column(column = "STATUS")
    private String STATUS;

    @Column(column = "STATUS_NAME")
    private String STATUS_NAME;

    @Column(column = "NOTE")
    private String NOTE;

    @Column(column = "CREATE_USERID")
    private String CREATE_USERID;

    @Column(column = "CREATE_DATE")
    private String CREATE_DATE;

    @Column(column = "CREATE_IP")
    private String CREATE_IP;

    @Column(column = "UPDATE_USERID")
    private String UPDATE_USERID;

    @Column(column = "UPDATE_DATE")
    private String UPDATE_DATE;

    @Column(column = "UPDATE_IP")
    private String UPDATE_IP;

    @Column(column = "PARAM_ID")
    private String PARAM_ID;

    @Column(column = "LINE_TYPE")
    private String LINE_TYPE;

    @Column(column = "STATION_NO_INPUT")
    private String STATION_NO_INPUT;

    @Column(column = "STATION_NO_OUTPUT")
    private String STATION_NO_OUTPUT;

    @Column(column = "SELECTED")
    private String SELECTED;

    public String getLINE_NO() {
        return LINE_NO;
    }

    public void setLINE_NO(String LINE_NO) {
        this.LINE_NO = LINE_NO;
    }

    public String getLINE_ID() {
        return LINE_ID;
    }

    public void setLINE_ID(String LINE_ID) {
        this.LINE_ID = LINE_ID;
    }

    public String getLINE_NAME() {
        return LINE_NAME;
    }

    public void setLINE_NAME(String LINE_NAME) {
        this.LINE_NAME = LINE_NAME;
    }

    public String getNAME_CHT() {
        return NAME_CHT;
    }

    public void setNAME_CHT(String NAME_CHT) {
        this.NAME_CHT = NAME_CHT;
    }

    public String getNAME_CHS() {
        return NAME_CHS;
    }

    public void setNAME_CHS(String NAME_CHS) {
        this.NAME_CHS = NAME_CHS;
    }

    public String getNAME_EN() {
        return NAME_EN;
    }

    public void setNAME_EN(String NAME_EN) {
        this.NAME_EN = NAME_EN;
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

    public String getLINE_TYPE_NAME() {
        return LINE_TYPE_NAME;
    }

    public void setLINE_TYPE_NAME(String LINE_TYPE_NAME) {
        this.LINE_TYPE_NAME = LINE_TYPE_NAME;
    }

    public String getSTATION_NAME_INPUT() {
        return STATION_NAME_INPUT;
    }

    public void setSTATION_NAME_INPUT(String STATION_NAME_INPUT) {
        this.STATION_NAME_INPUT = STATION_NAME_INPUT;
    }

    public String getSTATION_NAME_OUTPUT() {
        return STATION_NAME_OUTPUT;
    }

    public void setSTATION_NAME_OUTPUT(String STATION_NAME_OUTPUT) {
        this.STATION_NAME_OUTPUT = STATION_NAME_OUTPUT;
    }

    public String getSORTID() {
        return SORTID;
    }

    public void setSORTID(String SORTID) {
        this.SORTID = SORTID;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getSTATUS_NAME() {
        return STATUS_NAME;
    }

    public void setSTATUS_NAME(String STATUS_NAME) {
        this.STATUS_NAME = STATUS_NAME;
    }

    public String getNOTE() {
        return NOTE;
    }

    public void setNOTE(String NOTE) {
        this.NOTE = NOTE;
    }

    public String getCREATE_USERID() {
        return CREATE_USERID;
    }

    public void setCREATE_USERID(String CREATE_USERID) {
        this.CREATE_USERID = CREATE_USERID;
    }

    public String getCREATE_DATE() {
        return CREATE_DATE;
    }

    public void setCREATE_DATE(String CREATE_DATE) {
        this.CREATE_DATE = CREATE_DATE;
    }

    public String getCREATE_IP() {
        return CREATE_IP;
    }

    public void setCREATE_IP(String CREATE_IP) {
        this.CREATE_IP = CREATE_IP;
    }

    public String getUPDATE_USERID() {
        return UPDATE_USERID;
    }

    public void setUPDATE_USERID(String UPDATE_USERID) {
        this.UPDATE_USERID = UPDATE_USERID;
    }

    public String getUPDATE_DATE() {
        return UPDATE_DATE;
    }

    public void setUPDATE_DATE(String UPDATE_DATE) {
        this.UPDATE_DATE = UPDATE_DATE;
    }

    public String getUPDATE_IP() {
        return UPDATE_IP;
    }

    public void setUPDATE_IP(String UPDATE_IP) {
        this.UPDATE_IP = UPDATE_IP;
    }

    public String getPARAM_ID() {
        return PARAM_ID;
    }

    public void setPARAM_ID(String PARAM_ID) {
        this.PARAM_ID = PARAM_ID;
    }

    public String getLINE_TYPE() {
        return LINE_TYPE;
    }

    public void setLINE_TYPE(String LINE_TYPE) {
        this.LINE_TYPE = LINE_TYPE;
    }

    public String getSTATION_NO_INPUT() {
        return STATION_NO_INPUT;
    }

    public void setSTATION_NO_INPUT(String STATION_NO_INPUT) {
        this.STATION_NO_INPUT = STATION_NO_INPUT;
    }

    public String getSTATION_NO_OUTPUT() {
        return STATION_NO_OUTPUT;
    }

    public void setSTATION_NO_OUTPUT(String STATION_NO_OUTPUT) {
        this.STATION_NO_OUTPUT = STATION_NO_OUTPUT;
    }

    public String getSELECTED() {
        return SELECTED;
    }

    public void setSELECTED(String SELECTED) {
        this.SELECTED = SELECTED;
    }

    @Override
    public String toString() {
        return "SMT_WORKSHOP_LINE_SETTING{" +
                "LINE_NO='" + LINE_NO + '\'' +
                ", LINE_ID='" + LINE_ID + '\'' +
                ", LINE_NAME='" + LINE_NAME + '\'' +
                ", NAME_CHT='" + NAME_CHT + '\'' +
                ", NAME_CHS='" + NAME_CHS + '\'' +
                ", NAME_EN='" + NAME_EN + '\'' +
                ", WORKSHOP_ID='" + WORKSHOP_ID + '\'' +
                ", WORKSHOP_NAME='" + WORKSHOP_NAME + '\'' +
                ", WORKSHOP_NO='" + WORKSHOP_NO + '\'' +
                ", LINE_TYPE_NAME='" + LINE_TYPE_NAME + '\'' +
                ", STATION_NAME_INPUT='" + STATION_NAME_INPUT + '\'' +
                ", STATION_NAME_OUTPUT='" + STATION_NAME_OUTPUT + '\'' +
                ", SORTID='" + SORTID + '\'' +
                ", TYPE='" + TYPE + '\'' +
                ", STATUS='" + STATUS + '\'' +
                ", STATUS_NAME='" + STATUS_NAME + '\'' +
                ", NOTE='" + NOTE + '\'' +
                ", CREATE_USERID='" + CREATE_USERID + '\'' +
                ", CREATE_DATE='" + CREATE_DATE + '\'' +
                ", CREATE_IP='" + CREATE_IP + '\'' +
                ", UPDATE_USERID='" + UPDATE_USERID + '\'' +
                ", UPDATE_DATE='" + UPDATE_DATE + '\'' +
                ", UPDATE_IP='" + UPDATE_IP + '\'' +
                ", PARAM_ID='" + PARAM_ID + '\'' +
                ", LINE_TYPE='" + LINE_TYPE + '\'' +
                ", STATION_NO_INPUT='" + STATION_NO_INPUT + '\'' +
                ", STATION_NO_OUTPUT='" + STATION_NO_OUTPUT + '\'' +
                ", SELECTED='" + SELECTED + '\'' +
                '}';
    }

}
