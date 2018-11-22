package com.board.testboard.bean;

/**
 * 欠料预警信息
 */
public class WarnBean {
   
    private String MACHINE_SEQ;
    private String MACHINE_NO;
    private String FID;
    private String PART_NO;
    private String PART_DESC;
    private String VICE_FLAG;
    private String CONSUME_QTY;
    private String FEED_QTY;
    private String REEL_NO;
    private String QTY_HOURSOUTPUT;
    private String BOARD_COUNT;
    private String FEED_PART_NO;
    private String FEED_PART_DESC;
    private String CURRENT_QTY;
    private String IS_INCLUDED_UNION;
    private String QTY_NEED;
    private String NEED_PERCENT;
    private String TIME;
    private String AUDIT_FEEDER_INNER_CODE;

    public String getMACHINE_SEQ() {
        return MACHINE_SEQ;
    }

    public void setMACHINE_SEQ(String MACHINE_SEQ) {
        this.MACHINE_SEQ = MACHINE_SEQ;
    }

    public String getMACHINE_NO() {
        return MACHINE_NO;
    }

    public void setMACHINE_NO(String MACHINE_NO) {
        this.MACHINE_NO = MACHINE_NO;
    }

    public String getFID() {
        return FID;
    }

    public void setFID(String FID) {
        this.FID = FID;
    }

    public String getPART_NO() {
        return PART_NO;
    }

    public void setPART_NO(String PART_NO) {
        this.PART_NO = PART_NO;
    }

    public String getPART_DESC() {
        return PART_DESC;
    }

    public void setPART_DESC(String PART_DESC) {
        this.PART_DESC = PART_DESC;
    }

    public String getVICE_FLAG() {
        return VICE_FLAG;
    }

    public void setVICE_FLAG(String VICE_FLAG) {
        this.VICE_FLAG = VICE_FLAG;
    }

    public String getCONSUME_QTY() {
        return CONSUME_QTY;
    }

    public void setCONSUME_QTY(String CONSUME_QTY) {
        this.CONSUME_QTY = CONSUME_QTY;
    }

    public String getFEED_QTY() {
        return FEED_QTY;
    }

    public void setFEED_QTY(String FEED_QTY) {
        this.FEED_QTY = FEED_QTY;
    }

    public String getREEL_NO() {
        return REEL_NO;
    }

    public void setREEL_NO(String REEL_NO) {
        this.REEL_NO = REEL_NO;
    }

    public String getQTY_HOURSOUTPUT() {
        return QTY_HOURSOUTPUT;
    }

    public void setQTY_HOURSOUTPUT(String QTY_HOURSOUTPUT) {
        this.QTY_HOURSOUTPUT = QTY_HOURSOUTPUT;
    }

    public String getBOARD_COUNT() {
        return BOARD_COUNT;
    }

    public void setBOARD_COUNT(String BOARD_COUNT) {
        this.BOARD_COUNT = BOARD_COUNT;
    }

    public String getFEED_PART_NO() {
        return FEED_PART_NO;
    }

    public void setFEED_PART_NO(String FEED_PART_NO) {
        this.FEED_PART_NO = FEED_PART_NO;
    }

    public String getFEED_PART_DESC() {
        return FEED_PART_DESC;
    }

    public void setFEED_PART_DESC(String FEED_PART_DESC) {
        this.FEED_PART_DESC = FEED_PART_DESC;
    }

    public String getCURRENT_QTY() {
        return CURRENT_QTY;
    }

    public void setCURRENT_QTY(String CURRENT_QTY) {
        this.CURRENT_QTY = CURRENT_QTY;
    }

    public String getIS_INCLUDED_UNION() {
        return IS_INCLUDED_UNION;
    }

    public void setIS_INCLUDED_UNION(String IS_INCLUDED_UNION) {
        this.IS_INCLUDED_UNION = IS_INCLUDED_UNION;
    }

    public String getQTY_NEED() {
        return QTY_NEED;
    }

    public void setQTY_NEED(String QTY_NEED) {
        this.QTY_NEED = QTY_NEED;
    }

    public String getNEED_PERCENT() {
        return NEED_PERCENT;
    }

    public void setNEED_PERCENT(String NEED_PERCENT) {
        this.NEED_PERCENT = NEED_PERCENT;
    }

    public String getTIME() {
        return TIME;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }

    public String getAUDIT_FEEDER_INNER_CODE() {
        return AUDIT_FEEDER_INNER_CODE;
    }

    public void setAUDIT_FEEDER_INNER_CODE(String AUDIT_FEEDER_INNER_CODE) {
        this.AUDIT_FEEDER_INNER_CODE = AUDIT_FEEDER_INNER_CODE;
    }

    @Override
    public String toString() {
        return "WarnBean{" +
                "MACHINE_SEQ='" + MACHINE_SEQ + '\'' +
                ", MACHINE_NO='" + MACHINE_NO + '\'' +
                ", FID='" + FID + '\'' +
                ", PART_NO='" + PART_NO + '\'' +
                ", PART_DESC='" + PART_DESC + '\'' +
                ", VICE_FLAG='" + VICE_FLAG + '\'' +
                ", CONSUME_QTY='" + CONSUME_QTY + '\'' +
                ", FEED_QTY='" + FEED_QTY + '\'' +
                ", REEL_NO='" + REEL_NO + '\'' +
                ", QTY_HOURSOUTPUT='" + QTY_HOURSOUTPUT + '\'' +
                ", BOARD_COUNT='" + BOARD_COUNT + '\'' +
                ", FEED_PART_NO='" + FEED_PART_NO + '\'' +
                ", FEED_PART_DESC='" + FEED_PART_DESC + '\'' +
                ", CURRENT_QTY='" + CURRENT_QTY + '\'' +
                ", IS_INCLUDED_UNION='" + IS_INCLUDED_UNION + '\'' +
                ", QTY_NEED='" + QTY_NEED + '\'' +
                ", NEED_PERCENT='" + NEED_PERCENT + '\'' +
                ", TIME='" + TIME + '\'' +
                ", AUDIT_FEEDER_INNER_CODE='" + AUDIT_FEEDER_INNER_CODE + '\'' +
                '}';
    }
}
