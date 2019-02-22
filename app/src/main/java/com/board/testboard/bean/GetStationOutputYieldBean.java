package com.topcee.kanban.bean;

/**
 * 获取工站良率
 */
public class GetStationOutputYieldBean {
    private String sLineNo;
    private String sWo;
    private String sOutputYield;

    public String getsLineNo() {
        return sLineNo;
    }

    public void setsLineNo(String sLineNo) {
        this.sLineNo = sLineNo;
    }

    public String getsWo() {
        return sWo;
    }

    public void setsWo(String sWo) {
        this.sWo = sWo;
    }

    public String getsOutputYield() {
        return sOutputYield;
    }

    public void setsOutputYield(String sOutputYield) {
        this.sOutputYield = sOutputYield;
    }

    @Override
    public String toString() {
        return "GetStationOutputYieldBean{" +
                "sLineNo='" + sLineNo + '\'' +
                ", sWo='" + sWo + '\'' +
                ", sOutputYield='" + sOutputYield + '\'' +
                '}';
    }
}
