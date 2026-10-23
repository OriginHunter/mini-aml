package com.miniaml.exception;

import com.miniaml.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        // 把原来的 System.out.println 换成 log.error
        // 这样不仅会在控制台打印，还会自动带上时间、线程名，并且可以自动把异常堆栈打印出来
        log.error("系统发生异常，异常信息：{}", e.getMessage(), e);

        return Result.error("系统繁忙，请稍后再试");
    }
}