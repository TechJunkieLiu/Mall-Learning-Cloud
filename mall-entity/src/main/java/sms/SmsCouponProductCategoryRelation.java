package sms;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 优惠券和产品分类关系的实体
 *
 * @author lzq
 * @date 2023/09/08
 */
@Data
@ApiModel(value = "sms_coupon_product_category_relation", description = "优惠券和产品分类关系表实体")
public class SmsCouponProductCategoryRelation implements Serializable {

    private static final long serialVersionUID = 4612496951562044872L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "优惠券ID")
    private Long couponId;

    @ApiModelProperty(value = "产品分类ID")
    private Long productCategoryId;

    @ApiModelProperty(value = "产品分类名称")
    private String productCategoryName;

    @ApiModelProperty(value = "父分类名称")
    private String parentCategoryName;
}