package com.cmcc.zysoft.groupaddressbook.mobile.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Convenience class for setting and retrieving cookies.
 */
public final class RequestUtil {
	private static final Log log = LogFactory.getLog(RequestUtil.class);

	/**
	 * Checkstyle rule: utility classes should not have public constructor
	 */
	private RequestUtil() {
	}

	/**
	 * Convenience method to set a cookie
	 * 
	 * @param response
	 *            the current response
	 * @param name
	 *            the name of the cookie
	 * @param value
	 *            the value of the cookie
	 * @param path
	 *            the path to set it on
	 */
	public static void setCookie(HttpServletResponse response, String name,
			String value, String path) {
		if (log.isDebugEnabled()) {
			log.debug("Setting cookie '" + name + "' on path '" + path + "'");
		}

		Cookie cookie = new Cookie(name, value);
		cookie.setSecure(false);
		cookie.setPath(path);
		cookie.setMaxAge(3600 * 24 * 30); // 30 days

		response.addCookie(cookie);
	}

	/**
	 * Convenience method to get a cookie by name
	 * 
	 * @param request
	 *            the current request
	 * @param name
	 *            the name of the cookie to find
	 * 
	 * @return the cookie (if found), null if not found
	 */
	public static Cookie getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		Cookie returnCookie = null;

		if (cookies == null) {
			return returnCookie;
		}

		for (Cookie thisCookie : cookies) {
			if (thisCookie.getName().equals(name)) {
				// cookies with no value do me no good!
				if (!thisCookie.getValue().equals("")) {
					returnCookie = thisCookie;

					break;
				}
			}
		}

		return returnCookie;
	}

	/**
	 * Convenience method for deleting a cookie by name
	 * 
	 * @param response
	 *            the current web response
	 * @param cookie
	 *            the cookie to delete
	 * @param path
	 *            the path on which the cookie was set (i.e. /appfuse)
	 */
	public static void deleteCookie(HttpServletResponse response,
			Cookie cookie, String path) {
		if (cookie != null) {
			// Delete the cookie by setting its maximum age to zero
			cookie.setMaxAge(0);
			cookie.setPath(path);
			response.addCookie(cookie);
		}
	}

	/**
	 * Convenience method to get the application's URL based on request
	 * variables.
	 * 
	 * @param request
	 *            the current request
	 * @return URL to application
	 */
	public static String getAppURL(HttpServletRequest request) {
		StringBuffer url = new StringBuffer();
		int port = request.getServerPort();
		if (port < 0) {
			port = 80; // Work around java.net.URL bug
		}
		String scheme = request.getScheme();
		url.append(scheme);
		url.append("://");
		url.append(request.getServerName());
		if ((scheme.equals("http") && (port != 80))
				|| (scheme.equals("https") && (port != 443))) {
			url.append(':');
			url.append(port);
		}
		url.append(request.getContextPath());
		return url.toString();
	}

	/**
	 * 获取请求中的参数值
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param paramName
	 *            String
	 * @return 如果没有该参数，则返回空字符串
	 */
	public static String getParam(HttpServletRequest request, String paramName) {
		String value = request.getParameter(paramName);
		return value == null ? "" : value;
	}

	/**
	 * 获取请求中的参数值
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param paramName
	 *            String
	 * @return 如果没有该参数，则返回空字符串
	 */
	public static Long getLongParam(HttpServletRequest request, String paramName) {
		String value = getParam(request, paramName);
		Long l = null;
		try {
			l = Long.parseLong(value);
		} catch (NumberFormatException e) {
			log.error(e);
		}
		return l;
	}

	/**
	 * 获取请求中的参数值
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param paramName
	 *            String
	 * @return 如果没有该参数，则返回空字符串
	 */
	public static Integer getIntParam(HttpServletRequest request,
			String paramName) {
		String value = getParam(request, paramName);
		Integer i = null;
		try {
			i = Integer.parseInt(value);
		} catch (NumberFormatException e) {
			log.error(e);
		}
		return i;
	}
}
