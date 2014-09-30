/**
 * CopyRight © 2012 USTC SINOVATE SOFTWARE CO.LTD All Rights Reserved.
 */

package com.cmcc.zysoft.groupaddressbook.webservice.client;

import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
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
import com.cmcc.zysoft.sellmanager.model.Company;

/**
 * ICT服务开通和修改管理.
 * @author 袁凤建
 * <br />邮箱: yuan.fengjian@ustcinfo.com
 * <br />描述: IctServiceRegOrChangeClient.java
 * <br />版本: 1.0.0
 * <br />日期: 2013-7-8 下午2:43:15
 * <br />CopyRight © 2012 USTC SINOVATE SOFTWARE CO.LTD All Rights Reserved.
 */

@Component
public class IctServiceRegOrChangeClient {

	/**
	 * 日志.
	 */
	private static final Logger logger = LoggerFactory.getLogger(IctServiceRegOrChangeClient.class);
	
	/**
	 * 存放当天transID的map.
	 */
	private static final Hashtable<String, Long> TRANSID_TABLE = new Hashtable<String, Long>();
	
	/**
	 * 同步集团订购信息.
	 * @param TestFlag 测试标记---0：非测试交易, 1：测试交易
	 * @param OptType 操作类型---0-开通，1-修改
	 * @param company 企业信息
	 * @param ECName EC企业名称---要开通该业务的企业名称
	 * @param PrdCode 产品编号---BOSS分配的产品编号
	 * @param ServiceCode 业务编码---识别此业务的唯一标识(服务编码)
	 * @param PrdOrdNum 集团产品号码---标识集团订购关系
	 * @param ServiceName 业务名称
	 * @param AccessNo 业务基本接入号---每个业务都有一个基本接入号(短号)
	 * @param ECAccessPort EC接入端口号---每个EC订购都分配一个端口号，作为BOSS计费号码
	 * @param StartEfft 生效时间---该业务的生效日期。业务生效日期到达之前，暂时不向企业提供服务 格式:YYYY-MM-DD HH:MM:SS
	 * @param EndEfft 失效时间---格式:YYYY-MM-DD HH:MM:SS
	 * @param AdminUser EC管理员帐户名---开通时需要传一个EC管理员帐户(登陆名，在EC范围内唯一),此管理员可以登录进行业务管理分配成员使用SI业务
	 * @param AdminUFID EC管理员帐户标识---EC管理员的标识，唯一标识这个用户
	 * @param Priority 优先级---优先级，保留字段  现在用来传送Admin用户手机号码
	 * @param TrailFlag 是否存在试用期---0：无，下面的字段不必填写；1：有
	 * @param BizID 服务号码---BOSS根据产品自动生成：企业编号+001
	 * @param MemberNumLimit 成员许可数上限---取值范围：1~99999，表示可开通帐号数量，用于控制成员数量
	 * @param PlatType 业务平台模式---BOSS根据产品提供单选项：00500-模式1 00501-模式2
	 * @param SoftVersion 应用系统版本---A-标准版，B-增强版
	 * @param ICTCost 外勤管家集成费(元)---输入，支持输入2位小数，如100.99
	 * @param AreaCode 归属地市---A:合肥,B:芜湖,C:蚌埠,D:淮南,E:马鞍山,F:淮北,G:铜陵,H:安庆,J:黄山,K:阜阳,L:宿州,M:滁州,N:六安,P:宣城,Q:巢湖,R:贵池,S:亳州
	 * @param OPTNOTE 业务变更原因---添加、修改时均可为空
	 * @return map
	 */
	public static Map<String, Object> syncIctServiceRegOrChange(int testFlag, String OptType, Company company, String PrdCode, String ServiceCode,
			String PrdOrdNum, String ServiceName, String AccessNo, String ECAccessPort, String StartEfft, String EndEfft, 
			String AdminUser, String AdminUFID, String Priority, String TrailFlag, String BizID, String MemberNumLimit, String PlatType,
			String SoftVersion, String ICTCost, String AreaCode, String OPTNOTE) {
		logger.debug("##########  同步集团订购信息  开始!  ##########");
		Map<String, Object> map = new HashMap<String, Object>();
		//消息标志-接口业务代码,5位,见ADC管理平台SI接口业务代码表
		String bizCode = ADCBizCode.ICT_SERVICE_REGISTER_OR_CHANGE;
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
		String svcCont = assemblyIctServiceRegOrChangeSvcCont(OptType, company, PrdCode, ServiceCode, PrdOrdNum, ServiceName, AccessNo, ECAccessPort, StartEfft, EndEfft, AdminUser, AdminUFID, Priority, TrailFlag, BizID, MemberNumLimit, PlatType, SoftVersion, ICTCost, AreaCode, OPTNOTE);
		AdcSiRequest request = new AdcSiRequest(SIAppID, actionCode, bizCode, dealkind, priority, svcCont, testFlag, timeStamp, 
				transID, version);
		AdcSiResponse response = null;
		//同步集团订购信息
		try {
			SIInterfaceForADCServiceLocator adcServiceLocator = new SIInterfaceForADCServiceLocator();
			SIInterfaceForADC siInterfaceForADC = adcServiceLocator.getSIInterfaceForADC();
			response = siInterfaceForADC.ADCSIInterface(request);
			logger.error("#同步集团订购信息成功");
		} catch (Exception e) {
			response = null;
			e.printStackTrace();
			logger.error("#同步集团订购信息出错，异常信息：{}", e.getMessage());
		}
		logger.debug("##########  同步集团订购信息  结束!  ##########");
		map.put("response", response);
		return map;
	}
	
	/**
	 * 组装ICT服务开通和修改的业务报文.
	 * @param OptType 操作类型---0-开通，1-修改
	 * @param company 企业信息
	 * @param PrdCode 产品编号---BOSS分配的产品编号
	 * @param ServiceCode 业务编码---识别此业务的唯一标识(服务编码)
	 * @param PrdOrdNum 集团产品号码---标识集团订购关系
	 * @param ServiceName 业务名称
	 * @param AccessNo 业务基本接入号---每个业务都有一个基本接入号(短号)
	 * @param ECAccessPort EC接入端口号---每个EC订购都分配一个端口号，作为BOSS计费号码
	 * @param StartEfft 生效时间---该业务的生效日期。业务生效日期到达之前，暂时不向企业提供服务 格式:YYYY-MM-DD HH:MM:SS
	 * @param EndEfft 失效时间---格式:YYYY-MM-DD HH:MM:SS
	 * @param AdminUser EC管理员帐户名---开通时需要传一个EC管理员帐户(登陆名，在EC范围内唯一),此管理员可以登录进行业务管理分配成员使用SI业务
	 * @param AdminUFID EC管理员帐户标识---EC管理员的标识，唯一标识这个用户
	 * @param Priority 优先级---优先级，保留字段  现在用来传送Admin用户手机号码
	 * @param TrailFlag 是否存在试用期---0：无，下面的字段不必填写；1：有
	 * @param BizID 服务号码---BOSS根据产品自动生成：企业编号+001
	 * @param MemberNumLimit 成员许可数上限---取值范围：1~99999，表示可开通帐号数量，用于控制成员数量
	 * @param PlatType 业务平台模式---BOSS根据产品提供单选项：00500-模式1 00501-模式2
	 * @param SoftVersion 应用系统版本---A-标准版，B-增强版
	 * @param ICTCost 外勤管家集成费(元)---输入，支持输入2位小数，如100.99
	 * @param AreaCode 归属地市---A:合肥,B:芜湖,C:蚌埠,D:淮南,E:马鞍山,F:淮北,G:铜陵,H:安庆,J:黄山,K:阜阳,L:宿州,M:滁州,N:六安,P:宣城,Q:巢湖,R:贵池,S:亳州
	 * @param OPTNOTE 业务变更原因---添加、修改时均可为空
	 * @return String
	 */
	private static String assemblyIctServiceRegOrChangeSvcCont(String OptType, Company company, String PrdCode, String ServiceCode,
			String PrdOrdNum, String ServiceName, String AccessNo, String ECAccessPort, String StartEfft, String EndEfft, 
			String AdminUser, String AdminUFID, String Priority, String TrailFlag, String BizID, String MemberNumLimit, String PlatType,
			String SoftVersion, String ICTCost, String AreaCode, String OPTNOTE) {
		//数据来源(01-应用系统)
		String EcFrom = "01";
		//EC企业编码---要开通业务的企业代码(订购服务的客户)
		String ECCode= company.getCompanyCode();
		//EC企业名称---要开通该业务的企业名称
		String ECName = company.getCompanyName();
		//SI编码
		String SICode = "912057";
		//SI名称
		String SIName = "安徽移动";
		//转正式商用时间 格式：YYYY-MM-DD HH:MM:SS
		String ToBusinessTime = "";
		//试用业务量上限
		String BusiLimit = "";
		//业务配置参数名称
		String PARAMNAME = "";
		//业务配置参数值
		String PARAMVALUE = "";
		//订购功能点参数名称
		String POINTNAME = "";
		//订购功能点参数值
		String POINTVALUE = "";
		//业务License
		String LICENSE = "";
		logger.debug("##########  组装ICT服务开通和修改的业务报文  开始!  ##########");
		String svcCont = StringUtil.formatMsg(XMLConstant.PROVISIONG_ICT_SERVICE_REQUEST, EcFrom, SICode, SIName, OptType, ECCode, 
				ECName, PrdCode, ServiceCode, PrdOrdNum, ServiceName, AccessNo, ECAccessPort, StartEfft, EndEfft, AdminUser, AdminUFID,
				Priority, TrailFlag, ToBusinessTime, BusiLimit, PARAMNAME, PARAMVALUE, POINTNAME, POINTVALUE, LICENSE, BizID, 
				MemberNumLimit, PlatType, SoftVersion, ICTCost, AreaCode, OPTNOTE);
		logger.debug("##########  组装ICT服务开通和修改的业务报文  结束!  ##########");
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