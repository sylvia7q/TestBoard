package com.topcee.kanban.bean;

public class GoodRateBean2 {

    private String LineNo;
    private String PeriodName;
    private String StationNo2;
    private String StationName2;
    private String StationNoOutPutTotalQty2;
    private String StationNoOutPutOkQty2;

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
        return "GoodRateBean2{" +
                "LineNo='" + LineNo + '\'' +
                ", PeriodName='" + PeriodName + '\'' +
                ", StationNo2='" + StationNo2 + '\'' +
                ", StationName2='" + StationName2 + '\'' +
                ", StationNoOutPutTotalQty2='" + StationNoOutPutTotalQty2 + '\'' +
                ", StationNoOutPutOkQty2='" + StationNoOutPutOkQty2 + '\'' +
                '}';
    }
}
