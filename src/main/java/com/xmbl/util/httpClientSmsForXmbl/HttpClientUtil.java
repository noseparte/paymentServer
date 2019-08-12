package com.xmbl.util.httpClientSmsForXmbl;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.xmbl.util.XmblLoggerUtil;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  SendSmsPostUtil 
 * @创建时间:  2018年1月2日 下午8:56:05
 * @修改时间:  2018年1月2日 下午8:56:05
 * @类说明: httpClient工具类
 */
public class HttpClientUtil {
	
	/** 
     *  post 请求
     *  
     * @param url       资源地址 
     * @param map   参数列表 
     * @param encoding  编码 
     * @return 
     * @throws ParseException 
     * @throws IOException 
     */  
    public static String send(String url, Map<String,String> map) {  
    	XmblLoggerUtil.info("httpClient post 调用开始....");
		String encoding = "utf-8";
    	String body = "";  
    	try {
            //创建httpclient对象  
            CloseableHttpClient client = HttpClients.createDefault();  
            //创建post方式请求对象  
            HttpPost httpPost = new HttpPost(url);  
              
            //装填参数  
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();  
            if(map!=null){  
                for (Entry<String, String> entry : map.entrySet()) {  
                    nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));  
                }  
            }  
            //设置参数到请求对象中  
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));  
      
            XmblLoggerUtil.info("请求地址：",url);  
            XmblLoggerUtil.info("请求参数：",nvps.toString());  
              
            //设置header信息  
            //指定报文头【Content-type】、【User-Agent】  
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");  
            httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");  
              
            //执行请求操作，并拿到结果（同步阻塞）  
            CloseableHttpResponse response = client.execute(httpPost);  
            //获取结果实体  
            HttpEntity entity = response.getEntity();  
            if (entity != null) {  
                //按指定编码转换结果实体为String类型  
                body = EntityUtils.toString(entity, encoding);  
            }  
            EntityUtils.consume(entity);  
            //释放链接  
            response.close();  
            XmblLoggerUtil.info("httpClient post 调用结束....");
    	} catch (Exception e) {
    		XmblLoggerUtil.error("httpClient post 调用出错了,错误信息为:", e.getMessage());
    	}
    	return body;
    }
    
    public static void main(String[] args) throws ParseException, IOException {
    	String url = "http://18bd746871.iok.la:26242/templateSMSServer/sms/send";
    	Map<String,String> map = new HashMap<String,String>();
    	JSONObject jsonObj = new JSONObject();
    	jsonObj.put("appId", "8aaf07085dcad420015dcff42f4f01e8");
//    	jsonObj.put("mobiles", "17611478870");
    	jsonObj.put("mobiles", "18756977291");
    	jsonObj.put("templateId","198516");
    	jsonObj.put("params", "1245634,5");
    	String jsonStr = JSONObject.toJSONString(jsonObj);
    	map.put("jsonData", jsonStr);
        String body = send(url, map);  
        System.out.println("交易响应结果：");  
        System.out.println(body);  
    }
}
