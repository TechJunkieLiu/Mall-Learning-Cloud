package com.aiyangniu.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 导出数据到Excel模板实体类
 *
 * @author lzq
 * @date 2024/03/20
 */
@Data
public class AreaModelDTO {

    @ApiModelProperty(value = "序号")
    private Integer no;

    @ApiModelProperty(value = "编码")
    private Integer areaCode;

    @ApiModelProperty(value = "名称")
    private String areaName;

    @ApiModelProperty(value = "级别")
    private Integer level;
}
