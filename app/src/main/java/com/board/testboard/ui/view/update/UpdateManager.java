package com.board.testboard.ui.view.update;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog; 
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.board.testboard.R;
import com.board.testboard.app.MyApplication;
import com.board.testboard.http.GetData;
import com.board.testboard.utils.AppUtil;
import com.board.testboard.utils.DialogUtil;
import com.board.testboard.utils.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import butterknife.internal.Utils;


/**
 * 版本更新
 */
public class UpdateManager {

    private final String TAG = UpdateManager.class.getSimpleName();
    private Context mContext;
    //提示语
    private Dialog noticeDialog;

    private Dialog downloadDialog;
    /* 下载包安装路径 */
    private static final String savePath = android.os.Environment.getExternalStorageDirectory().getPath() + File.separator;
    private static final String saveFileName = savePath + "TIMS_KANBAN.apk";

    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;

    private static final int DOWN_UPDATE = 1;

    private static final int DOWN_OVER = 2;

    private int progress;

    private Thread downLoadThread;

    private boolean interceptFlag = false;
    private Dialog dialog = null;

    public UpdateManager(Context context) {
        this.mContext = context;
    }

    //外部接口让主Activity调用
    public void checkUpdateInfo(Context contenx){
        isUpdate(contenx);
    }

    /**
     * 检查软件是否有更新版本
     *
     * @return
     */
    private void isUpdate(final Context context)
    {
        new Thread(new Runnable() {

            @Override
            public void run() {

                JSONObject jsonObj = new JSONObject();
                try {
                    jsonObj.put("sMesUser", MyApplication.sMesUser);
                    jsonObj.put("sFactoryNo", MyApplication.sFactoryNo);
                    jsonObj.put("sLanguage", MyApplication.sLanguage);
                    jsonObj.put("sUserNo", MyApplication.sUserNo);
                    jsonObj.put("sClientIp", MyApplication.sClientIp);
                    jsonObj.put("sAppType", "TIMS_KANBAN");
                    jsonObj.put("sClientVersionCode", AppUtil.getVersionCode(mContext) + "");
                }
                catch (JSONException ex) {
                    ex.printStackTrace();
                }
                String show_result = GetData.getDataByJson(context,jsonObj.toString(), "CheckVersion");

                //需要数据传递，用下面方法；
                Message msg =new Message();
                msg.obj = show_result;//可以是基本类型，可以是对象，可以是List、map等；
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:   //返回版本信息
                    String show_result = (String)msg.obj;
                    try {
                        JSONArray jsonArray = new JSONArray(show_result);
                        JSONObject item = jsonArray.getJSONObject(0);
                        String sStatus = item.getString("status");
                        String sIsUpdate= item.getString("IsUpdate");
                        if(sStatus.equals("OK")) {
                            if (sIsUpdate.equals("Y")) {
                                showNoticeDialog(); //需要更新
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogUtil.e(TAG, "get version " + e.getMessage());
                    }
                    break;
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    break;
                case DOWN_OVER:
                    installApk();
                    break;
                default:
                    break;
            }
        }

    };


    /**
     * 显示更新的消息
     */
    private void showNoticeDialog(){
        String updateMsg = mContext.getString(R.string.have_the_new_version_on_the_server_please_update);

        dialog = DialogUtil.showDialog(mContext,
                mContext.getString(R.string.software_version_update),
                updateMsg,
                mContext.getString(R.string.now_update),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        showDownloadDialog();
                    }
                },
                mContext.getString(R.string.later_update),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
    }

    /**
     * 显示下载apk的进度
     */
    private void showDownloadDialog(){
        downloadDialog = new Dialog(mContext,R.style.myDialog);
        downloadDialog.setCancelable(false);
        LinearLayout layout = (LinearLayout) (LayoutInflater.from(mContext).inflate(R.layout.activity_update_manager, null));
        TextView tvTitle = layout.findViewById(R.id.update_title);//标题
        mProgress = layout.findViewById(R.id.update_progress);//消息
        Button cancel = layout.findViewById(R.id.update_btn_cancel);
        downloadDialog.setContentView(layout);
        tvTitle.setText(mContext.getString(R.string.software_version_update));
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadDialog.dismiss();
                interceptFlag = true;
            }
        });

        downloadDialog.show();
        DialogUtil.setDialogWidthHeight(mContext,downloadDialog,0.9f);
        downloadApk(); //下载_apk安装包
    }

    //下载_apk安装包
    private void downloadApk(){
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }
    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(MyApplication.sDownloadUrl);

                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                File file = new File(savePath);
                LogUtil.e(TAG,"file = " + file.getAbsolutePath());
                if(!file.exists()){
                    file.mkdir();
                }
                String apkFile = saveFileName;
                LogUtil.e(TAG,"apkFile = " + apkFile);
                File ApkFile = new File(apkFile);
                FileOutputStream fos = new FileOutputStream(ApkFile);

                int count = 0;
                byte buf[] = new byte[1024];

                do{
                    int numread = is.read(buf);
                    count += numread;
                    progress =(int)(((float)count / length) * 100);
                    //更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if(numread <= 0){
                        //下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf,0,numread);
                }while(!interceptFlag);//点击取消就停止下载.

                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch(IOException e){
                e.printStackTrace();
            }

        }
    };

    //安装_apk
    private void installApk(){

        File apkFile = new File(saveFileName);
        if (!apkFile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LogUtil.e(TAG, "版本大于 N ，开始使用 fileProvider 进行安装");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(
                    mContext
                    , "com.topcee.doov.kanban.FileProvider"
                    , apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            LogUtil.e(TAG, "正常进行安装");
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        mContext.startActivity(intent);
    }

    private void notNewVersionShow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getString(R.string.software_is_the_latest_version));
        int verCode = AppUtil.getVersionCode(mContext);
        String verName =AppUtil.getVersionName(mContext);
        StringBuffer sb = new StringBuffer();
        sb.append(mContext.getString(R.string.the_current_version)+":");
        sb.append(verName);

        sb.append(";\n"+mContext.getString(R.string.is_the_latest_version_no_need_to_update)+"!");
        builder.setMessage(sb.toString()); ///设置内容
        builder.setPositiveButton(mContext.getString(R.string.btn_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        noticeDialog = builder.create();
        noticeDialog.show();
    }

}
