package com.xmbl.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 读取配置文件 util
 * @author sunbenbao
 *
 */
public class PropertyUtil {
	private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);
    private static Properties props;

    private synchronized static void loadProps(String fileName){
        logger.info("开始加载properties文件内容.......");
        props = new Properties();
        InputStreamReader in = null;
        try {
        	// 第一种，通过类加载器进行获取properties文件流
        	in = new InputStreamReader(PropertyUtil.class.getClassLoader().getResourceAsStream(fileName),"UTF-8");
        	// 第二种，通过类进行获取properties文件流
            //in = PropertyUtil.class.getResourceAsStream("/" + fileName);
            props.load(in);
        } catch (FileNotFoundException e) {
            logger.error(fileName+"文件未找到");
        } catch (IOException e) {
            logger.error("出现IOException");
        } finally {
            try {
                if(null != in) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error(fileName+"文件流关闭出现异常");
            }
        }
        logger.info("加载properties文件内容完成...........");
        logger.info("properties文件内容：" + props);
    }

    /**
     * 通过文件名 和 键值获取对应value值
     * @param fileName 文件名
     * @param key 键
     * @return
     */
    public static String getProperty(String fileName,String key){
        if(null == props || (props!=null && (props.get("propName") == null || !props.get("propName").equals(fileName)))) {
            loadProps(fileName);
        }
//        boolean hasEnv = getIsHasEnv(fileName,key);
        String envStr = String.valueOf(props.get("env"));
        if (key!=null && !key.equals("env") && key.indexOf(envStr)==-1 && getIsHasEnv(fileName,key)) {// 有环境
        	String env =props.getProperty("env");
        	return props.getProperty(env+"."+key);
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
        	return props.getProperty(env+"."+key,defaultValue);
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
    	String mchId = getProperty("conf/env.properties", "dev.mchId");
    	System.out.println(mchId);
    }
    
}
