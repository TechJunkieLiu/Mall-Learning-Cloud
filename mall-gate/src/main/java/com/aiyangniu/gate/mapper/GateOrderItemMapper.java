package com.aiyangniu.gate.mapper;

import com.aiyangniu.entity.model.pojo.oms.OmsOrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单商品信息管理Mapper
 *
 * @author lzq
 * @date 2024/03/22
 */
public interface GateOrderItemMapper {

    /**
     * 批量插入
     *
     * @param list 订单列表（包含商品实体）
     * @return 插入数量
     */
    int insertList(@Param("list") List<OmsOrderItem> list);
}
