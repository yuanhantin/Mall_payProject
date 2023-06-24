package com.hanlinyuan.service.Imp;

import com.hanlinyuan.pojo.User;
import com.hanlinyuan.vo.ResponseVo;

/**
 * @Author: 翰林猿
 * @Description: TODO
 **/
public interface impUserService {
    ResponseVo<User> register(User user);
    ResponseVo<User> login(String username,String password);
}
