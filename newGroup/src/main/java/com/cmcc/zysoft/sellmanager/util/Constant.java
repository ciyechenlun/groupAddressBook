// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sellmanager.util;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述：常量类
 * <br />@version:1.0.0
 * <br />日期： 2012-12-13 上午10:16:02
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
public abstract class Constant {
	
	/**
	 * 手机号码之间连接符
	 */
	public static String SPLIT_HORIZONTAL_BAR = "-";
	
	/**
	 * 定位鉴权的用户ID
	 */
	public static final String USER_ID = "user_id";
	
	/**
	 * 定位鉴权的用户密码
	 */
	public static final String USER_PWD = "user_pwd";
	
	/**
	 * 定位鉴权的集团ID
	 */
	public static final String JITUAN_ID = "jituan_id";
	
	/**
	 * 根据手机号码获取详细定位信息的URL
	 */
	public static final String LOCATE_URL = "locate_url";
	
	/**
	 * 定位返回数据的格式
	 */
	public static final String DATA_TYPE = "data_type";
	
	/**
	 * 地图的key
	 */
	public static final String MAP_KEY = "map_key";
	
	/**
	 * 地理编码的url
	 */
	public static final String GEOCODE_URL = "geocode_url";
	
	/**
	 * 是否通过代理发送HTTP请求
	 */
	public static final String PROXY = "proxy";

	/**
	 * 代理服务器HOST
	 */
	public static final String PROXY_HOST = "proxy_host";
	
	/**
	 * 代理服务器端口
	 */
	public static final String PROXY_PORT = "proxy_port";
	
	/**
	 * 返回报文的编码方式
	 */
	public static final String RESPONSE_CHARSET = "response_charset";
	
	public static final String PHONE_KEY = "phone";
	
	public static String LNG_KEY = "longitude";
	
	public static String LAT_KEY = "latitude";
	
	public static final String SIZE_KEY = "size";
	
	public static final String SUCCESS_KEY = "success";
	
	public static final String FAILURE_KEY = "failure";
	
	public static final String LOCATION_KEY = "positions";
	
	public static final String CONSUME_TIME_KEY = "consumeTime";
	
	public static final String LOCATE_TIME_KEY = "locateTime";
	
	public static final String ERROR_KEY = "error";
	
	public static final String PROVINCE_KEY = "province";
	
	public static final String CITY_KYY = "city";
	
	public static final String TOWN_KEY = "town";
	
	public static final String DETAIL_KEY = "detail";
	
	/**
	 * 相同位置的距离直径
	 */
	public static final String SAME_LOCATE_DISTANCE = "same_locate_distance";
	
	/**
	 * 定位失败次数
	 */
	public static final String LOCATE_FAIL_TIME = "locate_fail_time";
	
	/**
	 * 位置相同的次数
	 */
	public static final String LOCATE_SAME_TIME = "locate_same_time";
}
