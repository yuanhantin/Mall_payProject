package com.hanlinyuan.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: 翰林猿
 * @Description: 用于返回登录的json格式的对象
 **/
@Data
public class userLoginForm {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
