package com.board.testboard.app;

public class ConstantsEnum {

    /**
     * 模板
     */
    public enum Template
    {
        LINE_TEMPLATE,
        WORKSHOP_TEMPLATE,
    }

    /**
     * 模块
     */
    public enum Module {

        KANBAN_MC_INFO ,//看板设备信息模块
        KANBAN_MC_PATTEM_INFO ,//看板模板信息模块
        COPYRIGHT_INFO ,//版权信息模块
        CURRENT_TIME ,//当前时间模块
        REFRESH_TIME ,//最近刷新时间模块
        KANBAN_NOTICE ,//公告信息模块
        LINE_INFO ,//生产线信息模块
        STATION_INFO ,//工站信息模块
        TERMINAL_INFO ,//工位信息模块
        BAS_LINE_INFO,//线别信息模块
        BAS_WORKSHOP_INFO,//车间信息模块
        BAS_STATION_INFO,//工站信息模块
        BAS_TERMINAL_INFO,//工位信息模块
        WO_INFO ,//生产线信息模块
        DUTY_PERSON_INFO ,//当班员工信息(线看板)模块
        PRODUCTION_INFO ,//生产信息(线看板)模块
        TIME_PRODUCTION_INFO ,//时段产量(线看板)模块
        OWE_PART_WARNING_INFO ,//欠料预警(线看板)模块
        PRODUCTION_WORKSHOP_INFO ,//生产车间信息(车间看板)模块
        LINE_PRODUCTION_INFO //线别产能信息(车间看板)模块
    }
}
