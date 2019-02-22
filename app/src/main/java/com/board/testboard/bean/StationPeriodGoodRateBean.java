package com.topcee.kanban.bean;

/**
 * 获取当前班次产出信息
 */
public class StationPeriodGoodRateBean {
    private String PeriodName;
    private String StationNo1;
    private String StationName1;
    private String StationNoOutPutTotalQty1;
    private String StationNoOutPutOkQty1;
    private String StationNo2;
    private String StationName2;
    private String StationNoOutPutTotalQty2;
    private String StationNoOutPutOkQty2;

    public String getPeriodName() {
        return PeriodName;
    }

    public void setPeriodName(String periodName) {
        PeriodName = periodName;
    }

    public String getStationNo1() {
        return StationNo1;
    }

    public void setStationNo1(String stationNo1) {
        StationNo1 = stationNo1;
    }

    public String getStationName1() {
        return StationName1;
    }

    public void setStationName1(String stationName1) {
        StationName1 = stationName1;
    }

    public String getStationNoOutPutTotalQty1() {
        return StationNoOutPutTotalQty1;
    }

    public void setStationNoOutPutTotalQty1(String stationNoOutPutTotalQty1) {
        StationNoOutPutTotalQty1 = stationNoOutPutTotalQty1;
    }

    public String getStationNoOutPutOkQty1() {
        return StationNoOutPutOkQty1;
    }

    public void setStationNoOutPutOkQty1(String stationNoOutPutOkQty1) {
        StationNoOutPutOkQty1 = stationNoOutPutOkQty1;
    }

    public String getStationNo2() {
        return StationNo2;
    }

    public void setStationNo2(String stationNo2) {
        StationNo2 = stationNo2;
    }

    public String getStationName2() {
        return StationName2;
    }

    public void setStationName2(String stationName2) {
        StationName2 = stationName2;
    }

    public String getStationNoOutPutTotalQty2() {
        return StationNoOutPutTotalQty2;
    }

    public void setStationNoOutPutTotalQty2(String stationNoOutPutTotalQty2) {
        StationNoOutPutTotalQty2 = stationNoOutPutTotalQty2;
    }

    public String getStationNoOutPutOkQty2() {
        return StationNoOutPutOkQty2;
    }

    public void setStationNoOutPutOkQty2(String stationNoOutPutOkQty2) {
        StationNoOutPutOkQty2 = stationNoOutPutOkQty2;
    }

    @Override
    public String toString() {
        return "StationPeriodGoodRateBean{" +
                "PeriodName='" + PeriodName + '\'' +
                ", StationNo1='" + StationNo1 + '\'' +
                ", StationName1='" + StationName1 + '\'' +
                ", StationNoOutPutTotalQty1='" + StationNoOutPutTotalQty1 + '\'' +
                ", StationNoOutPutOkQty1='" + StationNoOutPutOkQty1 + '\'' +
                ", StationNo2='" + StationNo2 + '\'' +
                ", StationName2='" + StationName2 + '\'' +
                ", StationNoOutPutTotalQty2='" + StationNoOutPutTotalQty2 + '\'' +
                ", StationNoOutPutOkQty2='" + StationNoOutPutOkQty2 + '\'' +
                '}';
    }
}
