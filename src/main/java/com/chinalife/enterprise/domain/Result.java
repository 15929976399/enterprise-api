package com.chinalife.enterprise.domain;

import java.io.Serializable;

public class Result<T>
        implements Serializable {
    private Integer code;
    private String msg;
    private T data;

    public Result() {
    }

    public Result(Integer code, T obj) {
        this.code = code;
        this.data = obj;
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
