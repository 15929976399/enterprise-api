package com.chinalife.enterprise.util;


import com.chinalife.enterprise.domain.Result;

public class ResultUtil {
    public static Object renderError() {
        Result result = new Result();
        result.setCode(Integer.valueOf(-1));
        result.setMsg("系统错误");
        return result;
    }

    public static Object renderError(String msg) {
        Result result = new Result();
        result.setCode(Integer.valueOf(-1));
        result.setMsg(msg);
        return result;
    }

    public static Object renderSuccess(String msg) {
        Result result = new Result();
        result.setMsg(msg);
        result.setCode(Integer.valueOf(0));
        return result;
    }

    public static Object renderSuccess() {
        Result result = new Result();
        result.setCode(Integer.valueOf(0));
        return result;
    }

    public static Object renderSuccess(Object obj) {
        Result result = new Result();
        result.setCode(Integer.valueOf(0));
        result.setData(obj);
        return result;
    }
}
