package com.aiyangniu.gate.service.impl;

import com.aiyangniu.entity.model.bo.OmsOrderReturnApplyParam;
import com.aiyangniu.entity.model.pojo.oms.OmsOrderReturnApply;
import com.aiyangniu.gate.mapper.OmsGateOrderReturnApplyMapper;
import com.aiyangniu.gate.service.OmsGateOrderReturnApplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 前台订单退货管理实现类
 *
 * @author lzq
 * @date 2024/08/30
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OmsGateOrderReturnApplyServiceImpl implements OmsGateOrderReturnApplyService {

    private final OmsGateOrderReturnApplyMapper omsGateOrderReturnApplyMapper;

    @Override
    public int create(OmsOrderReturnApplyParam returnApply) {
        OmsOrderReturnApply realApply = new OmsOrderReturnApply();
        BeanUtils.copyProperties(returnApply, realApply);
        realApply.setCreateTime(new Date());
        realApply.setStatus(0);
        return omsGateOrderReturnApplyMapper.insert(realApply);
    }
}
