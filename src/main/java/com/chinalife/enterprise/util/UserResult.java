package com.chinalife.enterprise.util;

import java.util.Map;

/**
 * Created by Admin on 2018/1/18.
 */
public class UserResult {
    private static final long serialVersionUID = 5576237395711742681L;

    private Integer errcode;

    private String errmsg;
    private String accessToken;

    private Map<String,Object> user;

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public Map<String, Object> getUser() {
        return user;
    }

    public void setUser(Map<String, Object> user) {
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
