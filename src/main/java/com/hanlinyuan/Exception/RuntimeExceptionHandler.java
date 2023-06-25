package com.hanlinyuan.Exception;

import com.hanlinyuan.Enum.ResponseEnum;
import com.hanlinyuan.vo.ResponseVo;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

/**
 * @Author: 翰林猿
 * @Description: TODO
 **/
@ControllerAdvice
public class RuntimeExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseVo handler(RuntimeException e){
        return ResponseVo.error(ResponseEnum.ERROR, e.getMessage());
    }

    @ExceptionHandler(UserLoginException.class)
    @ResponseBody
    public ResponseVo userLoginHandle() {
        return ResponseVo.error(ResponseEnum.NEED_LOGIN);
    }


    /**
     * 表单统一验证处理（如果传入的参数对不上DTO，会报MethodArgumentNotValidException，我们在这里捕获然后返回一句话说参数错误）
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseVo notValidExceptionHandle(MethodArgumentNotValidException e) {
        System.out.println("1");
        BindingResult bindingResult = e.getBindingResult();
        return ResponseVo.error(ResponseEnum.PARAM_ERROR,
                Objects.requireNonNull(bindingResult.getFieldError()).getField()+""+bindingResult.getFieldError().getDefaultMessage());

    }
}
