package com.hanlinyuan.Contorller;

import com.hanlinyuan.pojo.PayInfo;
import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.enums.BestPayPlatformEnum;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * @Author: 翰林猿
 * @Description: TODO
 **/

@Controller
@RequestMapping("/pay")
public class payController {
    @Autowired
    com.hanlinyuan.service.payService payService;
    @Autowired
    WxPayConfig wxPayConfig;
    @GetMapping("/create")
    public ModelAndView createPayService(@RequestParam("orderId") String orderId, @RequestParam("amount") BigDecimal amount, @RequestParam("payType")BestPayTypeEnum bestPayTypeEnum){
        PayResponse payResponse = payService.createPrePayTrade(orderId, amount, bestPayTypeEnum);
        String codeUrl = payResponse.getCodeUrl();

        HashMap codeUrlMap = new HashMap<>();
        codeUrlMap.put("codeUrl",codeUrl);
        codeUrlMap.put("orderId", orderId);
        codeUrlMap.put("returnUrl", wxPayConfig.getReturnUrl());
        return new ModelAndView("create",codeUrlMap);
    }

    //在支付了二维码之后，微信会访问你给的异步通知网址，携带信息给你用于确认是否支付成功
    @PostMapping("/notify")
    @ResponseBody
    public String asyncNotify(@RequestBody String notifyData) {
        return payService.asyncNotify(notifyData);
    }

    @GetMapping("/queryByOrderId")
    @ResponseBody       //在controller下，默认返回的是一个视图，所以如果要返回一个对象的话，要使用ResponseBody将其转成json格式，前端才能获取一个对象
    public PayInfo queryByOrderId(@RequestParam("orderId") String orderId){
        return payService.queryByOrderId(orderId);
    }

    @GetMapping("/test")
    public ModelAndView test(){
        return new ModelAndView("test");
    }
}
