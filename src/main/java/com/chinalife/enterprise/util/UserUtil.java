package com.chinalife.enterprise.util;

import com.chinalife.enterprise.domain.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 2018/1/18.
 */
public class UserUtil {
    /**
     * @descrtption  根据用户id获取用户信息
     * @Author fy
     * @Date 2017/1/13 17:52
     **/
    public  static Map<String,Object> getUser(String user_id){
        Map<String, Object> headers=new HashMap<String, Object>();
        headers.put("Authorization","Bearer "+ Constants.TOKEN);
        Map<String, Object> params=new HashMap<String, Object>();
        Map<String,Object> user=null;
        try {
            String obj= HttpClientUtil.httpGetRequest( Constants.PREFIX+"/users/"+user_id,headers, params);
            ObjectMapper mapper = new ObjectMapper();//转换器
            UserResult r = mapper.readValue(obj, UserResult.class); //json转换成map
            if(r!=null&&"0".equals(r.getErrcode()+"")){
                user=r.getUser();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    /**
     * @descrtption验证用户密码是否正确
     * @Author fy
     * @Date 2017/1/13 17:52
     **/
    public  static Integer validatePsd(String user_id,String psw){
        Integer errcode=-1;
        Map<String, Object> headers=new HashMap<String, Object>();
        headers.put("Authorization","Bearer "+ Constants.TOKEN);
        Map<String, Object> params=new HashMap<String, Object>();
        params.put("id",user_id);
        params.put("password", DigestUtils.md5Hex(psw));
        try {
            String obj=HttpClientUtil.httpPostRequest(Constants.PREFIX+"/users/validate", headers,params);

            ObjectMapper mapper = new ObjectMapper();//转换器
            UserResult r = mapper.readValue(obj, UserResult.class); //json转换成map
            if(r!=null){
                errcode=r.getErrcode();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return errcode;
    }
    /**
     * @descrtption重置密码
     * @Author fy
     * @Date 2017/1/13 17:52
     **/
    public  static Integer resetPsd(String user_id,String psw){
        Integer errcode=-1;
        Map<String, Object> headers=new HashMap<String, Object>();
        headers.put("Authorization","Bearer "+ Constants.TOKEN);
        Map<String, Object> params=new HashMap<String, Object>();
        params.put("password", DigestUtils.md5Hex(psw));
        try {
            String obj=HttpClientUtil.httpPutRequest(Constants.PREFIX+"/users/"+user_id, headers,params);

            ObjectMapper mapper = new ObjectMapper();//转换器
            UserResult r = mapper.readValue(obj, UserResult.class); //json转换成map
            if(r!=null){
                errcode=r.getErrcode();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return errcode;
    }

}
