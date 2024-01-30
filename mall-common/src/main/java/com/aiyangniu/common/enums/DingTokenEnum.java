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

    SEND_SMS_GROUP("edc0f54f60bc6c17aeca637d2570fc58401048f8ec96fc146bbab785977a2b76", "SECe76cd35ee80fe49c12d0fd9d979891b72ad8d8bdedbd1c6048edb78125bfb545", "短信发送异常通知"),
    SEND_SMS_BY_MARKET_IMPORT_TOKEN("edc0f54f60bc6c17aeca637d2570fc58401048f8ec96fc146bbab785977a2b76", "SECe76cd35ee80fe49c12d0fd9d979891b72ad8d8bdedbd1c6048edb78125bfb545", "导入通知消息"),
    SEND_SMS_BY_PRE_CASE_WARN_TOKEN("edc0f54f60bc6c17aeca637d2570fc58401048f8ec96fc146bbab785977a2b76","SECe76cd35ee80fe49c12d0fd9d979891b72ad8d8bdedbd1c6048edb78125bfb545",  "预警消息通知"),
    SEND_SMS_BY_MARKDOWN_TOKEN("edc0f54f60bc6c17aeca637d2570fc58401048f8ec96fc146bbab785977a2b76", "SECe76cd35ee80fe49c12d0fd9d979891b72ad8d8bdedbd1c6048edb78125bfb545", "系统消息通知，markdown类型"),
    SEND_SMS_BY_DEVELOPER_TOKEN("edc0f54f60bc6c17aeca637d2570fc58401048f8ec96fc146bbab785977a2b76", "SECe76cd35ee80fe49c12d0fd9d979891b72ad8d8bdedbd1c6048edb78125bfb545", "系统消息通知，技术专用");

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
