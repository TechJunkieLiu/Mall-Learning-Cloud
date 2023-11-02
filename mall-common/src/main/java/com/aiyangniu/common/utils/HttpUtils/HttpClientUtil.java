package com.aiyangniu.common.utils.HttpUtils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HTTP封装工具类（自定义HttpClient对象）
 *
 * @author lzq
 * @date 2023/08/23
 */
public class HttpClientUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);

    /**
     * 请求资源或服务，自定义client对象，传入请求参数，设置内容类型，并指定参数和返回数据的编码
     *
     * @param client client对象
     * @param url 资源地址
     * @param httpMethod 请求方法
     * @param parasMap 请求参数
     * @param headers 请求头信息
     * @param encoding 编码
     * @return 返回处理结果
     */
    public static String send(HttpClient client, String url, HttpMethodsEnum httpMethod, Map<String, String> parasMap, Header[] headers, String encoding) {
        String body = "";
        try {
            // 创建请求对象
            HttpRequestBase request = getRequest(url, httpMethod);

            // 设置header信息
            request.setHeaders(headers);

            // 判断是否支持设置entity(仅HttpPost、HttpPut、HttpPatch支持)
            if(HttpEntityEnclosingRequestBase.class.isAssignableFrom(request.getClass())){
                List<NameValuePair> paramList = new ArrayList<>();
                // 检测url中是否存在参数
                url = BaseUtil.checkHasParas(url, paramList);
                // 装填参数
                BaseUtil.map2List(paramList, parasMap);
                // 设置参数到请求对象中
                ((HttpEntityEnclosingRequestBase)request).setEntity(new UrlEncodedFormEntity(paramList, encoding));

                LOGGER.debug("请求地址：" + url);

                if(paramList.size() > 0){
                    LOGGER.debug("请求参数：" + paramList.toString());
                }
            }else{
                int idx = url.indexOf("?");
                LOGGER.debug("请求地址：" + url.substring(0, (idx > 0 ? idx-1 : url.length()-1)));
                if(idx > 0){
                    LOGGER.debug("请求参数：" + url.substring(idx + 1));
                }
            }
            // 调用发送请求
            body = execute(client, request, url, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return body;
    }

    /**
     * 请求资源或服务
     *
     * @param client client对象
     * @param request 请求对象
     * @param url 资源地址
     * @param encoding 编码
     * @return 返回处理结果
     */
    private static String execute(HttpClient client, HttpRequestBase request, String url, String encoding) {
        String body = "";
        HttpResponse response =null;
        try {

            // 执行请求操作，并拿到结果（同步阻塞）
            response = client.execute(request);

            // 获取结果实体
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                //按指定编码转换结果实体为String类型
                body = EntityUtils.toString(entity, encoding);
                LOGGER.debug(body);
            }
            EntityUtils.consume(entity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(response);
        }
        return body;
    }

    /**
     * 根据请求方法名，获取request对象
     *
     * @param url 资源地址
     * @param method 请求方式
     */
    private static HttpRequestBase getRequest(String url, HttpMethodsEnum method) {
        HttpRequestBase request;
        switch (method.getCode()) {
            case 0:
                request = new HttpGet(url);
                break;
            case 1:
                request = new HttpPost(url);
                break;
            case 2:
                request = new HttpHead(url);
                break;
            case 3:
                request = new HttpPut(url);
                break;
            case 4:
                request = new HttpDelete(url);
                break;
            case 5:
                request = new HttpTrace(url);
                break;
            case 6:
                request = new HttpPatch(url);
                break;
            case 7:
                request = new HttpOptions(url);
                break;
            default:
                request = new HttpPost(url);
                break;
        }
        return request;
    }

    /**
     * 尝试关闭response
     */
    private static void close(HttpResponse resp) {
        try {
            if(resp == null) {
                return;
            }
            // 如果CloseableHttpResponse是resp的父类，则支持关闭
            if(CloseableHttpResponse.class.isAssignableFrom(resp.getClass())){
                ((CloseableHttpResponse)resp).close();
            }
        } catch (IOException e) {
            LOGGER.error(String.valueOf(e));
        }
    }
}
