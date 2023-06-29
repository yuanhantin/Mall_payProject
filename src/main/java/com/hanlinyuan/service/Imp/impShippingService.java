package com.hanlinyuan.service.Imp;

import com.github.pagehelper.PageInfo;
import com.hanlinyuan.form.shippingForm;
import com.hanlinyuan.pojo.Shipping;
import com.hanlinyuan.vo.ResponseVo;

import java.util.Map;

/**
 * @Author: 翰林猿
 * @Description: TODO
 **/
public interface impShippingService {
    ResponseVo<Map<String , Integer>> add(Integer uid , shippingForm shippingForm);

    ResponseVo delete(Integer uid, Integer shippingId);

    ResponseVo update(Integer uid, Integer shippingId, shippingForm form);

    ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize);
}
