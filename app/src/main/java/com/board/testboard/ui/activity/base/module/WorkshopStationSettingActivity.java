package com.board.testboard.ui.activity.base.module;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.topcee.kanban.R;
import com.topcee.kanban.adapter.SMTStationAdapter;
import com.topcee.kanban.app.MyApplication;
import com.topcee.kanban.bean.Station;
import com.topcee.kanban.http.GetData;
import com.topcee.kanban.ui.activity.base.TitlebarBaseActivity;
import com.topcee.kanban.utils.DialogUtil;
import com.topcee.kanban.utils.SharedPreferencesUtil;
import com.topcee.kanban.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * [线别工站设置]
 */

public class WorkshopStationSettingActivity extends TitlebarBaseActivity {

    @BindView(R.id.view_base_lv_data)
    ListView lvData;
    private Context context;
    private Dialog dialog = null;
    private String sStationNo = "";  // 工站
    private List<Station> stationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = WorkshopStationSettingActivity.this;
        //获取工站
        GetStationInfo();
    }


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_base_setting;
    }

    @Override
    protected void initTitleView() {
        super.initTitleView();
        mTitle.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.VISIBLE);
        mTitle.setTitleText(getString(R.string.smt_station_set));
        mTitle.setBtnRight(0, R.string.save);
        mTitle.setBtnRightOnclickListener(saveOnClickListener);
        mTitle.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkshopStationSettingActivity.this.finish();
            }
        });
    }

    //获取工站
    private void GetStationInfo() {
        //判断线别是否有选择
        TimsGetBasStationInfoAsyncTask getStationInfoAsyncTask = new TimsGetBasStationInfoAsyncTask();
        getStationInfoAsyncTask.execute();
    }

    //获取工站-数据处理
    public class TimsGetBasStationInfoAsyncTask extends AsyncTask<String, Void, String> {
        String sLineNo = "";

        @Override
        protected void onPreExecute() {
            if (dialog != null) {
                dialog.dismiss();
            }
            dialog = DialogUtil.showDialog(context, getString(R.string.get_station_info), getString(R.string.loading_data),false,false);
        }

        @Override
        protected String doInBackground(String... params) {
            String methodname = "TimsGetBasStationInfo";
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("sMesUser", MyApplication.sMesUser);
                jsonObject.put("sFactoryNo", MyApplication.sFactoryNo);
                jsonObject.put("sLanguage", MyApplication.sLanguage);
                jsonObject.put("sUserNo", MyApplication.sUserNo);
                jsonObject.put("sClientIp", MyApplication.sClientIp);
                jsonObject.put("sLineNo", "");
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return GetData.getDataByJson(jsonObject.toString(), methodname);
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
                if (sStatus.equalsIgnoreCase("OK")) {
                    String sData = item.getString("data");
                    JSONArray jsonArrayData = new JSONArray(sData);
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
                        station.setLINE_NO("");
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
                SMTStationAdapter adapter = new SMTStationAdapter(context, R.layout.item_list_workshop_line_multiple_choice, stationList);
                lvData.setAdapter(adapter);
                lvData.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                //获取本地文件数据
                String sMoreStationNo = SharedPreferencesUtil.getValue(getBaseContext(), "WorkShopBoardStationNo", "");
                String sMoreStationNoInTemp[] = sMoreStationNo.split(",");
                for (int i = 0; i < lvData.getCount(); i++) {
                    String sStationNoTemp = stationList.get(i).getSTATION_NO();
                    final int nCheck = i;
                    for (int j = 0; j < sMoreStationNoInTemp.length; j++) {
                        //获取的线别代码，与保存的数组比对。若OK，则站位显示勾先把状态
                        if (sStationNoTemp.equalsIgnoreCase(sMoreStationNoInTemp[j])) {
                            lvData.setItemChecked(nCheck, true);
                        }
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
            long[] checkedItemIds = lvData.getCheckItemIds();
            if(checkedItemIds.length > 0){
                for (int i = 0; i < checkedItemIds.length; i++) {
                    if (i == 0) {
                        sStationNo = stationList.get((int) checkedItemIds[i]).getSTATION_NO();
                    } else {
                        sStationNo = sStationNo + "," + stationList.get((int) checkedItemIds[i]).getSTATION_NO();
                    }
                }
                if(!TextUtils.isEmpty(sStationNo)){
                    //保存数据至本地文件中
                    SharedPreferencesUtil.saveValue(getBaseContext(), "WorkShopBoardStationNo", sStationNo);
                    ToastUtil.showShortToast(context, String.format(getString(R.string.line_station_success),sStationNo));
                    finish();
                }else {
                    ToastUtil.showShortToast(context, String.format(getString(R.string.line_station_fail),sStationNo));
                }
            }else {
                ToastUtil.showShortToast(context, getString(R.string.please_select_station));
            }
        }
    };

}
