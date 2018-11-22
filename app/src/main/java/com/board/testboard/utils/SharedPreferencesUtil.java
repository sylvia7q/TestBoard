package com.board.testboard.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 存储数据类
 */

public class SharedPreferencesUtil {
    /**
     * 保存在手机里面的文件名
     */   
    private static final String FILE_NAME = "share_data";

    /**
     * 保存String值
     * @param context
     * @param key
     * @param value
     */
    public static void saveValue(Context context, String key, String value){
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, 0);
        sp.edit().putString(key, value).commit();
    }

    /**
     * 保存boolean值
     * @param context
     * @param key
     * @param value
     */
    public static void saveValue(Context context, String key, boolean value){
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, 0);
        sp.edit().putBoolean(key, value).commit();
    }

    /**
     * 保存float值
     * @param context
     * @param key
     * @param value
     */
    public static void saveValue(Context context, String key, float value){
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, 0);
        sp.edit().putFloat(key, value).commit();
    }


    /**
     * 保存int值
     * @param context
     * @param key
     * @param value
     */
    public static void saveValue(Context context, String key, int value){
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, 0);
        sp.edit().putInt(key, value).commit();
    }

    /**
     * 保存Integer值
     * @param context
     * @param key
     * @param value
     */
    public static void saveValue(Context context, String key, Integer value){
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, 0);
        sp.edit().putInt(key, value).commit();
    }
    /**
     * 保存long值
     * @param context
     * @param key
     * @param value
     */
    public static void saveValue(Context context, String key, long value){
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, 0);
        sp.edit().putLong(key, value).commit();
    }


    /**
     * 获取long值
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static long getValue(Context context, String key, long defaultValue){
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, 0);
        return sp.getLong(key, defaultValue);
    }



    /**
     * 获取boolean值
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getValue(Context context, String key, boolean defaultValue){
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, 0);
        return sp.getBoolean(key, defaultValue);
    }


    /**
     * 获取float值
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static float getValue(Context context, String key, float defaultValue){
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, 0);
        return sp.getFloat(key, defaultValue);
    }


    /**
     * 获取int值
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getValue(Context context, String key, int defaultValue){
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, 0);
        return sp.getInt(key, defaultValue);
    }

    /**
     * 获取Integer值
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getValue(Context context, String key, Integer defaultValue){
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, 0);
        return sp.getInt(key, defaultValue);
    }

    /**
     * 获取String值
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getValue(Context context, String key, String defaultValue){
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, 0);
        return sp.getString(key, defaultValue);
    }

    /**
     * 清除指定的值
     * @param context
     * @param key
     * @return
     */
    public static boolean removeValue(Context context , String key){
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.edit().remove(key).commit();
    }


    /**
     * 是否存在key
     * @param context
     * @param key
     * @return
     */
    public static boolean containsKey(Context context, String key){
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, 0);
        return sp.contains(key);
    }


    public static boolean getFirstStart(Context context , String key){
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key,true);
    }
}
