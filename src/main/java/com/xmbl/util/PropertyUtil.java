package com.xmbl.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
/**
 * 读取配置文件 util
 * @author sunbenbao
 *
 */
public class PropertyUtil {
    private static Properties props;
    private synchronized static void loadProps(String fileName){
        XmblLoggerUtil.info("开始加载properties文件内容.......");
        props = new Properties();
        InputStreamReader in = null;
        try {
        	// 第一种，通过类加载器进行获取properties文件流
        	in = new InputStreamReader(PropertyUtil.class.getClassLoader().getResourceAsStream(fileName),"UTF-8");
        	// 第二种，通过类进行获取properties文件流
            //in = PropertyUtil.class.getResourceAsStream("/" + fileName);
            props.load(in);
        } catch (FileNotFoundException e) {
        	XmblLoggerUtil.error(fileName,"文件未找到");
        } catch (IOException e) {
            XmblLoggerUtil.error("出现IOException");
        } finally {
            try {
                if(null != in) {
                    in.close();
                }
            } catch (IOException e) {
                XmblLoggerUtil.error(fileName,"文件流关闭出现异常");
            }
        }
        XmblLoggerUtil.info("加载properties文件内容完成...........");
        XmblLoggerUtil.info("properties文件内容：" ,props.toString());
    }

    /**
     * 通过文件名 和 键值获取对应value值
     * @param fileName 文件名
     * @param key 键
     * @return
     */
    public static String getProperty(String fileName,String key){
//    	logger.info(String.valueOf(props.get("propName")));
        if(null == props || (props!=null && (String.valueOf(props.get("propName")) == null || !String.valueOf(props.get("propName")).equals(fileName)))) {
            loadProps(fileName);
        }
//        boolean hasEnv = getIsHasEnv(fileName,key);
        String envStr = String.valueOf(props.get("env"));
        if ("dev.env".equals(key) || "test.env".equals(key) || "pro.env".equals(key) || "env".equals(key)) {
        	return envStr;
        }
        if (key!=null && key.indexOf(envStr) < 0 && getIsHasEnv(fileName,key)) {// 有环境
        	StringBuilder sb = new StringBuilder();
        	sb.append(envStr).append(".").append(key);
        	return props.getProperty(sb.toString());
        } else {//没有环境
        	return props.getProperty(key);
        }
    }

    /**
     * 通过文件名 和 键值获取对应value值
     * 备注: 若没找到则取默认值
     * @param fileName 文件名
     * @param key 键
     * @param defaultValue 值
     * @return
     */
    public static String getProperty(String fileName,String key, String defaultValue) {
        if(null == props || (props!=null && (props.get("propName") == null || !props.get("propName").equals(fileName)))) {
            loadProps(fileName);
        }
        String envStr = String.valueOf(props.get("env"));
        if (key!=null && !key.equals("env") && key.indexOf(envStr)==-1 && getIsHasEnv(fileName,key)) {// 有环境
        	String env =props.getProperty("env");
        	StringBuilder sb = new StringBuilder();
        	sb.append(env).append(".").append(key);
        	return props.getProperty(sb.toString(),defaultValue);
        } else {//没有环境
        	return props.getProperty(key,defaultValue);
        }
    }
    
    public static boolean getIsHasEnv(String fileName,String key) {
    	if(null == props || (props!=null && (props.get("propName") == null || !props.get("propName").equals(fileName)))) {
            loadProps(fileName);
        }
    	 // 判断是否配置开发 测试 生产环境
        String env =props.getProperty("env");
        return env != null && !"".equals(env);
    }
    
    public static void main(String[] args){
    	String env = getProperty("conf/env.properties", "env");
    	System.out.println(env);
    	String rest_url = getProperty("conf/env.properties", "sms.rest_url");
    	System.out.println(rest_url);
    }
    
}
