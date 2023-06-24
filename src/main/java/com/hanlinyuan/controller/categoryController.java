package com.hanlinyuan.controller;

import com.hanlinyuan.service.categoryService;
import com.hanlinyuan.vo.CategoryVo;
import com.hanlinyuan.vo.ResponseVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: 翰林猿
 * @Description: TODO
 **/
@RestController
@RequestMapping()
public class categoryController {

    @Resource
    categoryService categoryService;
    @GetMapping("/testcategory")
    public ResponseVo<List<CategoryVo>> test(){
        return categoryService.selectAll();
    }
}
