package com.xmbl.util;

import java.util.UUID;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  UUIDUtil 
 * @创建时间:  2017年12月21日 上午10:52:03
 * @修改时间:  2017年12月21日 上午10:52:03
 * @类说明:
 */
public class UUIDUtil {
	
	/**
	 * 生成uuid
	 * @return
	 */
	public static String generateUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
