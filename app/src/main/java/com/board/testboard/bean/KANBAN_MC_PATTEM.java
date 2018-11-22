package com.board.testboard.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 看板模板
 */
@Table(name="KANBAN_MC_PATTEM")
public class KANBAN_MC_PATTEM {

    @Id(column = "PATTEM_NO")
    private String PATTEM_NO;

    @Column(column = "KANBAN_MC_NO")
    private String KANBAN_MC_NO;

    @Column(column = "DISPLAY_LANGUAGE")
    private String DISPLAY_LANGUAGE;

    @Column(column = "DISPLAY_TIME")
    private String DISPLAY_TIME;

    @Column(column = "INTERNAL_CODE")
    private String INTERNAL_CODE;//模板内码

    @Column(column = "HEARTBEAT_TIME")
    private String HEARTBEAT_TIME;

    @Column(column = "REFRESH_TIME")
    private String REFRESH_TIME;

    @Column(column = "FACTORY_NO")
    private String FACTORY_NO;

    @Column(column = "IP")
    private String IP;

    @Column(column = "MAC")
    private String MAC;

    @Column(column = "MC_NAME")
    private String MC_NAME;

    @Column(column = "SORTID")
    private String SORTID;

    @Column(column = "STATUS")
    private String STATUS;

    @Column(column = "NOTE")
    private String NOTE;

    @Column(column = "CREATE_USERID")
    private String CREATE_USERID;

    @Column(column = "CREATE_DATE")
    private String CREATE_DATE;

    @Column(column = "CREATE_IP")
    private String CREATE_IP;

    @Column(column = "UPDATE_USERID")
    private String UPDATE_USERID;

    @Column(column = "UPDATE_DATE")
    private String UPDATE_DATE;

    @Column(column = "UPDATE_IP")
    private String UPDATE_IP;

    public String getPATTEM_NO() {
        return PATTEM_NO;
    }

    public void setPATTEM_NO(String PATTEM_NO) {
        this.PATTEM_NO = PATTEM_NO;
    }

    public String getKANBAN_MC_NO() {
        return KANBAN_MC_NO;
    }

    public void setKANBAN_MC_NO(String KANBAN_MC_NO) {
        this.KANBAN_MC_NO = KANBAN_MC_NO;
    }

    public String getDISPLAY_LANGUAGE() {
        return DISPLAY_LANGUAGE;
    }

    public void setDISPLAY_LANGUAGE(String DISPLAY_LANGUAGE) {
        this.DISPLAY_LANGUAGE = DISPLAY_LANGUAGE;
    }

    public String getDISPLAY_TIME() {
        return DISPLAY_TIME;
    }

    public void setDISPLAY_TIME(String DISPLAY_TIME) {
        this.DISPLAY_TIME = DISPLAY_TIME;
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

    public String getFACTORY_NO() {
        return FACTORY_NO;
    }

    public void setFACTORY_NO(String FACTORY_NO) {
        this.FACTORY_NO = FACTORY_NO;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public String getMC_NAME() {
        return MC_NAME;
    }

    public void setMC_NAME(String MC_NAME) {
        this.MC_NAME = MC_NAME;
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

    @Override
    public String toString() {
        return "KANBAN_MC_PATTEM{" +
                "PATTEM_NO='" + PATTEM_NO + '\'' +
                ", KANBAN_MC_NO='" + KANBAN_MC_NO + '\'' +
                ", DISPLAY_LANGUAGE='" + DISPLAY_LANGUAGE + '\'' +
                ", DISPLAY_TIME='" + DISPLAY_TIME + '\'' +
                ", INTERNAL_CODE='" + INTERNAL_CODE + '\'' +
                ", HEARTBEAT_TIME='" + HEARTBEAT_TIME + '\'' +
                ", REFRESH_TIME='" + REFRESH_TIME + '\'' +
                ", FACTORY_NO='" + FACTORY_NO + '\'' +
                ", IP='" + IP + '\'' +
                ", MAC='" + MAC + '\'' +
                ", MC_NAME='" + MC_NAME + '\'' +
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
