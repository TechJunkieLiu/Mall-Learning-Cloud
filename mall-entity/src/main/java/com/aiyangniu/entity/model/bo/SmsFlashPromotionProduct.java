package com.aiyangniu.entity.model.bo;

import com.aiyangniu.entity.model.pojo.pms.PmsProduct;
import com.aiyangniu.entity.model.pojo.sms.SmsFlashPromotionProductRelation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 限时购商品信息封装
 *
 * @author lzq
 * @date 2023/09/20
 */
@Getter
@Setter
public class SmsFlashPromotionProduct extends SmsFlashPromotionProductRelation {

    private static final long serialVersionUID = -3246807139476959248L;

    @ApiModelProperty("关联商品")
    private PmsProduct product;
}
