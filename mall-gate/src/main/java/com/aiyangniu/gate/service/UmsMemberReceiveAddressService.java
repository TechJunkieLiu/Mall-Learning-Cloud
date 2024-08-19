package com.aiyangniu.gate.service;

import com.aiyangniu.entity.model.pojo.ums.UmsMemberReceiveAddress;

import java.util.List;

/**
 * 会员收货地址管理接口
 *
 * @author lzq
 * @date 2024/03/22
 */
public interface UmsMemberReceiveAddressService {

    /**
     * 返回当前用户的收货地址
     *
     * @return 收货地址
     */
    List<UmsMemberReceiveAddress> list();

    /**
     * 获取地址详情
     *
     * @param id 地址id
     * @return 地址详情
     */
    UmsMemberReceiveAddress getItem(Long id);
}
