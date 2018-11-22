package com.board.testboard.bean;

import java.io.Serializable;

/**  
 * 看板设备代码
 */
public class KANBAN_MC implements Serializable{

    private String BM_ID;
    private String FACTORY_NO;
    private String KANBAN_MC_NO;
    private String MC_NAME;
    private String DISPLAY_LANGUAGE;
    private String MAC;
    private String IP;
    private String HEARTBEAT_TIME;
    private String REFRESH_TIME;
    private String SORTID;
    private String STATUS;
    private String NOTE;
    private String CREATE_USERID;
    private String CREATE_DATE;
    private String CREATE_IP;
    private String UPDATE_USERID;
    private String UPDATE_DATE;
    private String UPDATE_IP;

    public String getBM_ID() {
        return BM_ID;
    }

    public void setBM_ID(String BM_ID) {
        this.BM_ID = BM_ID;
    }

    public String getFACTORY_NO() {
        return FACTORY_NO;
    }

    public void setFACTORY_NO(String FACTORY_NO) {
        this.FACTORY_NO = FACTORY_NO;
    }

    public String getKANBAN_MC_NO() {
        return KANBAN_MC_NO;
    }

    public void setKANBAN_MC_NO(String KANBAN_MC_NO) {
        this.KANBAN_MC_NO = KANBAN_MC_NO;
    }

    public String getMC_NAME() {
        return MC_NAME;
    }

    public void setMC_NAME(String MC_NAME) {
        this.MC_NAME = MC_NAME;
    }

    public String getDISPLAY_LANGUAGE() {
        return DISPLAY_LANGUAGE;
    }

    public void setDISPLAY_LANGUAGE(String DISPLAY_LANGUAGE) {
        this.DISPLAY_LANGUAGE = DISPLAY_LANGUAGE;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getHEARTBEAT_TIME() {
        return HEARTBEAT_TIME;
    }

    public void setHEARTBEAT_TIME(String HEARTBEAT_TIME) {
        this.HEARTBEAT_TIME = HEARTBEAT_TIME;
    }

    public String getREFRESH_TIME() {
        return REFRESH_TIME;
    }

    public void setREFRESH_TIME(String REFRESH_TIME) {
        this.REFRESH_TIME = REFRESH_TIME;
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
        return "KANBAN_MC{" +
                "BM_ID='" + BM_ID + '\'' +
                ", FACTORY_NO='" + FACTORY_NO + '\'' +
                ", KANBAN_MC_NO='" + KANBAN_MC_NO + '\'' +
                ", MC_NAME='" + MC_NAME + '\'' +
                ", DISPLAY_LANGUAGE='" + DISPLAY_LANGUAGE + '\'' +
                ", MAC='" + MAC + '\'' +
                ", IP='" + IP + '\'' +
                ", HEARTBEAT_TIME='" + HEARTBEAT_TIME + '\'' +
                ", REFRESH_TIME='" + REFRESH_TIME + '\'' +
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
