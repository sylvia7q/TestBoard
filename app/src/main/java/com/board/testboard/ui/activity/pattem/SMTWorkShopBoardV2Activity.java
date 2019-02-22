package com.board.testboard.ui.activity.pattem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.topcee.kanban.R;
import com.topcee.kanban.app.MyApplication;
import com.topcee.kanban.bean.GetStationOutputYieldBean;
import com.topcee.kanban.bean.KANBAN_MC_PATTEM;
import com.topcee.kanban.bean.KANBAN_USER_PATTEM;
import com.topcee.kanban.bean.LineNoProductionBean;
import com.topcee.kanban.bean.LineWorkStatusBean;
import com.topcee.kanban.bean.MessageAttrBean;
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
import com.topcee.kanban.presenter.OnRequestListener;
import com.topcee.kanban.presenter.OnResultListener;
import com.topcee.kanban.ui.activity.base.module.WorkshopLineSettingActivity;
import com.topcee.kanban.ui.activity.base.module.WorkshopStationSettingActivity;
import com.topcee.kanban.ui.activity.base.set.SettingSMTWorkShopBoardActivity;
import com.topcee.kanban.ui.view.module.MarqueeView;
import com.topcee.kanban.ui.view.module.WorkshopTableViewV2;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

/**
 * 车间看板模板[用到此模板的客户为：TCL]
 * 区别其他车间看板在于，此模板没有线别产能图标控件，并且对于表格数据设置翻页功能
 * 包含制令单信息模块，制令单产出信息模块，生产状态模块，人员岗位信息模块，工站良率模块，班次信息模块，同步当前时间模块，各个模块信息，
 * 需要选择线别，需要选择工站，每页显示7行数据，每页显示时长为10秒(如果模板跳转展示时长大于10s，则车间当页数据展示时长为模板展示时长，如果模板跳转时长小于10s，则车间当页数据展示时长为10s)
 */
public class SMTWorkShopBoardV2Activity extends BasePattemActivity {

    @BindView(R.id.activity_workshop_board_view_table_v2)
    WorkshopTableViewV2 viewTable;

    @BindView(R.id.activity_workshop_board_view_table_tv_show_paging_data)
    TextView tvShowPagingData;

    private Context context;
    private String sLineNoIn = ""; //SMT2-2 1B
    private String sCurLineNoIn = ""; //当前页需要显示的线别
    private String sCurLineListIndex = ""; //当前页需要显示的线别
    private String sStationNoIn = "";//选择的所有工站
    private String sWoIn = "";//所有的制令单
    private final String TAG = SMTWorkShopBoardV2Activity.class.getSimpleName();
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
    private String time = "";//时间

    private static int mTaskId = 1;// 模拟的task id
    private String isAutoPattem = "N";//是否为自动展示模板

    //展示模板
    private List<KANBAN_MC_PATTEM> mcPattemList;
    private KANBAN_MC_PATTEM mc_pattem;
    private List<KANBAN_MC_PATTEM> allMcPattems;
    private String pattemNo = "";
    private int mcIndex = 0;//自动模板展示序号
    private String sSMTWorkshopLanguage = "S";//展示模板语言
    private String curDisplayLanguage = "S";//展示模板语言
    private long nSMTTotalMcPattemLastRefreshTime = 0l;//上次模板展示时间
    private long mcPattemCurRefreshTime = 0l;//当前模板展示时间
    private long pattemHeartbeatInteval = 1l;
    //用户模板
    private KANBAN_USER_PATTEM user_pattem;
    private String kanbanMcNo = "";//设备代码

    private boolean isStopReceiver = true;//停止刷新数据

    //模块信息
    private List<WoBean> woBeanList;//制令单信息
    private List<WoProductionBean> woProductionBeanList;//制令单产出信息
    private List<LineWorkStatusBean> lineWorkStatusBeanList;//获取线别状态
    private List<LineNoProductionBean> lineNoProductionBeanList;//显示-双柱状图-线别产能
    private List<ShiftBean> shiftList;//班次信息列表
    private List<MessageBean> messageList;//公告消息列表
    private List<MessageBean> curMessageList;//公告消息列表
    private List<MessageAttrBean> messageAttrBeanList;//公告消息属性列表
    private boolean messageFlag = true;//第一次获取公告信息
    private boolean messageStopFlag = false;//停止获取公告信息
    private MarqueeView scrollTextView;//公告信息滚动控件
    private int scrollIndex = 0;//滚动信息列表序号
    private String curScrollText = "";//当前滚动信息txt
    private int bulletinTextColor = Color.BLACK;//公告信息滚动字体颜色
    private int bulletinBgColor = Color.WHITE;//公告信息滚动背景颜色
    private List<ModuleInfoBean> moduleList;//各个模块信息列表
    private List<GetStationOutputYieldBean> yieldBeans;//工站良率信息列表

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
    private long nSMTTotalStationGoodRateLastRefreshTime = 0l;//同步时间模块上次刷新时间
    private long nSMTTotalStationGoodRateHeartbeatInteval = 0l;//同步时间模块刷新时间间隔

    private boolean blIsInitData = true;//第一次进来不管刷新时间是否到了，都执行一次
    private boolean blRefreshRate = true;//第一次进来不管刷新时间是否到了，都执行一次

    private boolean IsGetData = false; //调用后台接口获取数据(当显示最后一页数据时，IsGetData = true)
    private boolean woTimeRefresh = false;
    private boolean woProductionTimeRefresh = false;
    private boolean lineStatusTimeRefresh = false;
    private boolean rateTimeRefresh = false;
    private int nPageRow = 7; //每页显示行数
    private int nCount = 0; //总页数
    private int nId = 0; //相当于当前页号

    private Timer timer;
    private TimerTask timerTask;
    private Toast toast;
    
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1://获取最后刷新时间或者当前时间
                    if (!TextUtils.isEmpty((String) msg.obj)) {
                        setCurrentDate((String) msg.obj);
                    } else {//如果为空则取系统当前时间
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
                    if (blRefreshRate || rateTimeRefresh) {
                        blRefreshRate = false;
                        if(!TextUtils.isEmpty(sWoIn)){
                            LogUtil.e("sWoIn = " + sWoIn + ",sStationNoIn = " + sStationNoIn);
                            getStationOutputYield(sWoIn, sStationNoIn);
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = SMTWorkShopBoardV2Activity.this;

    }

    @Override
    protected int getModuleLayoutId() {
        return R.layout.activity_smt_work_shop_board_v2;
    }

    @Override
    protected void initUI() {
        initTitle();
    }

    private void initTitle() {
        menuTitle.setCommonTitle(View.GONE, View.GONE, View.GONE, View.VISIBLE);
        menuTitle.setBtnRight(R.drawable.icon_gear, 0);
        getLlWholeBg().setOnLongClickListener(new View.OnLongClickListener() {
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
                openActivity(context, SettingSMTWorkShopBoardActivity.class,"Activity","SMTWorkShopBoardV2Activity");
            }
        });
    }

    /**
     * 初始化模块界面
     */
    private void initModuleUI(String disCurTime, String disReTime, String disCopyright, String disMessage) {
        if (menuTitle.getVisibility() == View.VISIBLE) {
            menuTitle.setVisibility(View.GONE);
        }
        if (showUI(disCurTime) || showUI(disReTime)) {
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
//        disMessage(disMessage);

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

    /**
     * 设置模块英语
     */
    private void initCurUI() {
        if (getCurLanguage() == 1) {
            viewTable.setTxtNumber("Serial number");
            viewTable.setTxtLineNo("Line no");
            viewTable.setTxtPartNo("product no");
            viewTable.setTxtWo("Order");
            viewTable.setTxtBatch("batch");
            viewTable.setTxtCommissioningNumber("production number");
            viewTable.setTxtAoiGoodRate("AOI yield");
            setCompanyText("Copyright© TOPCEE TECHNOLOGIES Co., Ltd.");
            menuTitle.setBtnLeft("back");
        } else if (getCurLanguage() == 2) {
            viewTable.setTxtNumber("序號");
            viewTable.setTxtLineNo("線別");
            viewTable.setTxtPartNo("產品號");
            viewTable.setTxtWo("制令單");
            viewTable.setTxtBatch("批量");
            viewTable.setTxtCommissioningNumber("投產數");
            viewTable.setTxtAoiGoodRate("AOI良率");
            setCompanyText("版權所有©深圳市拓思科技有限公司 www.topcee.com");
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
        LogUtil.e("SMTWorkShopBoardActivity-----onStart");

    }


    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.e("SMTWorkShopBoardActivity-----onResume");
        isAutoPattem = getIntent().getStringExtra("isAutoPattem");
        getLlWholeBg().requestFocus();
        blIsInitData = true;
        blRefreshRate = true;
        IsGetData = false;
        nCount = 0;  //每页序号-初始化变量
        nId = 0; //相当于当前页号-初始化变量
        String sKanBanMCNo = SharedPreferencesUtil.getValue(getBaseContext(), "KANBAN_MC_NO", "");
        getPattem(sKanBanMCNo);

    }

    //获取看板模板信息
    public void getPattem(String sKanBanMCNo) {
        getKanBanUserPattem(context, sKanBanMCNo, new OnResultListener() {
            @Override
            public void onSuccess(String sDateNow) {
                DBUtil dbUtil = new DBUtil(context);
                mc_pattem = dbUtil.getMcPattemByCode(template_code[3]);//更新展示模板
                user_pattem = dbUtil.getUserPattemByCode(template_code[3]);//更新展示模板
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
                            sSMTWorkshopLanguage = curDisplayLanguage;
                        } else {
                            sSMTWorkshopLanguage = MyApplication.sLanguage;
                        }
                    }
                    smtTitleText = user_pattem.getPATTEM_TITLE();
                    setTitleText(smtTitleText);
                    setTitleTextSize(28);
                    initColor();
                    initModuleUI(disCurTime, disReTime, disCopyright, disMessage);//初始化模块界面
                    if (showUI(disMessage)) {
                        getKanBanMsgAttr(context, sSMTWorkshopLanguage, new OnRequestListener() {
                            @Override
                            public void onSuccess(List list) {
                                messageAttrBeanList = list;
                            }

                            @Override
                            public void onFail(String result) {

                            }
                        });
                    }
                    initCurUI();
                    if(TextUtils.isEmpty(sDateNow)){
                        sDateNow = DateUtils.longToDate(System.currentTimeMillis());
                    }
                    String rTime = sDateNow;//获取时间判断是否到了切换模板的时间
                    SharedPreferencesUtil.saveValue(context, "nSMTTotalMcPattemLastRefreshTimeV2", DateUtils.dateToLong(rTime));
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

        sLineNoIn = SharedPreferencesUtil.getValue(context, "WorkshopLineNo", "");
        sStationNoIn = SharedPreferencesUtil.getValue(getBaseContext(), "WorkShopBoardStationNo", "");//获取多工站
        if (TextUtils.isEmpty(sLineNoIn)) {
            ToastUtil.showShortToast(context,context.getString(R.string.more_line_select_line));
            openActivity(context, WorkshopLineSettingActivity.class);
            return;
        }
        if(TextUtils.isEmpty(sStationNoIn)){
            ToastUtil.showShortToast(context,context.getString(R.string.please_select_station));
            openActivity(context, WorkshopStationSettingActivity.class);
            return;
        }
        messageFlag = true;
        //判断线别、工站是否设置
        if (isAutoPattem.equals("Y")) {
            isStopReceiver = true;
            startService();
            initService();
        } else {
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
        LogUtil.e(TAG, "stopService");
        Intent intent = new Intent(context, SMTTotalAlarmService.class);
        stopService(intent);
        AlarmManagerUtil.cancelAlarmBroadcast(context, mTaskId, SMTTotalAlarmReceiver.class);
    }

    SMTTotalAlarmReceiver smtTotalAlarmReceiver = new SMTTotalAlarmReceiver();

    /**
     * 广播回调消息
     */
    private void initService() {
        smtTotalAlarmReceiver.setOnReceivedMessageListener(new SMTTotalAlarmReceiver.OnAlarmMessageListener() {
            @Override
            public void onReceived(String message) {
                if (isStopReceiver) {
                    DBUtil dbUtil = new DBUtil(context);
                    if (isAutoPattem.equals("Y")) {
                        nSMTTotalMcPattemLastRefreshTime = SharedPreferencesUtil.getValue(context, "nSMTTotalMcPattemLastRefreshTimeV2", 0l);
                        boolean timeRefresh = DateUtils.isRefreshData("切换模板",DateUtils.longToDate(nSMTTotalMcPattemLastRefreshTime), Long.parseLong(mc_pattem.getDISPLAY_TIME()));
                        if(timeRefresh) {
                            LogUtil.e(TAG, "到了切换模板的时间");
                            if (mcIndex < allMcPattems.size()) {
                                mcIndex++;
                            } else if (mcIndex >= allMcPattems.size()) {
                                mcIndex = 1;
                            }
                            if (allMcPattems != null && allMcPattems.size() == 1) {
                                initData();
                                return;
                            }
                            KANBAN_MC_PATTEM tempPattem = dbUtil.getMcPattemBySortId(String.valueOf(mcIndex));
                            if (tempPattem != null) {
                                String className = getClassName(getTemplateActivity(), tempPattem.getINTERNAL_CODE());
                                LogUtil.e(TAG, "当前要切换的模板为" + className);
                                if (!TextUtils.isEmpty(className)) {
                                    if (!className.equals("com.topcee.kanban.ui.activity.pattem.SMTWorkShopBoardV2Activity")) {
                                        openActivity(context, className, "isAutoPattem", isAutoPattem);
                                        SMTWorkShopBoardV2Activity.this.finish();
                                    } else {
                                        initData();
                                    }
                                    return;
                                } else {
                                    initData();
                                    return;
                                }
                            } else {
                                initData();
                                return;
                            }

                        } else {
                            LogUtil.e(TAG, "还没到切换模板的时间");
                            initData();
                        }
                    } else {
                        LogUtil.e(TAG, "不是自动模式");
                        initData();
                    }
                }
            }
        });

    }

    /**
     * 初始化数据
     */
    private void initData() {
        nSMTTotalWoLastRefreshTime = SharedPreferencesUtil.getValue(context, "nSMTTotalWoLastRefreshTimeV2", 0l);
        nSMTTotalWoProductionLastRefreshTime = SharedPreferencesUtil.getValue(context, "nSMTTotalWoProductionLastRefreshTimeV2", 0l);
        nSMTTotalLineStatusLastRefreshTime = SharedPreferencesUtil.getValue(context, "nSMTTotalLineStatusLastRefreshTimeV2", 0l);
        nSMTTotalShiftLastRefreshTime = SharedPreferencesUtil.getValue(context, "nSMTTotalShiftLastRefreshTimeV2", 0l);
        nSMTTotalMessageLastRefreshTime = SharedPreferencesUtil.getValue(context, "nSMTTotalMessageLastRefreshTimeV2", 0l);
        nSMTTotalStationGoodRateLastRefreshTime = SharedPreferencesUtil.getValue(context, "nSMTTotalStationGoodRateLastRefreshTimeV2", 0l);

        nPageRow = SharedPreferencesUtil.getValue(getBaseContext(), "nPageRowsDisplay", 7);
        String sLineNoTemp[] = sLineNoIn.split(",");
        int inList_size = sLineNoTemp.length; //显示数据总数量
        //分页数取整
        int nInt = 0;
        nInt = inList_size / nPageRow; // 数据总数量(inList_size) / 每页显示行数(nPageRow)
        //分页数取余
        int nMod = 0;
        nMod = inList_size % nPageRow; // 数据总数量(inList_size) / 每页显示行数(nPageRow)
        if (nMod > 0) //分页余数大于 0
        {
            nCount = nInt + 1;  //显示总页数
        } else { //余数小于等于 0
            nCount = nInt; //显示总页数
        }
        int nStart = 0; //当前页第一行
        int nEnd = 0; //当前页最后一行
        if (nId + 1 >= nCount) { //显示最后一页数据
            nStart = nId * nPageRow; //当前页第一行 = 当前页号 * 页显示数
            nEnd = inList_size - 1; //当前页最后一行
            IsGetData = true; //调用后台接口获取数据(当显示最后一页数据时，IsGetData = true)
        } else { //显示不为最后一页数据
            nStart = nId * nPageRow; //当前页第一行 = 当前页号 * 页显示数
            nEnd = (nId + 1) * nPageRow - 1; //当前页最后一行
        }
        for (int i = 0; i < sLineNoTemp.length; i++) {
            if (i >= nStart && i <= nEnd) {
                if(i == nStart){
                    sCurLineNoIn = sLineNoTemp[i];
                    sCurLineListIndex =  (i+ 1) + "";
                }else {
                    sCurLineNoIn = sCurLineNoIn + "," + sLineNoTemp[i];
                    sCurLineListIndex = sCurLineListIndex + ","+ (i+1);
                }

            }
        }
        woTimeRefresh = DateUtils.isRefreshData("制令单",DateUtils.longToDate(nSMTTotalWoLastRefreshTime),nSMTTotalWoHeartbeatInteval);
        rateTimeRefresh = DateUtils.isRefreshData("良率",DateUtils.longToDate(nSMTTotalStationGoodRateLastRefreshTime),nSMTTotalStationGoodRateHeartbeatInteval);
        lineStatusTimeRefresh = DateUtils.isRefreshData("生产状态",DateUtils.longToDate(nSMTTotalLineStatusLastRefreshTime),nSMTTotalLineStatusHeartbeatInteval);
        woProductionTimeRefresh = DateUtils.isRefreshData("制令单产出",DateUtils.longToDate(nSMTTotalWoProductionLastRefreshTime),nSMTTotalWoProductionHeartbeatInteval);

        if (blIsInitData || (woTimeRefresh && woProductionTimeRefresh && lineStatusTimeRefresh && rateTimeRefresh)) {
            int iCurrentPage = nId + 1; //当前页号
            LogUtil.e("iCurrentPage = " + iCurrentPage + ", nCount = "  + nCount);
            tvShowPagingData.setText(String.format(getString(R.string.smt_total_show_page),iCurrentPage,nCount,inList_size));
            getTimsGetWoInfo("SMT", "");
            getWorkStatus("SMT");
            getWoProductionInfo("SMT");

            long lastListItemRefreshTime = SharedPreferencesUtil.getValue(getBaseContext(), "lastListItemRefreshTimeV2", 0l);
            long nPageTurningDisplayTime = SharedPreferencesUtil.getValue(getBaseContext(), "nPageTurningDisplayTime", 10l);
            boolean listTimeRefresh = DateUtils.isRefreshData("翻页",DateUtils.longToDate(lastListItemRefreshTime),nPageTurningDisplayTime);
            if( listTimeRefresh){
                LogUtil.e("nId ===== " + nId);
                nId++;
                SharedPreferencesUtil.saveValue(getBaseContext(), "lastListItemRefreshTimeV2", System.currentTimeMillis());
            }
            if(IsGetData){
                IsGetData = false;
                nId = 0; //相当于当前页号-初始化变量
            }
        }

        boolean ShiftTimeRefresh = DateUtils.isRefreshData("班次",DateUtils.longToDate(nSMTTotalShiftLastRefreshTime),nSMTTotalShiftHeartbeatInteval);
        if (blIsInitData || ShiftTimeRefresh) {
            getShiftInfo("SMT", sCurLineNoIn);
        }
        String json = javaToJson();
        LogUtil.e(TAG, "json = " + json);
        if (showUI(disMessage)) {
            boolean messageTimeRefresh = DateUtils.isRefreshData("公告",DateUtils.longToDate(nSMTTotalMessageLastRefreshTime),nSMTTotalMessageHeartbeatInteval);
            if (blIsInitData || messageTimeRefresh ) {
                getKanBanMsg("", json);
            }
        }
        getModuleInfo(pattemNo);
        blIsInitData = false;
    }

    /**
     * 初始化颜色
     */
    private void initColor() {
        nSMTTotalTitleColor = SharedPreferencesUtil.getValue(getBaseContext(), "nSMTTotalTitleColor", MyApplication.nDefaultSMTTotalTitleColor);
        nSMTTotalOtherTextColor = SharedPreferencesUtil.getValue(getBaseContext(), "nSMTTotalOtherTextColor", MyApplication.nDefaultSMTTotalOtherTextColor);
        nSMTTotalWholeBgColor = SharedPreferencesUtil.getValue(getBaseContext(), "nSMTTotalWholeBgColor", MyApplication.nDefaultSMTTotalWholeBgColor);
        nSMTTotalChartBgColor = SharedPreferencesUtil.getValue(getBaseContext(), "nSMTTotalChartBgColor", MyApplication.nDefaultSMTTotalChartBgColor);
        nChartLineProductColor1 = SharedPreferencesUtil.getValue(getBaseContext(), "nChartLineProductColor1", MyApplication.nDefaultChartLineProductColor1);
        nChartLineProductColor2 = SharedPreferencesUtil.getValue(getBaseContext(), "nChartLineProductColor2", MyApplication.nDefaultChartLineProductColor2);
        nChartLineProductColor = new Integer[]{nChartLineProductColor1, nChartLineProductColor2};
        setPattemTitleTextColor(nSMTTotalTitleColor);
        setOtherTextColor(nSMTTotalOtherTextColor);
        setWholeBgColor(nSMTTotalWholeBgColor);
        setChartBgColor(nSMTTotalChartBgColor);
        int bulletinBgColor = context.getResources().getColor(R.color.bulletin_bg_color);
        setBulletinBgColor(bulletinBgColor);
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
        viewTable.setWorkshopTableInfoTextColor(color);
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
//        viewLineProduction.setBackgroundColor(color);//线别产能
    }


    //获取制令单数据
    public void getTimsGetWoInfo(String sInterfaceType,String sWorkShopNo) {
        getTimsGetWoInfo(sInterfaceType, sCurLineNoIn, sWorkShopNo, sSMTWorkshopLanguage, new OnGetDataListener() {
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
                    SharedPreferencesUtil.saveValue(context, "nSMTTotalWoLastRefreshTimeV2", DateUtils.dateToLong(sDateNow));
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
                    woBeanList = new ArrayList<>();
                    if (sStatus.equalsIgnoreCase("OK")) {
                        String sData = item.getString("data"); //时段数据
                        JSONArray itemData = new JSONArray(sData);
                        woBeanList = GsonUtils.getObjects(itemData.toString(), WoBean.class);

                    } else {
                        if(toast!=null){
                            toast.cancel();
                        }
                        toast  = Toast.makeText(context,sMsg,Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    if (woBeanList != null && woBeanList.size() >= 1) {
//                            LogUtil.e(TAG, "wo===woBeanList = " + woBeanList.toString());
                        for (int j = 0; j < woBeanList.size(); j++) {
                            WoBean woBean = woBeanList.get(j);
                            if(j == 0){
                                sWoIn = woBean.getsWo();
                            }else {
                                sWoIn = sWoIn + "," + woBean.getsWo();
                            }
                        }
                        Message message = new Message();
                        message.what = 4;
                        handler.sendMessage(message);
                    }
                    List<WorkshopTableBean> workshopTableBeans = new ArrayList<>();
                    String sLineNoTemp[] = sCurLineNoIn.split(",");
                    String sLineListIndexTemp[] = sCurLineListIndex.split(",");
                    for (int i = 0; i < sLineNoTemp.length; i++) {
                        WorkshopTableBean workshopTableBean = new WorkshopTableBean();
                        workshopTableBean.setNumber(sLineListIndexTemp[i]);
                        workshopTableBean.setLineNo(sLineNoTemp[i]);
                        sWoIn = "";//每次获取制令单之前,清理以前的数据
                        if (woBeanList != null && woBeanList.size() >= 1) {
                            for (int j = 0; j < woBeanList.size(); j++) {
                                WoBean woBean = woBeanList.get(j);
                                if(sLineNoTemp[i].equals(woBean.getsLineNo())){
                                    workshopTableBean.setCustomerNo(woBean.getsCustomerName());
                                    workshopTableBean.setWoPlanQty(woBean.getsWoQtyPlan());
                                    workshopTableBean.setProductNo(woBean.getsProductNo());
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
                        if (yieldBeans != null && yieldBeans.size() >= 1) {
                            for (int j = 0; j < yieldBeans.size(); j++) {
                                if (sLineNoTemp[i].equals(yieldBeans.get(j).getsLineNo())) {
                                    workshopTableBean.setGoodRate(yieldBeans.get(j).getsOutputYield());
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

    //获取制令单产出信息
    public void getWoProductionInfo(String sInterfaceType) {
        getWoProductionInfo(sInterfaceType, sCurLineNoIn, sSMTWorkshopLanguage, new OnGetDataListener() {
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
                    SharedPreferencesUtil.saveValue(context, "nSMTTotalWoProductionLastRefreshTimeV2", DateUtils.dateToLong(sDateNow));
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
                        woProductionBeanList = GsonUtils.getObjects(jsonArrayData.toString(), WoProductionBean.class);
                    } else {
                        if(toast!=null){
                            toast.cancel();
                        }
                        toast  = Toast.makeText(context,sMsg,Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    List<WorkshopTableBean> workshopTableBeans = new ArrayList<>();
                    String sLineNoTemp[] = sCurLineNoIn.split(",");
                    String sLineListIndexTemp[] = sCurLineListIndex.split(",");
                    for (int i = 0; i < sLineNoTemp.length; i++) {
                        WorkshopTableBean workshopTableBean = new WorkshopTableBean();
                        workshopTableBean.setNumber(sLineListIndexTemp[i]);
                        workshopTableBean.setLineNo(sLineNoTemp[i]);
                        if (woProductionBeanList != null && woProductionBeanList.size() >= 1) {
//                            LogUtil.e(TAG, "woProductionBeanList = " + woProductionBeanList.toString());
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
                                    workshopTableBean.setCustomerNo(woBeanList.get(j).getsCustomerName());
                                    workshopTableBean.setWoPlanQty(woBeanList.get(j).getsWoQtyPlan());
                                    workshopTableBean.setProductNo(woBeanList.get(j).getsProductNo());
                                }
                            }
                        }
                        if (yieldBeans != null && yieldBeans.size() >= 1) {
                            for (int j = 0; j < yieldBeans.size(); j++) {
                                if (sLineNoTemp[i].equals(yieldBeans.get(j).getsLineNo())) {
                                    workshopTableBean.setGoodRate(yieldBeans.get(j).getsOutputYield());
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
    public void getWorkStatus(String sInterfaceType) {
        getWorkStatus(sInterfaceType, sCurLineNoIn, sSMTWorkshopLanguage, new OnGetDataListener() {
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
                    SharedPreferencesUtil.saveValue(context, "nSMTTotalLineStatusLastRefreshTimeV2", DateUtils.dateToLong(sDateNow));
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
                        if(toast!=null){
                            toast.cancel();
                        }
                        toast  = Toast.makeText(context,sMsg,Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    List<WorkshopTableBean> workshopTableBeans = new ArrayList<>();
                    String sLineNoTemp[] = sCurLineNoIn.split(",");
                    String sLineListIndexTemp[] = sCurLineListIndex.split(",");
                    for (int i = 0; i < sLineNoTemp.length; i++) {
                        WorkshopTableBean workshopTableBean = new WorkshopTableBean();
                        workshopTableBean.setNumber(sLineListIndexTemp[i]);
                        workshopTableBean.setLineNo(sLineNoTemp[i]);
                        if (lineWorkStatusBeanList != null && lineWorkStatusBeanList.size() >= 1) {
//                            LogUtil.e(TAG, "lineWorkStatusBeanList = " + lineWorkStatusBeanList.toString());
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
                                    workshopTableBean.setCustomerNo(woBeanList.get(j).getsCustomerName());
                                    workshopTableBean.setWoPlanQty(woBeanList.get(j).getsWoQtyPlan());
                                    workshopTableBean.setProductNo(woBeanList.get(j).getsProductNo());
                                }
                            }
                        }
                        if (yieldBeans != null && yieldBeans.size() >= 1) {
                            for (int j = 0; j < yieldBeans.size(); j++) {
                                if (sLineNoTemp[i].equals(yieldBeans.get(j).getsLineNo())) {
                                    workshopTableBean.setGoodRate(yieldBeans.get(j).getsOutputYield());
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

    //获取工站良率信息
    public void getStationOutputYield(String sWoIn, String sStationNoIn) {
        getStationOutputYield(sWoIn, sStationNoIn, sSMTWorkshopLanguage, new OnGetDataListener() {
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
                    SharedPreferencesUtil.saveValue(context, "nSMTTotalStationGoodRateLastRefreshTimeV2", DateUtils.dateToLong(sDateNow));
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
                    yieldBeans = new ArrayList<>();
                    if (sStatus.equalsIgnoreCase("OK")) {
                        String sData = item.getString("data");
                        JSONArray jsonArrayData = new JSONArray(sData);
                        yieldBeans = GsonUtils.getObjects(jsonArrayData.toString(), GetStationOutputYieldBean.class);
                    } else {
                        if(toast!=null){
                            toast.cancel();
                        }
                        toast  = Toast.makeText(context,sMsg,Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    List<WorkshopTableBean> workshopTableBeans = new ArrayList<>();
                    String sLineNoTemp[] = sCurLineNoIn.split(",");
                    String sLineListIndexTemp[] = sCurLineListIndex.split(",");
                    for (int i = 0; i < sLineNoTemp.length; i++) {
                        WorkshopTableBean workshopTableBean = new WorkshopTableBean();
                        workshopTableBean.setNumber(sLineListIndexTemp[i]);
                        workshopTableBean.setLineNo(sLineNoTemp[i]);
                        if (yieldBeans != null && yieldBeans.size() >= 1) {
//                            LogUtil.e(TAG, "yieldBeans = " + yieldBeans.toString());
                            for (int j = 0; j < yieldBeans.size(); j++) {
                                GetStationOutputYieldBean outputYieldBean = yieldBeans.get(j);
                                if(sLineNoTemp[i].equals(outputYieldBean.getsLineNo())){
                                    workshopTableBean.setGoodRate(outputYieldBean.getsOutputYield());
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
                                    workshopTableBean.setCustomerNo(woBeanList.get(j).getsCustomerName());
                                    workshopTableBean.setWoPlanQty(woBeanList.get(j).getsWoQtyPlan());
                                    workshopTableBean.setProductNo(woBeanList.get(j).getsProductNo());
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
            DBUtil dbUtil = new DBUtil(context);
            List<SMT_WORKSHOP_LINE_SETTING> workshopLineSettings = dbUtil.getAllWorkshopLine();
            if (workshopLineSettings != null && workshopLineSettings.size() >= 1) {
                for (int i = 0; i < workshopLineSettings.size(); i++) {
                    JSONObject mesJsonObject = new JSONObject();
                    mesJsonObject.put("PATTEM_NO", pattemNo);
                    mesJsonObject.put("WORKSHOP_NO", workshopLineSettings.get(i).getWORKSHOP_NO());
                    mesJsonObject.put("LINE_NO", workshopLineSettings.get(i).getLINE_NO());
                    mesJsonObject.put("KANBAN_MC_NO", kanbanMcNo);
                    mesJsonObject.put("FACTORY_NO", MyApplication.sFactoryNo);
                    dataArray.put(mesJsonObject);
                }
            }
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
    public void getKanBanMsg(String code, String json) {
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
                    if (item.has("sDateNow")) {
                        sDateNow = item.getString("sDateNow");
                    }
                    if (TextUtils.isEmpty(sDateNow)) {
                        sDateNow = DateUtils.longToDate(System.currentTimeMillis());
                    }
                    SharedPreferencesUtil.saveValue(context, "nSMTTotalMessageLastRefreshTimeV2", DateUtils.dateToLong(sDateNow));
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

                    messageList = new ArrayList<>();
                    curMessageList = new ArrayList<>();
                    if (sStatus.equalsIgnoreCase("OK")) {
                        String sData = item.getString("data");
                        JSONArray jsonArrayData = new JSONArray(sData);
                        messageList = GsonUtils.getObjects(jsonArrayData.toString(), MessageBean.class);
                        messageStopFlag = false;
                    } else {
//                    ToastUtil.showLongToast(context, sMsg);
                        messageStopFlag = true;
                        setVisiblebulletin(View.GONE);
                    }
                    if (messageList != null && messageList.size() >= 1) {
                        messageStopFlag = false;
                        LogUtil.e(TAG, "messageList = " + messageList.toString());

                        for (int i = 0; i < messageList.size(); i++) {
                            MessageBean messageBean = new MessageBean();
                            messageBean.setFACTORY_NO(messageList.get(i).getFACTORY_NO());
                            messageBean.setMSG_ID(messageList.get(i).getMSG_ID());
                            messageBean.setMSG_TITLE(messageList.get(i).getMSG_TITLE());
                            messageBean.setMSG_TYPE(messageList.get(i).getMSG_TYPE());
                            messageBean.setMSG_DETAIL(messageList.get(i).getMSG_DETAIL());
                            messageBean.setVALID_DATE(messageList.get(i).getVALID_DATE());
                            messageBean.setINVALID_DATE(messageList.get(i).getINVALID_DATE());
                            if (messageAttrBeanList != null && messageAttrBeanList.size() >= 1) {
                                for (int j = 0; j < messageAttrBeanList.size(); j++) {

                                    if (messageList.get(i).getMSG_TYPE().equals(messageAttrBeanList.get(j).getMSG_TYPE())) {
                                        messageBean.setTYPE_NAME(messageAttrBeanList.get(j).getTYPE_NAME());
                                        messageBean.setRGB_TEXT(messageAttrBeanList.get(j).getRGB_TEXT());
                                        messageBean.setRGB_BACKGROUND(messageAttrBeanList.get(j).getRGB_BACKGROUND());
                                    }
                                }
                            }
                            curMessageList.add(messageBean);
                        }
                        LogUtil.e(TAG, "curMessageList = " + curMessageList.toString());
                        if (showUI(disMessage) && !messageStopFlag) {
                            setVisiblebulletin(View.VISIBLE);
                        } else {
                            setVisiblebulletin(View.GONE);
                        }
                        if (messageFlag) {
                            messageFlag = false;
                            scrollTextView = getViewBulletin();
                            curScrollText = getMsgFirst(curMessageList);
                            if (!TextUtils.isEmpty(curScrollText)) {
                                scroll();
                            } else {
                                if (scrollTextView != null) {
                                    stopScroll();
                                }
                            }
                        } else {
                            if (scrollTextView == null && !TextUtils.isEmpty(curScrollText)) {
                                if (messageStopFlag) {
                                    stopScroll();
                                    messageStopFlag = false;
                                }
                                scrollTextView = getViewBulletin();
                                scroll();//开始
                            }
                        }
                        if (scrollTextView != null) {
                            scrollTextView.setOnMargueeListener(new MarqueeView.OnMargueeListener() {
                                @Override
                                public void onRollOver() {
                                    if (messageStopFlag) {
                                        stopScroll();
                                        return;
                                    } else {
                                        if (scrollTextView.getVisibility() == View.INVISIBLE || scrollTextView.getVisibility() == View.GONE) {
                                            setVisiblebulletin(View.VISIBLE);
                                        }
                                    }
                                    curScrollText = getMsg(curMessageList);
                                    scroll();
                                }

                            });
                        }

                    } else {
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

    //获取班次模块信息
    public void getShiftInfo(String sInterfaceType, String sLineNo) {
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
                    SharedPreferencesUtil.saveValue(context, "nSMTTotalShiftLastRefreshTimeV2", DateUtils.dateToLong(sDateNow));
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
//                            setShift(String.format(getString(R.string.smt_line_board_shift_no), sShiftName));
//                            setShiftTime(String.format(getString(R.string.smt_line_board_shift_time_begin_end), sShiftTime));
//                            if (getCurLanguage() == 1) {
//                                setShift("Shift(" + sShiftName + ")");
//                                setShiftTime("Time slot(" + sShiftTime + ")");
//                            } else if (getCurLanguage() == 2) {
//                                setShift("班次(" + sShiftName + ")");
//                                setShiftTime("時段(" + sShiftTime + ")");
//                            }
                        } else {
//                        ToastUtil.showShortToast(context,String.format(getString(R.string.parse_error),methodname));
                        }
                    } else {
                        if(toast!=null){
                            toast.cancel();
                        }
                        toast  = Toast.makeText(context,sMsg,Toast.LENGTH_SHORT);
                        toast.show();
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
                                    case "MOD_STATUS"://生产状态模块
                                        nSMTTotalLineStatusHeartbeatInteval = Long.parseLong(moduleInfoBean.getHEARTBEAT_INTEVAL());
                                        break;
                                    case "MOD_MESSAGE"://公告信息模块
                                        nSMTTotalMessageHeartbeatInteval = Long.parseLong(moduleInfoBean.getHEARTBEAT_INTEVAL());
                                        break;
                                    case "MOD_STATION_GOOD_RATE"://获取工站良率
                                        nSMTTotalStationGoodRateHeartbeatInteval = Long.parseLong(moduleInfoBean.getHEARTBEAT_INTEVAL());
                                        break;
                                    case "MOD_SHIFT"://获取班次信息
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
                    if (!TextUtils.isEmpty(time)) {
                        String cTime = sDateNow;
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
        stopTimer();
        stopToast();
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
        LogUtil.e("SMTWorkShopBoardActivity-----onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.e("SMTWorkShopBoardActivity-----onDestroy");
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
