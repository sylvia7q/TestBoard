package com.board.testboard.ui.activity.pattem;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.topcee.kanban.R;
import com.topcee.kanban.app.MyApplication;
import com.topcee.kanban.bean.EmployeeBean;
import com.topcee.kanban.bean.EmployeeNewBean;
import com.topcee.kanban.bean.KANBAN_MC_PATTEM;
import com.topcee.kanban.bean.KANBAN_USER_PATTEM;
import com.topcee.kanban.bean.MessageAttrBean;
import com.topcee.kanban.bean.MessageBean;
import com.topcee.kanban.bean.ModuleInfoBean;
import com.topcee.kanban.bean.OperatingRateBean;
import com.topcee.kanban.bean.PeriodOutputBean;
import com.topcee.kanban.bean.SMT_BOARD_LINE;
import com.topcee.kanban.bean.ShiftBean;
import com.topcee.kanban.bean.WarnBean;
import com.topcee.kanban.bean.WoBean;
import com.topcee.kanban.bean.WoProductionBean;
import com.topcee.kanban.database.DBUtil;
import com.topcee.kanban.http.SMTLineAlarmReceiver;
import com.topcee.kanban.http.SMTLineAlarmService;
import com.topcee.kanban.presenter.OnGetDataListener;
import com.topcee.kanban.presenter.OnGetSoapObjectListener;
import com.topcee.kanban.presenter.OnRequestListener;
import com.topcee.kanban.presenter.OnResultListener;
import com.topcee.kanban.ui.activity.base.module.SMTLineSettingActivity;
import com.topcee.kanban.ui.activity.base.set.SettingSMTLineBoardActivity;
import com.topcee.kanban.ui.view.module.BarChartOweMaterialWarning;
import com.topcee.kanban.ui.view.module.EmployeeViewV2;
import com.topcee.kanban.ui.view.module.MarqueeView;
import com.topcee.kanban.ui.view.module.MultiaxialTimeProduction;
import com.topcee.kanban.ui.view.module.OperatingRatePieChartView;
import com.topcee.kanban.ui.view.module.WoView;
import com.topcee.kanban.utils.AlarmManagerUtil;
import com.topcee.kanban.utils.AppUtil;
import com.topcee.kanban.utils.DateUtils;
import com.topcee.kanban.utils.GsonUtils;
import com.topcee.kanban.utils.LogUtil;
import com.topcee.kanban.utils.SharedPreferencesUtil;
import com.topcee.kanban.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

/**
 * 线看板模板[现在在用的客户：TCL]
 * 包含制令单信息模块，制令单产出信息模块，人员岗位信息模块，设备运转率模块，时段产量模块，欠料预警模块，班次信息模块，同步当前时间模块，各个模块信息
 * 需要选择线别，设置线别展示时长
 */
public class SMTLineBoardV2Activity extends BasePattemActivity {

    @BindView(R.id.activity_line_board_view_wo)
    WoView viewWo;//制令单信息

    @BindView(R.id.activity_line_board_view_employee_v2)
    EmployeeViewV2 viewEmployee;//员工信息

    @BindView(R.id.activity_line_board_view_time_production)
    MultiaxialTimeProduction viewTimeProduction;//时段产量

    @BindView(R.id.activity_line_board_view_warning)
    BarChartOweMaterialWarning viewWarning;//欠料预警

    @BindView(R.id.activity_line_board_view_operating_rate)
    OperatingRatePieChartView viewOperatingRate;



    private final String TAG = SMTLineBoardV2Activity.class.getSimpleName();
    private Context context;
    //线别信息
    private String sLineNo = ""; //SMT2-2 1B
    private String sLineNameEn = ""; //SMT2-2 1B
    private String sLineNoIn = "";//选择的所有线别
    private int lineIndex = 0;//线别展示顺序
    private String workshopNo = "";//车间代码
    private long lastLineRefreshTime = 0l;
    private long curLineRefreshTime = 0l;
    private List<SMT_BOARD_LINE> moreLines;

    private String disCurTime = "";//显示当前时间
    private String disReTime = "";//显示最后刷新时间
    private String disCopyright = "";//显示版权信息
    private String disMessage = "";//显示公告

    private String sCustomer; //客户
    private String sProductNo; //产品号
    private String sWoPlanQty; //制令单计划数量
    private String sWoQtyOutput; //制令单产出数量
    private String sFinishingRate; //制令单完成率
    private String sCurShiftTotalQty; //当前班次总产量(所有线别产量之和)
    private String sProductionSchedule; //生产进度（制令单产出数量/制令单计划数量）
    private String sWo = ""; //制令单
    //颜色信息
    private int nSMTLineTitleColor;//标题字体颜色
    private int nSMTLineWoTitleColor;//制令单标题字体颜色
    private int nSMTLineWoContentColor;//制令单内容字体颜色
    private int nSMTLineEmployeeColor;//人员字体颜色
    private int nSMTLineRoleColor;//岗位字体颜色
    private int nSMTLineOtherTextColor;//其它字体颜色
    private int nSMTLineChartBgColor;//图表背景颜色
    private int nSMTLineWholeBgColor;//背景颜色
    private Integer nChartHourColor[];//图表时段产量
    private int nChartWarnColor;//图表欠料预警
    private int nChartOperatingRateColor;
    //模块信息
    private List<WoBean> woBeanList;//制令单信息
    private List<WoProductionBean> woProductionBeanList;//制令单产出信息
    private List<EmployeeBean> employeeBeanList;//人员岗位
    private List<PeriodOutputBean> periodOutputList;//时段产量
    private List<WarnBean> warningList;//欠料预警
    private String sChartWarnTitle = "";
    private String sChartWarnLabel = "";
    private List<ShiftBean> shiftList;
    private List<ModuleInfoBean> moduleList;
    private int warningPercent = 10;//欠料预警可用比例
    private int warningStation = 12;//欠料预警显示工站数
    private List<MessageBean> messageList;//公告消息列表
    private List<MessageBean> curMessageList;//公告消息列表
    private List<MessageAttrBean> messageAttrBeanList;//公告消息属性列表
    private boolean messageFlag = true;
    private boolean messageStopFlag = false;
    private MarqueeView scrollTextView;
    private int scrollIndex = 0;
    private String curScrollText = "";
    private int bulletinTextColor = Color.BLACK;
    private int bulletinBgColor = Color.WHITE;
    private List<OperatingRateBean> operatingRateBeans;
    private String sOperatingRateTitle = "";
    private String sOperatingRatePlotTitle = "";


    private String isAutoPattem = "N";//是否自动展示模板，true  为是， false 为否
    private static int mTaskId = 0;// 模拟的task id
    //展示模板
    private List<KANBAN_MC_PATTEM> allMcPattems;
    private KANBAN_MC_PATTEM mc_pattem;
    private String pattemNo = "";
    private int mcIndex = 1;
    private String kanbanMcNo = "";//看板设备代码
    private String sSMTLineLanguage = "S";//当前模板展示语言
    private String curDisplayLanguage = "S";//当前模板展示语言
    private long nSMTLineMcPattemLastRefreshTime = 0l;//上次模板展示时间
    private long mcPattemCurRefreshTime = 0l;//当前模板展示时间
    private long pattemHeartbeatInteval = 1l;
    //用户模板
    private KANBAN_USER_PATTEM user_pattem;

    private String smtTitleText = "";
    private String time = "";
    private String curTime = "";
    private String refreshTime = "";
    private boolean isStopReceiver = true; //定义跳转页面后是否执行-是否获取数据，true 执行 / false 不执行
    private boolean isGetStopData = false; //是否获取数据

    //模块刷新时间间隔
    private long nSMTLineWoLastRefreshTime = 0l;//制令单信息上次刷新时间
    private long nSMTLineWoHeartbeatInteval = 0l;//制令单刷新时间间隔
    private long nSMTLineWoProductionLastRefreshTime = 0l;//制令单上次产出信息刷新时间
    private long nSMTLineWoProductionHeartbeatInteval = 0l;//制令单产出信息时间间隔
    private long nSMTLineEmployeeLastRefreshTime = 0l;//人员岗位信息上次刷新时间
    private long nSMTLineEmployeeHeartbeatInteval = 0l;//人员岗位信息刷新时间间隔
    private long nSMTLineWarnLastRefreshTime = 0l;//欠料预警上次刷新时间
    private long nSMTLineWarnHeartbeatInteval = 0l;//欠料预警刷新时间间隔
    private long nSMTLinePeriodLastRefreshTime = 0l;//时段测量上次刷新时间
    private long nSMTLinePeriodHeartbeatInteval = 0l;//时段测量刷新时间间隔
    private long nSMTLineMessageLastRefreshTime = 0l;//公告消息上次刷新时间
    private long nSMTLineMessageHeartbeatInteval = 0l;//公告消息刷新时间间隔
    private long nSMTLineShiftLastRefreshTime = 0l;//班次上次刷新时间
    private long nSMTLineShiftHeartbeatInteval = 0l;//班次刷新时间间隔
    private long nSMTLineTimeLastRefreshTime = 0l;//同步时间模块上次刷新时间
    private long nSMTLineTimeHeartbeatInteval = 0l;//同步时间模块刷新时间间隔
    private long nSMTLineOperatingRateLastRefreshTime = 0l;//同步时间模块刷新时间间隔
    private long nSMTLineOperatingRateHeartbeatInteval = 0l;//同步时间模块刷新时间间隔
    private long nSMTLineCurRefreshTime = 0l;//当前时间

    private boolean blIsInitData = true;
    private boolean blIsInitWarnData = true;
    private boolean woTimeRefresh = false;//制令单刷新时间
    private boolean woProductionTimeRefresh = false;//制令单产出刷新时间
    private boolean personTimeRefresh = false;//人员岗位刷新时间
    private boolean warnTimeRefresh = false;//欠料预警刷新时间
    private boolean periodTimeRefresh = false;//时段测量刷新时间
    private boolean operateRateTimeRefresh = false;//设备运转率刷新时间
    private boolean shiftTimeRefresh = false;//班次刷新时间
    private boolean messageTimeRefresh = false;//消息刷新时间
    
    private Timer timer;
    private TimerTask timerTask;
    private Toast toast;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (!TextUtils.isEmpty((String) msg.obj)) {
                        setCurrentDate((String) msg.obj);
                    } else {
                        setCurrentDate(DateUtils.longToDate(System.currentTimeMillis()));
                    }
                    break;
                case 2:
                    getServerDate();//同步服务器时间
                    break;
                case 3:
                    setCurrentDate((String) msg.obj);
                    break;
                case 4:
                    if (blIsInitWarnData || warnTimeRefresh) {
                        blIsInitWarnData = false;
                        if (!TextUtils.isEmpty(sWo)) {
                            getWarnInfo(sWo);
                        } else {
                            List<WarnBean> tempWarns = new ArrayList<>();
                            for (int i = 0; i < 2; i++) {
                                WarnBean warnBean = new WarnBean();
                                warnBean.setMACHINE_SEQ("warn" + i);
                                warnBean.setNEED_PERCENT("0");
                                tempWarns.add(warnBean);
                            }
                            //显示-欠料预警
                            showBarChartOweMaterialWarning(viewWarning,tempWarns, nChartWarnColor, sChartWarnTitle, sChartWarnLabel);
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e(TAG + "onCreate");
        context = SMTLineBoardV2Activity.this;

    }

    @Override
    protected int getModuleLayoutId() {
        return R.layout.activity_smt_line_board_v2;
    }

    @Override
    protected void initUI() {
        initMenuTitleView();
    }

    /**
     * 初始化菜单标题
     */
    private void initMenuTitleView() {
        menuTitle.setCommonTitle(View.GONE, View.GONE, View.GONE, View.VISIBLE);
        menuTitle.setBtnRight(R.drawable.icon_gear, 0);
        onLongClick(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (menuTitle.getVisibility() == View.VISIBLE) {
                    menuTitle.setVisibility(View.GONE);
                } else {
                    menuTitle.setVisibility(View.VISIBLE);
                }
                if (menuTitle.getVisibility() == View.VISIBLE) {
                    menuTitle.getBtnRight().requestFocus();
                }
                return true;
            }
        });
        menuTitle.setBtnRightOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(context, SettingSMTLineBoardActivity.class,"Activity","SMTLineBoardV2Activity");
            }
        });
    }

    /**
     * 如果当前模板语言和系统语言不一致，则按模板语言标准来显示
     */
    private void initCurUI() {

        if (getCurLanguage() == 1) {
            viewWo.setCustomTitle("Client");
            viewWo.setWoTitle("Order");
            viewWo.setProductNoTitle("product code");
            viewWo.setWoPlanQtyTitle("Number of orders");
            viewWo.setWoCompletedQtyTitle("Number completed");
//            viewWo.setCurShiftTotalQtyTitle("Current shift total output");
            viewWo.setProductionScheduleTitle("production progress");
            setCompanyText("Copyright©TOPCEE TECHNOLOGIES Co., Ltd.");
            menuTitle.setBtnLeft("back");
            sChartWarnTitle = "Missing Material Warning";
            sChartWarnLabel = "Available (percentage)";
            sOperatingRateTitle = "Equipment operating rate";
            sOperatingRatePlotTitle = "Operating rate";
        } else if (getCurLanguage() == 2) {
            viewWo.setCustomTitle("客戶");
            viewWo.setWoTitle("制令單");
            viewWo.setProductNoTitle("産品號");
            viewWo.setWoPlanQtyTitle("制令單數量");
            viewWo.setWoCompletedQtyTitle("已完成數量");
//            viewWo.setCurShiftTotalQtyTitle("Current shift total output");
            viewWo.setProductionScheduleTitle("生産進度");
            setCompanyText("版權所有©深圳市拓思科技有限公司 www.topcee.com");
            menuTitle.setBtnLeft("返回");
            sChartWarnTitle = "欠料預警";
            sChartWarnLabel = "可用(百分比)";
            sOperatingRateTitle = "設備運轉率";
            sOperatingRatePlotTitle = "運轉率";
        }else {
            sChartWarnTitle = getString(R.string.smt_line_missing_material_warning);
            sChartWarnLabel = getString(R.string.smt_line_percentage_available);
            sOperatingRateTitle = getString(R.string.smt_line_equipment_operating_rate);
            sOperatingRatePlotTitle = getString(R.string.smt_line_operating_rate);
        }
    }

    /**
     * 初始化模块界面
     */
    private void initModuleView(String disCurTime, String disReTime, String disCopyright, String disMessage) {
        if (menuTitle.getVisibility() == View.VISIBLE) {
            menuTitle.setVisibility(View.GONE);
        }
        if (showUI(disCurTime) || showUI(disReTime)) {//当前时间和刷新时间共用一个控件，优先显示当前时间
            setCurrentDateVisibility(View.VISIBLE);
            String getCurTimeByServer = SharedPreferencesUtil.getValue(context, "getCurTimeByServer", "");
            //如果本地设置了从服务器获取当前时间，则每次从接口获取服务器时间，否则就取本地时间
            if (showUI(disReTime) && !showUI(disCurTime)) {//显示刷新时间
                time = "refreshTime";
            }else {//显示当前时间
                if (getCurTimeByServer.equals("Y")) {
                    time = "curTime";
                } else {
                    time = "sysTime";
                }
                startTimer();
            }

        } else {
            time = "";
            setCurrentDateVisibility(View.GONE);
        }
//        disCurTime(disCurTime);
//        disReTime(disReTime);
        disCopyright(disCopyright);
//        disMessage(disMessage);//一开始初始化显示，但是没有数据会显得有些突兀，所以在获取数据之后再显示
    }
    /**
     * 启用定时器
     */
    private void startTimer(){
        if(timer!=null){
            timer.cancel();
        }
        if(timerTask!=null){
            timerTask.cancel();
        }
        timer = new Timer(true);
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (time.equals("curTime")) {
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);
                } else if (time.equals("sysTime")) {
                    Message message = new Message();
                    message.obj = DateUtils.longToDate(System.currentTimeMillis());
                    message.what = 3;
                    handler.sendMessage(message);
                }

            }
        };
        timer.schedule(timerTask,0,1000 );
    }
    /**
     * 停止定时器
     */
    private void stopTimer(){
        timer.cancel();
        timerTask.cancel();
    }
    /**
     * 根据参数觉得决定是否显示UI
     *
     * @param
     * @return
     */
    private void disCurTime(String disCurTime) {
        if (showUI(disCurTime)) {
            setCurrentDateVisibility(View.VISIBLE);
        } else {
            setCurrentDateVisibility(View.GONE);
        }
    }

    /**
     * 根据参数觉得决定是否显示UI
     *
     * @param
     * @return
     */
    private void disReTime(String disReTime) {
        if (showUI(disReTime)) {
            setCurrentDateVisibility(View.VISIBLE);
        } else {
            setCurrentDateVisibility(View.GONE);
        }
    }

    /**
     * 根据参数觉得决定是否显示UI
     *
     * @param
     * @return
     */
    private void disCopyright(String disCopyright) {
        if (showUI(disCopyright)) {
            setCompanyVisibility(View.VISIBLE);
        } else {
            setCompanyVisibility(View.GONE);
        }
    }

    /**
     * 根据参数觉得决定是否显示UI
     *
     * @param
     * @return
     */
    private void disMessage(String disMessage) {
        if (showUI(disMessage)) {
            setVisiblebulletin(View.VISIBLE);
        } else {
            setVisiblebulletin(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.e(TAG + "onStart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.e(TAG + "onResume");
        isAutoPattem = getIntent().getStringExtra("isAutoPattem");
        if (isAutoPattem.equals("N")) {
            lineIndex = 0;
        }
        getLlWholeBg().requestFocus();
        blIsInitData = true;
        blIsInitWarnData = true;
        messageFlag = true;//判断公告消息是否是打开界面第一次获取
        String sKanBanMCNo = SharedPreferencesUtil.getValue(getBaseContext(), "KANBAN_MC_NO", "");
        getPattem(sKanBanMCNo);
    }

    //获取看板模板信息
    public void getPattem(String sKanBanMCNo) {

        getKanBanUserPattem(context, sKanBanMCNo, new OnResultListener() {
            @Override
            public void onSuccess(String sDateNow) {
                final DBUtil dbUtil = new DBUtil(context);
                mc_pattem = dbUtil.getMcPattemByCode(template_code[1]);//更新展示模板
                user_pattem = dbUtil.getUserPattemByCode(template_code[1]);//更新展示模板
                if (user_pattem != null) {
                    pattemNo = user_pattem.getPATTEM_NO();
                    disCurTime = user_pattem.getIS_DISP_CURRENTTIME();
                    disReTime = user_pattem.getIS_DISP_REFRESH_TIME();
                    disCopyright = user_pattem.getIS_DISP_COPYRIGHT();
                    disMessage = user_pattem.getIS_DISP_MESSAGE();
                    pattemHeartbeatInteval = Long.parseLong(user_pattem.getHEARTBEAT_INTEVAL());
                    if (mc_pattem != null) {
                        allMcPattems = dbUtil.getAllMcPattem();
                        mcIndex = Integer.parseInt(mc_pattem.getSORTID());
                        pattemNo = mc_pattem.getPATTEM_NO();
                        curDisplayLanguage = mc_pattem.getDISPLAY_LANGUAGE();
                        if (!curDisplayLanguage.equals(getLocalLanguage())) {
                            sSMTLineLanguage = curDisplayLanguage;
                        } else {
                            sSMTLineLanguage = MyApplication.sLanguage;
                        }
                        refreshTime = sDateNow;//获取时间判断是否到了切换模板的时间
                        LogUtil.e(TAG, "nSMTLineMcPattemLastRefreshTime = " + refreshTime);
                        SharedPreferencesUtil.saveValue(context, "nSMTLineMcPattemLastRefreshTimeV2",DateUtils.dateToLong(refreshTime));
                        if (!TextUtils.isEmpty(time)) {
                            switch (time) {
                                case "refreshTime":
                                    Message message = new Message();
                                    message.obj = refreshTime;
                                    message.what = 1;
                                    handler.sendMessage(message);
                                    break;
                            }

                        }
                    }
                    smtTitleText = user_pattem.getPATTEM_TITLE();
                    initColor();//初始化界面颜色
                    initModuleView(disCurTime, disReTime, disCopyright, disMessage);//初始化模块是否显示
                    if (showUI(disMessage)) {
                        getKanBanMsgAttr(context, sSMTLineLanguage, new OnRequestListener() {
                            @Override
                            public void onSuccess(List list) {
                                messageAttrBeanList = list;
                            }

                            @Override
                            public void onFail(String result) {

                            }
                        });
                    }
                    initCurUI();//更改界面语言

                    init();
                }
            }

            @Override
            public void onFail(String result) {
                ToastUtil.showShortToast(context, result);
            }
        });
    }

    /**
     * 初始化数据
     */
    private void init() {
        DBUtil dbUtil = new DBUtil(context);
        //判断线别、工站是否设置
        moreLines = dbUtil.getAllLine();
        lineIndex = SharedPreferencesUtil.getValue(context, "lineIndex", 0);
        if (moreLines != null && moreLines.size() >= 1) {
            sLineNoIn = SharedPreferencesUtil.getValue(getBaseContext(), "LineBoardLineNo", "");//获取多线别为了获取制令单信息

            if (isAutoPattem.equals("Y")) {//判断是否为自动模式
                isStopReceiver = true;
                startService();
                initService();
            } else {
                isStopReceiver = true;
                startService();
                initService();
            }
        } else {
            openActivity(context, SMTLineSettingActivity.class);
            return;
        }

    }

    //启动Service
    private void startService() {
        Intent i = new Intent(context, SMTLineAlarmService.class);
        // 获取10秒之后的日期时间字符串
        i.putExtra("alarm_time", DateUtils.getNLaterDateTimeString(Calendar.SECOND, -1));
        i.putExtra("task_id", mTaskId);
        i.putExtra("nRefreshTimeSet", pattemHeartbeatInteval);
        startService(i);
    }

    //停止Service
    private void sStopService() {
        LogUtil.e(TAG, "stopService");
        Intent intent = new Intent(context, SMTLineAlarmService.class);
        stopService(intent);
        AlarmManagerUtil.cancelAlarmBroadcast(context, mTaskId, SMTLineAlarmReceiver.class);
    }

    private void initService() {

        SMTLineAlarmReceiver smtLineAlarmReceiver = new SMTLineAlarmReceiver();
        smtLineAlarmReceiver.setOnReceivedMessageListener(new SMTLineAlarmReceiver.OnAlarmMessageListener() {
            @Override
            public void onReceived(String message) {
                if (isStopReceiver) {
                    DBUtil dbUtil = new DBUtil(context);

                    if (isAutoPattem.equals("Y")) {
                        nSMTLineMcPattemLastRefreshTime = SharedPreferencesUtil.getValue(context, "nSMTLineMcPattemLastRefreshTimeV2",0l);
                        boolean timeRefresh = DateUtils.isRefreshData("切换模板",DateUtils.longToDate(nSMTLineMcPattemLastRefreshTime), Long.parseLong(mc_pattem.getDISPLAY_TIME()));
                        if(timeRefresh) {
                            LogUtil.e(TAG, "到了切换模板的时间");
                            if (mcIndex < allMcPattems.size()) {
                                mcIndex++;
                            } else if (mcIndex >= allMcPattems.size()) {
                                mcIndex = 1;
                            }
                            if (allMcPattems != null && allMcPattems.size() == 1) {
                                initData(dbUtil);
                                return;
                            }
                            KANBAN_MC_PATTEM tempPattem = dbUtil.getMcPattemBySortId(String.valueOf(mcIndex));
                            if (tempPattem != null) {
                                String className = getClassName(getTemplateActivity(), tempPattem.getINTERNAL_CODE());//根据模板内码取展示模板的activity
                                LogUtil.e(TAG, "要切换的模板是" + className);
                                if (!TextUtils.isEmpty(className)) {
                                    if (!className.equals("com.topcee.kanban.ui.activity.pattem.SMTLineBoardV2Activity")) {
                                        openActivity(context, className, "isAutoPattem", isAutoPattem);
                                        SMTLineBoardV2Activity.this.finish();
                                    } else {
                                        initData(dbUtil);
                                    }
                                    return;
                                } else {
                                    initData(dbUtil);
                                    return;
                                }
                            } else {
                                initData(dbUtil);
                                return;
                            }
                        } else {
                            LogUtil.e(TAG, "还没到切换模板的时间");
                            initData(dbUtil);
                        }
                    } else {
                        LogUtil.e(TAG, "不是自动模式");
                        initData(dbUtil);
                    }

                }


            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData(DBUtil dbUtil) {
        nSMTLineWoLastRefreshTime = SharedPreferencesUtil.getValue(context, "nSMTLineWoLastRefreshTimeV2",0l);
        nSMTLineWoProductionLastRefreshTime = SharedPreferencesUtil.getValue(context, "nSMTLineWoProductionLastRefreshTimeV2",0l);
        nSMTLineEmployeeLastRefreshTime = SharedPreferencesUtil.getValue(context, "nSMTLineEmployeeLastRefreshTimeV2",0l);
        nSMTLinePeriodLastRefreshTime = SharedPreferencesUtil.getValue(context, "nSMTLinePeriodLastRefreshTimeV2",0l);
        nSMTLineWarnLastRefreshTime = SharedPreferencesUtil.getValue(context, "nSMTLineWarnLastRefreshTimeV2",0l);
        nSMTLineMessageLastRefreshTime = SharedPreferencesUtil.getValue(context, "nSMTLineMessageLastRefreshTimeV2",0l);
        nSMTLineShiftLastRefreshTime = SharedPreferencesUtil.getValue(context, "nSMTLineShiftLastRefreshTimeV2",0l);
        nSMTLineOperatingRateLastRefreshTime = SharedPreferencesUtil.getValue(context, "nSMTLineOperatingRateLastRefreshTimeV2",0l);

        if (lineIndex >= moreLines.size()) {
            lineIndex = 0;
        }
        sLineNo = moreLines.get(lineIndex).getLINE_NO();
        lastLineRefreshTime = SharedPreferencesUtil.getValue(context, "lastLineRefreshTimeV2",0l);
        curLineRefreshTime = System.currentTimeMillis();
        String displayTime = dbUtil.getDisPlayByLine(sLineNo).getDISPLAY_TIME();
        boolean lineRefresh = DateUtils.isRefreshData("切换线别",DateUtils.longToDate(lastLineRefreshTime), Long.parseLong(displayTime));
        if (lineRefresh) {
            LogUtil.e(TAG, "到了切换线别的时间:" + displayTime + " ，切换前的线别为" + sLineNo);
            lineIndex++;
            lastLineRefreshTime = System.currentTimeMillis();
            SharedPreferencesUtil.saveValue(context, "lastLineRefreshTimeV2", lastLineRefreshTime);
        } else {
            LogUtil.e(TAG, "还没到切换线别的时间");
        }
        if (lineIndex >= moreLines.size()) {
            lineIndex = 0;
        }
        sLineNo = moreLines.get(lineIndex).getLINE_NO();
        workshopNo = moreLines.get(lineIndex).getWORKSHOP_NO();
        setTitleText(sLineNo + smtTitleText);
        LogUtil.e(TAG, "当前线别为" + sLineNo);
        warningPercent = SharedPreferencesUtil.getValue(getBaseContext(), "materialWarningPercent", 10); //欠料预警可用比例
        warningStation = SharedPreferencesUtil.getValue(getBaseContext(), "materialWarningStation", 12);//欠料预警工站

        woTimeRefresh = DateUtils.isRefreshData("制令单",DateUtils.longToDate(nSMTLineWoLastRefreshTime),nSMTLineWoHeartbeatInteval);
        woProductionTimeRefresh = DateUtils.isRefreshData("制令单产出",DateUtils.longToDate(nSMTLineWoProductionLastRefreshTime),nSMTLineWoProductionHeartbeatInteval);
        periodTimeRefresh = DateUtils.isRefreshData("时段测量",DateUtils.longToDate(nSMTLinePeriodLastRefreshTime),nSMTLinePeriodHeartbeatInteval);
        operateRateTimeRefresh = DateUtils.isRefreshData("设备运转率",DateUtils.longToDate(nSMTLineOperatingRateLastRefreshTime),nSMTLineOperatingRateHeartbeatInteval);
        warnTimeRefresh = DateUtils.isRefreshData("欠料预警",DateUtils.longToDate(nSMTLineWarnLastRefreshTime),nSMTLineWarnHeartbeatInteval);
        personTimeRefresh = DateUtils.isRefreshData("人员岗位",DateUtils.longToDate(nSMTLineEmployeeLastRefreshTime),nSMTLineEmployeeHeartbeatInteval);
        shiftTimeRefresh = DateUtils.isRefreshData("班次",DateUtils.longToDate(nSMTLineShiftLastRefreshTime),nSMTLineShiftHeartbeatInteval);
        messageTimeRefresh = DateUtils.isRefreshData("公告消息",DateUtils.longToDate(nSMTLineMessageLastRefreshTime),nSMTLineMessageHeartbeatInteval);

        if (blIsInitData || (woTimeRefresh && woProductionTimeRefresh)) {
            getTimsGetWoInfo("SMT", sLineNo, "");//制令单
            getWoProductionInfo("SMT", sLineNo);//制令单产出
        }
        if (blIsInitData || periodTimeRefresh) {
            getTimsGetPeriodOutputInfo("SMT", sLineNo);//时段测量
        }
        if (blIsInitData || operateRateTimeRefresh) {
            getOperatingRate(sLineNo);//设备运转率
        }

        if (blIsInitData || personTimeRefresh) {
            getDutyPersonInfo(sLineNo);//人员岗位
        }
        if (blIsInitData || shiftTimeRefresh) {
            getShiftInfo("SMT", sLineNo);//班次
        }

        String json = javaToJson();
        LogUtil.e(TAG, "json = " + json);
        if (showUI(disMessage)) {
            if (blIsInitData || messageTimeRefresh) {
                getKanBanMsg("", json);//公告消息

            }
        }
        getModuleInfo(pattemNo);
        blIsInitData = false;
    }

    /**
     * 初始化颜色
     */
    private void initColor() {
        nSMTLineTitleColor = SharedPreferencesUtil.getValue(getBaseContext(), "nSMTLineTitleColor", MyApplication.nDefaultSMTLineTitleColor);
        nSMTLineWoTitleColor = SharedPreferencesUtil.getValue(getBaseContext(), "nSMTLineWoTitleColor", MyApplication.nDefaultSMTLineWoTitleColor);
        nSMTLineWoContentColor = SharedPreferencesUtil.getValue(getBaseContext(), "nSMTLineWoContentColor", MyApplication.nDefaultSMTLineWoContentColor);
        nSMTLineEmployeeColor = SharedPreferencesUtil.getValue(getBaseContext(), "nSMTLineEmployeeColor", MyApplication.nDefaultSMTLineEmployeeColor);
        nSMTLineRoleColor = SharedPreferencesUtil.getValue(getBaseContext(), "nSMTLineRoleColor", MyApplication.nDefaultSMTLineRoleColor);
//        nSMTLineOtherTextColor = SharedPreferencesUtil.getValue(getBaseContext(), "nSMTLineOtherTextColor", MyApplication.nDefaultSMTLineOtherTextColor);
        nSMTLineWholeBgColor = SharedPreferencesUtil.getValue(getBaseContext(), "nSMTLineWholeBgColor", MyApplication.nDefaultSMTLineWholeBgColor);
        nSMTLineChartBgColor = SharedPreferencesUtil.getValue(getBaseContext(), "nSMTLineChartBgColor", MyApplication.nDefaultSMTLineChartBgColor);
        Integer nChartHourColor1 = SharedPreferencesUtil.getValue(getBaseContext(), "nChartHourColor1", MyApplication.nDefaultChartHourColor1);
        Integer nChartHourColor2 = SharedPreferencesUtil.getValue(getBaseContext(), "nChartHourColor2", MyApplication.nDefaultChartHourColor2);
        nChartHourColor = new Integer[]{nChartHourColor1, nChartHourColor2};
        nChartWarnColor = SharedPreferencesUtil.getValue(getBaseContext(), "nChartWarnColor", MyApplication.nDefaultChartWarnColor);
        nChartOperatingRateColor = SharedPreferencesUtil.getValue(getBaseContext(), "nChartOperatingRateColor", MyApplication.nDefaultChartOperatingRateColor);
        setPattemTitleTextColor(nSMTLineTitleColor);
        //wo信息
        viewWo.setWoTitleColor(nSMTLineWoTitleColor);
        viewWo.setWoContentColor(nSMTLineWoContentColor);
        //图象人员姓名、角色
        viewEmployee.setEmployeeColor(nSMTLineEmployeeColor);
        viewEmployee.setRoleColor(nSMTLineRoleColor);
//        setOtherTextColor(nSMTLineOtherTextColor);
        setChartBgColor(nSMTLineChartBgColor);
        setWholeBgColor(nSMTLineWholeBgColor);
    }

    /**
     * 标题颜色
     *
     * @param color
     */
    private void setPattemTitleTextColor(int color) {
        menuTitle.setBtnLeftTextColor(color);
        //线别
        setTitleTextColor(color);
//        setBulletinTextColor(color);
        //时间、班次、时段
        setCurrentDateTextColor(color);
        setShiftTextColor(color);
        setShiftTimeTextColor(color);
        setCompanyTextColor(color);
    }

    /**
     * 标题默认颜色
     *
     * @param color
     */
    private void setOtherTextColor(int color) {
        //wo信息
        viewWo.setWoTitleColor(color);
        //图象人员姓名、角色
        viewEmployee.setEmployeeColor(color);
    }

    /**
     * 设置整个线板的默认的背景颜色
     */
    private void setWholeBgColor(int color) {
        setTemplateWholeBg(color);
    }

    /**
     * 设置图标控件的默认的背景颜色
     */
    private void setChartBgColor(int color) {
        viewWo.setWoInfoBgColor(color);//制令单数据
        viewEmployee.setEmployeeBgColor(color);//人员图像角色姓名
        viewTimeProduction.setBackgroundColor(color);//时段产量
        viewWarning.setBackgroundColor(color);//欠料预警
    }
    //获取制令单数据
    public void getTimsGetWoInfo(String sInterfaceType, String sLineNoIn, String sWorkShopNo) {
        getTimsGetWoInfo(sInterfaceType, sLineNoIn, sWorkShopNo, sSMTLineLanguage, new OnGetDataListener() {
            @Override
            public void onGetData(String result) {
                if (TextUtils.isEmpty(result)) {
                    if(toast!=null){
                        toast.cancel();
                    }
                    toast  = Toast.makeText(context,context.getString(R.string.interface_returns_failed),Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject item = jsonArray.getJSONObject(0);
                    String sStatus = item.getString("status");
                    String sMsg = item.getString("msg");
                    String sDateNow = "";
                    if(item.has("sDateNow")){
                        sDateNow = item.getString("sDateNow");
                    }
                    if(TextUtils.isEmpty(sDateNow)){
                        sDateNow = DateUtils.longToDate(System.currentTimeMillis());
                    }
                    SharedPreferencesUtil.saveValue(context,"nSMTLineWoLastRefreshTime",DateUtils.dateToLong(sDateNow));
                    if(!TextUtils.isEmpty(time)){
                        refreshTime = sDateNow;
                        switch (time){
                            case "refreshTime":
                                Message message = new Message();
                                message.obj = refreshTime;
                                message.what = 1;
                                handler.sendMessage(message);
                                break;
                            
                        }

                    }
                    woBeanList = new ArrayList<>();
                    if (sStatus.equalsIgnoreCase("OK")) {
                        String sData = item.getString("data"); //时段数据
                        JSONArray itemData = new JSONArray(sData);
                        woBeanList = GsonUtils.getObjects(itemData.toString(),WoBean.class);

                    }  else {
                        if(toast!=null){
                            toast.cancel();
                        }
                        toast  = Toast.makeText(context,sMsg,Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    if(woBeanList!=null && woBeanList.size()>=1){
                        WoBean woBean = woBeanList.get(0);
                        sCustomer = woBean.getsCustomerName();
                        sWo = woBean.getsWo();
                        sProductNo = woBean.getsProductNo();
                        sWoPlanQty = woBean.getsWoQtyPlan();
                    }else {
//                        ToastUtil.showShortToast(context,String.format(getString(R.string.parse_error),methodname));
                        sCustomer = "";
                        sWo = "";
                        sProductNo = "";
                        sWoPlanQty = "0";
                    }
                    Message message = new Message();
                    message.what = 4;
                    handler.sendMessage(message);
                    //显示-制令单信息
                    viewWo.setCustomText(sCustomer);//客户
                    viewWo.setWoText(sWo); //制令单
                    viewWo.setProductNoText(sProductNo);//产品号
                    viewWo.setWoPlanQtyText(sWoPlanQty); //制令单数量
                } catch (JSONException e) {
                    e.printStackTrace();
                    if(toast!=null){
                        toast.cancel();
                    }
                    toast  = Toast.makeText(context,e.getMessage().toString(),Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onGetDataError(String error) {

            }
        });
    }

    //获取制令单产出信息
    public void getWoProductionInfo(String sInterfaceType,String sLineNoIn) {
        getWoProductionInfo(sInterfaceType, sLineNoIn, sSMTLineLanguage, new OnGetDataListener() {
            @Override
            public void onGetData(String result) {
                if (TextUtils.isEmpty(result)) {
                    if(toast!=null){
                        toast.cancel();
                    }
                    toast  = Toast.makeText(context,context.getString(R.string.interface_returns_failed),Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject item = jsonArray.getJSONObject(0);
                    String sStatus = item.getString("status");
                    String sMsg = item.getString("msg");
                    String sDateNow = "";
                    if(item.has("sDateNow")){
                        sDateNow = item.getString("sDateNow");
                    }
                    if(TextUtils.isEmpty(sDateNow)){
                        sDateNow = DateUtils.longToDate(System.currentTimeMillis());
                    }
                    SharedPreferencesUtil.saveValue(context,"nSMTLineWoProductionLastRefreshTime",DateUtils.dateToLong(sDateNow));
                    if(!TextUtils.isEmpty(time)){
                        refreshTime = sDateNow;
                        switch (time){
                            case "refreshTime":
                                Message message = new Message();
                                message.obj = refreshTime;
                                message.what = 1;
                                handler.sendMessage(message);
                                break;
                            
                        }

                    }
                    if (sStatus.equalsIgnoreCase("OK")) {
                        String sData = item.getString("data");
                        JSONArray jsonArrayData = new JSONArray(sData);
                        woProductionBeanList = GsonUtils.getObjects(jsonArrayData.toString(),WoProductionBean.class);

                    } else {
                        if(toast!=null){
                            toast.cancel();
                        }
                        toast  = Toast.makeText(context,sMsg,Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    if(woProductionBeanList!=null && woProductionBeanList.size()>=1){
                        sWoQtyOutput = woProductionBeanList.get(0).getsQtyOutput();
                        sFinishingRate = woProductionBeanList.get(0).getsFinishingRate();
                    }else {
//                        ToastUtil.showShortToast(context,String.format(getString(R.string.parse_error),methodname));
                        sWoQtyOutput = "0";
                        sFinishingRate = "0";
                    }
                    //当前班次总产量
//                viewWo.setCurShiftTotalQtyText(sWoQtyOutput); //当前班次总产量(需要另写接口来获取这个字段)
                    if(TextUtils.isEmpty(sWoQtyOutput)){
                        sWoQtyOutput = "0";
                    }
                    if(TextUtils.isEmpty(sWoPlanQty)){
                        sWoPlanQty = "0";
                    }
                    viewWo.setProductionScheduleText(sWoQtyOutput + "/" + sWoPlanQty); //生产进度
                    viewWo.setWoCompletedQtyText(sWoQtyOutput);//制令单已完成数量
                    //进度条-显示
                    showProgressBar(viewWo,sFinishingRate);
                } catch (JSONException e) {
                    e.printStackTrace();
                    if(toast!=null){
                        toast.cancel();
                    }
                    toast  = Toast.makeText(context,e.getMessage().toString(),Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onGetDataError(String error) {

            }
        });
    }

    //获取人员岗位信息
    public void getDutyPersonInfo(String sLineNo) {
        if(MyApplication.sMesUser.equals("SZYDM")||MyApplication.sMesUser.equals("TCL")){
            getDutyPersonInfo(sLineNo, sSMTLineLanguage, new OnGetSoapObjectListener() {
                @Override
                public void onGetData(SoapObject object) {
                    if (null == object) {
                        return;
                    }
                    String sDateNow = DateUtils.longToDate(System.currentTimeMillis());

                    SharedPreferencesUtil.saveValue(context,"nSMTLineEmployeeLastRefreshTime",DateUtils.dateToLong(sDateNow));
                    if(!TextUtils.isEmpty(time)){
                        refreshTime = sDateNow;
                        switch (time){
                            case "refreshTime":
                                Message message = new Message();
                                message.obj = refreshTime;
                                message.what = 1;
                                handler.sendMessage(message);
                                break;
                            
                        }

                    }
                    try {
                        object = (SoapObject) object.getProperty("diffgram");
                        if(object.getPropertyCount()> 0 ){
                            object = (SoapObject) object.getProperty(0);
                            if (null == object) {
                                return;
                            }
                            for (int i = 0; i < 2; i++) {
                                boolean blIsClear = false;
                                EmployeeBean mUserInfo = new EmployeeBean();
                                if (i >= object.getPropertyCount()) {
                                    blIsClear = true;
                                } else {
                                    SoapObject soap = (SoapObject) object.getProperty(i);
                                    if(soap.hasProperty("USER_NO")){
                                        if(soap.getProperty("USER_NO").toString().equals("anyType{}")){
                                            mUserInfo.setUSER_NO("");
                                        }else {
                                            mUserInfo.setUSER_NO(soap.getProperty("USER_NO").toString()); //中文-姓名
                                        }
                                    }
                                    if(soap.hasProperty("USER_NAME")){
                                        if(soap.getProperty("USER_NAME").toString().equals("anyType{}")){
                                            mUserInfo.setUSER_NAME("");
                                        }else {
                                            mUserInfo.setUSER_NAME(soap.getProperty("USER_NAME").toString()); //中文-姓名
                                        }
                                    }
                                    if(soap.hasProperty("USER_NAME_CH")){
                                        if(soap.getProperty("USER_NAME_CH").toString().equals("anyType{}")){
                                            mUserInfo.setUSER_NAME_CH("");
                                        }else {
                                            mUserInfo.setUSER_NAME_CH(soap.getProperty("USER_NAME_CH").toString()); //中文-姓名
                                        }
                                    }
                                    if(soap.hasProperty("USER_NAME_EN")){
                                        if(soap.getProperty("USER_NAME_EN").toString().equals("anyType{}")){
                                            mUserInfo.setUSER_NAME_EN("");
                                        }else {
                                            mUserInfo.setUSER_NAME_EN(soap.getProperty("USER_NAME_EN").toString()); //英文-姓名
                                        }
                                    }

                                    if(soap.hasProperty("DUTIES_NO")){
                                        if(soap.getProperty("DUTIES_NO").toString().equals("anyType{}")){
                                            mUserInfo.setDUTIES_NO("");
                                        }else {
                                            mUserInfo.setDUTIES_NO(soap.getProperty("DUTIES_NO").toString()); //英文-岗位
                                        }
                                    }else {
                                        mUserInfo.setDUTIES_NO("");
                                    }
                                    if(soap.hasProperty("PHOTO")){
                                        byte[] btImage = Base64.decode(soap.getProperty("PHOTO").toString(), Base64.DEFAULT); //图片
                                        mUserInfo.setPHOTO(BitmapFactory.decodeByteArray(btImage, 0, btImage.length));
                                    }else {
                                        mUserInfo.setPHOTO(null);
                                    }

                                }
                                switch (i) {
                                    case 0:
                                        setUserImage(sSMTLineLanguage,viewEmployee.getIv_employee_01(), viewEmployee.getTv_employee_01(),  viewEmployee.getTv_role_01(), mUserInfo, blIsClear);
                                        break;
                                    case 1:
                                        setUserImage(sSMTLineLanguage,viewEmployee.getIv_employee_02(),  viewEmployee.getTv_employee_02(), viewEmployee.getTv_role_02(), mUserInfo, blIsClear);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }else {
                            boolean blIsClear = true;
                            for (int i = 0; i < 2; i++) {
                                EmployeeBean mUserInfo = new EmployeeBean();
                                switch (i) {
                                    case 0:
                                        setUserImage(sSMTLineLanguage,viewEmployee.getIv_employee_01(), viewEmployee.getTv_employee_01(),  viewEmployee.getTv_role_01(), mUserInfo, blIsClear);
                                        break;
                                    case 1:
                                        setUserImage(sSMTLineLanguage,viewEmployee.getIv_employee_02(),  viewEmployee.getTv_employee_02(), viewEmployee.getTv_role_02(), mUserInfo, blIsClear);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        if(toast!=null){
                        toast.cancel();
                    }
                    toast  = Toast.makeText(context,e.getMessage().toString(),Toast.LENGTH_SHORT);
                    toast.show();
                    }
                }

                @Override
                public void onGetDataError(String error) {

                }
            });
        }else {
            getKanbanDutyShiftPersonInfo("LINE", sLineNo, sSMTLineLanguage, new OnGetSoapObjectListener() {
                @Override
                public void onGetData(SoapObject object) {
                    if (null == object) {
                        return;
                    }
                    String sDateNow = DateUtils.longToDate(System.currentTimeMillis());

                    SharedPreferencesUtil.saveValue(context,"nSMTLineEmployeeLastRefreshTime",DateUtils.dateToLong(sDateNow));
                    if(!TextUtils.isEmpty(time)){
                        refreshTime = sDateNow;
                        switch (time){
                            case "refreshTime":
                                Message message = new Message();
                                message.obj = refreshTime;
                                message.what = 1;
                                handler.sendMessage(message);
                                break;
                            
                        }

                    }
                    try {
                        object = (SoapObject) object.getProperty("diffgram");
                        if(object.getPropertyCount()> 0 ){
                            object = (SoapObject) object.getProperty(0);
                            if (null == object) {
                                return;
                            }
                            for (int i = 0; i < 2; i++) {
                                boolean blIsClear = false;
                                EmployeeNewBean mUserInfo = new EmployeeNewBean();
                                if (i >= object.getPropertyCount()) {
                                    blIsClear = true;
                                } else {
                                    SoapObject soap = (SoapObject) object.getProperty(i);
                                    if(soap.hasProperty("USER_NO")){
                                        if(soap.getProperty("USER_NO").toString().equals("anyType{}")){
                                            mUserInfo.setUSER_NO("");
                                        }else {
                                            mUserInfo.setUSER_NO(soap.getProperty("USER_NO").toString()); //姓名
                                        }
                                    }
                                    if(soap.hasProperty("USER_NAME")){
                                        if(soap.getProperty("USER_NAME").toString().equals("anyType{}")){
                                            mUserInfo.setUSER_NAME("");
                                        }else {
                                            mUserInfo.setUSER_NAME(soap.getProperty("USER_NAME").toString()); //姓名
                                        }
                                    }
                                    if(soap.hasProperty("EMPLOYEE_NO")){
                                        if(soap.getProperty("EMPLOYEE_NO").toString().equals("anyType{}")){
                                            mUserInfo.setEMPLOYEE_NO("");
                                        }else {
                                            mUserInfo.setEMPLOYEE_NO(soap.getProperty("EMPLOYEE_NO").toString()); //
                                        }
                                    }
                                    if(soap.hasProperty("MAIL_ADDRESS")){
                                        if(soap.getProperty("MAIL_ADDRESS").toString().equals("anyType{}")){
                                            mUserInfo.setMAIL_ADDRESS("");
                                        }else {
                                            mUserInfo.setMAIL_ADDRESS(soap.getProperty("MAIL_ADDRESS").toString()); //地址
                                        }
                                    }
                                    if(soap.hasProperty("DEPT_NO")){
                                        if(soap.getProperty("DEPT_NO").toString().equals("anyType{}")){
                                            mUserInfo.setDEPT_NO("");
                                        }else {
                                            mUserInfo.setDEPT_NO(soap.getProperty("DEPT_NO").toString()); //部门
                                        }
                                    }else {
                                        mUserInfo.setDEPT_NO("");
                                    }
                                    if(soap.hasProperty("DEPT_NAME")){
                                        if(soap.getProperty("DEPT_NAME").toString().equals("anyType{}")){
                                            mUserInfo.setDEPT_NAME("");
                                        }else {
                                            mUserInfo.setDEPT_NAME(soap.getProperty("DEPT_NAME").toString()); //部门名称
                                        }
                                    }else {
                                        mUserInfo.setDEPT_NAME("");
                                    }
                                    if(soap.hasProperty("DUTY_NO")){
                                        if(soap.getProperty("DUTY_NO").toString().equals("anyType{}")){
                                            mUserInfo.setDUTIES_NO("");
                                        }else {
                                            mUserInfo.setDUTIES_NO(soap.getProperty("DUTY_NO").toString()); //岗位
                                        }
                                    }else {
                                        mUserInfo.setDUTIES_NO("");
                                    }
                                    if(soap.hasProperty("DUTY_NAME")){
                                        if(soap.getProperty("DUTY_NAME").toString().equals("anyType{}")){
                                            mUserInfo.setDUTY_NAME("");
                                        }else {
                                            mUserInfo.setDUTY_NAME(soap.getProperty("DUTY_NAME").toString()); //岗位名称
                                        }
                                    }else {
                                        mUserInfo.setDUTY_NAME("");
                                    }
                                    if(soap.hasProperty("PHOTO")){
                                        byte[] btImage = Base64.decode(soap.getProperty("PHOTO").toString(), Base64.DEFAULT); //图片
                                        mUserInfo.setPHOTO(BitmapFactory.decodeByteArray(btImage, 0, btImage.length));
                                    }else {
                                        mUserInfo.setPHOTO(null);
                                    }

                                }
                                switch (i) {
                                    case 0:
                                        setUserImageNew(sSMTLineLanguage,viewEmployee.getIv_employee_01(), viewEmployee.getTv_employee_01(),  viewEmployee.getTv_role_01(), mUserInfo, blIsClear);
                                        break;
                                    case 1:
                                        setUserImageNew(sSMTLineLanguage,viewEmployee.getIv_employee_02(),  viewEmployee.getTv_employee_02(), viewEmployee.getTv_role_02(), mUserInfo, blIsClear);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }else {
                            boolean blIsClear = true;
                            for (int i = 0; i < 2; i++) {
                                EmployeeNewBean mUserInfo = new EmployeeNewBean();
                                switch (i) {
                                    case 0:
                                        setUserImageNew(sSMTLineLanguage,viewEmployee.getIv_employee_01(), viewEmployee.getTv_employee_01(),  viewEmployee.getTv_role_01(), mUserInfo, blIsClear);
                                        break;
                                    case 1:
                                        setUserImageNew(sSMTLineLanguage,viewEmployee.getIv_employee_02(),  viewEmployee.getTv_employee_02(), viewEmployee.getTv_role_02(), mUserInfo, blIsClear);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        if(toast!=null){
                        toast.cancel();
                    }
                    toast  = Toast.makeText(context,e.getMessage().toString(),Toast.LENGTH_SHORT);
                    toast.show();
                    }
                }

                @Override
                public void onGetDataError(String error) {

                }
            });
        }

    }


    //获取预警信息查询
    public void getWarnInfo(String sWo) {
        getWarnInfo(sWo, sSMTLineLanguage, new OnGetDataListener() {
            @Override
            public void onGetData(String result) {
                if (TextUtils.isEmpty(result)) {
                    if(toast!=null){
                        toast.cancel();
                    }
                    toast  = Toast.makeText(context,context.getString(R.string.interface_returns_failed),Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject item = jsonArray.getJSONObject(0);
                    String sStatus = item.getString("status");
                    String sMsg = item.getString("msg");
                    String sDateNow = "";
                    if(item.has("sDateNow")){
                        sDateNow = item.getString("sDateNow");
                    }
                    if(TextUtils.isEmpty(sDateNow)){
                        sDateNow = DateUtils.longToDate(System.currentTimeMillis());
                    }
                    SharedPreferencesUtil.saveValue(context,"nSMTLineWarnLastRefreshTime",DateUtils.dateToLong(sDateNow));
                    if(!TextUtils.isEmpty(time)){
                        refreshTime = sDateNow;
                        switch (time){
                            case "refreshTime":
                                Message message = new Message();
                                message.obj = refreshTime;
                                message.what = 1;
                                handler.sendMessage(message);
                                break;
                            
                        }

                    }
                    warningList = new ArrayList<>();
                    if (sStatus.equalsIgnoreCase("OK")) {
                        String sData = item.getString("data");
                        JSONArray jsonArrayData = new JSONArray(sData);
                        warningList = GsonUtils.getObjects(jsonArrayData.toString(),WarnBean.class);
                    } else {
//                    ToastUtil.showLongToast(context, sMsg);
                    }

                    if(warningList!=null && warningList.size()>=1){
                        List<WarnBean> newWarnList = new ArrayList<>();
                        for (int i = 0; i < warningList.size(); i++) {
                            WarnBean warnBean = warningList.get(i);
                            String needPercent = warnBean.getNEED_PERCENT();
                            int percent = 0;
                            if(TextUtils.isEmpty(needPercent)){
                                needPercent = "0";
                            }
                            if(needPercent.equals("0")){
                                percent = 0;
                            }else {
                                percent = Integer.parseInt(needPercent);
                            }
                            if(percent <= warningPercent){//取值小于本地设置的欠料预警百分比
                                newWarnList.add(warnBean);
                            }
                        }
                        if(newWarnList!=null && newWarnList.size()>=1){
                            List<WarnBean> curWarnList = new ArrayList<>();
                            if(newWarnList.size() > warningStation){//显示本地设置的工站数
                                for (int i = 0; i < warningStation; i++) {
                                    WarnBean curWarnBean = newWarnList.get(i);
                                    curWarnList.add(curWarnBean);
                                }
                            }else {
                                curWarnList = newWarnList;
                            }
                            //显示-欠料预警
                            showBarChartOweMaterialWarning(viewWarning,curWarnList,nChartWarnColor,sChartWarnTitle,sChartWarnLabel);
                        }else {
                            List<WarnBean> tempWarns = new ArrayList<>();
                            for (int i = 0; i < 2; i++) {
                                WarnBean warnBean = new WarnBean();
                                warnBean.setMACHINE_SEQ("warn" + i);
                                warnBean.setNEED_PERCENT("0");
                                tempWarns.add(warnBean);
                            }
                            //显示-欠料预警
                            showBarChartOweMaterialWarning(viewWarning,tempWarns,nChartWarnColor,sChartWarnTitle,sChartWarnLabel);
                        }

                    }else {
                        List<WarnBean> tempWarns = new ArrayList<>();
                        for (int i = 0; i < 2; i++) {
                            WarnBean warnBean = new WarnBean();
                            warnBean.setMACHINE_SEQ("warn" + i);
                            warnBean.setNEED_PERCENT("0");
                            tempWarns.add(warnBean);
                        }
                        //显示-欠料预警
                        showBarChartOweMaterialWarning(viewWarning,tempWarns,nChartWarnColor,sChartWarnTitle,sChartWarnLabel);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if(toast!=null){
                        toast.cancel();
                    }
                    toast  = Toast.makeText(context,e.getMessage().toString(),Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onGetDataError(String error) {

            }
        });
    }

    //获取时段数据
    public void getTimsGetPeriodOutputInfo(String sInterfaceType, String sLineNo) {
        getTimsGetPeriodOutputInfo(sInterfaceType, sLineNo, sSMTLineLanguage, new OnGetDataListener() {
            @Override
            public void onGetData(String result) {
                if (TextUtils.isEmpty(result)) {
                    if(toast!=null){
                        toast.cancel();
                    }
                    toast  = Toast.makeText(context,context.getString(R.string.interface_returns_failed),Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject item = jsonArray.getJSONObject(0);
                    String sStatus = item.getString("status");
                    String sMsg = item.getString("msg");
                    String sDateNow = "";
                    if(item.has("sDateNow")){
                        sDateNow = item.getString("sDateNow");
                    }
                    if(TextUtils.isEmpty(sDateNow)){
                        sDateNow = DateUtils.longToDate(System.currentTimeMillis());
                    }
                    SharedPreferencesUtil.saveValue(context,"nSMTLinePeriodLastRefreshTime",DateUtils.dateToLong(sDateNow));
                    if(!TextUtils.isEmpty(time)){
                        refreshTime = sDateNow;
                        switch (time){
                            case "refreshTime":
                                Message message = new Message();
                                message.obj = refreshTime;
                                message.what = 1;
                                handler.sendMessage(message);
                                break;
                            
                        }

                    }
                    periodOutputList = new ArrayList<>();
                    if (sStatus.equalsIgnoreCase("OK")) {
                        String sData = item.getString("data");
                        JSONArray jsonArrayData = new JSONArray(sData);
                        periodOutputList = GsonUtils.getObjects(jsonArrayData.toString(),PeriodOutputBean.class);
                    } else {
                        if(toast!=null){
                            toast.cancel();
                        }
                        toast  = Toast.makeText(context,sMsg,Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    String sChartHourTitle = getString(R.string.smt_line_time_production);
                    String sChartHourLabel[] = {getString(R.string.smt_line_input_qty),getString(R.string.smt_line_output_qty)};
                    if(getCurLanguage()==1){
                        sChartHourTitle = "Time production";
                        sChartHourLabel[0] = "Number of inputs";
                        sChartHourLabel[1] = "Number of outputs";
                    }else if(getCurLanguage()==2){
                        sChartHourTitle = "時段産量";
                        sChartHourLabel[0] = "投入數";
                        sChartHourLabel[1] = "産出數";
                    }
                    if(periodOutputList!=null && periodOutputList.size()>=1){
                        //显示-时段数据图形控件
                        showMultiaxialTimeProduction(viewTimeProduction,periodOutputList, "",nChartHourColor,sChartHourTitle,sChartHourLabel);
                    }else {
                        List<PeriodOutputBean> tempPeriod = new ArrayList<>();
                        for (int i = 0; i < 2; i++) {
                            PeriodOutputBean outputBean = new PeriodOutputBean();
                            outputBean.setPeriodName("time" + i);
                            outputBean.setInPutQty("0");
                            outputBean.setOutPutQty("0");
                            tempPeriod.add(outputBean);
                        }
                        showMultiaxialTimeProduction(viewTimeProduction,tempPeriod, "",nChartHourColor,sChartHourTitle,sChartHourLabel);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if(toast!=null){
                        toast.cancel();
                    }
                    toast  = Toast.makeText(context,e.getMessage().toString(),Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onGetDataError(String error) {

            }
        });
    }

    /**
     * json = "[{\"status\":\"OK\",\"msg\":\"发送公告消息筛选条件!\",\"data\":[{\"LINE_NO\":\"SMT-1\",\"KANBAN_MC_NO\":\"1\",\"PATTEM_NO\":2,\"FACTORY_NO\":\"0\"}]}]";
     * 公告消息json
     */
    private String javaToJson() {
        kanbanMcNo = SharedPreferencesUtil.getValue(context, "KANBAN_MC_NO", "");
        JSONArray array = null;
        try {
            array = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            JSONArray dataArray = new JSONArray();
            JSONObject mesJsonObject = new JSONObject();
            mesJsonObject.put("PATTEM_NO", pattemNo);
            mesJsonObject.put("WORKSHOP_NO", workshopNo);
            mesJsonObject.put("LINE_NO", sLineNo);
            mesJsonObject.put("KANBAN_MC_NO", kanbanMcNo);
            mesJsonObject.put("FACTORY_NO", MyApplication.sFactoryNo);
            dataArray.put(mesJsonObject);
            jsonObject.put("status", "OK");
            jsonObject.put("msg", "发送公告消息筛选条件!");
            jsonObject.put("data", dataArray);
            array.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return array.toString();
    }

    //获取公告栏信息
    public void getKanBanMsg(String code,String json) {
        getKanBanMsg(code, json, sSMTLineLanguage, new OnGetDataListener() {
            @Override
            public void onGetData(String result) {
                if (TextUtils.isEmpty(result)) {
                    if(toast!=null){
                        toast.cancel();
                    }
                    toast  = Toast.makeText(context,context.getString(R.string.interface_returns_failed),Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject item = jsonArray.getJSONObject(0);
                    String sStatus = item.getString("status");
                    String sMsg = item.getString("msg");
                    String sDateNow = "";
                    if(item.has("sDateNow")){
                        sDateNow = item.getString("sDateNow");
                    }
                    if(TextUtils.isEmpty(sDateNow)){
                        sDateNow = DateUtils.longToDate(System.currentTimeMillis());
                    }
                    SharedPreferencesUtil.saveValue(context,"nSMTLineMessageLastRefreshTime",DateUtils.dateToLong(sDateNow));
                    if(!TextUtils.isEmpty(time)){
                        refreshTime = sDateNow;
                        switch (time){
                            case "refreshTime":
                                Message message = new Message();
                                message.obj = refreshTime;
                                message.what = 1;
                                handler.sendMessage(message);
                                break;
                            
                        }

                    }
                    messageList = new ArrayList<>();
                    curMessageList = new ArrayList<>();
                    if (sStatus.equalsIgnoreCase("OK")) {
                        String sData = item.getString("data");
                        JSONArray jsonArrayData = new JSONArray(sData);
                        messageList = GsonUtils.getObjects(jsonArrayData.toString(), MessageBean.class);
                        messageStopFlag = false;
                    }else {
//                    ToastUtil.showLongToast(context, sMsg);
                        LogUtil.e(TAG,"messageList = " + sMsg);
                        messageStopFlag = true;
                        setVisiblebulletin(View.GONE);
                    }
                    if(messageList!=null && messageList.size()>=1){
                        messageStopFlag = false;
                        LogUtil.e(TAG,"messageList = " + messageList.toString());

                        for (int i = 0; i < messageList.size(); i++) {
                            MessageBean messageBean = new MessageBean();
                            messageBean.setFACTORY_NO(messageList.get(i).getFACTORY_NO());
                            messageBean.setMSG_ID(messageList.get(i).getMSG_ID());
                            messageBean.setMSG_TITLE(messageList.get(i).getMSG_TITLE());
                            messageBean.setMSG_TYPE(messageList.get(i).getMSG_TYPE());
                            messageBean.setMSG_DETAIL(messageList.get(i).getMSG_DETAIL());
                            messageBean.setVALID_DATE(messageList.get(i).getVALID_DATE());
                            messageBean.setINVALID_DATE(messageList.get(i).getINVALID_DATE());
                            if(messageAttrBeanList!=null && messageAttrBeanList.size()>=1){
                                for (int j = 0; j < messageAttrBeanList.size(); j++) {
                                    if(messageList.get(i).getMSG_TYPE().equals(messageAttrBeanList.get(j).getMSG_TYPE())){
                                        messageBean.setTYPE_NAME(messageAttrBeanList.get(j).getTYPE_NAME());
                                        messageBean.setRGB_TEXT(messageAttrBeanList.get(j).getRGB_TEXT());
                                        messageBean.setRGB_BACKGROUND(messageAttrBeanList.get(j).getRGB_BACKGROUND());
                                    }
                                }
                            }
                            curMessageList.add(messageBean);
                        }
                        LogUtil.e(TAG,"curMessageList = " + curMessageList.toString());
                        if (showUI(disMessage) && !messageStopFlag) {
                            setVisiblebulletin(View.VISIBLE);
                        } else {
                            setVisiblebulletin(View.GONE);
                        }
                        if(messageFlag){
                            messageFlag = false;
                            scrollTextView = getViewBulletin();
                            curScrollText = getMsgFirst(curMessageList);
                            if(!TextUtils.isEmpty(curScrollText)){
                                scroll();
                            }else {
                                stopScroll();
                            }
                        }else {
                            if(scrollTextView==null && !TextUtils.isEmpty(curScrollText)){
                                if(messageStopFlag){
                                    stopScroll();
                                    messageStopFlag = false;
                                }
                                scrollTextView = getViewBulletin();
                                scroll();//开始
                            }
                        }
                        if(scrollTextView!=null){
                            scrollTextView.setOnMargueeListener(new MarqueeView.OnMargueeListener() {
                                @Override
                                public void onRollOver() {
                                    if(messageStopFlag){
                                        stopScroll();
                                        return;
                                    }else {
                                        if(scrollTextView.getVisibility() == View.INVISIBLE || scrollTextView.getVisibility() == View.GONE){
                                            setVisiblebulletin(View.VISIBLE);
                                        }
                                    }
                                    curScrollText = getMsg(curMessageList);
                                    scroll();
                                }

                            });
                        }

                    }else {
                        messageStopFlag = true;
                        setVisiblebulletin(View.GONE);
//                    ToastUtil.showShortToast(context,String.format(getString(R.string.parse_error),methodname));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if(toast!=null){
                        toast.cancel();
                    }
                    toast  = Toast.makeText(context,e.getMessage().toString(),Toast.LENGTH_SHORT);
                    toast.show();
                    messageStopFlag = true;
                    setVisiblebulletin(View.GONE);
                }
            }

            @Override
            public void onGetDataError(String error) {

            }
        });
    }

    /**
     * 获取滚动消息内容
     *
     * @param curMessageList
     * @return
     */
    private String getMsgFirst(List<MessageBean> curMessageList) {
        String tempName = curMessageList.get(scrollIndex).getTYPE_NAME();
        String tempDetail = curMessageList.get(scrollIndex).getMSG_DETAIL();
        String text = "[" + tempName + "]" + tempDetail;
        if (TextUtils.isEmpty(tempName) && TextUtils.isEmpty(tempName)) {
            for (int i = scrollIndex + 1; i < curMessageList.size(); i++) {//如果取的值是空的，则取下一条信息，
                tempName = curMessageList.get(scrollIndex).getTYPE_NAME();
                tempDetail = curMessageList.get(scrollIndex).getMSG_DETAIL();
                text = "[" + tempName + "]" + tempDetail;
                if (!TextUtils.isEmpty(tempName) && !TextUtils.isEmpty(tempName)) {//如果下条消息不为空，则跳出去,不为空继续取下一条信息，全部都取完还是没有消息的，也跳出去
                    scrollIndex = i;
                    break;
                } else {
                    if (i == curMessageList.size() - 1) {//如果全部消息取完还是空消息，则给scrollIndex赋值为0，下次从0开始取值
                        scrollIndex = 0;
                        stopScroll();
                        break;
                    }
                }
            }

        }

        return text;
    }

    /**
     * 获取滚动消息内容
     *
     * @param curMessageList
     * @return
     */
    private String getMsg(List<MessageBean> curMessageList) {
        scrollIndex++;
        if (scrollIndex >= curMessageList.size()) {
            scrollIndex = 0;
        }
        String tempName = curMessageList.get(scrollIndex).getTYPE_NAME();
        String tempDetail = curMessageList.get(scrollIndex).getMSG_DETAIL();
        String text = "[" + tempName + "]" + tempDetail;
        if (TextUtils.isEmpty(tempName) && TextUtils.isEmpty(tempName)) {
            for (int i = scrollIndex + 1; i < curMessageList.size(); i++) {//如果取的值是空的，则取下一条信息，
                tempName = curMessageList.get(scrollIndex).getTYPE_NAME();
                tempDetail = curMessageList.get(scrollIndex).getMSG_DETAIL();
                text = "[" + tempName + "]" + tempDetail;
                if (!TextUtils.isEmpty(tempName) && !TextUtils.isEmpty(tempName)) {//如果下条消息不为空，则跳出去,不为空继续取下一条信息，全部都取完还是没有消息的，也跳出去
                    scrollIndex = i;
                    break;
                } else {
                    if (i == curMessageList.size() - 1) {//如果全部消息取完还是空消息，则给scrollIndex赋值为0，下次从0开始取值
                        scrollIndex = 0;
                    }
                }
            }
        }
        return text;
    }

    /**
     * 消息滚动
     */
    private void scroll() {
        bulletinTextColor = AppUtil.parseColor(curMessageList.get(scrollIndex).getRGB_TEXT());
        bulletinBgColor = AppUtil.parseColor(curMessageList.get(scrollIndex).getRGB_BACKGROUND());
        setBulletinBgColor(bulletinBgColor);
        scrollTextView.setmTextColor(bulletinTextColor);
        scrollTextView.setFocusable(true);
        scrollTextView.setText(curScrollText);//设置文本
        scrollTextView.startScroll(); //开始
    }
    //获取班次信息
    public void getShiftInfo(String sInterfaceType,String sLineNo) {
        getShiftInfo(sInterfaceType, sLineNo, sSMTLineLanguage, new OnGetDataListener() {
            @Override
            public void onGetData(String result) {
                if (TextUtils.isEmpty(result)) {
                    if(toast!=null){
                        toast.cancel();
                    }
                    toast  = Toast.makeText(context,context.getString(R.string.interface_returns_failed),Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject item = jsonArray.getJSONObject(0);
                    String sStatus = item.getString("status");
                    String sMsg = item.getString("msg");
                    String sDateNow = "";
                    if(item.has("sDateNow")){
                        sDateNow = item.getString("sDateNow");
                    }
                    if(TextUtils.isEmpty(sDateNow)){
                        sDateNow = DateUtils.longToDate(System.currentTimeMillis());
                    }
                    SharedPreferencesUtil.saveValue(context,"nSMTLineShiftLastRefreshTime",DateUtils.dateToLong(sDateNow));
                    if(!TextUtils.isEmpty(time)){
                        refreshTime = sDateNow;
                        switch (time){
                            case "refreshTime":
                                Message message = new Message();
                                message.obj = refreshTime;
                                message.what = 1;
                                handler.sendMessage(message);
                                break;
                            
                        }

                    }
                    shiftList = new ArrayList<>();
                    if (sStatus.equalsIgnoreCase("OK")) {
                        String sData = item.getString("data");
                        JSONArray jsonArrayData = new JSONArray(sData);
                        shiftList = GsonUtils.getObjects(jsonArrayData.toString(), ShiftBean.class);

                    } else {
                        ToastUtil.showShortToast(context, sMsg);
                        return;
                    }
                    if(shiftList!=null && shiftList.size()>=1){

                        String  sShiftName = shiftList.get(0).getsShiftName();
                        String  sShiftTimeBegin = shiftList.get(0).getsShiftTimeBegin();
                        String  sShiftTimeEnd = shiftList.get(0).getsShiftTimeEnd();
                        String sShiftTime = sShiftTimeBegin + "-" + sShiftTimeEnd;
                        setShift(sShiftName + "(" + sShiftTime + ")");
//                        setShift(String.format(getString(R.string.smt_line_board_shift_no),sShiftName));
//                        setShiftTime(String.format(getString(R.string.smt_line_board_shift_time_begin_end),sShiftTime));
//                        if(getCurLanguage()==1){
//                            setShift("Shift(" + sShiftName+ ")" );
//                            setShiftTime("Time slot(" + sShiftTime+ ")" );
//                        }else if(getCurLanguage()==2){
//                            setShift("班次(" + sShiftName+ ")" );
//                            setShiftTime("時段(" + sShiftTime+ ")" );
//                        }
                    }else {
//                        ToastUtil.showShortToast(context,String.format(getString(R.string.parse_error),methodname));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if(toast!=null){
                        toast.cancel();
                    }
                    toast  = Toast.makeText(context,e.getMessage().toString(),Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onGetDataError(String error) {

            }
        });
    }

    //获取用户模板包含的模块信息
    public void getModuleInfo(String sPattemNo) {
        getModuleInfo(sPattemNo, sSMTLineLanguage, new OnGetDataListener() {
            @Override
            public void onGetData(String result) {
                if (TextUtils.isEmpty(result)) {
                    if(toast!=null){
                        toast.cancel();
                    }
                    toast  = Toast.makeText(context,context.getString(R.string.interface_returns_failed),Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject item = jsonArray.getJSONObject(0);
                    String sStatus = item.getString("status");
                    String sMsg = item.getString("msg");
                    String sDateNow = "";
                    if(item.has("sDateNow")){
                        sDateNow = item.getString("sDateNow");
                    }
                    if(TextUtils.isEmpty(sDateNow)){
                        sDateNow = DateUtils.longToDate(System.currentTimeMillis());
                    }
                    SharedPreferencesUtil.saveValue(context,"nSMTLineTimeLastRefreshTime",DateUtils.dateToLong(sDateNow));
                    if(!TextUtils.isEmpty(time)){
                        refreshTime = sDateNow;
                        switch (time){
                            case "refreshTime":
                                Message message = new Message();
                                message.obj = refreshTime;
                                message.what = 1;
                                handler.sendMessage(message);
                                break;
                            
                        }

                    }
                    moduleList = new ArrayList<>();
                    if (sStatus.equalsIgnoreCase("OK")) {
                        String sData = item.getString("data");
                        JSONArray jsonArrayData = new JSONArray(sData);
                        moduleList = GsonUtils.getObjects(jsonArrayData.toString(), ModuleInfoBean.class);

                    } else {
                        ToastUtil.showShortToast(context,sMsg);
                        return;
                    }
                    if(moduleList!=null && moduleList.size()>=1){
                        for (int i = 0; i < moduleList.size(); i++) {
                            ModuleInfoBean moduleInfoBean = moduleList.get(i);
                            switch (moduleInfoBean.getMODULE_NO()){
                                case "MOD_WO"://制令单模块
                                    nSMTLineWoHeartbeatInteval = Long.parseLong(moduleInfoBean.getHEARTBEAT_INTEVAL());
                                    break;
                                case "MOD_WOOUTPUT"://制令单产出信息模块
                                    nSMTLineWoProductionHeartbeatInteval = Long.parseLong(moduleInfoBean.getHEARTBEAT_INTEVAL());
                                    break;
                                case "MOD_PERIOD"://时段模块
                                    nSMTLinePeriodHeartbeatInteval = Long.parseLong(moduleInfoBean.getHEARTBEAT_INTEVAL());
                                    break;
                                case "MOD_FEEDREFLASH"://欠料预警模块
                                    nSMTLineWarnHeartbeatInteval = Long.parseLong(moduleInfoBean.getHEARTBEAT_INTEVAL());
                                    break;
                                case "MOD_PERSON"://人员岗位模块
                                    nSMTLineEmployeeHeartbeatInteval = Long.parseLong(moduleInfoBean.getHEARTBEAT_INTEVAL());
                                    break;
                                case "MOD_MESSAGE"://公告信息模块
                                    nSMTLineMessageHeartbeatInteval = Long.parseLong(moduleInfoBean.getHEARTBEAT_INTEVAL());
                                    break;
                                case "MOD_CUR_TIME"://同步当前时间模块
                                    nSMTLineTimeHeartbeatInteval = Long.parseLong(moduleInfoBean.getHEARTBEAT_INTEVAL());
                                    break;
                                case "MOD_MACHINE_RUN_RATE"://设备运转率模块
                                    nSMTLineOperatingRateHeartbeatInteval = Long.parseLong(moduleInfoBean.getHEARTBEAT_INTEVAL());
                                    break;
                                case "MOD_SHIFT"://获取班次信息
                                    nSMTLineShiftHeartbeatInteval = Long.parseLong(moduleInfoBean.getHEARTBEAT_INTEVAL());
                                    break;
                            }
                        }
                    }else {
//                        ToastUtil.showShortToast(context,String.format(getString(R.string.parse_error),methodname));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if(toast!=null){
                        toast.cancel();
                    }
                    toast  = Toast.makeText(context,e.getMessage().toString(),Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onGetDataError(String error) {

            }
        });
    }

    //获取看板模板信息
    public void getServerDate() {
        getServerDate(sSMTLineLanguage, new OnGetDataListener() {
            @Override
            public void onGetData(String result) {
                if (TextUtils.isEmpty(result)) {
                    if(toast!=null){
                        toast.cancel();
                    }
                    toast  = Toast.makeText(context,context.getString(R.string.interface_returns_failed),Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject item = jsonArray.getJSONObject(0);
                    String sStatus = item.getString("status");
                    String sMsg = item.getString("msg");
                    String sDateNow = "";
                    if(item.has("sDateNow")){
                        sDateNow = item.getString("sDateNow");
                    }
                    if(TextUtils.isEmpty(sDateNow)){
                        sDateNow = DateUtils.longToDate(System.currentTimeMillis());
                    }

                    if(!TextUtils.isEmpty(time)){
                        String cTime = sDateNow;
                        switch (time){
                            case "curTime":
                                Message message = new Message();
                                message.obj = cTime;
                                message.what = 1;
                                handler.sendMessage(message);
                                break;
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if(toast!=null){
                        toast.cancel();
                    }
                    toast  = Toast.makeText(context,e.getMessage().toString(),Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onGetDataError(String error) {

            }
        });
    }

    //获取设备运转率信息
    public void getOperatingRate(String sLineNo) {
        getOperatingRate(sLineNo, sSMTLineLanguage, new OnGetDataListener() {
            @Override
            public void onGetData(String result) {
                if (TextUtils.isEmpty(result)) {
                    if(toast!=null){
                        toast.cancel();
                    }
                    toast  = Toast.makeText(context,context.getString(R.string.interface_returns_failed),Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject item = jsonArray.getJSONObject(0);
                    String sStatus = item.getString("status");
                    String sMsg = item.getString("msg");
                    String sDateNow = "";
                    if (item.has("sDateNow")) {
                        sDateNow = item.getString("sDateNow");
                    }
                    if (TextUtils.isEmpty(sDateNow)) {
                        sDateNow = DateUtils.longToDate(System.currentTimeMillis());
                    }
                    SharedPreferencesUtil.saveValue(context, "nSMTLineOperatingRateLastRefreshTimeV2",DateUtils.dateToLong(sDateNow));
                    if (!TextUtils.isEmpty(time)) {
                        String cTime = sDateNow;
                        switch (time) {
                            case "refreshTime":
                                Message message = new Message();
                                message.obj = cTime;
                                message.what = 1;
                                handler.sendMessage(message);
                                break;
                        }

                    }
                    operatingRateBeans = new ArrayList<>();
                    if (sStatus.equalsIgnoreCase("OK")) {
                        String sData = item.getString("data");
                        JSONArray jsonArrayData = new JSONArray(sData);
                        operatingRateBeans = GsonUtils.getObjects(jsonArrayData.toString(), OperatingRateBean.class);
                    } else {
                        if(toast!=null){
                            toast.cancel();
                        }
                        toast  = Toast.makeText(context,sMsg,Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    if (operatingRateBeans != null && operatingRateBeans.size() >= 1) {
                        showOperatingRatePieChartView(viewOperatingRate,operatingRateBeans,nChartOperatingRateColor,sOperatingRateTitle,sOperatingRatePlotTitle);
                    } else {
//                        ToastUtil.showShortToast(context,String.format(getString(R.string.parse_error),methodname));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if(toast!=null){
                        toast.cancel();
                    }
                    toast  = Toast.makeText(context,e.getMessage().toString(),Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onGetDataError(String error) {

            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        LogUtil.e("finish");
    }

    /**
     * 判断当前模板语言
     *
     * @return
     */
    private int getCurLanguage() {
        int i = 0;
        if (curDisplayLanguage.equalsIgnoreCase("E") && !getLocalLanguage().equalsIgnoreCase(curDisplayLanguage)) {
            i = 1;
        } else if (curDisplayLanguage.equalsIgnoreCase("T") && !getLocalLanguage().equalsIgnoreCase(curDisplayLanguage)) {
            i = 2;
        } else {
            i = 0;
        }
        return i;
    }


    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.e(TAG + "onPause");
        stopScroll();
        isStopReceiver = false;
        stopHandler();
        SharedPreferencesUtil.saveValue(context, "lineIndex", lineIndex);
        sStopService();
        stopTimer();
        stopToast();
    }
    private void stopToast(){
        if(toast!=null){
            toast.cancel();
        }
    }
    private void stopHandler(){
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
    /**
     * 停止公告消息滚动
     */
    private void stopScroll() {
        if (scrollTextView != null) {
            messageStopFlag = true;
            scrollTextView.stopScroll();//停止滚动
            setVisiblebulletin(View.GONE);
            scrollTextView = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.e(TAG + "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.e(TAG + "onDestroy");

    }

    //返回
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}

