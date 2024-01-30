package com.aiyangniu.common.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
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
     * 发送钉钉群消息
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
     * 发送钉钉群消息（可以艾特人）
     *
     * @param accessToken 群机器人accessToken
     * @param content 发送内容
     * @param atPhone 艾特人电话
     */
    public static void sendDingGroupMsgPhone(String accessToken, String secret, String content, String atPhone) {
        JSONObject text = new JSONObject();
        text.put("content", content);

        JSONObject at = new JSONObject();
        at.put("atMobiles", atPhone);
        at.put("isAtAll", false);

        JSONObject textMsg = new JSONObject();
        textMsg.put("msgtype", "text");
        textMsg.put("text", text);
        textMsg.put("at", at);

        dealDingMsgSendNew(accessToken, secret, textMsg.toJSONString());
    }

    /**
     * 发送钉钉群消息（link类型）
     *
     * @param accessToken 群机器人accessToken
     * @param title 消息标题
     * @param text 消息内容，如果太长只会部分展示
     * @param picUrl 图片URL
     * @param messageUrl 点击消息跳转的URL
     */
    public static void sendDingLinkGroupMsg(String accessToken, String secret, String title, String text, String picUrl, String messageUrl) {
        LOGGER.info("【发送钉钉群消息】正在发送link类型的钉钉消息......");
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
        LOGGER.info("【发送钉钉群消息】正在发送markdown类型的钉钉消息......");
        JSONObject markdown = new JSONObject();
        markdown.put("title", title);
        markdown.put("text", text);

        JSONObject at = new JSONObject();
        at.put("atMobiles", atMobiles);
        at.put("isAtAll", false);

        JSONObject textMsg = new JSONObject();
        textMsg.put("msgtype", "markdown");
        textMsg.put("markdown", markdown);
        textMsg.put("at", at);

        dealDingMsgSendNew(accessToken, secret, textMsg.toJSONString());
    }

    /**
     * 发送钉钉群消息（FeedCard类型）
     */
    public static void sendDingFeedCardGroupMsg(String accessToken, String secret, String title, String text, String singleTitle, String singleURL) {
        String textMsg = "{\"feedCard\":{\"links\":[{\"title\":\""+ title +"\",\"text\":\""+ text +"\",\"singleTitle\":\""+ singleTitle +"\",\"singleURL\":\""+ singleURL +"\"}," +
                "{\"title\":\""+ title +"\",\"text\":\""+ text +"\",\"singleTitle\":\""+ singleTitle +"\",\"singleURL\":\""+ singleURL +"\"}]},\"msgtype\":\"feedCard\"}";
        dealDingMsgSendNew(accessToken, secret, StringEscapeUtils.unescapeJava(textMsg));
    }

    /**
     * 整体跳转ActionCard类型
     */
    public static void sendDingActionCardGroupMsg(String accessToken, String secret, String title, String text, String singleTitle, String singleURL) {
        String textMsg = "{\n" +
                "    \"actionCard\": {\n" +
                "        \"title\": \""+title+"\", \n" +
                "        \"text\": \"![screenshot](https://mmbiz.qpic.cn/mmbiz_jpg/jyPD8edcUjEwQ1Tdotpq94VE4G1wIMAxQyI2Oe7RaDRT0iaBRD2KdOL0iaL56jBWQX5Fzq3S7R66pyuEIZW83Ulw/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)"+text+"\", \n" +
                "        \"hideAvatar\": \"0\", \n" +
                "        \"btnOrientation\": \"0\", \n" +
                "        \"singleTitle\" : \""+singleTitle+"\",\n" +
                "        \"singleURL\" : \""+singleURL+"\"\n" +
                "    }, \n" +
                "    \"msgtype\": \"actionCard\"\n" +
                "}";
        dealDingMsgSendNew(accessToken, secret, StringEscapeUtils.unescapeJava(textMsg));
    }

    /**
     * 独立跳转ActionCard类型
     */
    public static void sendDingActionCardGroupMsg2(String accessToken, String secret, String title, String text, String singleTitle, String singleURL) {
        String textMsg = "{\n" +
                "    \"actionCard\": {\n" +
                "        \"title\": \"乔布斯 20 年前想打造一间苹果咖啡厅，而它正是 Apple Store 的前身\", \n" +
                "        \"text\": \"![screenshot](@lADOpwk3K80C0M0FoA) \n" +
                " ### 乔布斯 20 年前想打造的苹果咖啡厅 \n" +
                " Apple Store 的设计正从原来满满的科技感走向生活化，而其生活化的走向其实可以追溯到 20 年前苹果一个建立咖啡馆的计划\", \n" +
                "        \"hideAvatar\": \"0\", \n" +
                "        \"btnOrientation\": \"0\", \n" +
                "        \"btns\": [\n" +
                "            {\n" +
                "                \"title\": \"内容不错\", \n" +
                "                \"actionURL\": \"https://www.dingtalk.com/\"\n" +
                "            }, \n" +
                "            {\n" +
                "                \"title\": \"不感兴趣\", \n" +
                "                \"actionURL\": \"https://www.dingtalk.com/\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }, \n" +
                "    \"msgtype\": \"actionCard\"\n" +
                "}\n";
        dealDingMsgSendNew(accessToken, secret, StringEscapeUtils.unescapeJava(textMsg));
    }

    /**
     * 调用钉钉官方接口发送钉钉消息（新版本，需要配置安全设置）
     */
    private static void dealDingMsgSendNew(String accessToken, String secret, String textMsg) {
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
