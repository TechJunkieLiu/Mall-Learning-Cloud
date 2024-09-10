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
import org.springframework.util.CollectionUtils;

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
    private final UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;

    @Override
    public List<UmsMemberReceiveAddress> list() {
        UmsMember currentMember = umsMemberService.getCurrentMember();
        return list(new LambdaQueryWrapper<UmsMemberReceiveAddress>().eq(UmsMemberReceiveAddress::getMemberId, currentMember.getId()));
    }

    @Override
    public UmsMemberReceiveAddress getItem(Long id) {
        UmsMember currentMember = umsMemberService.getCurrentMember();
        List<UmsMemberReceiveAddress> addressList = list(new LambdaQueryWrapper<UmsMemberReceiveAddress>().eq(UmsMemberReceiveAddress::getMemberId, currentMember.getId()).eq(UmsMemberReceiveAddress::getId, id));
        if (!CollectionUtils.isEmpty(addressList)){
            return addressList.get(0);
        }
        return null;
    }

    @Override
    public int add(UmsMemberReceiveAddress address) {
        UmsMember currentMember = umsMemberService.getCurrentMember();
        address.setMemberId(currentMember.getId());
        return umsMemberReceiveAddressMapper.insert(address);
    }

    @Override
    public int delete(Long id) {
        UmsMember currentMember = umsMemberService.getCurrentMember();
        return umsMemberReceiveAddressMapper.delete(new LambdaQueryWrapper<UmsMemberReceiveAddress>().eq(UmsMemberReceiveAddress::getMemberId, currentMember.getId()).eq(UmsMemberReceiveAddress::getId, id));
    }

    @Override
    public int update(Long id, UmsMemberReceiveAddress address) {
        address.setId(null);
        UmsMember currentMember = umsMemberService.getCurrentMember();
        if (address.getDefaultStatus() == 1){
            // 先将原来的默认地址去除
            UmsMemberReceiveAddress receiveAddress = new UmsMemberReceiveAddress();
            receiveAddress.setDefaultStatus(0);
            umsMemberReceiveAddressMapper.update(receiveAddress, new LambdaQueryWrapper<UmsMemberReceiveAddress>().eq(UmsMemberReceiveAddress::getMemberId, currentMember.getId()).eq(UmsMemberReceiveAddress::getDefaultStatus, 1));
        }
        return umsMemberReceiveAddressMapper.update(address, new LambdaQueryWrapper<UmsMemberReceiveAddress>().eq(UmsMemberReceiveAddress::getMemberId, currentMember.getId()).eq(UmsMemberReceiveAddress::getId, id));
    }
}
