/**
 * CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */

package com.cmcc.zysoft.groupaddressbook.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpException;
import org.springframework.util.StringUtils;

import com.cmcc.zysoft.HttpClientUtil.HttpClientUtil;

/**
 * @author 袁凤建
 * <br />邮箱：yuan.fengjian@ustcinfo.com
 * <br />描述：SendMsgTest.java
 * <br />版本: 1.0.0
 * <br />日期：2013-6-20 上午11:16:37
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */

public class SendMsgTest {

	public static void main(String[] args) {
		String result = msgSendTest();
		System.out.println("========================================");
		System.out.println("result : " + result);
		System.out.println("========================================");
		
	}
	
	/**
	 * 发送短信URL
	 */
	public static final String MESS_SEND = "http://120.209.138.191/smms/provider/full/sms?msg=%s&spid=%s&ospid=%s";
	/**
	 * 测试发送短信.
	 * @return String
	 */
	public static String msgSendTest() {
		//手机号码
		String teles = "18949093199";
		//内容
		String content = "您好,您的手机号码是15956909259!";
		//对短信内容进行编码(解决无法发送中文问题)
		try {
			content = URLEncoder.encode(content, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "FAIL";
		}
		//发送短信URL
		String url = String.format(Constant.MESS_SEND, content, teles, Constant.SMSS_SPID, Constant.SMSS_OSPID);
		try {
			//发送短信
			String result = HttpClientUtil.get(url,1800000,teles);
			//判断发送短信是否成功 成功 将标示定为：SUCCESS 失败：FAIL
			if(StringUtils.hasText(result) && result.length() > 11) {
				String subResult = result.substring(9, 11);
				//同网返回01,异网返回R0
				if("01".equals(subResult) || "R0".equals(subResult)) {
					return "SUCCESS";
				}
			}
		} catch (HttpException e) {
			e.printStackTrace();
			return "FAIL";
		}
		return "FAIL";
	}
	
	/**
	 * 发送短信.
	 * @param content 短信内容
	 * @param mobile 手机号码
	 * @return String(成功:SUCCESS,失败:FAIL)
	 */
	public static String sendMsg(String content, String mobile) {
		//对短信内容进行编码(解决无法发送中文问题)
		try {
			content = URLEncoder.encode(content, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "FAIL";
		}
		//发送短信URL
		String url = String.format(MESS_SEND, content, Constant.SMSS_SPID, Constant.SMSS_OSPID);
		try {
			//发送短信
			String result = HttpClientUtil.get(url,1800000,mobile);
			//判断发送短信是否成功 成功 将标示定为：SUCCESS 失败：FAIL
			if(StringUtils.hasText(result) && result.length() > 11) {
				String subResult = result.substring(9, 11);
				//同网返回01,异网返回R0
				if("01".equals(subResult) || "R0".equals(subResult)) {
					return "SUCCESS";
				}
			}
		} catch (HttpException e) {
			e.printStackTrace();
			return "FAIL";
		}
		return "FAIL";
	}
}