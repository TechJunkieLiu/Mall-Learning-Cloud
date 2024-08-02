package com.aiyangniu.common.exception_springboot;

import com.aiyangniu.common.api.CommonResult;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * 全局异常处理（基于SpringBoot默认异常处理ErrorController）
 *
 * @author lzq
 * @date 2024/08/01
 */
@RestController
public class MyErrorController1 implements ErrorController {

    private ErrorAttributes errorAttributes;

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/error")
    public CommonResult errorDev(WebRequest webRequest) {
        Map<String, Object> body = getErrorAttributes(webRequest);
        CommonResult commonResult = new CommonResult();
        if (body.containsKey("exception")) {
            commonResult.setCode(Long.parseLong(body.get("code").toString()));
            commonResult.setMessage(String.valueOf(body.get("message")));
            commonResult.setData(body);
        }else {
            commonResult.setMessage("系统内部错误！");
        }
        return commonResult;
    }

    private Map<String, Object> getErrorAttributes(WebRequest webRequest) {
        return this.errorAttributes.getErrorAttributes(webRequest, false);
    }
}
