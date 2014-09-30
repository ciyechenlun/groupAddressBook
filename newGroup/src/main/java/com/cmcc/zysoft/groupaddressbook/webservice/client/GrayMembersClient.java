/**
 * CopyRight © 2012 USTC SINOVATE SOFTWARE CO.LTD All Rights Reserved.
 */

package com.cmcc.zysoft.groupaddressbook.webservice.client;

import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cmcc.zysoft.framework.utils.DateUtil;
import com.cmcc.zysoft.framework.utils.StringUtil;
import com.cmcc.zysoft.groupaddressbook.constant.ADCActionCode;
import com.cmcc.zysoft.groupaddressbook.constant.ADCBizCode;
import com.cmcc.zysoft.groupaddressbook.util.WebServiceUtil;
import com.cmcc.zysoft.groupaddressbook.util.XMLConstant;
import com.cmcc.zysoft.groupaddressbook.webservice.SIInterfaceForADC.SIInterfaceForADC;
import com.cmcc.zysoft.groupaddressbook.webservice.SIInterfaceForADC.SIInterfaceForADCServiceLocator;

/**
 * 灰度发布体验用户列表同步管理.
 * @author 袁凤建
 * <br />邮箱: yuan.fengjian@ustcinfo.com
 * <br />描述: GrayMembersClient.java
 * <br />版本: 1.0.0
 * <br />日期: 2013-7-8 下午4:48:06
 * <br />CopyRight © 2012 USTC SINOVATE SOFTWARE CO.LTD All Rights Reserved.
 */

@Component
public class GrayMembersClient {

	/**
	 * 日志.
	 */
	private static final Logger logger = LoggerFactory.getLogger(GrayMembersClient.class);
	
	/**
	 * 存放当天transID的map.
	 */
	private static final Hashtable<String, Long> TRANSID_TABLE = new Hashtable<String, Long>();
	
	/**
	 * 同步灰度用户列表信息.
	 * @param testFlag 测试标记---(0：非测试交易, 1：测试交易)
	 * @param ProductCode 产品编码---由BOSS统一分配的产品标识,如果不走BOSS进行集团订购的产品，则由云平台分配
	 * @param ProductName 产品名称
	 * @param Version 版本号---如：1.2.3。该版本必需在云平台上已存在且为体验版。根据版本号可以查询该版本的功能描述。
	 * @param Desc 描述信息---描述本次发布的目的。如：网达定制版、ICT中心内部测试版等等。 具体内容由业务平台提供，用于管理员审核的依据之一。
	 * @param Scope 发布范围---对于灰度发布范围的描述。对应业务平台的用户筛选条件。
	 * @param Operate 操作说明---01：添加体验用户 02：删除体验用户
	 * @param Mobile 用户手机号---用户手机号，必须在云平台是订购成员
	 * @return map
	 */
	public static Map<String, Object> syncGrayMembers(int testFlag, String ProductCode, String ProductName, String Version,
			String Desc, String Scope, String Operate, List<Map<String, Object>> empList) {
		logger.debug("##########  同步灰度用户列表信息  开始!  ##########");
		Map<String, Object> map = new HashMap<String, Object>();
		//消息标志-接口业务代码,5位,见ADC管理平台SI接口业务代码表
		String bizCode = ADCBizCode.GRAY_MEMBERS;
		//消息序列号-组成方式：”SI”＋8位日期＋12位唯一数，每天从000000000001开始，如SI20060801000000000001
		String transID = getTransID();
		//时间戳-格式为: yyyyMMddHHmmssnnnn
		String timeStamp = DateUtil.formatDate(new Date(), DateUtil.DATE_FORMAT_TIMESSS);
		//动作码(1-请求，2-应答)
		int actionCode = ADCActionCode.REQUEST;
		//业务应用标识(标识是哪个SI应用系统，每个SI系统约定（分配）一个标识) --- 此处可为空
		String SIAppID = "";
		//同步异步方式(0-同步操作 ，1-异步操作)
		int dealkind = 0;
		int priority = 0;
		String version = "V1.0";
		//请求报文体内容-具体业务数据，以XML表达
		String svcCont = assemblyProvisioningSvcCont(ProductCode, ProductName, Version, Desc, Scope, Operate, empList);
		AdcSiRequest request = new AdcSiRequest(SIAppID, actionCode, bizCode, dealkind, priority, svcCont, testFlag, timeStamp, 
				transID, version);
		AdcSiResponse response = null;
		//同步灰度用户列表信息
		try {
			SIInterfaceForADCServiceLocator adcServiceLocator = new SIInterfaceForADCServiceLocator();
			SIInterfaceForADC siInterfaceForADC = adcServiceLocator.getSIInterfaceForADC();
			response = siInterfaceForADC.ADCSIInterface(request);
			map.put("success", true);
			logger.error("#同步灰度用户列表信息成功");
		} catch (Exception e) {
			response = null;
			e.printStackTrace();
			map.put("success", false);
			logger.error("#同步灰度用户列表信息出错，异常信息：{}", e.getMessage());
		}
		logger.debug("##########  同步灰度用户列表信息  结束!  ##########");
		map.put("response", response);
		return map;
	}
	
	/**
	 * 组装灰度发布体验用户列表同步管理的业务报文.
	 * @param ProductCode 产品编码---由BOSS统一分配的产品标识,如果不走BOSS进行集团订购的产品，则由云平台分配
	 * @param ProductName 产品名称
	 * @param Version 版本号---如：1.2.3。该版本必需在云平台上已存在且为体验版。根据版本号可以查询该版本的功能描述。
	 * @param Desc 描述信息---描述本次发布的目的。如：网达定制版、ICT中心内部测试版等等。 具体内容由业务平台提供，用于管理员审核的依据之一。
	 * @param Scope 发布范围---对于灰度发布范围的描述。对应业务平台的用户筛选条件。
	 * @param Operate 操作说明---01：添加体验用户 02：删除体验用户
	 * @param Mobile 用户手机号---用户手机号，必须在云平台是订购成员
	 * @return String
	 */
	private static String assemblyProvisioningSvcCont(String ProductCode, String ProductName, String Version, String Desc, 
			String Scope, String Operate, List<Map<String, Object>> empList) {
		logger.debug("##########  组装灰度发布体验用户列表同步管理的业务报文  开始!  ##########");
		String mobileXML = "";
		String mobilesXML = "";
		for(Map<String, Object> map : empList) {
			mobileXML = StringUtil.formatMsg(XMLConstant.GRAY_MEMBERS_DETAIL, map.get("mobile").toString());
			mobilesXML += mobileXML;
		}
		String svcCont = StringUtil.formatMsg(XMLConstant.GRAY_MEMBERS_REQUEST, ProductCode, ProductName, Version, Desc, Scope, 
				Operate, mobilesXML);
		logger.debug("##########  组装灰度发布体验用户列表同步管理的业务报文  结束!  ##########");
		return svcCont;
	}
	
	/**
	 * 获取transId.
	 * @return String
	 */
	private static String getTransID() {
		//获取当天的日期字符串
		String currentDay = DateUtil.formatDate(new Date(), DateUtil.FORMAT_SHORT);
		//从TRANSID_MAP中获取当天的transID值
		Long transId = TRANSID_TABLE.get(currentDay);
		//如果当天的transId为空，新增
		transId = transId==null? 1 : transId;
		TRANSID_TABLE.clear();
		TRANSID_TABLE.put(currentDay, transId);
		return WebServiceUtil.getTransID(transId);
	}
}