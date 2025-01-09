package com.aiyangniu.common.utils;

import cn.hutool.core.util.StrUtil;
import java.util.regex.Pattern;

/**
 * 证件号码工具类
 *
 * @author lzq
 * @date 2023/09/25
 */
public class NumberUtil {

    /**
     * 身份证号码长度（一代）
     */
    private final static Integer ID_CARD_FIRST = 15;

    /**
     * 身份证号码长度（二代）
     */
    private final static Integer ID_CARD_SECOND = 18;

    /**
     * 手机号码正则（最新运营商）
     */
    private final static String MOBILE_PHONE = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$";

    /**
     * 身份证号码正则
     */
    private final static String ID_CARD = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";

    /**
     * 验证是否为手机号码
     */
    public static boolean isMobilePhone(CharSequence value) {
        return Pattern.matches(MOBILE_PHONE, value);
    }

    /**
     * 验证是否为身份证号码
     */
    public static boolean isIdCard(CharSequence value) {
        return Pattern.matches(ID_CARD, value);
    }

    /**
     * 银行卡号脱敏
     */
    public static String getHideCardNo(String cardNo){
        if (StrUtil.isNotBlank(cardNo)) {
            StringBuffer cardBuffer = new StringBuffer();
            char[] card = cardNo.toCharArray();
            if (card.length > 8 && card.length <= 12) {
                card[card.length - 5] = '*';
                card[card.length - 6] = '*';
                card[card.length - 7] = '*';
                card[card.length - 8] = '*';
            }
            if (card.length > 12) {
                for (int i = 5; i <= 12; i++) {
                    card[card.length - i] = '*';
                }
            }
            int head = card.length%4 + 4;
            for (int i = 0; i < head; i++) {
                cardBuffer.append(card[i]);
            }
            cardBuffer.append("");
            for (int i = 0; (i + head)< card.length; i++) {
                cardBuffer.append(card[i + head]);
                if ((i+1)%4 == 0) {
                    cardBuffer.append("");
                }
            }
            return cardBuffer.toString().trim();
        }
        return cardNo;
    }

    /**
     * 名字脱敏
     * 规则，张三丰，脱敏为：**丰
     */
    public static String nameDesensitization(String name){
        if(name == null || name.isEmpty()){
            return "";
        }
        String myName = null;
        char[] chars = name.toCharArray();
        if(chars.length == 1){
            myName = name;
        }
        if(chars.length == 2){
            myName = name.replace(name.substring(0,1), "*");
        }
        if(chars.length > 2){
            myName = name.replaceAll(name.substring(0, chars.length-1), "*");
        }
        return myName;
    }

    /**
     * 对手机号进行脱敏
     */
    public static String getHidePhone(String phone) {
        if (StrUtil.isNotBlank(phone)) {
            phone = phone.replaceAll("(^\\d{3})\\d.*(\\d{4})", "$1****$2");
        }
        return phone;
    }

    /**
     * 对身份证号进行脱敏
     */
    public static String desensitizedIdNumber(String idCard){
        if (StrUtil.isNotBlank(idCard)) {
            if (idCard.length() == ID_CARD_FIRST){
                idCard = idCard.replaceAll("(\\w{3})\\w*(\\w{4})", "$1********$2");
            }
            if (idCard.length() == ID_CARD_SECOND){
                idCard = idCard.replaceAll("(\\w{3})\\w*(\\w{4})", "$1***********$2");
            }
        }
        return idCard;
    }
}
