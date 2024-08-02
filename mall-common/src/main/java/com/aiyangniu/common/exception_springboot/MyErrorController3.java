package com.aiyangniu.common.exception_springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全局异常处理（基于SpringBoot默认异常处理ErrorController）
 *
 * @author lzq
 * @date 2024/07/26
 */
@RestController
public class MyErrorController3 extends BasicErrorController {

    @Autowired
    public MyErrorController3(ErrorAttributes errorAttributes, ServerProperties serverProperties, List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, serverProperties.getError(), errorViewResolvers);
    }

    /**
     * 处理html请求
     */
    @Override
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        HttpStatus status = getStatus(request);
        Map<String, Object> model = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.TEXT_HTML));
        ModelAndView modelAndView = new ModelAndView("myErrorPage", model, status);
        return modelAndView;
    }

    /**
     * 处理json请求
     */
    @Override
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        Map<String, Object> body = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.ALL));
        // 可以换成项目中自定义的通信json
        Map<String, Object> resultBody = new HashMap<>(16);
        resultBody.put("success", false);
        resultBody.put("code", body.get("status"));
        resultBody.put("msg", body.get("error"));
        return new ResponseEntity<>(resultBody, HttpStatus.OK);
    }
}
