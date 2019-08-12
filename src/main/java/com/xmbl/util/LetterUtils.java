package com.xmbl.util;
/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  LetterUtils 
 * @创建时间:  2017年12月21日 下午8:16:03
 * @修改时间:  2017年12月21日 下午8:16:03
 * @类说明:
 */
public class LetterUtils {
	
	/**
	 *  获取全部转大写字母
	 * @return
	 */
	public static String toUpperCaseLetter(String letter) {
		return letter.toUpperCase();
	}
	
	/**
	 *  获取全部转小写字母
	 * @return
	 */
	public static String toLowerCaseLetter(String letter) {
		return letter.toLowerCase();
	}
	
	public static void main(String [] args) {
		System.out.println(toUpperCaseLetter("abccddA"));
		System.out.println(toLowerCaseLetter("abccddA"));
	}
}
