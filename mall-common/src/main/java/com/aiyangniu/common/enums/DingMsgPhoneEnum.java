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

    GENERAL_PURPOSE("13853740741,15695357257,15047968011", "通用（包含技术、产品、领导等）"),
    DEVELOPER_PHONE("13853740741,13171077730", "技术人员"),
    PRODUCT_PERSONNEL_PHONE("18867535357", "产品人员"),
    DATA_ANALYST_PHONE("", "数据分析人员");

    private String phone;
    private String name;

    DingMsgPhoneEnum(String phone, String name) {
        this.phone = phone;
        this.name = name;
    }
}
