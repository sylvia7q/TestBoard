package com.board.testboard.bean;

/**
 * 制令单信息
 */  
public class WoBean {

    private String sLineNo;
    private String sCustomerName;
    private String sWo;
    private String sProductNo;
    private String sWoQtyPlan;
    private String sSide;
    private String sMo;
    private String sMoQtyPlan;
    private String sStationNoInput;
    private String sStationNoOutput;
    private String sQtyHoursoutput;
    private String sWoProcessingDate;

    public String getsLineNo() {
        return sLineNo;
    }

    public void setsLineNo(String sLineNo) {
        this.sLineNo = sLineNo;
    }

    public String getsCustomerName() {
        return sCustomerName;
    }

    public void setsCustomerName(String sCustomerName) {
        this.sCustomerName = sCustomerName;
    }

    public String getsWo() {
        return sWo;
    }

    public void setsWo(String sWo) {
        this.sWo = sWo;
    }

    public String getsProductNo() {
        return sProductNo;
    }

    public void setsProductNo(String sProductNo) {
        this.sProductNo = sProductNo;
    }

    public String getsWoQtyPlan() {
        return sWoQtyPlan;
    }

    public void setsWoQtyPlan(String sWoQtyPlan) {
        this.sWoQtyPlan = sWoQtyPlan;
    }

    public String getsSide() {
        return sSide;
    }

    public void setsSide(String sSide) {
        this.sSide = sSide;
    }

    public String getsMo() {
        return sMo;
    }

    public void setsMo(String sMo) {
        this.sMo = sMo;
    }

    public String getsMoQtyPlan() {
        return sMoQtyPlan;
    }

    public void setsMoQtyPlan(String sMoQtyPlan) {
        this.sMoQtyPlan = sMoQtyPlan;
    }

    public String getsStationNoInput() {
        return sStationNoInput;
    }

    public void setsStationNoInput(String sStationNoInput) {
        this.sStationNoInput = sStationNoInput;
    }

    public String getsStationNoOutput() {
        return sStationNoOutput;
    }

    public void setsStationNoOutput(String sStationNoOutput) {
        this.sStationNoOutput = sStationNoOutput;
    }

    public String getsQtyHoursoutput() {
        return sQtyHoursoutput;
    }

    public void setsQtyHoursoutput(String sQtyHoursoutput) {
        this.sQtyHoursoutput = sQtyHoursoutput;
    }

    public String getsWoProcessingDate() {
        return sWoProcessingDate;
    }

    public void setsWoProcessingDate(String sWoProcessingDate) {
        this.sWoProcessingDate = sWoProcessingDate;
    }

    @Override
    public String toString() {
        return "WoBean{" +
                "sLineNo='" + sLineNo + '\'' +
                ", sCustomerName='" + sCustomerName + '\'' +
                ", sWo='" + sWo + '\'' +
                ", sProductNo='" + sProductNo + '\'' +
                ", sWoQtyPlan='" + sWoQtyPlan + '\'' +
                ", sSide='" + sSide + '\'' +
                ", sMo='" + sMo + '\'' +
                ", sMoQtyPlan='" + sMoQtyPlan + '\'' +
                ", sStationNoInput='" + sStationNoInput + '\'' +
                ", sStationNoOutput='" + sStationNoOutput + '\'' +
                ", sQtyHoursoutput='" + sQtyHoursoutput + '\'' +
                ", sWoProcessingDate='" + sWoProcessingDate + '\'' +
                '}';
    }
}
