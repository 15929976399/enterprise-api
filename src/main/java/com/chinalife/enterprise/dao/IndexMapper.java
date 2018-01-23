package com.chinalife.enterprise.dao;


import com.chinalife.enterprise.domain.ClaimInfo;
import com.chinalife.enterprise.domain.ImgInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public  interface IndexMapper {
    /**
     * 查询所有的提交信息列表
     * @param add_user 添加账号
     * @param status   状态  0 未处理  1已处理
     * @param type     理赔类型
     * @return
     */
     List<ClaimInfo> queryClaimInfo(@Param("add_user")String add_user, @Param("status")String status,
                                    @Param("type")Integer type,@Param("branch")String branch);
    /**
     * 修改处理状态
     * @param id id
     * @return
     */
     void updateStatus(@Param("id")Integer id);
    /**
     * 根据claim_info id查询image_info  list
     * @param id id
     * @return
     */
     List<ImgInfo> queryImgInfo(@Param("id")Integer id);
    /**
     * 查询一条投诉信息
     * @return
     */
     ClaimInfo findClaimInfo(@Param("id")Integer id);

     Map<String,Object> queryOperater(@Param("pid")String pid);

}
