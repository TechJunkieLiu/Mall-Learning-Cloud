package com.aiyangniu.demo.controller;

import com.aiyangniu.demo.config.SmsConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 短信验证码
 *
 * @author lzq
 * @date 2023/10/18
 */
@Slf4j
@Api(value = "SmsController", tags = "短信验证码")
@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SmsController {

    private final SmsConfig smsConfig;
    private final RestTemplate restTemplate;
    private static final String URL_TEMPLATE = "https://jmsms.market.alicloudapi.com/sms/send?mobile=%s&templateId=%s&value=%s";

//    @Value("${sms.app-code}")
//    private String smsAppCode;
//
//    @Value("${sms.template-id}")
//    private String smsTemplateId;

    @ApiOperation(value = "发送短信验证码")
    @GetMapping("/send")
    public void send(@RequestParam String mobile, @RequestParam String value) {
        String url = String.format(URL_TEMPLATE, mobile, smsConfig.getTemplateId(), value);
        HttpHeaders headers = new HttpHeaders();
        // 最后在header中的格式（中间是英⽂空格）
        headers.set("Authorization", "APPCODE " + smsConfig.getAppCode());
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        log.info("url = {}, body = {}", url, response.getBody());
        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("发送短信成功，响应信息:{}", response.getBody());
        } else {
            log.error("发送短信失败，响应信息:{}", response.getBody());
        }
    }
}
