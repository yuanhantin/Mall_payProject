package com.hanlinyuan.service;

import com.hanlinyuan.Enum.ResponseEnum;
import com.hanlinyuan.Enum.userEnum;
import com.hanlinyuan.dao.UserMapper;
import com.hanlinyuan.pojo.User;
import com.hanlinyuan.service.Imp.impUserService;
import com.hanlinyuan.vo.ResponseVo;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @Author: 翰林猿
 * @Description: 用户登录service层
 **/

@Service
public class userService implements impUserService {
    @Resource
    UserMapper userMapper;
    @Override
    public ResponseVo register(User user) {
        //注册用户名不能重复
        int countByUsername = userMapper.countByUsername(user.getUsername());
        if (countByUsername > 0){
            return ResponseVo.error(ResponseEnum.USERNAME_EXIST);
        }
        //email不能重复
        int countByEmail = userMapper.countByEmail(user.getEmail());
        if (countByEmail > 0){
            return ResponseVo.error(ResponseEnum.EMAIL_EXIST);
        }
        //记得给注册的用户设置权限
        user.setRole(userEnum.CUSTOM.getCode());

        //密码等等要遵守规范填写（这里先不实现）
        //把密码用spring自带的md5加密成utf-8的字符串放入user里
        String md5DigestAsHex = DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8));
        user.setPassword(md5DigestAsHex);
        //校验完毕之后，写入数据库
        int insertUser = userMapper.insertSelective(user);
        if (insertUser==0){
            return ResponseVo.error(ResponseEnum.ERROR);
        }
        return ResponseVo.error(ResponseEnum.SUCCESS);
    }

    @Override
    public ResponseVo login(String username,String password) {
        User user = userMapper.selectByUsername(username);
        if (user == null){
            //要返回用户名或密码错误，为什么不直接显示用户名不存在或者密码错误，其实是一个安全措施，避免别人发现有这个用户然后开始破解密码。
            return ResponseVo.error(ResponseEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        //先把传进来的密码解析成md5，再和数据库进行对比
        String passwordFromOutside = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        if (!user.getPassword().equalsIgnoreCase(passwordFromOutside)){
            //返回用户名或密码错误
            return ResponseVo.error(ResponseEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        return ResponseVo.success(user);
    }

    public void error(){
        throw new RuntimeException("测试是否被异常类捕获，从而直接返回前端需要的格式");
    }
}
