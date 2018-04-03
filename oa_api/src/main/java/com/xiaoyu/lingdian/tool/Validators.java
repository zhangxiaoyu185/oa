package com.xiaoyu.lingdian.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 基础数据类型验证模块
 *
 */
public final class Validators {

	public static boolean isEmail(String value) {

		return !isEmpty(value) && value.indexOf("@") > 0;
	}

	/**
	 * 当判断数组是为null或长度为0是返回 <code>true</code>
	 * 
	 * @param args
	 * @return
	 */
	public static boolean isEmpty(Object[] args) {

		return args == null || args.length == 0;
	}

	public static boolean isEmpty(String value) {

		return value == null || value.trim().length() == 0;
	}

	public static boolean isNumber(String value) {
		if (isEmpty(value)) {
			return false;
		}

		for (int i = 0; i < value.length(); i++) {
			if (value.charAt(i) > '9' || value.charAt(i) < '0') {
				return false;
			}
		}
		return true;
	}

	public static boolean isFloat(String value) {

		if (isEmpty(value)) {
			return false;
		}

		for (int i = 0; i < value.length(); i++) {
			if ((value.charAt(i) > '9' || value.charAt(i) < '0')
					&& value.charAt(i) != '.' && value.charAt(i) != '-') {
				return false;
			}
		}
		return true;
	}

	public static boolean isNumber(String value, int min, int max) {

		if (!isNumber(value)) {
			return false;
		}

		int number = Integer.parseInt(value);
		return number >= min && number <= max;
	}

	public static boolean isString(String value, int minLength, int maxLength) {

		if (value == null) {
			return false;
		}

		if (minLength < 0) {
			return value.length() <= maxLength;
		}
		else if (maxLength < 0) {
			return value.length() >= minLength;
		}
		else {
			return value.length() >= minLength && value.length() <= maxLength;
		}
	}

	public static boolean isTime(String value) {

		if (isEmpty(value) || value.length() > 8) {
			return false;
		}

		String[] items = value.split(":");

		if (items.length != 2 && items.length != 3) {
			return false;
		}

		for (int i = 0; i < items.length; i++) {
			if (items[i].length() != 2 && items[i].length() != 1) {
				return false;
			}
		}

		return !(!isNumber(items[0], 0, 23) || !isNumber(items[1], 0, 59) || (items.length == 3 && !isNumber(
				items[2], 0, 59)));
	}

	/**
	 * 手机号验证
	 * 
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isMobile(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches();
		return b;
	}

	/**
	 * 电话号码验证
	 * 
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isPhone(String str) {
		Pattern p1 = null, p2 = null;
		Matcher m = null;
		boolean b = false;
		p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$"); // 验证带区号的
		p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$"); // 验证没有区号的
		if (str.length() > 9) {
			m = p1.matcher(str);
			b = m.matches();
		} else {
			m = p2.matcher(str);
			b = m.matches();
		}
		return b;
	}
	
	private Validators() {

	}

}
