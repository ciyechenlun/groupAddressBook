// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.dao;

import org.springframework.stereotype.Repository;

import com.cmcc.zysoft.sellmanager.model.Device;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：DeviceDao
 * <br />版本:1.0.0
 * <br />日期： 2013-5-14 下午3:04:52
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Repository
public class DeviceDao extends HibernateBaseDaoImpl<Device, String>{
	
	/**
	 * 根据用户ID返回设备注册信息
	 * @param employee_id
	 * @return
	 */
	public Device getDeivceByEmployeeId(String employee_id)
	{
		String sql = "FROM Device WHERE employeeId=?";
		return (Device) this.getHibernateTemplate().find(sql, employee_id).get(0);
	}
}
