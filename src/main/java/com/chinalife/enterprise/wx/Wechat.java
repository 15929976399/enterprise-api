package com.chinalife.enterprise.wx;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.activation.MimetypesFileTypeMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Wechat {
	
	private String _accesstoken;
	private Date _accesstoken_date;
	
	private String _jsticket;
	private Date _jsticket_date;

	
	 /**
     * 
     * @param token
     * @param signature// 微信加密签名  
     * @param timestamp// 时间戳  
     * @param nonce// 随机数  
     * @param echostr//随机字符串
     * @return
     */
    public boolean vaild (String token,String signature,String timestamp,String nonce,String echostr) {
        //验证URL真实性    
            List<String> list = new ArrayList<String>();  
            list.add(token);  
            list.add(timestamp);  
            list.add(nonce);
            //1. 将token、timestamp、nonce三个参数进行字典序排序  
            Collections.sort(list, new Comparator<String>() {  
                public int compare(String o1, String o2) {  
                    return o1.compareTo(o2);  
                }  
            });  
            //2. 将三个参数字符串拼接成一个字符串进行sha1加密  
            String temp = SHA1(list.get(0) + list.get(1) + list.get(2));  
            return temp.equalsIgnoreCase(signature);
    }
	
    /**
     * 回复消息
     * @param echostr
     * @param in
     * @return
     */
    public String reply(String echostr,InputStream in,CallBack callback) {
        // 处理接收消息    
        Message msg = new Message();
        msg = parse(in);

        return reply(echostr,msg,callback);
        
    }
    
    
    
    /**
     * 回复消息
     * @param echostr
     * @param
     * @return
     */
     public String reply(String echostr,Message msg,CallBack callback) {
    	String replay = "";
        if (echostr != null && "".equals(echostr) == false) {
            replay = echostr;
        }
        
    	if(callback!=null){
        	return replay + callback.reply(msg);
        } else {
        	return null;
        }
    }
    
	
	 /**
     * 获取 接口票据
     * @return
     */
    public String getAccessToken(String appid,String secret) {
    	
    	if(_accesstoken_date==null){
    		_accesstoken_date = new Date();
    	}
    	
    	Date now  =new Date();
    	long delay = now.getTime() - _accesstoken_date.getTime();
    	if(_accesstoken!=null &&  delay<=5000000){
    		return _accesstoken;
    	}
    	
    	_accesstoken_date = now;
    	HttpClient client = new HttpClient();
    	String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+secret;
		client.send(url,HttpClient.METHOD.GET);
		String content = client.getContent();
		System.out.println("AccessTokencontent======="+content);
		client.close();
		
		//TODO 解析json 获取token信息
		String regex = "\"access_token\":\"([^\"]+)\"";
		  Pattern p = Pattern.compile(regex);
		  Matcher m = p.matcher(content);
		  if(m.find()){
			  _accesstoken = m.group(1);
		  }
    	
    	return _accesstoken;
    }
	
	
	public static String UploadImg(String appid, String secret, Map<String, String> textMap, Map<String, String> fileMap)
	  {
	    String accessToken = new Wechat().getAccessToken(appid, secret);
	    String postUrl = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=" + accessToken + "&type=image";

	    String res = "";
	    HttpURLConnection conn = null;
	    String BOUNDARY = "----------参数分隔";
	    try {
	      URL url = new URL(postUrl);

	      conn = (HttpURLConnection)url.openConnection();
	      conn.setConnectTimeout(5000);
	      conn.setReadTimeout(30000);

	      conn.setDoOutput(true);
	      conn.setDoInput(true);
	      conn.setUseCaches(false);
	      conn.setRequestMethod("POST");
	      conn.setRequestProperty("Connection", "Keep-Alive");
	      conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
	      conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
	      OutputStream out = new DataOutputStream(conn.getOutputStream());

	      if (textMap != null) {
	        StringBuffer strBuf = new StringBuffer();
	        Iterator iter = textMap.entrySet().iterator();
	        while (iter.hasNext()) {
	          Map.Entry entry = (Map.Entry)iter.next();
	          String inputName = (String)entry.getKey();
	          String inputValue = (String)entry.getValue();
	          if (inputValue == null) {
	            continue;
	          }
	          strBuf.append("\r\n").append("--").append(BOUNDARY).append(
	            "\r\n");
	          strBuf.append("Content-Disposition: form-data; name=\"" + 
	            inputName + "\"\r\n\r\n");
	          strBuf.append(inputValue);
	        }
	        out.write(strBuf.toString().getBytes());
	      }

	      if (fileMap != null) {
	        Iterator iter = fileMap.entrySet().iterator();
	        while (iter.hasNext()) {
	          Map.Entry entry = (Map.Entry)iter.next();
	          String inputName = (String)entry.getKey();
	          String inputValue = (String)entry.getValue();
	          if (inputValue == null) {
	            continue;
	          }
	          File file = new File(inputValue);
	          String filename = file.getName();
	          String contentType = new MimetypesFileTypeMap().getContentType(file);

	          if (filename.endsWith(".png")) {
	            contentType = "image/png";
	          }
	          if ((contentType == null) || (contentType.equals(""))) {
	            contentType = "application/octet-stream";
	          }
	          StringBuffer strBuf = new StringBuffer();
	          strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
	          strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename + "\"\r\n");
	          strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
	          out.write(strBuf.toString().getBytes());
	          DataInputStream in = new DataInputStream(new FileInputStream(file));
	          int bytes = 0;
	          byte[] bufferOut = new byte[1024];
	          while ((bytes = in.read(bufferOut)) != -1) {
	            out.write(bufferOut, 0, bytes);
	          }
	          in.close();
	        }
	      }
	      byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
	      out.write(endData);
	      out.flush();
	      out.close();

	      StringBuffer strBuf = new StringBuffer();
	      BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

	      String line = null;
	      while ((line = reader.readLine()) != null) {
	        strBuf.append(line).append("\n");
	      }
	      res = strBuf.toString();
	      reader.close();
	      reader = null;
	    } catch (Exception e) {
	      System.out.println("发送POST请求出错。" + postUrl);
	      e.printStackTrace();
	    } finally {
	      if (conn != null) {
	        conn.disconnect();
	        conn = null;
	      }
	    }
	    return res;
	  }

	  public static void downloadImg(String mediaId, String access_token, String filePath)
	  {
	    InputStream is = null;
	    String url = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=" + access_token + "&media_id=" + mediaId;
	    try
	    {
	      URL urlGet = new URL(url);
	      HttpURLConnection conn = (HttpURLConnection)urlGet.openConnection();
	      conn.setRequestMethod("GET");
	      conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	      conn.setDoOutput(true);
	      conn.setDoInput(true);

	      System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
	      System.setProperty("sun.net.client.defaultReadTimeout", "30000");

	      conn.connect();

	      is = conn.getInputStream();
		  //String contentType = conn.getHeaderField("Content-Type");
			byte[] data = new byte[1024];
	      int len = 0;
	      FileOutputStream fileOutputStream = null;
	      try {
	        fileOutputStream = new FileOutputStream(filePath);

	        while ((len = is.read(data)) != -1)
	          fileOutputStream.write(data, 0, len);
	      }
	      catch (IOException e)
	      {
	        e.printStackTrace();

	        if (is != null) {
	          try {
	            is.close();
	          } catch (IOException e2) {
	            e2.printStackTrace();
	          }
	        }
	        if (fileOutputStream == null) return; try {
	          fileOutputStream.close();
	        }
	        catch (IOException e1) {
	          e1.printStackTrace();
	        }
	      }
	      finally
	      {
	        if (is != null) {
	          try {
	            is.close();
	          } catch (IOException e) {
	            e.printStackTrace();
	          }
	        }
	        if (fileOutputStream != null)
	          try {
	            fileOutputStream.close();
	          }
	          catch (IOException e) {
	            e.printStackTrace();
	          }
	      }
	      try
	      {
	        fileOutputStream.close();
	      }
	      catch (IOException e) {
	        e.printStackTrace();
	      }
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }
	  }

	  public Map<String, String> jsConfig(String jsapi_ticket, String url, String appId)
	  {
	    String timestamp = Long.toString(System.currentTimeMillis() / 1000L);
	    Map map = new HashMap();

	    map.put("noncestr", getRandomStringByLength(32));
	    map.put("jsapi_ticket", jsapi_ticket);
	    map.put("timestamp", timestamp);
	    map.put("url", url);

	    map.put("signature", getJsSign(map));
	    map.put("appid", appId);
	    return map;
	  }
	  
	  /**
     * 获取一定长度的随机字符串
     * @param length 指定字符串长度
     * @return 一定长度的字符串
     */
    public static  String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
    
    public String getJsSign(Map<String,String> map){
    	ArrayList<String> list = new ArrayList<String>();
        for(Map.Entry<String,String> entry:map.entrySet()){
            if(entry.getValue()!=""){
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString().substring(0,sb.length()-1);
        result = SHA1(result);
        return result.toLowerCase();
    }
    
    /**
     *  SHA1 加密
     * @param str 代加密字符串
     * @return
     */
    public static String SHA1(String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(str.getBytes());
            byte[] b = messageDigest.digest();
            String hs = "";
    		String stmp = "";
    		for (int n = 0; n < b.length; n++) {
    			stmp = (Integer.toHexString(b[n] & 0XFF));
    			if (stmp.length() == 1)
    				hs = hs + "0" + stmp;
    			else
    				hs = hs + stmp;
    		}
    		return hs.toUpperCase();

    		//以下算法结果同上
//            int len = b.length;
//            StringBuilder buf = new StringBuilder(len * 2);
//            // 把密文转换成十六进制的字符串形式
//            char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5',
//                    '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
//            for (int j = 0; j < len; j++) {
//                buf.append(HEX_DIGITS[(b[j] >> 4) & 0x0f]);
//                buf.append(HEX_DIGITS[b[j] & 0x0f]);
//            }
//            return buf.toString().toUpperCase();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String getJsTicket(String appid,String secret){
    	if(_jsticket_date == null) {
    		_jsticket_date = new Date();
    	}

    	Date now = new Date();

    	long delay = now.getTime() - _jsticket_date.getTime();
    	if(_jsticket!=null && delay <=5000000){
			return _jsticket;
		}

    	_jsticket_date = now;
		System.out.println("appid====="+appid);
		System.out.println("secret====="+secret);
    	String accessToken = this.getAccessToken(appid, secret);
		System.out.println("accessToken====="+accessToken);
		HttpClient client = new HttpClient();
		String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+accessToken+"&type=jsapi";
		client.send(url,HttpClient.METHOD.GET);
		String content = client.getContent();
		System.out.println("ticket====="+content);
		client.close();

		// TODO 解析json 获取ticket信息
		String regex = "\"ticket\":\"([^\"]+)\"";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		if (m.find()) {
			_jsticket = m.group(1);
		}

		return _jsticket;
    }

    public static Message parse(InputStream in) {
        Message msg = null;

        try {
        	/*BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			StringBuffer sb = new StringBuffer();
			String line = null;
			while((line = br.readLine())!=null){
				sb.append(line).append("\n");
			}
			logger.debug("xml{}",sb.toString());
			*/
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            //Document document = db.parse(new InputSource(new ByteArrayInputStream(sb.toString().getBytes("UTF-8"))));
            Document document = db.parse(in);
            NodeList xmls = document.getElementsByTagName("xml");
			Element xml = (Element) xmls.item(0);

            NodeList childNodes = xml.getChildNodes();

            msg = new Message();
            Field[] fields = msg.getClass().getDeclaredFields();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node el = childNodes.item(i);
                for (Field f : fields) {

                    if (f.getName().equalsIgnoreCase(el.getNodeName())) {
                        try {
                        	Method m = msg.getClass().getDeclaredMethod("set"+f.getName(),f.getType());
                        	if(m!=null) {
	                            if (f.getType() == Long.class) {
	                            	m.invoke(msg, Long.parseLong(el.getTextContent()));
	                            } else {
	                            	m.invoke(msg,el.getTextContent());
	                            }
                        	}
                        } catch (IllegalArgumentException ex) {
                            ex.printStackTrace();
                        } catch (IllegalAccessException ex) {
                            ex.printStackTrace();
                        } catch (NoSuchMethodException e) {
							e.printStackTrace();
						} catch (SecurityException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						} catch (DOMException e) {
							e.printStackTrace();
						}
                        break;
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }
}
