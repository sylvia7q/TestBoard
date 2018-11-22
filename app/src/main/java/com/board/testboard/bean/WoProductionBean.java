package com.board.testboard.bean;

/**
 * 制令单产出信息
 */
public class WoProductionBean {
   
    private String sLineNo;
    private String sWo;
    private String sAchievingRate;
    private String sFinishingRate;
    private String sObjectiveOutput;
    private String sQtyOutput;

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

    public String getsAchievingRate() {
        return sAchievingRate;
    }

    public void setsAchievingRate(String sAchievingRate) {
        this.sAchievingRate = sAchievingRate;
    }

    public String getsFinishingRate() {
        return sFinishingRate;
    }

    public void setsFinishingRate(String sFinishingRate) {
        this.sFinishingRate = sFinishingRate;
    }

    public String getsObjectiveOutput() {
        return sObjectiveOutput;
    }

    public void setsObjectiveOutput(String sObjectiveOutput) {
        this.sObjectiveOutput = sObjectiveOutput;
    }

    public String getsQtyOutput() {
        return sQtyOutput;
    }

    public void setsQtyOutput(String sQtyOutput) {
        this.sQtyOutput = sQtyOutput;
    }

    @Override
    public String toString() {
        return "WoProductionBean{" +
                "sLineNo='" + sLineNo + '\'' +
                ", sWo='" + sWo + '\'' +
                ", sAchievingRate='" + sAchievingRate + '\'' +
                ", sFinishingRate='" + sFinishingRate + '\'' +
                ", sObjectiveOutput='" + sObjectiveOutput + '\'' +
                ", sQtyOutput='" + sQtyOutput + '\'' +
                '}';
    }
}
