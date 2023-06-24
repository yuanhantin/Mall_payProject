package com.hanlinyuan.form;

import lombok.Data;

import javax.validation.constraints.NotNull;
/**
 * @Author: 翰林猿
 * @Description: 添加商品的表单
 **/

@Data
public class CartAddForm {

	@NotNull
	private Integer productId;

	private Boolean selected = true;
}
