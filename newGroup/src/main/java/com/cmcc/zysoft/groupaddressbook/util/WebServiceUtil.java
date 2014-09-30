/**
 * CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */

package com.cmcc.zysoft.groupaddressbook.util;

import java.util.Date;

import com.cmcc.zysoft.framework.utils.DateUtil;
import com.cmcc.zysoft.groupaddressbook.webservice.client.AdcSiRequest;
import com.cmcc.zysoft.groupaddressbook.webservice.client.AdcSiResponse;

/**
 * 接口工具类.
 * @author 袁凤建
 * <br />邮箱：yuan.fengjian@ustcinfo.com
 * <br />描述：WebServiceUtil.java
 * <br />版本: 1.0.0
 * <br />日期：2013-7-2 上午9:08:34
 * <br />CopyRight © 2012 USTC SINOVATE SOFTWARE CO.LTD All Rights Reserved.
 */

public class WebServiceUtil {

	/**
	 * 消息序列号前缀.
	 */
	private static final String PREFIX_TRANS_ID = "SI";

	/**
	 * 获取消息序列号.
	 * @param id 序列号
	 * @return 规定格式序列化
	 */
	public static String getTransID(long id) {
		return PREFIX_TRANS_ID + DateUtil.formatDate(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD) + generateIDString(id);
	}

	/**
	 * 生成12位的ID字符串.
	 * @param id 序列号
	 * @return 序列号字符串
	 */
	public static String generateIDString(long id) {
		String idStr = String.valueOf(id);
		int length = idStr.length();
		for (int i = 12 - length; i > 0; i--) {
			idStr = "0" + idStr;
		}
		return idStr;
	}

	/**
	 * 生成时间戳.
	 * @return String
	 */
	public static String generateTimeStamp() {
		return DateUtil.formatDate(new Date(), DateUtil.DATE_FORMAT_TIMESSS);
	}

	/**
	 * 复制request的属性到response
	 * @param request
	 * @return adcSiResponse
	 */
	public static AdcSiResponse copyReq2Res(AdcSiRequest request) {
		AdcSiResponse response = new AdcSiResponse();
		response.setActionCode(request.getActionCode());
		response.setBizCode(request.getBizCode());
		response.setDealkind(request.getDealkind());
		response.setPriority(request.getPriority());
		response.setSIAppID(request.getSIAppID());
		response.setTransID(request.getTransID());
		response.setTestFlag(request.getTestFlag());
		response.setTimeStamp(request.getTimeStamp());
		response.setVersion(request.getVersion());
		return response;
	}
}