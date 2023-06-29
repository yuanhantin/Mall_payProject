package com.hanlinyuan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.hanlinyuan.dao")
public class PayWechatApplication {
    public static void main(String[] args) {
        SpringApplication.run(PayWechatApplication.class, args);
    }

}
