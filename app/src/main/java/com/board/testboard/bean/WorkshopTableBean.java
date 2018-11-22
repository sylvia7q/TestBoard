package com.board.testboard.bean;

/**   
 * 车间看板表格数据
 */
public class WorkshopTableBean {

    private String number;
    private String lineNo;
    private String productNo;
    private String wo;
    private String woPlanQty;
    private String finishingRate;
    private String achievingRate;
    private String qtyOutput;
    private String workStatusName;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getWo() {
        return wo;
    }

    public void setWo(String wo) {
        this.wo = wo;
    }

    public String getWoPlanQty() {
        return woPlanQty;
    }

    public void setWoPlanQty(String woPlanQty) {
        this.woPlanQty = woPlanQty;
    }

    public String getFinishingRate() {
        return finishingRate;
    }

    public void setFinishingRate(String finishingRate) {
        this.finishingRate = finishingRate;
    }

    public String getAchievingRate() {
        return achievingRate;
    }

    public void setAchievingRate(String achievingRate) {
        this.achievingRate = achievingRate;
    }

    public String getQtyOutput() {
        return qtyOutput;
    }

    public void setQtyOutput(String qtyOutput) {
        this.qtyOutput = qtyOutput;
    }

    public String getWorkStatusName() {
        return workStatusName;
    }

    public void setWorkStatusName(String workStatusName) {
        this.workStatusName = workStatusName;
    }

    @Override
    public String toString() {
        return "WorkshopTableBean{" +
                "number='" + number + '\'' +
                ", lineNo='" + lineNo + '\'' +
                ", productNo='" + productNo + '\'' +
                ", wo='" + wo + '\'' +
                ", woPlanQty='" + woPlanQty + '\'' +
                ", finishingRate='" + finishingRate + '\'' +
                ", achievingRate='" + achievingRate + '\'' +
                ", qtyOutput='" + qtyOutput + '\'' +
                ", workStatusName='" + workStatusName + '\'' +
                '}';
    }
}
