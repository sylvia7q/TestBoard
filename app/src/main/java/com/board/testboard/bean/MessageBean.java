package com.board.testboard.bean;

/**
 * 公告消息
 */
public class MessageBean {  
    private String MSG_ID;
    private String FACTORY_NO;
    private String MSG_TYPE;
    private String MSG_TITLE;
    private String MSG_DETAIL;
    private String VALID_DATE;
    private String INVALID_DATE;
    private String TYPE_NAME;
    private String RGB;

    public String getMSG_ID() {
        return MSG_ID;
    }

    public void setMSG_ID(String MSG_ID) {
        this.MSG_ID = MSG_ID;
    }

    public String getFACTORY_NO() {
        return FACTORY_NO;
    }

    public void setFACTORY_NO(String FACTORY_NO) {
        this.FACTORY_NO = FACTORY_NO;
    }

    public String getMSG_TYPE() {
        return MSG_TYPE;
    }

    public void setMSG_TYPE(String MSG_TYPE) {
        this.MSG_TYPE = MSG_TYPE;
    }

    public String getMSG_TITLE() {
        return MSG_TITLE;
    }

    public void setMSG_TITLE(String MSG_TITLE) {
        this.MSG_TITLE = MSG_TITLE;
    }

    public String getMSG_DETAIL() {
        return MSG_DETAIL;
    }

    public void setMSG_DETAIL(String MSG_DETAIL) {
        this.MSG_DETAIL = MSG_DETAIL;
    }

    public String getVALID_DATE() {
        return VALID_DATE;
    }

    public void setVALID_DATE(String VALID_DATE) {
        this.VALID_DATE = VALID_DATE;
    }

    public String getINVALID_DATE() {
        return INVALID_DATE;
    }

    public void setINVALID_DATE(String INVALID_DATE) {
        this.INVALID_DATE = INVALID_DATE;
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

    @Override
    public String toString() {
        return "MessageBean{" +
                "MSG_ID='" + MSG_ID + '\'' +
                ", FACTORY_NO='" + FACTORY_NO + '\'' +
                ", MSG_TYPE='" + MSG_TYPE + '\'' +
                ", MSG_TITLE='" + MSG_TITLE + '\'' +
                ", MSG_DETAIL='" + MSG_DETAIL + '\'' +
                ", VALID_DATE='" + VALID_DATE + '\'' +
                ", INVALID_DATE='" + INVALID_DATE + '\'' +
                ", TYPE_NAME='" + TYPE_NAME + '\'' +
                ", RGB='" + RGB + '\'' +
                '}';
    }
}
