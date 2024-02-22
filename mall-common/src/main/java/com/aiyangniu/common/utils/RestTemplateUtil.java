package com.aiyangniu.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

/**
 * RestTemplate请求调用工具类（同服务、跨服务）
 * 相较于之前常用的HttpClient，RestTemplate是一种更为优雅的调用RESTFul服务的方式。简化了与http服务的通信方式，统一了RESTFul的标准，封装了http连接，我们只需要传入url及其返回值类型即可
 * 注：参数中的类型最好与被调用请求url的返回体类型一致
 *
 * @author lzq
 * @date 2024/02/21
 */
@Component
public class RestTemplateUtil {

    /**
     * 请求头类型名
     */
    private static final String HEADER_NAME = "Content-Type";
    /**
     * 请求头类型值
     */
    private static final String HEADER_VALUE = "application/json";
    /**
     * 身份令牌key
     */
    private static final String TOKEN_NAME = "UserToken";
    /**
     * 调用的url返回的消息体的key
     */
    private static final String RESPONSE_BODY = "responseBody";
    /**
     * 静态的RestTemplate对象
     */
    private static RestTemplate rtl;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 初始化顺序处理
     * 在自动注入后初始化
     */
    @PostConstruct
    public void initTemplate(){
        rtl = restTemplate;
    }

    /**
     * post请求连接
     */
    public static JSONObject post(String url, Class responseType){
        // 构建请求头
        HttpHeaders headers = new HttpHeaders();
        // 请求头内容类型和对应的值
        headers.add(HEADER_NAME, HEADER_VALUE);
        // 请求头组装的请求体
        HttpEntity httpEntity = new HttpEntity(headers);
        // 响应体构建
        ResponseEntity responseEntity = rtl.postForEntity(url, httpEntity, responseType);
        // 响应体获取
        Object responseBodyObj = responseEntity.getBody();
        // 响应体转换类型
        JSONObject responseBodyJsonObj = new JSONObject();
        responseBodyJsonObj.put(RESPONSE_BODY, responseBodyObj);
        // 响应体返回(调用的接口返回的内容)
        return responseBodyJsonObj;
    }

    /**
     * post请求连接
     */
    public static JSONObject post(String userToken, String url, Class responseType){
        // 构建请求头
        HttpHeaders headers = new HttpHeaders();
        // 请求头内容类型和对应的值
        headers.add(HEADER_NAME, HEADER_VALUE);
        // 请求头内容含有token值
        headers.add(TOKEN_NAME, userToken);
        // 请求头组装请求体
        HttpEntity httpEntity = new HttpEntity(headers);
        // 响应体构建传参
        ResponseEntity responseEntity = rtl.postForEntity(url, httpEntity, responseType);
        // 响应体获取
        Object responseBodyObj = responseEntity.getBody();
        // 响应体转换类型object转jsonObject
        JSONObject responseBodyJsonObj = new JSONObject();
        responseBodyJsonObj.put(RESPONSE_BODY, responseBodyObj);
        // 返回响应体
        return responseBodyJsonObj;
    }

    /**
     * post请求连接
     */
    public static JSONObject post(String url, JSONObject requestJson, Class responseType){
        // 请求头构建
        HttpHeaders headers = new HttpHeaders();
        // 请求头内容类型
        headers.add(HEADER_NAME, HEADER_VALUE);
        // 请求头组装请求体，含有请求内容
        HttpEntity httpEntity = new HttpEntity(requestJson, headers);
        // 响应体构建
        ResponseEntity responseEntity = rtl.postForEntity(url, httpEntity, responseType);
        // 响应体获取
        Object responseBodyObj = responseEntity.getBody();
        // 响应体转换类型
        JSONObject responseBodyJsonObj = new JSONObject();
        responseBodyJsonObj.put(RESPONSE_BODY, responseBodyObj);
        // 返回响应体内容
        return responseBodyJsonObj;
    }

    /**
     * post请求连接
     */
    public static JSONObject post(String userToken, String url, JSONObject requestJson, Class responseType){
        // 构建请求头
        HttpHeaders headers = new HttpHeaders();
        // 请求头内容填充
        headers.add(HEADER_NAME, HEADER_VALUE);
        // 请求头内容含有token值
        headers.add(TOKEN_NAME, userToken);
        // 请求头及请求内容组装请求体
        HttpEntity httpEntity = new HttpEntity(requestJson, headers);
        // 响应体构建
        ResponseEntity responseEntity = rtl.postForEntity(url, httpEntity, responseType);
        // 响应体获取
        Object responseBodyObj = responseEntity.getBody();
        // 响应体类型转换
        JSONObject responseBodyJsonObj = new JSONObject();
        responseBodyJsonObj.put(RESPONSE_BODY, responseBodyObj);
        // 响应体返回
        return responseBodyJsonObj;
    }

    /**
     * get请求连接
     */
    public static JSONObject get(String url, Class responseType){
        // 构建请求头
        HttpHeaders headers = new HttpHeaders();
        // 请求头内容类型和对应的值
        headers.add(HEADER_NAME, HEADER_VALUE);
        // 请求头组装的请求体
        HttpEntity httpEntity = new HttpEntity(headers);
        // 响应体构建
        ResponseEntity responseEntity = rtl.getForEntity(url, responseType, httpEntity);
        // 响应体获取
        Object responseBodyObj = responseEntity.getBody();
        // 响应体转换类型
        JSONObject responseBodyJsonObj = new JSONObject();
        responseBodyJsonObj.put(RESPONSE_BODY, responseBodyObj);
        // 响应体返回
        return responseBodyJsonObj;
    }

    /**
     * get请求连接
     */
    public static JSONObject getNonEntity(String url, Class responseType){
        // 响应体构建
        ResponseEntity responseEntity = rtl.getForEntity(url, responseType);
        // 响应体获取
        Object responseBodyObj = responseEntity.getBody();
        // 响应体转换类型
        JSONObject responseBodyJsonObj = new JSONObject();
        responseBodyJsonObj.put(RESPONSE_BODY, responseBodyObj);
        // 响应体返回
        return responseBodyJsonObj;
    }

    /**
     * get请求连接
     */
    public static JSONObject get(String userToken, String url, Class responseType){
        // 构建请求头
        HttpHeaders headers = new HttpHeaders();
        // 请求头内容类型和对应的值
        headers.add(HEADER_NAME, HEADER_VALUE);
        // 请求头内容含有token值
        headers.add(TOKEN_NAME, userToken);
        // 请求头组装请求体
        HttpEntity httpEntity = new HttpEntity(headers);
        // 响应体构建传参
        ResponseEntity responseEntity = rtl.getForEntity(url, responseType, httpEntity);
        // 响应体获取
        Object responseBodyObj = responseEntity.getBody();
        // 响应体转换类型object转jsonObject
        JSONObject responseBodyJsonObj = new JSONObject();
        responseBodyJsonObj.put(RESPONSE_BODY, responseBodyObj);
        // 返回响应体
        return responseBodyJsonObj;
    }

    /**
     * get请求连接
     */
    public static JSONObject get(String url, JSONObject requestJson, Class responseType){
        // 请求头构建
        HttpHeaders headers = new HttpHeaders();
        // 请求头内容类型
        headers.add(HEADER_NAME, HEADER_VALUE);
        // 请求头组装请求体，含有请求内容
        HttpEntity httpEntity = new HttpEntity(requestJson, headers);
        // 响应体构建
        ResponseEntity responseEntity = rtl.getForEntity(url, responseType, httpEntity);
        // 响应体获取
        Object responseBodyObj = responseEntity.getBody();
        // 响应体转换类型
        JSONObject responseBodyJsonObj = new JSONObject();
        responseBodyJsonObj.put(RESPONSE_BODY, responseBodyObj);
        // 返回响应体内容
        return responseBodyJsonObj;
    }

    /**
     * get请求连接
     */
    public static JSONObject get(String userToken, String url, JSONObject requestJson, Class responseType){
        // 构建请求头
        HttpHeaders headers = new HttpHeaders();
        // 请求头内容填充
        headers.add(HEADER_NAME, HEADER_VALUE);
        // 请求头内容含有token值
        headers.add(TOKEN_NAME, userToken);
        // 请求头及请求内容组装请求体
        HttpEntity httpEntity = new HttpEntity(requestJson, headers);
        // 响应体构建
        ResponseEntity responseEntity = rtl.getForEntity(url, responseType, httpEntity);
        // 响应体获取
        Object responseBodyObj = responseEntity.getBody();
        // 响应体类型转换
        JSONObject responseBodyJsonObj = new JSONObject();
        responseBodyJsonObj.put(RESPONSE_BODY, responseBodyObj);
        // 响应体返回
        return responseBodyJsonObj;
    }
}
