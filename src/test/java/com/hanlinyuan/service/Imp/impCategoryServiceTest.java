package com.hanlinyuan.service.Imp;


import com.hanlinyuan.pay_wechat.MallApplicationTests;
import com.hanlinyuan.service.categoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;


import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;


/**
 * @Author: 翰林猿
 * @Description: TODO
 **/
@Slf4j
public class impCategoryServiceTest  extends MallApplicationTests {
    @Resource
    categoryService categoryService;
    @Test
    public void findSubCategoryId() {
        Set<Integer> set = new HashSet<>();
        categoryService.findSubCategoryId(100001,set);
        log.info("set={}",set);
    }
}