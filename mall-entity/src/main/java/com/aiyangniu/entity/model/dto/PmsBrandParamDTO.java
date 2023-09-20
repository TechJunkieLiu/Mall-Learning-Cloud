package com.aiyangniu.entity.model.dto;

import com.aiyangniu.entity.validator.FlagValidator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * 品牌请求参数
 *
 * @author lzq
 * @date 2023/09/20
 */
@Data
@EqualsAndHashCode
public class PmsBrandParamDTO {

    @NotBlank(message = "品牌名称不能为空")
    @ApiModelProperty(value = "品牌名称", required = true)
    private String name;

    @ApiModelProperty(value = "品牌首字母")
    private String firstLetter;

    @Min(value = 0, message = "排序最小为0")
    @ApiModelProperty(value = "排序字段")
    private Integer sort;

    @FlagValidator(value = {"0","1"}, message = "厂家状态不正确")
    @ApiModelProperty(value = "是否为厂家制造商")
    private Integer factoryStatus;

    @FlagValidator(value = {"0","1"}, message = "显示状态不正确")
    @ApiModelProperty(value = "是否进行显示")
    private Integer showStatus;

    @NotBlank(message = "品牌logo不能为空")
    @ApiModelProperty(value = "品牌logo", required = true)
    private String logo;

    @ApiModelProperty(value = "品牌大图")
    private String bigPic;

    @ApiModelProperty(value = "品牌故事")
    private String brandStory;
}
