package com.topcee.kanban.bean;

/**
 * 获取停机信息
 */
public class FeedAlarmInfoBean {
    private String SCS_ID; //停机现象数据ID
    private String STOP_TYPE; //停机类型代码
    private String STOP_TYPE_NAME; //停机类型名称
    private String LINE_NO; //线别
    private String WO; //制令单
    private String MACHINE_SORTID; //设备序号
    private String PLAN_STOP_TIME; //停机队列数据-实际停机时间
    private String MSG_ID_STOP; //停机指令ID(id > 0 ,时间为NULL,代表已经停机; id > 0 ,时间为不为空,代表到达该时间后，会执行停机指令; id = 0，没有停机指令，只有停机队列信息)
    private String MACHINE_NO; //设备代码
    private String FID; //站位
    private String REEL_NO; //料卷号
    private String STOP_REASON;//停机详细信息
    private String CREATE_USERID; //创建人员
    private String CREATE_DATE; //创建时间
    private String IS_MACHINE_STOP; //设备是否停机(Y/N)

    public String getSCS_ID() {
        return SCS_ID;
    }

    public void setSCS_ID(String SCS_ID) {
        this.SCS_ID = SCS_ID;
    }

    public String getSTOP_TYPE() {
        return STOP_TYPE;
    }

    public void setSTOP_TYPE(String STOP_TYPE) {
        this.STOP_TYPE = STOP_TYPE;
    }

    public String getSTOP_TYPE_NAME() {
        return STOP_TYPE_NAME;
    }

    public void setSTOP_TYPE_NAME(String STOP_TYPE_NAME) {
        this.STOP_TYPE_NAME = STOP_TYPE_NAME;
    }

    public String getLINE_NO() {
        return LINE_NO;
    }

    public void setLINE_NO(String LINE_NO) {
        this.LINE_NO = LINE_NO;
    }

    public String getWO() {
        return WO;
    }

    public void setWO(String WO) {
        this.WO = WO;
    }

    public String getMACHINE_SORTID() {
        return MACHINE_SORTID;
    }

    public void setMACHINE_SORTID(String MACHINE_SORTID) {
        this.MACHINE_SORTID = MACHINE_SORTID;
    }

    public String getPLAN_STOP_TIME() {
        return PLAN_STOP_TIME;
    }

    public void setPLAN_STOP_TIME(String PLAN_STOP_TIME) {
        this.PLAN_STOP_TIME = PLAN_STOP_TIME;
    }

    public String getMSG_ID_STOP() {
        return MSG_ID_STOP;
    }

    public void setMSG_ID_STOP(String MSG_ID_STOP) {
        this.MSG_ID_STOP = MSG_ID_STOP;
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

    public String getREEL_NO() {
        return REEL_NO;
    }

    public void setREEL_NO(String REEL_NO) {
        this.REEL_NO = REEL_NO;
    }

    public String getSTOP_REASON() {
        return STOP_REASON;
    }

    public void setSTOP_REASON(String STOP_REASON) {
        this.STOP_REASON = STOP_REASON;
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

    public String getIS_MACHINE_STOP() {
        return IS_MACHINE_STOP;
    }

    public void setIS_MACHINE_STOP(String IS_MACHINE_STOP) {
        this.IS_MACHINE_STOP = IS_MACHINE_STOP;
    }

    @Override
    public String toString() {
        return "FeedAlarmInfoBean{" +
                "SCS_ID='" + SCS_ID + '\'' +
                ", STOP_TYPE='" + STOP_TYPE + '\'' +
                ", STOP_TYPE_NAME='" + STOP_TYPE_NAME + '\'' +
                ", LINE_NO='" + LINE_NO + '\'' +
                ", WO='" + WO + '\'' +
                ", MACHINE_SORTID='" + MACHINE_SORTID + '\'' +
                ", PLAN_STOP_TIME='" + PLAN_STOP_TIME + '\'' +
                ", MSG_ID_STOP='" + MSG_ID_STOP + '\'' +
                ", MACHINE_NO='" + MACHINE_NO + '\'' +
                ", FID='" + FID + '\'' +
                ", REEL_NO='" + REEL_NO + '\'' +
                ", STOP_REASON='" + STOP_REASON + '\'' +
                ", CREATE_USERID='" + CREATE_USERID + '\'' +
                ", CREATE_DATE='" + CREATE_DATE + '\'' +
                ", IS_MACHINE_STOP='" + IS_MACHINE_STOP + '\'' +
                '}';
    }
}
