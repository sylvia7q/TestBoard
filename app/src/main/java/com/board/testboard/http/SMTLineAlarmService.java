package com.board.testboard.http;

import android.app.AlarmManager;
import android.app.IntentService;
import android.content.Intent;

import com.board.testboard.utils.AlarmManagerUtil;
import com.board.testboard.utils.DateUtils;
import com.board.testboard.utils.LogUtil;


/**
 * [创建一个服务AlarmService，为了让不同的定时任务运行在不同的线程中，让AlarmService继承支持线程的IntentService类]]
 * Created by YANGT on 2017/11/24.
 */ 

public class SMTLineAlarmService extends IntentService {
    // 从其他地方通过Intent传递过来的提醒时间
    private String alarmDateTime;
    private String TAG = SMTLineAlarmReceiver.class.getSimpleName();

    //必须实现父类的构造方法
    public SMTLineAlarmService() {
        super("AlarmService");
    }

    //必须重写的核心方法
    @Override
    protected void onHandleIntent(Intent intent) {
        alarmDateTime = intent.getStringExtra("alarm_time");
        // taskId用于区分不同的任务
        int taskId = intent.getIntExtra("task_id", 0);
        long nRefreshTimeSet = intent.getLongExtra("nRefreshTimeSet", 0l);
        long alarmDateTimeMillis = DateUtils.stringToMillis(alarmDateTime);
        LogUtil.e(TAG,"alarmDateTime =  " + alarmDateTime + ", taskId = " + taskId + ", nRefreshTimeSet = " + nRefreshTimeSet + ", alarmDateTimeMillis = " + alarmDateTimeMillis);
        //10s后发送广播，周期性执行定时任务(10s重复周期)
        AlarmManagerUtil.sendRepeatAlarmBroadcast(this, taskId, AlarmManager.RTC_WAKEUP, alarmDateTimeMillis, nRefreshTimeSet* 1000, SMTLineAlarmReceiver.class);

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
