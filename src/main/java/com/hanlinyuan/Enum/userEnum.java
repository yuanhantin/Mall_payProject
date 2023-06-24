package com.hanlinyuan.Enum;

/**
 * @Author: 翰林猿
 * @Description: 使用枚举规范一下用户权限的设置
 **/
public enum userEnum {
    ADMIN(0),CUSTOM(1);
    Integer code;

    public Integer getCode() {
        return code;
    }

    userEnum(Integer code) {
        this.code = code;
    }
}
