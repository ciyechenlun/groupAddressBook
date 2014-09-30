package com.cmcc.zysoft.groupaddressbook.mobile.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

/**
 * Ugc工具类
 * 
 * @author Administrator
 * 
 */
public class PortalUtil {
//	private static Logger log = Logger.getLogger(PortalUtil.class);
//	private static Pattern CONT_URL_P = Pattern.compile("/n(\\d+)d(\\d+)c(\\d+)(\\..*)");

	/**
	 * 
	 * @param key
	 *            String
	 * @param args
	 *            Object[]
	 * @return
	 */
	public static String getText(String key, Object[] args) {
		if (PortalConstants.appCodeResourceBundle == null)
			PortalConstants.appCodeResourceBundle = ResourceBundle.getBundle(
					PortalConstants.BUNDLE_KEY, Locale.CHINA);
		if (PortalConstants.appCodeResourceBundle != null) {
			try {
				String result = PortalConstants.appCodeResourceBundle
						.getString(key);
				if (result != null)
					return MessageFormat.format(result, args);
				else
					return result;
			} catch (Exception e) {
				return key;
			}
		}
		return "";
	}

	/**
	 * 
	 * @param key
	 *            String
	 * @return
	 */
	public static String getText(String key) {
		return getText(key, null);
	}

	/**
	 * 将message中{i} 替换为数组params中的值
	 * 
	 * @param message
	 *            String
	 * @param params
	 *            String[]
	 * @return
	 */
	public static String replacePushMessage(String message, String[] params) {
		if (message != null && !"".equals(message)) {
			if (params != null && params.length > 0) {
				for (int i = 0; i < params.length; i++) {
					message = message.replace("${" + i + "}", params[i]);
				}
			}
		}
		return message;
	}

	/**
	 * 得到当前域名及端口及上下文 如 http://c2.cmvideo.cn:8080/sup/
	 * 
	 * @param request
	 *            如果为空则读取配制文件httpstr属性
	 * @return
	 */
	public static String getDomain(HttpServletRequest request) {
		String httpstr = "";
		if (request != null) {
			httpstr = "http://" + request.getServerName();
			int port = request.getServerPort();
			if (port != 0 && port != 80)
				httpstr += ":" + port;
			httpstr += request.getContextPath();
		} else {
			httpstr = PortalConstants.conf.getString("httpstr");
		}
		return httpstr;
	}

	/**
	 * 
	 * @param path
	 *            String
	 * @return
	 */
	public static String replaceSeparator(String path) {
		return (path == null) ? null : path.replaceAll("\\\\", "/").replaceAll(
				"//", "/");
	}

	/**
	 * 
	 * @param version
	 *            String
	 * @return
	 */
	public static Long versionToLong(String version) {
		if (!checkVersion(version))
			return null;
		String[] numbers = version.split("\\.");

		Long index = 1000L;
		Long versionNum = 0L;
		for (int i = numbers.length - 1; i >= 0; i--) {
			if (i == numbers.length - 1)
				versionNum += Long.parseLong(numbers[i]);
			else {
				versionNum += Long.parseLong(numbers[i]) * index;
				index = index * 1000L;
			}
		}
		return versionNum;
	}

	/**
	 * 
	 * @param version
	 *            String
	 * @return
	 */
	public static boolean checkVersion(String version) {
		if (version == null || "".equals(version)
				|| version.split("\\.").length < 3
				|| version.split("\\.").length > 5) {
			return false;
		}

		try {
			String[] numbers = version.split("\\.");
			for (int i = 0; i < numbers.length; i++) {
				Long num = Long.parseLong(numbers[i]);
				if (num > 999L || num < 0L)
					return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}
