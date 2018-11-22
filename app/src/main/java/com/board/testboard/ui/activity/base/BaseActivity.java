package com.board.testboard.ui.activity.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Toast;


import com.board.testboard.R;
import com.board.testboard.app.MyApplication;
import com.board.testboard.bean.KANBAN_MC;
import com.board.testboard.bean.KANBAN_MC_PATTEM;
import com.board.testboard.bean.KANBAN_USER_PATTEM;
import com.board.testboard.database.DBUtil;
import com.board.testboard.http.GetData;
import com.board.testboard.presenter.OnRequestListener;
import com.board.testboard.presenter.OnResultListener;
import com.board.testboard.ui.activity.base.module.KanBanMCActivity;
import com.board.testboard.utils.AppUtil;
import com.board.testboard.utils.GsonUtils;
import com.board.testboard.utils.LogUtil;
import com.board.testboard.utils.SharedPreferencesUtil;
import com.board.testboard.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Activity基类
 */

public class BaseActivity extends Activity {

    // 管理运行的所有的activity
    public final static List<Activity> mActivities = new LinkedList<Activity>();

    public static BaseActivity activity;
    private final String TAG = BaseActivity.class.getSimpleName();
    private GetKanBanMcPattemTask getKanBanMcPattemTask;
    private GetKanBanUserPattemTask getKanBanUserPattemTask;
    private GetKanBanTask getKanBanTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        synchronized (mActivities) {
            mActivities.add(this);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
    }
    @Override
    protected void onPause() {
        super.onPause();
        activity = null;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        synchronized (mActivities) {
            mActivities.remove(this);
        }
        stopTask(getKanBanMcPattemTask);
        stopTask(getKanBanUserPattemTask);
        stopTask(getKanBanTask);
    }


    public void killAll() {
        // 复制了一份mActivities 集合Å
        List<Activity> copy;
        synchronized (mActivities) {
            copy = new LinkedList<>(mActivities);
        }
        for (Activity activity : copy) {
            activity.finish();
        }
        // 杀死当前的进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     *
     * @param context
     * @param cls
     */
    public void openActivity(Context context,Class<?> cls){
        Intent intent = new Intent(context, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     * 带一个参数跳转页面
     * @param context
     * @param cls
     * @param paramsKey
     * @param paramsValues
     */
    public void openActivity(Context context,Class<?> cls,String paramsKey,String paramsValues){
        Intent intent = new Intent(context, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(paramsKey,paramsValues);
        startActivity(intent);
    }

    public void openActivity(Context context,Class<?> cls,int requestCode){
        Intent intent = new Intent(context, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(intent,requestCode);
    }
    public void openActivity(Context context,String className){
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName(context,className);
        startActivity(intent);
    }
    public void openActivity(Context context,String className,String paramsKey,String paramsValues){
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(paramsKey,paramsValues);
        intent.setClassName(context,className);
        startActivity(intent);
    }

    /**
     * 获取模板列表
     * @return
     */
    public List<HashMap<String, String>> getTemplateActivity() {
        List<HashMap<String, String>> mapList = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();

        String template_code[] = getResources().getStringArray(R.array.template_code);
        String templateName[] = getResources().getStringArray(R.array.template_name);
        int count = template_code.length;
        for(int i = 0; i< count; i++) {
            map.put(template_code[i],templateName[i]);
            mapList.add(map);
        }
        return mapList;
    }

    /**
     * 获取具体模板
     * @param listMcTemplateName
     * @param key
     * @return
     */
    public String getClassName(List<HashMap<String, String>> listMcTemplateName,String key){
        String className = "";
        for (int j = 0; j < listMcTemplateName.size(); j++) {
            Map<String,String> map = listMcTemplateName.get(j);
            if(map.containsKey(key)){
                className = map.get(key);
                return className;
            }
        }
        return className;
    }

    /**
     * 颜色转换
     * @param rgb
     * @return
     */
    public int parseColor(String rgb){
        int color = Color.rgb(255,0,0);
        if(!TextUtils.isEmpty(rgb)){
            String rgbs[] = rgb.split(",");
            if(rgbs.length!=0 && rgbs.length == 3){
                int red = Integer.parseInt(rgbs[0]);
                int green = Integer.parseInt(rgbs[1]);
                int blue = Integer.parseInt(rgbs[2]);
                if((0<=red&&red<=255)&&(0<=green&&green<=255)&&(0<=blue&&blue<=255)){
                    color = Color.rgb(red,green,blue);
                }
            }
        }
        return color;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String str) {
        switch (str) {
            case "EVENT_REFRESH_LANGUAGE":
                changeAppLanguage();
//                recreate();//刷新界面
                break;
        }
    }
    /**
     * 根据参数觉得决定是否显示UI
     *
     * @param params
     * @return
     */
    protected boolean showUI(String params) {
        if (params.equals("Y")) {
            return true;
        } else if (params.equals("N")) {
            return false;
        } else {
            return false;
        }
    }
    /**
     * 切换语言
     * 1、文件夹为value-en，代码为Locale.ENGLISH或Locale.US（美国），Locale.CANADA（澳大利亚）……
     * 2、文件夹为value-en-rUS，那么代码只能为Locale.US
     */
    public void changeAppLanguage() {
        String language = SharedPreferencesUtil.getValue(this, "DISPLAY_LANGUAGE", "S");
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            switch (language) {
                case "E":
                    config.setLocale(Locale.US);
                    break;
                case "S":
                    config.setLocale(Locale.SIMPLIFIED_CHINESE);
                    break;
                case "T":
                    config.setLocale(Locale.TRADITIONAL_CHINESE);
                    break;
                default:
                    config.setLocale(Locale.SIMPLIFIED_CHINESE);
                    break;
            }
        } else {
            switch (language) {
                case "E":
                    config.locale = Locale.US;
                    break;
                case "S":
                    config.locale = Locale.SIMPLIFIED_CHINESE;
                    break;
                case "T":
                    config.locale = Locale.TRADITIONAL_CHINESE;
                    break;
                default:
                    config.locale = Locale.SIMPLIFIED_CHINESE;
                    break;
            }
        }

        resources.updateConfiguration(config, dm);
    }

    /**
     * 判断当前语言是否要切换
     */
    public boolean checkLocalLanguage(){
        boolean flag;
        String displayLanguage = SharedPreferencesUtil.getValue(this, "DISPLAY_LANGUAGE", "S");
        String language = "";
        switch (displayLanguage){
            case "E":
                language = "en_US";
                break;
            case "S":
                language = "zh_CN";
                break;
            case "T":
                language = "zh_TW";
                break;
            default:
                language = "zh_CN";
                break;
        }
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();

        String localLanguage = config.locale.toString();
        LogUtil.e(TAG,"localLanguage = " + localLanguage);
        if(localLanguage.equals(language)){
            flag = false;
        }else {
            flag = true;
        }
        return flag;
    }

    /**
     * 获取当地语言
     * @return
     */
    public String getLocalLanguage(){
        String language = "";
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        String localLanguage = config.locale.toString();
        if(localLanguage.equals("en_US")){
            language = "E";
        }else if(localLanguage.equals("zh_CN")){
            language = "S";
        }else if(localLanguage.equals("zh_TW")){
            language = "T";
        }
        LogUtil.e(TAG,"localLanguage = " + localLanguage + ", language = " + language);
        return language;
    }
    public  void stopTask(AsyncTask task){
        if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
            task.cancel(true); // 如果Task还在运行，则先取消它，然后执行关闭activity代码
        }
    }
    //获取看板模板信息
    public  void getKanBanMcPattem(Context context, String sKanBanMCNo, String sPattenNo, List<KANBAN_USER_PATTEM> userPattems, OnResultListener listener) {
         getKanBanMcPattemTask = new GetKanBanMcPattemTask(context,userPattems,listener);
        getKanBanMcPattemTask.execute(sKanBanMCNo, sPattenNo);
    }
    //获取看板模板信息-数据处理
    public  class GetKanBanMcPattemTask extends AsyncTask<String, Void, String> {
        private Context context;
        private String methodname = "";
        private OnResultListener listener;
        private List<KANBAN_USER_PATTEM> userPattems;
        private List<KANBAN_MC_PATTEM> mcPattemList;

        public GetKanBanMcPattemTask(Context context,  List<KANBAN_USER_PATTEM> userPattems,OnResultListener listener) {
            this.context = context;
            this.userPattems = userPattems;
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            if(isCancelled()){
                return null;
            }
            methodname = "TimsGetKanBanMcPattemInfo";
            JSONObject jsonObject = new JSONObject();
            String sKanbanMcNo = params[0];
            String sPattenNo = params[1];
            try {

                jsonObject.put("sMesUser", MyApplication.sMesUser);
                jsonObject.put("sFactoryNo", MyApplication.sFactoryNo);
                jsonObject.put("sLanguage", MyApplication.sLanguage);
                jsonObject.put("sUserNo", MyApplication.sUserNo);
                jsonObject.put("sClientIp", MyApplication.sClientIp);
                jsonObject.put("sKanbanMcNo", sKanbanMcNo);
                jsonObject.put("sPattenNo", sPattenNo);
            } catch (JSONException ex) {
                ex.printStackTrace();
                listener.onFail(ex.getMessage().toString());
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
                listener.onFail(context.getString(R.string.interface_returns_failed));
                return;
            }
            try {
                JSONArray jsonArray = new JSONArray(json);
                JSONObject item = jsonArray.getJSONObject(0);
                String sStatus = item.getString("status");
                String sMsg = item.getString("msg");
                mcPattemList = new ArrayList<>();
                if (sStatus.equalsIgnoreCase("OK")) {
                    String sData = item.getString("data");
                    JSONArray jsonArrayData = new JSONArray(sData);
                    mcPattemList = GsonUtils.getObjects(jsonArrayData.toString(), KANBAN_MC_PATTEM.class);
                    if(mcPattemList!=null && mcPattemList.size()>=1){
                        LogUtil.e(TAG,"mcPattemList = " + mcPattemList.toString());
                        if(mcPattemList!=null && mcPattemList.size()>=2){
                            //顺序排
                            Collections.sort(mcPattemList, new Comparator<KANBAN_MC_PATTEM>() {
                                public int compare(KANBAN_MC_PATTEM arg0, KANBAN_MC_PATTEM arg1) {
                                    int hits0 = Integer.parseInt(arg0.getSORTID());
                                    int hits1 = Integer.parseInt(arg1.getSORTID());
                                    if (hits1 > hits0) {
                                        return 1;
                                    } else if (hits1 == hits0) {
                                        return 0;
                                    } else {
                                        return -1;
                                    }
                                }
                            });
                        }
                        saveMcPattem(context,userPattems,mcPattemList,listener);
                    }else {
                        listener.onFail(context.getString(R.string.parse_error));
                    }
                } else {
                    Toast.makeText(context,sMsg,Toast.LENGTH_SHORT).show();
                    listener.onFail(sMsg);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                listener.onFail(e.getMessage().toString());
            }

        }
    }
    //获取用户模板信息
    public void getKanBanUserPattem(Context context,String sKanBanMcNo,OnResultListener listener) {
         getKanBanUserPattemTask = new GetKanBanUserPattemTask(context,sKanBanMcNo,listener);
        getKanBanUserPattemTask.execute();
    }
    //获取用户模板信息-数据处理
    public class GetKanBanUserPattemTask extends AsyncTask<String, Void, String> {

        private Context context;
        private String sKanBanMcNo;
        private OnResultListener listener;
        private List<KANBAN_USER_PATTEM> userPattemList;

        public GetKanBanUserPattemTask(Context context, String sKanBanMcNo, OnResultListener listener) {
            this.context = context;
            this.sKanBanMcNo = sKanBanMcNo;
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            String methodname = "TimsGetKanBanUserPattemInfo";
            JSONObject jsonObject = new JSONObject();
            try {

                jsonObject.put("sMesUser", MyApplication.sMesUser);
                jsonObject.put("sFactoryNo", MyApplication.sFactoryNo);
                jsonObject.put("sLanguage", MyApplication.sLanguage);
                jsonObject.put("sUserNo", MyApplication.sUserNo);
                jsonObject.put("sClientIp", MyApplication.sClientIp);
            } catch (JSONException ex) {
                ex.printStackTrace();
                listener.onFail(ex.getMessage().toString());
            }

            return GetData.getDataByJson(context,jsonObject.toString(), methodname);

        }

        @Override
        protected void onPostExecute(String json) {
//            json = "[{\"status\":\"OK\",\"msg\":\"提示OK信息!\",\"sDateNow\":\"2018.09.10 16:20:20\",\"data\":[{\"PATTEM_NO\":\"2\",\"DISPLAY_LANGUAGE\":\"S\",\"NAME_CHS\":\"SMT线看板\",\"NAME_CHT\":\"12\",\"NAME_EN\":\"12\",\"INTERNAL_CODE\":\"doov_001\",\"KANBAN_TYPE\":\"1\",\"IS_CUSTOMIZE\":\"Y\",\"TITLE_CHS\":\"1245\",\"TITLE_CHT\":\"1245\",\"TITLE_EN\":\"1245\",\"HEARTBEAT_INTEVAL\":\"18\",\"IS_DISP_COPYRIGHT\":\"Y\",\"IS_DISP_CURRENTTIME\":\"N\",\"IS_DISP_REFRESH_TIME\":\"Y\",\"IS_DISP_MESSAGE\":\"Y\",\"SORTID\":1,\"STATUS\":\"222\"}]}]";
            if (TextUtils.isEmpty(json)) {
                listener.onFail(context.getString(R.string.interface_returns_failed));
                return;
            }
            try {
                JSONArray jsonArray = new JSONArray(json);
                JSONObject item = jsonArray.getJSONObject(0);
                String sStatus = item.getString("status");
                String sMsg = item.getString("msg");
                userPattemList = new ArrayList<>();
                if (sStatus.equalsIgnoreCase("OK")) {
                    String sData = item.getString("data");
                    JSONArray jsonArrayData = new JSONArray(sData);
                    userPattemList = GsonUtils.getObjects(jsonArrayData.toString(), KANBAN_USER_PATTEM.class);
                } else {
                    listener.onFail(sMsg);
                }

                if(userPattemList!=null && userPattemList.size()>=1){
                    LogUtil.e(TAG,"userPattemList = " + userPattemList.toString());
                    List<KANBAN_USER_PATTEM> curUserPattem = saveUserPattem(context,userPattemList);
                    if(curUserPattem!=null && curUserPattem.size()>=1){
                        getKanBanMcPattem(context,sKanBanMcNo,"",curUserPattem,listener);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                listener.onFail(e.getMessage());
            }

        }
    }
    /**
     * 初始化用户模板列表
     */
    private List<KANBAN_USER_PATTEM> saveUserPattem(Context context,List<KANBAN_USER_PATTEM> userPattemList){

        DBUtil dbUtil = new DBUtil(context);
        List<KANBAN_USER_PATTEM> oldAllUserPattem = dbUtil.getAllUserPattem();
        if(oldAllUserPattem!=null && oldAllUserPattem.size()>=1){
            for (int i = 0; i < oldAllUserPattem.size(); i++) {
                dbUtil.deleteUserPattem(oldAllUserPattem.get(i));
            }
        }
        List<KANBAN_USER_PATTEM> newAllUserPattem;
        List<HashMap<String, String>> mapList = getTemplateActivity();
        if(userPattemList.size() <= mapList.size()){
            for(int i = 0; i< userPattemList.size(); i++) {
                KANBAN_USER_PATTEM kanbanUserPattem = userPattemList.get(i);
                for (int j = 0; j < mapList.size(); j++) {
                    if(mapList.get(j).containsKey(kanbanUserPattem.getINTERNAL_CODE())){//筛选出INTERNAL_CODE与本地存储一致的看板显示在列表。
                        dbUtil.saveUserPattem(kanbanUserPattem);
                    }
                }

            }
        }else {
            ToastUtil.showShortToast(context,context.getString(R.string.return_undefined_template));
        }
        newAllUserPattem = dbUtil.getAllUserPattem();
        return newAllUserPattem;
    }

    /**
     * 保存展示模板
     * @param
     * @return
     */
    public void saveMcPattem(Context context,List<KANBAN_USER_PATTEM> user_pattems,List<KANBAN_MC_PATTEM> mc_pattems,OnResultListener listener) {
        DBUtil dbUtil = new DBUtil(context);
        List<KANBAN_MC_PATTEM> oldAllMCPattem = dbUtil.getAllMcPattem();
        if(oldAllMCPattem!=null && oldAllMCPattem.size()>=1){
            for (int i = 0; i < oldAllMCPattem.size(); i++) {
                dbUtil.deleteMCPattem(oldAllMCPattem.get(i));
            }
        }
        for(int i = 0; i< user_pattems.size(); i++) {
            KANBAN_USER_PATTEM userPattem = user_pattems.get(i);
            for (int j = 0; j < mc_pattems.size(); j++) {
                KANBAN_MC_PATTEM mcPattem = mc_pattems.get(j);
                if(mcPattem.getPATTEM_NO().equals(userPattem.getPATTEM_NO())){//获取用户看板的模板内码
                    KANBAN_MC_PATTEM newMcPattem = new KANBAN_MC_PATTEM();
                    newMcPattem.setPATTEM_NO(mcPattem.getPATTEM_NO());
                    newMcPattem.setKANBAN_MC_NO(mcPattem.getKANBAN_MC_NO());
                    newMcPattem.setFACTORY_NO(mcPattem.getFACTORY_NO());
                    newMcPattem.setDISPLAY_LANGUAGE(mcPattem.getDISPLAY_LANGUAGE());
                    newMcPattem.setDISPLAY_TIME(mcPattem.getDISPLAY_TIME());
                    newMcPattem.setHEARTBEAT_TIME(mcPattem.getHEARTBEAT_TIME());
                    newMcPattem.setREFRESH_TIME(mcPattem.getREFRESH_TIME());
                    newMcPattem.setINTERNAL_CODE(userPattem.getINTERNAL_CODE());
                    newMcPattem.setIP(mcPattem.getIP());
                    newMcPattem.setMAC(mcPattem.getMAC());
                    newMcPattem.setMC_NAME(mcPattem.getMC_NAME());
                    newMcPattem.setSORTID(mcPattem.getSORTID());
                    newMcPattem.setNOTE(mcPattem.getNOTE());
                    newMcPattem.setSTATUS(mcPattem.getSTATUS());
                    newMcPattem.setCREATE_DATE(mcPattem.getCREATE_DATE());
                    newMcPattem.setCREATE_IP(mcPattem.getCREATE_IP());
                    newMcPattem.setCREATE_USERID(mcPattem.getCREATE_USERID());
                    newMcPattem.setUPDATE_DATE(mcPattem.getUPDATE_DATE());
                    newMcPattem.setUPDATE_IP(mcPattem.getUPDATE_IP());
                    newMcPattem.setUPDATE_USERID(mcPattem.getUPDATE_USERID());
                    dbUtil.saveMcPattem(newMcPattem);
                }
            }
        }
        listener.onSuccess();
    }

    //获取看板代码
    public void getKanBanMCCode(Context context,String sKanBanMCNo,OnRequestListener listener) {
         getKanBanTask = new GetKanBanTask(context,sKanBanMCNo,listener);
        getKanBanTask.execute(sKanBanMCNo);
    }

    //获取看板设备代码-数据处理
    public class GetKanBanTask extends AsyncTask<String, Void, String> {
        private Context context;
        private String sKanBanMCNo;
        private OnRequestListener listener;
        private List<KANBAN_MC> codeBeanList;

        public GetKanBanTask(Context context, String sKanBanMCNo, OnRequestListener listener) {
            this.context = context;
            this.sKanBanMCNo = sKanBanMCNo;
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            String methodname = "TimsGetKanBanMcInfo";
            JSONObject jsonObject = new JSONObject();
            String sKanBanNo = params[0];
//            String sIp = params[1];
//            String sMac = params[2];
            try {

                jsonObject.put("sMesUser", MyApplication.sMesUser);
                jsonObject.put("sFactoryNo", MyApplication.sFactoryNo);
                jsonObject.put("sLanguage", MyApplication.sLanguage);
                jsonObject.put("sUserNo", MyApplication.sUserNo);
                jsonObject.put("sClientIp", MyApplication.sClientIp);
                jsonObject.put("sKanbanMcNo", sKanBanNo);
                jsonObject.put("sMac", MyApplication.sMac);
                jsonObject.put("sIp", AppUtil.getIp());
            } catch (JSONException ex) {
                ex.printStackTrace();
                listener.onFail(ex.getMessage());
            }
            return GetData.getDataByJson(context,jsonObject.toString(), methodname);

        }

        @Override
        protected void onPostExecute(String json) {
//            json = "[{\"status\": \"OK\",\"msg\": \"OK\",\"data\":[{\"KANBAN_MC_NO\":\"1\",\"FACTORY_NO\":\"0\",\"NAME_CHS\":\"SMT-1\",\"NAME_CHT\": \"123\",\"NAME_EN\": \"123\",\"HEARTBEAT_TIME\": \"10\",\"REFRESH_TIME\": \"2018\",\"SORTID\":1,\"STATUS\": \"123\",\"NOTE\": \"123\",\"DISPLAY_LANGUAGE\": \"S\",\"MAC\": \"192.168.0.3\",\"IP\": \"192.168.0.3\",\"CREATE_USERID\": \"sa\",\"CREATE_DATE\": \"2018/9/4 15:02:53\",\"CREATE_IP\": \"192.168.1.36\",\"UPDATE_USERID\": \"sa\",\"UPDATE_DATE\": \"2018/9/4 15:02:53\",\"UPDATE_IP\": \"192.168.1.36\"},{\"KANBAN_MC_NO\":\"2\",\"FACTORY_NO\":\"0\",\"NAME_CHS\":\"SMT-2\",\"NAME_CHT\": \"123\",\"NAME_EN\": \"123\",\"HEARTBEAT_TIME\": \"15\",\"REFRESH_TIME\": \"2018/9/4 15:02:53\",\"SORTID\":2,\"STATUS\": \"123\",\"NOTE\": \"123\",\"DISPLAY_LANGUAGE\": \"S\",\"CREATE_USERID\": \"sa\",\"CREATE_DATE\": \"2018/9/4 15:02:53\",\"CREATE_IP\": \"192.168.1.36\",\"UPDATE_USERID\": \"sa\",\"UPDATE_DATE\": \"2018/9/4 15:02:53\",\"UPDATE_IP\": \"192.168.1.36\"}]}]";
            if (null == json || json.equals("")) {
                ToastUtil.showShortToast(context,getString(R.string.interface_returns_failed));
                return;
            }
            try {
                JSONArray jsonArray = new JSONArray(json);
                JSONObject item = jsonArray.getJSONObject(0);
                String sStatus = item.getString("status");
                String sMsg = item.getString("msg");
                codeBeanList = new ArrayList<>();
                if (sStatus.equalsIgnoreCase("OK")) {
                    String sData = item.getString("data");
                    JSONArray itemData = new JSONArray(sData);
                    codeBeanList = GsonUtils.getObjects(itemData.toString(),KANBAN_MC.class);
                } else {
                    listener.onFail(sMsg);
                }
                if (codeBeanList != null && codeBeanList.size()>=1) {
                    listener.onSuccess(codeBeanList);
                    if(!TextUtils.isEmpty(sKanBanMCNo)){
                        updateKanbanMcCode(context,codeBeanList,sKanBanMCNo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                listener.onFail(e.getMessage());
            }
        }
    }
    /**
     * 更新看板设备代码
     */
    public void updateKanbanMcCode(Context context,List<KANBAN_MC> codeBeanList,String sKanBanMCNo){
        List<KANBAN_MC> newCodeBeanList = new ArrayList<>();
        if(codeBeanList !=null && codeBeanList.size()>=1){
            for (int i = 0; i < codeBeanList.size(); i++) {
                for (Iterator iterator = codeBeanList.iterator(); iterator.hasNext();) {
                    KANBAN_MC kanbanMc = (KANBAN_MC) iterator.next();
                    if (sKanBanMCNo.equals(kanbanMc.getKANBAN_MC_NO())) {
                        newCodeBeanList.add(kanbanMc);//如果设备代码存在服务器中，则更新信息
                        break;
                    }
                }
            }
        }
        if(newCodeBeanList!=null && newCodeBeanList.size()>=1){
            String kanbanMcNo = newCodeBeanList.get(0).getKANBAN_MC_NO();
            String factoryNo = newCodeBeanList.get(0).getFACTORY_NO();
            String mcName = newCodeBeanList.get(0).getMC_NAME();
            String hearTableTime = newCodeBeanList.get(0).getHEARTBEAT_TIME();
            String refreshTime = newCodeBeanList.get(0).getREFRESH_TIME();
            String sortid = newCodeBeanList.get(0).getSORTID();
            String status = newCodeBeanList.get(0).getSTATUS();
            String note = newCodeBeanList.get(0).getNOTE();
            String displayLanguage = newCodeBeanList.get(0).getDISPLAY_LANGUAGE();
            String createUserId = newCodeBeanList.get(0).getCREATE_USERID();
            String createDate = newCodeBeanList.get(0).getCREATE_DATE();
            String createIp = newCodeBeanList.get(0).getCREATE_IP();
            String updateUserId = newCodeBeanList.get(0).getUPDATE_USERID();
            String updateDate = newCodeBeanList.get(0).getUPDATE_DATE();
            String updateIp = newCodeBeanList.get(0).getUPDATE_IP();
            //保存数据至本地文件中
            SharedPreferencesUtil.saveValue(getBaseContext(), "KANBAN_MC_NO", kanbanMcNo);
            SharedPreferencesUtil.saveValue(getBaseContext(), "FACTORY_NO", factoryNo);
            SharedPreferencesUtil.saveValue(getBaseContext(), "MC_NAME", mcName);
            SharedPreferencesUtil.saveValue(getBaseContext(), "HEARTBEAT_TIME", hearTableTime);
            SharedPreferencesUtil.saveValue(getBaseContext(), "REFRESH_TIME", refreshTime);
            SharedPreferencesUtil.saveValue(getBaseContext(), "SORTID", sortid);
            SharedPreferencesUtil.saveValue(getBaseContext(), "STATUS", status);
            SharedPreferencesUtil.saveValue(getBaseContext(), "NOTE", note);
            SharedPreferencesUtil.saveValue(getBaseContext(), "DISPLAY_LANGUAGE", displayLanguage);
            SharedPreferencesUtil.saveValue(getBaseContext(), "CREATE_USERID", createUserId);
            SharedPreferencesUtil.saveValue(getBaseContext(), "CREATE_DATE", createDate);
            SharedPreferencesUtil.saveValue(getBaseContext(), "CREATE_IP", createIp);
            SharedPreferencesUtil.saveValue(getBaseContext(), "UPDATE_USERID", updateUserId);
            SharedPreferencesUtil.saveValue(getBaseContext(), "UPDATE_DATE", updateDate);
            SharedPreferencesUtil.saveValue(getBaseContext(), "UPDATE_IP", updateIp);
            MyApplication.sLanguage = displayLanguage;
            SharedPreferencesUtil.saveValue(context, "sLanguage", MyApplication.sLanguage); //语言
            if(checkLocalLanguage()){
                EventBus.getDefault().post("EVENT_REFRESH_LANGUAGE");
                checkAutoShowTemplate(context);
            }else {
                checkAutoShowTemplate(context);
            }
        }else {//已选择的看板代码不存在，请重新选择看板代码
            Toast.makeText(context,String.format(getString(R.string.select_the_kanban_mc_code_empty),sKanBanMCNo),Toast.LENGTH_SHORT).show();
            openActivity(context,KanBanMCActivity.class); //如果设备代码不存在服务器中，则重新选择

        }
    }
    /**
     * 判断是否为自动展示模板
     */
    private void checkAutoShowTemplate(Context context){
        boolean isAutoRun = SharedPreferencesUtil.getValue(getBaseContext(), "isAutoRun", false);
        //如果是自动模式，则跳转到需要展示的模板，如果不是，则跳转到菜单界面
        if(isAutoRun){
            openPattemActivity(context);
        }else {
            openActivity(context, PattemMenuActivity.class);
        }
    }

    /**
     * 跳转到需要展示的模板
     */
    private void openPattemActivity(final Context context){
        String kanbanMcNo = SharedPreferencesUtil.getValue(getBaseContext(), "KANBAN_MC_NO", "");
        getKanBanUserPattem(context, kanbanMcNo, new OnResultListener() {
            @Override
            public void onSuccess() {
                DBUtil dbUtil = new DBUtil(context);
                List<KANBAN_MC_PATTEM> mcPattems = dbUtil.getAllMcPattem();
                String className = getClassName(getTemplateActivity(),mcPattems.get(0).getINTERNAL_CODE());
                LogUtil.e(TAG,"className = " + className);
                if(!TextUtils.isEmpty(className)){
                    openActivity(context,className,"isAutoPattem","Y");
                }else {
                    openActivity(context,PattemMenuActivity.class);
                }

//                String PATTEM_NO = SharedPreferencesUtil.getValue(context,"PATTEM_NO","");
//                if(!TextUtils.isEmpty(PATTEM_NO)){//如果存有当前展示模板的名字，则继续展示该模板，如果没有，则按展示模板数据顺序展示模板
//                    KANBAN_MC_PATTEM kanbanMcPattem = dbUtil.getMcPattemByNo(PATTEM_NO);
//                    String className = getClassName(getTemplateActivity(),kanbanMcPattem.getINTERNAL_CODE());
//                    openActivity(context,className);
//                }else {
//                    List<KANBAN_MC_PATTEM> mcPattems = dbUtil.getAllMcPattem();
//                    String className = getClassName(getTemplateActivity(),mcPattems.get(0).getINTERNAL_CODE());
//                    openActivity(context,className);
//                }

            }

            @Override
            public void onFail(String result) {
                openActivity(context, PattemMenuActivity.class);
            }
        });
    }
}
