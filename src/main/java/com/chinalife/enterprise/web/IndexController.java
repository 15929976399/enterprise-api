package com.chinalife.enterprise.web;/**
 * Created by Admin on 2017/9/28.
 */


import com.chinalife.enterprise.domain.ClaimInfo;
import com.chinalife.enterprise.domain.ImgInfo;
import com.chinalife.enterprise.service.IndexService;
import com.chinalife.enterprise.util.*;
import com.github.pagehelper.PageInfo;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipOutputStream;

/**
 * @author fy
 * @create 2017-09-28 9:55
 **/
@Controller
public class IndexController {
    @Autowired
    private IndexService indexService;
    @Autowired
    private Environment env;
    private final ResourceLoader resourceLoader;
    @Autowired
    public IndexController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    /**
     * @descrtption
     * @Author fy
     * @Date 2016/11/29 8:52
     **/
    @RequestMapping(value="/login",method = RequestMethod.GET)
    public String login() {
        return "login";
    }
    @ResponseBody
    @RequestMapping(value="/login",method = RequestMethod.POST)
    public Object login(String pid,String password,HttpSession session) {
        try {
            // 验证用户名密码
            Map<String,Object> user= UserUtil.getUser(pid);
            if(user!=null){
                Integer errcode=UserUtil.validatePsd(pid,password);
                if(!"0".equals(errcode+"")){
                    return  ResultUtil.renderError("登录失败:密码不正确");
                }
            }else{
                return  ResultUtil.renderError("登录失败:用户名不正确");
            }

            //验证是否有权限
            Map<String,Object> operator=indexService.queryOperater(pid);
            if(operator==null){
                return  ResultUtil.renderError("登录失败:用户名没有权限");
            }
            //登录成功 向session加入信息
            session.setAttribute("pid",pid);
            session.setAttribute("level",operator.get("level")+"");
            session.setAttribute("branch_code",user.get("branch_code"));
            return  ResultUtil.renderSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return  ResultUtil.renderError();
        }
    }

    /**列表页面
     * @author fy
     * @create 2017-06-09 14:26
     **/
    @RequestMapping(value="/index")
    public String index(String add_user, String status, Integer type,Integer pageNum,
                        ModelMap modelMap, HttpSession session){
        if(session.getAttribute("pid")==null){
            return "login";
        }

        String level=session.getAttribute("level")+"";
        String branch_code=session.getAttribute("branch_code")+"";
        String branch=null;
        if("2".equals(level)){
            branch=branch_code.substring(0,4);
        }else if("3".equals(level)){
            branch=branch_code;
        }

        PageInfo page=indexService.queryClaimInfo(add_user,status,type,branch,pageNum);

        modelMap.put("page",page);
        modelMap.put("add_user",add_user);
        modelMap.put("status",status);
        modelMap.put("type",type);
        return "index";
    }
    /**详情页面
     * @author fy
     * @create 2017-06-09 14:26
     **/
    @RequestMapping(value="/{id}/detail")
    public String detail(@PathVariable Integer id,ModelMap modelMap,HttpSession session){
        if(session.getAttribute("pid")==null){
            return "login";
        }
        List<ImgInfo> list=indexService.queryImgInfo(id);
        modelMap.put("list",list);
        modelMap.put("id",id);
        return "detail";
    }

    /**修改处理状态
     * @author fy
     * @create 2017-06-09 14:26
     **/
    @RequestMapping(value="/{id}/index",method = RequestMethod.GET)
    @ResponseBody
    public Object updateStatus(@PathVariable Integer id)throws Exception{
        try {

            indexService.updateStatus(id);

            return  ResultUtil.renderSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return  ResultUtil.renderError();
        }
    }
    /**
     * 批量打包下载文件生成zip文件下载
     *
     * @param
     */
    @RequestMapping(value = "{id}/loadFiles", method = RequestMethod.GET)
    public void downloadFiles(@PathVariable Integer id,HttpServletResponse response)
            throws ServletException, IOException {
        ClaimInfo claimInfo=indexService.findClaimInfo(id);
//        List<ImgInfo> list=indexService.queryImgInfo(id);
        String photoPath=env.getProperty("photoPath");
        String file_str=claimInfo.getFile_str();
        String inputFileName=photoPath+file_str+"/";

        String zipFileName = photoPath+file_str+".zip"; // 压缩后的zip文件 可随意定一个磁盘路径或者相对路径
        try {

            CustomFileUtil.zip(inputFileName, zipFileName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        File temp = new File(zipFileName);
        CustomFileUtil.downloadFile(temp, response, true);
    }
    /**
     * 一键下载图片
     */
    @RequestMapping(value = "downUrl")
    public void admin_applyloan_downUrl(HttpServletResponse response,Integer id) {
        String downLoadPath=env.getProperty("downLoadPath");
        try {
            //1.获取项目中文件的文件夹
//            String basePath = request.getSession().getServletContext().getRealPath("/") +"download/";
            String basePath = env.getProperty("temporaryPath");
            File dir = new File(basePath);
            if (!dir.exists()) {
                dir.mkdir();
            }
            //2.获取oss上的文件路径List集合：https://www.xxx.xx/xxx.jpg
            ClaimInfo claimInfo=indexService.findClaimInfo(id);
            List<ImgInfo> list=indexService.queryImgInfo(id);
            downLoadPath=downLoadPath+claimInfo.getFile_str()+"/";
            //3.定义文件list
            List<File> files = new ArrayList<File>();
            //4.定义zip文件名
            String fileName = UUID.randomUUID().toString() + ".zip";
            //4.1定义临时单个文件的list，方便后面删除
            List<String> tempUrlList = new ArrayList<String>();
            //5.将文件从https上下载进服务器的目录，用files装好
            for(ImgInfo imgInfo:list){
                URL u = new URL(downLoadPath+imgInfo.getImg_url()+".jpg");
                String tempUrl = basePath +System.currentTimeMillis()+ imgInfo.getImg_url()+".jpg";
                File f = new File(tempUrl);
                if(!f.exists()){
                    try {
                        f.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                InputStream ins = u.openStream();
                OutputStream os = new FileOutputStream(f);
                int bytesRead = 0;
                byte[] buffer = new byte[2048];
                while ((bytesRead = ins.read(buffer, 0, 2048)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.close();
                ins.close();
                files.add(f);
                tempUrlList.add(tempUrl);
            }
            //6.创建zip文件
            ZipUtil.createFile(basePath, fileName);
            File file = new File(basePath + fileName);
            FileOutputStream outStream = new FileOutputStream(file);
            ZipOutputStream toClient = new ZipOutputStream(outStream);
            //7.将files打包成zip文件
            ZipUtil.zipFile(files, toClient);
            toClient.close();
            outStream.close();
            //8.下载zip文件，并删除服务器源文件
            ZipUtil.downloadFile(file, response, true);
            //9.删除服务器临时的单个文件
            for(String t : tempUrlList){
                DeleteFileUtil.delete(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
