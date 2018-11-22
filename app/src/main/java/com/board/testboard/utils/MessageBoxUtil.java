package com.board.testboard.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * [提示框]
 */

public class MessageBoxUtil { 
    public static AlertDialog.Builder CreateDialog(Context ctx, String title, String message)
    {
        AlertDialog.Builder dlg= new AlertDialog.Builder(ctx);
        return  dlg.setTitle(title).setMessage(message);
    }
    public static AlertDialog.Builder CreateDialog(Context ctx, int style,String title, String message)
    {
        AlertDialog.Builder dlg= new AlertDialog.Builder(ctx,style);
        return  dlg.setTitle(title).setMessage(message);
    }

    public static void Show(Context ctx, String title,String sConfirm, String message)
    {
        CreateDialog(ctx, title, message).setPositiveButton(sConfirm,null).show();
    }

    public static void Show(Context ctx, String title, String message)
    {
        CreateDialog(ctx, title, message).setCancelable(false).setPositiveButton("确定",null).show();
    }

    public static void Show(Context ctx, String title, String message,DialogInterface.OnClickListener okHandler)
    {
        CreateDialog(ctx, title, message).setPositiveButton("确定",okHandler).show();
    }

    public static void Confirm(Context ctx, String title, String message, String sConfirm, String sCancel, DialogInterface.OnClickListener okHandler, DialogInterface.OnClickListener cancelHandler)
    {
        CreateDialog(ctx, title, message).setPositiveButton(sConfirm, okHandler).setNegativeButton(sCancel, cancelHandler).show();
    }
    public static void Confirm(Context ctx,int style, String title, String message, String sConfirm, String sCancel, DialogInterface.OnClickListener okHandler, DialogInterface.OnClickListener cancelHandler)
    {
        CreateDialog(ctx, title, message).setPositiveButton(sConfirm, okHandler).setNegativeButton(sCancel, cancelHandler).show();
    }
}
