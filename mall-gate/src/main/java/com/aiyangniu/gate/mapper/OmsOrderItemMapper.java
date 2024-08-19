package com.aiyangniu.gate.mapper;

import com.aiyangniu.entity.model.pojo.oms.OmsOrderItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单中所包含的商品Mapper
 *
 * @author lzq
 * @date 2023/09/20
 */
public interface OmsOrderItemMapper extends BaseMapper<OmsOrderItem> {

    /**
     * 批量插入
     *
     * @param list 订单列表（包含商品实体）
     * @return 插入数量
     */
    int insertList(@Param("list") List<OmsOrderItem> list);
}
