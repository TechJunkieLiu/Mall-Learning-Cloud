package com.aiyangniu.entity.model.bo;

import com.aiyangniu.entity.model.pojo.oms.OmsOrder;
import com.aiyangniu.entity.model.pojo.oms.OmsOrderItem;
import com.aiyangniu.entity.model.pojo.oms.OmsOrderOperateHistory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 订单详情信息（包含订单商品信息）
 *
 * @author lzq
 * @date 2023/09/20
 */
@Data
public class OmsOrderDetail extends OmsOrder {

    private static final long serialVersionUID = 7195752814367945612L;

    @Getter
    @Setter
    @ApiModelProperty("订单商品列表")
    private List<OmsOrderItem> orderItemList;

    @Getter
    @Setter
    @ApiModelProperty("订单操作记录列表")
    private List<OmsOrderOperateHistory> historyList;
}
