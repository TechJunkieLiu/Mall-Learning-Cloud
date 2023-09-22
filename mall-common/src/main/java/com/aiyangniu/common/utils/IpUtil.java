package com.aiyangniu.common.utils;

import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 获取IP工具类
 *
 * @author lzq
 * @date 2023/09/22
 */
public class IpUtil {

    private static final String POINT = ",";
    private static final String WINDOWS = "Windows";
    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST_IPV4  = "127.0.0.1";
    private static final String LOCALHOST_IPV6  = "0:0:0:0:0:0:0:1";
    private static final Logger LOGGER = LoggerFactory.getLogger(IpUtil.class);

    /**
     * 获取用户真实IP地址
     *
     * 不使用request.getRemoteAddr()的原因是有可能用户使用了代理软件方式避免真实IP地址
     * 如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，取X-Forwarded-For中第一个非unknown的有效IP字符串
     */
    public static String getIpAddress(HttpServletRequest request) {
        // 通过HTTP代理服务器转发时添加
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            // 从本地访问时根据网卡取本机配置的IP
            if (LOCALHOST_IPV4.equals(ipAddress) || LOCALHOST_IPV6.equals(ipAddress)) {
                InetAddress inetAddress = null;
                try {
                    inetAddress = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                assert inetAddress != null;
                ipAddress = inetAddress.getHostAddress();
            }
        }
        // 通过多个代理转发的情况，第一个IP为客户端真实IP，多个IP会按照','分割
        if (ipAddress != null && ipAddress.length() > 15) {
            if (ipAddress.indexOf(POINT) > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

    /**
     * 获取客户端IP
     *
     * 支持反向代理，比如 Nginx，但不支持正向代理，比如客户端浏览器自己使用代理工具
     * 场景：向不同省份的用户展示不同的内容
     */
    public static String getClientIP(HttpServletRequest request){
        String xip = request.getHeader("X-Real-IP");
        String xFor = request.getHeader("X-Forwarded-For");
        if (StrUtil.isNotEmpty(xFor) && !UNKNOWN.equalsIgnoreCase(xFor)){
            // 多次反向代理后会有多个IP值，第一个IP才是真实IP（X-Forwarded-For: client, proxy1, proxy2, proxy...）
            int index = xFor.indexOf(POINT);
            if (index != -1){
                return xFor.substring(0, index);
            }else {
                return xFor;
            }
        }
        xFor = xip;
        if (StrUtil.isNotEmpty(xFor) && !UNKNOWN.equalsIgnoreCase(xFor)){
            return xFor;
        }
        if (StrUtil.isBlank(xFor) || UNKNOWN.equalsIgnoreCase(xFor)){
            xFor = request.getHeader("Proxy-Client-IP");
        }
        if (StrUtil.isBlank(xFor) || UNKNOWN.equalsIgnoreCase(xFor)){
            xFor = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StrUtil.isBlank(xFor) || UNKNOWN.equalsIgnoreCase(xFor)){
            xFor = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StrUtil.isBlank(xFor) || UNKNOWN.equalsIgnoreCase(xFor)){
            xFor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StrUtil.isBlank(xFor) || UNKNOWN.equalsIgnoreCase(xFor)){
            xFor = request.getRemoteAddr();
        }
        return xFor;
    }

    /**
     * 获取本机IP地址
     */
    public static String getLocalIpAddress() throws Exception {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (allNetInterfaces.hasMoreElements()){
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                // 对网络接口进行筛选排除：回送接口、虚拟网卡、未在使用中
                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()){
                    continue;
                }else {
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()){
                        ip = addresses.nextElement();
                        if (ip != null && ip instanceof Inet4Address){
                            return ip.getHostAddress();
                        }
                    }
                }
            }
        }catch (Exception e){
            LOGGER.error("IP地址获取失败：{}", e.toString());
        }
        return "";
    }

    /**
     * 获取MAC地址
     */
    public static String getMac(String ip) throws IOException {
        String os = System.getProperty("os.name");
        if (os.startsWith(WINDOWS)) {
            return getMacForWindows(ip);
        }else {
            return getMacForLinux(ip);
        }
    }

    /**
     * 从Windows机器上获取MAC地址
     */
    public static String getMacForWindows(final String ip) {
        String result = "";
        String[] cmd = {"cmd", "/c", "ping " + ip};
        String[] another = {"cmd", "/c", "ipconfig -all"};
        // 获取执行命令后的result
        String cmdResult = callCmd(cmd, another);
        // 从上一步的结果中获取Mac地址
        result = filterMacAddress(ip, cmdResult, "-");
        return result;
    }

    /**
     * 从类Unix机器上获取MAC地址
     */
    public static String getMacForLinux(final String ip) throws IOException {
        String macAddress = "";
        if (ip != null) {
            try {
                Process process = Runtime.getRuntime().exec("arp "+ip);
                InputStreamReader ir = new InputStreamReader(process.getInputStream());
                LineNumberReader input = new LineNumberReader(ir);
                String line;
                StringBuffer sb = new StringBuffer();
                while ((line = input.readLine()) != null) {
                    sb.append(line);
                }
                macAddress = sb.toString();
                if (StrUtil.isNotBlank(macAddress)) {
                    macAddress = macAddress.substring(macAddress.indexOf(":") - 2, macAddress.lastIndexOf(":") + 3);
                }
                return macAddress;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return macAddress;
    }

    /**
     * 命令执行
     */
    public static String callCmd(String[] cmd, String[] another) {
        String result = "";
        String line = "";
        try {
            Runtime rt = Runtime.getRuntime();
            // 执行第一个命令
            Process proc = rt.exec(cmd);
            proc.waitFor();
            // 执行第二个命令
            proc = rt.exec(another);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);
            while ((line = br.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取MAC地址
     */
    public static String filterMacAddress(final String ip, final String sourceString, final String macSeparator) {
        String result = "";
        String regExp = "((([0-9,A-F,a-f]{1,2}" + macSeparator + "){1,5})[0-9,A-F,a-f]{1,2})";
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(sourceString);
        while (matcher.find()) {
            result = matcher.group(1);
            // 因计算机多网卡问题，截取紧靠IP后的第一个Mac地址
            int num = sourceString.indexOf(ip) - sourceString.indexOf(": " + result + " ");
            if (num > 0 && num < 300) {
                break;
            }
        }
        return result;
    }
}
