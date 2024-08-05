package com.aiyangniu.common.utils.HttpUtils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 通过Apache封装好的CloseableHttpClient
 * CloseableHttpClient是在HttpClient的基础上修改更新而来的
 * 涉及到请求头token的设置（请求验证），利用fastjson转换请求或返回结果字符串为json格式，当然上面两种方式也是可以设置请求头token、json的
 *
 * @author lzq
 * @date 2024/05/06
 */
public class CloseableHttpClientUtil {

    public static final String CHARSET = "UTF-8";
    private static final CloseableHttpClient HTTP_CLIENT;
    private static final Logger LOGGER = LoggerFactory.getLogger(CloseableHttpClientUtil.class);

    private static String tokenString = "";

    // 采用静态代码块，初始化超时时间配置，再根据配置生成默认httpClient对象
    static {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000).build();
        HTTP_CLIENT = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }

    /**
     * Get
     */
    public static String doGet(String url, String token) {
        HttpGet httpGet = new HttpGet(url);
        try {
            if (tokenString != null && !tokenString.equals("")){
                tokenString = getToken();
            }
            // api_gateway_auth_token自定义header头，用于token验证使用
            httpGet.addHeader("api_gateway_auth_token", token);
            httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36");

            HttpResponse response = HTTP_CLIENT.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 返回json格式
                String res = EntityUtils.toString(response.getEntity());
                return res;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get
     */
    public static String doGet(String url, Map<String, String> params) {
        if (StrUtil.isBlank(url)) {
            return null;
        }
        try {
            if (ObjectUtil.isNotEmpty(params)) {
                List<NameValuePair> pairs = new ArrayList<>();
                for (String key : params.keySet()) {
                    String value = params.get(key);
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(key, value));
                    }
                }
                // 将请求参数和url进行拼接
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, CHARSET));
            }
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = HTTP_CLIENT.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode !=  HttpStatus.SC_OK) {
                httpGet.abort();
                throw new RuntimeException("HttpClient, error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null) {
                result = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);
            response.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * HTTP Post
     */
    public static String doPost(String url, JSONObject json) {
        HttpPost httpPost = new HttpPost(url);
        // api_gateway_auth_token自定义header头，用于token验证使用
        httpPost.addHeader("api_gateway_auth_token",  getToken());
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36");
        try {
            StringEntity se = new StringEntity(json.toString());
            se.setContentEncoding("UTF-8");
            // 发送json数据需要设置contentType
            se.setContentType("application/x-www-form-urlencoded");
            // 设置请求参数
            httpPost.setEntity(se);
            HttpResponse response = HTTP_CLIENT.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 返回json格式
                String res = EntityUtils.toString(response.getEntity());
                return res;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (HTTP_CLIENT != null){
                try {
                    HTTP_CLIENT.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * HTTP Post（用于请求json格式的参数，StringEntity传参）
     */
    public static String doPostJsonOne(String url, String params) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        String charSet = "UTF-8";
        StringEntity entity = new StringEntity(params, charSet);
        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = HTTP_CLIENT.execute(httpPost);
            int state = response.getStatusLine().getStatusCode();
            if (state == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                return EntityUtils.toString(responseEntity);
            }
            else{
                LOGGER.error("请求返回: " + state + " ( " + url + " )");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * HTTP Post（用于请求json格式的参数，StringEntity传参）
     */
    public static String doPostJsonTwo(String url, String params) {
        if (StrUtil.isBlank(url)) {
            return null;
        }
        HttpPost httpPost = new HttpPost(url);
        if (StrUtil.isNotEmpty(params)){
            httpPost.setEntity(new StringEntity(params, ContentType.APPLICATION_JSON));
        }
        try (CloseableHttpResponse response = HTTP_CLIENT.execute(httpPost)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode !=  HttpStatus.SC_OK) {
                httpPost.abort();
                throw new RuntimeException("HttpClientPost，error status code：" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null) {
                result = EntityUtils.toString(entity, CHARSET);
            }
            EntityUtils.consume(entity);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * HTTP Post（用于key-value格式的参数，UrlEncodedFormEntity传参）
     */
    public static String doPostKvOne(String url, Map<String, String> params){
        BufferedReader in = null;
        try {
            // 实例化HTTP方法
            HttpPost httpPost = new HttpPost();
            httpPost.setURI(new URI(url));

            // 设置参数
            List<NameValuePair> nameValuePairList = new ArrayList<>();

            for (Iterator iter = params.keySet().iterator(); iter.hasNext();) {
                String name = (String) iter.next();
                String value = String.valueOf(params.get(name));
                nameValuePairList.add(new BasicNameValuePair(name, value));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList, CHARSET));

            HttpResponse response = HTTP_CLIENT.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            if(code ==  HttpStatus.SC_OK){
                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder("");
                String line = "";
                String property = System.getProperty("line.separator");
                while ((line = in.readLine()) != null) {
                    sb.append(line).append(property);
                }
                in.close();
                return sb.toString();
            } else{
                System.out.println("状态码：" + code);
                return null;
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * HTTP Post（用于key-value格式的参数，UrlEncodedFormEntity传参）
     */
    public static String doPostKvTwo(String url, Map<String, String> params) {
        if (StrUtil.isBlank(url)) {
            return null;
        }
        List<NameValuePair> pairs = null;
        if (params != null && !params.isEmpty()) {
            pairs = new ArrayList<>(params.size());
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String value = entry.getValue();
                if (value != null) {
                    pairs.add(new BasicNameValuePair(entry.getKey(), value));
                }
            }
        }
        HttpPost httpPost = new HttpPost(url);
        try (CloseableHttpResponse response = HTTP_CLIENT.execute(httpPost)) {
            if (pairs != null && pairs.size() > 0) {
                // 传入的不管是String、Boolean、Integer，这里放到 Http Entity 里面的类型都是字节类型，都是通过将byte进行ASCII编码来实现的，
                // 服务器端反序列化成String类型后，通过MVC的框架进行解析，注意这里也需要区分提交方式，框架可能会选取适当的httpMessageConverter进行解析
                httpPost.setEntity(new UrlEncodedFormEntity(pairs, CHARSET));
            }
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode !=  HttpStatus.SC_OK) {
                httpPost.abort();
                throw new RuntimeException("HttpClientPost，error status code：" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null) {
                result = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * HTTPS Get
     */
    public static String doGetSSL(String url, Map<String, String> params) {
        if (StrUtil.isBlank(url)) {
            return null;
        }
        try {
            if (params != null && !params.isEmpty()) {
                List<NameValuePair> pairs = new ArrayList<>(params.size());
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String value = entry.getValue();
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(entry.getKey(), value));
                    }
                }
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, CHARSET));
            }
            HttpGet httpGet = new HttpGet(url);

            // https 注意这里获取https内容，使用了忽略证书的方式(创建忽略整数验证的CloseableHttpClient对象)，当然还有其他的方式来获取https内容
            CloseableHttpClient httpsClient = CloseableHttpClientUtil.createSSLClientDefault();
            CloseableHttpResponse response = httpsClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode !=  HttpStatus.SC_OK) {
                httpGet.abort();
                throw new RuntimeException("HttpClient, error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null) {
                result = EntityUtils.toString(entity, CHARSET);
            }
            EntityUtils.consume(entity);
            response.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getToken() {
        String token = "";
        JSONObject object = new JSONObject();
        object.put("appid", "appid");
        object.put("secretkey", "secretkey");
        HttpPost httpPost = new HttpPost("http://localhost/login");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36");
        try {
            StringEntity se = new StringEntity(object.toString());
            se.setContentEncoding("UTF-8");
            // 发送json数据需要设置contentType
            se.setContentType("application/x-www-form-urlencoded");
            // 设置请求参数
            httpPost.setEntity(se);
            HttpResponse response = HTTP_CLIENT.execute(httpPost);
            // 这里可以把返回的结果按照自定义的返回数据结果，把string转换成自定义类
            // ResultTokenBO result = JSONObject.parseObject(response, ResultTokenBO.class);
            // 把response转为jsonObject
            JSONObject result = (JSONObject) JSONObject.parseObject(String.valueOf(response));
            if (result.containsKey("token")) {
                token = result.getString("token");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return token;
    }

    public static CloseableHttpClient createSSLClientDefault() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                // 信任所有
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();
    }
}
