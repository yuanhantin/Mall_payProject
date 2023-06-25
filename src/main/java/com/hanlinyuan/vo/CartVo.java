package com.hanlinyuan.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: 翰林猿
 * @Description: 购物车list的Vo类
 **/
@Data
public class CartVo {

	private List<CartProductVo> cartProductVoList;

	private Boolean selectedAll;

	private BigDecimal cartTotalPrice;

	private Integer cartTotalQuantity;
}
