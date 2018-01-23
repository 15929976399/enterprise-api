package com.chinalife.enterprise.api;


import com.chinalife.enterprise.domain.ClaimInfo;
import com.chinalife.enterprise.domain.ImgInfo;
import com.chinalife.enterprise.service.InsuranceService;
import com.chinalife.enterprise.util.ResultUtil;
import com.chinalife.enterprise.wx.Wechat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequestMapping({"/api"})
@RestController
public class WeixinController {
    @Autowired
    private Environment env;
    @Autowired
    private InsuranceService insuranceService;
    private static Wechat wechat = new Wechat();

    /**
     * 获取调用微信参数参数跳转至页面
     *
     * @return
     */
    @RequestMapping(value = "/param", method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Object param( @RequestBody Map<String, Object> map) {
//		String ddd=request.getServerName()+"/uploadpic"+ request.getRequestURI();
     //   request.getScheme() + "://" + request.getServerName() + "/uploadpic" + request.getRequestURI()
        String url=map.get("url")+"";
        String WxAppId = env.getProperty("WxAppId");
        String WxAppSecret = env.getProperty("WxAppSecret");
        Map paramMap = wechat.jsConfig(wechat.getJsTicket(WxAppId, WxAppSecret),url, WxAppId);
        System.out.print("jsapi_ticket======"+paramMap.get("jsapi_ticket")+"");
        return paramMap;
    }

    @RequestMapping(value = {"/claims"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Object savePathInfo(HttpServletRequest request, @RequestBody Map<String, Object> map)
            throws Exception {
        String photoPath = env.getProperty("photoPath");
        long file_str=System.currentTimeMillis();
        photoPath=photoPath+file_str+"/";
        File dir = new File(photoPath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        String user_name = request.getAttribute("user_name") + "";
        String type = map.get("type") + "";
        String WxAppId = env.getProperty("WxAppId");
        String WxAppSecret = env.getProperty("WxAppId");

        ClaimInfo claimInfo = new ClaimInfo();
        claimInfo.setAdd_user(user_name);
        claimInfo.setType(Integer.valueOf(Integer.parseInt(type)));
        claimInfo.setFile_str(file_str+"");

        List<String> imgTypeList = (List) map.get("img_type");
        List<ImgInfo> imagInfoList = new ArrayList();
        String access_token = wechat.getAccessToken(WxAppId, WxAppSecret);
        for (String imgType : imgTypeList) {
//            imgType = (String)localIterator1.next();
            List<String> imgUrlList = (List) map.get(imgType);
            for (String url : imgUrlList) {
                ImgInfo imgInfo = new ImgInfo();
                imgInfo.setImg_type(imgType);
                imgInfo.setImg_url(url);
                imagInfoList.add(imgInfo);

                String path = photoPath + url + ".jpg";
//				System.out.println("media_id:"+ media_id);
                Wechat.downloadImg(url, access_token, path);
            }
        }
        this.insuranceService.addClaim(claimInfo, imagInfoList);
        return ResultUtil.renderSuccess();
    }

//    @RequestMapping(value = {"/imageBatch"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
//    public Object uploadBatch(HttpServletRequest request, String type, String[] img_types)
//            throws Exception {
//        String photoPath = this.env.getProperty("photoPath");
//        String user_name = request.getAttribute("user_name") + "";
//        ClaimInfo claimInfo = new ClaimInfo();
//        claimInfo.setAdd_user(user_name);
//        claimInfo.setType(Integer.valueOf(Integer.parseInt(type)));
//
//        MultipartHttpServletRequest mhttp = (MultipartHttpServletRequest) request;
//
//
//        File dir = new File(photoPath);
//        if (!dir.exists()) {
//            dir.mkdir();
//        }
//        List<ImgInfo> imagInfoList = new ArrayList();
////        String img_type;
//        for (String img_type : img_types) {
//            List<MultipartFile> multipartFileList = mhttp.getFiles("pic" + img_type);
//            for (MultipartFile item : multipartFileList) {
//                ImgInfo imgInfo = new ImgInfo();
//                imgInfo.setImg_type(img_type);
//                String img_url = executeUpload(photoPath, item);
//                imgInfo.setImg_url(img_url);
//                imagInfoList.add(imgInfo);
//            }
//        }
//        this.insuranceService.addClaim(claimInfo, imagInfoList);
//        return ResultUtil.renderSuccess();
//    }
//
//    @RequestMapping(value = {"/image"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
//    public Object uploadOne(HttpServletRequest request)
//            throws Exception {
//        String photoPath = this.env.getProperty("photoPath");
//        MultipartHttpServletRequest mhttp = (MultipartHttpServletRequest) request;
//
//
//        File dir = new File(photoPath);
//        if (!dir.exists()) {
//            dir.mkdir();
//        }
//        MultipartFile multipartFileList = mhttp.getFile("pic");
//        String img_url = executeUpload(photoPath, multipartFileList);
//        return ResultUtil.renderSuccess(img_url);
//    }

//    @RequestMapping(value={"/claims"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
//    public Object savePathInfo(HttpServletRequest request, @RequestBody Map<String, Object> map)
//            throws Exception
//    {
//        String user_name = request.getAttribute("user_name") + "";
//        String type = map.get("type") + "";
//
//        ClaimInfo claimInfo = new ClaimInfo();
//        claimInfo.setAdd_user(user_name);
//        claimInfo.setType(Integer.valueOf(Integer.parseInt(type)));
//
//        List<String> imgTypeList = (List)map.get("img_type");
//        List<ImgInfo> imagInfoList = new ArrayList();
//        for (String imgType: imgTypeList)
//        {
////            imgType = (String)localIterator1.next();
//            List<String> imgUrlList = (List)map.get(imgType);
//            for (String url : imgUrlList)
//            {
//                ImgInfo imgInfo = new ImgInfo();
//                imgInfo.setImg_type(imgType);
//                imgInfo.setImg_url(url);
//                imagInfoList.add(imgInfo);
//            }
//        }
//        String imgType;
//        this.insuranceService.addClaim(claimInfo, imagInfoList);
//        return ResultUtil.renderSuccess();
//    }

    private String executeUpload(String uploadDir, MultipartFile file)
            throws Exception {
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

        String filename = UUID.randomUUID() + suffix;

        File serverFile = new File(uploadDir + filename);

        file.transferTo(serverFile);
        return filename;
    }
}
