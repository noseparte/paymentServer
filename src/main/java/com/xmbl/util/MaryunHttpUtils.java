/**
 * 
 */
package com.xmbl.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import com.alibaba.fastjson.JSON;

/**
 * Copyright © 2017 noseparte(Libra) © Like the wind, like rain
 * @Author Noseparte
 * @Compile 2017年10月24日 -- 下午6:03:20
 * @Version 1.0
 * @Description	Apache HttpClient Util
 */
public class MaryunHttpUtils {
	
	    public static class UHeader{
	    	/**
	    	 * the title of header.
	    	 */
	    	private String headerTitle;
	    	private String headerValue;
			public  UHeader(String headerTitle, String headerValue) {
				super();
				this.headerTitle = headerTitle;
				this.headerValue = headerValue;
			}
			public String getHeaderTitle() {
				return headerTitle;
			}
			public void setHeaderTitle(String headerKey) {
				this.headerTitle = headerKey;
			}
			public String getHeaderValue() {
				return headerValue;
			}
			public void setHeaderValue(String headerValue) {
				this.headerValue = headerValue;
			}
	    }
	    public static String getResponse(String url,HashMap<String,String> args,List<UHeader> headerList){
	    	String info = "";
	    	try {
	    		HttpClient client = new HttpClient();
//		    	client = setProxy(client);
//		    	client.getHostConfiguration().setProxy("10.170.187.42", 3128);
		    	client.setConnectionTimeout(60000);
		    	client.setTimeout(60000);
				GetMethod method = new GetMethod(url);
				client.getParams().setContentCharset("UTF-8");
				if(headerList.size()>0){
					for(int i = 0;i<headerList.size();i++){ 
						UHeader header = headerList.get(i);
						method.setRequestHeader(header.getHeaderTitle(),header.getHeaderValue());
					}
				}
				Iterator it = args.entrySet().iterator();
				while(it.hasNext()){
					Entry entry = (Entry)it.next();
					HttpMethodParams pram = new HttpMethodParams();
					pram.setParameter(entry.getKey().toString(), entry.getValue().toString());
					method.setParams(pram);
				}
				method.getParams().setParameter(
						HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
				client.executeMethod(method);
				info = new String(method.getResponseBody(), "UTF-8");
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	return info;
	    }
	    public static String httpGet(String url,HashMap<String,String> args,List<UHeader> headerList){
	    	String info = "";
	    	try {
		    	HttpClient client = new HttpClient();
		    	//client = setProxy(client);
//		    	client.getHostConfiguration().setProxy("10.170.187.42", 3128);
		    	Iterator it = args.entrySet().iterator();
		    	String sargs = "";
				while(it.hasNext()){
					Entry entry = (Entry)it.next();
					sargs += "&"+entry.getKey().toString()+"="+entry.getValue();
				}
				if(!"".equals(sargs)){
					sargs = "?"+sargs.substring(1, sargs.length());
				}
				System.out.println(url+sargs);
				GetMethod method = new GetMethod(url+sargs);
//				client.getParams().setContentCharset("UTF-8");
				if(headerList!=null&&headerList.size()>0){
					for(int i = 0;i<headerList.size();i++){
						UHeader header = headerList.get(i);
						method.setRequestHeader(header.getHeaderTitle(),header.getHeaderValue());
					}
				}
				client.executeMethod(method);
				info = new String(method.getResponseBody(), "UTF-8");
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	return info;
	    }
	    public static String getPostResponse(String url,Map<String, Object> map,List<UHeader> headerList){
	    	String info = "";
	    	try {
		    	HttpClient client = new HttpClient();
		    	//client = setProxy(client);
		    	
//		    	client.getHostConfiguration().setProxy("10.170.187.42", 3128);
				PostMethod method = new PostMethod(url);
				client.getParams().setContentCharset("UTF-8");
				if(headerList.size()>0){
					for(int i = 0;i<headerList.size();i++){
						UHeader header = headerList.get(i);
						method.setRequestHeader(header.getHeaderTitle(),header.getHeaderValue());
					}
				}
				Iterator it = map.entrySet().iterator();
				while(it.hasNext()){
					Entry entry = (Entry)it.next();
					method.addParameter(entry.getKey().toString(), entry.getValue().toString());
				}
				method.getParams().setParameter(
						HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
				client.executeMethod(method);
				info = new String(method.getResponseBody(), "UTF-8");
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	return info;
	    }
	    public static String getPostResponseHeader(String url,HashMap<String,String> args,List<UHeader> headerList,String headerName){
	    	String info = "";
	    	try {
		    	HttpClient client = new HttpClient();
				PostMethod method = new PostMethod(url);
				client.getParams().setContentCharset("UTF-8");
				if(headerList.size()>0){
					for(int i = 0;i<headerList.size();i++){
						UHeader header = headerList.get(i);
						method.setRequestHeader(header.getHeaderTitle(),header.getHeaderValue());
					}
				}
				Iterator it = args.entrySet().iterator();
				while(it.hasNext()){
					Entry entry = (Entry)it.next();
					method.addParameter(entry.getKey().toString(), entry.getValue().toString());
				}
				method.getParams().setParameter(
						HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
				client.executeMethod(method);
				Header h = method.getResponseHeader(headerName);
				String rb = new String(method.getResponseBody(), "UTF-8");
				Map map = new HashMap();
				map.put("ErrorCode", rb);
				map.put(headerName,  h.getValue());
				info = JSON.toJSONString(map);
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
	    	return info;
	    }
	    public static String getPostResponse(String url,String argJson,List<UHeader> headerList){
	    	String info = "";
	    	try {
		    	HttpClient client = new HttpClient();
				PostMethod method = new PostMethod(url);
				client.getParams().setContentCharset("UTF-8");
				if(headerList.size()>0){
					for(int i = 0;i<headerList.size();i++){
						UHeader header = headerList.get(i);
						method.setRequestHeader(header.getHeaderTitle(),header.getHeaderValue());
					}
				}
				method.getParams().setParameter(
						HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
				if(argJson != null && !argJson.trim().equals("")) {
					RequestEntity requestEntity = new StringRequestEntity(argJson,"application/json","UTF-8");
//					System.out.println("requestEntity:=================" + JSON.toJSONString(requestEntity));
					method.setRequestEntity(requestEntity);
				}
				method.releaseConnection();
				client.executeMethod(method);
				InputStream inputStream = method.getResponseBodyAsStream();  
				BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));  
				StringBuffer stringBuffer = new StringBuffer();  
				String str= "";  
				while((str = br.readLine()) != null){  
					stringBuffer.append(str);  
				}  
				info = new String(stringBuffer);
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	return info;
	    }
	    public static String getPostResponseHeader(String url,String argJson,List<UHeader> headerList,String headerName){
	    	String info = "";
	    	try {
		    	HttpClient client = new HttpClient();
				PostMethod method = new PostMethod(url);
				client.getParams().setContentCharset("UTF-8");
				if(headerList.size()>0){
					for(int i = 0;i<headerList.size();i++){
						UHeader header = headerList.get(i);
						method.setRequestHeader(header.getHeaderTitle(),header.getHeaderValue());
					}
				}
				method.getParams().setParameter(
						HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
				if(argJson != null && !argJson.trim().equals("")) {
					RequestEntity requestEntity = new StringRequestEntity(argJson,"application/json","UTF-8");
					method.setRequestEntity(requestEntity);
				}
				method.releaseConnection();
				Header h =  method.getResponseHeader(headerName);
				info = h.getValue();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	return info;
	    }

}
