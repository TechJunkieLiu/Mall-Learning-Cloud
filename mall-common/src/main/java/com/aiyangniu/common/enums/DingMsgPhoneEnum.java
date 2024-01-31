package com.aiyangniu.common.enums;

import lombok.Getter;

/**
 * 钉钉消息接收用户，配置钉钉绑定的电话即可
 *
 * @author lzq
 * @date 2024/01/30
 */
@Getter
public enum DingMsgPhoneEnum {

    GENERAL_PURPOSE("138****0741,156****7257,150****8012", "通用（包含技术、产品、领导等）"),
    PRODUCT_PERSONNEL_PHONE("188****5357,155****9685", "产品人员"),
    DEVELOPER_PHONE("13171077730", "技术人员"),
    DATA_ANALYST_PHONE("", "数据分析人员");

    private String phone;
    private String name;

    DingMsgPhoneEnum(String phone, String name) {
        this.phone = phone;
        this.name = name;
    }
}
