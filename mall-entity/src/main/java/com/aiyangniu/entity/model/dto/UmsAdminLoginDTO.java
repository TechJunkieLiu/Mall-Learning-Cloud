package com.aiyangniu.entity.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录参数
 *
 * @author lzq
 * @date 2023/09/20
 */
@Data
@EqualsAndHashCode
public class UmsAdminLoginDTO {

    @NotBlank
    @ApiModelProperty(value = "用户名",required = true)
    private String username;

    @NotBlank
    @ApiModelProperty(value = "密码",required = true)
    private String password;
}
