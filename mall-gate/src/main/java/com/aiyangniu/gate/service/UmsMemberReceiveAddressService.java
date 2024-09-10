package com.aiyangniu.gate.service;

import com.aiyangniu.entity.model.pojo.ums.UmsMemberReceiveAddress;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 添加收货地址
     *
     * @param address 会员收货地址
     * @return 添加数量
     */
    int add(UmsMemberReceiveAddress address);

    /**
     * 删除收货地址
     *
     * @param id 收货地址ID
     * @return 删除数量
     */
    int delete(Long id);

    /**
     * 修改收货地址
     *
     * @param id 收货地址ID
     * @param address 修改的收货地址信息
     * @return 修改数量
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    int update(Long id, UmsMemberReceiveAddress address);
}
