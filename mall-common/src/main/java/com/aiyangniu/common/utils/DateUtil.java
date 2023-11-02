package com.aiyangniu.common.utils;

import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间日期处理工具类
 *
 * @author lzq
 * @date 2023/09/25
 */
public class DateUtil {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_SHORT_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter DATE_TIME_LONG_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmSSS");
    private static final DateTimeFormatter DATE_SHORT_FORMAT_C = DateTimeFormatter.ofPattern("yyyy年MM月dd日");

    private static final String DATE_FORMAT_STR = "yyyy-MM-dd";
    private static final String TIME_FORMAT_STR = "HH:mm:ss";
    private static final String DATE_TIME_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_TIME_LONG_FORMAT_STR = "yyyyMMddHHmmSSS";

    /**
     * 从Date类型的时间中提取日期部分
     */
    public static Date getDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 从Date类型的时间中提取时间部分
     */
    public static Date getTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, 1970);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 获取含有时间的ID字符串
     */
    public static String getIdByDateTime(){
        Date now = new Date();
        SimpleDateFormat simple = new SimpleDateFormat(DATE_TIME_LONG_FORMAT_STR);
        return simple.format(now);
    }

    /**
     * 将 LocalDateTime 对象转换为 Date 对象
     */
    public static Date getDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        ZoneId zoneId = ZoneId.systemDefault();
        return Date.from(dateTime.atZone(zoneId).toInstant());
    }

    /**
     * 如果参数长度不为0，则取第一个参数进行格式化，否则取当前日期时间，精确到秒（参数长度可为0）
     */
    public static String toFull(Date... date){
        SimpleDateFormat simple = new SimpleDateFormat(DATE_TIME_FORMAT_STR);
        if (date != null && date.length > 0){
            return simple.format(date);
        }
        return simple.format(new Date());
    }

    /**
     * 根据 字符串格式 将 String类型 数据 转换为 对应的 时间格式
     */
    public static Date str2Date(String date){
        SimpleDateFormat simple = null;
        switch (date.trim().length()){
            // 日期+时间
            case 19:
                simple = new SimpleDateFormat(DATE_TIME_FORMAT_STR);
                break;
            // 日期
            case 10:
                simple = new SimpleDateFormat(DATE_FORMAT_STR);
                break;
            // 时间
            case 8:
                simple = new SimpleDateFormat(TIME_FORMAT_STR);
                break;
            default:
                break;
        }
        try {
            assert simple != null;
            return simple.parse(date.trim());
        }catch (ParseException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据 时间格式 将 Date类型 数据 转换为 对应的 字符串格式
     */
    public static String date2Str(Date date, DateTimeFormatter formatter) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = date.toInstant().atZone(zoneId);
        return zonedDateTime.toLocalDateTime().format(formatter);
    }

    /**
     * 将带有时、分、秒格式的日期转化为yyyy-MM-dd 00:00:00（参数长度可为0）
     * 即获取参数日期的0点0分0秒
     */
    public static Date getStartDateTime1(Date... date){
        SimpleDateFormat simple = new SimpleDateFormat(DATE_FORMAT_STR);
        Date dateOr = date.length == 0 ? new Date() : date[0];
        String format = simple.format(dateOr);
        try {
            return simple.parse(format);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将带有时、分、秒格式的日期转化为yyyy-MM-dd 00:00:00（参数长度可为0）
     * 即获取参数日期的0点0分0秒
     */
    public static Date getStartDateTime2(Date date) {
        if (date == null) {
            return null;
        }
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = date.toInstant().atZone(zoneId).withHour(0).withMinute(0).withSecond(0);
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * 将带有时、分、秒格式的日期转化为yyyy-MM-dd 23:59:59（参数长度可为0）
     * 即获取参数日期的23点59分59秒
     */
    public static Date getEndDateTime(Date dateTime) {
        if (dateTime == null) {
            return null;
        }
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = dateTime.toInstant().atZone(zoneId).withHour(23).withMinute(59).withSecond(59);
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * 推算一个月内向前或向后偏移多少天（也可以推算年）
     *
     * @param date 被推算的日期
     * @param amount 偏移量
     * @param isStart 是否严格按整天计算（yyyy-MM-dd 00:00:00）
     */
    public static Date addDayOfMonth(Date date, Integer amount, Boolean isStart){
        date = date == null ? new Date() : date;
        if (isStart){
            date = getStartDateTime1(date);
        }
        Calendar calendar = Calendar.getInstance();
        assert date != null;
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, amount);
        return calendar.getTime();
    }

    /**
     * 推算一个小时内向前或向后偏移多少分钟（秒、毫秒不可以）
     *
     * @param date 被推算的日期
     * @param amount 偏移量
     * @param isStart 是否严格按整分钟计算（yyyy-MM-dd HH:mm:00）
     */
    public static Date addMinuteOfHour(Date date, Integer amount, Boolean isStart) {
        date = date == null ? new Date() : date;
        if (isStart) {
            SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
            try {
                date = simple.parse(simple.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, amount);
        return cal.getTime();
    }

    /**
     * 秒数转化时分秒
     */
    public static String secondFormat(Integer second) {
        return second/3600 + "时" + second % 3600 / 60 + "分" + second % 60 + "秒";
    }

    /**
     * 日期格式转换时间戳
     */
    public static long timeMillisOf(String time, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date != null ? date.getTime() : System.currentTimeMillis();
    }

    /**
     * 时间戳转换日期格式
     */
    public static Date dateOf(long timeMillis){
        return new Date(timeMillis);
    }
}
