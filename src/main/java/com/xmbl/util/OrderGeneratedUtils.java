package com.xmbl.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * Copyright © 2017 noseparte(Libra) © Like the wind, like rain
 * @Author Noseparte
 * @Compile 2017年11月2日 -- 下午5:18:35
 * @Version 1.0
 * @Description	订单生成工具类
 */
public class OrderGeneratedUtils {

	 /** 
     * 锁对象，可以为任意对象 
     */  
    private static Object lockObj = "lockerOrder";  
    /** 
     * 订单号生成计数器 
     */  
    private static long orderNumCount = 0L;  
    /** 
     * 每毫秒生成订单号数量最大值 
     */  
    private static int maxPerMSECSize=1000; 
    
    /**  
     * 生成订单编号  
     * @return  
     */    
    public static synchronized String getOrderNo() {    
    	try {  
            // 最终生成的订单号  
            String finOrderNum = "";  
            synchronized (lockObj) {  
                // 取系统当前时间作为订单号变量前半部分，精确到毫秒  
                long nowLong = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));  
                // 计数器到最大值归零，可扩展更大，目前1毫秒处理峰值1000个，1秒100万  
                if (orderNumCount >= maxPerMSECSize) {  
                    orderNumCount = 0L;  
                }  
                //组装订单号  
                String countStr=maxPerMSECSize +orderNumCount+"";  
                finOrderNum=nowLong+countStr.substring(1);  
                orderNumCount++;  
                System.out.println(finOrderNum);  
                // Thread.sleep(1000);  
            }  
            return finOrderNum;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
    	return null;  
    }    
	
    
    public static void main(String[] args) {  
//        // 测试多线程调用订单号生成工具  
//        try {  
//            Thread t1 = new Thread(new Runnable() {  
//                public void run() {  
//                	OrderGeneratedUtils makeOrder = new OrderGeneratedUtils();  
//                    makeOrder.getOrderNo();  
//                }  
//            });  
//            t1.start();  
//        } catch (Exception e) {  
//            e.printStackTrace();  
//        }  
    }
    
}
