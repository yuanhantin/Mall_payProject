package com.hanlinyuan.service;

import com.hanlinyuan.Enum.userEnum;
import com.hanlinyuan.pay_wechat.MallApplicationTests;
import com.hanlinyuan.pojo.User;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @Author: 翰林猿
 * @Description: TODO
 **/
@Transactional
public class userServiceTest extends MallApplicationTests {

    @Resource
    private userService userService;
    @Test
    public void register() {
        User yuanhantin = new User("yuan", "123123", "456@qq.com", userEnum.CUSTOM.getCode());
        userService.register(yuanhantin);
        System.out.println("yuanhantin 第一次注册成功");
    }
}