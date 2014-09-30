// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.spring.security.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述：常量类
 * <br />版本:1.0.0
 * <br />日期： 2013-1-13 上午10:47:30
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
public final class Constant {

	/**
	 * 错误消息key
	 */
	public static final String ERROR_MESSAGE = "ERROR_MESSAGE";
	
	/**
	 * 错误信息map
	 */
	public static final Map<String, String> ERROR_MESSAGE_MAP = new HashMap<String, String>();
	
	static{
		ERROR_MESSAGE_MAP.put("401", "您还未登录或者Session失效，请您重新登录!");
	}
	
	/**
	 * SESSION中的公司信息
	 */
	public static final String SESSION_COMPANY = "SESSION_COMPANY";
	
	/**
	 * SESSION中的登录地址
	 */
	public static final String SESSION_LOGINURL = "SESSION_LOGINURL";
	
	/**
	 * 公司LOGO
	 */
	public static final String SESSION_COMPANY_LOGO = "SESSION_COMPANY_LOGO";
	
	/**
	 * 用户姓名
	 */
	public static final String SESSION_USER_NAME = "SESSION_USER_NAME";
	
	/**
	 * 存放在cookie当中的登录名
	 */
	public static final String COOKIE_J_USER_NAME = "COOKIE_J_USER_NAME";
	
	/**
	 * 存放在cookie当中的密码密文
	 */
	public static final String COOKIE_J_PASS_WORD = "COOKIE_J_PASS_WORD";
	
	/**
	 * Spring Security RememberMe Cookie Name
	 */
	public static final String SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY = "SPRING_SECURITY_REMEMBER_ME_COOKIE";
	
	public static final String _spring_security_remember_me = "_spring_security_remember_me";
	
	public static final String _spring_security_remember_me_on = "on";
	
}
