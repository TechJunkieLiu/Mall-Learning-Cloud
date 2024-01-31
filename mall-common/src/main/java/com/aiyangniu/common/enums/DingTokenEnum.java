package com.aiyangniu.common.enums;

import lombok.Getter;

/**
 * 钉钉消息群机器人access_token、secret
 *
 * @author lzq
 * @date 2024/01/30
 */
@Getter
public enum DingTokenEnum {

    SEND_SMS_GROUP("b2d4aa9d545d73ef147f0b2530ab347a28a453a7674b4825bb23228aa2d306b1", "SEC92d50d5324ef770282f5601061c6eca57ac478e5f40d200a318183cd208ffb45", "短信发送异常通知"),
    SEND_SMS_BY_MARKET_IMPORT_TOKEN("b2d4aa9d545d73ef147f0b2530ab347a28a453a7674b4825bb23228aa2d306b1", "SEC92d50d5324ef770282f5601061c6eca57ac478e5f40d200a318183cd208ffb45", "导入通知消息"),
    SEND_SMS_BY_PRE_CASE_WARN_TOKEN("b2d4aa9d545d73ef147f0b2530ab347a28a453a7674b4825bb23228aa2d306b1","SEC92d50d5324ef770282f5601061c6eca57ac478e5f40d200a318183cd208ffb45",  "预警消息通知"),
    SEND_SMS_BY_MARKDOWN_TOKEN("b2d4aa9d545d73ef147f0b2530ab347a28a453a7674b4825bb23228aa2d306b1", "SEC92d50d5324ef770282f5601061c6eca57ac478e5f40d200a318183cd208ffb45", "系统消息通知，markdown类型"),
    SEND_SMS_BY_DEVELOPER_TOKEN("b2d4aa9d545d73ef147f0b2530ab347a28a453a7674b4825bb23228aa2d306b1", "SEC92d50d5324ef770282f5601061c6eca57ac478e5f40d200a318183cd208ffb45", "系统消息通知，技术专用");

    /** 机器人Webhook地址中的access_token **/
    private String token;
    /** 密钥，机器人安全设置页面，加签一栏下面显示的SEC开头的字符串 **/
    private String secret;

    private String name;

    DingTokenEnum(String token, String secret, String name) {
        this.token = token;
        this.secret = secret;
        this.name = name;
    }
}
