package com.hanlinyuan.controller;


import com.hanlinyuan.form.shippingForm;
import com.hanlinyuan.pojo.User;
import com.hanlinyuan.service.shippingService;
import com.hanlinyuan.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static com.hanlinyuan.Const.mallConst.CURRENT_USER;

@RestController
public class ShippingController {

	@Autowired
	private shippingService shippingService;

	@PostMapping("/shippings")
	public ResponseVo add(@Valid @RequestBody shippingForm form,
						  HttpSession session) {
		User user = (User) session.getAttribute(CURRENT_USER);
		return shippingService.add(user.getId(), form);
	}

	@DeleteMapping("/shippings/{shippingId}")
	public ResponseVo delete(@PathVariable Integer shippingId,
							 HttpSession session) {
		User user = (User) session.getAttribute(CURRENT_USER);
		return shippingService.delete(user.getId(), shippingId);
	}

	@PutMapping("/shippings/{shippingId}")
	public ResponseVo update(@PathVariable Integer shippingId,
							 @Valid @RequestBody shippingForm form,
							 HttpSession session) {
		User user = (User) session.getAttribute(CURRENT_USER);
		return shippingService.update(user.getId(), shippingId, form);
	}

	@GetMapping("/shippings")
	public ResponseVo list(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
						   @RequestParam(required = false, defaultValue = "10") Integer pageSize,
						   HttpSession session) {
		User user = (User) session.getAttribute(CURRENT_USER);
		return shippingService.list(user.getId(), pageNum, pageSize);
	}
}
