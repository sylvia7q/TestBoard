package com.board.testboard.bean;

/**
 * 公告消息属性设置
 */
public class MessageAttrBean {

    private String MSG_TYPE;
    private String FACTORY_NO;
    private String TYPE_NAME;
    private String RGB;
    private String SORTID;
    private String STATUS;
    private String NOTE;
    private String CREATE_USERID;
    private String CREATE_DATE;
    private String CREATE_IP;
    private String UPDATE_USERID;
    private String UPDATE_DATE;
    private String UPDATE_IP;

    public String getMSG_TYPE() {
        return MSG_TYPE;
    }

    public void setMSG_TYPE(String MSG_TYPE) {
        this.MSG_TYPE = MSG_TYPE;
    }

    public String getFACTORY_NO() {
        return FACTORY_NO;
    }

    public void setFACTORY_NO(String FACTORY_NO) {
        this.FACTORY_NO = FACTORY_NO;
    }

    public String getTYPE_NAME() {
        return TYPE_NAME;
    }

    public void setTYPE_NAME(String TYPE_NAME) {
        this.TYPE_NAME = TYPE_NAME;
    }

    public String getRGB() {
        return RGB;
    }

    public void setRGB(String RGB) {
        this.RGB = RGB;
    }

    public String getSORTID() {
        return SORTID;
    }

    public void setSORTID(String SORTID) {
        this.SORTID = SORTID;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getNOTE() {
        return NOTE;
    }

    public void setNOTE(String NOTE) {
        this.NOTE = NOTE;
    }

    public String getCREATE_USERID() {
        return CREATE_USERID;
    }

    public void setCREATE_USERID(String CREATE_USERID) {
        this.CREATE_USERID = CREATE_USERID;
    }

    public String getCREATE_DATE() {
        return CREATE_DATE;
    }

    public void setCREATE_DATE(String CREATE_DATE) {
        this.CREATE_DATE = CREATE_DATE;
    }

    public String getCREATE_IP() {
        return CREATE_IP;
    }

    public void setCREATE_IP(String CREATE_IP) {
        this.CREATE_IP = CREATE_IP;
    }

    public String getUPDATE_USERID() {
        return UPDATE_USERID;
    }

    public void setUPDATE_USERID(String UPDATE_USERID) {
        this.UPDATE_USERID = UPDATE_USERID;
    }

    public String getUPDATE_DATE() {
        return UPDATE_DATE;
    }

    public void setUPDATE_DATE(String UPDATE_DATE) {
        this.UPDATE_DATE = UPDATE_DATE;
    }

    public String getUPDATE_IP() {
        return UPDATE_IP;
    }

    public void setUPDATE_IP(String UPDATE_IP) {
        this.UPDATE_IP = UPDATE_IP;
    }

    @Override
    public String toString() {
        return "MessageAttrBean{" +
                "MSG_TYPE='" + MSG_TYPE + '\'' +
                ", FACTORY_NO='" + FACTORY_NO + '\'' +
                ", TYPE_NAME='" + TYPE_NAME + '\'' +
                ", RGB='" + RGB + '\'' +
                ", SORTID='" + SORTID + '\'' +
                ", STATUS='" + STATUS + '\'' +
                ", NOTE='" + NOTE + '\'' +
                ", CREATE_USERID='" + CREATE_USERID + '\'' +
                ", CREATE_DATE='" + CREATE_DATE + '\'' +
                ", CREATE_IP='" + CREATE_IP + '\'' +
                ", UPDATE_USERID='" + UPDATE_USERID + '\'' +
                ", UPDATE_DATE='" + UPDATE_DATE + '\'' +
                ", UPDATE_IP='" + UPDATE_IP + '\'' +
                '}';
    }
}
