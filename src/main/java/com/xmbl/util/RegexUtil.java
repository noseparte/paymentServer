package com.xmbl.util;

import java.util.HashMap;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.Assert;

/**
 * 正则表达式验证
 * 
 * <br>
 * </br>
 * 
 * @author sunbenbao
 * 
 */
public class RegexUtil {
	

	/**
	 * email 电子邮箱
	 * 
	 */
	public static final String EMAIL_STR = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

	/**
	 * email 电子邮箱
	 */
	public static final String EMAIL_STR2 = "\\w+(\\.\\w+)*@\\w+(\\.\\w+)+";

	/**
	 * 验证固话号码
	 * (^(\\d{2,4}[-_－—]?)?\\d{3,8}([-_－—]?\\d{3,8})?([-_－—]?\\d{1,7})?$)|
	 * (^0?1[35]\\d{9}$)
	 */
	public static final String PHONE_STR = "^(0\\d{2}-\\d{8}(-\\d{1,4})?)|(0\\d{3}-\\d{7,8}(-\\d{1,4})?)$";

	/**
	 * 验证手机号码
	 * 
	 * 移动号码段:139、138、137、136、135、134、150、151、152、157、158、159、182、183、187、188、147
	 * 联通号码段:130、131、132、136、185、186、145 电信号码段:133、153、180、189
	 * 
	 */
	public static final String MOBILE_STR = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$";

	/**
	 * Integer正则表达式 ^-?(([1-9]\d*$)|0)
	 */
	public static final String INTEGER = "^-?(([1-9]\\d*$)|0)";

	/**
	 * 正整数正则表达式 >=0 ^[1-9]\d*|0$
	 */
	public static final String INTEGER_NEGATIVE = "^[1-9]\\d*|0$";

	/**
	 * 负整数正则表达式 <=0 ^-[1-9]\d*|0$
	 */
	public static final String INTEGER_POSITIVE = "^-[1-9]\\d*|0$";

	/**
	 * Double正则表达式 ^-?([1-9]\d*\.\d*|0\.\d*[1-9]\d*|0?\.0+|0)$
	 */
	public static final String DOUBLE = "^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$";

	/**
	 * 正Double正则表达式 >=0 ^[1-9]\d*\.\d*|0\.\d*[1-9]\d*|0?\.0+|0$　
	 */
	public static final String DOUBLE_NEGATIVE = "^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0$";

	/**
	 * 负Double正则表达式 <= 0 ^(-([1-9]\d*\.\d*|0\.\d*[1-9]\d*))|0?\.0+|0$
	 */
	public static final String DOUBLE_POSITIVE = "^(-([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*))|0?\\.0+|0$";

	/**
	 * 年龄正则表达式 ^(?:[1-9][0-9]?|1[01][0-9]|120)$ 匹配0-120岁
	 */
	public static final String AGE = "^(?:[1-9][0-9]?|1[01][0-9]|120)$";

	/**
	 * 邮编正则表达式 [0-9]\d{5}(?!\d) 国内6位邮编
	 */
	public static final String CODE = "[0-9]\\d{5}(?!\\d)";

	/**
	 * 匹配由数字、26个英文字母或者下划线组成的字符串 ^\w+$
	 */
	public static final String STR_ENG_NUM_ = "^\\w+$";

	/**
	 * 匹配由数字和26个英文字母组成的字符串 ^[A-Za-z0-9]+$
	 */
	public static final String STR_ENG_NUM = "^[A-Za-z0-9]+";

	/**
	 * 匹配由26个英文字母组成的字符串 ^[A-Za-z]+$
	 */
	public static final String STR_ENG = "^[A-Za-z]+$";

	/**
	 * 过滤特殊字符串正则 regEx=
	 * "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“'。，、？]";
	 */
	public static final String STR_SPECIAL = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“'。，、？]";

	/***
	 * 日期正则 支持： YYYY-MM-DD YYYY/MM/DD YYYY_MM_DD YYYYMMDD YYYY.MM.DD的形式
	 */
	public static final String DATE_ALL = "((^((1[8-9]\\d{2})|([2-9]\\d{3}))([-\\/\\._]?)(10|12|0?[13578])([-\\/\\._]?)(3[01]|[12][0-9]|0?[1-9])$)"
			+ "|(^((1[8-9]\\d{2})|([2-9]\\d{3}))([-\\/\\._]?)(11|0?[469])([-\\/\\._]?)(30|[12][0-9]|0?[1-9])$)"
			+ "|(^((1[8-9]\\d{2})|([2-9]\\d{3}))([-\\/\\._]?)(0?2)([-\\/\\._]?)(2[0-8]|1[0-9]|0?[1-9])$)|(^([2468][048]00)([-\\/\\._]?)(0?2)([-\\/\\._]?)(29)$)|(^([3579][26]00)"
			+ "([-\\/\\._]?)(0?2)([-\\/\\._]?)(29)$)"
			+ "|(^([1][89][0][48])([-\\/\\._]?)(0?2)([-\\/\\._]?)(29)$)|(^([2-9][0-9][0][48])([-\\/\\._]?)"
			+ "(0?2)([-\\/\\._]?)(29)$)"
			+ "|(^([1][89][2468][048])([-\\/\\._]?)(0?2)([-\\/\\._]?)(29)$)|(^([2-9][0-9][2468][048])([-\\/\\._]?)(0?2)"
			+ "([-\\/\\._]?)(29)$)|(^([1][89][13579][26])([-\\/\\._]?)(0?2)([-\\/\\._]?)(29)$)|"
			+ "(^([2-9][0-9][13579][26])([-\\/\\._]?)(0?2)([-\\/\\._]?)(29)$))";

	/***
	 * 日期正则 支持： YYYY-MM-DD
	 */
	public static final String DATE_FORMAT1 = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)";

	/**
	 * URL正则表达式 匹配 http www ftp
	 */
	public static final String URL = "^(http|www|ftp|)?(://)?(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*((:\\d+)?)(/(\\w+(-\\w+)*))*(\\.?(\\w)*)(\\?)?"
			+ "(((\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*(\\w*%)*(\\w*\\?)*"
			+ "(\\w*:)*(\\w*\\+)*(\\w*\\.)*"
			+ "(\\w*&)*(\\w*-)*(\\w*=)*)*(\\w*)*)$";

	/**
	 * 15位和18位身份证 正则表达式简单验证
	 * 不包含18位身份证的验证区域加权年龄性别等特殊验证
	 * 
	 * <br></br>
	 * 身份证生成规则如下:
	 * 身份证18位分别代表的含义，身份证15位升级到18位，原来年用2位且没有最后一位，从左到右方分别表示
	 * ①1-2 升级行政区代码
	 * ②3-4 地级行政区划分代码
	 * ③5-6 县区行政区分代码
	 * ④7-10 11-12 13-14 出生年、月、日
	 * ⑤15-17 顺序码，同一地区同年、同月、同日出生人的编号，奇数是男性，偶数是女性
	 * ⑥18 校验码，如果是0-9则用0-9表示，如果是10则用X（罗马数字10）表示
	 * 
	 * 身份证原来的15位编码方式已经停用，而且15位没有使用校验位
	 * 最后一位可能出现的X并不是英文字母Ｘ，而是希腊数字10的缩写X
	 * 
	 * 规则:
	 * 1、15位身份证号码组成：
	 * ddddddyymmddxxs共15位，其中：
	 * dddddd为6位的地方代码，根据这6位可以获得该身份证号所在地。
	 * yy为2位的年份代码，是身份证持有人的出身年份。
	 * mm为2位的月份代码，是身份证持有人的出身月份。
	 * dd为2位的日期代码，是身份证持有人的出身日。
	 * 这6位在一起组成了身份证持有人的出生日期。
	 * xx为2位的顺序码，这个是随机数。
	 * s为1位的性别代码，奇数代表男性，偶数代表女性。
	 * 
	 * 2、18位身份证号码组成：
	 * ddddddyyyymmddxxsp共18位，其中：
	 * 其他部分都和15位的相同。年份代码由原来的2位升级到4位。最后一位为校验位。
	 * 校验规则是：
	 * （1）十七位数字本体码加权求和公式 
	 *         S = Sum(Ai * Wi), i = 0, ... , 16 ，先对前17位数字的权求和 
	 * 			Ai:表示第i位置上的身份证号码数字值 
	 * 			Wi:表示第i位置上的加权因子 
	 * 			Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2 
	 * （2）计算模 
	 * 		Y = mod(S, 11) 
	 * （3）通过模得到对应的校验码 
	 * 		Y: 0 1 2 3 4 5 6 7 8 9 10 
	 * 		校验码: 1 0 X 9 8 7 6 5 4 3 2
	 *     也就是说，如果得到余数为1 则最后的校验位p应该为对应的0.如果校验位不是，则该身份证号码不正确
	 *     
	 * 
	 */
	public static final String IDCARD="(^\\d{18}$)|(^\\d{15}$)";
	
	/**
	 * 机构代码
	 */
	public static final String JIGOU_CODE = "^[A-Z0-9]{8}-[A-Z0-9]$";

	/**
	 * 匹配数字组成的字符串 ^[0-9]+$
	 */
	public static final String STR_NUM = "^[0-9]+$";
	
	/**
	 * ip地址
	 * 匹配 127.0.0.1 | 255.255.255.0 | 192.168.0.1
	 * 不匹配 1200.5.4.3 | abc.def.ghi.jkl | 255.foo.bar.1
	 * 
	 */
	public static final String IP1 = "^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$";

	/**
	 * ip2地址
	 * 匹配 192.168.0.1 | 192.168.0.1/32 | 255.255.0.0/1
	 * 不匹配 010.0.0.0 | 192.168.0.1/33 | 256.0.1.55
	 */
	public static final String IP2 = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
			+"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
			+"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
			+"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
	
	/**
	 * xml 中字符串  key value 键值对获取
	 * <br></br>
	 * eg: 
	 * xml字符串如下:
	 * <commodityConfig id="1" item="60" 
	 *   reward="-1" firstReward="2" cost="6" tradeName="钻石" 
	 *   notice="方块王国通用货币60钻石" />
	 *   
	 *   <br></br>
	 *   匹配结果:
	 *   id="1" 和 item="60" 和 reward="-1" 和
	 *   firstReward="2" 和 firstReward="2" 和 
	 *   cost="6" 和 tradeName="钻石" 和
	 *   notice="方块王国通用货币60钻石"
	 * <br></br>
	 * 
	 */
	public static final String XML_KEY_VALUE_STRING = "(([\\w]+[\\s]*)=([\\s]*\"[([\\w\\s_\\.-]*[\u4e00-\u9fa5]*)]*\"[\\s]*))";
	
	// =====================================验证逻辑===================================================

	/**
	 * 判断字段是否为空 符合返回ture
	 * 
	 * @param str
	 * @return boolean
	 */
	public static synchronized boolean StrisNull(String str) {
		return null == str || str.trim().length() <= 0 ? true : false;
	}

	/**
	 * 判断字段是非空 符合返回ture
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean StrNotNull(String str) {
		return !StrisNull(str);
	}

	/**
	 * 字符串null赋值默认值
	 * 
	 * @param str
	 *            目标字符串
	 * @param defaut
	 *            默认值
	 * @return String
	 */
	public static String nulltoStr(String str, String defaut) {
		return StrisNull(str) ? defaut : str;
	}

	/**
	 * 判断字段是否超长 字串为空返回fasle, 超过长度{leng}返回ture 反之返回false
	 * 
	 * @param str
	 * @param leng
	 * @return boolean
	 */
	public static boolean isLengOut(String str, int leng) {
		return StrisNull(str) ? false : str.trim().length() > leng;
	}

	/**
	 * 过滤特殊字符串 返回过滤后的字符串
	 * 
	 * @param regex
	 *            正则表达式
	 * @param validateStr
	 *            验证字符串
	 * @param replaceStr
	 *            替换字符串
	 * @return
	 * 
	 *         eg: regex=STR_SPECIAL validateStr=18756977291@163.com
	 *         replaceStr=** return=18756977291**163**com
	 */
	public static String filterStr(String regex, String validateStr,
			String replaceStr) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(validateStr);
		return m.replaceAll(replaceStr).trim();
	}

	/**
	 * 验证字符串是否满足正则表达式
	 * 
	 * @param regex
	 *            正则表达式
	 * @param validateStr
	 *            要验证的字符串
	 * @return
	 */
	public static boolean checkRegex(String regex, String validateStr) {
		if (regex == null || "".equals(regex.toString()) || validateStr == null
				|| validateStr.trim().length() <= 0) {
			return false;
		}
		return Pattern.matches(regex, validateStr);
	}

	// #############################使用手机号码替换######################################
	/**
     *  将手机号码第4位到第7位替换成*
     * @param validateStr
     * @return
     */
    public static String phoneNumHide(String validateStr){
 	   String regex = "(\\d{3})\\d{4}(\\d{4})";
 	   String replaceStr = "$1****$2";
 	  return  validateStr.replaceAll(regex, replaceStr);
    }
    
    /**
     * 区域码验证
     * todo 
     * @param areaCode
     * @return
     */
    private static boolean  areaCodeCheck(String areaCode) {
		return true;
	}
    /**
     * 身份证验证(严格验证) 包含 15位和18位身份证验证
     * @param idCardStr 身份证号码
     * @return
     */
    public static boolean IdCardAllCheck(String idCardStr) {
    	if (StrisNull(idCardStr)) {// 身份证号码传入为空
    		return false;
    	}
    	// 简单验证15位和18位是否符合身份证验证规则
    	if (checkRegex(RegexUtil.IDCARD, idCardStr)){
    		// 验证通过后进入严格验证部分
    		// 根据身份证长度分别验证
    		try {
    			if (idCardStr.length() == 15) {// 身份证15位验证
        			// 前6位的地方代码验证
        			String idCardAreaCode = idCardStr.substring(0, 6);
        			Assert.isTrue(areaCodeCheck(idCardAreaCode), "6位区域码验证不通过");
        			// yymmdd身份证人出生日期验证
        			String yymmddStr = idCardStr.substring(6, 12);
        			Assert.isTrue(checkRegex(DATE_ALL,"19"+yymmddStr), "身份证日期格式不正确");
        			// xx为2位的顺序码
        			String randomCode2Str = idCardStr.substring(12, 14);
        			// 2位随机数字码验证
        			Assert.isTrue(checkRegex("^\\d{2}$",randomCode2Str), "2位的顺序码验证错误");
        			// s为1位的性别代码，奇数代表男性，偶数代表女性。
        			String randomCode1Str =  idCardStr.substring(14);
        			Assert.isTrue(checkRegex("^\\d{1}$",randomCode1Str), "1位的性别代码验证错误");
        		}
        		if (idCardStr.length() == 18) {// 身份证18位验证
        			// 前6位的地方代码验证
        			String idCardAreaCode = idCardStr.substring(0, 6);
        			Assert.isTrue(areaCodeCheck(idCardAreaCode), "6位区域码验证不通过");
        			// yyyymmdd身份证人出生日期验证
        			String yyyymmddStr = idCardStr.substring(6, 14);
        			Assert.isTrue(checkRegex(DATE_ALL,yyyymmddStr), "身份证日期格式不正确");
        			// xx为2位的顺序码
        			String randomCode2Str = idCardStr.substring(14, 16);
        			// 2位随机数字码验证
        			Assert.isTrue(checkRegex("^\\d{2}$",randomCode2Str), "2位的顺序码验证错误");
        			// s为1位的性别代码，奇数代表男性，偶数代表女性。
        			String randomCode1Str =  idCardStr.substring(16,17);
        			Assert.isTrue(checkRegex("^\\d{1}$",randomCode1Str), "1位的性别代码验证错误");
        			// 身份证加权验证
//        			String powerCode1Str =  idCardAreaCode.substring(17);
        			Assert.isTrue(PowerUtil.idcardLastIsRight(idCardStr), "身份证加权验证失败");
        		}
        		return true;
    		} catch (Exception e) {
    			e.printStackTrace();
    			return false;
    		}
    	}
    	return false;
	}
    
    /**
     * 通过xmlStr 获取满足正则表达式的结果 并存入map中
     * @param xmlStr
     * @return  能够匹配上的xml字符串
     */
    public static Map<String ,String> getKeyValOfXmlStr(String xmlStr) {
		Map<String,String> attrMap =  new HashMap<String,String>();
		Pattern p=Pattern.compile(XML_KEY_VALUE_STRING); 
		Matcher m=p.matcher(xmlStr); 
		String group = null;
		String key = null;
		String val = null;
		while(m.find()) { 
//			System.out.println(m.group()); 
//			System.out.println(m.start()); 
//			System.out.println(m.end()); 
			group = m.group();
			key = group.substring(0, group.indexOf("="));
			val = group.substring(group.indexOf("\"")+1, group.lastIndexOf("\""));
			attrMap.put(key, val);
		}
		return attrMap;
	}
    
	public static void main(String[] args) {
		// System.out.println(checkRegex(DATE_STR,"2007-2-29"));
		// System.out.println(checkRegex(EMAIL_STR2,"13340261412.629@qq.12.COM"));
		// System.out.println(checkRegex(PHONE_STR,"614129"));
		// System.out.println(checkRegex(MOBILE_STR,"614129"));

		// 特殊字符串替换
		// System.out.println(filterStr(STR_SPECIAL,"18756977291@163.com","**"));

		// 业务需求 替换字符串自定位置的字符串
//		System.out.println(phoneNumHide("18756977291"));
//		System.out.println(checkRegex(DATE_ALL, "20080229"));
//		System.out.println(IdCardAllCheck("340321198903168296"));
//		System.out.println(checkRegex(IP,"120.168.1.101"));
//		System.out.println(checkRegex(IP2,"192.168.0.1"));
//		System.out.println(checkRegex(URL,"http://192.168.0.123/jinyoupay/index.html.abb?xxx=222&eee=22&&###"));
		
		// xml 字符串正则匹配获取key value 键值对
//		String str = "<commodityConfig id=\"1\" item=\"60\" reward=\"-1\" firstReward=\"2\" cost=\"6\" tradeName=\"钻石\" notice=\"方块王国通用货币60钻石\" />";
//		Map<String,String> mapStr = getKeyValOfXmlStr(str);
//		System.out.println(mapStr);
	}
}
