package com.aiyangniu.common.utils.HttpUtils;

import org.springframework.lang.Nullable;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * 通过JDK网络类Java.net.HttpURLConnection调用第三方http接口（比较原始）
 *
 * @author lzq
 * @date 2024/05/06
 */
public class HttpUrlConnectionUtil {

    public static String doGet(String httpUrl){
        InputStream is = null;
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            // 创建连接
            URL url = new URL(httpUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置请求方式
            conn.setRequestMethod("GET");
            // 设置15秒连接超时时间
            conn.setConnectTimeout(15000);
            // 设置30秒读取超时时间
            conn.setReadTimeout(30000);
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            // 开始连接
            conn.connect();
            // 获取响应数据
            if (conn.getResponseCode() == 200) {
                // 获取返回的数据
                is = conn.getInputStream();
                if (null != is) {
                    br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                    String temp;
                    while (null != (temp = br.readLine())) {
                        sb.append(temp);
                    }
                }
            }
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static String doPost(String httpUrl, @Nullable String param) {
        OutputStream os = null;
        InputStream is = null;
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            // 创建连接对象
            URL url = new URL(httpUrl);
            // 创建连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置请求方法
            conn.setRequestMethod("POST");
            // 设置15秒连接超时时间
            conn.setConnectTimeout(15000);
            // 设置30秒读取超时时间
            conn.setReadTimeout(30000);
            // 设置是否可读取(是否向httpUrlConnection输出-默认false、是否从httpUrlConnection读入-默认true)
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // Post请求不能使用缓存-默认true
            conn.setUseCaches(false);
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            // 拼装参数
            if ("".equals(param)) {
                // 设置参数
                os = conn.getOutputStream();
                // 拼装参数
                os.write(param.getBytes(StandardCharsets.UTF_8));
            }

            // 设置权限
//            String authString = "username:password";
//            String authStringEnc = Base64.getEncoder().encodeToString(authString.getBytes());
//            conn.setRequestProperty("Authorization", "Basic " + authStringEnc);
            // 设置请求头
//            conn.setRequestProperty("Header-Name", "Header-Value");

            // 开启连接
            conn.connect();
            // 读取响应
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                if (null != is) {
                    br = new BufferedReader(new InputStreamReader(is, "GBK"));
                    String temp;
                    while (null != (temp = br.readLine())) {
                        sb.append(temp);
                        sb.append("\r\n");
                    }
                }
            }
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}
