package com.aiyangniu.admin.service;

import com.aiyangniu.entity.model.pojo.oms.OmsCompanyAddress;

import java.util.List;

/**
 * 收货地址管理接口
 *
 * @author lzq
 * @date 2024/01/16
 */
public interface OmsCompanyAddressService {

    /**
     * 获取全部收货地址
     *
     * @return 收货地址列表
     */
    List<OmsCompanyAddress> list();
}
