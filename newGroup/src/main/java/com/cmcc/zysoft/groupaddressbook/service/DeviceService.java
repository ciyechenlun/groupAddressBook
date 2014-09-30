// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.dao.DeviceDao;
import com.cmcc.zysoft.sellmanager.model.Device;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：DeviceService
 * <br />版本:1.0.0
 * <br />日期： 2013-5-14 下午3:05:43
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class DeviceService extends BaseServiceImpl<Device, String>{
	
	@Resource
	private DeviceDao deviceDao;
	
	@Override
	public HibernateBaseDao<Device, String>getHibernateBaseDao(){
		return this.deviceDao;
	}
	
	/**
	 * 根据用户ID返回设备注册信息
	 * @param employee_id
	 * @return
	 */
	public Device getDeivceByEmployeeId(String employee_id)
	{
		return this.getDeivceByEmployeeId(employee_id);
	}
	
}
