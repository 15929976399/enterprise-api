package com.chinalife.enterprise.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping({"weixin"})
public class WeixinValidateController {
    @RequestMapping({"/validate"})
    public String weixincheck(HttpServletRequest request, HttpServletResponse res)
            throws NoSuchAlgorithmException, IOException {
        StringBuffer xmlretur = new StringBuffer();
        res.setCharacterEncoding("utf-8");
        try {
            String echostr = request.getParameter("echostr");
            String timestamp = request.getParameter("timestamp");
            String nonce = request.getParameter("nonce");
            String signature = request.getParameter("signature");
            String encodingAesKey = "hBj3EXAEo1tyYVVxxkbrExUipaasAcRaVySkkw8AiF6";
            String token = "77894512123fdsfsdfeeee";
            List<String> canshuList = new ArrayList();
            canshuList.add(token);
            canshuList.add(timestamp);
            canshuList.add(nonce);
            Collections.sort(canshuList);

            String str = "";
            for (int i = 0; i < canshuList.size(); i++) {
                str = str + (String) canshuList.get(i);
            }
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(str.getBytes());
            byte[] digest = md.digest();

            StringBuffer hexstr = new StringBuffer();
            String shaHex = "";
            for (int i = 0; i < digest.length; i++) {
                shaHex = Integer.toHexString(digest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexstr.append(0);
                }
                hexstr.append(shaHex);
            }
            String sha1str = hexstr.toString();
            if (sha1str.equals(signature)) {
                PrintWriter writ = res.getWriter();


                writ.write(echostr);
                writ.flush();
                writ.close();
            }
            BufferedReader bis = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String line = null;
            String result = "";
            try {
                while ((line = bis.readLine()) != null) {
                    result = result + line + "\r\n";
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                bis.close();
            }
            System.out.println(result);
            InputStream inStream = request.getInputStream();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            outStream.close();
            inStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
