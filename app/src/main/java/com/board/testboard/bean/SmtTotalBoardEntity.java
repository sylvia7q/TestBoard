package com.board.testboard.bean;

/**
 * [SMT车间看板数据]   
 * Created by YANGT
 * 2018/4/17.
 */

public class SmtTotalBoardEntity {
    private String sNumber; //序号
    private String sWorkStatus; //线别生产状态代码
    private String sWorkStatusName; //线别生产状态名称
    private String sLineNo; //线别
    private String sWo; //制令单
    private String sCustomerName; //客户
    private String sProductNo; //产品
    private String sWoQtyPlan; //制令单计划数量
    private String sObjectiveOutput; //目标产出
    private String sQtyOutput; //实际产出
    private String sAchievingRate; //达成率
    private String sFinishingRate; //完成率
    private String sdlQtyInput; //投产数量
    private String sdlQtyOutput; //实际产出

    public String getsNumber() {
        return sNumber;
    }

    public void setsNumber(String sNumber) {
        this.sNumber = sNumber;
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

    public String getsCustomerName() {
        return sCustomerName;
    }

    public void setsCustomerName(String sCustomerName) {
        this.sCustomerName = sCustomerName;
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

    public String getSdlQtyInput() {
        return sdlQtyInput;
    }

    public void setSdlQtyInput(String sdlQtyInput) {
        this.sdlQtyInput = sdlQtyInput;
    }

    public String getSdlQtyOutput() {
        return sdlQtyOutput;
    }

    public void setSdlQtyOutput(String sdlQtyOutput) {
        this.sdlQtyOutput = sdlQtyOutput;
    }
}
