package com.chinalife.enterprise.service.impl;

import com.chinalife.enterprise.dao.InsuranceMapper;
import com.chinalife.enterprise.domain.ClaimInfo;
import com.chinalife.enterprise.domain.ImgInfo;
import com.chinalife.enterprise.service.InsuranceService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InsuranceServiceImpl implements InsuranceService {
    @Autowired
    private InsuranceMapper insuranceMapper;

    public Map<String, Object> findUser(String user_name) {
        return this.insuranceMapper.findUser(user_name);
    }

    public void updatePwd(String user_name, String new_pwd) {
        this.insuranceMapper.updatePwd(user_name, new_pwd);
    }

    public List<Map<String, Object>> queryPolicy(String user_name) {
        return this.insuranceMapper.queryPolicy(user_name);
    }

    public List<Map<String, Object>> queryInsurance(String user_name, String user_type, String employee) {
        return this.insuranceMapper.queryInsurance(user_name, user_type, employee);
    }

    public PageInfo queryClaims(String user_name, Integer pageNum) {
        if ((pageNum == null) || (pageNum.intValue() == 0)) {
            pageNum = Integer.valueOf(1);
        }
        Integer pageSize = Integer.valueOf(10);
        Map<String, Object> user = this.insuranceMapper.findUser(user_name);
        PageHelper.startPage(pageNum.intValue(), pageSize.intValue());
        List<Map<String, Object>> list = this.insuranceMapper.queryClaims(user_name, user.get("user_type") + "");
        PageInfo page = new PageInfo(list);
        return page;
    }

    public List<Map<String, Object>> queryMtn(String user_name) {
        return this.insuranceMapper.queryMtn(user_name);
    }

    public void addClaim(ClaimInfo claimInfo, List<ImgInfo> imagInfoList) {
        this.insuranceMapper.addClaimInfo(claimInfo);
        for (ImgInfo imagInfo : imagInfoList) {
            imagInfo.setClaim_id(claimInfo.getId());
            this.insuranceMapper.addImgInfo(imagInfo);
        }
    }
}
