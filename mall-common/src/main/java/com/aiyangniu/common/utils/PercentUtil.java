package com.aiyangniu.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 百分比工具类
 *
 * @author lzq
 * @date 2023/11/02
 */
public class PercentUtil {

    /**
     * 计算百分率
     */
    public static double getPercent(Object one, Object total){
        if (total != null){
            if (total instanceof BigDecimal){
                BigDecimal oneDecimal = (BigDecimal) one;
                BigDecimal totalDecimal = (BigDecimal) total;
                if (totalDecimal.compareTo(BigDecimal.ZERO) != 0){
                    BigDecimal bigDecimal = oneDecimal.divide(totalDecimal, 4, BigDecimal.ROUND_HALF_UP);
                    return bigDecimal.doubleValue()*100;
                }
                return 0;
            }
            if (total instanceof Integer){
                Integer oneInteger =  (Integer) one;
                Integer totalInteger = (Integer) total;
                if (totalInteger != 0){
                    BigDecimal bigDecimal = BigDecimal.valueOf(oneInteger.doubleValue() / totalInteger.doubleValue()).setScale(4, RoundingMode.HALF_UP);
                    return bigDecimal.doubleValue()*100;
                }
                return 0;
            }
        }
        return 0;
    }
}
