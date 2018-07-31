package com.xmbl.util;

import java.util.Random;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Copyright © 2017 noseparte(Libra) © Like the wind, like rain
 * @Author Noseparte
 * @Compile 2017年11月6日 -- 上午11:20:41
 * @Version 1.0
 * @Description	微信支付生成随机数算法
 */

@Slf4j
public class WXRandomNumberUtil {

	/**
	 * 生成随机数算法
	 * 
	 * @param length
	 * @return
	 */
    public static String getRandomStringByLength(int length) {  
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";  
        Random random = new Random();  
        StringBuffer sb = new StringBuffer();  
        for (int i = 0; i < length; i++) {  
            int number = random.nextInt(base.length());  
            sb.append(base.charAt(number));  
        }  
        return sb.toString();  
    }  
    
    
    
    /**
     * 微信支付交易金额格式化
     * 
     * @param amount
     * @return
     */
    public static int wx_format_PayAmount(String amount) {
    	int pay_amount = 0;
//    	pay_amount = Integer.parseInt((amount.split("\\.")[0])) * 100;
    	pay_amount = (int)(Double.parseDouble(amount) * 100);
    	return pay_amount;
    }
    
    
    public static void main(String[] args) {
    	int amount = wx_format_PayAmount("0.01");
    	log.info("infoMsg:======== amount,{}",amount);
	}
    
    
}
