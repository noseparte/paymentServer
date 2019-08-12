package com.xmbl.constant;

import com.xmbl.util.PropertyUtil;

/**
 * 小米菠萝对应创识科技中注册的商家信息
 * 
 * @author sunbenbao
 *
 */
public class MchInfoConstant {
	
	// 当前环境
	public  static final String MCH_ENV = PropertyUtil.getProperty("conf/env.properties", "env");
	
	// 小米菠萝在创识科技注册的商户id 后期会动态获取
	public  static final String MCH_ID = PropertyUtil.getProperty("conf/env.properties", "mchId");
	
	// 小米菠萝在创识科技交易密钥秘钥（传输提交参数时使用）
	public static final String MCH_KEY=PropertyUtil.getProperty("conf/env.properties", "mchKey");
	
	// 小米菠萝登录地址
	public static final String MCH_LOGIN_ADDRESS=PropertyUtil.getProperty("conf/env.properties", "mchLoginAddress");
	
	// 商户服务平台登录用户
	public static final String MCH_USERNAME = PropertyUtil.getProperty("conf/env.properties", "mchUsername");
	
	//	口令
	public static final String MCH_PASSWORD = PropertyUtil.getProperty("conf/env.properties", "mchPassword");
	
	// 终端设备号(门店号)
	//public static final String DEVICE_INFO_STORE_NUMBER = "STORE_NUMBER1";
	
	// 终端设备号(收银设备ID)
	//public static final String DEVICE_INFO_CASHIER_DEVICE_ID="CASHIER_DEVICE_ID1";
	
	// 终端设备号 (PC网页或公众号内支付请传"WEB")
	//public static final String DEVICE_INFO_PCORJSPAY = "WEB";
	
	// 小米菠萝企业版微信公众号
	public static final String MCH_WEIXIN_JSPAY_ID = PropertyUtil.getProperty("conf/env.properties", "mchWeixinJspayId");
	
	// 微信支付分配的商户号
	public static final String MCH_WEIXIN_JSPAY_MCHID = PropertyUtil.getProperty("conf/env.properties", "mchWeixinJspayMchid");
	
	// 微信安卓移动版
	public static final String MCH_WEIXIN_ANDROID_SCH_CUSTOM_PARAM = PropertyUtil.getProperty("conf/env.properties", "android.sch_custom_param");
	
	// 微信苹果移动版
	public static final String MCH_WEIXIN_IOS_SCH_CUSTOM_PARAM = PropertyUtil.getProperty("conf/env.properties", "ios.sch_custom_param");
	
	// 微信wap移动版
	public static final String MCH_WEIXIN_WAP_SCH_CUSTOM_PARAM = PropertyUtil.getProperty("conf/env.properties", "wap.sch_custom_param");
	
	// app 应用名
	public static final String MCH_WEIXIN_APP_NAME = PropertyUtil.getProperty("conf/env.properties", "app_name");
	
	// 安卓 包名
	public static final String MCH_WEIXIN_ANDROID_PACKAGE_NAME = PropertyUtil.getProperty("conf/env.properties", "android.package_name");
	
	// 苹果 包名
	public static final String MCH_WEIXIN_IOS_BUNDLE_ID = PropertyUtil.getProperty("conf/env.properties", "ios.bundle_id");
	
	// WAP 应用官网名称
	public static final String MCH_WEIXIN_WAP_WAPNAME = PropertyUtil.getProperty("conf/env.properties", "wap.wap_name");
	
	// wap 应用官网链接
	public static final String MCH_WEIXIN_WAP_WAPURL = PropertyUtil.getProperty("conf/env.properties", "wap.wap_url");
	
	// 第三方调用接口地址:金游url地址
	public static String JINYOU_URL = "https://pay.echase.cn/jygateway/api";
	
	// 金游支付完成页面显示
	public static String JINYOU_CALLBACKURL = "/jygateway/api/order/callbackurl";
	
	// 金游支付异步回调接口响应地址
	public static String JINYOU_NOTICE_URL="/jygateway/api/order/notice";
	
	// 商品发货服务id 1
	public static String SEND_GOODS_SERVER_ID = PropertyUtil.getProperty("conf/env.properties", "send_goos_server_id");
	
	// 商品发货ip  10.254.218.118
	public static String SEND_GOODS_IP = PropertyUtil.getProperty("conf/env.properties", "send_goos_ip");
	
	//	商品发货地址port  9223
	public static String SEND_GOODS_PORT = PropertyUtil.getProperty("conf/env.properties", "send_goos_port");
	
	// 金游服务名
	public static String JY_SERVER_NAME = PropertyUtil.getProperty("conf/env.properties", "jy_server_name");
}
