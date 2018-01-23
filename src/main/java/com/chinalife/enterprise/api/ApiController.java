package com.chinalife.enterprise.api;


import com.chinalife.enterprise.service.InsuranceService;
import com.chinalife.enterprise.util.JwtHelper;
import com.chinalife.enterprise.util.Md5SaltTool;
import com.chinalife.enterprise.util.ResultUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping({"/api"})
@RestController
public class ApiController {
    @Autowired
    private Environment env;
    @Autowired
    private InsuranceService insuranceService;

        @RequestMapping(value = {"/login"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Object login(@RequestBody Map<String, Object> param)
            throws Exception {
        String user_name = param.get("user_name") + "";
        String pwd = param.get("pwd") + "";
        if ((user_name == null) || ("".equals(user_name))) {
            ResultUtil.renderError("请输入账号");
        }
        if ((pwd == null) || ("".equals(pwd))) {
            ResultUtil.renderError("请输入密码");
        }
        Map<String, Object> user = this.insuranceService.findUser(user_name.trim());
        if (user == null) {
            return ResultUtil.renderError("账号不存在");
        }
        if (!Md5SaltTool.validPassword(pwd, user.get("pwd") + "")) {
            return ResultUtil.renderError("密码错误");
        }
        String jwt = JwtHelper.createJWT(user_name, user.get("user_type") + "", -1L, this.env.getProperty("base64Security"));
        return ResultUtil.renderSuccess(jwt);
    }

    @RequestMapping({"/nologin"})
    public Object nologin()
            throws Exception {
        return ResultUtil.renderError("未登录");
    }

    @RequestMapping(value = {"/pwd"}, method = {org.springframework.web.bind.annotation.RequestMethod.PUT})
    public Object upPwd(HttpServletRequest request, @RequestBody Map<String, Object> param)
            throws Exception {
        String user_name = request.getAttribute("user_name") + "";
        String old_pwd = param.get("old_pwd") + "";
        String new_pwd = param.get("new_pwd") + "";
        if ((new_pwd == null) || ("".equals(new_pwd))) {
            return ResultUtil.renderError("新密码不能为空");
        }
        Map<String, Object> user = this.insuranceService.findUser(user_name.trim());
        if (user == null) {
            return ResultUtil.renderError("账号不存在");
        }
        if (!Md5SaltTool.validPassword(old_pwd, user.get("pwd") + "")) {
            return ResultUtil.renderError("旧密码错误");
        }
        new_pwd = Md5SaltTool.getEncryptedPwd(new_pwd);
        this.insuranceService.updatePwd(user_name, new_pwd);
        return ResultUtil.renderSuccess();
    }

    @RequestMapping(value = {"/firm_policy"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object firm_policy(HttpServletRequest request)
            throws Exception {
        String user_name = request.getAttribute("user_name") + "";
        List<Map<String, Object>> policyList = this.insuranceService.queryPolicy(user_name);

        Map<String, Object> map = new HashMap();
        List<Map<String, Object>> policy_info = new ArrayList();
        for (int i = 0; i < policyList.size(); i++) {
            Map<String, Object> policy = (Map) policyList.get(i);
            if (i == 0) {
                Map<String, Object> firm_info = new HashMap();
                firm_info.put("comp_name", policy.get("comp_name") + "");
                firm_info.put("emp_appl_num", policy.get("emp_appl_num") + "");
                firm_info.put("contract_psn", hideNameFirst(policy.get("contract_psn") + ""));
                map.put("firm_info", firm_info);
            }
            Map<String, Object> item = new HashMap();
            item.put("cntr_no", policy.get("cntr_no") + "");
            item.put("prd_name", policy.get("prd_name") + "");
            item.put("sum_face_amnt", policy.get("sum_face_amnt") + "");
            item.put("sum_premium", policy.get("sum_premium") + "");
            item.put("in_force_date", policy.get("in_force_date") + "");
            item.put("cntr_expiry_date", policy.get("cntr_expiry_date") + "");
            policy_info.add(item);
        }
        map.put("policy_info", policy_info);
        return ResultUtil.renderSuccess(map);
    }

    private String hideNameFirst(String str) {
        str = str.substring(1);
        return "*" + str;
    }

    @RequestMapping(value = {"/emp_policy"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object emp_policy(HttpServletRequest request, String employee)
            throws Exception {
        String user_name = request.getAttribute("user_name") + "";
        String user_type = request.getAttribute("user_type") + "";
        List<Map<String, Object>> insuranceList = this.insuranceService.queryInsurance(user_name, user_type, employee);
        Map<String, Object> map = new HashMap();
        List<Map<String, Object>> insurance_info = new ArrayList();
        for (int i = 0; i < insuranceList.size(); i++) {
            Map<String, Object> insurance = (Map) insuranceList.get(i);
            if (i == 0) {
                Map<String, Object> emp_info = new HashMap();
                emp_info.put("name", hideNameFirst(insurance.get("name") + ""));
                emp_info.put("sex", insurance.get("sex") + "");
                emp_info.put("birth_date", insurance.get("birth_date") + "");
                emp_info.put("id_type", insurance.get("id_type") + "");
                String id_no = insurance.get("id_no") + "";
                String one = id_no.substring(0, 6);
                String two = id_no.substring(id_no.length() - 2);
                emp_info.put("id_no", one + "**********" + two);
                map.put("emp_info", emp_info);
            }
            Map<String, Object> item = new HashMap();
            item.put("cntr_no", insurance.get("cntr_no") + "");
            item.put("prd_name", insurance.get("prd_name") + "");
            item.put("face_amnt", insurance.get("face_amnt") + "");
            item.put("premium", insurance.get("premium") + "");
            item.put("in_force_date", insurance.get("in_force_date") + "");
            item.put("term_date", insurance.get("term_date") + "");
            insurance_info.add(item);
        }
        map.put("insurance_info", insurance_info);
        return ResultUtil.renderSuccess(map);
    }

    @RequestMapping(value = {"/claims"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object claims(HttpServletRequest request, Integer page_num)
            throws Exception {
        String user_name = request.getAttribute("user_name") + "";
        PageInfo page = this.insuranceService.queryClaims(user_name, page_num);

        Map<String, Object> map = new HashMap();
        map.put("total", Long.valueOf(page.getTotal()));
        map.put("page_num", Integer.valueOf(page.getPageNum()));
        map.put("claims", page.getList());
        return ResultUtil.renderSuccess(map);
    }

    @RequestMapping(value = {"/mtn"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object mtn(HttpServletRequest request)
            throws Exception {
        String user_name = request.getAttribute("user_name") + "";
        return this.insuranceService.queryMtn(user_name);
    }
}
