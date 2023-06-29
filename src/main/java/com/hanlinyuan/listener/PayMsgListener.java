package com.hanlinyuan.listener;

import com.google.gson.Gson;
import com.hanlinyuan.pojo.PayInfo;
import com.hanlinyuan.service.Imp.IOrderService;
import lombok.experimental.FieldNameConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @Author: 翰林猿
 * @Description: 接收rabbitMQ的数据
 **/

@Component
@RabbitListener(queues = "payNotify")   //接收来自payNotify队列的消息
@Slf4j
public class PayMsgListener {
    Gson gson = new Gson();
    @Autowired
    private IOrderService orderService;
    @RabbitHandler
    public void process(String msg){
        log.info("收到消息---》{}",msg);
        PayInfo payInfo = gson.fromJson(msg, PayInfo.class);

        if(payInfo.getPlatformStatus().equals("SUCCESS")){
            //修改订单状态为已支付
            orderService.paid(payInfo.getOrderNo());
        }
    }
}
