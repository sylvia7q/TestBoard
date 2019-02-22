package com.topcee.kanban.ui.activity.pattem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;


import com.topcee.kanban.R;
import com.topcee.kanban.app.MyApplication;
import com.topcee.kanban.bean.KANBAN_MC_PATTEM;
import com.topcee.kanban.bean.KANBAN_USER_PATTEM;
import com.topcee.kanban.bean.LineNoProductionBean;
import com.topcee.kanban.bean.LineWorkStatusBean;
import com.topcee.kanban.bean.MessageBean;
import com.topcee.kanban.bean.ModuleInfoBean;
import com.topcee.kanban.bean.SMT_WORKSHOP_LINE_SETTING;
import com.topcee.kanban.bean.ShiftBean;
import com.topcee.kanban.bean.WoBean;
import com.topcee.kanban.bean.WoProductionBean;
import com.topcee.kanban.bean.WorkshopTableBean;
import com.topcee.kanban.database.DBUtil;
import com.topcee.kanban.http.SMTTotalAlarmReceiver;
import com.topcee.kanban.http.SMTTotalAlarmService;
import com.topcee.kanban.presenter.OnGetDataListener;
import com.topcee.kanban.presenter.OnResultListener;
import com.topcee.kanban.ui.activity.base.PattemMenuActivity;
import com.topcee.kanban.ui.activity.base.module.WorkshopLineSettingActivity;
import com.topcee.kanban.ui.activity.base.set.SettingSMTWorkShopBoardActivity;
import com.topcee.kanban.ui.view.module.MarqueeView;
import com.topcee.kanban.ui.view.module.MultiaxialLineNoProduction;
import com.topcee.kanban.ui.view.module.WorkshopTableViewV3;
import com.topcee.kanban.utils.AlarmManagerUtil;
import com.topcee.kanban.utils.DateUtils;
import com.topcee.kanban.utils.GsonUtils;
import com.topcee.kanban.utils.LogUtil;
import com.topcee.kanban.utils.SharedPreferencesUtil;
import com.topcee.kanban.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

/**
 * [用到此模板的客户为DOOV]
 * 区别在于,DOOV使用粉色作为背景，标题栏部分用了DOOV公司专用LOGO，并且滚动条内容显示在标题栏中间
 * 车间看板模板
 */
public class SMTWorkShopBoardV3Activity extends BasePattemV3WorkshopV4LineActivity {

    @BindView(R.id.activity_workshop_board_view_table)
    WorkshopTableViewV3 viewTable;

    @BindView(R.id.activity_workshop_board_view_line_production)
    MultiaxialLineNoProduction viewLineProduction;

    private final String TAG = SMTWorkShopBoardV3Activity.class.getSimpleName();
    private Context context;
    private String sLineNoIn = ""; //SMT2-2 1B

    //颜色信息
    private int nSMTTotalTitleColor;
    private int nSMTTotalOtherTextColor;
    private int nSMTTotalWholeBgColor;
    private int nSMTTotalChartBgColor;
    private Integer nChartLineProductColor[];
    private int nChartLineProductColor1;
    private int nChartLineProductColor2;

    private String disCurTime = "";//显示当前时间
    private String disReTime = "";//显示最后刷新时间
    private String disCopyright = "";//显示版权信息
    private String disMessage = "";//显示公告
    private String time = "";

    private static int mTaskId = 1;// 模拟的task id
    private String isAutoPattem = "N";//是否为自动展示模板

    //展示模板
    private List<KANBAN_MC_PATTEM> mcPattemList;
    private KANBAN_MC_PATTEM mc_pattem;
    private List<KANBAN_MC_PATTEM> allMcPattems;
    private String pattemNo = "";
    private int mcIndex = 0;
    private String sSMTWorkshopLanguage = "S";//展示模板语言
    private String curDisplayLanguage = "S";//展示模板语言
    private long nSMTTotalMcPattemLastRefreshTime = 0l;//上次模板展示时间
    private long mcPattemCurRefreshTime = 0l;//当前模板展示时间
    private long pattemHeartbeatInteval = 1l;
    //用户模板
    private KANBAN_USER_PATTEM user_pattem;
    private String kanbanMcNo = "";

    private boolean isStopReceiver = true;

    //模块信息
    private List<WoBean> woBeanList;//制令单信息
    private List<WoProductionBean> woProductionBeanList;//制令单产出信息
    private List<LineWorkStatusBean> lineWorkStatusBeanList;//获取线别状态
    private List<LineNoProductionBean> lineNoProductionBeanList;//显示-双柱状图-线别产能
    private List<ShiftBean> shiftList;
    private List<MessageBean> messageList;
    private boolean messageFlag = true;
    private boolean messageStopFlag = false;
    private MarqueeView scrollTextView;
    private String scrollText = "";
    private String newScrollText = "";
    private List<ModuleInfoBean> moduleList;

    private String smtTitleText = "";//标题
    //模块刷新时间间隔
    private long nSMTTotalWoLastRefreshTime = 0l;//制令单信息上次刷新时间
    private long nSMTTotalWoHeartbeatInteval = 0l;//制令单刷新时间间隔
    private long nSMTTotalWoProductionLastRefreshTime = 0l;//制令单上次产出信息刷新时间
    private long nSMTTotalWoProductionHeartbeatInteval = 0l;//制令单产出信息时间间隔
    private long nSMTTotalLineStatusLastRefreshTime = 0l;//线别状态上次刷新时间
    private long nSMTTotalLineStatusHeartbeatInteval = 0l;//线别状态刷新时间间隔
    private long nSMTTotalLineProductionLastRefreshTime = 0l;//线别产出刷新时间
    private long nSMTTotalLineProductionHeartbeatInteval = 0l;//线别产出刷新时间
    private long nSMTTotalMessageLastRefreshTime = 0l;//公告消息上次刷新时间
    private long nSMTTotalMessageHeartbeatInteval = 0l;//公告消息刷新时间间隔
    private long nSMTTotalShiftLastRefreshTime = 0l;//班次上次刷新时间
    private long nSMTTotalShiftHeartbeatInteval = 0l;//班次刷新时间间隔

    private boolean blIsInitData = true;//第一次进来不管刷新时间是否到了，都执行一次
    private boolean blRefreshRate = true;//第一次进来不管刷新时间是否到了，都执行一次

    private boolean IsGetData = false; //调用后台接口获取数据(当显示最后一页数据时，IsGetData = true)
    private boolean woTimeRefresh = false;
    private boolean woProductionTimeRefresh = false;
    private boolean lineStatusTimeRefresh = false;
    private boolean lineProductionTimeRefresh = false;
    private boolean shiftTimeRefresh = false;
    private boolean messageTimeRefresh = false;

    private Timer timer;
    private TimerTask timerTask;
    private Toast toast;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if(!TextUtils.isEmpty((String) msg.obj)){
                        bottom.setCurrentDate((String) msg.obj);
                    }else {
                        bottom.setCurrentDate(DateUtils.longToDate(System.currentTimeMillis()));
                    }
                    break;
                case 2:
                    getServerDate();//同步服务器时间
                    break;
                case 3:
                    setCurrentDate((String) msg.obj);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e("SMTWorkShopBoardActivity-----onCreate");
        context = SMTWorkShopBoardV3Activity.this;

    }

    @Override
    protected int getModuleLayoutId() {
        return R.layout.activity_smt_work_shop_board_v3;
    }

    @Override
    protected void initUI() {
        initTitle();
    }
    private void initTitle(){
        menuTitle.setCommonTitle(View.GONE, View.GONE, View.GONE, View.VISIBLE);
        menuTitle.setBtnRight(R.drawable.icon_gear, 0);
        getLlWholeBg().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(menuTitle.getVisibility()==View.VISIBLE) {
                    menuTitle.setVisibility(View.GONE);
                } else {
                    menuTitle.setVisibility(View.VISIBLE);
                    menuTitle.getBtnRight().requestFocus();
                }

                return true;
            }
        });
        menuTitle.setBtnRightOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(context, SettingSMTWorkShopBoardActivity.class,"Activity","SMTWorkShopBoardV3Activity");
            }
        });
    }

    /**
     * 初始化颜色
     */
    private void initColor(){
        nSMTTotalTitleColor = SharedPreferencesUtil.getValue(getBaseContext(), "nSMTTotalTitleColor", MyApplication.nDefaultSMTTotalTitleColor);
        nSMTTotalOtherTextColor = SharedPreferencesUtil.getValue(getBaseContext(), "nSMTTotalOtherTextColor", MyApplication.nDefaultSMTTotalOtherTextColor);
        nSMTTotalWholeBgColor = SharedPreferencesUtil.getValue(getBaseContext(), "nSMTTotalV3WholeBgColor", MyApplication.nDefaultSMTTotalV3WholeBgColor);//V3车间看板默认背景色为粉色
        nSMTTotalChartBgColor = SharedPreferencesUtil.getValue(getBaseContext(), "nSMTTotalChartBgColor", MyApplication.nDefaultSMTTotalChartBgColor);
        nChartLineProductColor1 = SharedPreferencesUtil.getValue(getBaseContext(), "nChartLineProductColor1", MyApplication.nDefaultChartLineProductColor1);
        nChartLineProductColor2 = SharedPreferencesUtil.getValue(getBaseContext(), "nChartLineProductColor2", MyApplication.nDefaultChartLineProductColor2);
        nChartLineProductColor = new Integer[]{nChartLineProductColor1,nChartLineProductColor2};
        setPattemTitleTextColor(nSMTTotalTitleColor);
        setOtherTextColor(nSMTTotalOtherTextColor);
        setWholeBgColor(nSMTTotalWholeBgColor);
        setChartBgColor(nSMTTotalChartBgColor);

    }

    /**
     * 标题颜色
     * @param color
     */
    private void setPattemTitleTextColor(int color){
        menuTitle.setBtnLeftTextColor(color);
        //线别
        setTitleTextColor(color);
//        setBulletinTextColor(color);
        //时间、班次、时段
        bottom.setCurrentDateTextColor(color);
        bottom.setShiftTextColor(color);
        bottom.setShiftTimeTextColor(color);
        bottom.setCompanyTextColor(color);

    }
    /**
     * 标题默认颜色
     * @param color
     */
    private void setOtherTextColor(int color){
        viewTable.setWorkshopTableInfoTextColor(color);
    }

    /**
     * 设置整个线板的默认的背景颜色
     */
    private void setWholeBgColor(int color){
        setTemplateWholeBg(color);
        setBulletinBgColor(color);
    }

    /**
     * 设置图标控件的默认的背景颜色
     */
    private void setChartBgColor(int color){
        viewLineProduction.setBackgroundColor(color);//时段产量
    }
    /**
     * 初始化模块界面
     */
    private void initModuleUI(String disCurTime, String disReTime, String disCopyright, String disMessage) {
        if( showUI(disCurTime)|| showUI(disReTime) ){
            bottom.setCurrentDateVisibility(View.VISIBLE);
            String getCurTimeByServer = SharedPreferencesUtil.getValue(context,"getCurTimeByServer","");
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
            bottom.setCurrentDateVisibility(View.GONE);
        }
//        disCurTime(disCurTime);
//        disReTime(disReTime);
        disCopyright(disCopyright);
        disMessage(disMessage);

    }

    /**
     * 启动定时器
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
     * 停止计时器
     */
    private void stopTimer(){
        timer.cancel();
        timerTask.cancel();
    }
    /**
     * 判断当前模板语言
     * @return
     */
    private int getCurLanguage(){
        int i = 0;
        if(curDisplayLanguage.equalsIgnoreCase("E") && !getLocalLanguage().equalsIgnoreCase(curDisplayLanguage)){
            i = 1;
        }else if(curDisplayLanguage.equalsIgnoreCase("T") && !getLocalLanguage().equalsIgnoreCase(curDisplayLanguage)){
            i = 2;
        }else {
            i = 0;
        }
        return i;
    }
    /**
     * 设置模块英语
     */
    private void initCurUI(){
        if(getCurLanguage() == 1){
            viewTable.setTxtNumber("Serial number");
            viewTable.setTxtLineNo("Line no");
            viewTable.setTxtPartNo("product no");
            viewTable.setTxtWo("Order");
            viewTable.setTxtWoPlanQty("Quantity");
            viewTable.setTxtFinishingRate("Order completion rate");
            viewTable.setTxtAchievingRate("Achieving rate");
            viewTable.setTxtWorkstatusName("Line status");
            bottom.setCompanyText("Copyright© TOPCEE TECHNOLOGIES Co., Ltd.");
            menuTitle.setBtnLeft("back");
        }else if(getCurLanguage() == 2){
            viewTable.setTxtNumber("序號");
            viewTable.setTxtLineNo("線別");
            viewTable.setTxtPartNo("產品號");
            viewTable.setTxtWo("制令單");
            viewTable.setTxtWoPlanQty("數量");
            viewTable.setTxtFinishingRate("制令單完成率");
            viewTable.setTxtAchievingRate("達成率");
            viewTable.setTxtWorkstatusName("線體狀態");
            bottom.setCompanyText("版權所有©深圳市拓思科技有限公司 www.topcee.com");
            menuTitle.setBtnLeft("返回");
        }

    }
    /**
     * 根据参数觉得决定是否显示UI
     *
     * @param
     * @return
     */
    private void disCurTime(String disCurTime) {
        if (showUI(disCurTime)) {
            bottom.setCurrentDateVisibility(View.VISIBLE);
        } else {
            bottom.setCurrentDateVisibility(View.GONE);
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
            bottom.setCurrentDateVisibility(View.VISIBLE);
        } else {
            bottom.setCurrentDateVisibility(View.GONE);
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
            bottom.setCompanyVisibility(View.VISIBLE);
        } else {
            bottom.setCompanyVisibility(View.GONE);
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
        LogUtil.e("SMTWorkShopBoardActivity-----onStart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.e("SMTWorkShopBoardActivity-----onResume");
        isAutoPattem = getIntent().getStringExtra("isAutoPattem");
        getLlWholeBg().requestFocus();
        blIsInitData = true;
        messageFlag = true;
        String sKanBanMCNo = SharedPreferencesUtil.getValue(getBaseContext(), "KANBAN_MC_NO", "");
        getPattem(sKanBanMCNo);

    }

    //获取看板模板信息
    public void getPattem(String sKanBanMCNo) {
        getKanBanUserPattem(context, sKanBanMCNo, new OnResultListener() {
            @Override
            public void onSuccess(String sDateNow) {
                DBUtil dbUtil = new DBUtil(context);
                mc_pattem = dbUtil.getMcPattemByCode(template_code[6]);//更新展示模板
                user_pattem = dbUtil.getUserPattemByCode(template_code[6]);//更新展示模板
                if(user_pattem!=null){
                    pattemNo = user_pattem.getPATTEM_NO();
                    disCurTime = user_pattem.getIS_DISP_CURRENTTIME();
                    disReTime = user_pattem.getIS_DISP_REFRESH_TIME();
                    disCopyright = user_pattem.getIS_DISP_COPYRIGHT();
                    disMessage = user_pattem.getIS_DISP_MESSAGE();
                    pattemHeartbeatInteval = Long.parseLong(user_pattem.getHEARTBEAT_INTEVAL());
                    if(mc_pattem!=null){
                        allMcPattems = dbUtil.getAllMcPattem();
                        mcIndex = Integer.parseInt(mc_pattem.getSORTID());
                        pattemNo = mc_pattem.getPATTEM_NO();
                        curDisplayLanguage = mc_pattem.getDISPLAY_LANGUAGE();
                        if(!curDisplayLanguage.equals(getLocalLanguage())){
                            sSMTWorkshopLanguage = curDisplayLanguage;
                        }else {
                            sSMTWorkshopLanguage = MyApplication.sLanguage;
                        }
                        String rTime = sDateNow;//获取时间判断是否到了切换模板的时间
                        SharedPreferencesUtil.saveValue(context,"nSMTTotalMcPattemLastRefreshTimeV3",DateUtils.dateToLong(rTime));
                        if(!TextUtils.isEmpty(time)){
                            switch (time){
                                case "refreshTime":
                                    Message message = new Message();
                                    message.obj = rTime;
                                    message.what = 1;
                                    handler.sendMessage(message);
                                    break;
                            }

                        }
                    }
                    smtTitleText = user_pattem.getPATTEM_TITLE();
                    setTitleText(smtTitleText);
                    initColor();
                    initModuleUI( disCurTime, disReTime, disCopyright, disMessage);//初始化模块界面
                    initCurUI();

                    init();
                }
            }
            @Override
            public void onFail(String result) {
                if(toast!=null){
                    toast.cancel();
                }
                toast  = Toast.makeText(context,result,Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    /**
     * 获取模块信息
     */
    private void init() {

        sLineNoIn = SharedPreferencesUtil.getValue(context,"WorkshopLineNo","");
        if(TextUtils.isEmpty(sLineNoIn)){
            openActivity(context, WorkshopLineSettingActivity.class);
            return;
        }
        //判断线别、工站是否设置
        if(isAutoPattem.equals("Y")){
            isStopReceiver = true;
            startService();
            initService();
        }else {
            isStopReceiver = true;
            startService();
            initService();
        }
    }
    //启动Service
    private void startService() {

        Intent i = new Intent(context, SMTTotalAlarmService.class);
        // 获取10秒之后的日期时间字符串
        i.putExtra("alarm_time", DateUtils.getNLaterDateTimeString(Calendar.SECOND, -1));
        i.putExtra("task_id", mTaskId);
        i.putExtra("nRefreshTimeSet", pattemHeartbeatInteval);
        startService(i);
    }

    //停止Service
    private void sStopService() {
        LogUtil.e(TAG,"stopService");
        Intent intent = new Intent(context, SMTTotalAlarmService.class);
        stopService(intent);
        AlarmManagerUtil.cancelAlarmBroadcast(context,mTaskId,SMTTotalAlarmReceiver.class);
    }
    SMTTotalAlarmReceiver smtTotalAlarmReceiver = new SMTTotalAlarmReceiver();

    /**
     * 广播回调消息
     */
    private void initService(){
        smtTotalAlarmReceiver.setOnReceivedMessageListener(new SMTTotalAlarmReceiver.OnAlarmMessageListener() {
            @Override
            public void onReceived(String message) {
                if(isStopReceiver){
                    DBUtil dbUtil = new DBUtil(context);
                    if(isAutoPattem.equals("Y")){
                        nSMTTotalMcPattemLastRefreshTime = SharedPreferencesUtil.getValue(context,"nSMTTotalMcPattemLastRefreshTimeV3",0l);
                        boolean timeRefresh = DateUtils.isRefreshData("切换模板",DateUtils.longToDate(nSMTTotalMcPattemLastRefreshTime), Long.parseLong(mc_pattem.getDISPLAY_TIME()));
                        if(timeRefresh) {
                            LogUtil.e(TAG,"到了切换模板的时间");
                            if (mcIndex < allMcPattems.size()) {
                                mcIndex++;
                            } else if (mcIndex >= allMcPattems.size()) {
                                mcIndex = 1;
                            }
                            if(allMcPattems!=null && allMcPattems.size() == 1){
                                initData();
                                return;
                            }
                            KANBAN_MC_PATTEM tempPattem = dbUtil.getMcPattemBySortId(String.valueOf(mcIndex));
                            if (tempPattem != null) {
                                String className = getClassName(getTemplateActivity(), tempPattem.getINTERNAL_CODE());
                                LogUtil.e(TAG,"当前要切换的模板为" + className);
                                if(!TextUtils.isEmpty(className)){
                                    if(!className.equals("com.topcee.kanban.ui.activity.pattem.SMTWorkShopBoardV3Activity")) {
                                        openActivity(context, className, "isAutoPattem", isAutoPattem);
                                        SMTWorkShopBoardV3Activity.this.finish();
                                    }else {
                                        initData();
                                    }
                                    return;
                                }else {
                                    initData();
                                    return;
                                }
                            }else {
                                initData();
                                return;
                            }
                        }else {
                            LogUtil.e(TAG,"还没到切换模板的时间");
                            initData();
                        }
                    }else {
                        LogUtil.e(TAG,"不是自动模式");
                        initData();
                    }
                }
            }
        });

    }

    /**
     * 初始化数据
     */
    private void initData(){

        nSMTTotalWoLastRefreshTime = SharedPreferencesUtil.getValue(context,"nSMTTotalWoLastRefreshTimeV3",0l);
        nSMTTotalWoProductionLastRefreshTime = SharedPreferencesUtil.getValue(context,"nSMTTotalWoProductionLastRefreshTimeV3",0l);
        nSMTTotalLineStatusLastRefreshTime = SharedPreferencesUtil.getValue(context,"nSMTTotalLineStatusLastRefreshTimeV3",0l);
        nSMTTotalLineProductionLastRefreshTime = SharedPreferencesUtil.getValue(context,"nSMTTotalLineProductionLastRefreshTimeV3",0l);
        nSMTTotalShiftLastRefreshTime = SharedPreferencesUtil.getValue(context,"nSMTTotalShiftLastRefreshTimeV3",0l);
        nSMTTotalMessageLastRefreshTime = SharedPreferencesUtil.getValue(context,"nSMTTotalMessageLastRefreshTimeV3",0l);

        woTimeRefresh = DateUtils.isRefreshData("制令单",DateUtils.longToDate(nSMTTotalWoLastRefreshTime),nSMTTotalWoHeartbeatInteval);
        lineStatusTimeRefresh = DateUtils.isRefreshData("生产状态",DateUtils.longToDate(nSMTTotalLineStatusLastRefreshTime),nSMTTotalLineStatusHeartbeatInteval);
        woProductionTimeRefresh = DateUtils.isRefreshData("制令单产出",DateUtils.longToDate(nSMTTotalWoProductionLastRefreshTime),nSMTTotalWoProductionHeartbeatInteval);

        lineProductionTimeRefresh = DateUtils.isRefreshData("线别产能",DateUtils.longToDate(nSMTTotalLineProductionLastRefreshTime),nSMTTotalLineProductionHeartbeatInteval);
        shiftTimeRefresh = DateUtils.isRefreshData("班次",DateUtils.longToDate(nSMTTotalShiftLastRefreshTime),nSMTTotalShiftHeartbeatInteval);
        messageTimeRefresh = DateUtils.isRefreshData("公告消息",DateUtils.longToDate(nSMTTotalMessageLastRefreshTime),nSMTTotalMessageHeartbeatInteval);

        if (blIsInitData || (woTimeRefresh && woProductionTimeRefresh && lineStatusTimeRefresh)) {
            getTimsGetWoInfo("SMT",sLineNoIn,"");
            getWorkStatus("SMT",sLineNoIn);
            getWoProductionInfo("SMT",sLineNoIn);
        }
        if(blIsInitData || lineProductionTimeRefresh){
            LogUtil.e(TAG,"线别产能刷新时间到了");
            getLineProductionInfo("SMT",sLineNoIn);
        }
        if(blIsInitData || shiftTimeRefresh){
            LogUtil.e(TAG,"班次刷新时间到了");
            getShiftInfo("SMT",sLineNoIn);
        }
        String json = javaToJson();
        LogUtil.e(TAG,"json = " + json);
        if (showUI(disMessage)){
            if(blIsInitData || messageTimeRefresh){
                getKanBanMsg("",json);
            }
        }
        getModuleInfo(pattemNo);
        blIsInitData=false;

    }



    //获取制令单数据
    public void getTimsGetWoInfo(String sInterfaceType, final String sLineNoIn, String sWorkShopNo) {
        getTimsGetWoInfo(sInterfaceType, sLineNoIn, sWorkShopNo, sSMTWorkshopLanguage, new OnGetDataListener() {
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
                    SharedPreferencesUtil.saveValue(context, "nSMTTotalWoLastRefreshTimeV3", DateUtils.dateToLong(sDateNow));
                    String rTime = sDateNow;
                    if (!TextUtils.isEmpty(time)) {
                        switch (time) {
                            case "refreshTime":
                                Message message = new Message();
                                message.obj = rTime;
                                message.what = 1;
                                handler.sendMessage(message);
                                break;
                        }

                    }
                    woBeanList = new ArrayList<>();
                    if (sStatus.equalsIgnoreCase("OK")) {
                        String sData = item.getString("data"); //时段数据
                        JSONArray itemData = new JSONArray(sData);
                        woBeanList = GsonUtils.getObjects(itemData.toString(), WoBean.class);

                    } else {
                        ToastUtil.showLongToast(context, sMsg);
                    }

                    List<WorkshopTableBean> workshopTableBeans = new ArrayList<>();
                    String sLineNoTemp[] = sLineNoIn.split(",");
                    for (int i = 0; i < sLineNoTemp.length; i++) {
                        WorkshopTableBean workshopTableBean = new WorkshopTableBean();
                        workshopTableBean.setNumber((i + 1) + "");
                        workshopTableBean.setLineNo(sLineNoTemp[i]);
                        if (woBeanList != null && woBeanList.size() >= 1) {
                            for (int j = 0; j < woBeanList.size(); j++) {
                                WoBean woBean = woBeanList.get(j);

                                if(sLineNoTemp[i].equals(woBean.getsLineNo())){
                                    workshopTableBean.setWoPlanQty(woBean.getsWoQtyPlan());
                                    workshopTableBean.setProductNo(woBean.getsProductNo());
                                }
                            }
                        } else {
//                        ToastUtil.showShortToast(context,String.format(getString(R.string.parse_error),methodname));
                        }
                        if (woProductionBeanList != null && woProductionBeanList.size() >= 1) {
                            for (int j = 0; j < woProductionBeanList.size(); j++) {
                                WoProductionBean woProductionBean = woProductionBeanList.get(j);
                                if (sLineNoTemp[i].equals(woProductionBean.getsLineNo())) {
                                    workshopTableBean.setWo(woProductionBean.getsWo());
                                    workshopTableBean.setFinishingRate(woProductionBean.getsFinishingRate());//完成率
                                    workshopTableBean.setAchievingRate(woProductionBean.getsAchievingRate());//达成率
                                    workshopTableBean.setQtyOutput(woProductionBean.getsQtyOutput());//产出数
                                }
                            }
                        }
                        if (lineWorkStatusBeanList != null && lineWorkStatusBeanList.size() >= 1) {
                            for (int j = 0; j < lineWorkStatusBeanList.size(); j++) {
                                if (sLineNoTemp[i].equals(lineWorkStatusBeanList.get(j).getsLineNo())) {
                                    workshopTableBean.setWorkStatusNo(lineWorkStatusBeanList.get(j).getsWorkStatus());
                                    workshopTableBean.setWorkStatusName(lineWorkStatusBeanList.get(j).getsWorkStatusName());
                                    workshopTableBean.setRgb(lineWorkStatusBeanList.get(j).getsRgb());
                                }

                            }
                        }

                        workshopTableBeans.add(workshopTableBean);

                    }


                    if (workshopTableBeans != null) {
                        viewTable.initTableView(context,workshopTableBeans);
                        SharedPreferencesUtil.saveValue(getBaseContext(), "lastListItemRefreshTime", System.currentTimeMillis());
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

    //获取制令单产出信息
    public void getWoProductionInfo(String sInterfaceType, final String sLineNoIn) {
        getWoProductionInfo(sInterfaceType, sLineNoIn, sSMTWorkshopLanguage, new OnGetDataListener() {
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
                    SharedPreferencesUtil.saveValue(context, "nSMTTotalWoProductionLastRefreshTimeV3", DateUtils.dateToLong(sDateNow));
                    String rTime = sDateNow;
                    if (!TextUtils.isEmpty(time)) {
                        switch (time) {
                            case "refreshTime":
                                Message message = new Message();
                                message.obj = rTime;
                                message.what = 1;
                                handler.sendMessage(message);
                                break;
                        }

                    }
                    if (sStatus.equalsIgnoreCase("OK")) {
                        String sData = item.getString("data");
                        JSONArray jsonArrayData = new JSONArray(sData);
                        woProductionBeanList = GsonUtils.getObjects(jsonArrayData.toString(), WoProductionBean.class);
                    } else {
                        ToastUtil.showLongToast(context, sMsg);
                    }
                    List<WorkshopTableBean> workshopTableBeans = new ArrayList<>();
                    String sLineNoTemp[] = sLineNoIn.split(",");
                    for (int i = 0; i < sLineNoTemp.length; i++) {
                        WorkshopTableBean workshopTableBean = new WorkshopTableBean();
                        workshopTableBean.setNumber((i + 1) + "");
                        workshopTableBean.setLineNo(sLineNoTemp[i]);
                        if (woProductionBeanList != null && woProductionBeanList.size() >= 1) {
                            for (int j = 0; j < woProductionBeanList.size(); j++) {
                                WoProductionBean woProductionBean = woProductionBeanList.get(j);
                                if(sLineNoTemp[i].equals(woProductionBean.getsLineNo())){
                                    workshopTableBean.setWo(woProductionBean.getsWo());
                                    workshopTableBean.setFinishingRate(woProductionBean.getsFinishingRate());//完成率
                                    workshopTableBean.setAchievingRate(woProductionBean.getsAchievingRate());//达成率
                                    workshopTableBean.setQtyOutput(woProductionBean.getsQtyOutput());//线体总产量
                                }
                            }
                        }
                        if (lineWorkStatusBeanList != null && lineWorkStatusBeanList.size() >= 1) {
                            for (int j = 0; j < lineWorkStatusBeanList.size(); j++) {
                                if (sLineNoTemp[i].equals(lineWorkStatusBeanList.get(j).getsLineNo())) {
                                    workshopTableBean.setWorkStatusNo(lineWorkStatusBeanList.get(j).getsWorkStatus());
                                    workshopTableBean.setWorkStatusName(lineWorkStatusBeanList.get(j).getsWorkStatusName());
                                    workshopTableBean.setRgb(lineWorkStatusBeanList.get(j).getsRgb());
                                }
                            }
                        }
                        if (woBeanList != null && woBeanList.size() >= 1) {
                            for (int j = 0; j < woBeanList.size(); j++) {
                                if (sLineNoTemp[i].equals(woBeanList.get(j).getsLineNo())) {
                                    workshopTableBean.setWoPlanQty(woBeanList.get(j).getsWoQtyPlan());
                                    workshopTableBean.setProductNo(woBeanList.get(j).getsProductNo());
                                }
                            }
                        }
                        workshopTableBeans.add(workshopTableBean);
                    }

                    if (workshopTableBeans != null) {
                        viewTable.initTableView(context,workshopTableBeans);
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

    //获取生产状态
    public void getWorkStatus(String sInterfaceType, final String sLineNoIn) {
        getWorkStatus(sInterfaceType, sLineNoIn, sSMTWorkshopLanguage, new OnGetDataListener() {
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
                    SharedPreferencesUtil.saveValue(context, "nSMTTotalLineStatusLastRefreshTimeV3", DateUtils.dateToLong(sDateNow));
                    if (!TextUtils.isEmpty(time)) {
                        String rTime = sDateNow;
                        switch (time) {
                            case "refreshTime":
                                Message message = new Message();
                                message.obj = rTime;
                                message.what = 1;
                                handler.sendMessage(message);
                                break;
                        }

                    }
                    if (sStatus.equalsIgnoreCase("OK")) {
                        String sData = item.getString("data");
                        JSONArray jsonArrayData = new JSONArray(sData);
                        lineWorkStatusBeanList = GsonUtils.getObjects(jsonArrayData.toString(), LineWorkStatusBean.class);
                    } else {
                        ToastUtil.showLongToast(context, sMsg);
                    }
                    List<WorkshopTableBean> workshopTableBeans = new ArrayList<>();
                    String sLineNoTemp[] = sLineNoIn.split(",");
                    for (int i = 0; i < sLineNoTemp.length; i++) {
                        WorkshopTableBean workshopTableBean = new WorkshopTableBean();
                        workshopTableBean.setNumber((i + 1) + "");
                        workshopTableBean.setLineNo(sLineNoTemp[i]);
                        if (lineWorkStatusBeanList != null && lineWorkStatusBeanList.size() >= 1) {
                            for (int j = 0; j < lineWorkStatusBeanList.size(); j++) {
                                LineWorkStatusBean workStatusBean = lineWorkStatusBeanList.get(j);
                                if(sLineNoTemp[i].equals(workStatusBean.getsLineNo())){
                                    workshopTableBean.setWorkStatusNo(workStatusBean.getsWorkStatus());
                                    workshopTableBean.setWorkStatusName(workStatusBean.getsWorkStatusName());
                                    workshopTableBean.setRgb(workStatusBean.getsRgb());
                                }
                            }
                        }
                        if (woProductionBeanList != null && woProductionBeanList.size() >= 1) {
                            for (int j = 0; j < woProductionBeanList.size(); j++) {
                                WoProductionBean woProductionBean = woProductionBeanList.get(j);
                                if (sLineNoTemp[i].equals(woProductionBean.getsLineNo())) {
                                    workshopTableBean.setWo(woProductionBean.getsWo());
                                    workshopTableBean.setFinishingRate(woProductionBean.getsFinishingRate());//完成率
                                    workshopTableBean.setAchievingRate(woProductionBean.getsAchievingRate());//达成率
                                    workshopTableBean.setQtyOutput(woProductionBean.getsQtyOutput());//线体总产量
                                }
                            }
                        }
                        if (woBeanList != null && woBeanList.size() >= 1) {
                            for (int j = 0; j < woBeanList.size(); j++) {
                                if (sLineNoTemp[i].equals(woBeanList.get(j).getsLineNo())) {
                                    workshopTableBean.setWoPlanQty(woBeanList.get(j).getsWoQtyPlan());
                                    workshopTableBean.setProductNo(woBeanList.get(j).getsProductNo());
                                }
                            }
                        }

                        workshopTableBeans.add(workshopTableBean);
                    }


                    if (workshopTableBeans != null) {
                        viewTable.initTableView(context,workshopTableBeans);
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

    //获取线别产出信息
    public void getLineProductionInfo(String sInterfaceType,String lineNoIn) {
         getLineProductionInfo(sInterfaceType, lineNoIn, sSMTWorkshopLanguage, new OnGetDataListener() {
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
                     SharedPreferencesUtil.saveValue(context,"nSMTTotalLineProductionLastRefreshTimeV3",DateUtils.dateToLong(sDateNow));
                     String rTime = sDateNow;
                     if(!TextUtils.isEmpty(time)){
                         switch (time){
                             case "refreshTime":
                                 Message message = new Message();
                                 message.obj = rTime;
                                 message.what = 1;
                                 handler.sendMessage(message);
                                 break;
                         }

                     }
                     if (sStatus.equalsIgnoreCase("OK")) {
                         String sData = item.getString("data");
                         JSONArray jsonArrayData = new JSONArray(sData);
                         lineNoProductionBeanList = GsonUtils.getObjects(jsonArrayData.toString(),LineNoProductionBean.class);
                     } else {
                         ToastUtil.showLongToast(context, sMsg);
                     }

                     String sChartLineNoProductionTitle = getString(R.string.smt_line_production);
                     String sChartLineNoProductionLabel[] = {getString(R.string.smt_line_input_qty),getString(R.string.smt_line_output_qty)};
                     if(getCurLanguage() == 1){
                         sChartLineNoProductionTitle = "Line production";
                         sChartLineNoProductionLabel[0] = "Number of inputs";
                         sChartLineNoProductionLabel[1] = "Number of outputs";
                     }else if(getCurLanguage() == 2){
                         sChartLineNoProductionTitle = "時段産量";
                         sChartLineNoProductionLabel[0] = "投入數";
                         sChartLineNoProductionLabel[1] = "産出數";
                     }
                     if(lineNoProductionBeanList!=null && lineNoProductionBeanList.size()>=1){
                         showLineNoProduction(viewLineProduction,lineNoProductionBeanList,nChartLineProductColor,sChartLineNoProductionTitle,sChartLineNoProductionLabel);
                     }else {
                         List<LineNoProductionBean> tempLineNoPros = new ArrayList<>();
                         String tempLines[] = sLineNoIn.split(",");
                         for (int i = 0; i < tempLines.length; i++) {
                             LineNoProductionBean lineNoProductionBean = new LineNoProductionBean();
                             lineNoProductionBean.setsLineNo(tempLines[i]);
                             lineNoProductionBean.setSdlQtyInput("0");
                             lineNoProductionBean.setSdlQtyOutput("0");
                             tempLineNoPros.add(lineNoProductionBean);
                         }
                         showLineNoProduction(viewLineProduction,lineNoProductionBeanList,nChartLineProductColor,sChartLineNoProductionTitle,sChartLineNoProductionLabel);
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
    private String javaToJson(){
        kanbanMcNo = SharedPreferencesUtil.getValue(context,"KANBAN_MC_NO","");
        JSONArray array = null;
        try {
            array = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            JSONArray dataArray = new JSONArray();
            DBUtil dbUtil = new DBUtil(context);
            List<SMT_WORKSHOP_LINE_SETTING> workshopLineSettings = dbUtil.getAllWorkshopLine();
            if(workshopLineSettings!=null && workshopLineSettings.size()>=1){
                for (int i = 0; i < workshopLineSettings.size(); i++) {
                    JSONObject mesJsonObject = new JSONObject();
                    mesJsonObject.put("PATTEM_NO",pattemNo);
                    mesJsonObject.put("WORKSHOP_NO",workshopLineSettings.get(i).getWORKSHOP_NO());
                    mesJsonObject.put("LINE_NO",workshopLineSettings.get(i).getLINE_NO());
                    mesJsonObject.put("KANBAN_MC_NO",kanbanMcNo);
                    mesJsonObject.put("FACTORY_NO",MyApplication.sFactoryNo);
                    dataArray.put(mesJsonObject);
                }
            }
            jsonObject.put("status","OK");
            jsonObject.put("msg","发送公告消息筛选条件!");
            jsonObject.put("data",dataArray);
            array.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return array.toString();
    }
    //获取公告栏信息
    public void getKanBanMsg(String code,String json) {
        getKanBanMsg(code, json, sSMTWorkshopLanguage, new OnGetDataListener() {
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
                    SharedPreferencesUtil.saveValue(context,"nSMTTotalMessageLastRefreshTimeV3",DateUtils.dateToLong(sDateNow));
                    String rTime = sDateNow;
                    if(!TextUtils.isEmpty(time)){
                        switch (time){
                            case "refreshTime":
                                Message message = new Message();
                                message.obj = rTime;
                                message.what = 1;
                                handler.sendMessage(message);
                                break;
                        }

                    }
                    messageList = new ArrayList<>();
                    if (sStatus.equalsIgnoreCase("OK")) {
                        String sData = item.getString("data");
                        JSONArray jsonArrayData = new JSONArray(sData);
                        messageList = GsonUtils.getObjects(jsonArrayData.toString(), MessageBean.class);
                        messageStopFlag = false;
                    }else {
//                    ToastUtil.showLongToast(context, sMsg);
                        messageStopFlag = true;
                        setVisiblebulletin(View.GONE);
                    }
                    if(messageList!=null && messageList.size()>=1){
                        messageStopFlag = false;
                        LogUtil.e(TAG,"messageList = " + messageList.toString());

                        for (int i = 0; i < messageList.size(); i++) {
                            if(i == 0){
                                newScrollText = "[" + messageList.get(i).getMSG_TYPE() + "]:" + messageList.get(i).getMSG_DETAIL();
                            }else {
                                newScrollText = newScrollText + "         " + "[" + messageList.get(i).getMSG_TYPE() + "]:" + messageList.get(i).getMSG_DETAIL();
                            }
                        }
                        if(showUI(disMessage) && !messageStopFlag){
                            setVisiblebulletin(View.VISIBLE);
                        }else {
                            setVisiblebulletin(View.GONE);
                        }
                        if(messageFlag){
                            messageFlag = false;
                            scrollTextView = getViewBulletin();
                            scrollText = newScrollText;
                            scroll();//开始
                        }else {
                            if(scrollTextView==null && !TextUtils.isEmpty(newScrollText)){
                                if(messageStopFlag){
                                    stopScroll();
                                    messageStopFlag = false;
                                }
                                scrollTextView = getViewBulletin();
                                scrollText = newScrollText;
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
                                    if(!scrollText.equals(newScrollText)){
                                        scrollText = newScrollText;
                                        LogUtil.e(TAG,"scrollText = " + scrollText);
                                        scroll();//开始
                                    }
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

    private void scroll(){
        scrollTextView.setFocusable(true);
        scrollTextView.setText(scrollText);//设置文本
        scrollTextView.startScroll(); //开始
    }
    //获取班次模块信息
    public void getShiftInfo(String sInterfaceType,String sLineNo) {
        getShiftInfo(sInterfaceType, sLineNo, sSMTWorkshopLanguage, new OnGetDataListener() {
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
                    String rTime = sDateNow;
                    SharedPreferencesUtil.saveValue(context, "nSMTTotalShiftLastRefreshTimeV3", DateUtils.dateToLong(sDateNow));
                    if (!TextUtils.isEmpty(time)) {
                        switch (time) {
                            case "refreshTime":
                                Message message = new Message();
                                message.obj = rTime;
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
                        if (shiftList != null && shiftList.size() >= 1) {
                            LogUtil.e(TAG, "shiftList = " + shiftList.toString());
                            String sShiftName = shiftList.get(0).getsShiftName();
                            String sShiftTimeBegin = shiftList.get(0).getsShiftTimeBegin();
                            String sShiftTimeEnd = shiftList.get(0).getsShiftTimeEnd();
                            String sShiftTime = sShiftTimeBegin + "-" + sShiftTimeEnd;
                            setShift(sShiftName + "(" + sShiftTime + ")");
//                            bottom.setShift(String.format(getString(R.string.smt_line_board_shift_no), sShiftName));
//                            bottom.setShiftTime(String.format(getString(R.string.smt_line_board_shift_time_begin_end), sShiftTime));
//                            if (getCurLanguage() == 1) {
//                                bottom.setShift("Shift(" + sShiftName + ")");
//                                bottom.setShiftTime("Time slot(" + sShiftTime + ")");
//                            } else if (getCurLanguage() == 2) {
//                                bottom.setShift("班次(" + sShiftName + ")");
//                                bottom.setShiftTime("時段(" + sShiftTime + ")");
//                            }
                        } else {
//                        ToastUtil.showShortToast(context,String.format(getString(R.string.parse_error),methodname));
                        }
                    } else {
                        ToastUtil.showShortToast(context, sMsg);
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
        getModuleInfo(sPattemNo, sSMTWorkshopLanguage, new OnGetDataListener() {
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
                    String rTime = sDateNow;
                    if (!TextUtils.isEmpty(time)) {
                        switch (time) {
                            case "refreshTime":
                                Message message = new Message();
                                message.obj = rTime;
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
                        if (moduleList != null && moduleList.size() >= 1) {
                            for (int i = 0; i < moduleList.size(); i++) {
                                ModuleInfoBean moduleInfoBean = moduleList.get(i);
                                switch (moduleInfoBean.getMODULE_NO()) {
                                    case "MOD_WO"://制令单模块
                                        nSMTTotalWoHeartbeatInteval = Long.parseLong(moduleInfoBean.getHEARTBEAT_INTEVAL());
                                        break;
                                    case "MOD_WOOUTPUT"://制令单产出信息模块
                                        nSMTTotalWoProductionHeartbeatInteval = Long.parseLong(moduleInfoBean.getHEARTBEAT_INTEVAL());
                                        break;
                                    case "MOD_LINE_WORK_STATUS"://生产状态模块
                                        nSMTTotalLineStatusHeartbeatInteval = Long.parseLong(moduleInfoBean.getHEARTBEAT_INTEVAL());
                                        break;
                                    case "MOD_MESSAGE"://公告信息模块
                                        nSMTTotalMessageHeartbeatInteval = Long.parseLong(moduleInfoBean.getHEARTBEAT_INTEVAL());
                                        break;
                                    case "MOD_LINEOUTPUT"://线别产能模块
                                        nSMTTotalLineProductionHeartbeatInteval = Long.parseLong(moduleInfoBean.getHEARTBEAT_INTEVAL());
                                        break;
                                    case "MOD_SHIFT"://班次信息模块
                                        nSMTTotalShiftHeartbeatInteval = Long.parseLong(moduleInfoBean.getHEARTBEAT_INTEVAL());
                                        break;
                                }
                            }
                        } else {
//                        ToastUtil.showShortToast(context,String.format(getString(R.string.parse_error),methodname));
                        }
                    } else {
//                        ToastUtil.showShortToast(context, sMsg);
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
        getServerDate(sSMTWorkshopLanguage, new OnGetDataListener() {
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
                    String cTime = sDateNow;
                    if (!TextUtils.isEmpty(time)) {
                        switch (time) {
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

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.e("SMTWorkShopBoardActivity-----onPause");
        stopScroll();
        isStopReceiver = false;
        sStopService();
        stopHandler();
        stopTimer();//停止计时器
        stopToast();//停止弹出消息
    }
    private void stopToast(){
        if(toast!=null){
            toast.cancel();
        }
    }
    private void stopHandler(){
        if(handler!=null){
            handler.removeCallbacksAndMessages(null);
        }
    }

    /**
     * 停止公告消息滚动
     */
    private void stopScroll(){
        if(scrollTextView!=null){
            messageStopFlag = true;
            scrollTextView.stopScroll();//停止滚动
            scrollTextView = null;
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    // 用来计算返回键的点击间隔时间
    private long exitTime = 0;
    //返回
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                //弹出提示，可以有多种方式
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                isStopReceiver = false;
                sStopService();
                openActivity(context, PattemMenuActivity.class);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
