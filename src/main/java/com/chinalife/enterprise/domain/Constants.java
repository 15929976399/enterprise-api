package com.chinalife.enterprise.domain;/**
 * Created by Admin on 2016/12/5.
 */

/**
 * 定义一些代码中用到的常量
 *
 * @author fy
 * @create 2016-12-05 15:59
 **/
public class Constants {
    //默认页数
    public static Integer DEFAULT_PAGE=1;
    //默认每页显示的大小
    public static Integer DEFAULT_SIZE=10;

    // 验证accessToken
    public static final String CHECK_ACCESS_CODE_URL = "http://localhost:8080/zetark-oauth2-server/checkAccessToken?accessToken=";
    public static final String TOKEN="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiIxNjEwMDE1NCIsImV4cCI6MTUxNTY1ODM5NzcyMH0.dnp8l51nqMyMpUxNLg2YZ7o3kTWkAULJw_pHT0JfME8";
    //    public static final String  PREFIX="http://10.184.1.8/api/eck/test";
    public static final String  PREFIX="http://10.184.1.8/api/eck/v2";

}
