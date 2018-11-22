package com.board.testboard.ui.activity.base.module;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ListView;


import com.board.testboard.R;
import com.board.testboard.adapter.SMTLineAdapter;
import com.board.testboard.app.MyApplication;
import com.board.testboard.bean.LINE_DISPLAY_TIME;
import com.board.testboard.bean.SMT_BOARD_LINE;
import com.board.testboard.bean.SMT_LINE_SETTING;
import com.board.testboard.database.DBUtil;
import com.board.testboard.http.GetData;
import com.board.testboard.ui.activity.base.TitlebarBaseActivity;
import com.board.testboard.utils.DialogUtil;
import com.board.testboard.utils.GsonUtils;
import com.board.testboard.utils.LogUtil;
import com.board.testboard.utils.SharedPreferencesUtil;
import com.board.testboard.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * [线看板多线别设置]
 */

public class SMTLineSettingActivity extends TitlebarBaseActivity {

    @BindView(R.id.view_base_lv_data)
    ListView lvData;
    private Dialog dialog = null;
    private Context context;
    private SMTLineAdapter adapter;
    private List<SMT_LINE_SETTING> lineSettingList;
    private List<SMT_LINE_SETTING> curLineSettingList;
    private List<SMT_BOARD_LINE> dbLines;
    private int firstPosition;
    private int top;
    private final String TAG = SMTLineSettingActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = SMTLineSettingActivity.this;
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
        mTitle.setTitleText(R.string.more_line_setting);
        mTitle.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.VISIBLE);
        mTitle.setBtnRight(0, R.string.save);
        mTitle.setBtnRightOnclickListener(saveOnClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

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
            dialog = DialogUtil.showStatusDialog(context, getString(R.string.get_line_info), getString(R.string.loading_data));
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
                lineSettingList = new ArrayList<>();
                if (sStatus.equalsIgnoreCase("OK")) {
                    String sData = item.getString("data");
                    JSONArray jsonArrayData = new JSONArray(sData);
                    lineSettingList = GsonUtils.getObjects(jsonArrayData.toString(),SMT_LINE_SETTING.class);
                } else {
                    ToastUtil.showShortToast(context,sMsg);
                    return;
                }
                curLineSettingList = new ArrayList<>();
                DBUtil dbUtil = new DBUtil(context);
                dbLines = dbUtil.getAllLine();
                if(lineSettingList!=null && lineSettingList.size()>=1){
                    for (int i = 0; i < lineSettingList.size(); i++) {
                        SMT_LINE_SETTING lineSetting = new SMT_LINE_SETTING();
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

                adapter = new SMTLineAdapter(context, R.layout.item_list_workshop_line_multiple_choice, curLineSettingList);
                lvData.setAdapter(adapter);
                lvData.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                //获取本地文件数据
                String sMoreLineNo = SharedPreferencesUtil.getValue(getBaseContext(), "LineBoardLineNo", "");
                String sMoreLineNoInTemp[] = sMoreLineNo.split(",");
                for (int i = 0; i < lvData.getCount(); i++) {
                    String sStationNoTemp = curLineSettingList.get(i).getLINE_NO();
                    final int nCheck = i;
                    for (int j = 0; j < sMoreLineNoInTemp.length; j++) {
                        //获取的线别代码，与保存的数组比对。若OK，则站位显示勾先把状态
                        if (sStationNoTemp.equalsIgnoreCase(sMoreLineNoInTemp[j])) {
                            lvData.setItemChecked(nCheck, true);
                        }
                    }
                }

                lvData.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        openActivity(context,LineAttributeSettingActivity.class,"LINE_NO",curLineSettingList.get(position).getLINE_NO());
                        return false;
                    }
                });
                lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        DBUtil tempDbutil = new DBUtil(context);
                        String sLineNo = curLineSettingList.get(position).getLINE_NO();
                        String sLineName = curLineSettingList.get(position).getLINE_NAME();
                        CheckedTextView checkedTextView = lvData.getChildAt(position - lvData.getFirstVisiblePosition()).findViewById(R.id.item_list_line);
                        if(checkedTextView.isChecked()){
                            if(tempDbutil.getDisPlayByLine(sLineNo)==null){
                                ToastUtil.showShortToast(context,String.format(getString(R.string.display_time_data_is_null),sLineName,MyApplication.nLineDisplayTime + ""));
                            }else {
                                String sDisplayTime = tempDbutil.getDisPlayByLine(sLineNo).getDISPLAY_TIME();
                                ToastUtil.showShortToast(context,String.format(getString(R.string.display_time_data),sLineName,sDisplayTime));
                            }
                        }
                    }
                });

                lvData.setOnScrollListener(new AbsListView.OnScrollListener() {

                    /**
                     * 滚动状态改变时调用
                     */
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        // 不滚动时保存当前滚动到的位置
                        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                            if (curLineSettingList != null && curLineSettingList.size()>=1) {
                                firstPosition = lvData.getFirstVisiblePosition();
                            }
                        }
                        View v = lvData.getChildAt(0);
                        top =(v==null)?0:v.getTop();

                    }

                    /**
                     * 滚动时调用
                     */
                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    }
                });
                lvData.setSelectionFromTop(firstPosition, top);
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showShortToast(context,e.getMessage().toString());
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
            if(checkedItemIds.length > 0){
                dbUtil.deleteAllSMT_BOARD_LINE();
                for (int i = 0; i < checkedItemIds.length; i++) {
                    if (i == 0) {
                        sLineNo = curLineSettingList.get((int) checkedItemIds[i]).getLINE_NO();
                    } else {
                        sLineNo = sLineNo + "," + curLineSettingList.get((int) checkedItemIds[i]).getLINE_NO();
                    }
                    SMT_LINE_SETTING lineSetting = curLineSettingList.get((int) checkedItemIds[i]);
                    SMT_BOARD_LINE smtBoardLine = new SMT_BOARD_LINE();
                    smtBoardLine.setLINE_NO(lineSetting.getLINE_NO());
                    smtBoardLine.setLINE_NAME(lineSetting.getLINE_NAME());
                    smtBoardLine.setWORKSHOP_ID(lineSetting.getWORKSHOP_ID());
                    smtBoardLine.setWORKSHOP_NO(lineSetting.getWORKSHOP_NO());
                    smtBoardLine.setWORKSHOP_NAME(lineSetting.getWORKSHOP_NAME());
                    smtBoardLine.setPARAM_ID(lineSetting.getPARAM_ID());
                    LINE_DISPLAY_TIME lineDisplayTime = dbUtil.getDisPlayByLine(lineSetting.getLINE_NO());
                    if(lineDisplayTime == null){
                        LINE_DISPLAY_TIME curlineDisplayTime = new LINE_DISPLAY_TIME();
                        curlineDisplayTime.setLINE_NO(lineSetting.getLINE_NO());
                        curlineDisplayTime.setDISPLAY_TIME(MyApplication.nLineDisplayTime + "");
                        curlineDisplayTime.setDISPLAY_TIME_SELECTED("");
                        dbUtil.saveLINE_DISPLAY_TIME(curlineDisplayTime);
                        smtBoardLine.setDISPLAY_TIME(MyApplication.nLineDisplayTime + "");
                    }else {
                        smtBoardLine.setDISPLAY_TIME(lineDisplayTime.getDISPLAY_TIME());
                    }
                    dbUtil.saveSMT_BOARD_LINE(smtBoardLine);
                }
                //保存数据至本地文件中
                SharedPreferencesUtil.saveValue(getBaseContext(), "LineBoardLineNo", sLineNo);
                ToastUtil.showShortToast(context, "[" + sLineNo + "]" + getString(R.string.more_line_success));

                List<LINE_DISPLAY_TIME> curDisplayTimeList = getLINE_DISPLAY_TIME(dbUtil);
                for (int i = 0; i < curDisplayTimeList.size(); i++) {
                    LINE_DISPLAY_TIME tempLineDisplayTime = curDisplayTimeList.get(i);
                    if(tempLineDisplayTime.getDISPLAY_TIME_SELECTED().equals("N")){
                        dbUtil.deleteLINE_DISPLAY_TIME(tempLineDisplayTime);
                    }
                }
                LogUtil.e(TAG,"dbUtil.getAllLineDisplayTime() = " + dbUtil.getAllLineDisplayTime().toString());
                LogUtil.e(TAG,"dbUtil.getAllLine() = " + dbUtil.getAllLine().toString());
                finish();
            }else {
                ToastUtil.showShortToast(context,getString(R.string.more_line_select_line));
                return;
            }

        }
    };
    public List<LINE_DISPLAY_TIME> getLINE_DISPLAY_TIME(DBUtil dbUtil){
        List<SMT_BOARD_LINE> newBoardLineList = dbUtil.getAllLine();
        List<LINE_DISPLAY_TIME> lineDisplayTimeList = dbUtil.getAllLineDisplayTime();
        List<LINE_DISPLAY_TIME> newDisplayTimeList;
        for (int j = 0; j < lineDisplayTimeList.size(); j++) {
            LINE_DISPLAY_TIME tempLineDisplayTime = lineDisplayTimeList.get(j);
            LINE_DISPLAY_TIME newLineDisplayTime = new LINE_DISPLAY_TIME();
            newLineDisplayTime.setLINE_NO(tempLineDisplayTime.getLINE_NO());
            newLineDisplayTime.setDISPLAY_TIME(tempLineDisplayTime.getDISPLAY_TIME());
            for (int i = 0; i < newBoardLineList.size(); i++) {
                SMT_BOARD_LINE newLine = newBoardLineList.get(i);
                if(tempLineDisplayTime.getLINE_NO().equals(newLine.getLINE_NO())){
                    newLineDisplayTime.setDISPLAY_TIME_SELECTED("Y");
                    break;
                }else {
                    newLineDisplayTime.setDISPLAY_TIME_SELECTED("N");
                }
            }
            dbUtil.saveLINE_DISPLAY_TIME(newLineDisplayTime);
        }
        newDisplayTimeList  = dbUtil.getAllLineDisplayTime();
        return newDisplayTimeList;
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (dialog != null) {
            dialog.dismiss();
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent(event);
    }
}
