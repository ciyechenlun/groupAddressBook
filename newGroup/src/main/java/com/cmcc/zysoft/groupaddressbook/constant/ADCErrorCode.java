/**
 * CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */

package com.cmcc.zysoft.groupaddressbook.constant;

/**
 * 错误码
 * @author 袁凤建
 * <br />邮箱：yuan.fengjian@ustcinfo.com
 * <br />描述：ADCErrorCode.java
 * <br />版本: 1.0.0
 * <br />日期：2013-7-3 上午11:32:07
 * <br />CopyRight © 2012 USTC SINOVATE SOFTWARE CO.LTD All Rights Reserved.
 */

public final class ADCErrorCode {
	
	//~------------所有消息 start----------------//
	
	/**
	 * 成功:正常返回
	 */
	public static final String SUCCESS = "0";	
	
	/**
	 * 对未定义错误的描述,未定义错误
	 */
	public static final String ERROR = "1";
	
	//~------------所有消息 end----------------//
	
	
	//~------------单点登录鉴权消息 start----------------//
	/**
	 * 数据非法加密 :<br />
	 * SI应用系统私钥与ADC中的私钥不一致
	 */
	public static final String CAS_ILLEGAL_ENCRYPTION = "101";	
	
	/**
	 * 用户Token已失效:<br />	
	 * 用户Token过期，需要重新登录
	 */
	public static final String CAS_EXPIRY_TOKEN = "102";	
	
	/**
	 * 用户Token不存在:<br />	
	 * 用户可能重新登录ADC，但未重新登录SI应用系统
	 */
	public static final String CAS_NONE_TOKEN = "103";	
	
	/**
	 * 用户登录未知系统:<br />	
	 * ADC对用户登录的系统未知
	 */
	public static final String CAS_LOGIN_UNKOWN_SYSTEM = "104";	
	
	/**
	 * 其他错误:<br />
	 * 1**编码定义鉴权消息其他错误
	 */
	public static final String CAS_OTHER_ERROR = "105";	

	//~------------单点登录鉴权消息 end----------------//
	
	//~------------心跳消息 start----------------//
	
	/**
	 * 数据非法加密 :<br />	
	 * SI应用系统私钥与ADC中的私钥不一致
	 */
	public static final String HEARTBEAT_ILLEGAL_ENCRYPTION = "201";
	
	/**
	 * 用户Token已失效:<br />		
	 * 用户Token过期，需要重新登录
	 */
	public static final String HEARTBEAT_EXPIRY_TOKEN = "202";

	
	/**
	 * 用户Token不存在
	 * 用户可能重新登录ADC，但未重新登录SI应用系统
	 */
	public static final String HEARTBEAT_NONE_TOKEN = "203";
	
	/**
	 * 其他错误:<br/>
	 * 2**编码定义心跳消息其他错误
	 */
	public static final String HEARTBEAT_OTHER_ERROR = "204";	
	
	//~------------心跳消息 end----------------//
	
	//~------------话单传送消息 start----------------//
	/**
	 * 数据非法加密 :<br/>	
	 * SI应用系统私钥与ADC中的私钥不一致
	 */
	public static final String TICKET_TRANS_ILLEGAL_ENCRYPTION = "301";	
	
	/**
	 * 用户Token已失效:<br/>	
	 * 用户Token过期，需要重新登录
	 */
	public static final String TICKET_TRANS_EXPIRY_TOKEN= "302";	
	
	/**
	 * 用户Token不存在:<br/>	
	 * 用户可能重新登录ADC，但未重新登录SI应用系统
	 */
	public static final String TICKET_TRANS_NONE_TOKEN= "303";		
	
	/**
	 * 用户订购关系不存在:<br/>	
	 * 用户没有权限使用该功能
	 */
	public static final String TICKET_TRANS_NONE_ORDER= "304";		
	
	/**
	 * 未知业务资费信息:<br/>
	 * ADC平台未设置该业务的计费策略
	 */
	public static final String TICKET_TRANS_UNKNOWN_BIZ_COST= "305";	
	
	/**
	 * 其他错误:<br/>	
	 * 3**编码定义批价消息其他错误
	 */
	public static final String TICKET_TRANS_OTHER_ERROR = "306";	
	
	//~------------话单传送消息 end----------------//
	
	//~-----------集团客户帐号绑定消息（业务开通/变更）start-----------//

	/**
	 * 业务配置参数错误:<br/>
	 * 业务订购配置不符合SI应用系统的约定
	 */
	public static final String ENTERPRISE_ACCOUNT_WRONG_CONFIG= "401";		
	
	/**
	 * 业务功能点订购错误:<br/>:<br/>
	 * 订购的功能点与SI应用系统提供的不一致
	 */
	public static final String ENTERPRISE_ACCOUNT_BIZ_ORDER_ERROR= "402";		
	
	/**
	 * License数量错误:<br/>
	 * License可能不是非负的整数
	 */
	public static final String ENTERPRISE_ACCOUNT_LICENSE_NUM_ERROR= "403";	
	
	/**
	 * 未知操作类型:<br/>
	 * SI应用系统无法识别操作类型（订购、取消、暂停、恢复）
	 */
	public static final String ENTERPRISE_ACCOUNT_UNKNOWN_TYPE= "404";		
	
	/**
	 * 其他错误:<br/>	
	 * 4**编码定义该类消息其他错误
	 */
	public static final String ENTERPRISE_ACCOUNT_OTHER_ERROR= "405";		
	
	//~-----------集团客户帐号绑定消息（业务开通/变更）start-----------//
	
	//~----------员工帐号绑定消息（业务开通/变更）start-----------//
	/**
	 * 未知集团客户帐号:<br/>
	 * 集团客户帐号在SI应用系统中不存在
	 */
	public static final String EMPLOYEE_ACCOUNT_UNKNOWN_ACCOUNT = "501";		
	
	/**
	 * 用户手机号码非法:<br/>
	 * 用户手机号码错误
	 */
	public static final String EMPLOYEE_ACCOUNT_WRONG_PHONE_NUMBER= "502";	
	
	/**
	 * 其他错误:<br/>
	 * 5**编码定义该类消息其他错误
	 */
	public static final String EMPLOYEE_ACCOUNT_OTHER_ERROR= "503";		
	
	/**
	 * 其他错误
	 */
	public static final String EMPLOYEE_ACCOUNT_OTHER= "602";	
	
	//~----------员工帐号绑定消息（业务开通/变更）end-----------//
	
	//~------------------SI客户端鉴权 start------------------//
	/**
	 * 集团客户不存在
	 */
	public static final String SIAUTH_NONE_GROUP_CUSTOMER= "701";	
	
	/**
	 * 用户不存在	
	 */
	public static final String SIAUTH_NONE_USER= "702";		
	
	/**
	 * 密码不符
	 */
	public static final String SIAUTH_WRONG_PASSWORD= "703";		
	
	/**
	 * 集团客户未审核或已注销或已冻结
	 */
	public static final String SIAUTH_GROUP_CUSTOMER_NO_ACTIVE= "704";		
	
	/**
	 * 用户已注销或已冻结	
	 */
	public static final String SIAUTH_USER_NO_ACTIVE= "705";		
	
	/**
	 * 集团客户不存在
	 */
	public static final String NONE_GROUP_CUSTOMER= "111";			
	
	/**
	 * 用户不存在	
	 */
	public static final String NONE_USER= "112";		
	
	/**
	 * 密码不符
	 */
	public static final String WRONG_PASSWORD= "113";			
	
	/**
	 * 集团客户未审核或已注销或已冻结
	 */
	public static final String GROUP_CUSTOMER_NO_ACTIVE= "114";			

	/**
	 * 用户已注销或已冻结	
	 */
	public static final String USER_NO_ACTIVE= "115";		
	
	/**
	 * 用户ID与用户帐户不匹配	
	 */
	public static final String USER_ACCOUT_ID_NOTMACTH= "116";	
	
	//~------------------SI客户端鉴权 end------------------//
}