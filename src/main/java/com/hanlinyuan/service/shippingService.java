package com.hanlinyuan.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hanlinyuan.Enum.ResponseEnum;
import com.hanlinyuan.dao.ShippingMapper;
import com.hanlinyuan.form.shippingForm;
import com.hanlinyuan.pojo.Shipping;
import com.hanlinyuan.service.Imp.impShippingService;
import com.hanlinyuan.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 翰林猿
 * @Description: TODO
 **/
@Service
public class shippingService implements impShippingService {
    @Resource
    private ShippingMapper shippingMapper;

    @Override
    public ResponseVo<Map<String , Integer>> add(Integer uid , shippingForm shippingForm) {
        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(shippingForm,shipping);
        shipping.setUserId(uid);        //记得把uid也设置好
        int row = shippingMapper.insertSelective(shipping);
        if (row==0){
            return ResponseVo.error(ResponseEnum.ERROR);
        }
        HashMap<String,Integer> map = new HashMap<>();          //insertSelective
        map.put("shippingId",shipping.getId());
        return ResponseVo.success(map);
    }

    @Override
    public ResponseVo delete(Integer uid, Integer shippingId) {
        int row = shippingMapper.deleteByIdAndUid(uid, shippingId);
        if (row == 0) {
            return ResponseVo.error(ResponseEnum.DELETE_SHIPPING_FAIL);
        }

        return ResponseVo.success();
    }

    @Override
    public ResponseVo update(Integer uid, Integer shippingId, shippingForm form) {
        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(form, shipping);
        shipping.setUserId(uid);
        shipping.setId(shippingId);
        int row = shippingMapper.updateByPrimaryKeySelective(shipping);
        if (row == 0) {
            return ResponseVo.error(ResponseEnum.ERROR);
        }
        return ResponseVo.success();
    }

    @Override
    public ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippings = shippingMapper.selectByUid(uid);
        PageInfo pageInfo = new PageInfo(shippings);
        return ResponseVo.success(pageInfo);
    }
}
