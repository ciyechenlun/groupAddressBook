/**
 * CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */

package com.cmcc.zysoft.groupaddressbook.constant;

/**
 * ADC管理平台SI接口业务代码表
 * @author 袁凤建
 * <br />邮箱：yuan.fengjian@ustcinfo.com
 * <br />描述：ADCBizCode.java
 * <br />版本: 1.0.0
 * <br />日期：2013-7-3 上午11:31:28
 * <br />CopyRight © 2012 USTC SINOVATE SOFTWARE CO.LTD All Rights Reserved.
 */

public final class ADCBizCode {

	/**
	 * SSO鉴权接口	
	 */
	public static final String SSO_AUTHENTICATION = "SI101";
	
	/**
	 * SSO心跳接口	
	 */
	public static final String SSO_HEARTBEAT = "SI102";
	
	/**
	 * 服务开通请求接口	
	 */
	public static final String SERVICE_REGISTER = "SI201";
	
	
	/**
	 * 服务状态变更接口	
	 */
	public static final String SERVICE_STATUS_CHANGE = "SI202";
	
	/**
	 * 成员管理接口	
	 */
	public static final String MEMBERS_MANAGE = "SI301";
	
	/**
	 * 客户端鉴权（保留，不建议使用）	
	 */
	public static final String CLIENT_AUTHENTICATION_NO_SUGGEST = "SI701";
	
	/**
	 * 客户端心跳	
	 */
	public static final String CLIENT_HEARTBEAT = "SI801";
	
	
	/**
	 * 客户端鉴权（新增，建议使用）	
	 */
	public static final String CLIENT_AUTHENTICATION_SUGGEST = "SI702";
	
	/**
	 * EC业务量信息同步	
	 */
	public static final String EC_BIZ_INFO_SYSNC = "SI902";
	
	/**
	 * 个人业务信息同步	
	 */
	public static final String PERSONAL_BIZ_INFO_SYSNC = "SI1001";
	
	/**
	 * 上行短信接口	
	 */
	public static final String UP_SMS = "SI1002";
	
	/**
	 * 供应商企业信息同步	
	 */
	public static final String SUPPLIER_INFO_SYSNC = "SI1101";
	
	/**
	 * 供应商企业成员管理	
	 */
	public static final String SUPPLIER_MEMBERS_MANAGE = "SI1102";
	
	/**
	 * 客户端鉴权（新增，验证码接口）	
	 */
	public static final String CLIENT_AUTHENTICATION_WITH_AUTHCODE = "SI703";
	
	/**
	 * 手机短信验证码	
	 */
	public static final String MOBLIE_SMS_AUTHCODE = "SI103";
	
	/**
	 * 修改密码	
	 */
	public static final String CHANGE_PASSWORD = "SI104";
	
	/**
	 * 重置密码	
	 */
	public static final String RESET_PASSWORD = "SI105";
	
	/**
	 * 短信确认码	
	 */
	public static final String SMS_CONFIRM_CODE = "SI106";
	
	/**
	 * ICT服务开通和修改	
	 */
	public static final String ICT_SERVICE_REGISTER_OR_CHANGE = "SI107";
	
	/**
	 * ICT客户成员订购管理
	 */
	public static final String ICT_CUSTOMER_MEMBERS_ORDER = "SI108";
	
	/**
	 * 灰度发布体验用户
	 */
	public static final String  GRAY_MEMBERS = "WD101";
	/**
	 * 获取短号
	 */
	public static final String GET_MOBILE_SHORT = "SI905";
}