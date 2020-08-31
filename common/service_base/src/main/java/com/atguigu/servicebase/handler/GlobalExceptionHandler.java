package com.atguigu.servicebase.handler;

import com.atguigu.commonutils.ExceptionUtil;
import com.atguigu.commonutils.R;
import com.atguigu.servicebase.exception.GuliException;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //全局异常处理
    @ExceptionHandler(Exception.class)
    public R error(Exception e){
        e.printStackTrace();
        log.error(ExceptionUtil.getMessage(e));
        return R.error().message("全局异常处理");
    }

    //特定异常处理
    @ExceptionHandler(ArithmeticException.class)
    public R error(ArithmeticException e){
        e.printStackTrace();
        log.error(ExceptionUtil.getMessage(e));
        return R.error().message("算术异常");
    }

    //自定义异常处理
    @ExceptionHandler(GuliException.class)
    public R error(GuliException e){
        //控制台答应异常
        e.printStackTrace();
        //日志文件输出异常
        log.error(e.getMsg());
        log.error(ExceptionUtil.getMessage(e));
        return R.error().message(e.getMsg()).code(e.getCode());
    }
}
