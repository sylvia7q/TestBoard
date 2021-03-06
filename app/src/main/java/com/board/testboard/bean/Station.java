package com.board.testboard.bean;
 
public class Station { 

    private String LINE_NO;

    private String STATION_NO;

    private String STATION_ID;

    private String STATION_NAME;

    private String STATION_NAME_CHS;

    private String STATION_NAME_CHT;

    private String STATION_NAME_EN;

    private String PROCESS_NO;

    private String SELECTED;

    @Override
    public String toString() {
        return "Station{" +
                "LINE_NO='" + LINE_NO + '\'' +
                ", STATION_NO='" + STATION_NO + '\'' +
                ", STATION_ID='" + STATION_ID + '\'' +
                ", STATION_NAME='" + STATION_NAME + '\'' +
                ", STATION_NAME_CHS='" + STATION_NAME_CHS + '\'' +
                ", STATION_NAME_CHT='" + STATION_NAME_CHT + '\'' +
                ", STATION_NAME_EN='" + STATION_NAME_EN + '\'' +
                ", PROCESS_NO='" + PROCESS_NO + '\'' +
                ", SELECTED='" + SELECTED + '\'' +
                '}';
    }

    public String getLINE_NO() {
        return LINE_NO;
    }

    public void setLINE_NO(String LINE_NO) {
        this.LINE_NO = LINE_NO;
    }

    public String getSTATION_NO() {
        return STATION_NO;
    }

    public void setSTATION_NO(String STATION_NO) {
        this.STATION_NO = STATION_NO;
    }

    public String getSTATION_ID() {
        return STATION_ID;
    }

    public void setSTATION_ID(String STATION_ID) {
        this.STATION_ID = STATION_ID;
    }

    public String getSTATION_NAME() {
        return STATION_NAME;
    }

    public void setSTATION_NAME(String STATION_NAME) {
        this.STATION_NAME = STATION_NAME;
    }

    public String getSTATION_NAME_CHS() {
        return STATION_NAME_CHS;
    }

    public void setSTATION_NAME_CHS(String STATION_NAME_CHS) {
        this.STATION_NAME_CHS = STATION_NAME_CHS;
    }

    public String getSTATION_NAME_CHT() {
        return STATION_NAME_CHT;
    }

    public void setSTATION_NAME_CHT(String STATION_NAME_CHT) {
        this.STATION_NAME_CHT = STATION_NAME_CHT;
    }

    public String getSTATION_NAME_EN() {
        return STATION_NAME_EN;
    }

    public void setSTATION_NAME_EN(String STATION_NAME_EN) {
        this.STATION_NAME_EN = STATION_NAME_EN;
    }

    public String getPROCESS_NO() {
        return PROCESS_NO;
    }

    public void setPROCESS_NO(String PROCESS_NO) {
        this.PROCESS_NO = PROCESS_NO;
    }

    public String getSELECTED() {
        return SELECTED;
    }

    public void setSELECTED(String SELECTED) {
        this.SELECTED = SELECTED;
    }
}
