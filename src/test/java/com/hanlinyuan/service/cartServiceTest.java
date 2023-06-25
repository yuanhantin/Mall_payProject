package com.hanlinyuan.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hanlinyuan.form.CartAddForm;
import com.hanlinyuan.form.CartUpdateForm;
import com.hanlinyuan.pay_wechat.MallApplicationTests;
import com.hanlinyuan.vo.CartProductVo;
import com.hanlinyuan.vo.CartVo;
import com.hanlinyuan.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @Author: 翰林猿
 * @Description: TODO
 **/
@Slf4j
public class cartServiceTest extends MallApplicationTests {

    @Resource
    private cartService cartService;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    @Test
    public void add() {
        CartAddForm cartAddForm = new CartAddForm();
        cartAddForm.setProductId(29);
        cartAddForm.setSelected(true);
        ResponseVo<CartVo> responseVo = cartService.add(1, cartAddForm);
        log.info("list={} "+ gson.toJson(responseVo));
    }

    @Test
    public void list() {
        ResponseVo<CartVo> list = cartService.list(1);
        log.info("list={}", gson.toJson(list));
    }

    @Test
    public void update() {
        CartUpdateForm form = new CartUpdateForm();
        form.setQuantity(5);        //把26号product数量改为5个
        form.setSelected(false);
        ResponseVo<CartVo> responseVo = cartService.update(1, 26, form);
        log.info("result={}", gson.toJson(responseVo));
    }

    @Test
    public void delete() {
        log.info("【删除购物车...】");
        ResponseVo<CartVo> responseVo = cartService.delete(1, 26);
        log.info("result={}", gson.toJson(responseVo));
    }
}