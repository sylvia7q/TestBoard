package com.board.testboard.bean;

/**
 * 时段数据
 */
public class PeriodOutputBean {   

    private String sLineNo; //线别
    private String PeriodName; //时段名称
    private String InPutQty; //投入数
    private String OutPutQty; //产出数

    public String getsLineNo() {
        return sLineNo;
    }

    public void setsLineNo(String sLineNo) {
        this.sLineNo = sLineNo;
    }

    public String getPeriodName() {
        return PeriodName;
    }

    public void setPeriodName(String periodName) {
        PeriodName = periodName;
    }

    public String getInPutQty() {
        return InPutQty;
    }

    public void setInPutQty(String inPutQty) {
        InPutQty = inPutQty;
    }

    public String getOutPutQty() {
        return OutPutQty;
    }

    public void setOutPutQty(String outPutQty) {
        OutPutQty = outPutQty;
    }

    @Override
    public String toString() {
        return "PeriodOutputBean{" +
                "sLineNo='" + sLineNo + '\'' +
                ", PeriodName='" + PeriodName + '\'' +
                ", InPutQty='" + InPutQty + '\'' +
                ", OutPutQty='" + OutPutQty + '\'' +
                '}';
    }
}
