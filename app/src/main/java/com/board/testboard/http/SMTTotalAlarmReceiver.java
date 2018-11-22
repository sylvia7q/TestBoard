package com.board.testboard.http;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.board.testboard.utils.DateUtils;
import com.board.testboard.utils.LogUtil;


/**
 * [定时-广播接收]
 * Created by YANGT
 * 2018/3/22.
 */
 
public class SMTTotalAlarmReceiver extends BroadcastReceiver {

    private final String TAG = SMTTotalAlarmReceiver.class.getSimpleName();
    public static final String REFRESH_KAN_BAN_DATA_ACTION = "REFRESH_KAN_BAN_DATA_ACTION";
    private static  OnAlarmMessageListener onAlarmMessageListener;

    //回调接口
    public interface OnAlarmMessageListener {
        void onReceived(String message);
    }

    public void setOnReceivedMessageListener(OnAlarmMessageListener onAlarmMessageListener) {
        this.onAlarmMessageListener = onAlarmMessageListener;
    }
    public SMTTotalAlarmReceiver(){
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(onAlarmMessageListener!=null){
            String str = DateUtils.longToDate(System.currentTimeMillis());
            LogUtil.e(TAG, str);
            onAlarmMessageListener.onReceived(str);
        }

    }

}
