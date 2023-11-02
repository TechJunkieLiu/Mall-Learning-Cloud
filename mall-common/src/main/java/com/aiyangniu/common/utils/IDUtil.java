package com.aiyangniu.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * ID生成器工具类
 *
 * @author lzq
 * @date 2023/11/02
 */
public class IDUtil {

    /**
     * UUID（含字母）
     */
    public static String genPrimaryKey(){
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    /**
     * 生成与当前时间有关系的ID号
     */
    public static String genOrdersNum(){
        Date now = new Date();
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        // System.nanoTime() 得到当前时间的纳秒值，JDK5.0 以后
        return df.format(now) + System.currentTimeMillis();
    }
}
