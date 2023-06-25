package com.hanlinyuan.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: 翰林猿
 * @Description: 用于返回注册时的数据传输对象（DTO）Data Transfer Object
 **/
@Data
public class userRegisterForm {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String email;
}
