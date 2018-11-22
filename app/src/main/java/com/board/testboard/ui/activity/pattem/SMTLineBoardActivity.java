package com.board.testboard.ui.activity.pattem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.board.testboard.R;
import com.board.testboard.app.MyApplication;
import com.board.testboard.bean.EmployeeBean;
import com.board.testboard.bean.KANBAN_MC_PATTEM;
import com.board.testboard.bean.KANBAN_USER_PATTEM;
import com.board.testboard.bean.MessageAttrBean;
import com.board.testboard.bean.MessageBean;
import com.board.testboard.bean.ModuleInfoBean;
import com.board.testboard.bean.PeriodOutputBean;
import com.board.testboard.bean.SMT_BOARD_LINE;
import com.board.testboard.bean.ShiftBean;
import com.board.testboard.bean.WarnBean;
import com.board.testboard.bean.WoBean;
import com.board.testboard.bean.WoProductionBean;
import com.board.testboard.database.DBUtil;
import com.board.testboard.http.GetData;
import com.board.testboard.http.SMTLineAlarmReceiver;
import com.board.testboard.http.SMTLineAlarmService;
import com.board.testboard.presenter.OnRequestListener;
import com.board.testboard.presenter.OnResultListener;
import com.board.testboard.ui.activity.base.module.SMTLineSettingActivity;
import com.board.testboard.ui.activity.base.set.SettingSMTLineBoardActivity;
import com.board.testboard.ui.view.module.BarChartOweMaterialWarning;
import com.board.testboard.ui.view.module.EmployeeView;
import com.board.testboard.ui.view.module.MarqueeView;
import com.board.testboard.ui.view.module.MultiaxialTimeProduction;
import com.board.testboard.ui.view.module.WoView;
import com.board.testboard.utils.AlarmManagerUtil;
import com.board.testboard.utils.DateUtils;
import com.board.testboard.utils.GsonUtils;
import com.board.testboard.utils.LogUtil;
import com.board.testboard.utils.SharedPreferencesUtil;
import com.board.testboard.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;

/**
 * 线看板模板
 */
public class SMTLineBoardActivity extends BasePattemActivity{

    @BindView(R.id.activity_line_board_view_wo)
    WoView viewWo;//制令单信息

    @BindView(R.id.activity_line_board_view_employee)
    EmployeeView viewEmployee;//员工信息

    @BindView(R.id.activity_line_board_view_time_production)
    MultiaxialTimeProduction viewTimeProduction;//时段产量

    @BindView(R.id.activity_line_board_view_warning)
    BarChartOweMaterialWarning viewWarning;//欠料预警


    private  final String TAG = SMTLineBoardActivity.class.getSimpleName();
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
    private int nSMTLineOtherTextColor;//其它字体颜色
    private int nSMTLineChartBgColor;//图表背景颜色
    private int nSMTLineWholeBgColor;//背景颜色
    private Integer nChartHourColor[];//图表时段产量
    private int nChartWarnColor;//图表欠料预警
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
    private long nSMTLineCurRefreshTime = 0l;//当前时间

    private TimsGetWoInfoTask getWoInfoTask;
    private TimsGetWoProductionTask getWoProductionTask;
    private TimsGetPeriodOutputTask getPeriodOutputTask;
    private GetWarnInfoTask getWarnInfoTask;
    private GetKanBanMsgTask getKanBanMsgTask;
    private TimsGetShiftInfoTask getShiftInfoTask;
    private GetModuleInfoTask moduleInfoTask;
    private GetServerDateTask serverDateTask;
    private GetDutyPersonInfoTask getDutyPersonInfoTask;
    private boolean blIsInitData = true;

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
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e(TAG + "onCreate");
        context = SMTLineBoardActivity.this;

    }

    @Override
    protected int getModuleLayoutId() {
        return R.layout.activity_smt_line_board;
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
                if(menuTitle.getVisibility() == View.VISIBLE){
                    menuTitle.getBtnRight().requestFocus();
                }
                return true;
            }
        });
        menuTitle.setBtnRightOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(context, SettingSMTLineBoardActivity.class);
            }
        });
    }

    /**
     * 当前页面改为英文
     */
    private void initCurUI(){
        if(getCurLanguage()==1){
            viewWo.setCustomTitle("Client");
            viewWo.setWoTitle("Order");
            viewWo.setProductNoTitle("product code");
            viewWo.setWoPlanQtyTitle("Number of orders");
//            viewWo.setCurShiftTotalQtyTitle("Current shift total output");
            viewWo.setProductionScheduleTitle("production progress");
            bottom.setCompanyText("Copyright©TOPCEE TECHNOLOGIES Co., Ltd.");
            menuTitle.setBtnLeft("back");
        }else if(getCurLanguage()==2){
            viewWo.setCustomTitle("客戶");
            viewWo.setWoTitle("制令單");
            viewWo.setProductNoTitle("産品號");
            viewWo.setWoPlanQtyTitle("制令單數量");
//            viewWo.setCurShiftTotalQtyTitle("Current shift total output");
            viewWo.setProductionScheduleTitle("生産進度");
            bottom.setCompanyText("版權所有©深圳市拓思科技有限公司 www.topcee.com");
            menuTitle.setBtnLeft("返回");
        }
    }

    /**
     * 初始化模块界面
     */
    private void initModuleView(String disCurTime, String disReTime, String disCopyright, String disMessage) {
        if( showUI(disCurTime)|| showUI(disReTime) ){//当前时间和刷新时间共用一个控件，优先显示当前时间
            bottom.setCurrentDateVisibility(View.VISIBLE);
            String getCurTimeByServer = SharedPreferencesUtil.getValue(context,"getCurTimeByServer","");
            if(showUI(disCurTime)&& showUI(disReTime) ){
                if(getCurTimeByServer.equals("Y")){//如果本地设置了从服务器获取当前时间，则每次从接口获取服务器时间，否则就取本地时间
                    time = "curTime";
                }else {
                    time = "sysTime";
                }
            }else if(showUI(disCurTime)){
                if(getCurTimeByServer.equals("Y")){
                    time = "curTime";
                }else {
                    time = "sysTime";
                }
            }else if(showUI(disReTime)){
                time = "refreshTime";
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
        LogUtil.e(TAG + "onStart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.e(TAG + "onResume");
        isAutoPattem = getIntent().getStringExtra("isAutoPattem");
        getLlWholeBg().requestFocus();
        blIsInitData = true;
        String sKanBanMCNo = SharedPreferencesUtil.getValue(getBaseContext(), "KANBAN_MC_NO", "");
        getPattem(sKanBanMCNo);
    }

    //启动Service
    private void startService() {
        Intent i = new Intent(context, SMTLineAlarmService.class);
        // 获取10秒之后的日期时间字符串
        i.putExtra("alarm_time", DateUtils.getNLaterDateTimeString(Calendar.SECOND, 1));
        i.putExtra("task_id", mTaskId);
        i.putExtra("nRefreshTimeSet", pattemHeartbeatInteval);
        startService(i);
    }

    //停止Service
    private void sStopService() {
        LogUtil.e(TAG,"stopService");
        Intent intent = new Intent(context, SMTLineAlarmService.class);
        stopService(intent);
        AlarmManagerUtil.cancelAlarmBroadcast(context,mTaskId,SMTLineAlarmReceiver.class);
    }

    private void initService(){

        SMTLineAlarmReceiver smtLineAlarmReceiver = new SMTLineAlarmReceiver();
        smtLineAlarmReceiver.setOnReceivedMessageListener(new SMTLineAlarmReceiver.OnAlarmMessageListener() {
            @Override
            public void onReceived(String message) {
                if(isStopReceiver){
                    DBUtil dbUtil = new DBUtil(context);

                    if (isAutoPattem.equals("Y")) {
                        nSMTLineMcPattemLastRefreshTime = SharedPreferencesUtil.getValue(context,"nSMTLineMcPattemLastRefreshTime",0l);
                        mcPattemCurRefreshTime = System.currentTimeMillis();
                        LogUtil.e(TAG,"是自动模式,刷新时间为" + DateUtils.longToDate(nSMTLineMcPattemLastRefreshTime) + ",当前时间为"+ DateUtils.longToDate(mcPattemCurRefreshTime));
                        if(mcPattemCurRefreshTime - nSMTLineMcPattemLastRefreshTime> Integer.parseInt(mc_pattem.getDISPLAY_TIME()) * 1000){
                            LogUtil.e(TAG,"到了切换模板的时间");
                            if(mcIndex < allMcPattems.size()){
                                mcIndex++;
                            }else if(mcIndex >= allMcPattems.size()){
                                mcIndex = 1;
                            }
                            KANBAN_MC_PATTEM tempPattem = dbUtil.getMcPattemBySortId(String.valueOf(mcIndex));
                            if(tempPattem!=null){
                                String className = getClassName(getTemplateActivity(),tempPattem.getINTERNAL_CODE());//根据模板内码取展示模板的activity
                                LogUtil.e(TAG,"要切换的模板是" + className);
                                if(!TextUtils.isEmpty(className)){
                                    openActivity(context,className,"isAutoPattem",isAutoPattem);
                                    SMTLineBoardActivity.this.finish();
                                    return;
                                }
                            }
                        }else {
                            LogUtil.e(TAG,"还没到切换模板的时间");
                            initData(dbUtil);
                        }
                    }else {
                        LogUtil.e(TAG,"不是自动模式");
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
        if(time.equals("curTime")){
            getServerDate();//获取当前时间
        }else if(time.equals("sysTime")){
            bottom.setCurrentDate(DateUtils.longToDate(System.currentTimeMillis()));
        }
        if(lineIndex >= moreLines.size()){
            lineIndex = 0;
        }
        sLineNo = moreLines.get(lineIndex).getLINE_NO();
        lastLineRefreshTime = SharedPreferencesUtil.getValue(context,"lastLineRefreshTime",0l);
        curLineRefreshTime = System.currentTimeMillis();
        String displayTime = dbUtil.getDisPlayByLine(sLineNo).getDISPLAY_TIME();
        LogUtil.e(TAG,"线别刷新时间为" + DateUtils.longToDate(lastLineRefreshTime) + ",当前时间为"+ DateUtils.longToDate(curLineRefreshTime));
        if(curLineRefreshTime-lastLineRefreshTime> Integer.parseInt(displayTime) * 1000){
            LogUtil.e(TAG,"到了切换线别的时间，切换前的线别为" + sLineNo);
            lineIndex++;
            lastLineRefreshTime = System.currentTimeMillis();
            SharedPreferencesUtil.saveValue(context,"lastLineFreshTime",lastLineRefreshTime);
        }else {
            LogUtil.e(TAG,"还没到切换线别的时间");
        }
        if(lineIndex >= moreLines.size()){
            lineIndex = 0;
        }
        sLineNo = moreLines.get(lineIndex).getLINE_NO();
        workshopNo = moreLines.get(lineIndex).getWORKSHOP_NO();
        smtTitle.setTitleText(sLineNo + smtTitleText);
        LogUtil.e(TAG,"当前线别为" + sLineNo);
        warningPercent = SharedPreferencesUtil.getValue(getBaseContext(), "materialWarningPercent", 10); //欠料预警可用比例
        warningStation = SharedPreferencesUtil.getValue(getBaseContext(), "materialWarningStation", 12);//欠料预警工站
        nSMTLineWoLastRefreshTime = SharedPreferencesUtil.getValue(context,"nSMTLineWoLastRefreshTime",0l);
        nSMTLineWoProductionLastRefreshTime = SharedPreferencesUtil.getValue(context,"nSMTLineWoProductionLastRefreshTime",0l);
        nSMTLineEmployeeLastRefreshTime = SharedPreferencesUtil.getValue(context,"nSMTLineEmployeeLastRefreshTime",0l);
        nSMTLinePeriodLastRefreshTime = SharedPreferencesUtil.getValue(context,"nSMTLinePeriodLastRefreshTime",0l);
        nSMTLineWarnLastRefreshTime = SharedPreferencesUtil.getValue(context,"nSMTLineWarnLastRefreshTime",0l);
        nSMTLineMessageLastRefreshTime = SharedPreferencesUtil.getValue(context,"nSMTLineMessageLastRefreshTime",0l);
        nSMTLineShiftLastRefreshTime = SharedPreferencesUtil.getValue(context,"nSMTLineShiftLastRefreshTime",0l);
        nSMTLineCurRefreshTime = System.currentTimeMillis();
        if(blIsInitData || (nSMTLineCurRefreshTime - nSMTLineWoLastRefreshTime > nSMTLineWoHeartbeatInteval * 1000)){
            LogUtil.e(TAG,"制令单刷新时间为" + DateUtils.longToDate(nSMTLineWoLastRefreshTime)+"当前时间为" + DateUtils.longToDate(nSMTLineCurRefreshTime) + "，到切换制令单信息的时间，" + nSMTLineWoHeartbeatInteval);
            getTimsGetWoInfo("SMT",sLineNo,"");
        }
        if(blIsInitData || (nSMTLineCurRefreshTime - nSMTLineWoProductionLastRefreshTime > nSMTLineWoProductionHeartbeatInteval * 1000)){
            LogUtil.e(TAG,"制令单产出刷新时间为" + DateUtils.longToDate(nSMTLineWoProductionLastRefreshTime)+"当前时间为" + DateUtils.longToDate(nSMTLineCurRefreshTime) + "，到切换制令单产出信息的时间，" + nSMTLineWoProductionHeartbeatInteval);
            getWoProductionInfo("SMT",sLineNoIn);
        }

        if(blIsInitData || (nSMTLineCurRefreshTime - nSMTLinePeriodLastRefreshTime> nSMTLinePeriodHeartbeatInteval * 1000)){
            LogUtil.e(TAG,"时段测量刷新时间为" + DateUtils.longToDate(nSMTLinePeriodLastRefreshTime)+"当前时间为" + DateUtils.longToDate(nSMTLineCurRefreshTime) + "，到切换时段测量信息的时间，"+nSMTLinePeriodHeartbeatInteval);
            getTimsGetPeriodOutputInfo("SMT",sLineNo);
        }

        if(blIsInitData || (nSMTLineCurRefreshTime - nSMTLineEmployeeLastRefreshTime > nSMTLineEmployeeHeartbeatInteval * 1000)){
            LogUtil.e(TAG,"人员岗位信息刷新时间为" + DateUtils.longToDate(nSMTLineEmployeeLastRefreshTime)+"当前时间为" + DateUtils.longToDate(nSMTLineCurRefreshTime) + "，到切换人员岗位信息的时间，"+nSMTLineEmployeeHeartbeatInteval);
            getDutyPersonInfo(sLineNo);
        }
        if(blIsInitData || (nSMTLineCurRefreshTime - nSMTLineShiftLastRefreshTime> nSMTLineShiftHeartbeatInteval * 1000)){
            LogUtil.e(TAG,"班次信息刷新时间为" + DateUtils.longToDate(nSMTLineShiftLastRefreshTime)+"当前时间为" + DateUtils.longToDate(nSMTLineCurRefreshTime) + "，到切换班次信息的时间，"+nSMTLineShiftHeartbeatInteval);
            getShiftInfo("SMT",sLineNo);
        }

        String json = javaToJson();
        LogUtil.e(TAG,"json = " + json);
        if(blIsInitData || (nSMTLineCurRefreshTime - nSMTLineMessageLastRefreshTime> nSMTLineMessageHeartbeatInteval * 1000)){
            LogUtil.e(TAG,"公告信息刷新时间为" + DateUtils.longToDate(nSMTLineMessageLastRefreshTime)+"当前时间为" + DateUtils.longToDate(nSMTLineCurRefreshTime) + "，到切换公告信息的时间，"+nSMTLineMessageHeartbeatInteval);
            if(!TextUtils.isEmpty(disMessage) && disMessage.equals("Y")){
                getKanBanMsg("",json);
            }
        }
        if(blIsInitData||(nSMTLineCurRefreshTime - nSMTLineTimeLastRefreshTime > nSMTLineTimeHeartbeatInteval * 1000)){
            getModuleInfo(pattemNo);
        }
        blIsInitData = false;
    }

    /**
     * 初始化颜色
     */
    private void initColor() {
        nSMTLineTitleColor = SharedPreferencesUtil.getValue(getBaseContext(), "nSMTLineTitleColor", MyApplication.nDefaultSMTLineTitleColor);
        nSMTLineOtherTextColor = SharedPreferencesUtil.getValue(getBaseContext(), "nSMTLineOtherTextColor", MyApplication.nDefaultSMTLineOtherTextColor);
        nSMTLineWholeBgColor = SharedPreferencesUtil.getValue(getBaseContext(), "nSMTLineWholeBgColor", MyApplication.nDefaultSMTLineWholeBgColor);
        nSMTLineChartBgColor = SharedPreferencesUtil.getValue(getBaseContext(), "nSMTLineChartBgColor", MyApplication.nDefaultSMTLineChartBgColor);
        Integer nChartHourColor1 = SharedPreferencesUtil.getValue(getBaseContext(), "nChartHourColor1", MyApplication.nDefaultChartHourColor1);
        Integer nChartHourColor2 = SharedPreferencesUtil.getValue(getBaseContext(), "nChartHourColor2", MyApplication.nDefaultChartHourColor2);
        nChartHourColor = new Integer[]{nChartHourColor1, nChartHourColor2};
        nChartWarnColor = SharedPreferencesUtil.getValue(getBaseContext(), "nChartWarnColor", MyApplication.nDefaultChartWarnColor);
        setTitleTextColor(nSMTLineTitleColor);
        setOtherTextColor(nSMTLineOtherTextColor);
        setChartBgColor(nSMTLineChartBgColor);
        setWholeBgColor(nSMTLineWholeBgColor);
        int bulletinBgColor = context.getResources().getColor(R.color.bulletin_bg_color);
        setBulletinBgColor(bulletinBgColor);
    }

    /**
     * 标题颜色
     *
     * @param color
     */
    private void setTitleTextColor(int color) {
        menuTitle.setBtnLeftTextColor(color);
        //线别
        smtTitle.setTitleTextColor(color);
//        smtTitle.setBulletinTextColor(color);
        //时间、班次、时段
        bottom.setCurrentDateTextColor(color);
        bottom.setShiftTextColor(color);
        bottom.setShiftTimeTextColor(color);
        bottom.setCompanyTextColor(color);
    }

    /**
     * 标题默认颜色
     *
     * @param color
     */
    private void setOtherTextColor(int color) {
        //wo信息
        viewWo.setWoInfoTextColor(color);
        //图象人员姓名、角色
        viewEmployee.setEmployeeTextColor(color);
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
        getWoInfoTask = new TimsGetWoInfoTask();
        getWoInfoTask.execute(sInterfaceType, sLineNoIn, sWorkShopNo);
    }


    //获取制令单数据-数据处理
    public class TimsGetWoInfoTask extends AsyncTask<String, Void, String> {

        String methodname = "";
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            if(isCancelled()){
                return null;
            }
             methodname = "TimsGetWoInfo";
            JSONObject jsonObject = new JSONObject();
            String sInterfaceType = params[0];
            String sLineNoIn = params[1];
            String sWorkShopNo = params[2];
            try {

                jsonObject.put("sMesUser", MyApplication.sMesUser);
                jsonObject.put("sFactoryNo", MyApplication.sFactoryNo);
                jsonObject.put("sLanguage", sSMTLineLanguage);
                jsonObject.put("sUserNo", MyApplication.sUserNo);
                jsonObject.put("sClientIp", MyApplication.sClientIp);
                jsonObject.put("sInterfaceType", sInterfaceType);
                jsonObject.put("sLineNoIn", sLineNoIn);
                jsonObject.put("sWorkShopNo", sWorkShopNo); //多工站
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return GetData.getDataByJson(context, jsonObject.toString(), methodname);
        }

        @Override
        protected void onPostExecute(String json) {
            if(isCancelled()){
                return ;
            }
//            json = "[{\"status\":\"OK\",\"msg\":\"提示OK信息!\",\"sDateNow\":\"2018.09.10 16:20:20\",\"data\":[{\"sLineNo\":\"线别\",\"sCustomerName\":\"客户名称\",\"sWo\":\"制令单\",\"sProductNo\":\"产品号\",\"sWoQtyPlan\":\"WO计划数量\",\"sMo\":\"工单\",\"sMoQtyPlan\":\"MO计划数量\"}]}]";
            if (null == json || json.equals("")) {

                ToastUtil.showLongToast(context, context.getResources().getString(R.string.interface_returns_failed));
                return;
            }
            try {
                JSONArray jsonArray = new JSONArray(json);
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
                        case "sysTime":
                            break;
                        case "curTime":
                            break;
                    }

                }
                woBeanList = new ArrayList<>();
                if (sStatus.equalsIgnoreCase("OK")) {
                    String sData = item.getString("data"); //时段数据
                    JSONArray itemData = new JSONArray(sData);
                    woBeanList = GsonUtils.getObjects(itemData.toString(),WoBean.class);

                    if(woBeanList!=null && woBeanList.size()>=1){
                        WoBean woBean = woBeanList.get(0);
                        sCustomer = woBean.getsCustomerName();
                        sWo = woBean.getsWo();
                        sProductNo = woBean.getsProductNo();
                        sWoPlanQty = woBean.getsWoQtyPlan();

                    }else {
//                        ToastUtil.showShortToast(context,String.format(getString(R.string.parse_error),methodname));
                    }
                }  else {
                    ToastUtil.showLongToast(context, sMsg);
                }
                sChartWarnTitle = getString(R.string.smt_line_missing_material_warning);
                sChartWarnLabel = getString(R.string.smt_line_percentage_available);
                if(getCurLanguage()==1){
                    sChartWarnTitle = "Missing Material Warning";
                    sChartWarnLabel = "Available (percentage)";
                }else if(getCurLanguage()==2){
                    sChartWarnTitle = "欠料預警";
                    sChartWarnLabel = "可用(百分比)";
                }
                nSMTLineCurRefreshTime = System.currentTimeMillis();
                if(blIsInitData || (nSMTLineCurRefreshTime - nSMTLineWarnLastRefreshTime> nSMTLineWarnHeartbeatInteval * 1000)){
                    LogUtil.e(TAG,"欠料预警刷新时间为" + DateUtils.longToDate(nSMTLineWarnLastRefreshTime)+"当前时间为" + DateUtils.longToDate(nSMTLineCurRefreshTime) + "，到切换欠料预警的时间，"+nSMTLineWarnHeartbeatInteval);
                    if(!TextUtils.isEmpty(sWo)){
                        getWarnInfo(sWo);
                    }else {
                        List<WarnBean> tempWarns = new ArrayList<>();
                        for (int i = 0; i < 2; i++) {
                            WarnBean warnBean = new WarnBean();
                            warnBean.setMACHINE_SEQ("warn" + i);
                            warnBean.setNEED_PERCENT("0");
                            tempWarns.add(warnBean);
                        }
                        //显示-欠料预警
                        showBarChartOweMaterialWarning(tempWarns,nChartWarnColor,sChartWarnTitle,sChartWarnLabel);
                    }

                }
                //显示-制令单信息
                viewWo.setCustomText(sCustomer);//客户
                viewWo.setWoText(sWo); //制令单
                viewWo.setProductNoText(sProductNo);//产品号
                viewWo.setWoPlanQtyText(sWoPlanQty); //制令单数量
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showLongToast(context, e.getMessage());

                return;
            }
        }
    }
    //获取制令单产出信息
    public void getWoProductionInfo(String sInterfaceType,String sLineNoIn) {
         getWoProductionTask = new TimsGetWoProductionTask();
         getWoProductionTask.execute(sInterfaceType,sLineNoIn);
    }

    //获取制令单产出信息-数据处理
    public class TimsGetWoProductionTask extends AsyncTask<String, Void, String> {
        String sInterfaceType = "";
        String sLineNoIn = "";
        String methodname = "";
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            if(isCancelled()){
                return null;
            }
             methodname = "TimsGetWoProduction";
            JSONObject jsonObject = new JSONObject();
            sInterfaceType = params[0];
            sLineNoIn = params[1];
            try {
                jsonObject.put("sMesUser", MyApplication.sMesUser);
                jsonObject.put("sFactoryNo", MyApplication.sFactoryNo);
                jsonObject.put("sLanguage", sSMTLineLanguage);
                jsonObject.put("sUserNo", MyApplication.sUserNo);
                jsonObject.put("sClientIp", MyApplication.sClientIp);
                jsonObject.put("sInterfaceType", sInterfaceType); //多线别
                jsonObject.put("sLineNoIn", sLineNoIn); //多线别
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return GetData.getDataByJson(context,jsonObject.toString(), methodname);
        }

        @Override
        protected void onPostExecute(String json) {
            if(isCancelled()){
                return ;
            }
            if (null == json || json.equals("")) {
                ToastUtil.showLongToast(context, getString(R.string.interface_returns_failed));
                return;
            }
            try {
                JSONArray jsonArray = new JSONArray(json);
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
                        case "sysTime":
                            break;
                        case "curTime":
                            break;
                    }

                }
                if (sStatus.equalsIgnoreCase("OK")) {
                    String sData = item.getString("data");
                    JSONArray jsonArrayData = new JSONArray(sData);
                    woProductionBeanList = GsonUtils.getObjects(jsonArrayData.toString(),WoProductionBean.class);
                    if(woProductionBeanList!=null && woProductionBeanList.size()>=1){
                        sWoQtyOutput = woProductionBeanList.get(0).getsQtyOutput();
                        sFinishingRate = woProductionBeanList.get(0).getsFinishingRate();
                    }else {
//                        ToastUtil.showShortToast(context,String.format(getString(R.string.parse_error),methodname));
                    }
                } else {
                    ToastUtil.showLongToast(context, sMsg);
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
                //进度条-显示
                showProgressBar(sFinishingRate);
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showLongToast(context, e.getMessage());
                return;
            }
        }
    }

    //进度条-显示
    private void showProgressBar(String sFinishingRate) {
        if (!TextUtils.isEmpty(sFinishingRate)) {
            try {
                int nFinishingRate = (int) Math.ceil(Double.parseDouble(sFinishingRate));
                viewWo.setProgress(nFinishingRate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(TextUtils.isEmpty(sFinishingRate)){
            sFinishingRate = "0";
        }
        viewWo.setProgressBarData(sFinishingRate + "%");//完成率
    }
    //获取人员岗位信息
    public void getDutyPersonInfo(String sLineNo) {
         getDutyPersonInfoTask = new GetDutyPersonInfoTask();
        getDutyPersonInfoTask.execute(sLineNo);

    }
    //获取员工信息-数据处理
    public class GetDutyPersonInfoTask extends AsyncTask<String, Void, SoapObject> {
        String sLineNo = "";

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected SoapObject doInBackground(String... params) {
            if(isCancelled()){
                return null;
            }
            String methodname = "TimsGetDutyPersonLineUserInfo";
            JSONObject jsonObject = new JSONObject();
            sLineNo = params[0];
            try {
                jsonObject.put("sMesUser", MyApplication.sMesUser);
                jsonObject.put("sFactoryNo", MyApplication.sFactoryNo);
                jsonObject.put("sLanguage", sSMTLineLanguage);
                jsonObject.put("sUserNo", MyApplication.sUserNo);
                jsonObject.put("sClientIp", MyApplication.sClientIp);
                jsonObject.put("sLineNo", sLineNo);
                jsonObject.put("sManualShift", "N"); //
                jsonObject.put("sShiftNoCurrent", "NA"); //
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return GetData.getDataByObject(jsonObject.toString(), methodname);
        }

        @Override
        protected void onPostExecute(SoapObject object) {
            if(isCancelled()){
                return ;
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
                    case "sysTime":
                        break;
                    case "curTime":
                        break;
                }

            }
            try {
                object = (SoapObject) object.getProperty("diffgram");
                object = (SoapObject) object.getProperty(0);
                if (null == object) {
                    return;
                }
                LogUtil.e(TAG,"object = " + object.toString());
                for (int i = 0; i < 4; i++) {
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
                            setUserImage(viewEmployee.getIv_employee_01(), viewEmployee.getTv_employee_01(),  viewEmployee.getTv_role_01(), mUserInfo, blIsClear);
                            break;
                        case 1:
                            setUserImage(viewEmployee.getIv_employee_02(),  viewEmployee.getTv_employee_02(), viewEmployee.getTv_role_02(), mUserInfo, blIsClear);
                            break;
                        case 2:
                            setUserImage(viewEmployee.getIv_employee_03(),  viewEmployee.getTv_employee_03(), viewEmployee.getTv_role_03(), mUserInfo, blIsClear);
                            break;
                        case 3:
                            setUserImage(viewEmployee.getIv_employee_04(),  viewEmployee.getTv_employee_04(), viewEmployee.getTv_role_04(), mUserInfo, blIsClear);
                        default:
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.showShortToast(context,e.getMessage().toString());
            }
        }
    }

    //显示图片、姓名、角色
    private void setUserImage(final ImageView iv, final TextView tv_user, final TextView tv_role, final EmployeeBean mUserInfo, final Boolean IsClear) {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (IsClear) {
                        Bitmap  bmp = BitmapFactory.decodeResource(getResources(),R.drawable.icon_nothing);
                        iv.setImageBitmap(bmp);
                        switch (sSMTLineLanguage) {
                            case "S":
                                tv_user.setText("无"); //英文姓名
                                tv_role.setText("无"); //英文岗位
                                break;
                            case "E":
                                tv_user.setText("nothing"); //英文姓名
                                tv_role.setText("nothing"); //英文岗位
                                break;
                            case "T":
                                tv_user.setText("無"); //繁体姓名
                                tv_role.setText("無"); //繁体岗位
                                break;
                            default:
                                tv_user.setText("无"); //英文姓名
                                tv_role.setText("无"); //英文岗位
                                break;
                        }
                    } else {
                        if (mUserInfo.getPHOTO() != null) {
                            iv.setImageBitmap(mUserInfo.getPHOTO());
                        } else {
                            Bitmap  bmp = BitmapFactory.decodeResource(getResources(),R.drawable.icon_nothing);
                            iv.setImageBitmap(bmp);
                        }
                        if(TextUtils.isEmpty(mUserInfo.getUSER_NAME())){
                            switch (sSMTLineLanguage){
                                case "S":
                                    tv_user.setText("无"); //中文姓名
                                    break;
                                case "E":
                                    tv_user.setText("nothing"); //英文姓名
                                    break;
                                case "T":
                                    tv_user.setText("無"); //繁体字姓名
                                    break;
                                default:
                                    tv_user.setText("无"); //中文姓名
                                    break;
                            }
                        }else{
                            tv_user.setText(mUserInfo.getUSER_NAME());
                        }
                        if(TextUtils.isEmpty(mUserInfo.getDUTIES_NO())){
                            switch (sSMTLineLanguage){
                                case "S":
                                    tv_role.setText("无"); //中文岗位
                                    break;
                                case "E":
                                    tv_role.setText("nothing"); //英文岗位
                                    break;
                                case "T":
                                    tv_role.setText("無"); //繁体字岗位
                                    break;
                                default:
                                    tv_role.setText("无"); //中文岗位
                                    break;
                            }
                        }else{
                            tv_role.setText(mUserInfo.getDUTIES_NO());
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取预警信息查询
    public void getWarnInfo(String sWo) {
         getWarnInfoTask = new GetWarnInfoTask();
         getWarnInfoTask.execute(sWo);
    }

    //在线预警信息查询-数据处理
    public class GetWarnInfoTask extends AsyncTask<String, Void, String> {
        String methodname = "";
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            if(isCancelled()){
                return null;
            }
             methodname = "TimsGetFeedReflashInfo";
            JSONObject jsonObject = new JSONObject();
            String sWo = params[0];
            try {

                jsonObject.put("sMesUser", MyApplication.sMesUser);
                jsonObject.put("sFactoryNo", MyApplication.sFactoryNo);
                jsonObject.put("sLanguage", sSMTLineLanguage);
                jsonObject.put("sUserNo", MyApplication.sUserNo);
                jsonObject.put("sClientIp", MyApplication.sClientIp);
                jsonObject.put("sWo", sWo);
                jsonObject.put("sSort", ""); //排序
                jsonObject.put("sTopItem", ""); //显示数据
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return GetData.getDataByJson(context, jsonObject.toString(), methodname);
        }

        @Override
        protected void onPostExecute(String json) {
            if(isCancelled()){
                return ;
            }
            if (null == json || json.equals("")) {
                ToastUtil.showLongToast(context, getString(R.string.interface_returns_failed));
                return;
            }
            try {
                JSONArray jsonArray = new JSONArray(json);
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
                        case "sysTime":
                            break;
                        case "curTime":
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
                        showBarChartOweMaterialWarning(curWarnList,nChartWarnColor,sChartWarnTitle,sChartWarnLabel);
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
                    showBarChartOweMaterialWarning(tempWarns,nChartWarnColor,sChartWarnTitle,sChartWarnLabel);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showLongToast(context, e.getMessage());
                return;
            }
        }
    }

    //获取时段数据
    public void getTimsGetPeriodOutputInfo(String sInterfaceType, String sLineNo) {
         getPeriodOutputTask = new TimsGetPeriodOutputTask();
        getPeriodOutputTask.execute(sInterfaceType, sLineNo);
    }

    //获取时段数据-数据处理
    public class TimsGetPeriodOutputTask extends AsyncTask<String, Void, String> {
        String methodname = "";
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            if(isCancelled()){
                return null;
            }
             methodname = "TimsGetPeriodOutput";
            JSONObject jsonObject = new JSONObject();
            String sInterfaceType = params[0];
            String sLineNo = params[1];
            try {

                jsonObject.put("sMesUser", MyApplication.sMesUser);
                jsonObject.put("sFactoryNo", MyApplication.sFactoryNo);
                jsonObject.put("sLanguage", sSMTLineLanguage);
                jsonObject.put("sUserNo", MyApplication.sUserNo);
                jsonObject.put("sClientIp", MyApplication.sClientIp);
                jsonObject.put("sInterfaceType", sInterfaceType);
                jsonObject.put("sLineNo", sLineNo); //排序
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return GetData.getDataByJson(context, jsonObject.toString(), methodname);
        }

        @Override
        protected void onPostExecute(String json) {
            if(isCancelled()){
                return ;
            }
            if (null == json || json.equals("")) {
                ToastUtil.showLongToast(context, getString(R.string.interface_returns_failed));
                return;
            }
            try {
                JSONArray jsonArray = new JSONArray(json);
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
                        case "sysTime":
                            break;
                        case "curTime":
                            break;
                    }

                }
                periodOutputList = new ArrayList<>();
                if (sStatus.equalsIgnoreCase("OK")) {
                    String sData = item.getString("data");
                    JSONArray jsonArrayData = new JSONArray(sData);
                    periodOutputList = GsonUtils.getObjects(jsonArrayData.toString(),PeriodOutputBean.class);
                } else {
                    ToastUtil.showLongToast(context, sMsg);
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
                    showMultiaxialTimeProduction(periodOutputList, "",nChartHourColor,sChartHourTitle,sChartHourLabel);
                }else {
                    List<PeriodOutputBean> tempPeriod = new ArrayList<>();
                    for (int i = 0; i < 2; i++) {
                        PeriodOutputBean outputBean = new PeriodOutputBean();
                        outputBean.setPeriodName("time" + i);
                        outputBean.setInPutQty("0");
                        outputBean.setOutPutQty("0");
                        tempPeriod.add(outputBean);
                    }
                    showMultiaxialTimeProduction(tempPeriod, "",nChartHourColor,sChartHourTitle,sChartHourLabel);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showLongToast(context, e.getMessage());
                return;
            }
        }
    }

    //显示-时段数据图表
    private void showMultiaxialTimeProduction(List<PeriodOutputBean> periodOutputList, String sQtyHoursoutput, Integer[] color,String sChartHourTitle,String sChartHourLabel[]) {
        if (periodOutputList != null && periodOutputList.size() > 0) {
            viewTimeProduction.refreshChart();
            viewTimeProduction.initView(periodOutputList, sQtyHoursoutput, color,sChartHourTitle,sChartHourLabel);
        }
    }

    //显示-欠料预警图表
    private void showBarChartOweMaterialWarning(List<WarnBean> warnBeans,int color,String sChartWarnTitle,String sChartWarnLabel) {
        if (warnBeans != null && warnBeans.size() > 0) {
            LogUtil.e(TAG,"warnBeans = " + warnBeans.toString());
            viewWarning.refreshChart();
            viewWarning.initView(warnBeans,color,sChartWarnTitle,sChartWarnLabel);
        }
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
            JSONObject mesJsonObject = new JSONObject();
            mesJsonObject.put("PATTEM_NO",pattemNo);
            mesJsonObject.put("WORKSHOP_NO",workshopNo);
            mesJsonObject.put("LINE_NO",sLineNo);
            mesJsonObject.put("KANBAN_MC_NO",kanbanMcNo);
            mesJsonObject.put("FACTORY_NO",MyApplication.sFactoryNo);
            dataArray.put(mesJsonObject);
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
         getKanBanMsgTask = new GetKanBanMsgTask();
         getKanBanMsgTask.execute(code,json);
    }

    //获取公告栏信息-数据处理
    public class GetKanBanMsgTask extends AsyncTask<String, Void, String> {
        String methodname = "";

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            if(isCancelled()){
                return null;
            }
             methodname = "TimsGetMsgMessage";
            JSONObject jsonObject = new JSONObject();
            String sCode = params[0];
            String sJson = params[1];
            try {
                jsonObject.put("sMesUser", MyApplication.sMesUser);
                jsonObject.put("sFactoryNo", MyApplication.sFactoryNo);
                jsonObject.put("sLanguage", sSMTLineLanguage);
                jsonObject.put("sUserNo", MyApplication.sUserNo);
                jsonObject.put("sClientIp", MyApplication.sClientIp);
                jsonObject.put("sCode", sCode);
                jsonObject.put("sJson", sJson);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return GetData.getDataByJson(context, jsonObject.toString(), methodname);
        }

        @Override
        protected void onPostExecute(String json) {
            if(isCancelled()){
                return ;
            }
            if (null == json || json.equals("")) {
                ToastUtil.showLongToast(context, getString(R.string.interface_returns_failed));
                return;
            }
            try {
                JSONArray jsonArray = new JSONArray(json);
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
                        case "sysTime":
                            break;
                        case "curTime":
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
                }
                if(messageList!=null && messageList.size()>=1){
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
                                switch (messageList.get(i).getMSG_TYPE()){
                                    case "":
                                        break;
                                }
                                if(messageList.get(i).getMSG_TYPE().equals(messageAttrBeanList.get(j).getMSG_TYPE())){
                                    messageBean.setTYPE_NAME(messageAttrBeanList.get(j).getTYPE_NAME());
                                    messageBean.setRGB(messageAttrBeanList.get(j).getRGB());
                                }
                            }
                        }
                        curMessageList.add(messageBean);
                    }
                    LogUtil.e(TAG,"curMessageList = " + curMessageList.toString());
                    scrollTextView = getViewBulletin();
                    curScrollText = getMsg(curMessageList,scrollIndex);
                    bulletinTextColor = parseColor(curMessageList.get(scrollIndex).getRGB());
                    if(!TextUtils.isEmpty(curScrollText)){
                        if(!messageStopFlag){
                            setVisiblebulletin(View.VISIBLE);
                        }
                        if(messageFlag){
                            messageFlag = false;
                            scroll();
                        }
                        scrollTextView.setOnMargueeListener(new MarqueeView.OnMargueeListener() {
                            @Override
                            public void onRollOver() {
                                if(messageStopFlag){
                                    scrollTextView.stopScroll();
                                    setVisiblebulletin(View.INVISIBLE);
                                    return;
                                }else {
                                    setVisiblebulletin(View.VISIBLE);
                                }
                                scrollIndex++;
                                if(scrollIndex >= curMessageList.size()){
                                    scrollIndex = 0;
                                }
                                curScrollText = getMsg(curMessageList,scrollIndex);
                                bulletinTextColor = parseColor(curMessageList.get(scrollIndex).getRGB());
                                scroll();
                            }

                        });
                    }
                }else {
                    messageStopFlag = true;
//                    ToastUtil.showShortToast(context,String.format(getString(R.string.parse_error),methodname));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showLongToast(context, e.getMessage());
                return;
            }
        }
    }

    /**
     * 获取滚动消息内容
     * @param curMessageList
     * @param index
     * @return
     */
    private String getMsg(List<MessageBean> curMessageList,int index){
        String text = "[" + curMessageList.get(index).getTYPE_NAME() + "]" + curMessageList.get(index).getMSG_DETAIL();
        return text;
    }

    /**
     * 消息滚动
     */
    private void scroll(){
        scrollTextView.setmTextColor(bulletinTextColor);
        scrollTextView.setFocusable(true);
        scrollTextView.setText(curScrollText);//设置文本
        scrollTextView.startScroll(); //开始
    }
    //获取班次信息
    public void getShiftInfo(String sInterfaceType,String sLineNo) {
         getShiftInfoTask = new TimsGetShiftInfoTask();
         getShiftInfoTask.execute(sInterfaceType, sLineNo);
    }
    //获取班次信息-数据处理
    public class TimsGetShiftInfoTask extends AsyncTask<String, Void, String> {
        String methodname = "";
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            if(isCancelled()){
                return null;
            }
             methodname = "TimsGetShiftInfo";
            JSONObject jsonObject = new JSONObject();
            String sInterfaceType = params[0];
            String sLineNo = params[1];
            try {

                jsonObject.put("sMesUser", MyApplication.sMesUser);
                jsonObject.put("sFactoryNo", MyApplication.sFactoryNo);
                jsonObject.put("sLanguage", sSMTLineLanguage);
                jsonObject.put("sUserNo", MyApplication.sUserNo);
                jsonObject.put("sClientIp", MyApplication.sClientIp);
                jsonObject.put("sInterfaceType", sInterfaceType);
                jsonObject.put("sLineNo", sLineNo);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            return GetData.getDataByJson(context,jsonObject.toString(), methodname);

        }

        @Override
        protected void onPostExecute(String json) {
            if(isCancelled()){
                return ;
            }
            if (null == json || json.equals("")) {
                ToastUtil.showShortToast(context, getString(R.string.interface_returns_failed));
                return;
            }
            try {
                JSONArray jsonArray = new JSONArray(json);
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
                        case "sysTime":
                            break;
                        case "curTime":
                            break;
                    }

                }
                shiftList = new ArrayList<>();
                if (sStatus.equalsIgnoreCase("OK")) {
                    String sData = item.getString("data");
                    JSONArray jsonArrayData = new JSONArray(sData);
                    shiftList = GsonUtils.getObjects(jsonArrayData.toString(), ShiftBean.class);
                    if(shiftList!=null && shiftList.size()>=1){

                        String  sShiftName = shiftList.get(0).getsShiftName();
                        String  sShiftTimeBegin = shiftList.get(0).getsShiftTimeBegin();
                        String  sShiftTimeEnd = shiftList.get(0).getsShiftTimeEnd();
                        String sShiftTime = sShiftTimeBegin + "-" + sShiftTimeEnd;
                        bottom.setShift(String.format(getString(R.string.smt_line_board_shift_no),sShiftName));
                        bottom.setShiftTime(String.format(getString(R.string.smt_line_board_shift_time_begin_end),sShiftTime));
                        if(getCurLanguage()==1){
                            bottom.setShift("Shift(" + sShiftName+ ")" );
                            bottom.setShiftTime("Time slot(" + sShiftTime+ ")" );
                        }else if(getCurLanguage()==2){
                            bottom.setShift("班次(" + sShiftName+ ")" );
                            bottom.setShiftTime("時段(" + sShiftTime+ ")" );
                        }
                    }else {
//                        ToastUtil.showShortToast(context,String.format(getString(R.string.parse_error),methodname));
                    }
                } else {
                    ToastUtil.showShortToast(context, sMsg);
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showShortToast(context,  e.getMessage().toString());
                return;
            }
        }
    }

    //获取用户模板包含的模块信息
    public void getModuleInfo(String sPattemNo) {
         moduleInfoTask = new GetModuleInfoTask();
         moduleInfoTask.execute(sPattemNo);
    }
    //获取用户模板包含的模块信息-数据处理
    public class GetModuleInfoTask extends AsyncTask<String, Void, String> {
        String methodname = "";
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            if(isCancelled()){
                return null;
            }
             methodname = "TimsGetKanBanUserPattemModule";
            JSONObject jsonObject = new JSONObject();
            String sPattemNo = params[0];
            try {

                jsonObject.put("sMesUser", MyApplication.sMesUser);
                jsonObject.put("sFactoryNo", MyApplication.sFactoryNo);
                jsonObject.put("sLanguage", sSMTLineLanguage);
                jsonObject.put("sUserNo", MyApplication.sUserNo);
                jsonObject.put("sClientIp", MyApplication.sClientIp);
                jsonObject.put("sPattemNo", sPattemNo);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            return GetData.getDataByJson(context,jsonObject.toString(), methodname);

        }

        @Override
        protected void onPostExecute(String json) {
            if(isCancelled()){
                return ;
            }
            if (null == json || json.equals("")) {
                ToastUtil.showShortToast(context,getString(R.string.interface_returns_failed));
                return;
            }
            try {
                JSONArray jsonArray = new JSONArray(json);
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
                        case "sysTime":
                            break;
                        case "curTime":
                            break;
                    }

                }
                moduleList = new ArrayList<>();
                if (sStatus.equalsIgnoreCase("OK")) {
                    String sData = item.getString("data");
                    JSONArray jsonArrayData = new JSONArray(sData);
                    moduleList = GsonUtils.getObjects(jsonArrayData.toString(), ModuleInfoBean.class);
                    if(moduleList!=null && moduleList.size()>=1){
                        for (int i = 0; i < moduleList.size(); i++) {
                            ModuleInfoBean moduleInfoBean = moduleList.get(i);
                            switch (moduleInfoBean.getMODULE_NO()){
                                case "WO"://制令单模块
                                    nSMTLineWoHeartbeatInteval = Long.parseLong(moduleInfoBean.getHEARTBEAT_INTEVAL());
                                    break;
                                case "WOOUTPUT"://制令单产出信息模块
                                    nSMTLineWoProductionHeartbeatInteval = Long.parseLong(moduleInfoBean.getHEARTBEAT_INTEVAL());
                                    break;
                                case "PERIOD"://时段模块
                                    nSMTLinePeriodHeartbeatInteval = Long.parseLong(moduleInfoBean.getHEARTBEAT_INTEVAL());
                                    break;
                                case "FEEDREFLASH"://欠料预警模块
                                    nSMTLineWarnHeartbeatInteval = Long.parseLong(moduleInfoBean.getHEARTBEAT_INTEVAL());
                                    break;
                                case "PERSON"://人员岗位模块
                                    nSMTLineEmployeeHeartbeatInteval = Long.parseLong(moduleInfoBean.getHEARTBEAT_INTEVAL());
                                    break;
                                case "MESSAGE"://公告信息模块
                                    nSMTLineMessageHeartbeatInteval = Long.parseLong(moduleInfoBean.getHEARTBEAT_INTEVAL());
                                    break;
                                case "TIME"://同步当前时间模块
                                    nSMTLineTimeHeartbeatInteval = Long.parseLong(moduleInfoBean.getHEARTBEAT_INTEVAL());
                                    break;
                            }
                        }
                    }else {
//                        ToastUtil.showShortToast(context,String.format(getString(R.string.parse_error),methodname));
                    }
                } else {
                    ToastUtil.showShortToast(context,sMsg);
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showShortToast(context,e.getMessage().toString());
                return;
            }
        }
    }
    //获取看板模板信息
    public void getServerDate() {
         serverDateTask = new GetServerDateTask();
        serverDateTask.execute();
    }
    //获取看板模板信息-数据处理
    public class GetServerDateTask extends AsyncTask<String, Void, String> {
        String methodname = "";
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            if(isCancelled()){
                return null;
            }
            methodname = "TimsSynchronizeServerDate";
            JSONObject jsonObject = new JSONObject();
            try {

                jsonObject.put("sMesUser", MyApplication.sMesUser);
                jsonObject.put("sFactoryNo", MyApplication.sFactoryNo);
                jsonObject.put("sLanguage", sSMTLineLanguage);
                jsonObject.put("sUserNo", MyApplication.sUserNo);
                jsonObject.put("sClientIp", MyApplication.sClientIp);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            return GetData.getDataByJson(context,jsonObject.toString(), methodname);

        }

        @Override
        protected void onPostExecute(String json) {
            if(isCancelled()){
                return ;
            }
//            json = "[{\"status\":\"OK\",\"msg\":\"提示OK信息!\",\"data\":[{\"KMD_ID\":\"1\",\"KANBAN_MC_NO\":\"1\",\"PATTEM_NO\":\"1\",\"DISPLAY_LANGUAGE\":\"S\",\"DISPLAY_TIME\":\"2018.09.10 16:20:20\",\"REFRESH_TIME\":\"2018.09.10 16:20:20\",\"NAME_CHS\":\"SMT车间看板\",\"NAME_CHT\":\"123\",\"NAME_EN\":\"123\",\"INTERNAL_CODE\":\"doov_002\",\"KANBAN_TYPE\":\"1\",\"IS_CUSTOMIZE\":\"Y\",\"TITLE_CHS\":\"\",\"TITLE_CHT\":\"12\",\"TITLE_EN\":\"12\",\"HEARTBEAT_INTEVAL\":\"15\",\"IS_DISP_COPYRIGHT\":\"Y\",\"IS_DISP_CURRENTTIME\":\"N\",\"IS_DISP_REFRESH_TIME\":\"Y\",\"IS_DISP_MESSAGE\":\"Y\",\"SORTID\":1,\"STATUS\":\"\",\"NOTE\":\"\"]}]";
            if (null == json || json.equals("")) {
                ToastUtil.showShortToast(context,getString(R.string.interface_returns_failed));
                return;
            }
            try {
                JSONArray jsonArray = new JSONArray(json);
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
                    curTime = sDateNow;
                    switch (time){
                        case "curTime":
                            Message message = new Message();
                            message.obj = curTime;
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        case "refreshTime":
                            break;
                        case "sysTime":
                            break;
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showShortToast(context,e.getMessage());
                return;
            }
        }
    }
    //获取看板模板信息
    public void getPattem(String sKanBanMCNo) {

        getKanBanUserPattem(context, sKanBanMCNo, new OnResultListener() {
            @Override
            public void onSuccess() {
                final DBUtil dbUtil = new DBUtil(context);
                mc_pattem = dbUtil.getMcPattemByCode(template_code[0]);//更新展示模板
                user_pattem = dbUtil.getUserPattemByCode(template_code[0]);//更新展示模板
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
                            sSMTLineLanguage = curDisplayLanguage;
                        }else {
                            sSMTLineLanguage = MyApplication.sLanguage;
                        }

                    }
                    smtTitleText = user_pattem.getPATTEM_TITLE();
                    initColor();//初始化界面颜色
                    initModuleView(disCurTime,disReTime,disCopyright,disMessage);//初始化模块是否显示
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
                    if(!TextUtils.isEmpty(time)){
                        String mcRefreshTime = mc_pattem.getREFRESH_TIME();
//                        String mcRefreshTime = "";
                        long nCurSMTLineMcPattemLastRefreshTime = System.currentTimeMillis();
                        if(TextUtils.isEmpty(mcRefreshTime)){
                            refreshTime = DateUtils.longToDate(nCurSMTLineMcPattemLastRefreshTime);
                        }else {
                            refreshTime = mcRefreshTime;
                        }
                        LogUtil.e(TAG,"nSMTLineMcPattemLastRefreshTime = " + refreshTime);
                        SharedPreferencesUtil.saveValue(context,"nSMTLineMcPattemLastRefreshTime",DateUtils.dateToLong(refreshTime));
                        switch (time){
                            case "refreshTime":
                                Message message = new Message();
                                message.obj = refreshTime;
                                message.what = 1;
                                handler.sendMessage(message);
                                break;
                            case "sysTime":
                                break;
                            case "curTime":
                                break;
                        }

                    }
                    init();
                }
            }
            @Override
            public void onFail(String result) {
                ToastUtil.showShortToast(context,result);
            }
        });
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
     * 初始化数据
     */
    private void init(){
        DBUtil dbUtil = new DBUtil(context);
        //判断线别、工站是否设置
        moreLines = dbUtil.getAllLine();
        lineIndex = SharedPreferencesUtil.getValue(context,"lineIndex",0);
        if(moreLines!=null && moreLines.size()>=1){
            sLineNoIn = SharedPreferencesUtil.getValue(getBaseContext(), "LineBoardLineNo", "");//获取多线别为了获取制令单信息
            messageFlag = true;//判断公告消息是否是打开界面第一次获取

            if(isAutoPattem.equals("Y")){//判断是否为自动模式
                isStopReceiver = true;
                startService();
                initService();
            }else {
                isStopReceiver = true;
                startService();
                initService();
            }
        }else {
            openActivity(context, SMTLineSettingActivity.class);
            return;
        }

    }
    @Override
    public void finish() {
        super.finish();
        LogUtil.e("finish");
//        overridePendingTransition(0, 0);
    }


    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.e(TAG + "onPause");
        isStopReceiver = false;
        SharedPreferencesUtil.saveValue(context,"lineIndex",lineIndex);
        sStopService();
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
        if(scrollTextView!=null){
            messageStopFlag = true;
            scrollTextView.stopScroll();//停止滚动
        }
        sStopService();
        if(handler!=null){
            handler.removeCallbacksAndMessages(null);
        }
        stopTask();
    }
    //返回
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    //离开当前页面所有任务停止运行
    private void stopTask(){
        stopTask(getDutyPersonInfoTask);
        stopTask(getShiftInfoTask);
        stopTask(getWarnInfoTask);
        stopTask(getWoInfoTask);
        stopTask(getWoProductionTask);
        stopTask(getPeriodOutputTask);
        stopTask(getKanBanMsgTask);
        stopTask(moduleInfoTask);
        stopTask(serverDateTask);
    }

}
