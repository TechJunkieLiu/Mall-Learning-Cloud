package com.aiyangniu.gate.service;

import com.aiyangniu.common.api.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 认证服务远程调用接口
 *
 * @author lzq
 * @date 2023/09/26
 */
@FeignClient("mall-auth")
public interface AuthService {

    /**
     * 根据登录信息获取Token
     *
     * @param parameters 登录信息
     * @return Token
     */
    @PostMapping(value = "/oauth/token")
    CommonResult getAccessToken(@RequestParam Map<String, String> parameters);
}
