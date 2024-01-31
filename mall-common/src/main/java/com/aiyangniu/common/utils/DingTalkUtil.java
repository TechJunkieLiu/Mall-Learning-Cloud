package com.aiyangniu.common.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.util.Arrays;

/**
 * 钉钉消息发送工具类
 * 机器人发送消息频率限制：每个机器人每分钟最多发送20条，如果超过20条，会限流10分钟
 *
 * @author lzq
 * @date 2024/01/30
 */
public class DingTalkUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DingTalkUtil.class);

    /**
     * 发送钉钉群消息（text文本）
     */
    public static void sendDingGroupMsg(String accessToken, String secret, String content) {
        JSONObject text = new JSONObject();
        text.put("content", content);
        JSONObject textMsg = new JSONObject();
        textMsg.put("msgtype", "text");
        textMsg.put("text", text);
        dealDingMsgSendNew(accessToken, secret, textMsg.toJSONString());
    }

    /**
     * 发送钉钉群消息（text文本-可以艾特人）
     *
     * @param accessToken 群机器人accessToken
     * @param content 发送内容
     * @param atMobiles 艾特人电话
     */
    public static void sendDingGroupMsgPhone(String accessToken, String secret, String content, String atMobiles) {
        JSONObject text = new JSONObject();
        text.put("content", content);
        JSONObject at = new JSONObject();
        at.put("atMobiles", Arrays.asList(atMobiles.split(",")));
        at.put("isAtAll", false);
        JSONObject textMsg = new JSONObject();
        textMsg.put("msgtype", "text");
        textMsg.put("text", text);
        textMsg.put("at", at);
        dealDingMsgSendNew(accessToken, secret, textMsg.toJSONString());
    }

    /**
     * 发送钉钉群消息（link链接）
     *
     * @param accessToken 群机器人accessToken
     * @param title 消息标题
     * @param text 消息内容，如果太长只会部分展示
     * @param picUrl 图片URL
     * @param messageUrl 点击消息跳转的URL
     */
    public static void sendDingLinkGroupMsg(String accessToken, String secret, String title, String text, String picUrl, String messageUrl) {
        JSONObject link = new JSONObject();
        link.put("text", text);
        link.put("title", title);
        link.put("picUrl", picUrl);
        link.put("messageUrl", messageUrl);
        JSONObject textMsg = new JSONObject();
        textMsg.put("msgtype", "link");
        textMsg.put("link", link);
        dealDingMsgSendNew(accessToken, secret, textMsg.toJSONString());
    }

    /**
     * 发送钉钉群消息（markdown类型）
     *
     * @param accessToken 群机器人accessToken
     * @param secret 密钥，机器人安全设置页面，加签一栏下面显示的SEC开头的字符
     * @param title  消息标题
     * @param text  消息内容，如果太长只会部分展示
     * @param atMobiles 艾特人电话
     */
    public static void sendDingMarkdownGroupMsg(String accessToken, String secret, String title, String text, String atMobiles) {
        JSONObject markdown = new JSONObject();
        markdown.put("title", title);
        markdown.put("text", text);
        JSONObject at = new JSONObject();
        at.put("atMobiles", Arrays.asList(atMobiles.split(",")));
        at.put("isAtAll", false);
        JSONObject textMsg = new JSONObject();
        textMsg.put("msgtype", "markdown");
        textMsg.put("markdown", markdown);
        textMsg.put("at", at);
        dealDingMsgSendNew(accessToken, secret, textMsg.toJSONString());
    }

    /**
     * 发送钉钉群消息（feedCard类型）
     */
    public static void sendDingFeedCardGroupMsg(String accessToken, String secret, String title, String messageUrl, String picUrl) {
        JSONObject link1 = new JSONObject();
        link1.put("title", title);
        link1.put("messageURL", messageUrl);
        link1.put("picURL", picUrl);
        JSONObject link2 = new JSONObject();
        link2.put("title", title);
        link2.put("messageURL", messageUrl);
        link2.put("picURL", picUrl);
        JSONArray links = new JSONArray();
        links.add(link1);
        links.add(link2);
        JSONObject feedCard = new JSONObject();
        feedCard.put("links", links);
        JSONObject textMsg = new JSONObject();
        textMsg.put("msgtype", "feedCard");
        textMsg.put("feedCard", feedCard);
        dealDingMsgSendNew(accessToken, secret, textMsg.toJSONString());
    }

    /**
     * 整体跳转ActionCard类型
     */
    public static void sendDingActionCardGroupMsg(String accessToken, String secret, String title, String text, String singleTitle, String singleURL) {
        JSONObject actionCard = new JSONObject();
        actionCard.put("title", title);
        actionCard.put("text", text);
        actionCard.put("hideAvatar", "0");
        actionCard.put("btnOrientation", "0");
        actionCard.put("singleTitle", singleTitle);
        actionCard.put("singleURL", singleURL);
        JSONObject textMsg = new JSONObject();
        textMsg.put("msgtype", "actionCard");
        textMsg.put("actionCard", actionCard);
        dealDingMsgSendNew(accessToken, secret, textMsg.toJSONString());
    }

    /**
     * 独立跳转ActionCard类型
     */
    public static void sendDingActionCardGroupMsg2(String accessToken, String secret, String title, String text, String singleTitle1, String singleTitle2, String singleUrl1, String singleUrl2) {
        JSONObject btns1 = new JSONObject();
        btns1.put("title", singleTitle1);
        btns1.put("actionURL", singleUrl1);
        JSONObject btns2 = new JSONObject();
        btns2.put("title", singleTitle2);
        btns2.put("actionURL", singleUrl2);
        JSONArray btns = new JSONArray();
        btns.add(btns1);
        btns.add(btns2);
        JSONObject actionCard = new JSONObject();
        actionCard.put("title", title);
        actionCard.put("text", text);
        actionCard.put("hideAvatar", "0");
        actionCard.put("btnOrientation", "0");
        actionCard.put("btns", btns);
        JSONObject textMsg = new JSONObject();
        textMsg.put("msgtype", "actionCard");
        textMsg.put("actionCard", actionCard);
        dealDingMsgSendNew(accessToken, secret, textMsg.toJSONString());
    }

    /**
     * 调用钉钉官方接口发送钉钉消息（新版本，需要配置安全设置）
     */
    private static void dealDingMsgSendNew(String accessToken, String secret, String textMsg) {
        LOGGER.info("【发送钉钉群消息】正在发送钉钉群消息......");
        Long timestamp = System.currentTimeMillis();
        String sign = getSign(secret, timestamp);
        String url = "https://oapi.dingtalk.com/robot/send?access_token=" + accessToken + "&timestamp=" + timestamp + "&sign=" + sign;
        try {
            LOGGER.info("【发送钉钉群消息】请求参数：url = {}, textMsg = {}", url, textMsg);
            String res = HttpUtil.post(url, textMsg);
            LOGGER.info("【发送钉钉群消息】消息响应结果：" + res);
        } catch (Exception e) {
            LOGGER.warn("【发送钉钉群消息】请求钉钉接口异常，errMsg = {}", e);
        }
    }

    /**
     * 调用钉钉官方接口发送钉钉消息（旧版本，废弃，不需要配置安全设置）
     */
    private static void dealDingMsgSend(String accessToken, String textMsg) {
        HttpClient httpclient = HttpClients.createDefault();
        String webHookToken = "https://oapi.dingtalk.com/robot/send?access_token=" + accessToken;
        HttpPost httppost = new HttpPost(webHookToken);
        httppost.addHeader("Content-Type", "application/json; charset=utf-8");
        StringEntity stringEntity = new StringEntity(textMsg, "utf-8");
        httppost.setEntity(stringEntity);
        try {
            HttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                String result = EntityUtils.toString(response.getEntity(), "utf-8");
                LOGGER.info("【发送钉钉群消息】消息响应结果：" + JSON.toJSONString(result));
            }
        } catch (Exception e) {
            LOGGER.error("【发送钉钉群消息】error：" + e.getMessage(), e);
        }
    }

    /**
     * 计算签名
     */
    private static String getSign(String secret, Long timestamp){
        try {
            String stringToSign = timestamp + "\n" + secret;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
            String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)),"UTF-8");
            LOGGER.info("【发送钉钉群消息】获取到签名sign = {}", sign);
            return sign;
        } catch (Exception e) {
            LOGGER.error("【发送钉钉群消息】计算签名异常，errMsg = {}", e);
            return null;
        }
    }
}
