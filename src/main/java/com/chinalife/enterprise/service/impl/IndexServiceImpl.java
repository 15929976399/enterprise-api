package com.chinalife.enterprise.service.impl;

import com.chinalife.enterprise.dao.IndexMapper;
import com.chinalife.enterprise.domain.ClaimInfo;
import com.chinalife.enterprise.domain.Constants;
import com.chinalife.enterprise.domain.ImgInfo;
import com.chinalife.enterprise.service.IndexService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class IndexServiceImpl implements IndexService {
    @Autowired
    private IndexMapper indexMapper;
    /**
     * 查询所有的提交信息列表
     * @param add_user 添加账号
     * @param status   状态  0 未处理  1已处理
     * @param type     理赔类型
     * @return
     */
    public PageInfo queryClaimInfo(String add_user, String status,Integer type,String branch,Integer pageNum){
        if(pageNum==null||pageNum==0){
            pageNum= Constants.DEFAULT_PAGE;
        }
        Integer   pageSize= Constants.DEFAULT_SIZE;
        PageHelper.startPage(pageNum.intValue(), pageSize.intValue());
        List<ClaimInfo> list = indexMapper.queryClaimInfo(add_user, status,type,branch);
        PageInfo page = new PageInfo(list);
        return page;
    }
    /**
     * 修改处理状态
     * @param id id
     * @return
     */
    public void updateStatus(Integer id){
        indexMapper.updateStatus(id);
    }
    /**
     * 根据claim_info id查询image_info  list
     * @param id id
     * @return
     */
    public List<ImgInfo> queryImgInfo(Integer id){
        return indexMapper.queryImgInfo(id);
    }
    /**
     * 查询一条投诉信息
     * @return
     */
    public ClaimInfo findClaimInfo(Integer id){
        return indexMapper.findClaimInfo(id);
    }
    public Map<String,Object> queryOperater(String  pid){
        return indexMapper.queryOperater(pid);
    }

}
