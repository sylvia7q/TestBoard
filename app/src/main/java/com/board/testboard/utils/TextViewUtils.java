package com.board.testboard.utils;

import android.graphics.Color;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.board.testboard.R;


/**
 * textview工具类
 * Created by LongQ on 2018/1/11.
 */

public class TextViewUtils {

    //设置控件可点击并且获取焦点
    public static void setClickableShow(Button button, boolean isClickable){
        button.setEnabled(isClickable);
        if(isClickable){
            button.setBackgroundResource(R.drawable.btn_selector);
        }else{
            button.setBackgroundResource(R.color.divide_line);
        }
    }
    /**
     * 判断是true 或者 false
     * @param params
     * @return
     */
    public static boolean yes(String params){
        if(params.equals("Y")){
            return true;
        }else if(params.equals("N")){
            return false;
        }
        return false;
    }

    /**
     * 转换数量
     * @param params
     * @return
     */
    public static long getNumber(String params){
        long num = 0;
        if(!TextUtils.isEmpty(params)){
            return Long.parseLong(params);
        }else{
            return num;
        }
    }
    public static void setText(TextView textView, int text){
        textView.setText(text);
    }
    public static void setText(TextView textView, String text){
        textView.setText(text);
    }
    public static void setTextColor(TextView textView, int color){
        textView.setTextColor(color);
    }
    public static void setTextColor(TextView textView,int r,int g, int b){
        textView.setTextColor(Color.rgb(r, g, b));
    }
    public static void setBackgroundResource(LinearLayout layout, int color){
        layout.setBackgroundResource(color);
    }
    public static void setBackgroundColor(LinearLayout layout,int r,int g,int b){
        layout.setBackgroundColor(Color.rgb(r, g, b));
    }
}
