package com.board.testboard.ui.activity.base.module;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;


import com.board.testboard.R;
import com.board.testboard.adapter.SMTStationAdapter;
import com.board.testboard.app.MyApplication;
import com.board.testboard.bean.LINE_STATION;
import com.board.testboard.bean.Station;
import com.board.testboard.database.DBUtil;
import com.board.testboard.http.GetData;
import com.board.testboard.ui.activity.base.TitlebarBaseActivity;
import com.board.testboard.utils.DialogUtil;
import com.board.testboard.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * [线别工站设置]
 */

public class LineStationSettingActivity extends TitlebarBaseActivity {

    @BindView(R.id.view_base_lv_data)
    ListView lvData;
    private Context context;
    private List<String> listStationNo = null;
    private List<String> listStationName = null;
    private List<String> curListStationName = null;
    private List<String> listStationNameEn = null;
    private List<String> listStationNameChs = null;
    private Dialog dialog = null;
    private String sLineNo = "";  // 线别
    private List<LINE_STATION> dbStationList;
    private List<Station> curStationList;
    private List<Station> stationList;
    private List<Station> selectStations = new ArrayList<>();
    private SMTStationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = LineStationSettingActivity.this;
        sLineNo = getIntent().getStringExtra("LINE_NO");
        //获取工站
        GetStationInfo(sLineNo);
    }


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_base_setting;
    }

    @Override
    protected void initTitleView() {
        super.initTitleView();
        mTitle.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.VISIBLE);
        mTitle.setTitleText(String.format(getString(R.string.line_station_set),sLineNo));
        mTitle.setBtnRight(0, R.string.save);
        mTitle.setBtnRightOnclickListener(saveOnClickListener);
        mTitle.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LineStationSettingActivity.this.finish();
            }
        });
    }

    //获取工站
    private void GetStationInfo(String sLineNo) {
        //判断线别是否有选择
        TimsGetBasStationInfoAsyncTask getStationInfoAsyncTask = new TimsGetBasStationInfoAsyncTask();
        getStationInfoAsyncTask.execute(sLineNo);
    }

    //获取工站-数据处理
    public class TimsGetBasStationInfoAsyncTask extends AsyncTask<String, Void, String> {
        String sLineNo = "";

        @Override
        protected void onPreExecute() {
            if (dialog != null) {
                dialog.dismiss();
            }
            dialog = DialogUtil.showStatusDialog(context, getString(R.string.get_station_info), getString(R.string.loading_data));
        }

        @Override
        protected String doInBackground(String... params) {
            String methodname = "TimsGetBasStationInfo";
            JSONObject jsonObject = new JSONObject();
            sLineNo = params[0];
            try {
                jsonObject.put("sMesUser", MyApplication.sMesUser);
                jsonObject.put("sFactoryNo", MyApplication.sFactoryNo);
                jsonObject.put("sLanguage", MyApplication.sLanguage);
                jsonObject.put("sUserNo", MyApplication.sUserNo);
                jsonObject.put("sClientIp", MyApplication.sClientIp);
                jsonObject.put("sLineNo", sLineNo);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return GetData.getDataByJson(context,jsonObject.toString(), methodname);
        }

        @Override
        protected void onPostExecute(String json) {
            dialog.dismiss();
//            json = "[{\"status\": \"OK\",\"msg\": \"OK\",\"data\":[{\"STATION_ID\":\"SMT-A\",\"STATION_NO\":\"SMT-A\",\"NAME_CHT\":\"SMT-1\",\"NAME_CHS\":\"SMT-1\",\"NAME_EN\":\"SMT-1\",\"PROCESS_NO\":\"SMT-1\"},{\"STATION_ID\":\"SMT-B\",\"STATION_NO\":\"SMT-B\",\"NAME_CHT\":\"SMT-2\",\"NAME_CHS\":\"SMT-2\",\"NAME_EN\":\"SMT-2\",\"PROCESS_NO\":\"SMT-2\"}]}]";
            if (null == json || json.equals("")) {
                ToastUtil.showShortToast(context,getString(R.string.interface_returns_failed));
                return;
            }
            try {
                JSONArray jsonArray = new JSONArray(json);
                JSONObject item = jsonArray.getJSONObject(0);
                String sStatus = item.getString("status");
                String sMsg = item.getString("msg");
                stationList = new ArrayList<>();
                curListStationName = new ArrayList<>();
                if (sStatus.equalsIgnoreCase("OK")) {
                    String sData = item.getString("data");
                    JSONArray jsonArrayData = new JSONArray(sData);
                    listStationNo = new ArrayList<String>();
                    listStationName = new ArrayList<String>();
                    listStationNameEn = new ArrayList<String>();
                    listStationNameChs = new ArrayList<String>();
                    for (int i = 0; i < jsonArrayData.length(); i++) {
                        JSONObject itemData = jsonArrayData.getJSONObject(i);
                        String sStationId = itemData.getString("STATION_ID");
                        String sStationNo = itemData.getString("STATION_NO");
                        String sStationName = itemData.getString("STATION_NAME");
                        String sStationNameChs = itemData.getString("NAME_CHS");
                        String sStationNameCht = itemData.getString("NAME_CHT");
                        String sStationNameEn = itemData.getString("NAME_EN");
                        String sProcessNo = itemData.getString("PROCESS_NO");
                        if (sStationNameEn.equals("")) {
                            sStationNameEn = sStationNo;
                        }
                        String sNewStationName = sStationName + "[" + sStationNo + "]";
                        Station station = new Station();
                        station.setLINE_NO(sLineNo);
                        station.setSTATION_ID(sStationId);
                        station.setSTATION_NO(sStationNo);
                        station.setSTATION_NAME(sNewStationName);
                        station.setSTATION_NAME_CHT(sStationNameCht);
                        station.setSTATION_NAME_CHS(sStationNameChs);
                        station.setSTATION_NAME_EN(sStationNameEn);
                        station.setPROCESS_NO(sProcessNo);
                        stationList.add(station);
                    }
                } else {
                    ToastUtil.showShortToast(context,sMsg);
                    return;
                }
                curStationList = new ArrayList<>();
                if(stationList!=null){
                    DBUtil dbUtil = new DBUtil(context);
                    dbStationList = dbUtil.getAllStation();
                    if(stationList !=null && stationList.size()>=1){
                        for (int i = 0; i < stationList.size(); i++) {
                            Station station = new Station();
                            station.setLINE_NO(sLineNo);
                            station.setSTATION_ID(stationList.get(i).getSTATION_ID());
                            station.setSTATION_NO(stationList.get(i).getSTATION_NO());
                            station.setSTATION_NAME(stationList.get(i).getSTATION_NAME());
                            station.setSTATION_NAME_CHT(stationList.get(i).getSTATION_NAME_CHT());
                            station.setSTATION_NAME_CHS(stationList.get(i).getSTATION_NAME_CHS());
                            station.setSTATION_NAME_EN(stationList.get(i).getSTATION_NAME_EN());
                            station.setPROCESS_NO(stationList.get(i).getPROCESS_NO());
                            String stationNo = stationList.get(i).getSTATION_NO();
                            String lineNo = stationList.get(i).getLINE_NO();
                            if(dbStationList !=null && dbStationList.size()>=1){
                                for (int j = 0; j < dbStationList.size(); j++) {
                                    if(lineNo.equals(dbStationList.get(j).getLINE_NO())&& stationNo.equals(dbStationList.get(j).getSTATION_NO())){
                                        station.setSELECTED("Y");
                                    }else {
                                        station.setSELECTED("N");
                                    }
                                }
                            }else {
                                station.setSELECTED("N");
                            }

                            curStationList.add(station);
                        }
                    }
                    adapter = new SMTStationAdapter(context,curStationList);
                    lvData.setAdapter(adapter);
                    if(adapter!=null){
                        adapter.setOnItemClickListener(new SMTStationAdapter.OnItemClickListener() {

                            @Override
                            public void select(int id, boolean isChecked) {
                                if(isChecked){
                                    selectStations.add(stationList.get(id));
                                }
                            }
                        });
                    }
                }
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
            if(adapter!=null){
                DBUtil dbUtil = new DBUtil(context);
                if(curStationList!=null && curStationList.size()>=1){
                    for (int i = 0; i < curStationList.size(); i++) {
                        Station station = curStationList.get(i);
                        if(curStationList.get(i).getLINE_NO().equals("Y")){
                            selectStations.add(station);
                        }
                    }
                }
                if(selectStations !=null && selectStations.size()>=1){
                    for (int i = 0; i < selectStations.size(); i++) {
                        Station station = selectStations.get(i);
                        LINE_STATION newStation = new LINE_STATION();
                        newStation.setLINE_NO(sLineNo);
                        newStation.setSTATION_ID(station.getSTATION_ID());
                        newStation.setSTATION_NO(station.getSTATION_NO());
                        newStation.setSTATION_NAME(station.getSTATION_NAME());
                        newStation.setSTATION_NAME_CHS(station.getSTATION_NAME_CHS());
                        newStation.setSTATION_NAME_CHT(station.getSTATION_NAME_CHT());
                        newStation.setSTATION_NAME_EN(station.getSTATION_NAME_EN());
                        newStation.setPROCESS_NO(station.getPROCESS_NO());
                        dbUtil.saveLINE_STATION(newStation);
                    }
                    ToastUtil.showShortToast(context, getString(R.string.line_station_success));
                    finish();
                }else {
                    ToastUtil.showShortToast(context, getString(R.string.please_select_station));
                }
            }
        }
    };

}
