package com.board.testboard.bean;

/**
 * 线别状态  
 */
public class LineWorkStatusBean {
    private String sLineNo;
    private String sWorkStatus;
    private String sWorkStatusName;

    public String getsLineNo() {
        return sLineNo;
    }

    public void setsLineNo(String sLineNo) {
        this.sLineNo = sLineNo;
    }

    public String getsWorkStatus() {
        return sWorkStatus;
    }

    public void setsWorkStatus(String sWorkStatus) {
        this.sWorkStatus = sWorkStatus;
    }

    public String getsWorkStatusName() {
        return sWorkStatusName;
    }

    public void setsWorkStatusName(String sWorkStatusName) {
        this.sWorkStatusName = sWorkStatusName;
    }

    @Override
    public String toString() {
        return "LineWorkStatusBean{" +
                "sLineNo='" + sLineNo + '\'' +
                ", sWorkStatus='" + sWorkStatus + '\'' +
                ", sWorkStatusName='" + sWorkStatusName + '\'' +
                '}';
    }
}
