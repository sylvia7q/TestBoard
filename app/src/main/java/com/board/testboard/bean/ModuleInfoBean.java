package com.board.testboard.bean;

/**
 * 模块信息
 */
public class ModuleInfoBean {  

    private String BSPM_ID;
    private String FACTORY_NO;
    private String PATTEM_NO;
    private String MODULE_NO;
    private String MODULE_NAME;
    private String PATTEM_NAME;
    private String TITLE_NAME;
    private String INTERFACE_INTERNAL_CODE;
    private String HEARTBEAT_INTEVAL;
    private String SORTID;
    private String STATUS;
    private String NOTE;
    private String CREATE_USERID;
    private String CREATE_DATE;
    private String CREATE_IP;
    private String UPDATE_USERID;
    private String UPDATE_DATE;
    private String UPDATE_IP;
    private String INTERNAL_CODE;
    private String KANBAN_TYPE;
    private String IS_CUSTOMIZE;
    private String PATTEM_HEARTBEAT_INTEVAL;
    private String IS_DISP_COPYRIGHT;
    private String IS_DISP_CURRENTTIME;
    private String IS_DISP_REFRESH_TIME;
    private String IS_DISP_MESSAGE;

    public String getBSPM_ID() {
        return BSPM_ID;
    }

    public void setBSPM_ID(String BSPM_ID) {
        this.BSPM_ID = BSPM_ID;
    }

    public String getFACTORY_NO() {
        return FACTORY_NO;
    }

    public void setFACTORY_NO(String FACTORY_NO) {
        this.FACTORY_NO = FACTORY_NO;
    }

    public String getPATTEM_NO() {
        return PATTEM_NO;
    }

    public void setPATTEM_NO(String PATTEM_NO) {
        this.PATTEM_NO = PATTEM_NO;
    }

    public String getMODULE_NO() {
        return MODULE_NO;
    }

    public void setMODULE_NO(String MODULE_NO) {
        this.MODULE_NO = MODULE_NO;
    }

    public String getMODULE_NAME() {
        return MODULE_NAME;
    }

    public void setMODULE_NAME(String MODULE_NAME) {
        this.MODULE_NAME = MODULE_NAME;
    }

    public String getPATTEM_NAME() {
        return PATTEM_NAME;
    }

    public void setPATTEM_NAME(String PATTEM_NAME) {
        this.PATTEM_NAME = PATTEM_NAME;
    }

    public String getTITLE_NAME() {
        return TITLE_NAME;
    }

    public void setTITLE_NAME(String TITLE_NAME) {
        this.TITLE_NAME = TITLE_NAME;
    }

    public String getINTERFACE_INTERNAL_CODE() {
        return INTERFACE_INTERNAL_CODE;
    }

    public void setINTERFACE_INTERNAL_CODE(String INTERFACE_INTERNAL_CODE) {
        this.INTERFACE_INTERNAL_CODE = INTERFACE_INTERNAL_CODE;
    }

    public String getHEARTBEAT_INTEVAL() {
        return HEARTBEAT_INTEVAL;
    }

    public void setHEARTBEAT_INTEVAL(String HEARTBEAT_INTEVAL) {
        this.HEARTBEAT_INTEVAL = HEARTBEAT_INTEVAL;
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

    public String getINTERNAL_CODE() {
        return INTERNAL_CODE;
    }

    public void setINTERNAL_CODE(String INTERNAL_CODE) {
        this.INTERNAL_CODE = INTERNAL_CODE;
    }

    public String getKANBAN_TYPE() {
        return KANBAN_TYPE;
    }

    public void setKANBAN_TYPE(String KANBAN_TYPE) {
        this.KANBAN_TYPE = KANBAN_TYPE;
    }

    public String getIS_CUSTOMIZE() {
        return IS_CUSTOMIZE;
    }

    public void setIS_CUSTOMIZE(String IS_CUSTOMIZE) {
        this.IS_CUSTOMIZE = IS_CUSTOMIZE;
    }

    public String getPATTEM_HEARTBEAT_INTEVAL() {
        return PATTEM_HEARTBEAT_INTEVAL;
    }

    public void setPATTEM_HEARTBEAT_INTEVAL(String PATTEM_HEARTBEAT_INTEVAL) {
        this.PATTEM_HEARTBEAT_INTEVAL = PATTEM_HEARTBEAT_INTEVAL;
    }

    public String getIS_DISP_COPYRIGHT() {
        return IS_DISP_COPYRIGHT;
    }

    public void setIS_DISP_COPYRIGHT(String IS_DISP_COPYRIGHT) {
        this.IS_DISP_COPYRIGHT = IS_DISP_COPYRIGHT;
    }

    public String getIS_DISP_CURRENTTIME() {
        return IS_DISP_CURRENTTIME;
    }

    public void setIS_DISP_CURRENTTIME(String IS_DISP_CURRENTTIME) {
        this.IS_DISP_CURRENTTIME = IS_DISP_CURRENTTIME;
    }

    public String getIS_DISP_REFRESH_TIME() {
        return IS_DISP_REFRESH_TIME;
    }

    public void setIS_DISP_REFRESH_TIME(String IS_DISP_REFRESH_TIME) {
        this.IS_DISP_REFRESH_TIME = IS_DISP_REFRESH_TIME;
    }

    public String getIS_DISP_MESSAGE() {
        return IS_DISP_MESSAGE;
    }

    public void setIS_DISP_MESSAGE(String IS_DISP_MESSAGE) {
        this.IS_DISP_MESSAGE = IS_DISP_MESSAGE;
    }

    @Override
    public String toString() {
        return "ModuleInfoBean{" +
                "BSPM_ID='" + BSPM_ID + '\'' +
                ", FACTORY_NO='" + FACTORY_NO + '\'' +
                ", PATTEM_NO='" + PATTEM_NO + '\'' +
                ", MODULE_NO='" + MODULE_NO + '\'' +
                ", MODULE_NAME='" + MODULE_NAME + '\'' +
                ", PATTEM_NAME='" + PATTEM_NAME + '\'' +
                ", TITLE_NAME='" + TITLE_NAME + '\'' +
                ", INTERFACE_INTERNAL_CODE='" + INTERFACE_INTERNAL_CODE + '\'' +
                ", HEARTBEAT_INTEVAL='" + HEARTBEAT_INTEVAL + '\'' +
                ", SORTID='" + SORTID + '\'' +
                ", STATUS='" + STATUS + '\'' +
                ", NOTE='" + NOTE + '\'' +
                ", CREATE_USERID='" + CREATE_USERID + '\'' +
                ", CREATE_DATE='" + CREATE_DATE + '\'' +
                ", CREATE_IP='" + CREATE_IP + '\'' +
                ", UPDATE_USERID='" + UPDATE_USERID + '\'' +
                ", UPDATE_DATE='" + UPDATE_DATE + '\'' +
                ", UPDATE_IP='" + UPDATE_IP + '\'' +
                ", INTERNAL_CODE='" + INTERNAL_CODE + '\'' +
                ", KANBAN_TYPE='" + KANBAN_TYPE + '\'' +
                ", IS_CUSTOMIZE='" + IS_CUSTOMIZE + '\'' +
                ", PATTEM_HEARTBEAT_INTEVAL='" + PATTEM_HEARTBEAT_INTEVAL + '\'' +
                ", IS_DISP_COPYRIGHT='" + IS_DISP_COPYRIGHT + '\'' +
                ", IS_DISP_CURRENTTIME='" + IS_DISP_CURRENTTIME + '\'' +
                ", IS_DISP_REFRESH_TIME='" + IS_DISP_REFRESH_TIME + '\'' +
                ", IS_DISP_MESSAGE='" + IS_DISP_MESSAGE + '\'' +
                '}';
    }
}
