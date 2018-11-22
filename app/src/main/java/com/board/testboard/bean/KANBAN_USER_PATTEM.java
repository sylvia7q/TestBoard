package com.board.testboard.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 用户模板
 */
@Table(name="KANBAN_USER_PATTEM")
public class KANBAN_USER_PATTEM {

    @Id(column = "PATTEM_NO")
    private String PATTEM_NO;

    @Column(column = "KUP_ID")
    private String KUP_ID;

    @Column(column = "FACTORY_NO")
    private String FACTORY_NO;

    @Column(column = "PATTEM_NAME")
    private String PATTEM_NAME;//看板名字

    @Column(column = "PATTEM_TITLE")
    private String PATTEM_TITLE;//看板标题


    @Column(column = "INTERNAL_CODE")
    private String INTERNAL_CODE;//模板内码

    @Column(column = "KANBAN_TYPE")
    private String KANBAN_TYPE;//模板类别

    @Column(column = "IS_CUSTOMIZE")
    private String IS_CUSTOMIZE;//为客制看板

    @Column(column = "HEARTBEAT_INTEVAL")
    private String HEARTBEAT_INTEVAL;//心跳间隔(秒)

    @Column(column = "IS_DISP_COPYRIGHT")
    private String IS_DISP_COPYRIGHT;//显示版权信息?

    @Column(column = "IS_DISP_CURRENTTIME")
    private String IS_DISP_CURRENTTIME;//显示当前时间?

    @Column(column = "IS_DISP_REFRESH_TIME")
    private String IS_DISP_REFRESH_TIME;//显示最后刷新时间?

    @Column(column = "IS_DISP_MESSAGE")
    private String IS_DISP_MESSAGE;//显示公告消息?

    @Column(column = "SORTID")
    private String SORTID;//序号

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

    public String getKUP_ID() {
        return KUP_ID;
    }

    public void setKUP_ID(String KUP_ID) {
        this.KUP_ID = KUP_ID;
    }

    public String getFACTORY_NO() {
        return FACTORY_NO;
    }

    public void setFACTORY_NO(String FACTORY_NO) {
        this.FACTORY_NO = FACTORY_NO;
    }

    public String getPATTEM_NAME() {
        return PATTEM_NAME;
    }

    public void setPATTEM_NAME(String PATTEM_NAME) {
        this.PATTEM_NAME = PATTEM_NAME;
    }

    public String getPATTEM_TITLE() {
        return PATTEM_TITLE;
    }

    public void setPATTEM_TITLE(String PATTEM_TITLE) {
        this.PATTEM_TITLE = PATTEM_TITLE;
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

    public String getHEARTBEAT_INTEVAL() {
        return HEARTBEAT_INTEVAL;
    }

    public void setHEARTBEAT_INTEVAL(String HEARTBEAT_INTEVAL) {
        this.HEARTBEAT_INTEVAL = HEARTBEAT_INTEVAL;
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
        return "KANBAN_USER_PATTEM{" +
                "PATTEM_NO='" + PATTEM_NO + '\'' +
                ", KUP_ID='" + KUP_ID + '\'' +
                ", FACTORY_NO='" + FACTORY_NO + '\'' +
                ", PATTEM_NAME='" + PATTEM_NAME + '\'' +
                ", PATTEM_TITLE='" + PATTEM_TITLE + '\'' +
                ", INTERNAL_CODE='" + INTERNAL_CODE + '\'' +
                ", KANBAN_TYPE='" + KANBAN_TYPE + '\'' +
                ", IS_CUSTOMIZE='" + IS_CUSTOMIZE + '\'' +
                ", HEARTBEAT_INTEVAL='" + HEARTBEAT_INTEVAL + '\'' +
                ", IS_DISP_COPYRIGHT='" + IS_DISP_COPYRIGHT + '\'' +
                ", IS_DISP_CURRENTTIME='" + IS_DISP_CURRENTTIME + '\'' +
                ", IS_DISP_REFRESH_TIME='" + IS_DISP_REFRESH_TIME + '\'' +
                ", IS_DISP_MESSAGE='" + IS_DISP_MESSAGE + '\'' +
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
