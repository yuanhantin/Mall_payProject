package com.hanlinyuan.service;

import com.github.pagehelper.PageInfo;
import com.hanlinyuan.Enum.ResponseEnum;
import com.hanlinyuan.pay_wechat.MallApplicationTests;
import com.hanlinyuan.service.Imp.impProductService;
import com.hanlinyuan.vo.ProductDetailVo;
import com.hanlinyuan.vo.ResponseVo;
import com.hanlinyuan.vo.productVo;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

import java.util.List;


/**
 * @Author: 翰林猿
 * @Description: TODO
 **/
public class productServiceTest extends MallApplicationTests {

    @Resource
    private productService productService;
    @Test
    public void list() {
        ResponseVo<PageInfo> responseVo = productService.list(null, 2, 3);
        //期望responseVo.getStatus()的值与ResponseEnum.SUCCESS.getCode()的值相等。如果相等，断言通过，测试继续执行；如果不相等，断言失败，测试中止。
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }
    @Test
    public void detail() {
        ResponseVo<ProductDetailVo> responseVo = productService.detail(26);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }
}