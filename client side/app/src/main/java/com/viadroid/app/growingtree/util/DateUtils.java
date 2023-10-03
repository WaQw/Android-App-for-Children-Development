package com.viadroid.app.growingtree.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static String PATTEN_YMDSF = "yyyy-MM-dd HH:mm";

    private static final String[] CHINESE_ZODIAC =
            {"Monkey", "Rooster", "Dog", "Pig", "Rat", "Ox", "Tiger", "Rabbit", "Dragon", "Snake", "Horse", "Sheep"};
    private static final String[] ZODIAC = {
            "Aquarius", "Pisces", "Aries", "Taurus", "Gemini", "Cancer",
            "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius", "Capricorn"
    };
    private static final int[] ZODIAC_FLAGS = {20, 19, 21, 21, 21, 22, 23, 23, 23, 24, 23, 22};

    /**
     * 计算两个日期之间间隔天数
     */
    public static int daysBetween(Date startTime, Date endTime) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(startTime);
            long time1 = cal.getTimeInMillis();
            cal.setTime(endTime);
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24);
            return Integer.parseInt(String.valueOf(between_days));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getZodiac(final String time, final String patten) {
        return getZodiac(string2Date(time, patten));
    }

    public static String getZodiac(final Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return getZodiac(month, day);
    }

    public static String getZodiac(final int month, final int day) {
        return ZODIAC[day >= ZODIAC_FLAGS[month - 1]
                ? month - 1
                : (month + 10) % 12];
    }


    public static String getChineseZodiac(final String time, final String patten) {
        return getChineseZodiac(string2Date(time, patten));
    }

    public static String getChineseZodiac(final Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return CHINESE_ZODIAC[cal.get(Calendar.YEAR) % 12];
    }

    public static Date string2Date(String time, String pattern) {
        try {
            return getDateParser(pattern).parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static SimpleDateFormat getDateParser(String pattern) {
        return new SimpleDateFormat(pattern);
    }

    /**
     * 获取当前时间字符串,自定义返回类型
     */
    public static String getNowString(String pattern) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
////        return simpleDateFormat.format(getNowMills());
        return getDateParser(pattern).format(getNowMills());
    }

    /**
     * 获取当前时间的时间戳,毫秒
     **/
    public static long getNowMills() {
        return System.currentTimeMillis();
    }

}