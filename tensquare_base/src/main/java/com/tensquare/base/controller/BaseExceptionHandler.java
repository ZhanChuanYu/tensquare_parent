package com.tensquare.base.controller;

import entity.Result;
import entity.StatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
@ControllerAdvice
//@RestControllerAdvice
public class BaseExceptionHandler{

    /**
     * 处理异常的方法
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        return new Result(false, StatusCode.ERROR,"执行失败 ",e.getMessage());
    }
}
