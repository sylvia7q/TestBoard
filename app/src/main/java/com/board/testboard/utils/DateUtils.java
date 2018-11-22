package com.board.testboard.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期工具类
 */ 
public class DateUtils {
    private static final String TAG = DateUtils.class.getSimpleName();
    // 获取当前时间n[]之后的时间的日期时间字符串（N的单位为Calendar的那些表示时间的常量）
    public static String getNLaterDateTimeString(int nType, int n) {
        Date date = new Date();
        Calendar c = new GregorianCalendar();
        c.setTime(date);
        c.add(nType, n);

        return CalendarToString(c);
    }
    // Calendar转换成String
    public static String CalendarToString(Calendar calendar) {
        Date date = ((GregorianCalendar) calendar).getTime();
        return DateToString(date);
    }
    //获取日期
    public static String getDate() {
        //日期
        SimpleDateFormat simpleDat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDat.format(new Date(new Date().getYear(),new Date().getMonth(),new Date().getDate()-1));
        return date;
    }
    //获取日期
    public static String getTime() {
        //时间
        SimpleDateFormat simpleTime = new SimpleDateFormat("HH:mm");
        String time = simpleTime.format(new Date());
        return time;
    }
    public static String getDateTime(){
        return getDate() + " " + getTime();
    }
    // 获取当前时间的日期时间字符串，格式："yyyy-MM-dd HH:mm:ss"
    public static String getCurrentDateTimeString() {
        Date date = new Date();
        return DateToString(date);
    }

    // Date对象转换成日期时间字符串
    public static String DateToString(Date date) {
        String dateTimeStr = null;
        DateFormat fmt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        dateTimeStr = fmt.format(date);
        return dateTimeStr;
    }

    // 把日期时间字符串的时间转换成毫秒值（RTC）
    public static long stringToMillis(String dateTime) {
        Calendar c = StringToGregorianCalendar(dateTime);

        return c.getTimeInMillis();
    }
    // 字符串转换成Calendar
    public static Calendar StringToGregorianCalendar(String dateTimeStr) {
        Date date = StringToDate(dateTimeStr);
        Calendar calendar = new GregorianCalendar();

        calendar.setTime(date);
        return calendar;
    }

    // 注：dateTimeStr带不带前导0都是可以的，比如"2011-01-01 01:02:03"和"2011-1-1 1:2:3"都是合法的
    public static Date StringToDate(String dateTimeStr) {
        Date date = new Date();
        DateFormat fmt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            date = fmt.parse(dateTimeStr);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 将日期转换为时间戳
     * @param dateTimeStr
     * @return
     */
    public static long dateToLong(String dateTimeStr) {
        long time = 0l;
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            time = dateformat.parse(dateTimeStr.trim()).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }
    /**
     * 将时间戳转换为日期
     * @param time
     * @return
     */
    public static String longToDate(long time) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String dateStr = dateformat.format(time);
        return dateStr;
    }
}
