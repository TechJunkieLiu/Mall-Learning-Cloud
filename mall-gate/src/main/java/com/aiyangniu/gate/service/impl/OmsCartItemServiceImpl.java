package com.aiyangniu.gate.service.impl;

import com.aiyangniu.entity.model.pojo.oms.OmsCartItem;
import com.aiyangniu.gate.mapper.OmsCartItemMapper;
import com.aiyangniu.gate.service.OmsCartItemService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 购物车管理实现类
 *
 * @author lzq
 * @date 2023/09/26
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OmsCartItemServiceImpl implements OmsCartItemService {

    private final OmsCartItemMapper omsCartItemMapper;

    @Override
    public List<OmsCartItem> list(Long memberId) {
        return omsCartItemMapper.selectList(new LambdaQueryWrapper<OmsCartItem>().eq(OmsCartItem::getDeleteStatus, 0).eq(OmsCartItem::getMemberId, memberId));
    }
}
