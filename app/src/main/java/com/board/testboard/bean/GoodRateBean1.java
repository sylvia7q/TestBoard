package com.topcee.kanban.bean;

public class GoodRateBean1 {

    private String LineNo;
    private String PeriodName;
    private String StationNo1;
    private String StationName1;
    private String StationNoOutPutTotalQty1;
    private String StationNoOutPutOkQty1;

    public String getLineNo() {
        return LineNo;
    }

    public void setLineNo(String lineNo) {
        LineNo = lineNo;
    }

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

    @Override
    public String toString() {
        return "GoodRateBean1{" +
                "LineNo='" + LineNo + '\'' +
                ", PeriodName='" + PeriodName + '\'' +
                ", StationNo1='" + StationNo1 + '\'' +
                ", StationName1='" + StationName1 + '\'' +
                ", StationNoOutPutTotalQty1='" + StationNoOutPutTotalQty1 + '\'' +
                ", StationNoOutPutOkQty1='" + StationNoOutPutOkQty1 + '\'' +
                '}';
    }
}
