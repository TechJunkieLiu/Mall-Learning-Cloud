package com.aiyangniu.gate.service.impl;

import com.aiyangniu.entity.model.pojo.ums.UmsMember;
import com.aiyangniu.entity.model.pojo.ums.UmsMemberReceiveAddress;
import com.aiyangniu.gate.mapper.UmsMemberReceiveAddressMapper;
import com.aiyangniu.gate.service.UmsMemberReceiveAddressService;
import com.aiyangniu.gate.service.UmsMemberService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 会员收货地址管理实现类
 *
 * @author lzq
 * @date 2024/03/22
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UmsMemberReceiveAddressServiceImpl extends ServiceImpl<UmsMemberReceiveAddressMapper, UmsMemberReceiveAddress> implements UmsMemberReceiveAddressService {

    private final UmsMemberService umsMemberService;

    @Override
    public List<UmsMemberReceiveAddress> list() {
        UmsMember currentMember = umsMemberService.getCurrentMember();
        return list(new LambdaQueryWrapper<UmsMemberReceiveAddress>().eq(UmsMemberReceiveAddress::getMemberId, currentMember.getId()));
    }
}
