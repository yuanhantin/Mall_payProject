package com.hanlinyuan.form;

import lombok.Data;

/**
 * @Author: 翰林猿
 * @Description: 更新商品的数据传输对象（DTO）Data Transfer Object
 **/
@Data
public class CartUpdateForm {

	private Integer quantity;

	private Boolean selected;
}
