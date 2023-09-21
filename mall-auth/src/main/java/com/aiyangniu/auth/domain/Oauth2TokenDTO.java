package com.aiyangniu.auth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * OAuth2获取Token返回信息封装
 *
 * @author lzq
 * @date 2023/09/21
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class Oauth2TokenDTO {

    @ApiModelProperty("访问令牌")
    private String token;

    @ApiModelProperty("刷令牌")
    private String refreshToken;

    @ApiModelProperty("访问令牌头前缀")
    private String tokenHead;

    @ApiModelProperty("有效时间（秒）")
    private Integer expiresIn;
}
