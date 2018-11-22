package com.board.testboard.bean;

/**
 * 班次信息
 */
public class ShiftBean {

    private String sShiftNo;  
    private String sShiftName;
    private String sPeriodType;
    private String dtShiftTimeBegin;
    private String dtShiftTimeEnd;
    private String sShiftTimeBegin;
    private String sShiftTimeEnd;

    public String getsShiftNo() {
        return sShiftNo;
    }

    public void setsShiftNo(String sShiftNo) {
        this.sShiftNo = sShiftNo;
    }

    public String getsShiftName() {
        return sShiftName;
    }

    public void setsShiftName(String sShiftName) {
        this.sShiftName = sShiftName;
    }

    public String getsPeriodType() {
        return sPeriodType;
    }

    public void setsPeriodType(String sPeriodType) {
        this.sPeriodType = sPeriodType;
    }

    public String getDtShiftTimeBegin() {
        return dtShiftTimeBegin;
    }

    public void setDtShiftTimeBegin(String dtShiftTimeBegin) {
        this.dtShiftTimeBegin = dtShiftTimeBegin;
    }

    public String getDtShiftTimeEnd() {
        return dtShiftTimeEnd;
    }

    public void setDtShiftTimeEnd(String dtShiftTimeEnd) {
        this.dtShiftTimeEnd = dtShiftTimeEnd;
    }

    public String getsShiftTimeBegin() {
        return sShiftTimeBegin;
    }

    public void setsShiftTimeBegin(String sShiftTimeBegin) {
        this.sShiftTimeBegin = sShiftTimeBegin;
    }

    public String getsShiftTimeEnd() {
        return sShiftTimeEnd;
    }

    public void setsShiftTimeEnd(String sShiftTimeEnd) {
        this.sShiftTimeEnd = sShiftTimeEnd;
    }

    @Override
    public String toString() {
        return "ShiftBean{" +
                "sShiftNo='" + sShiftNo + '\'' +
                ", sShiftName='" + sShiftName + '\'' +
                ", sPeriodType='" + sPeriodType + '\'' +
                ", dtShiftTimeBegin='" + dtShiftTimeBegin + '\'' +
                ", dtShiftTimeEnd='" + dtShiftTimeEnd + '\'' +
                ", sShiftTimeBegin='" + sShiftTimeBegin + '\'' +
                ", sShiftTimeEnd='" + sShiftTimeEnd + '\'' +
                '}';
    }
}
