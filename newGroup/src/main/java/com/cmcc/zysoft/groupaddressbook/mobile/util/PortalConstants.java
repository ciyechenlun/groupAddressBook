package com.cmcc.zysoft.groupaddressbook.mobile.util;

import java.util.ResourceBundle;

import org.apache.commons.configuration.Configuration;
import org.springframework.context.ApplicationContext;

/**
 * Ugc常量类
 * 
 * @author Administrator
 * 
 */
public final class PortalConstants {
	public static final String OPHONE_WIFI_PARAM = "netType=wifi";
	public static final String OPHONE_TD_PARAM = "netType=TD";

	/** 2g 网络 */
	public static final String NET_TYPE_2_GENERATION = "1";
	/** 3g 网络 */
	public static final String NET_TYPE_3_GENERATION = "2";
	/** wlan 网络 */
	public static final String NET_TYPE_4_GENERATION = "4";
	/** WWW 网络 */
	public static final String NET_TYPE_WWW_GENERATION = "6";

	public static final String UA = "User-Agent";

	/** 文件上传路径 **/
	public static String FILE_PATH;

	public static int CLIENT_TYPE_SUP = 1;
	public static int CLIENT_TYPE_RMS = 2;

	public static final Integer UGC_UPLOAD_READ_CACHE_DEFAULT = 81920;
	public static final Integer UGC_UPLOAD_READ_CACHE_RMS = 1024000;
	public static final Integer FILE_UPLOAD_THREAD_MAX = 100;

	/**
	 * 请求处理成功
	 */
	public static final String CORRECT_REQUEST_HANDLED = "01";
	/**
	 * 客户端错误：参数不正确
	 */
	public static final String ERROR_CLIENT_WRONG_PARAMS = "11";
	/**
	 * 客户端错误：文件长度超过5MB
	 */
	public static final String ERROR_CLIENT_TOO_LONG = "12";
	/**
	 * 客户端错误：文件已经上传成功
	 */
	public static final String ERROR_CLIENT_FILE_UPLOADED = "13";
	/**
	 * 客户端错误：文件位置不正确
	 */
	public static final String ERROR_CLIENT_WRONG_POSITION = "14";
	/**
	 * 客户端错误：文件格式不正确
	 */
	public static final String ERROR_CLIENT_WRONG_CTYPE = "15";
	/**
	 * 客户端错误：客户端上传线程数过大
	 */
	public static final String ERROR_CLIENT_UPLOAD_THREAD_TOO_MUCH = "16";
	/**
	 * 客户端错误：客户端上传线程数过大
	 */
	public static final String ERROR_CLIENT_TIME_OUT = "17";
	/**
	 * 客户端错误：客户端上传文件超过参数指定长度
	 */
	public static final String ERROR_CLIENT_FILE_SPILL_OVER = "18";
	/**
	 * 服务器端错误:文件打开失败
	 */
	public static String ERROR_SERVER_OPEN_FILE_FAILED = "21";
	/**
	 * 服务器端错误:MD5码校验失败
	 */
	public static String ERROR_SERVER_MD5_VALIDATE_FAILED = "24";
	/**
	 * 服务器端错误:服务器端上传线程已满
	 */
	public static String ERROR_SERVER_THREAD_POOL_OVERFLOW = "22";

	public static final String FILE_UPLOAD_ENCRYPT_KEY = "WONDERTEportalWRP";

	public static final String BUNDLE_KEY = "ApplicationResources";
	public static final String FEE_KEY = "Fee";
	public static final String CONFIG = "appConfig";
	public static final String PREFERRED_LOCALE_KEY = "org.apache.struts2.action.LOCALE";

	public static ApplicationContext ctx = null;
	public static ResourceBundle errorCodeResourceBundle = null;
	public static ResourceBundle appCodeResourceBundle = null;
	public static Configuration conf;

	// 限制最大访问记录数,避免因错误写法造成数据耗光
	public static final int QUERY_PAGE_MAX_SIZE = 2000;
	public static final int NODE_TREE_PAGE_SIZE = 2000;
	public static final int DOWN_LOAD_MAX_SIZE = 5000;

	// 顶级的模板目录ID
	public static Long TOP_NODE_ID = 0L;
	public static final String TEMPLATE_TREE_NODES = "TEMPLATE_TREE_NODES";
	public static final String RESOURCE_TREE_NODES = "RESOURCE_TREE_NODES";
	// 模板历史版本最大数量
	public static final int HISTORY_MAX_COUNT = 25;

	// 系统常量
	public static final String PREVIEW_PATH = "PREVIEW_PATH";
}
