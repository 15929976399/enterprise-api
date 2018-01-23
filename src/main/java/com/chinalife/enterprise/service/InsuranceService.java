package com.chinalife.enterprise.service;


import com.chinalife.enterprise.domain.ClaimInfo;
import com.chinalife.enterprise.domain.ImgInfo;
import com.github.pagehelper.PageInfo;
import java.util.List;
import java.util.Map;

public  interface InsuranceService
{
      Map<String, Object> findUser(String paramString);

      void updatePwd(String paramString1, String paramString2);

      List<Map<String, Object>> queryPolicy(String paramString);

      List<Map<String, Object>> queryInsurance(String paramString1, String paramString2, String paramString3);

      PageInfo queryClaims(String paramString, Integer paramInteger);

      List<Map<String, Object>> queryMtn(String paramString);

      void addClaim(ClaimInfo paramClaimInfo, List<ImgInfo> paramList);
}
