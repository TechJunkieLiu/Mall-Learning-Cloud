package com.aiyangniu.demo.controller;

import com.aiyangniu.common.enums.DingMsgPhoneEnum;
import com.aiyangniu.common.enums.DingTokenEnum;
import com.aiyangniu.common.utils.DingTalkUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * 钉钉消息发送测试类
 *
 * @author lzq
 * @date 2024/01/30
 */
@Slf4j
@Api(value = "DingTalkController", tags = "钉钉消息发送测试类")
@RestController
@RequestMapping("/ding")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DingTalkController {

    @Value("${spring.profiles.active}")
    private String profile;

    private static final Logger LOGGER = LoggerFactory.getLogger(DingTalkController.class);

    @ApiOperation("发送钉钉群消息（可以艾特人）")
    @PostMapping(value = "/sendDingMsg")
    public void sendDingMsg(@RequestParam String content){

        DingTalkUtil.sendDingGroupMsg(DingTokenEnum.SEND_SMS_BY_DEVELOPER_TOKEN.getToken(), DingTokenEnum.SEND_SMS_BY_DEVELOPER_TOKEN.getSecret(), content);

        DingTalkUtil.sendDingGroupMsgPhone(DingTokenEnum.SEND_SMS_BY_DEVELOPER_TOKEN.getToken(), DingTokenEnum.SEND_SMS_BY_DEVELOPER_TOKEN.getSecret(), content, DingMsgPhoneEnum.DEVELOPER_PHONE.getPhone());

        DingTalkUtil.sendDingGroupMsgPhone(DingTokenEnum.SEND_SMS_BY_DEVELOPER_TOKEN.getToken(), DingTokenEnum.SEND_SMS_BY_DEVELOPER_TOKEN.getSecret(), "【系统消息】钉钉消息推送测试，by:lzq......", DingMsgPhoneEnum.DEVELOPER_PHONE.getPhone());

        DingTalkUtil.sendDingGroupMsgPhone(DingTokenEnum.SEND_SMS_BY_DEVELOPER_TOKEN.getToken(), DingTokenEnum.SEND_SMS_BY_DEVELOPER_TOKEN.getSecret(), "【系统消息】"+ profile +"环境，XXX任务开始执行......", DingMsgPhoneEnum.DEVELOPER_PHONE.getPhone());

        DingTalkUtil.sendDingLinkGroupMsg(DingTokenEnum.SEND_SMS_BY_DEVELOPER_TOKEN.getToken(), DingTokenEnum.SEND_SMS_BY_DEVELOPER_TOKEN.getSecret(), "【系统消息】", "我喜欢，驾驭着代码在风驰电掣中创造完美！我喜欢，操纵着代码在随心所欲中体验生活！我喜欢，书写着代码在时代浪潮中完成经典！每一段新的代码在我手中诞生对我来说就象观看刹那花开的感动！", "https://crawl.nosdn.127.net/img/61c1272596da591797928931bd694930.jpg", "https://open.dingtalk.com");

        String text = "#### 杭州天气 @138*****41\n> 9度，西北风1级，空气良89，相对温度73%\n\n> ![screenshot](http://i.weather.com.cn/images/cn/news/2019/10/23/1571815463327062988.jpg)\n> ###### 10点20分发布 [天气](http://www.weather.com.cn/weather/101210101.shtml)";
        DingTalkUtil.sendDingMarkdownGroupMsg(DingTokenEnum.SEND_SMS_BY_MARKDOWN_TOKEN.getToken(), DingTokenEnum.SEND_SMS_BY_MARKDOWN_TOKEN.getSecret(), "杭州天气", text, "[13853740741,15695357257,15047968011]");

        DingTalkUtil.sendDingFeedCardGroupMsg(DingTokenEnum.SEND_SMS_BY_DEVELOPER_TOKEN.getToken(), DingTokenEnum.SEND_SMS_BY_DEVELOPER_TOKEN.getSecret(), "【系统消息】", "123", "阅读全文1", "https://www.dingtalk.com");

        DingTalkUtil.sendDingActionCardGroupMsg(DingTokenEnum.SEND_SMS_BY_DEVELOPER_TOKEN.getToken(), DingTokenEnum.SEND_SMS_BY_DEVELOPER_TOKEN.getSecret(), "【系统消息】", "456", "阅读全文2", "https://www.dingtalk.com");

        DingTalkUtil.sendDingActionCardGroupMsg2(DingTokenEnum.SEND_SMS_BY_DEVELOPER_TOKEN.getToken(), DingTokenEnum.SEND_SMS_BY_DEVELOPER_TOKEN.getSecret(), "【系统消息】", "789", "阅读全文3", "https://www.dingtalk.com");

        LOGGER.info("发送钉钉群消息成功（如果是回调函数则使用JSONObject接收参数）");
    }
}
