package com.aiyangniu.admin.service.impl;

import com.aiyangniu.admin.mapper.OmsOrderMapper;
import com.aiyangniu.admin.mapper.OmsOrderOperateHistoryMapper;
import com.aiyangniu.admin.service.OmsOrderService;
import com.aiyangniu.entity.model.bo.OmsOrderDetail;
import com.aiyangniu.entity.model.dto.OmsMoneyInfoParamDTO;
import com.aiyangniu.entity.model.dto.OmsOrderDeliveryParamDTO;
import com.aiyangniu.entity.model.dto.OmsOrderQueryParamDTO;
import com.aiyangniu.entity.model.dto.OmsReceiverInfoParamDTO;
import com.aiyangniu.entity.model.pojo.oms.OmsOrder;
import com.aiyangniu.entity.model.pojo.oms.OmsOrderOperateHistory;
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
 * 订单管理实现类
 *
 * @author lzq
 * @date 2024/01/18
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OmsOrderServiceImpl implements OmsOrderService {

    private final OmsOrderMapper omsOrderMapper;
    private final OmsOrderOperateHistoryMapper omsOrderOperateHistoryMapper;

    @Override
    public List<OmsOrder> list(OmsOrderQueryParamDTO queryParam, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        return omsOrderMapper.getList(queryParam);
    }

    @Override
    public int delivery(List<OmsOrderDeliveryParamDTO> deliveryParamList) {
        // 批量发货
        int count = omsOrderMapper.delivery(deliveryParamList);
        // 添加操作记录
        List<OmsOrderOperateHistory> operateHistoryList = deliveryParamList.stream()
                .map(omsOrderDeliveryParam -> {
                    OmsOrderOperateHistory history = new OmsOrderOperateHistory();
                    history.setOrderId(omsOrderDeliveryParam.getOrderId());
                    history.setCreateTime(new Date());
                    history.setOperateMan("后台管理员");
                    history.setOrderStatus(2);
                    history.setNote("完成发货");
                    return history;
                }).collect(Collectors.toList());
        omsOrderOperateHistoryMapper.insertList(operateHistoryList);
        return count;
    }

    @Override
    public int close(String ids, String note) {
        int count = 0;
        List<Long> idList = Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(idList)){
            OmsOrder record = new OmsOrder();
            record.setStatus(4);
            count = omsOrderMapper.update(record, new LambdaQueryWrapper<OmsOrder>().eq(OmsOrder::getDeleteStatus, 0).in(OmsOrder::getId, idList));
            List<OmsOrderOperateHistory> historyList = idList.stream().map(orderId -> {
                OmsOrderOperateHistory history = new OmsOrderOperateHistory();
                history.setOrderId(orderId);
                history.setCreateTime(new Date());
                history.setOperateMan("后台管理员");
                history.setOrderStatus(4);
                history.setNote("订单关闭:" + note);
                return history;
            }).collect(Collectors.toList());
            omsOrderOperateHistoryMapper.insertList(historyList);
        }
        return count;
    }

    @Override
    public int delete(List<Long> ids) {
        OmsOrder record = new OmsOrder();
        record.setDeleteStatus(1);
        return  omsOrderMapper.update(record, new LambdaQueryWrapper<OmsOrder>().eq(OmsOrder::getDeleteStatus, 0).in(OmsOrder::getId, ids));
    }

    @Override
    public OmsOrderDetail detail(Long id) {
        return omsOrderMapper.getDetail(id);
    }

    @Override
    public int updateReceiverInfo(OmsReceiverInfoParamDTO receiverInfoParam) {
        OmsOrder order = new OmsOrder();
        order.setId(receiverInfoParam.getOrderId());
        order.setReceiverName(receiverInfoParam.getReceiverName());
        order.setReceiverPhone(receiverInfoParam.getReceiverPhone());
        order.setReceiverPostCode(receiverInfoParam.getReceiverPostCode());
        order.setReceiverDetailAddress(receiverInfoParam.getReceiverDetailAddress());
        order.setReceiverProvince(receiverInfoParam.getReceiverProvince());
        order.setReceiverCity(receiverInfoParam.getReceiverCity());
        order.setReceiverRegion(receiverInfoParam.getReceiverRegion());
        order.setModifyTime(new Date());
        int count = omsOrderMapper.updateById(order);
        // 插入操作记录
        OmsOrderOperateHistory history = new OmsOrderOperateHistory();
        history.setOrderId(receiverInfoParam.getOrderId());
        history.setCreateTime(new Date());
        history.setOperateMan("后台管理员");
        history.setOrderStatus(receiverInfoParam.getStatus());
        history.setNote("修改收货人信息");
        omsOrderOperateHistoryMapper.insert(history);
        return count;
    }

    @Override
    public int updateMoneyInfo(OmsMoneyInfoParamDTO moneyInfoParam) {
        OmsOrder order = new OmsOrder();
        order.setId(moneyInfoParam.getOrderId());
        order.setFreightAmount(moneyInfoParam.getFreightAmount());
        order.setDiscountAmount(moneyInfoParam.getDiscountAmount());
        order.setModifyTime(new Date());
        int count = omsOrderMapper.updateById(order);
        // 插入操作记录
        OmsOrderOperateHistory history = new OmsOrderOperateHistory();
        history.setOrderId(moneyInfoParam.getOrderId());
        history.setCreateTime(new Date());
        history.setOperateMan("后台管理员");
        history.setOrderStatus(moneyInfoParam.getStatus());
        history.setNote("修改费用信息");
        omsOrderOperateHistoryMapper.insert(history);
        return count;
    }

    @Override
    public int updateNote(Long id, String note, Integer status) {
        OmsOrder order = new OmsOrder();
        order.setId(id);
        order.setNote(note);
        order.setModifyTime(new Date());
        int count = omsOrderMapper.updateById(order);
        OmsOrderOperateHistory history = new OmsOrderOperateHistory();
        history.setOrderId(id);
        history.setCreateTime(new Date());
        history.setOperateMan("后台管理员");
        history.setOrderStatus(status);
        history.setNote("修改备注信息："+note);
        omsOrderOperateHistoryMapper.insert(history);
        return count;
    }
}
