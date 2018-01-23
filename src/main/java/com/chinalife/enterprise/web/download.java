//package com.chinalife.enterprise.web;
//
//import com.chinalife.enterprise.util.DeleteFileUtil;
//import com.chinalife.enterprise.util.ZipUtil;
//import org.apache.catalina.servlet4preview.http.HttpServletRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.env.Environment;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.*;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//import java.util.zip.ZipOutputStream;
//
///**
// * Created by Admin on 2018/1/18.
// */
//public  class download {
//    @Autowired
//    private Environment env;
//
//    /**
//     * 一键下载图片
//     */
//    @SuppressWarnings("unchecked")
//    @RequestMapping(value = "downUrl")
//    public void admin_applyloan_downUrl(HttpServletRequest request,
//                                        HttpServletResponse response,
//                                        @RequestParam(value = "id", required = true) Long id) {
//        String photoPath=env.getProperty("photoPath");
//        try {
//            //1.获取项目中文件的文件夹
//            String basePath = request.getSession().getServletContext().getRealPath("/") +"download";
//            //2.获取oss上的文件路径List集合：https://www.xxx.xx/xxx.jpg
//            ApplyLoan applyLoan = applyLoanService.getById(id);
//            List<String> urlListAll = new ArrayList<String>();
//            List<String> urlList = (List<String>) applyLoan.getParamsMap().get("carImg");
//            List<String> ownerList = (List<String>) applyLoan.getParamsMap().get("ownerImg");
//            urlListAll.addAll(urlList);
//            urlListAll.addAll(ownerList);
//            //3.定义文件list
//            List<File> files = new ArrayList<File>();
//            //4.定义zip文件名
//            String fileName = UUID.randomUUID().toString() + ".zip";
//            //4.1定义临时单个文件的list，方便后面删除
//            List<String> tempUrlList = new ArrayList<String>();
//            //5.将文件从https上下载进服务器的目录，用files装好
//            for (String url : urlListAll) {
//                URL u = new URL((String) request.getServletContext().getAttribute("imagePath") + url);
//                String tempUrl = basePath +System.currentTimeMillis()+ url.substring(url.lastIndexOf("."));
//                File f = new File(tempUrl);
//                if(!f.exists()){
//                    try {
//                        f.createNewFile();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                InputStream ins = u.openStream();
//                OutputStream os = new FileOutputStream(f);
//                int bytesRead = 0;
//                byte[] buffer = new byte[2048];
//                while ((bytesRead = ins.read(buffer, 0, 2048)) != -1) {
//                    os.write(buffer, 0, bytesRead);
//                }
//                os.close();
//                ins.close();
//                files.add(f);
//                tempUrlList.add(tempUrl);
//            }
//            //6.创建zip文件
//            ZipUtil.createFile(basePath, fileName);
//            File file = new File(basePath + fileName);
//            FileOutputStream outStream = new FileOutputStream(file);
//            ZipOutputStream toClient = new ZipOutputStream(outStream);
//            //7.将files打包成zip文件
//            ZipUtil.zipFile(files, toClient);
//            toClient.close();
//            outStream.close();
//            //8.下载zip文件，并删除服务器源文件
//            ZipUtil.downloadFile(file, response, true);
//            //9.删除服务器临时的单个文件
//            for(String t : tempUrlList){
//                DeleteFileUtil.delete(t);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//}
