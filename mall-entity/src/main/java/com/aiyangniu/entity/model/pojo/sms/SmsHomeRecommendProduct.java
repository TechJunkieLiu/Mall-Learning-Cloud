package com.aiyangniu.entity.model.pojo.sms;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 人气推荐商品实体
 *
 * @author lzq
 * @date 2023/09/20
 */
@Data
@ApiModel(value = "sms_home_recommend_product", description = "人气推荐商品表实体")
public class SmsHomeRecommendProduct implements Serializable {

    private static final long serialVersionUID = 7200899653542902952L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "商品ID")
    private Long productId;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "推荐状态")
    private Integer recommendStatus;

    @ApiModelProperty(value = "排序")
    private Integer sort;
}