package com.hanlinyuan.service;

import com.google.gson.Gson;
import com.hanlinyuan.Enum.PayPlatformEnum;
import com.hanlinyuan.dao.PayInfoMapper;
import com.hanlinyuan.pojo.PayInfo;
import com.hanlinyuan.service.Imp.impPayService;
import com.lly835.bestpay.enums.BestPayPlatformEnum;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.enums.OrderStatusEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @Author: 翰林猿
 * @Description: TODO
 **/
@Slf4j
@Service
public class payService implements impPayService {

    private final static String QUEUE_PAY_NOTIFY = "payNotify";
    @Resource
    private BestPayService bestPayService;

    @Resource
    private PayInfoMapper payInfoMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;
    @Override
    public PayResponse createPrePayTrade(String orderId, BigDecimal amount, BestPayTypeEnum bestPayTypeEnum) {
        //1.将支付的信息插入数据库的payInfo表
        PayInfo payInfo = new PayInfo(Long.parseLong(orderId),
                PayPlatformEnum.getByBestPayTypeEnum(bestPayTypeEnum).getCode(),
                OrderStatusEnum.NOTPAY.name(),
                amount);
        payInfoMapper.insertSelective(payInfo);

        //2.在发起支付之前配置需要的payRequest（订单的名字id金额内容）
        PayRequest payRequest = new PayRequest();
        payRequest.setOrderName("微信公众账号支付订单2");
        payRequest.setOrderId(orderId);
        payRequest.setOrderAmount(amount.doubleValue());
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_NATIVE);
        //3.发起支付，会从微信那获得一个回应，后续用于获取并生成付款的二维码
        PayResponse response = bestPayService.pay(payRequest);
        return response;
    }


    @Override
    public String asyncNotify(String notifyData) {
        //1. 签名检验,通过SDK的API，接受来自微信的异步返回通知，用于确定是否真的成功支付。
        PayResponse payResponse = bestPayService.asyncNotify(notifyData);
        log.info("异步通知 response={}", payResponse);

        //2. 金额校验
        //如果不对账是比较严重的（正常情况下是不会发生的）在企业中应该给开发人员发出告警比如钉钉、短信等方式

        //从数据库通过orderID查订单
        PayInfo payInfo = payInfoMapper.selectByOrderNo(Long.parseLong(payResponse.getOrderId()));
        if (payInfo == null) {
            throw new RuntimeException("通过orderNo查询到的结果是null");
        }
        //如果从数据库里查出来的订单是还没有支付，先校验金额是否与之前在数据库里存的一致，如果没问题了，就把数据库里的订单修改成已支付。
        if (!payInfo.getPlatformStatus().equals(OrderStatusEnum.SUCCESS.name())){
            //判断金额是否正确
            if (payInfo.getPayAmount().compareTo(BigDecimal.valueOf(payResponse.getOrderAmount()))!=0){
                throw new RuntimeException("异步通知返回的数据金额与数据库不一致");
            }
            //如果金额正确，修改订单支付状态成已支付，并update一下
            payInfo.setPlatformStatus(OrderStatusEnum.SUCCESS.name());
            payInfo.setPlatformNumber(payResponse.getOutTradeNo());     //获取交易流水号插入进去
            payInfoMapper.updateByPrimaryKeySelective(payInfo);

        }

        // pay发送MQ消息，mall接受MQ消息,将订单对象payInfo转成json格式（方便在mq里查看）再放入队列中
        String payInfoJson = new Gson().toJson(payInfo);
        amqpTemplate.convertAndSend(QUEUE_PAY_NOTIFY,payInfoJson);   //s：队列的名字 o：消息

        //支付成功之后，微信会一直隔几秒就给你发消息确认 ，可以告诉微信不要再发了，
        if (payResponse.getPayPlatformEnum() == BestPayPlatformEnum.WX) {
            return "<xml>\n" +
                    "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                    "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                    "</xml>";
            //支付宝的话则是返回success，但是本项目不使用。
        }else if (payResponse.getPayPlatformEnum() == BestPayPlatformEnum.ALIPAY) {
            return "success";
        }
        throw new RuntimeException("异步通知中错误的支付平台");
    }

    @Override
    @Transactional
    public PayInfo queryByOrderId(String orderId) {
        return payInfoMapper.selectByOrderNo(Long.valueOf(orderId));
    }
}
