/**
 * CopyRight © 2013 USTC SINOVATE SOFTWARE CO.LTD All Rights Reserved.
 */

package com.cmcc.zysoft.groupaddressbook.webservice.SIInterfaceForADCSoap;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import org.apache.axis.AxisProperties;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cmcc.zysoft.framework.utils.DateUtil;
import com.cmcc.zysoft.framework.utils.StringUtil;
import com.cmcc.zysoft.framework.utils.XMLUtil;
import com.cmcc.zysoft.groupaddressbook.constant.ADCActionCode;
import com.cmcc.zysoft.groupaddressbook.constant.ADCBizCode;
import com.cmcc.zysoft.groupaddressbook.util.WebServiceUtil;
import com.cmcc.zysoft.groupaddressbook.util.XMLConstant;

/**
 * 短号获取.
 * @author 张军
 * <br />邮箱: zhang.jun3@ustcinfo.com
 * <br />描述: MobileShortICTClient.java
 * <br />版本: 1.0.0
 * <br />日期: 2013-12-10 上午10:16:33
 * <br />CopyRight © 2013 USTC SINOVATE SOFTWARE CO.LTD All Rights Reserved.
 */


public class MobileShortICTClient {
	
	/**
	 * 日志.
	 */
	private static final Logger logger = LoggerFactory.getLogger(MobileShortICTClient.class);

	
	/**
	 * 存放当天transID的map.
	 */
	private static final Hashtable<String, Long> TRANSID_TABLE = new Hashtable<String, Long>();

	/**
	 * 根据手机长号获取手机短号，v网id
	 * @param testFlag
	 * @param mobile 手机长号
	 * @return
	 */
	public static Map<String,String> getMobileShortAndVId(String mobile) {
		if(null == mobile || ("").equals(mobile)){
			return null;
		}
		logger.debug("##########  获取短号  开始!  ##########");
		//消息标志-接口业务代码,5位,见ADC管理平台SI接口业务代码表
		String bizCode = ADCBizCode.GET_MOBILE_SHORT;
		//消息序列号-组成方式：”SI”＋8位日期＋12位唯一数，每天从000000000001开始，如SI20060801000000000001
		String transID = getTransID();
		//时间戳-格式为: yyyyMMddHHmmssnnnn
		String timeStamp = DateUtil.formatDate(new Date(), DateUtil.DATE_FORMAT_TIMESSS);
		//动作码(1-请求，2-应答)
		int actionCode = ADCActionCode.REQUEST;
		//业务应用标识(标识是哪个SI应用系统，每个SI系统约定（分配）一个标识) --- 此处可为空
		String SIAppID = "1";
		//同步异步方式(0-同步操作 ，1-异步操作)
		int dealkind = 1;
		int priority = 1;
		String version = "V1.0";
		//测试标记:发起方填写，0：非测试交易，1：测试交易；测试必须是业务级别，即在同一个业务流水中所有交易必须具有相同的测试标记(默认为0即可)
		int testFlag = 0;
		//请求报文体内容-具体业务数据，以XML表达
		List<String> mobileList = new ArrayList<String>();
		mobileList.add(mobile);
		String svcCont = assemblyMobileShortICTSvcCont(mobileList);
		AdcSiRequestX request =  new AdcSiRequestX(bizCode, transID, timeStamp, actionCode, SIAppID, testFlag, dealkind, priority, version, svcCont);
		AdcSiResponseX response = null;
		//同步ICT客户成员订购信息
		try {
			AxisProperties.setProperty("http.proxyHost", "proxy.ah.cmcc");
			AxisProperties.setProperty("http.proxyPort", "8080");
			SIInterfaceForADCLocator adcLocator = new SIInterfaceForADCLocator();
			SIInterfaceForADCSoap_PortType portType = adcLocator.getSIInterfaceForADCSoap();
			response = portType.ADCSIInterface(request);
			logger.info(response.toString());
			logger.error("#获取短号成功");
		} catch (Exception e) {
			response = null;
			e.printStackTrace();
			logger.error("#获取短号出错，异常信息：{}", e.getMessage());
		}
		logger.debug("##########  获取短号  结束!  ##########");
		List<Map<String,String>> list = parseXml(response);
		if(null != list && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	/**
	 * 多手机号查询
	 * @param mobileList
	 * @return
	 */
	public static List<Map<String,String>> getMobileShortAndVId(List<String> mobileList) {
		if(null == mobileList || mobileList.size()==0){
			return null;
		}
		logger.debug("##########  获取短号  开始!  ##########");
		//消息标志-接口业务代码,5位,见ADC管理平台SI接口业务代码表
		String bizCode = ADCBizCode.GET_MOBILE_SHORT;
		//消息序列号-组成方式：”SI”＋8位日期＋12位唯一数，每天从000000000001开始，如SI20060801000000000001
		String transID = getTransID();
		//时间戳-格式为: yyyyMMddHHmmssnnnn
		String timeStamp = DateUtil.formatDate(new Date(), DateUtil.DATE_FORMAT_TIMESSS);
		//动作码(1-请求，2-应答) 
		int actionCode = ADCActionCode.REQUEST;
		//业务应用标识(标识是哪个SI应用系统，每个SI系统约定（分配）一个标识) --- 此处可为空
		String SIAppID = "1";
		//同步异步方式(0-同步操作 ，1-异步操作)
		int dealkind = 1;
		int priority = 1; 
		String version = "V1.0";
		//测试标记:发起方填写，0：非测试交易，1：测试交易；测试必须是业务级别，即在同一个业务流水中所有交易必须具有相同的测试标记(默认为0即可)
		int testFlag = 0;
		//请求报文体内容-具体业务数据，以XML表达
		String svcCont = assemblyMobileShortICTSvcCont(mobileList);
		AdcSiRequestX request = new AdcSiRequestX(bizCode, transID, timeStamp, actionCode, SIAppID, testFlag, dealkind, priority, version, svcCont);
		AdcSiResponseX response = null;
		//同步ICT客户成员订购信息
		try {
			//AxisProperties.setProperty("http.proxyHost", "proxy.ah.cmcc");
			//AxisProperties.setProperty("http.proxyPort", "8080");
			SIInterfaceForADCLocator adcLocator = new SIInterfaceForADCLocator();
			SIInterfaceForADCSoap_PortType portType = adcLocator.getSIInterfaceForADCSoap();
			response = portType.ADCSIInterface(request);
			logger.error("#获取短号成功");
		} catch (Exception e) {
			response = null;
			e.printStackTrace();
			logger.error("#获取短号出错，异常信息：{}", e.getMessage());
		}
		logger.debug("##########  获取短号  结束!  ##########");
		return parseXml(response);
	}
	
	/**
	 * 组装获取短号的报文
	 * @param mobile 手机长号
	 * @return
	 */
	private static String assemblyMobileShortDetail(List<String> mobileList) {
		String mobileNums="";
		for (int i=0;i<mobileList.size();i++) {
			String ordSeq = DateUtil.formatDate(new Date(), DateUtil.DB_TIME_PATTERN)+"00"+(i+1);
			mobileNums+= StringUtil.formatMsg(XMLConstant.MOBILE_SHORT_REQUEST,ordSeq,mobileList.get(i));
		}
		return mobileNums;
	}
	/**
	 * 组装获取短号的报文
	 * @param mobile 手机长号
	 * @return
	 */
	private static String assemblyMobileShortICTSvcCont(List<String> mobileList) {
		logger.debug("##########  组装获取短号的报文  开始!  ##########");
		//组装获取短号的报文
		String mobileNums = assemblyMobileShortDetail(mobileList);
		String svcCont = StringUtil.formatMsg(XMLConstant.MOBILE_SHORT_REQUEST_HEAD,mobileNums);
		logger.debug("##########  组装获取短号的报文  结束!  ##########");
		return svcCont;
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
	/**
	 * 解析xml
	 * @param response
	 * @return
	 */
	public static List<Map<String,String>> parseXml(AdcSiResponseX response){
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if(null != response && null != response.getSvcCont()){
			try {
				Document  doc = XMLUtil.parseXML(response.getSvcCont());
				Element  ShortNumResponse = doc.getRootElement();
				for(Iterator i = ShortNumResponse.elementIterator(); i.hasNext();){   
					Element OprResult = (Element) i.next();   
					for(Iterator j = OprResult.elementIterator(); j.hasNext();){  
						Map<String, String> map = new HashMap<String, String>();
						Element BizRegRsp=(Element) j.next();   
						for(Iterator x = BizRegRsp.elementIterator(); x.hasNext();){ 
							Element RealInfo = (Element) x.next();   
							for(Iterator y = RealInfo.elementIterator(); y.hasNext();){
								Element mobileNum = (Element) y.next();  
								System.out.println(mobileNum.getName()+":"+mobileNum.getText());
								map.put(mobileNum.getName(), mobileNum.getText());
							}
						} 
						list.add(map);
					}
				} 
				System.out.println("****"+list);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}
}