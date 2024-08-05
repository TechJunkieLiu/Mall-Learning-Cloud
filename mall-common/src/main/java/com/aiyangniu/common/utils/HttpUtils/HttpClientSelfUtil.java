package com.aiyangniu.common.utils.HttpUtils;

import cn.hutool.core.collection.CollectionUtil;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通过Apache封装好的HttpClient（自定义HttpClient对象）
 *
 * @author lzq
 * @date 2024/05/06
 */
public class HttpClientSelfUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);

    /**
     * 自定义client对象，传入请求参数，设置内容类型，并指定参数和返回数据的编码
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
                // 检测url中是否含有参数，如果有，则把参数加到参数列表中
                url = checkHasParas(url, paramList);
                // 参数转换，将map中的参数，转到参数列表中
                if (CollectionUtil.isNotEmpty(parasMap)){
                    for (Map.Entry<String, String> entry : parasMap.entrySet()) {
                        paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                    }
                }
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
            // 尝试关闭response
            close(response);
        }
        return body;
    }

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

    public static String checkHasParas(String url, List<NameValuePair> paramList) {
        // 检测url中是否存在参数
        if (url.contains("?") && url.indexOf("?") < url.indexOf("=")) {
            // 生成参数，参数格式  k1=v1&k2=v2
            Map<String, String> map = buildParas(url.substring(url.indexOf("?") + 1));
            // 参数转换，将map中的参数，转到参数列表中
            if (CollectionUtil.isNotEmpty(map)){
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }
            url = url.substring(0, url.indexOf("?"));
        }
        return url;
    }

    public static Map<String, String> buildParas(String paras){
        String[] p = paras.split("&");
        String[][] ps = new String[p.length][2];
        int pos;
        for (int i = 0; i < p.length; i++) {
            pos = p[i].indexOf("=");
            ps[i][0] = p[i].substring(0, pos);
            ps[i][1] = p[i].substring(pos+1);
        }

        // 创建参数队列 {{"k1","v1"},{"k2","v2"}}
        Map<String, String> map = new HashMap<>(16);
        for (String[] str : ps) {
            map.put(str[0], str[1]);
        }
        return map;
    }

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
