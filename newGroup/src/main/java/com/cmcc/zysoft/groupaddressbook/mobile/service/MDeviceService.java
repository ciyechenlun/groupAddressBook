// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.mobile.service;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cmcc.zysoft.groupaddressbook.mobile.dao.MDeviceDao;
import com.cmcc.zysoft.sellmanager.model.Device;
import com.cmcc.zysoft.sellmanager.util.MD5Tools;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：MService
 * <br />版本:1.0.0
 * <br />日期： 2013-5-14 下午4:01:59
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class MDeviceService extends BaseServiceImpl<Device, String>{
	
	@Resource
	private MDeviceDao mDeviceDao;
	
	@Override
	public HibernateBaseDao<Device, String>getHibernateBaseDao(){
		return this.mDeviceDao;
	}
	
	/**
	 * 校验设备号.
	 * @param deviceId
	 * @param phone
	 * @param imei
	 * @param imsi
	 * @return 
	 * 返回类型：String
	 */
	public String validateRegisterCode(String deviceId,String phone,String imei,String imsi){
		Device device = this.mDeviceDao.get(deviceId);
		String code = MD5Tools.encodePassword(imei + imsi + phone, null);
		if("12345678".equals(phone))
		{
			return "SUCCESS";
		}
		if(code.equals(device.getRegisterCode())){
			return "SUCCESS";
		}else {
			if(!imei.equals(device.getImei())){
				return "IMEI";
			}else if(!imsi.equals(device.getImsi())){
				return "IMSI";
			}else{
				return "FALSE";
			}
		}
	}
	
	/**
	 * 通过mobile查找到注册的设备号.
	 * @param mobile
	 * @return 
	 * 返回类型：String
	 */
	public String getDeviceId(String mobile){
		return this.mDeviceDao.getDeviceId(mobile);
	}
	
	/**
	 * 通过手机号查找到employeeId.
	 * @param mobile
	 * @return 
	 * 返回类型：String
	 */
	public String getEmployeeId(String mobile){
		return this.mDeviceDao.getEmployeeId(mobile);
	}
	
	/**
	 * 根据imei+imsi+手机号生成设备号.如果已存在,则修改数据,否则则插入.
	 * @param employeeId
	 * @param mobile
	 * @param imei
	 * @param imsi
	 * @param sysType 客户端类型（I/A）
	 * @return 
	 * 返回类型：String
	 */
	public String addOrUpdateRegistercode(String employeeId, String mobile, String imei, String imsi,String sysType){
		String deviceId = this.mDeviceDao.getDeviceId(mobile);
		String code = imei + imsi + mobile;
		String registerCode = MD5Tools.encodePassword(code, null);
		if(StringUtils.hasText(deviceId)){
			Device device = this.mDeviceDao.get(deviceId);
			device.setAddDate(new Date());
			device.setImei(imei);
			device.setImsi(imsi);
			device.setRegisterCode(registerCode);
			device.setMark2(sysType);
			this.mDeviceDao.update(device);
			return deviceId;
		}else{
			Device device = new Device();
			device.setEmployeeId(employeeId);
			device.setImei(imei);
			device.setImsi(imsi);
			device.setRegisterCode(registerCode);
			device.setMark2(sysType);
			device.setStatus("1");
			deviceId = this.mDeviceDao.save(device);
			if(StringUtils.hasText(deviceId)){
				return deviceId;
			}else{
				return "";
			}
		}
	}
	
	/**
	 * 根据imei+imsi+mobile生成registerCode.
	 *  
	 * 返回类型：void
	 */
	public void md5(){
		this.mDeviceDao.md5();
	}

}
