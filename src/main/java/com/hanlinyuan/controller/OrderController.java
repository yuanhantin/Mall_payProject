package com.hanlinyuan.controller;

import com.github.pagehelper.PageInfo;

import com.hanlinyuan.form.OrderCreateForm;
import com.hanlinyuan.pojo.User;
import com.hanlinyuan.service.Imp.IOrderService;
import com.hanlinyuan.vo.OrderVo;
import com.hanlinyuan.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static com.hanlinyuan.Const.mallConst.CURRENT_USER;


@RestController
public class OrderController {

	@Autowired
	private IOrderService orderService;

	@PostMapping("/orders")
	public ResponseVo<OrderVo> create(@Valid @RequestBody OrderCreateForm form,
									  HttpSession session) {
		User user = (User) session.getAttribute(CURRENT_USER);
		return orderService.create(user.getId(), form.getShippingId());
	}

	@GetMapping("/orders")
	public ResponseVo<PageInfo> list(@RequestParam Integer pageNum,
									 @RequestParam Integer pageSize,
									 HttpSession session) {
		User user = (User) session.getAttribute(CURRENT_USER);
		return orderService.list(user.getId(), pageNum, pageSize);
	}

	@GetMapping("/orders/{orderNo}")
	public ResponseVo<OrderVo> detail(@PathVariable Long orderNo,
									  HttpSession session) {
		User user = (User) session.getAttribute(CURRENT_USER);
		return orderService.detail(user.getId(), orderNo);
	}

	@PutMapping("/orders/{orderNo}")
	public ResponseVo cancel(@PathVariable Long orderNo,
							 HttpSession session) {
		User user = (User) session.getAttribute(CURRENT_USER);
		return orderService.cancel(user.getId(), orderNo);
	}
}
