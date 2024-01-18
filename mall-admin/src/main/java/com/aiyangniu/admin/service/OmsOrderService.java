package com.aiyangniu.admin.service;

import com.aiyangniu.entity.model.bo.OmsOrderDetail;
import com.aiyangniu.entity.model.dto.OmsMoneyInfoParamDTO;
import com.aiyangniu.entity.model.dto.OmsOrderDeliveryParamDTO;
import com.aiyangniu.entity.model.dto.OmsOrderQueryParamDTO;
import com.aiyangniu.entity.model.dto.OmsReceiverInfoParamDTO;
import com.aiyangniu.entity.model.pojo.oms.OmsOrder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 订单管理接口
 *
 * @author lzq
 * @date 2024/01/18
 */
public interface OmsOrderService {

    /**
     * 订单查询
     *
     * @param dto 查询参数
     * @param pageSize 页条数
     * @param pageNum 当前页
     * @return 订单列表
     */
    List<OmsOrder> list(OmsOrderQueryParamDTO dto, Integer pageSize, Integer pageNum);

    /**
     * 批量发货
     *
     * @param deliveryParamList 订单发货列表
     * @return 发货个数
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    int delivery(List<OmsOrderDeliveryParamDTO> deliveryParamList);

    /**
     * 批量关闭订单
     *
     * @param ids 订单IDS
     * @param note 备注
     * @return 关闭个数
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    int close(String ids, String note);

    /**
     * 批量删除订单
     *
     * @param ids 订单IDS
     * @return 删除个数
     */
    int delete(List<Long> ids);

    /**
     * 获取指定订单详情
     *
     * @param id 订单ID
     * @return 订单详情
     */
    OmsOrderDetail detail(Long id);

    /**
     * 修改订单收货人信息
     *
     * @param dto 订单修改收货人信息参数
     * @return 修改个数
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    int updateReceiverInfo(OmsReceiverInfoParamDTO dto);

    /**
     * 修改订单费用信息
     *
     * @param moneyInfoParam 修改订单费用信息参数
     * @return 修改个数
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    int updateMoneyInfo(OmsMoneyInfoParamDTO moneyInfoParam);

    /**
     * 修改订单备注
     *
     * @param id 订单ID
     * @param note 备注
     * @param status 状态
     * @return 修改个数
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    int updateNote(Long id, String note, Integer status);
}
