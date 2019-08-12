package com.xmbl.util;

import java.awt.Color;
import java.security.MessageDigest;
import java.util.Random;

import lombok.extern.java.Log;

public final class Md5PasswordEncoder {
	/**
	 * MD5字符
	 */
	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
		'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String encode(final String password) throws Exception{
		if (password == null) {
			return null;
		}
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		messageDigest.update(password.getBytes());
		final byte[] digest = messageDigest.digest();
		return getFormattedText(digest);
	}

	private static String getFormattedText(byte[] bytes) {
		final StringBuilder buf = new StringBuilder(bytes.length * 2);
		for (int j = 0; j < bytes.length; j++) {
			buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
			buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
		}
		return buf.toString();
	}

	public static String getRandomString(final int leng){
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < leng; i++) {
			sb.append(filter(new Random().nextInt(10)));
		}
		return sb.toString();
	}

	/*
	 * 给定范围获得随机颜色
	 */
	public static Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	/** 把除了大小写字母和数字外的字符全过滤掉 **/
	private static char filter(int k) {
		//String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String str="0123456789";
		/**
		if (k < 65)
			k = 48 + (k % 10);
		if (k > 64 && k < 96)
			k = 65 + (k % 26);
		if (k > 96)
			k = 97 + (k % 26);
		return (char) k;
		 */
		return str.charAt(k);
	}

	public static void main(String[] args) {
		
		
		try {
			String encode = encode("xmbl888...");
			System.out.println(encode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
