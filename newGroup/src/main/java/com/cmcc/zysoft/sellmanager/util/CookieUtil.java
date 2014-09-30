// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sellmanager.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述：Cookie操作工具类
 * <br />版本:1.0.0
 * <br />日期： 2013-1-14 上午11:49:14
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
public class CookieUtil {

	/**
	 * 添加cookie
	 * @param request
	 * @param response
	 * @param name
	 * @param value
	 */
	public static void addCookie(HttpServletResponse response,String name,String value,int maxAge){
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setMaxAge(maxAge);
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		response.addCookie(cookie);
	}
	
	/**
	 * 根据cookie名称获取cookie的值
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request,String cookieName){
		if(cookieName==null){
			return "";
		}
		Cookie cookie = getCookie(request, cookieName);
		if(cookie!=null){
			return cookie.getValue();
		}else{
			return "";
		}
		
	}
	
	/**
	 * 根据cookie名称获取cookie对象
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static Cookie getCookie(HttpServletRequest request,String cookieName){
		Cookie[] cookies = request.getCookies();
		Cookie cookie = null;
		if(cookies!=null && cookies.length>0){
			for (int i = 0; i < cookies.length; i++) {
				cookie = cookies[i];
				if(cookieName.equals(cookie.getName())){
					return cookie;
				}
			}
		}
		return null;
	}
	
	/**
	 * 删除cookie
	 * @param request
	 * @param response
	 * @param cookieName
	 * @return
	 */
	public static boolean deleteCookie(HttpServletRequest request,HttpServletResponse response,String cookieName){
		if(cookieName!=null){
			Cookie cookie = getCookie(request, cookieName);
			if(cookie!=null){
				cookie.setMaxAge(0);
				cookie.setPath("/");
				response.addCookie(cookie);
				return true;
			}
		}
		return false;
	}
}
