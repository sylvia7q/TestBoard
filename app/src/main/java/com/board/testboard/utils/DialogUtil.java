package com.board.testboard.utils;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.board.testboard.R;


/**
 * [弹出框]
 */

public class DialogUtil {

    public static Dialog showStatusDialog(Context ctx, String title, String message){
        final Dialog dialog = new Dialog(ctx, R.style.myDialog);
        dialog.setCancelable(false);
        LinearLayout layout = (LinearLayout) (LayoutInflater.from(ctx).inflate(R.layout.dialog_reminder, null));
        TextView titleTextView = layout.findViewById(R.id.dialog_reminder_title);//标题
        TextView message1 = layout.findViewById(R.id.dialog_reminder_message);//消息
        Button confirm = layout.findViewById(R.id.dialog_reminder_btn_confirm);
        Button cancel = layout.findViewById(R.id.dialog_reminder_btn_cancel);
        cancel.setVisibility(View.GONE);
        dialog.setContentView(layout);
        titleTextView.setText(title);//标题
        message1.setText(message);//消息
        if(title.equalsIgnoreCase("OK")){
            titleTextView.setBackgroundResource(R.color.blue);
            titleTextView.setTextColor(ctx.getResources().getColor(R.color.white));
            message1.setTextColor(ctx.getResources().getColor(R.color.black));
            confirm.setBackgroundResource(R.drawable.btn_selector);
            cancel.setBackgroundResource(R.drawable.btn_selector);
            confirm.setTextColor(ctx.getResources().getColor(R.color.text_color_dialog_prompt_select));
            cancel.setTextColor(ctx.getResources().getColor(R.color.text_color_dialog_prompt_select));
        }else if(title.equalsIgnoreCase("NG")||title.equalsIgnoreCase("ERROR")){
            titleTextView.setBackgroundResource(R.color.red);
            titleTextView.setTextColor(ctx.getResources().getColor(R.color.white));
            message1.setTextColor(ctx.getResources().getColor(R.color.black));
            confirm.setBackgroundResource(R.drawable.btn_selector_error);
            cancel.setBackgroundResource(R.drawable.btn_selector_error);
            confirm.setTextColor(ctx.getResources().getColor(R.color.text_color_dialog_error_select));
            cancel.setTextColor(ctx.getResources().getColor(R.color.text_color_dialog_error_select));
        }else {
            titleTextView.setBackgroundResource(R.color.btn_warn);
            titleTextView.setTextColor(ctx.getResources().getColor(R.color.white));
            message1.setTextColor(ctx.getResources().getColor(R.color.black));
            confirm.setBackgroundResource(R.drawable.btn_selector_warn);
            cancel.setBackgroundResource(R.drawable.btn_selector_warn);
            confirm.setTextColor(ctx.getResources().getColor(R.color.text_color_dialog_warn_select));
            cancel.setTextColor(ctx.getResources().getColor(R.color.text_color_dialog_warn_select));
        }
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        setDialogWidthHeight(ctx,dialog,0.9f);
        return dialog;
    }
    /**
     * 设置对话框的宽和高
     */
    public static void setDialogWidthHeight(Context context,Dialog sDialog, float f,float h) {
        WindowManager.LayoutParams lp = sDialog.getWindow().getAttributes(); //获取对话框当前的参数值
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * f); // 宽度设置为屏幕的0.6
        lp.height = (int) (d.heightPixels * h);
        sDialog.getWindow().setAttributes(lp); //设置生效
    }
    /**
     * 设置对话框的宽和高
     */
    public static void setDialogWidthHeight(Context context,Dialog sDialog, float f) {
        WindowManager.LayoutParams lp = sDialog.getWindow().getAttributes(); //获取对话框当前的参数值
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * f); // 宽度设置为屏幕的0.6
        lp.height =  WindowManager.LayoutParams.WRAP_CONTENT;
        sDialog.getWindow().setAttributes(lp); //设置生效
    }

    public static Dialog showDialog(Context ctx, String title, String message,View.OnClickListener confirmListener,View.OnClickListener cancelListener){
        final Dialog dialog = new Dialog(ctx,R.style.myDialog);
        dialog.setCancelable(false);
        LinearLayout layout = (LinearLayout) (LayoutInflater.from(ctx).inflate(R.layout.dialog_reminder, null));
        TextView titleTextView = layout.findViewById(R.id.dialog_reminder_title);//标题
        TextView message1 = layout.findViewById(R.id.dialog_reminder_message);//消息
        Button confirm = layout.findViewById(R.id.dialog_reminder_btn_confirm);
        Button cancel = layout.findViewById(R.id.dialog_reminder_btn_cancel);
        dialog.setContentView(layout);
        titleTextView.setText(title);//标题
        message1.setText(message);//消息
        titleTextView.setBackgroundResource(R.color.blue);
        titleTextView.setTextColor(ctx.getResources().getColor(R.color.white));
        message1.setTextColor(ctx.getResources().getColor(R.color.black));
        confirm.setTextColor(ctx.getResources().getColor(R.color.text_color_dialog_prompt_select));
        cancel.setTextColor(ctx.getResources().getColor(R.color.text_color_dialog_prompt_select));
        confirm.setBackgroundResource(R.drawable.btn_selector);
        cancel.setBackgroundResource(R.drawable.btn_selector);
        dialog.show();
        confirm.setOnClickListener(confirmListener);
        cancel.setOnClickListener(cancelListener);
        setDialogWidthHeight(ctx,dialog,0.9f);
        return dialog;
    }
    public static Dialog showDialog(Context ctx, String title, String message,boolean confirmVisibility,boolean cancelVisibility){
        final Dialog dialog = new Dialog(ctx,R.style.myDialog);
        dialog.setCancelable(false);
        LinearLayout layout = (LinearLayout) (LayoutInflater.from(ctx).inflate(R.layout.dialog_reminder, null));
        TextView titleTextView = layout.findViewById(R.id.dialog_reminder_title);//标题
        TextView message1 = layout.findViewById(R.id.dialog_reminder_message);//消息
        Button confirm = layout.findViewById(R.id.dialog_reminder_btn_confirm);
        Button cancel = layout.findViewById(R.id.dialog_reminder_btn_cancel);
        dialog.setContentView(layout);
        titleTextView.setText(title);//标题
        message1.setText(message);//消息
        titleTextView.setBackgroundResource(R.color.blue);
        titleTextView.setTextColor(ctx.getResources().getColor(R.color.white));
        message1.setTextColor(ctx.getResources().getColor(R.color.black));
        confirm.setTextColor(ctx.getResources().getColor(R.color.text_color_dialog_prompt_select));
        cancel.setTextColor(ctx.getResources().getColor(R.color.text_color_dialog_prompt_select));
        confirm.setBackgroundResource(R.drawable.btn_selector);
        cancel.setBackgroundResource(R.drawable.btn_selector);
        dialog.show();
        if(!confirmVisibility){
            confirm.setVisibility(View.GONE);
        }
        if(!cancelVisibility){
            cancel.setVisibility(View.GONE);
        }
        setDialogWidthHeight(ctx,dialog,0.9f);
        return dialog;
    }

    public static Dialog showDialog(Context ctx, String title, String message, String btnConfirmTitle, View.OnClickListener onConfirmClickListener, String btnCancelTitle, View.OnClickListener onCancelClickListener){
        final Dialog dialog = new Dialog(ctx,R.style.myDialog);
        dialog.setCancelable(false);
        LinearLayout layout = (LinearLayout) (LayoutInflater.from(ctx).inflate(R.layout.dialog_reminder, null));
        TextView tvTitle = layout.findViewById(R.id.dialog_reminder_title);//标题
        TextView tvMessage = layout.findViewById(R.id.dialog_reminder_message);//消息

        Button confirm = layout.findViewById(R.id.dialog_reminder_btn_confirm);
        Button cancel = layout.findViewById(R.id.dialog_reminder_btn_cancel);
        dialog.setContentView(layout);
        tvTitle.setText(title);//标题
        tvMessage.setText(message);//消息
        tvTitle.setBackgroundResource(R.color.blue);
        tvTitle.setTextColor(ctx.getResources().getColor(R.color.white));
        tvMessage.setTextColor(ctx.getResources().getColor(R.color.black));
        tvMessage.setTextSize(18.0f);
        confirm.setTextColor(ctx.getResources().getColor(R.color.text_color_dialog_prompt_select));
        cancel.setTextColor(ctx.getResources().getColor(R.color.text_color_dialog_prompt_select));
        confirm.setBackgroundResource(R.drawable.btn_selector);
        cancel.setBackgroundResource(R.drawable.btn_selector);
        dialog.show();
        if(TextUtils.isEmpty(btnConfirmTitle)){
            confirm.setVisibility(View.GONE);
        }else {
            confirm.setText(btnConfirmTitle);
            confirm.setTextSize(18.0f);
            confirm.setOnClickListener(onConfirmClickListener);
        }
        if(TextUtils.isEmpty(btnCancelTitle)){
            cancel.setVisibility(View.GONE);
        }else {
            cancel.setText(btnCancelTitle);
            cancel.setTextSize(18.0f);
            cancel.setOnClickListener(onCancelClickListener);
        }
        setDialogWidthHeight(ctx,dialog,0.9f);
        return dialog;
    }
}
