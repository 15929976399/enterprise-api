package com.chinalife.enterprise.dao;


import java.util.List;
import java.util.Map;

import com.chinalife.enterprise.domain.ClaimInfo;
import com.chinalife.enterprise.domain.ImgInfo;
import org.apache.ibatis.annotations.Param;

public  interface InsuranceMapper {
      Map<String, Object> findUser(@Param("user_name") String paramString);

      void updatePwd(@Param("user_name") String paramString1, @Param("new_pwd") String paramString2);

      List<Map<String, Object>> queryPolicy(@Param("user_name") String paramString);

      List<Map<String, Object>> queryInsurance(@Param("user_name") String paramString1, @Param("user_type") String paramString2, @Param("employee") String paramString3);

      List<Map<String, Object>> queryClaims(@Param("user_name") String paramString1, @Param("user_type") String paramString2);

      List<Map<String, Object>> queryMtn(@Param("user_name") String paramString);

      void addClaimInfo(@Param("claimInfo") ClaimInfo paramClaimInfo);

      void addImgInfo(@Param("imgInfo") ImgInfo paramImgInfo);
}
