package com.chinalife.enterprise.interceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chinalife.enterprise.util.ResultUtil;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({Exception.class})
    @ResponseBody
    public Object jsonErrorHandler(HttpServletRequest request, HttpServletResponse response, Exception e)
            throws Exception {
        e.printStackTrace();
        return ResultUtil.renderError();
    }
}
