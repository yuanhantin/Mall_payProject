package com.hanlinyuan.service.Imp;

import com.hanlinyuan.form.CartAddForm;
import com.hanlinyuan.form.CartUpdateForm;
import com.hanlinyuan.pojo.Cart;
import com.hanlinyuan.vo.CartProductVo;
import com.hanlinyuan.vo.CartVo;
import com.hanlinyuan.vo.ResponseVo;

import java.util.List;

/**
 * @Author: 翰林猿
 * @Description: 购物车接口
 **/

public interface impCartService {
    ResponseVo<CartVo>add(Integer uid , CartAddForm cartAddForm);
    ResponseVo<CartVo>list(Integer uid);
    ResponseVo<CartVo> update(Integer uid, Integer productId, CartUpdateForm form);//传入需要修改的参数，用updateform保存

    ResponseVo<CartVo> delete(Integer uid, Integer productId);

    ResponseVo<CartVo> selectAll(Integer uid);

    ResponseVo<CartVo> unSelectAll(Integer uid);

    ResponseVo<Integer> sum(Integer uid);

    List<Cart> listForCart(Integer uid);
}
