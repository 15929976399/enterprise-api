package com.chinalife.enterprise.service;


import com.chinalife.enterprise.domain.ClaimInfo;
import com.chinalife.enterprise.domain.ImgInfo;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public  interface IndexService {
    /**
     * 查询所有的提交信息列表
     * @param add_user 添加账号
     * @param status   状态  0 未处理  1已处理
     * @param type     理赔类型
     * @return
     */
      PageInfo queryClaimInfo(String add_user, String status,Integer type,String branch,Integer pageNum);
      /**
       * 修改处理状态
       * @param id id
       * @return
       */
      void updateStatus(Integer id);
      /**
       * 根据claim_info id查询image_info  list
       * @param id id
       * @return
       */
      List<ImgInfo> queryImgInfo(Integer id);
      /**
       * 查询一条投诉信息
       * @return
       */
      ClaimInfo findClaimInfo(Integer id);
      Map<String,Object> queryOperater(String  pid);
}
