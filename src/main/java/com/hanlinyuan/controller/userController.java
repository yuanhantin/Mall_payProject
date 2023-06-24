package com.hanlinyuan.controller;

import com.hanlinyuan.Enum.ResponseEnum;
import com.hanlinyuan.form.userLoginForm;
import com.hanlinyuan.form.userRegisterForm;
import com.hanlinyuan.pojo.User;
import com.hanlinyuan.service.userService;
import com.hanlinyuan.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static com.hanlinyuan.Const.mallConst.CURRENT_USER;

/**
 * @Author: 翰林猿
 * @Description: 用户的Controller层
 **/
@RestController
@RequestMapping()
@Slf4j
public class userController {
    @Autowired
    private userService userService;
    @PostMapping("/user/register")
    public ResponseVo Register(@Valid @RequestBody userRegisterForm userRegisterForm, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            //输出出错的字段的message信息
            System.out.println(bindingResult.getFieldError().getDefaultMessage());
            log.error("注册提交的参数有误，{},{}",bindingResult.getFieldError().getField(),
                    bindingResult.getFieldError().getDefaultMessage());

            return ResponseVo.error(ResponseEnum.PARAM_ERROR,bindingResult);
        }
        User user = new User();
        BeanUtils.copyProperties(userRegisterForm, user);
        return userService.register(user);
    }

    @PostMapping("/user/login")
    public ResponseVo login(@Valid @RequestBody userLoginForm userLoginForm, BindingResult bindingResult, HttpSession httpSession){
        if (bindingResult.hasErrors()){
            return ResponseVo.error(ResponseEnum.PARAM_ERROR,bindingResult);
        }
        ResponseVo userResponseVo = userService.login(userLoginForm.getUsername(), userLoginForm.getPassword());
        //我们获取到用户之后，要给用户设置session,这样就能判断用户是否登录，登陆了之后才能做一些其他业务操作。
        httpSession.setAttribute(CURRENT_USER,userResponseVo.getData());
        log.info("/user sessionId={}", httpSession.getId());
        return userResponseVo;
    }

    @GetMapping("/user")
    public ResponseVo<User> user(HttpSession httpSession){
        log.info("/user sessionId={}", httpSession.getId());
        User user = (User) httpSession.getAttribute(CURRENT_USER);
        //用户已登录,则返回用户data和status（和前端约定好的），需要注意的是密码记得置空，要不然安全出问题。
        user.setPassword("");
        return ResponseVo.success(user);
    }

    @PostMapping("/user/logout")
    public ResponseVo logout(HttpSession httpSession) {
        log.info("/user/logout sessionId={}", httpSession.getId());
        //直接把用户的session给他去掉就好了
        httpSession.removeAttribute(CURRENT_USER);
        return ResponseVo.success();
    }
}
