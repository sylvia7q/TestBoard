package com.board.testboard.app;

import android.app.Application;
import android.graphics.Color;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * [全局变量]
 * Created by longq
 * 2018/4/10.
 */

public class MyApplication extends Application{

    public static String sServiceBese = ""; //IP+端口
    public static String sUrl = ""; //WebService地址
    public static String sDownloadUrl = ""; //APK下载地址
    public static String sApkName = ""; //APK名称

    public static String sMesUser = ""; //客户编码(客户代码)
    public static String sFactoryNo = "0"; //工厂代码：0
    public static String sLanguage = "S"; //(默认S)语言：S/T/E
    public static String sClientIp = "127.0.0.1"; //IP地址
    public static String sMac = "0"; //MAC地址

    public static String sCurrentUserNo = "";//验证权限账号
    public static String sUserNo = ""; //用户登陆账号
    public static String sUserName = ""; //用户登陆名称

    public static boolean isBack = false;

    public static String sKanBanMCNo = ""; //看板设备代码
    public static HashMap<Integer, Boolean> kanBanNoStates = new HashMap<Integer, Boolean>();  //在这里要做判断保证只有一个RadioButton被选中
    public static String sLineNo = ""; //线别
    public static String sMoreLineNo = ""; //多线别
    public static String sStation = ""; //工站
    public static Integer nLineDisplayTime = 30; //线别默认展示时长

    public static Integer nDefaultChartHourBgColor = Color.rgb(79,129,188);//时段产量背景颜色
    public static Integer nDefaultChartHourColor1 = Color.rgb(79,129,188);//时段产量投入数
    public static Integer nDefaultChartHourColor2 = Color.rgb(192,80,78);//时段产量产出数
    public static Integer nDefaultChartWarnBgColor = Color.rgb(252,175,59);//欠料预警背景颜色
    public static Integer nDefaultChartWarnColor = Color.rgb(252,175,59);//欠料预警
    public static Integer nDefaultChartLineProductBgColor = Color.rgb(90,155,213);//线别产能背景颜色
    public static Integer nDefaultChartLineProductColor1 = Color.rgb(90,155,213);//线别产能投入数
    public static Integer nDefaultChartLineProductColor2 = Color.rgb(254,0,0);//线别产能产出数

    public static Integer nDefaultSMTLineTitleColor = Color.rgb(255,255,255);//线看板标题栏字体颜色
    public static Integer nDefaultSMTLineOtherTextColor = Color.rgb(255,0,0);//线看板其他字体颜色
    public static Integer nDefaultSMTLineWholeBgColor = Color.rgb(221,18,123);//线看板整个背景颜色
    public static Integer nDefaultSMTLineChartBgColor = Color.rgb(255,255,255);//线看板图控件背景颜色


    public static Integer nDefaultSMTTotalTitleColor = Color.rgb(255,255,255);//车间看板标题颜色
    public static Integer nDefaultSMTTotalOtherTextColor = Color.rgb(255,255,255);//车间看板其他字体颜色
    public static Integer nDefaultSMTTotalWholeBgColor = Color.rgb(221,18,123);//车间看板整个背景颜色
    public static Integer nDefaultSMTTotalChartBgColor = Color.rgb(255,255,255);//车间看板图表控件背景颜色

    public static Integer nMsgErrorColor = Color.rgb(255,0,0);
    public static Integer nMsgAlertColor = Color.rgb(0,255,0);
    public static Integer nMsgInfoColor = Color.rgb(0,0,255);
    public static String sMsgErrorTypeName = "";
    public static String sMsgAlertTypeName = "";
    public static String sMsgInfoTypeName = "";
}
