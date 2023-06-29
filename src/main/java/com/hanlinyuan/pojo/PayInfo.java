package com.hanlinyuan.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: 翰林猿
 * @Description: 这个对象其实是 pay项目里的，正确的使用姿势应该是由pay项目将其打包成一个jar包，发给mall项目使用。这里为了方便就直接复制了。
 **/
@Data
public class PayInfo {
    private Integer id;

    private Integer userId;

    private Long orderNo;

    private Integer payPlatform;

    private String platformNumber;

    private String platformStatus;

    private BigDecimal payAmount;

    private Date createTime;

    private Date updateTime;

    public PayInfo(Long orderNo, Integer payPlatform, String platformStatus, BigDecimal payAmount) {
        this.orderNo = orderNo;
        this.payPlatform = payPlatform;
        this.platformStatus = platformStatus;
        this.payAmount = payAmount;
    }
}