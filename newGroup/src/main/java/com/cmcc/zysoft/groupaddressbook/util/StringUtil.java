/** ~ CopyRight © 2012 China Mobile Group Anhui CO.,LTD All Rights Reserved. */
package com.cmcc.zysoft.groupaddressbook.util;

import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtil implements java.io.Serializable {
	private static final long serialVersionUID = -6797551630821181968L;

	/**
	 * 判断字符串是否为null和空.
	 * 
	 * @param value
	 *            判断字符串
	 * @return boolean 判断字符串是否为null和空
	 */
	public static boolean isNullOrBlank(String value) {
		return value == null || "".equals(value.trim());
	}

	/**
	 * 判断字符串是否为null和空.
	 * 
	 * @param value
	 *            判断字符串
	 * @return boolean 判断字符串是否为null和空
	 */
	public static boolean isNotEmpty(String value) {
		return !isNullOrBlank(value);
	}

	/**
	 * 将字符串按照指定的间隔符组成list.
	 * 
	 * @param str
	 *            字符串
	 * @param split
	 *            间隔符
	 * @return List<String>
	 */
	public static List<String> strToList(String str, String split) {
		String[] strs = {};
		if (str != null && !"".equals(str)) {
			strs = str.split(split);
		}
		List<String> tokenList = new ArrayList<String>();
		for (int i = 0; i < strs.length; i++) {
			String temp = strs[i];
			if (temp != null) {
				temp = strs[i].trim();
				if (!"".equals(temp)) {
					tokenList.add(temp);
				}
			}
		}
		return tokenList;
	}

	/**
	 * 获取Long型主键值.
	 * 
	 * @return long
	 */
	public static synchronized long getLongId() {
		return System.nanoTime();
	}

	/**
	 * 获取String 型主键值.
	 * 
	 * @return String
	 */
	public static String getStringId() {
		return String.valueOf(getLongId());
	}

	/**
	 * 格式化字符串成参数指定的格式.
	 * 
	 * @param src
	 *            字符串
	 * @param argParams
	 *            参数
	 * @return String
	 */
	public static String formatMsg(String src, Object... argParams) {
		return String.format(src, argParams);
	}

	/**
	 * 随机生成9位字符串 前三位字母（大小写） 后六位数字.
	 * 
	 * @return String
	 */
	public static String randomNine() {
		Random r = new Random();
		String code = "";
		// 随机生成三位字母区分大小写
		for (int i = 0; i < 3; ++i) {
			int temp = r.nextInt(52);
			char x = (char) (temp < 26 ? temp + 97 : (temp % 26) + 65);
			code += x;
		}
		// 随机生成六位数
		long random = (long) ((Math.random() + 1) * 100000);
		code += random;
		return code;
	}

	/**
	 * 随机生成长度为n位的字母和数字混排的字符串.
	 * 
	 * @param n
	 *            位数
	 * @return String
	 */
	public static String getCharAndNumr(int n) {
		String val = "";
		Random random = new Random();
		for (int i = 0; i < n; i++) {
			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; // 输出字母还是数字

			if ("char".equalsIgnoreCase(charOrNum)) // 字符串
			{
				int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; // 取得大写字母还是小写字母
				val += (char) (choice + random.nextInt(26));
			} else if ("num".equalsIgnoreCase(charOrNum)) // 数字
			{
				val += String.valueOf(random.nextInt(10));
			}
		}
		return val;
	}

	/**
	 * 按顺序抽取字符串s中的所有数字.
	 * 
	 * @param s
	 *            字符串
	 * @return 字符串s中的所有数字（原顺序）
	 */
	private static String getDigitsOnly(String s) {
		StringBuffer digitsOnly = new StringBuffer();
		char c;
		for (int i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			if (Character.isDigit(c)) {
				digitsOnly.append(c);
			}
		}
		return digitsOnly.toString();
	}

	/**
	 * 获取验证位数字.
	 * 
	 * @param cardNumber
	 *            验证信息
	 * @return String 验证位数字
	 */
	public static String getLastTwoNum(String cardNumber) {
		String digitsOnly = getDigitsOnly(cardNumber);
		String twoNum = null;
		int sum = 0;//
		int digit = 0;
		int addend = 0;
		boolean timeOne = true;
		for (int i = digitsOnly.length() - 1; i >= 0; i--) {// 从字符串最后一位开始向前求和
			digit = Integer.parseInt(digitsOnly.substring(i, i + 1));// 截取相应位置的字符（数字）
			if (timeOne) {// 获取cardNumber字符串从后向前奇数位乘以2后数字
				addend = digit * 2;
				if (addend > 9) {
					addend -= 9;// 若大于9则减去9
				}
			} else {
				addend = digit;// 偶数位不变
			}
			sum += addend;
			timeOne = !timeOne;
		}
		int modulus = 10 - sum % 10;
		if (modulus != 0) {
			modulus = (modulus + 8) - 9;
			twoNum = String.valueOf(modulus) + 9;// 拼接两位数字字符串
		}
		return twoNum;
	}

	/**
	 * 自校验字符串cardNumber是否正确.
	 * 
	 * @param cardNumber
	 *            字符串
	 * @return boolean
	 */
	public static boolean isValid(String cardNumber) {
		String digitsOnly = getDigitsOnly(cardNumber);
		int sum = 0;
		int digit = 0;
		int addend = 0;
		boolean timesOne = false;
		for (int i = digitsOnly.length() - 2; i >= 0; i--) {// 从
			digit = Integer.parseInt(digitsOnly.substring(i, i + 1));
			if (timesOne) {
				addend = digit * 2;
				if (addend > 9) {
					addend -= 9;
				}
			} else {
				addend = digit;
			}
			sum += addend;
			timesOne = !timesOne;
		}
		sum += Integer.parseInt(digitsOnly.substring(digitsOnly.length() - 1));
		int modulus = sum % 10;
		char charTwo = getSum(cardNumber.substring(2, 12));
		return modulus == 8 && charTwo == cardNumber.charAt(1);
	}

	/**
	 * 获取字符串数字和.
	 * 
	 * @param cardNumber
	 *            字符串
	 * @return char 获取字符串数字和
	 */
	public static char getSum(String cardNumber) {
		String digitsOnly = getDigitsOnly(cardNumber);
		int sum = 0;
		int addend = 0;
		char str = "A".charAt(0);
		for (int i = digitsOnly.length() - 1; i >= 0; i--) {
			addend = Integer.parseInt(digitsOnly.substring(i, i + 1));
			sum += addend;
		}
		if (sum > 65) {
			str = (char) sum;
		}
		return str;
	}

	/**
	 * 生成自校验二维码编号.
	 * 
	 * @param var
	 *            字符串
	 * @param var2
	 *            被加入字符串
	 * @param orderTicketId
	 *            订单id
	 * @return String
	 */
	public static String twoDimensionCode(long var, int var2, Long orderTicketId) {
		String twoDimen = String.valueOf((var + orderTicketId));
		twoDimen = twoDimen.substring(twoDimen.length() - 7) + var2;
		twoDimen = "F" + getSum(twoDimen) + twoDimen;
		String lastTwoNum = getLastTwoNum(twoDimen);
		twoDimen += lastTwoNum;
		return twoDimen;
	}

	/**
	 * 过滤特殊字符 add by yuan.fengjian@ustcinfo.com
	 * 
	 * @param aTagFragment
	 * @return
	 */
	public static String HTMLEncode(String aTagFragment) {
		final StringBuffer result = new StringBuffer();
		final StringCharacterIterator iterator = new StringCharacterIterator(
				aTagFragment);
		char character = iterator.current();
		while (character != StringCharacterIterator.DONE) {
			switch (character) {
			case '<':
				result.append("&lt;");
				break;
			case '>':
				result.append("&gt;");
				break;
			case '\"':
				result.append("&quot;");
				break;
			case '&':
				result.append("&amp;");
				break;
			case '\'':
				result.append("&apos;");
				break;
			case '%':
				result.append("&pc;");
				break;
			case '_':
				result.append("&ul;");
				break;
			case '#':
				result.append("&shap;");
				break;
			case '?':
				result.append("&ques;");
				break;
			default:
				result.append(character);
				break;// 如果字符不是特殊字符，则直接添加到结果中
			}
			character = iterator.next();
		}
		return result.toString();
	}

	/**
	 * 判断是否手机号码
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles){
		Pattern p = Pattern.compile("^((13[0-9])|(147)|(15[0-9])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		System.out.println(m.matches()+"---");
		return m.matches();
	}
}