package com.aiyangniu.demo.controller;

import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.common.utils.HttpUtils.CloseableHttpClientUtil;
import com.aiyangniu.common.utils.HttpUtils.HttpUrlConnectionUtil;
import com.aiyangniu.demo.dto.VendorPrice;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP测试类
 *
 * @author lzq
 * @date 2024/03/05
 */
@Slf4j
@Api(value = "HttpController", tags = "HTTP测试类")
@RestController
@RequestMapping("/http")
public class HttpController {

    @ApiOperation(value = "POST请求")
    @GetMapping(value = "/test")
    public CommonResult<String> test() {

        String url1 = "http://192.168.3.27/http/get";
        String url2 = "http://192.168.3.27/http/post";

        VendorPrice vendorPrice = new VendorPrice();
        vendorPrice.setInquiryNo("1");
        vendorPrice.setSource("爱养牛招采系统");
        vendorPrice.setMaterialCode("2");
        vendorPrice.setMaterialName("3");
        vendorPrice.setMaterialDesc("4");
        vendorPrice.setCompanyCode("5");
        vendorPrice.setCompanyName("6");
        vendorPrice.setVendorCode("7");
        vendorPrice.setVendorErpCode("8");
        vendorPrice.setVendorName("9");
        vendorPrice.setCurrencyCode("CNY");
        vendorPrice.setCurrencyName("人民币");
        vendorPrice.setUnitCode("TO");
        vendorPrice.setUnitName("11");
        vendorPrice.setPriceBase(100);
        vendorPrice.setTaxRateCode("0000");
        vendorPrice.setTaxRateValue("0.0");

        String jsonString = JSONObject.toJSONString(vendorPrice);
        String param = "[" + jsonString + "]";
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(vendorPrice);

        Map<String, String> map = new HashMap<>(16);
        map.put("苹果", "apple");

        String result1 = CloseableHttpClientUtil.doGet(url1, "123");
        String result2 = CloseableHttpClientUtil.doGet(url1, map);
        String result3 = CloseableHttpClientUtil.doPost(url2, jsonObject);
        String result4 = CloseableHttpClientUtil.doPostJsonOne(url2, param);
        String result5 = CloseableHttpClientUtil.doPostJsonTwo(url2, param);
        String result6 = CloseableHttpClientUtil.doPostKvOne(url2, map);
        String result7 = CloseableHttpClientUtil.doPostKvTwo(url2, map);
        String result8 = CloseableHttpClientUtil.doGetSSL(url1, map);


        String result9 = HttpUrlConnectionUtil.doGet(url1);
        String result10 = HttpUrlConnectionUtil.doPost(url2, jsonString);


        log.info(result1);
        log.info(result2);


        return CommonResult.success("请求成功！");
    }

    @ApiOperation(value = "目标方法")
    @GetMapping(value = "/get")
    public void get() {
        log.info("请求成功！");
    }

    @ApiOperation(value = "目标方法")
    @PostMapping(value = "/post")
    public String post(String result) {
        log.info("请求结果返回：" + result);
        return result;
    }

}
