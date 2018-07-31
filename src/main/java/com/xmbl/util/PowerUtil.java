package com.xmbl.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 加权验证工具类
 * <br></br>
 * 备注:暂时用于18位身份证最后一位验证计算
 * 
 * @author sunbenbao
 *
 */
public class PowerUtil {
	
	/**
	 * 17位身份证加权因子
	 */
	private static final List<Integer> POWER_WI_LIST= Arrays.asList(7,9,10, 5 ,8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2);
	
	 /**
	  * POWER_SUM/mod 取余
	  * 
	  * 通过模得到对应的校验码 
	  * 
	 * 	Y: 0 1 2 3 4 5 6 7 8 9 10 
	 * 	校验码: 1 0 X 9 8 7 6 5 4 3 2
	  */
	private static final List<String> POWER_SUM_LIST=Arrays.asList("1","0","X","9","8", "7","6","5","4","3", "2");
	
	/**
	 * 加权因子取模数
	 */
	private static final Integer modNumber = 11;
	
	/**
	 * 获取18位身份证号码最后一位是什么？
	 * @param powerStr 18位身份证
	 * @return 返回身份证最后一位
	 */
	public static String getPowerMod(String powerStr) {
		try {
			List<String> strLst = Arrays.asList(powerStr.split(""));
			List<Integer> intLst = new ArrayList<Integer>();
			for(int i=0;i<strLst.size();i++) {
				if (strLst.get(i) != null && !"".equals(strLst.get(i).trim()) && RegexUtil.checkRegex(RegexUtil.INTEGER_NEGATIVE, strLst.get(i).trim())) {
//					powerSum +=POWER_WI_LIST.get(i) * Integer.parseInt(strLst.get(i).trim());
					intLst.add(Integer.parseInt(strLst.get(i).trim()));
				}
			}
			
			Integer powerSum = 0;
			for (int i = 0; i < intLst.size(); i++) {
				powerSum +=POWER_WI_LIST.get(i) * intLst.get(i);
			}
			// 加权和 取余 11
			Integer modNum = powerSum% modNumber;
			// 获取校验码
			String validateCodeStr = POWER_SUM_LIST.get(modNum);
			return validateCodeStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 验证身份证号码最后一位是否正确
	 * @param idcardStr 身份证号码
	 * @return  是否正确
	 */
	public static boolean idcardLastIsRight(String idcardStr){
		String idCard17Str = idcardStr.substring(0, 17);
		String powerModStr = getPowerMod(idCard17Str);
		if ( powerModStr!=null){// 取模后字符串
			String idCardLast1Str=idcardStr.substring(17);
			return idCardLast1Str !=null && powerModStr.equals(idCardLast1Str.trim());
		}
		return false;
	}
	
	public static void main(String[] args) {
//		System.out.println(idcardLastIsRight("340321198903168211"));
	}
}
