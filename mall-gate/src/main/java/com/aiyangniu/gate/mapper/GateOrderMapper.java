package com.aiyangniu.gate.mapper;

import com.aiyangniu.entity.model.bo.OmsOrderDetail;
import com.aiyangniu.entity.model.pojo.oms.OmsOrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 前台订单管理Mapper
 *
 * @author lzq
 * @date 2023/09/20
 */
public interface GateOrderMapper {

    /**
     * 获取超时订单
     *
     * @param minute 超时时间（分）
     * @return 包含商品信息的订单商品列表（超时）
     */
    List<OmsOrderDetail> getTimeOutOrders(@Param("minute") Integer minute);

    /**
     * 批量修改订单状态
     *
     * @param ids 订单ID列表（超时订单）
     * @param status 订单状态（已关闭）
     * @return 修改数量
     */
    int updateOrderStatus(@Param("ids") List<Long> ids, @Param("status") Integer status);

    /**
     * 解除取消订单的库存锁定
     *
     * @param orderItemList 订单商品列表（超时取消）
     * @return 解除数量
     */
    int releaseSkuStockLock(@Param("itemList") List<OmsOrderItem> orderItemList);

    /**
     * 获取订单及下单商品详情
     *
     * @param orderId 订单ID
     * @return 商品详情
     */
    OmsOrderDetail getDetail(@Param("orderId") Long orderId);

    /**
     * 恢复所有下单商品的锁定库存及扣减真实库存（pms_sku_stock）
     *
     * @param orderItemList 下单商品
     * @return 恢复数量
     */
    int updateSkuStock(@Param("itemList") List<OmsOrderItem> orderItemList);
}
