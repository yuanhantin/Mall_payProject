package com.hanlinyuan.service.Imp;

import com.hanlinyuan.pojo.PayInfo;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;

import java.math.BigDecimal;

/**
 * @Author: 翰林猿
 * @Description: TODO
 **/
public interface impPayService {
    /**
     * @param order  订单
     * @param amount 交易金额
     * @return
     */
    PayResponse createPrePayTrade(String order , BigDecimal amount, BestPayTypeEnum bestPayTypeEnum);

    public String asyncNotify(String notifyData);

    public PayInfo queryByOrderId(String orderId);
}
