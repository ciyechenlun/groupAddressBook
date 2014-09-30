package com.cmcc.zysoft.groupaddressbook.mobile.util;

/**
 * 中间件常量类
 * 
 * @author Administrator
 * 
 */
public class UgcConstants {
	// 01 请求处理成功
	public static final String SUCCESS = "01";
	// 11 参数不正确
	public static final String ERROR_CLIENT_WRONG_PARAMS = "11";
	// 12 文件长度超过5MB
	public static final String ERROR_CLIENT_FILE_SIZE = "12";
	// 13 文件已经上传成功
	public static final String SUCCESS_CLIENT_FILE_UPLOADED = "13";
	// 14 文件位置不正确
	public static final String ERROR_FILE_POS = "14";
	// 15 文件格式不正确
	public static final String ERROR_FILE_TYPE = "15";
	// 16 客户端上传线程数过大
	public static final String ERROR_MAXED_UPLOAD_THREAD = "16";
	// 21 文件打开失败
	public static final String ERROR_OPEN_FILE = "21";
	// 22 服务器端上传线程已满
	public static final String ERROR_MAX_UPLOAD_THREAD = "22";
	// 23 链接已失效
	public static final String ERROR_INVALID_URL = "23";
	// 24 MD5码校验失败
	public static final String ERROR_FILE_MD5 = "24";
}
