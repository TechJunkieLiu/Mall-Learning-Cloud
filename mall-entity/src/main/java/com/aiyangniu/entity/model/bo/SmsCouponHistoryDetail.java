package com.aiyangniu.entity.model.bo;

import com.aiyangniu.entity.model.pojo.sms.SmsCoupon;
import com.aiyangniu.entity.model.pojo.sms.SmsCouponHistory;
import com.aiyangniu.entity.model.pojo.sms.SmsCouponProductCategoryRelation;
import com.aiyangniu.entity.model.pojo.sms.SmsCouponProductRelation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 优惠券领取历史详情（包括优惠券信息和关联关系）
 *
 * @author lzq
 * @date 2023/09/20
 */
@Getter
@Setter
public class SmsCouponHistoryDetail extends SmsCouponHistory {

    private static final long serialVersionUID = -2232835408782793544L;

    @ApiModelProperty("相关优惠券信息")
    private SmsCoupon coupon;

    @ApiModelProperty("优惠券关联商品")
    private List<SmsCouponProductRelation> productRelationList;

    @ApiModelProperty("优惠券关联商品分类")
    private List<SmsCouponProductCategoryRelation> categoryRelationList;
}
