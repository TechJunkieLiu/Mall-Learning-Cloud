package com.aiyangniu.entity.model.pojo.sms;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 首页推荐品牌实体
 *
 * @author lzq
 * @date 2023/09/20
 */
@Data
@ApiModel(value = "sms_home_brand", description = "首页推荐品牌表实体")
public class SmsHomeBrand implements Serializable {

    private static final long serialVersionUID = -8929103349035603035L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "品牌ID")
    private Long brandId;

    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "推荐状态")
    private Integer recommendStatus;

    @ApiModelProperty(value = "排序")
    private Integer sort;
}