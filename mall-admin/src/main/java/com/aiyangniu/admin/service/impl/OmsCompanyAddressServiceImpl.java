package com.aiyangniu.admin.service.impl;

import com.aiyangniu.admin.mapper.OmsCompanyAddressMapper;
import com.aiyangniu.admin.service.OmsCompanyAddressService;
import com.aiyangniu.entity.model.pojo.oms.OmsCompanyAddress;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收货地址管理实现类
 *
 * @author lzq
 * @date 2024/01/16
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OmsCompanyAddressServiceImpl implements OmsCompanyAddressService {

    private final OmsCompanyAddressMapper omsCompanyAddressMapper;

    @Override
    public List<OmsCompanyAddress> list() {
        return omsCompanyAddressMapper.selectList(new LambdaQueryWrapper<>());
    }
}
