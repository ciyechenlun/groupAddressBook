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
 * ICT客户成员订购管理.
 * @author 袁凤建
 * <br />邮箱: yuan.fengjian@ustcinfo.com
 * <br />描述: MemberShipICTClient.java
 * <br />版本: 1.0.0
 * <br />日期: 2013-7-8 上午10:16:33
 * <br />CopyRight © 2012 USTC SINOVATE SOFTWARE CO.LTD All Rights Reserved.
 */

@Component
public class MemberShipICTClient {
	
	/**
	 * 日志.
	 */
	private static final Logger logger = LoggerFactory.getLogger(MemberShipICTClient.class);

	
	/**
	 * 存放当天transID的map.
	 */
	private static final Hashtable<String, Long> TRANSID_TABLE = new Hashtable<String, Long>();

	/**
	 * 同步ICT客户成员订购信息.
	 * @param testFlag 测试标记(0：非测试交易, 1：测试交易)
	 * @param ECCode EC企业代码: 要修改成员的业务的企业计费代码
	 * @param ECName EC企业名称: 该业务的企业名称
	 * @param PrdOrdNum 集团产品代码: 订购关系的唯一标示
	 * @param ServiseCode 服务标识: 服务的唯一标识
	 * @param ServiseName 服务名称
	 * @param ItemName 扩展信息名称
	 * @param ItemValue 信息值
	 * @param empList 成员信息列表
	 * @param OptType 操作类型: 01－加入名单 02－退出名单  03－定购  04－取消定购  05-业务信息变更
	 * @param AuthType 登录鉴权类型: 0-普通鉴权 1-手机号码+IMSI绑定鉴权
	 * @return map
	 */
	public static Map<String, Object> syncMemberShip(int testFlag, String ECCode, String ECName, String PrdOrdNum, String ServiseCode, 
			String ServiseName, String ItemName, String ItemValue, List<Map<String,Object>> empList, String OptType, String AuthType) {
		logger.debug("##########  同步ICT客户成员订购信息  开始!  ##########");
		Map<String, Object> map = new HashMap<String, Object>();
		//消息标志-接口业务代码,5位,见ADC管理平台SI接口业务代码表
		String bizCode = ADCBizCode.ICT_CUSTOMER_MEMBERS_ORDER;
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
		String svcCont = assemblyMemberShipICTSvcCont(ECCode, ECName, PrdOrdNum, ServiseCode, ServiseName, ItemName, ItemValue, 
				empList, OptType, AuthType);
		AdcSiRequest request = new AdcSiRequest(SIAppID, actionCode, bizCode, dealkind, priority, svcCont, testFlag, timeStamp, 
				transID, version);
		AdcSiResponse response = null;
		//同步ICT客户成员订购信息
		try {
			SIInterfaceForADCServiceLocator adcServiceLocator = new SIInterfaceForADCServiceLocator();
			SIInterfaceForADC siInterfaceForADC = adcServiceLocator.getSIInterfaceForADC();
			response = siInterfaceForADC.ADCSIInterface(request);
			map.put("success", true);
			logger.error("#同步ICT客户成员订购信息成功");
		} catch (Exception e) {
			response = null;
			e.printStackTrace();
			map.put("success", false);
			logger.error("#同步ICT客户成员订购信息出错，异常信息：{}", e.getMessage());
		}
		logger.debug("##########  同步ICT客户成员订购信息  结束!  ##########");
		map.put("response", response);
		return map;
	}
	
	/**
	 * 组装ICT客户成员订购管理的业务报文.
	 * @param ECCode EC企业代码: 要修改成员的业务的企业计费代码
	 * @param ECName EC企业名称: 该业务的企业名称
	 * @param PrdOrdNum 集团产品代码: 订购关系的唯一标示
	 * @param ServiseCode 服务标识: 服务的唯一标识
	 * @param ServiseName 服务名称
	 * @param ItemName 扩展信息名称
	 * @param ItemValue 信息值
	 * @param empList 成员信息列表
	 * @return String
	 */
	private static String assemblyMemberShipICTSvcCont(String ECCode, String ECName, String PrdOrdNum, String ServiseCode, 
			String ServiseName, String ItemName, String ItemValue, List<Map<String,Object>> empList, String OptType, String AuthType) {
		//数据来源: 01-应用系统
		String UserFrom = "01";
		//成员变更列表: 每次修改可有多个成员，更新类型包括新增，修改，删除
		String Members = assemblyMembersDetail(PrdOrdNum, empList, OptType, AuthType);
		logger.debug("##########  组装ICT客户成员订购管理的业务报文  开始!  ##########");
		//组装ICT客户成员订购管理的业务报文
		String svcCont = StringUtil.formatMsg(XMLConstant.ICT_CUSTOMER_MEMBERS_ORDER_REQUEST, UserFrom, ECCode, ECName, PrdOrdNum,
				ServiseCode, ServiseName, Members, ItemName, ItemValue);
		logger.debug("##########  组装ICT客户成员订购管理的业务报文  结束!  ##########");
		return svcCont;
	}
	
	/**
	 * 组装成员信息报文.
	 * @param BizID 服务号码
	 * @param empList 成员信息列表
	 * @param OptType 操作类型: 01－加入名单 02－退出名单  03－定购  04－取消定购  05-业务信息变更
	 * @param AuthType 登录鉴权类型: 0-普通鉴权 1-手机号码+IMSI绑定鉴权
	 * @return String
	 */
	private static String assemblyMembersDetail(String BizID, List<Map<String,Object>> empList, String OptType, String AuthType) {
		//成员号码
		String MemberMobile = "";
		//成员名称: 默认传手机号
		String MemberName = "";
		//成员账号: 默认传手机号
		String MemberAccount = "";
		//成员代码: 用户的唯一标识，由ADC生成
		String UFID = "";
		//业务平台类型: 00500-模式1 00501-模式2
		String PlatType = "00500";
		//外勤管家功能费成交价
		String ICTFee = "0.00";
		//付费主体: 0-个人付费，1-集团统付
		String ICTFeePayer = "1";
		//更新时间
		String EditDate = DateUtil.formatDate(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD);
		String ItemName = "";
		String ItemValue = "";
		String memberXML = "";
		String membersXML = "";
		String imsi = "";
		logger.debug("##########  组装成员信息报文  开始!  ##########");
		for(Map<String,Object> map : empList) {
			MemberMobile = map.get("mobile").toString();
			MemberName = map.get("employee_name").toString();
			MemberAccount = map.get("mobile").toString();
			UFID = map.get("employee_id").toString();
			imsi = map.get("id_card").toString();
			memberXML = StringUtil.formatMsg(XMLConstant.ICT_CUSTOMER_MEMBERS_DETAIL, OptType, MemberMobile, MemberName,
					MemberAccount, UFID, BizID, PlatType, AuthType, ICTFee, ICTFeePayer, EditDate, ItemName, ItemValue, imsi);
			membersXML += memberXML;
		}
		logger.debug("##########  组装成员信息报文  结束!  ##########");
		return membersXML;
	}
	
	/**
	 * 获取transId
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