package com.aiyangniu.common.utils;

import cn.hutool.json.JSONUtil;
import nl.bitwalker.useragentutils.Browser;
import nl.bitwalker.useragentutils.OperatingSystem;
import nl.bitwalker.useragentutils.UserAgent;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 解析User-Agent工具类
 *
 * @author lzq
 * @date 2023/09/22
 */
public class AgentUtil {

    /**
     * Servlet获取客户端相关信息
     */
    public static String getAgentInfo(HttpServletRequest request) {
        // 解析agent字符串
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        // 获取浏览器对象
        Browser browser = userAgent.getBrowser();
        // 获取操作系统对象
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        // 组装返回值
        Map<String, Object> map = new HashMap<>();
        map.put("浏览器名", browser.getName());
        map.put("浏览器类型", browser.getBrowserType());
        map.put("浏览器家族", browser.getGroup());
        map.put("浏览器生产厂商", browser.getManufacturer());
        map.put("浏览器使用的渲染引擎", browser.getRenderingEngine());
        map.put("浏览器版本", userAgent.getBrowserVersion());
        map.put("操作系统名", operatingSystem.getName());
        map.put("访问设备类型", operatingSystem.getDeviceType());
        map.put("操作系统家族", operatingSystem.getGroup());
        map.put("操作系统生产厂商", operatingSystem.getManufacturer());
        return JSONUtil.toJsonStr(map);
    }
}
