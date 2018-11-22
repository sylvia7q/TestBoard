package com.board.testboard.ui.activity.base.module;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;


import com.board.testboard.R;
import com.board.testboard.adapter.SMTWorkshopLineAdapter;
import com.board.testboard.app.MyApplication;
import com.board.testboard.bean.SMT_WORKSHOP_LINE_SETTING;
import com.board.testboard.database.DBUtil;
import com.board.testboard.http.GetData;
import com.board.testboard.ui.activity.base.TitlebarBaseActivity;
import com.board.testboard.utils.DialogUtil;
import com.board.testboard.utils.GsonUtils;
import com.board.testboard.utils.SharedPreferencesUtil;
import com.board.testboard.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * [车间看板多线别设置]
 */

public class WorkshopLineSettingActivity extends TitlebarBaseActivity {

    @BindView(R.id.view_base_lv_data)
    ListView lvData;
    private SMTWorkshopLineAdapter adapter;
    private Dialog dialog = null;
    private Context context;
    private List<SMT_WORKSHOP_LINE_SETTING> lineSettingList = null;
    private List<SMT_WORKSHOP_LINE_SETTING> curLineSettingList = null;
    private List<SMT_WORKSHOP_LINE_SETTING> dbLines = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = WorkshopLineSettingActivity.this;

    }

    @Override
    protected void onStart() {
        super.onStart();
        //获取线别
        GetMoreLineInfo();
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_base_setting;
    }



    @Override
    protected void initTitleView() {
        super.initTitleView();
        mTitle.setTitleText(R.string.more_work_shop_line_setting);
        mTitle.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.VISIBLE);
        mTitle.setBtnRight(0, R.string.save);
        mTitle.setBtnRightOnclickListener(saveOnClickListener);
    }

    //获取线别
    private void GetMoreLineInfo() {
        GetMoreLineInfoAsyncTask getMoreLineInfoAsyncTask = new GetMoreLineInfoAsyncTask();
        getMoreLineInfoAsyncTask.execute();
    }


    //获取线别-数据处理
    public class GetMoreLineInfoAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            if (dialog != null) {
                dialog.dismiss();
            }
            dialog = DialogUtil.showDialog(context, getString(R.string.get_line_info), getString(R.string.loading_data),false,false);
        }

        @Override
        protected String doInBackground(String... params) {
            String methodname = "TimsGetBasLineInfo";
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("sMesUser", MyApplication.sMesUser);
                jsonObject.put("sFactoryNo", MyApplication.sFactoryNo);
                jsonObject.put("sLanguage", MyApplication.sLanguage);
                jsonObject.put("sUserNo", MyApplication.sUserNo);
                jsonObject.put("sClientIp", MyApplication.sClientIp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return GetData.getDataByJson(context,jsonObject.toString(), methodname);
        }

        @Override
        protected void onPostExecute(String json) {
            dialog.dismiss();
//            json = "[{\"status\": \"OK\",\"msg\": \"OK\",\"data\":[{\"LINE_NO\":\"SMT-1\",\"NAME_CHT\":\"SMT-1\",\"NAME_CHS\":\"SMT-1\",\"NAME_EN\":\"SMT-1\",\"WORKSHOP_ID\":\"1\"},{\"LINE_NO\":\"SMT-2\",\"NAME_CHT\":\"SMT-2\",\"NAME_CHS\":\"SMT-2\",\"NAME_EN\":\"SMT-2\",\"WORKSHOP_ID\":\"1\"},{\"LINE_NO\":\"SMT-3\",\"NAME_CHT\":\"SMT-3\",\"NAME_CHS\":\"SMT-3\",\"NAME_EN\":\"SMT-3\",\"WORKSHOP_ID\":\"2\"}]}]";
            if (null == json || json.equals("")) {
                ToastUtil.showShortToast(context,getString(R.string.interface_returns_failed));
                return;
            }
            try {
                JSONArray jsonArray = new JSONArray(json);
                JSONObject item = jsonArray.getJSONObject(0);
                String sStatus = item.getString("status");
                String sMsg = item.getString("msg");
                if (sStatus.equalsIgnoreCase("OK")) {
                    String sData = item.getString("data");
                    JSONArray jsonArrayData = new JSONArray(sData);
                    lineSettingList = GsonUtils.getObjects(jsonArrayData.toString(),SMT_WORKSHOP_LINE_SETTING.class);

                } else {
                    ToastUtil.showShortToast(context,sMsg);
                    return;
                }
                curLineSettingList = new ArrayList<>();
                DBUtil dbUtil = new DBUtil(context);
                dbLines = dbUtil.getAllWorkshopLine();
                if(lineSettingList!=null && lineSettingList.size()>=1){
                    for (int i = 0; i < lineSettingList.size(); i++) {
                        SMT_WORKSHOP_LINE_SETTING lineSetting = new SMT_WORKSHOP_LINE_SETTING();
                        lineSetting.setLINE_ID(lineSettingList.get(i).getLINE_ID());
                        lineSetting.setLINE_NO(lineSettingList.get(i).getLINE_NO());
                        lineSetting.setNAME_CHS(lineSettingList.get(i).getNAME_CHS());
                        lineSetting.setNAME_CHT(lineSettingList.get(i).getNAME_CHT());
                        lineSetting.setNAME_EN(lineSettingList.get(i).getNAME_EN());
                        String sLineName = lineSettingList.get(i).getLINE_NAME() + "[" + lineSettingList.get(i).getLINE_NO() + "]";
                        lineSetting.setLINE_NAME(sLineName);
                        lineSetting.setWORKSHOP_ID(lineSettingList.get(i).getWORKSHOP_ID());
                        lineSetting.setWORKSHOP_NO(lineSettingList.get(i).getWORKSHOP_NO());
                        lineSetting.setWORKSHOP_NAME(lineSettingList.get(i).getWORKSHOP_NAME());
                        lineSetting.setPARAM_ID(lineSettingList.get(i).getPARAM_ID());
                        lineSetting.setTYPE(lineSettingList.get(i).getTYPE());
                        lineSetting.setLINE_TYPE(lineSettingList.get(i).getLINE_TYPE());
                        lineSetting.setLINE_TYPE_NAME(lineSettingList.get(i).getLINE_TYPE_NAME());
                        lineSetting.setSTATION_NO_INPUT(lineSettingList.get(i).getSTATION_NO_INPUT());
                        lineSetting.setSTATION_NO_OUTPUT(lineSettingList.get(i).getSTATION_NO_OUTPUT());
                        lineSetting.setSTATION_NAME_INPUT(lineSettingList.get(i).getSTATION_NAME_INPUT());
                        lineSetting.setSTATION_NAME_OUTPUT(lineSettingList.get(i).getSTATION_NAME_OUTPUT());
                        lineSetting.setSTATUS(lineSettingList.get(i).getSTATUS());
                        lineSetting.setSTATUS_NAME(lineSettingList.get(i).getSTATUS_NAME());
                        lineSetting.setSORTID(lineSettingList.get(i).getSORTID());
                        lineSetting.setNOTE(lineSettingList.get(i).getNOTE());
                        lineSetting.setCREATE_DATE(lineSettingList.get(i).getCREATE_DATE());
                        lineSetting.setCREATE_IP(lineSettingList.get(i).getCREATE_IP());
                        lineSetting.setCREATE_USERID(lineSettingList.get(i).getCREATE_USERID());
                        lineSetting.setUPDATE_DATE(lineSettingList.get(i).getUPDATE_DATE());
                        lineSetting.setUPDATE_IP(lineSettingList.get(i).getUPDATE_IP());
                        lineSetting.setUPDATE_USERID(lineSettingList.get(i).getUPDATE_USERID());
                        if(dbLines !=null && dbLines.size()>=1){
                            for (int j = 0; j < dbLines.size(); j++) {
                                if(lineSettingList.get(i).getLINE_NO().equals(dbLines.get(j).getLINE_NO())){
                                    lineSetting.setSELECTED("Y");
                                    break;
                                }else {
                                    lineSetting.setSELECTED("N");
                                }
                            }
                        }else {
                            lineSetting.setSELECTED("N");
                        }
                        curLineSettingList.add(lineSetting);
                    }
                }
                adapter = new SMTWorkshopLineAdapter(context, R.layout.item_list_workshop_line_multiple_choice, curLineSettingList);
                lvData.setAdapter(adapter);
                lvData.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                //获取本地文件数据
                String sMoreLineNo = SharedPreferencesUtil.getValue(getBaseContext(), "WorkshopLineNo", "");
                String sMoreLineNoInTemp[] = sMoreLineNo.split(",");
                for (int i = 0; i < lvData.getCount(); i++) {
                    String sStationNoTemp = curLineSettingList.get(i).getLINE_NO();
                    final int nCheck = i;
                    for (int j = 0; j < sMoreLineNoInTemp.length; j++) {
                        //获取的线别代码，与保存的数组比对。若OK，则站位显示勾先把状态
                        if (sStationNoTemp.equalsIgnoreCase(sMoreLineNoInTemp[j].toString())) {
                            lvData.setItemChecked(nCheck, true);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showShortToast(context, e.getMessage().toString());
                return;
            }
        }
    }

    private View.OnClickListener saveOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DBUtil dbUtil = new DBUtil(context);
            long[] checkedItemIds = lvData.getCheckItemIds();
            String sLineNo = "";
            if(checkedItemIds.length>0){
                dbUtil.deleteAllSMT_WORKSHOP_LINE_SETTING();
                for (int i = 0; i < checkedItemIds.length; i++) {
                    if (i == 0) {
                        sLineNo = curLineSettingList.get((int) checkedItemIds[i]).getLINE_NO();
                    } else {
                        sLineNo = sLineNo + "," + curLineSettingList.get((int) checkedItemIds[i]).getLINE_NO();
                    }
                    SMT_WORKSHOP_LINE_SETTING workshopLineSetting = curLineSettingList.get((int) checkedItemIds[i]);
                    dbUtil.saveSMT_WORKSHOP_LINE_SETTING(workshopLineSetting);
                }
                MyApplication.sMoreLineNo = sLineNo;
                //保存数据至本地文件中
                SharedPreferencesUtil.saveValue(getBaseContext(), "WorkshopLineNo", sLineNo);
                ToastUtil.showShortToast(context, "[" + sLineNo + "]" + getString(R.string.more_line_success));
                finish();
            }else {
                ToastUtil.showShortToast(context, getString(R.string.more_line_select_line));
            }

        }
    };
    @Override
    protected void onPause() {
        super.onPause();
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
