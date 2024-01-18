package com.aiyangniu.admin.service.impl;

import com.aiyangniu.admin.mapper.OmsOrderReturnReasonMapper;
import com.aiyangniu.admin.service.OmsOrderReturnReasonService;
import com.aiyangniu.entity.model.pojo.oms.OmsOrderReturnReason;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单退货原因管理实现类
 *
 * @author lzq
 * @date 2024/01/18
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OmsOrderReturnReasonServiceImpl implements OmsOrderReturnReasonService {

    private final OmsOrderReturnReasonMapper omsOrderReturnReasonMapper;

    @Override
    public int create(OmsOrderReturnReason returnReason) {
        returnReason.setCreateTime(new Date());
        return omsOrderReturnReasonMapper.insert(returnReason);
    }

    @Override
    public int update(Long id, OmsOrderReturnReason returnReason) {
        returnReason.setId(id);
        return omsOrderReturnReasonMapper.updateById(returnReason);
    }

    @Override
    public int delete(String ids) {
        int count = 0;
        List<Long> idList = Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(idList)){
            count = omsOrderReturnReasonMapper.delete(new LambdaQueryWrapper<OmsOrderReturnReason>().in(OmsOrderReturnReason::getId, ids));
        }
        return count;
    }

    @Override
    public List<OmsOrderReturnReason> list(Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        return omsOrderReturnReasonMapper.selectList(new LambdaQueryWrapper<OmsOrderReturnReason>().orderByDesc(OmsOrderReturnReason::getSort));
    }

    @Override
    public int updateStatus(List<Long> ids, Integer status) {
        if(!status.equals(0) && !status.equals(1)){
            return 0;
        }
        OmsOrderReturnReason record = new OmsOrderReturnReason();
        record.setStatus(status);
        return omsOrderReturnReasonMapper.update(record, new LambdaQueryWrapper<OmsOrderReturnReason>().in(OmsOrderReturnReason::getId, ids));
    }

    @Override
    public OmsOrderReturnReason getItem(Long id) {
        return omsOrderReturnReasonMapper.selectById(id);
    }
}
