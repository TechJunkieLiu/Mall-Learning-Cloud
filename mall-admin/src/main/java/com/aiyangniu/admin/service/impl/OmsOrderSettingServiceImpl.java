package com.aiyangniu.admin.service.impl;

import com.aiyangniu.admin.mapper.OmsOrderSettingMapper;
import com.aiyangniu.admin.service.OmsOrderSettingService;
import com.aiyangniu.entity.model.pojo.oms.OmsOrderSetting;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 订单设置管理实现类
 *
 * @author lzq
 * @date 2024/01/19
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OmsOrderSettingServiceImpl implements OmsOrderSettingService {

    private final OmsOrderSettingMapper orderSettingMapper;

    @Override
    public OmsOrderSetting getItem(Long id) {
        return orderSettingMapper.selectById(id);
    }

    @Override
    public int update(Long id, OmsOrderSetting orderSetting) {
        orderSetting.setId(id);
        return orderSettingMapper.updateById(orderSetting);
    }
}
