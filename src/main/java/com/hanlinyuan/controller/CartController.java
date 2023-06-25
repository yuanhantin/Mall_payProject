package com.hanlinyuan.controller;


import com.hanlinyuan.form.CartAddForm;
import com.hanlinyuan.vo.CartProductVo;
import com.hanlinyuan.vo.ResponseVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
public class CartController {
	@PostMapping("/carts")
	public ResponseVo<CartProductVo> add(@Valid @RequestBody CartAddForm cartAddForm) {
		return null;
	}
}
