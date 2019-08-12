package com.xmbl.config;

import java.util.Date;

import com.xmbl.util.DateUtils;
import com.xmbl.util.PropertyUtil;

/**
 * Copyright © 2018 noseparte © BeiJing BoLuo Network Technology Co. Ltd.
 * @Author Noseparte
 * @Compile 2018年7月18日 -- 上午10:53:31
 * @Version 1.0
 * @Description		alipay支付宝相关配置
 */
public class AlipayConfig {

	// 应用网关
	public final static String gatewayUrl = "https://openapi.alipay.com/gateway.do";
	// 商户ID
	public final static String app_id = "2018070560565457";
	// 支付宝公钥
	public final static String ALIPAY_PUBLIC_KEY = "";
	// 应用私钥
	public final static String APP_PRIVATE_KEY = "";
	// AES密钥
	public final static String AES_KEY = "";
	// 异步通知地址
	public static String NOTIFY_URL = PropertyUtil.getProperty("conf/env.properties", "alipay.notifyurl");
	
	// 签名类型			
	public final static String sign_type = "RSA2";
	// 编码格式
	public final static String charset = "UTF-8";
	// 仅支持JSON 
	public final static String format = "JSON";
	// 接口名称
	public final static String method = "alipay.trade.app.pay";
	// 调用的接口版本
	public final static String version = "1.0";
	// 支付场景 
	public final static String scene = "bar_code";
	// 调用的接口版本
	public final static String timestamp = DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	
//	public static void main(String[] args) {
//		String parivate_key = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCynujVSLDVjDomi92KycwPE5UiFDrTenEOKdbqklJ9XGwe/7ergyR7yE1w1ap+qKkkH7NucyrONrHT3BD2QJKgG6y4fIjpPuSEr4V0abxjiMhmRVwy2G/fN0bS4XnY+0pda6icVKqivP/KBs6m0+VAK5TqhG/HVorg1pqr0QU3wOD7xkACQ0cy4Fmv9OCxIiqRi0tZW8Cm6lJZMhOolFDpietsqGv2cw7HWukLDBoxDAM7Et9TvHIONquz6NKQ34ISykvGzAHNJW22+P7lFoJxXOpHOKpQr32jPQyNYTt4oU5mxQD1nIr3ug2LKPjCQMF8Vadv87MtVw+zjEQtxNCxAgMBAAECggEBAKG5s8J5IHMZlgUUmjBxaes0pdDHVEPqFrbmNwsIFNwgdFzU163Y/Y193HAPZWFswEhO051rdRRk/Tl/hXQRHzEMHaSkTSoyoaFpcoHmwoW0cQ34OvRmd1Q5rkx9jl9hlwFi2rVBC4ZxbX+0DiOKs4UYcKqt4q8vsmrEpCJxDDJfUpX9HCSzOb44H8EMkSxo+tESjboHo9GAZ9alY++c4GWm9pPomb0JgYvpJOmW3XCgq4DeTtxrBL60XxLD+NiABtMrjG807mRsax41Lm/aze1tzy54DYefv2bnWbxh0rJxg349cTri0rtv8w6RdZIYwazUPZeUl9YAR4Tp8SgrDmECgYEA5eod0lBIYhd3Shvf7YgFXpjwj9G4zycbiV7utZKz6eHtOPzTVVMX786hLnG4xKGtJuKOtOtDvJbLivS85yYjccLw8ajIdxSpuGaWzEag9fCBHjHAyFbn+1Z24CyoI+vE7Okor2ysOtlhCd1DRJbyjUHeeSU1g9qmHXrsWObxJBUCgYEAxuL2K8hdfcYtpRawyfJNqdqJZPUMjqnABkUpg5BAZ0uBEUTxh5Qc0giQg/W6viYRQ4Tm+xYI9t0DwBouS0uQiSHSpAaovKXrdVkUcXb6cBatTdR21rMY9TqrD0rMwHPhXk7L+ebwEHti4h7k89dOJzoaf/qkt0e/7WjyYbPs1S0CgYB1qtUqXwY8la/qkMuRIK1G1dVAdeA0vNod+yUGbpd/mq4zHUn3gDhe9gyN3YvnrqsUnJzWwRWAvlhbvJe1YVKAlXmE5JH8R+n4wyBoWbllRXp/fo0gu0Gs43qhjplbEP5PFaFJ7r9SuxAr4T7dKrfmTZDTxZRW8B9WNSQi98bwZQKBgQChtUv+0UzixM28GzTnHfHzltJ7mZr6LB8LehSE+jbDRerNRcc0O0MrqvcFtDh//qeuxFKh+IqVXaJ9AUx+wBPmy+9emmN771QA98rdbqW1mVqTm5p8euZiDa+kIWdVW2L0sRC8Qf8TB5+gxwnVEUxkLKP7RqnCs73jpJ7evdJkiQKBgQDPysrAuEdnBcKqOOraATtflo9z+2mxVR2LfCLmD2RnS22AgtAdIfVIzH5LCyrxu9Cq0K+oQZhM8N5XTDrVh2RuF3XTFyczhpge3Uo6oN7U6Q2rYTwim6HkPmu9N15ksRZL1dMmLuT3n/yAY/TAynHjeyL9UArTiyf9BG4Ps1pqMw==";
//		boolean flag = parivate_key.equals(APP_PRIVATE_KEY);
//		System.out.println(flag);
//	}
	
}
