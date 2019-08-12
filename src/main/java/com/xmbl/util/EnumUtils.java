package com.xmbl.util;

import com.xmbl.enumeration.EnumSortTypeCode;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  EnumUtils 
 * @创建时间:  2017年12月22日 上午10:27:52
 * @修改时间:  2017年12月22日 上午10:27:52
 * @类说明: 枚举类工具类
 */
public class EnumUtils {
	
	public static String getTypeNameByTypeId(String typeId) {
		String typeName = "";
		for (EnumSortTypeCode enumSortTypeCode : EnumSortTypeCode.values()) {
			if (enumSortTypeCode.getId().equals(typeId)) {
				typeName = enumSortTypeCode.getType();
				return typeName;
			}
		}
		return typeName;
	}
}
