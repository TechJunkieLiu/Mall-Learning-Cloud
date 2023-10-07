package com.aiyangniu.gate.component;

import com.aiyangniu.gate.service.OmsGateOrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 取消订单消息的接收者
 *
 * @author lzq
 * @date 2023/10/07
 */
@Component
@RabbitListener(queues = "mall.order.cancel")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CancelOrderReceiver {

    private final OmsGateOrderService omsGateOrderService;
    private static final Logger LOGGER = LoggerFactory.getLogger(CancelOrderReceiver.class);

    @RabbitHandler
    public void handle(Long orderId){
        omsGateOrderService.cancelOrder(orderId);
        LOGGER.info("process orderId:{}", orderId);
    }
}
