package com.board.testboard.ui.activity.pattem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;


import com.board.testboard.R;
import com.board.testboard.app.MyApplication;
import com.board.testboard.bean.KANBAN_MC_PATTEM;
import com.board.testboard.bean.KANBAN_USER_PATTEM;
import com.board.testboard.bean.LineNoProductionBean;
import com.board.testboard.bean.LineWorkStatusBean;
import com.board.testboard.bean.MessageAttrBean;
import com.board.testboard.bean.MessageBean;
import com.board.testboard.bean.ModuleInfoBean;
import com.board.testboard.bean.SMT_WORKSHOP_LINE_SETTING;
import com.board.testboard.bean.ShiftBean;
import com.board.testboard.bean.WoBean;
import com.board.testboard.bean.WoProductionBean;
import com.board.testboard.bean.WorkshopTableBean;
import com.board.testboard.database.DBUtil;
import com.board.testboard.http.GetData;
import com.board.testboard.http.SMTTotalAlarmReceiver;
import com.board.testboard.http.SMTTotalAlarmService;
import com.board.testboard.presenter.OnRequestListener;
import com.board.testboard.presenter.OnResultListener;
import com.board.testboard.ui.activity.base.module.WorkshopLineSettingActivity;
import com.board.testboard.ui.activity.base.set.SettingSMTWorkShopBoardActivity;
import com.board.testboard.ui.view.module.MarqueeView;
import com.board.testboard.ui.view.module.MultiaxialLineNoProduction;
import com.board.testboard.ui.view.module.WorkshopTableView;
import com.board.testboard.utils.AlarmManagerUtil;
import com.board.testboard.utils.DateUtils;
import com.board.testboard.utils.GsonUtils;
import com.board.testboard.utils.LogUtil;
import com.board.testboard.utils.SharedPreferencesUtil;
import com.board.testboard.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;

/**
 * 车间看板模板
 */
public class SMTWorkShopBoardActivity extends BasePattemActivity {

    @BindView(R.id.activity_workshop_board_view_table)
    WorkshopTableView viewTable;

    @BindView(R.id.activity_workshop_board_view_line_production)
    MultiaxialLineNoProduction viewLineProduction;

    private final String TAG = SMTWorkShopBoardActivity.class.getSimpleName();
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
    private String curTime = "";
    private String refreshTime = "";

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
    private List<MessageBean> messageList;//公告消息列表
    private List<MessageBean> curMessageList;//公告消息列表
    private List<MessageAttrBean> messageAttrBeanList;//公告消息属性列表
    private boolean messageFlag = true;
    private boolean messageStopFlag = false;
    private MarqueeView scrollTextView;
    private int scrollIndex = 0;
    private String curScrollText = "";
    private int bulletinTextColor = Color.BLACK;
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
    private long nSMTTotalTimeLastRefreshTime = 0l;//同步时间模块上次刷新时间
    private long nSMTTotalTimeHeartbeatInteval = 0l;//同步时间模块刷新时间间隔
    private long nSMTTotalCurRefreshTime = 0l;//线别产出刷新时间

    private TimsGetWoInfoTask getWoInfoTask;
    private TimsGetWoProductionTask getWoProductionTask;
    private GetKanBanMsgTask getKanBanMsgTask;
    private TimsGetShiftInfoTask getShiftInfoTask;
    private GetModuleInfoTask moduleInfoTask;
    private GetServerDateTask serverDateTask;
    private GetKanBanMcPattemTask getKanBanMcPattemTask;
    private GetLineProductionInfoTask getLineProductionInfoTask;
    private GetWorkStatusTask workStatusTask;

    private boolean blIsInitData=true;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1://获取最后刷新时间或者当前时间
                    if(!TextUtils.isEmpty((String) msg.obj)){
                        bottom.setCurrentDate((String) msg.obj);
                    }else {//如果为空则取系统当前时间
                        bottom.setCurrentDate(DateUtils.longToDate(System.currentTimeMillis()));
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e("SMTWorkShopBoardActivity-----onCreate");
        context = SMTWorkShopBoardActivity.this;

    }

    @Override
    protected int getModuleLayoutId() {
        return R.layout.activity_smt_work_shop_board;
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
                openActivity(context, SettingSMTWorkShopBoardActivity.class);
            }
        });
    }

    /**
     * 初始化模块界面
     */
    private void initModuleUI(String disCurTime, String disReTime, String disCopyright, String disMessage) {
        if( showUI(disCurTime)|| showUI(disReTime) ){
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
        String sKanBanMCNo = SharedPreferencesUtil.getValue(getBaseContext(), "KANBAN_MC_NO", "");
        getPattem(sKanBanMCNo);

    }
    //启动Service
    private void startService() {

        Intent i = new Intent(context, SMTTotalAlarmService.class);
        // 获取10秒之后的日期时间字符串
        i.putExtra("alarm_time", DateUtils.getNLaterDateTimeString(Calendar.SECOND, 1));
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
                        nSMTTotalMcPattemLastRefreshTime = SharedPreferencesUtil.getValue(context,"nSMTTotalMcPattemLastRefreshTime",0l);
                        mcPattemCurRefreshTime = System.currentTimeMillis();
                        LogUtil.e(TAG,"是自动模式，刷新时间为" + DateUtils.longToDate(nSMTTotalMcPattemLastRefreshTime) + ",当前时间为"+ DateUtils.longToDate(mcPattemCurRefreshTime));
                        if(mcPattemCurRefreshTime - nSMTTotalMcPattemLastRefreshTime> Integer.parseInt(mc_pattem.getDISPLAY_TIME()) * 1000) {
//                        if(mcPattemCurRefreshTime - nSMTTotalMcPattemLastRefreshTime> 180 * 1000) {
                            LogUtil.e(TAG,"到了切换模板的时间");
                            if (mcIndex < allMcPattems.size()) {
                                mcIndex++;
                            } else if (mcIndex >= allMcPattems.size()) {
                                mcIndex = 1;
                            }
                            KANBAN_MC_PATTEM tempPattem = dbUtil.getMcPattemBySortId(String.valueOf(mcIndex));
                            if (tempPattem != null) {
                                String className = getClassName(getTemplateActivity(), tempPattem.getINTERNAL_CODE());
                                LogUtil.e(TAG,"当前要切换的模板为" + className);
                                if(!TextUtils.isEmpty(className)){
                                    openActivity(context,className,"isAutoPattem",isAutoPattem);
                                    SMTWorkShopBoardActivity.this.finish();
                                    return;
                                }
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
        if(time.equals("curTime")){
            getServerDate();//获取当前时间
        }else if(time.equals("sysTime")){
            bottom.setCurrentDate(DateUtils.longToDate(System.currentTimeMillis()));
        }
        nSMTTotalWoLastRefreshTime = SharedPreferencesUtil.getValue(context,"nSMTTotalWoLastRefreshTime",0l);
        nSMTTotalWoProductionLastRefreshTime = SharedPreferencesUtil.getValue(context,"nSMTTotalWoProductionLastRefreshTime",0l);
        nSMTTotalLineStatusLastRefreshTime = SharedPreferencesUtil.getValue(context,"nSMTTotalLineStatusLastRefreshTime",0l);
        nSMTTotalLineProductionLastRefreshTime = SharedPreferencesUtil.getValue(context,"nSMTTotalLineProductionLastRefreshTime",0l);
        nSMTTotalShiftLastRefreshTime = SharedPreferencesUtil.getValue(context,"nSMTTotalShiftLastRefreshTime",0l);
        nSMTTotalMessageLastRefreshTime = SharedPreferencesUtil.getValue(context,"nSMTTotalMessageLastRefreshTime",0l);
        nSMTTotalTimeLastRefreshTime = SharedPreferencesUtil.getValue(context,"nSMTTotalShiftLastRefreshTime",0l);
        nSMTTotalCurRefreshTime = System.currentTimeMillis();
        if(blIsInitData ||(nSMTTotalCurRefreshTime - nSMTTotalWoLastRefreshTime > nSMTTotalWoHeartbeatInteval * 1000)){
            LogUtil.e(TAG,"制令单刷新时间为" + DateUtils.longToDate(nSMTTotalWoLastRefreshTime)+"当前时间为" + DateUtils.longToDate(nSMTTotalCurRefreshTime) + "，到切换制令单信息的时间，" + nSMTTotalWoHeartbeatInteval);
            getTimsGetWoInfo("SMT",sLineNoIn,"");
        }
        if(blIsInitData||(nSMTTotalCurRefreshTime - nSMTTotalWoProductionLastRefreshTime > nSMTTotalWoProductionHeartbeatInteval * 1000)){
            LogUtil.e(TAG,"制令单产出刷新时间为" + DateUtils.longToDate(nSMTTotalWoProductionLastRefreshTime)+"当前时间为" + DateUtils.longToDate(nSMTTotalCurRefreshTime) + "，到切换制令单产出信息的时间，" + nSMTTotalWoProductionHeartbeatInteval);
            getWoProductionInfo("SMT",sLineNoIn);
        }
        if(blIsInitData||(nSMTTotalCurRefreshTime - nSMTTotalLineStatusLastRefreshTime > nSMTTotalLineStatusHeartbeatInteval * 1000)){
            LogUtil.e(TAG,"生产状态刷新时间为" + DateUtils.longToDate(nSMTTotalLineStatusLastRefreshTime)+"当前时间为" + DateUtils.longToDate(nSMTTotalCurRefreshTime) + "，到切换生产状态信息的时间，" + nSMTTotalLineStatusHeartbeatInteval);
            getWorkStatus("SMT",sLineNoIn);
        }
        if(blIsInitData||(nSMTTotalCurRefreshTime - nSMTTotalLineProductionLastRefreshTime > nSMTTotalLineProductionHeartbeatInteval * 1000)){
            LogUtil.e(TAG,"线别产能刷新时间为" + DateUtils.longToDate(nSMTTotalLineProductionLastRefreshTime)+"当前时间为" + DateUtils.longToDate(nSMTTotalCurRefreshTime) + "，到切换线别产能信息的时间，" + nSMTTotalLineProductionHeartbeatInteval);
            getLineProductionInfo("SMT",sLineNoIn);
        }

        if(blIsInitData ||(nSMTTotalCurRefreshTime - nSMTTotalShiftLastRefreshTime > nSMTTotalShiftHeartbeatInteval * 1000)){
            LogUtil.e(TAG,"班次刷新时间为" + DateUtils.longToDate(nSMTTotalShiftLastRefreshTime)+"当前时间为" + DateUtils.longToDate(nSMTTotalCurRefreshTime) + "，到切换班次消息的时间，" + nSMTTotalShiftHeartbeatInteval);
            getShiftInfo("SMT",sLineNoIn);
        }
        String json = javaToJson();
        LogUtil.e(TAG,"json = " + json);
        if(blIsInitData||(nSMTTotalCurRefreshTime - nSMTTotalMessageLastRefreshTime > nSMTTotalMessageHeartbeatInteval * 1000)){
            LogUtil.e(TAG,"公告消息刷新时间为" + DateUtils.longToDate(nSMTTotalMessageLastRefreshTime)+"当前时间为" + DateUtils.longToDate(nSMTTotalCurRefreshTime) + "，到切换公告消息的时间，" + nSMTTotalMessageHeartbeatInteval);
            if(!TextUtils.isEmpty(disMessage) && disMessage.equals("Y")){
                LogUtil.e(TAG,"获取当前公告信息并显示");
                getKanBanMsg("",json);
            }
        }
        if(blIsInitData||(nSMTTotalCurRefreshTime - nSMTTotalTimeLastRefreshTime > nSMTTotalTimeHeartbeatInteval * 1000)){
            getModuleInfo(pattemNo);
        }
        blIsInitData=false;
    }

    /**
     * 初始化颜色
     */
    private void initColor(){
        nSMTTotalTitleColor = SharedPreferencesUtil.getValue(getBaseContext(), "nSMTTotalTitleColor", MyApplication.nDefaultSMTTotalTitleColor);
        nSMTTotalOtherTextColor = SharedPreferencesUtil.getValue(getBaseContext(), "nSMTTotalOtherTextColor", MyApplication.nDefaultSMTTotalOtherTextColor);
        nSMTTotalWholeBgColor = SharedPreferencesUtil.getValue(getBaseContext(), "nSMTTotalWholeBgColor", MyApplication.nDefaultSMTTotalWholeBgColor);
        nSMTTotalChartBgColor = SharedPreferencesUtil.getValue(getBaseContext(), "nSMTTotalChartBgColor", MyApplication.nDefaultSMTTotalChartBgColor);
        nChartLineProductColor1 = SharedPreferencesUtil.getValue(getBaseContext(), "nChartLineProductColor1", MyApplication.nDefaultChartLineProductColor1);
        nChartLineProductColor2 = SharedPreferencesUtil.getValue(getBaseContext(), "nChartLineProductColor2", MyApplication.nDefaultChartLineProductColor2);
        nChartLineProductColor = new Integer[]{nChartLineProductColor1,nChartLineProductColor2};
        setTitleTextColor(nSMTTotalTitleColor);
        setOtherTextColor(nSMTTotalOtherTextColor);
        setWholeBgColor(nSMTTotalWholeBgColor);
        setChartBgColor(nSMTTotalChartBgColor);
        int bulletinBgColor = context.getResources().getColor(R.color.bulletin_bg_color);
        setBulletinBgColor(bulletinBgColor);
    }

    /**
     * 标题颜色
     * @param color
     */
    private void setTitleTextColor(int color){
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
    }

    /**
     * 设置图标控件的默认的背景颜色
     */
    private void setChartBgColor(int color){
        viewLineProduction.setBackgroundColor(color);//时段产量
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
                jsonObject.put("sLanguage", sSMTWorkshopLanguage);
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
                SharedPreferencesUtil.saveValue(context,"nSMTTotalWoLastRefreshTime",DateUtils.dateToLong(sDateNow));
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

                    List<WorkshopTableBean> workshopTableBeans = new ArrayList<>();
                    if(woBeanList!=null && woBeanList.size()>=1){
                        LogUtil.e(TAG,"woBeanList = " + woBeanList.toString());
                        for (int i = 0; i < woBeanList.size(); i++) {
                            WoBean woBean = woBeanList.get(i);
                            WorkshopTableBean workshopTableBean = new WorkshopTableBean();
                            workshopTableBean.setNumber((i + 1) + "");
                            workshopTableBean.setWoPlanQty(woBean.getsWoQtyPlan());
                            workshopTableBean.setProductNo(woBean.getsProductNo());
                            if(woProductionBeanList!=null && woProductionBeanList.size()>=1){
                                for (int j = 0; j < woProductionBeanList.size(); j++) {
                                    WoProductionBean woProductionBean = woProductionBeanList.get(j);
                                    if(woProductionBean.getsLineNo().equals(woBean.getsLineNo())){
                                        workshopTableBean.setLineNo(woProductionBean.getsLineNo());
                                        workshopTableBean.setWo(woProductionBean.getsWo());
                                        workshopTableBean.setFinishingRate(woProductionBean.getsFinishingRate());//完成率
                                        workshopTableBean.setAchievingRate(woProductionBean.getsAchievingRate());//达成率
                                        workshopTableBean.setQtyOutput(woProductionBean.getsQtyOutput());//线体总产量
                                    }
                                }
                            }
                            if(lineWorkStatusBeanList!=null && lineWorkStatusBeanList.size()>=1){
                                for (int j = 0; j < lineWorkStatusBeanList.size(); j++) {
                                    if(woBean.getsLineNo().equals(lineWorkStatusBeanList.get(j).getsLineNo())){
                                        workshopTableBean.setWorkStatusName(lineWorkStatusBeanList.get(j).getsWorkStatusName());
                                    }

                                }
                            }
                            workshopTableBeans.add(workshopTableBean);
                        }
                        if(workshopTableBeans!=null){
                            viewTable.initTableView(context,workshopTableBeans);
                        }
                    }else {
//                        ToastUtil.showShortToast(context,String.format(getString(R.string.parse_error),methodname));
                    }
                }  else {
                    ToastUtil.showLongToast(context, sMsg);
                }
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
                jsonObject.put("sLanguage", sSMTWorkshopLanguage);
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
                SharedPreferencesUtil.saveValue(context,"nSMTTotalWoProductionLastRefreshTime",DateUtils.dateToLong(sDateNow));
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

                    List<WorkshopTableBean> workshopTableBeans = new ArrayList<>();
                    if(woProductionBeanList!=null && woProductionBeanList.size()>=1) {
                        LogUtil.e(TAG, "woProductionBeanList = " + woProductionBeanList.toString());
                        for (int i = 0; i < woProductionBeanList.size(); i++) {
                            WoProductionBean woProductionBean = woProductionBeanList.get(i);
                            WorkshopTableBean workshopTableBean = new WorkshopTableBean();
                            workshopTableBean.setNumber((i + 1) + "");
                            workshopTableBean.setLineNo(woProductionBean.getsLineNo());
                            workshopTableBean.setWo(woProductionBean.getsWo());
                            workshopTableBean.setFinishingRate(woProductionBean.getsFinishingRate());//完成率
                            workshopTableBean.setAchievingRate(woProductionBean.getsAchievingRate());//达成率
                            workshopTableBean.setQtyOutput(woProductionBean.getsQtyOutput());//线体总产量
                            if (lineWorkStatusBeanList != null && lineWorkStatusBeanList.size() >= 1) {
                                for (int j = 0; j < lineWorkStatusBeanList.size(); j++) {
                                    if (woProductionBean.getsLineNo().equals(lineWorkStatusBeanList.get(j).getsLineNo())) {
                                        workshopTableBean.setWorkStatusName(lineWorkStatusBeanList.get(j).getsWorkStatusName());
                                    }
                                }
                            }
                            if (woBeanList != null && woBeanList.size() >= 1) {
                                for (int j = 0; j < woBeanList.size(); j++) {
                                    if (woProductionBean.getsLineNo().equals(woBeanList.get(j).getsLineNo())) {
                                        workshopTableBean.setWoPlanQty(woBeanList.get(j).getsWoQtyPlan());
                                        workshopTableBean.setProductNo(woBeanList.get(j).getsProductNo());
                                    }
                                }
                            }
                            workshopTableBeans.add(workshopTableBean);
                        }
                        if (workshopTableBeans != null) {
                            viewTable.initTableView(context, workshopTableBeans);
                        }
                    }else {
//                        ToastUtil.showShortToast(context,String.format(getString(R.string.parse_error),methodname));
                    }

                } else {
                    ToastUtil.showLongToast(context, sMsg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showLongToast(context, e.getMessage());
                return;
            }
        }
    }

    //获取生产状态
    public void getWorkStatus(String sInterfaceType,String sLineNoIn) {
         workStatusTask = new GetWorkStatusTask();
        workStatusTask.execute(sInterfaceType,sLineNoIn);
    }

    //获取生产状态-数据处理
    public class GetWorkStatusTask extends AsyncTask<String, Void, String> {
        String methodname = "";
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            if(isCancelled()){
                return null;
            }
             methodname = "TimsGetWorkStatus";
            JSONObject jsonObject = new JSONObject();
            String sInterfaceType = params[0];
            String sLineNoIn = params[1];
            try {
                jsonObject.put("sMesUser", MyApplication.sMesUser);
                jsonObject.put("sFactoryNo", MyApplication.sFactoryNo);
                jsonObject.put("sLanguage", sSMTWorkshopLanguage);
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
                SharedPreferencesUtil.saveValue(context,"nSMTTotalLineStatusLastRefreshTime",DateUtils.dateToLong(sDateNow));
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
                    lineWorkStatusBeanList = GsonUtils.getObjects(jsonArrayData.toString(),LineWorkStatusBean.class);

                    List<WorkshopTableBean> workshopTableBeans = new ArrayList<>();
                    if(lineWorkStatusBeanList!=null && lineWorkStatusBeanList.size()>=1){
                        LogUtil.e(TAG,"lineWorkStatusBeanList = " + lineWorkStatusBeanList.toString());
                        for (int i = 0; i < lineWorkStatusBeanList.size(); i++) {
                            LineWorkStatusBean workStatusBean = lineWorkStatusBeanList.get(i);
                            WorkshopTableBean workshopTableBean = new WorkshopTableBean();
                            workshopTableBean.setNumber((i + 1) + "");
                            workshopTableBean.setLineNo(workStatusBean.getsLineNo());
                            workshopTableBean.setWorkStatusName(workStatusBean.getsWorkStatusName());

                            if(woProductionBeanList!=null && woProductionBeanList.size()>=1){
                                for (int j = 0; j < woProductionBeanList.size(); j++) {
                                    WoProductionBean woProductionBean = woProductionBeanList.get(j);
                                    if(woProductionBean.getsLineNo().equals(workStatusBean.getsLineNo())){
                                        workshopTableBean.setWo(woProductionBean.getsWo());
                                        workshopTableBean.setFinishingRate(woProductionBean.getsFinishingRate());//完成率
                                        workshopTableBean.setAchievingRate(woProductionBean.getsAchievingRate());//达成率
                                        workshopTableBean.setQtyOutput(woProductionBean.getsQtyOutput());//线体总产量
                                    }
                                }
                            }
                            if (woBeanList != null && woBeanList.size() >= 1) {
                                for (int j = 0; j < woBeanList.size(); j++) {
                                    if (workStatusBean.getsLineNo().equals(woBeanList.get(j).getsLineNo())) {
                                        workshopTableBean.setWoPlanQty(woBeanList.get(j).getsWoQtyPlan());
                                        workshopTableBean.setProductNo(woBeanList.get(j).getsProductNo());
                                    }
                                }
                            }
                            workshopTableBeans.add(workshopTableBean);
                        }
                        if(workshopTableBeans!=null){
                            viewTable.initTableView(context,workshopTableBeans);
                        }
                    }else {
//                        ToastUtil.showShortToast(context,String.format(getString(R.string.parse_error),methodname));
                    }
                } else {
                    ToastUtil.showLongToast(context, sMsg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showLongToast(context, e.getMessage());
                return;
            }
        }
    }

    //获取线别产出信息
    public void getLineProductionInfo(String sInterfaceType,String sLineNoIn) {
         getLineProductionInfoTask = new GetLineProductionInfoTask();
        getLineProductionInfoTask.execute(sInterfaceType,sLineNoIn);
    }

    //获取线别产出信息-数据处理
    public class GetLineProductionInfoTask extends AsyncTask<String, Void, String> {
        String methodname = "";
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            if(isCancelled()){
                return null;
            }
             methodname = "TimsGetLineProductionInfo";
            JSONObject jsonObject = new JSONObject();
            String sInterfaceType = params[0];
            String sLineNoIn = params[1];
            try {
                jsonObject.put("sMesUser", MyApplication.sMesUser);
                jsonObject.put("sFactoryNo", MyApplication.sFactoryNo);
                jsonObject.put("sLanguage", sSMTWorkshopLanguage);
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
                SharedPreferencesUtil.saveValue(context,"nSMTTotalLineProductionLastRefreshTime",DateUtils.dateToLong(sDateNow));
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
                    showLineNoProduction(lineNoProductionBeanList,nChartLineProductColor,sChartLineNoProductionTitle,sChartLineNoProductionLabel);
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
                    showLineNoProduction(lineNoProductionBeanList,nChartLineProductColor,sChartLineNoProductionTitle,sChartLineNoProductionLabel);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showLongToast(context, e.getMessage());
                return;
            }
        }
    }

    //显示-线别产能
    public void showLineNoProduction(List<LineNoProductionBean> lineNoProductionList, Integer[] color,String sChartLineNoProductionTitle,String sChartLineNoProductionLabel[]) {
        if(lineNoProductionList!=null){
            viewLineProduction.refreshChart();
            viewLineProduction.initView(lineNoProductionList,color,sChartLineNoProductionTitle,sChartLineNoProductionLabel);

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
                jsonObject.put("sLanguage", sSMTWorkshopLanguage);
                jsonObject.put("sUserNo", MyApplication.sUserNo);
                jsonObject.put("sClientIp", MyApplication.sClientIp);
                jsonObject.put("sCode", sCode);
                jsonObject.put("sJson", sJson);
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
                SharedPreferencesUtil.saveValue(context,"nSMTTotalMessageLastRefreshTime",DateUtils.dateToLong(sDateNow));
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
    //获取班次模块信息
    public void getShiftInfo(String sInterfaceType,String sLineNo) {
         getShiftInfoTask = new TimsGetShiftInfoTask();
        getShiftInfoTask.execute(sInterfaceType, sLineNo);
    }

    //获取班次模块信息-数据处理
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
                jsonObject.put("sLanguage", sSMTWorkshopLanguage);
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
                SharedPreferencesUtil.saveValue(context,"nSMTTotalShiftLastRefreshTime",DateUtils.dateToLong(sDateNow));
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
                        LogUtil.e(TAG,"shiftList = " + shiftList.toString());
                        String  sShiftName = shiftList.get(0).getsShiftName();
                        String  sShiftTimeBegin = shiftList.get(0).getsShiftTimeBegin();
                        String  sShiftTimeEnd = shiftList.get(0).getsShiftTimeEnd();
                        String sShiftTime = sShiftTimeBegin + "-" + sShiftTimeEnd;
                        bottom.setShift(String.format(getString(R.string.smt_line_board_shift_no),sShiftName));
                        bottom.setShiftTime(String.format(getString(R.string.smt_line_board_shift_time_begin_end),sShiftTime));
                        if(getCurLanguage() == 1){
                            bottom.setShift("Shift(" + sShiftName+ ")" );
                            bottom.setShiftTime("Time slot(" + sShiftTime+ ")" );
                        }else if(getCurLanguage() == 2){
                            bottom.setShift("班次(" + sShiftName+ ")" );
                            bottom.setShiftTime("時段(" + sShiftTime+ ")" );
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
                jsonObject.put("sLanguage", sSMTWorkshopLanguage);
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
                SharedPreferencesUtil.saveValue(context,"nSMTTotalTimeLastRefreshTime",DateUtils.dateToLong(sDateNow));
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
                                    nSMTTotalWoHeartbeatInteval = Long.parseLong(moduleInfoBean.getHEARTBEAT_INTEVAL());
                                    break;
                                case "WOOUTPUT"://制令单产出信息模块
                                    nSMTTotalWoProductionHeartbeatInteval = Long.parseLong(moduleInfoBean.getHEARTBEAT_INTEVAL());
                                    break;
                                case "STATUS"://生产状态模块
                                    nSMTTotalLineStatusHeartbeatInteval = Long.parseLong(moduleInfoBean.getHEARTBEAT_INTEVAL());
                                    break;
                                case "MESSAGE"://公告信息模块
                                    nSMTTotalMessageHeartbeatInteval = Long.parseLong(moduleInfoBean.getHEARTBEAT_INTEVAL());
                                    break;
                                case "TIME"://同步当前时间模块
                                    nSMTTotalTimeHeartbeatInteval = Long.parseLong(moduleInfoBean.getHEARTBEAT_INTEVAL());
                                    break;
                                case "LINEOUTPUT"://同步当前时间模块
                                    nSMTTotalLineProductionHeartbeatInteval = Long.parseLong(moduleInfoBean.getHEARTBEAT_INTEVAL());
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
                jsonObject.put("sLanguage", sSMTWorkshopLanguage);
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
                ToastUtil.showShortToast(context,e.getMessage().toString());
                return;
            }
        }
    }
    //获取看板模板信息
    public void getPattem(String sKanBanMCNo) {
        getKanBanUserPattem(context, sKanBanMCNo, new OnResultListener() {
            @Override
            public void onSuccess() {
                DBUtil dbUtil = new DBUtil(context);
                mc_pattem = dbUtil.getMcPattemByCode(template_code[1]);//更新展示模板
                user_pattem = dbUtil.getUserPattemByCode(template_code[1]);//更新展示模板
                LogUtil.e(TAG,"mc_pattem = " + mc_pattem.toString());
                LogUtil.e(TAG,"user_pattem = " + user_pattem.toString());
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
                    }
                    smtTitleText = user_pattem.getPATTEM_TITLE();
                    smtTitle.setTitleText(smtTitleText);
                    initColor();
                    initModuleUI( disCurTime, disReTime, disCopyright, disMessage);//初始化模块界面
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
                    if(!TextUtils.isEmpty(time)){
                        String mcRefreshTime = mc_pattem.getREFRESH_TIME();
//                        String mcRefreshTime = "";
                        long nCurSMTTotalMcPattemLastRefreshTime = System.currentTimeMillis();
                        if(TextUtils.isEmpty(mcRefreshTime)){
                            refreshTime = DateUtils.longToDate(nCurSMTTotalMcPattemLastRefreshTime);
                        }else {
                            refreshTime = mcRefreshTime;
                        }
                        LogUtil.e(TAG,"nSMTTotalMcPattemLastRefreshTime = " + refreshTime);
                        SharedPreferencesUtil.saveValue(context,"nSMTTotalMcPattemLastRefreshTime",DateUtils.dateToLong(refreshTime));
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
     * 获取模块信息
     */
    private void init() {

        sLineNoIn = SharedPreferencesUtil.getValue(context,"WorkshopLineNo","");
        if(TextUtils.isEmpty(sLineNoIn)){
            openActivity(context, WorkshopLineSettingActivity.class);
            return;
        }
        messageFlag = true;
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


    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.e("SMTWorkShopBoardActivity-----onPause");
        isStopReceiver = false;
        sStopService();

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
        if(scrollTextView!=null){
            messageStopFlag = true;
            scrollTextView.stopScroll();//停止滚动
        }
        if(handler!=null){
            handler.removeCallbacksAndMessages(null);
        }
        sStopService();
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
        stopTask(getKanBanMcPattemTask);
        stopTask(getShiftInfoTask);
        stopTask(getWoInfoTask);
        stopTask(getWoProductionTask);
        stopTask(getKanBanMsgTask);
        stopTask(moduleInfoTask);
        stopTask(serverDateTask);
        stopTask(getLineProductionInfoTask);
    }
}
