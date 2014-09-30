// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.util;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;

import com.chinamobile.openmas.client.Sms;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：sendMsg
 * <br />版本:1.0.0
 * <br />日期： 2013-3-29 下午5:25:21
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
public class sendMsg {
	
	/**
	 * 发送短信.
	 * @param msg 信息内容
	 * @param address 手机号
	 * @return 
	 * 返回类型：String
	 */
	public static String sendMMM(String msg,String address){
		Sms sms;
		String GateWayid = "";
		try {
			sms = new Sms("http://211.138.183.2:9080/openmasservice");
			String[] destinationAddresses = new String[]{address};
		    String extendCode = ""; //自定义扩展代码（模块）
		    String ApplicationID= "DefaultApplicationTXL";
		    String Password = "yCsGnaPEYSTXL";
		    //发送短信
		    GateWayid = sms.SendMessage(destinationAddresses, msg, extendCode, ApplicationID, Password);
		    return GateWayid;
		} catch (AxisFault e) {
			e.printStackTrace();
			return "";
		} catch (RemoteException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 发送短信.(mas接口，批量)
	 * @param msg 信息内容
	 * @param address 手机号
	 * @return 
	 * 返回类型：String
	 */
	@SuppressWarnings("unused")
	public static String sendMMAll(String msg,String address){
		Sms sms;
		String GateWayid = "";
		try {
			
			String[] phones = address.split("[,]");
			
			sms = new Sms("http://211.138.183.2:9080/openmasservice");
			//String[] destinationAddresses = new String[]{address};
		    String extendCode = ""; //自定义扩展代码（模块）
		    String ApplicationID= "DefaultApplicationTXL";
		    String Password = "yCsGnaPEYSTXL";
		    //发送短信
		    
		    GateWayid = sms.SendMessage(phones, msg, extendCode, ApplicationID, Password);
		    return "SUCCESS";
		} catch (AxisFault e) {
			e.printStackTrace();
			return "";
		} catch (RemoteException e) {
			e.printStackTrace();
			return "";
		}
	}

}
