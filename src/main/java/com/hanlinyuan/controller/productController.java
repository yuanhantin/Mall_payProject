package com.hanlinyuan.controller;

import com.github.pagehelper.PageInfo;
import com.hanlinyuan.dao.ProductMapper;
import com.hanlinyuan.service.productService;
import com.hanlinyuan.vo.ProductDetailVo;
import com.hanlinyuan.vo.ResponseVo;
import jdk.nashorn.internal.runtime.logging.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author: 翰林猿
 * @Description: TODO
 **/
@RestController
public class productController {
    @Resource
    private productService productService;
    @GetMapping("/products")
    public ResponseVo<PageInfo> list(@RequestParam(required = false) Integer categoryId,
                                     @RequestParam(required = false ,defaultValue = "1") Integer pageNum,
                                     @RequestParam(required = false ,defaultValue = "10") Integer pageSize){
        return productService.list(categoryId,pageNum,pageSize);
    }
    @GetMapping("/products/{productId}")
    public ResponseVo<ProductDetailVo> detail(@PathVariable Integer productId) {
        return productService.detail(productId);
    }
}
