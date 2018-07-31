package com.xmbl.constant;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xmbl.util.PropertyUtil;
import com.xmbl.util.XmlParseUtil.XmlParseUtil;
import com.xmbl.util.msgUtil.MsgUtil;

/**
 * 商品信息常量初始化
 * 
 * @author sunbenbao
 *
 */
public class GoodsInfoConstant {
	private static Logger LOGGER = LoggerFactory.getLogger(GoodsInfoConstant.class);
	public static List<Map<String,String>> goodsInfoMapLst = null;
	
	static {
		try {
			String env = PropertyUtil.getProperty("conf/env.properties", "env");
			if (StringUtils.isBlank(env)) {// 如果未读取到配置环境,将默认设置为开发环境。
				// 防止环境被恶意删除
				env = "dev";
			}
			LOGGER.info(MsgUtil.generateLogMsg("当前开发环境:",env));
			goodsInfoMapLst = XmlParseUtil.getKeyValMapLstByFilePathAndLabelName("xml/"+env+"/main.commodityConfig.xml", "commodityConfig");
			LOGGER.info(MsgUtil.generateLogMsg("****************商品信息加载成功: ",String.valueOf(goodsInfoMapLst.size())," ****************"));
		} catch (Exception e) {
			LOGGER.error("商品信息加载异常。。。。",e.getMessage());
		}
	}
	
}
