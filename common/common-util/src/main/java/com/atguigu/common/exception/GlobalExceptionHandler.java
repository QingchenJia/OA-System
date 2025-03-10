package com.atguigu.common.exception;

import com.atguigu.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result<?> exception(Exception e) {
        e.printStackTrace();
        return Result.fail();
    }

    @ExceptionHandler(CustomException.class)
    public Result<?> customException(CustomException ce) {
        log.warn(ce.getMessage());
        return Result.fail();
    }
}
