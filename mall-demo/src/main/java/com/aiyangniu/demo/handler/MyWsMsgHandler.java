package com.aiyangniu.demo.handler;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;
import org.tio.websocket.server.handler.IWsMsgHandler;

/**
 * 消息处理类
 *
 * @author lzq
 * @date 2024/04/30
 */
@Component
public class MyWsMsgHandler implements IWsMsgHandler {

    /**
     * 对httpResponse参数进行补充并返回，如果返回null表示不想和对方建立连接，框架会断开连接，如果返回非null，框架会把这个对象发送给对方
     * 注：请不要在这个方法中向对方发送任何消息，因为这个时候握手还没完成，发消息会导致协议交互失败。
     * 对于大部分业务，该方法只需要一行代码：return httpResponse;
     */
    @Override
    public HttpResponse handshake(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        // 可以在此做一些业务逻辑，返回null表示不想连接
        return httpResponse;
    }

    /**
     * 握手成功后触发该方法
     */
    @Override
    public void onAfterHandshaked(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        // 拿到用户id
        String id = httpRequest.getParam("userId");
        // 绑定用户
        Tio.bindUser(channelContext, id);
        // 给用户发送消息
        JSONObject message = new JSONObject();
        message.put("msg", "连接成功...");
        message.put("sendName", "系统提醒");
        WsResponse wsResponse = WsResponse.fromText(message.toString(), "UTF-8");
        Tio.sendToUser(channelContext.tioConfig, id, wsResponse);
    }

    /**
     * 当收到Opcode.BINARY消息时，执行该方法。也就是说如果你的ws是基于BINARY传输的，就会走到这个方法
     *
     * @return 可以是WsResponse、byte[]、ByteBuffer、String或null，如果是null，框架不会回消息
     */
    @Override
    public Object onBytes(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        System.out.println("我走了onBytes");
        return null;
    }

    /**
     * 当收到Opcode.CLOSE时，执行该方法，业务层在该方法中一般不需要写什么逻辑，空着就好
     *
     * @return 可以是WsResponse、byte[]、ByteBuffer、String或null，如果是null，框架不会回消息
     */
    @Override
    public Object onClose(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        // 关闭连接
        Tio.remove(channelContext, "WebSocket Close");
        return null;
    }

    /**
     * 收到Opcode.TEXT消息时，执行该方法。也就是说如何你的ws是基于TEXT传输的，就会走到这个方法
     *
     * @return 可以是WsResponse、byte[]、ByteBuffer、String或null，如果是null，框架不会回消息
     */
    @Override
    public Object onText(WsRequest wsRequest, String text, ChannelContext channelContext) throws Exception {
        JSONObject message = JSONObject.parseObject(text);
        // 接收消息的用户ID
        String receiver = message.getString("receiver");
        // 发送消息者
        String sendName = message.getString("sendName");
        // 消息
        String msg = message.getString("msg");

        // 保存聊天记录到DB等业务逻辑...

        WsResponse wsResponse = WsResponse.fromText(message.toString(), "UTF-8");
        Tio.sendToUser(channelContext.tioConfig, receiver, wsResponse);

        JSONObject resp = new JSONObject();
        resp.put("sendName", "系统提醒");
        resp.put("msg", "发送成功");
        return resp.toString();
    }
}
