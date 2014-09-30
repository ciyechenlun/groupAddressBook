// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.mobile.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmcc.zysoft.sellmanager.model.Device;
import com.cmcc.zysoft.sellmanager.util.MD5Tools;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：MDeviceDao
 * <br />版本:1.0.0
 * <br />日期： 2013-5-14 下午3:57:34
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Repository
public class MDeviceDao extends HibernateBaseDaoImpl<Device, String>{
	
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 通过手机号查找到注册的设备号.
	 * @param mobile
	 * @return 
	 * 返回类型：String
	 */
	public String getDeviceId(String mobile){
		String sql = "SELECT " +
				"dev.device_id " +
				"FROM tb_c_device dev,tb_c_system_user us,tb_c_employee emp " +
				"WHERE emp.employee_id=dev.employee_id " +
				"AND dev.status='1' " +
				"AND emp.employee_id=us.employee_id AND dev.employee_id=us.employee_id " +
				"AND us.user_name=?";
		List<Map<String, Object>> deviceIds = this.jdbcTemplate.queryForList(sql,mobile);
		if(deviceIds.size()>0){
			return deviceIds.get(0).get("device_id").toString();
		}else{
			return "";
		}
	}
	
	/**
	 * 通过手机号查找到employeeId.
	 * @param mobile
	 * @return 
	 * 返回类型：String
	 */
	public String getEmployeeId(String mobile){
		String sql = "SELECT us.employee_id FROM tb_c_system_user us WHERE us.user_name=?";
		List<Map<String, Object>> employeeIds = this.jdbcTemplate.queryForList(sql,mobile);
		if(employeeIds.size()>0){
			return employeeIds.get(0).get("employee_id").toString();
		}else{
			return "";
		}
	}
	
	/**
	 * 根据imei+imsi+mobile生成registerCode.
	 *  
	 * 返回类型：void
	 */
	public void md5(){
		importDeviceInfoFromEmployee();
		String rowSql = "SELECT " +
				"device_id,imei,imsi,mark1 FROM tb_c_device";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(rowSql);
		String imei,imsi,phone,register_code = "";
		for(int i=0;i<list.size();i++){
			imei = list.get(i).get("imei").toString();
			imsi = list.get(i).get("imsi").toString();
			phone = list.get(i).get("mark1").toString();
			register_code = MD5Tools.encodePassword(imei+imsi+phone, null);
			String sql = "UPDATE tb_c_device set register_code='"+register_code+"' " +
					"where device_id='"+list.get(i).get("device_id").toString()+"'";
			this.jdbcTemplate.execute(sql);
		}
	}
	
	/**
	 * 将在设备表不存在的用户设备信息导入至tb_c_device
	 */
	private void importDeviceInfoFromEmployee()
	{
		String sql = "INSERT INTO tb_c_device (employee_id,device_id,imei,imsi,mark1) SELECT employee_id,REPLACE(UUID(),'-',''),id_card,qq,mobile FROM tb_c_employee WHERE " +
				"employee_id NOT IN (SELECT employee_id FROM tb_c_device) AND id_card!='' AND qq!=''";
		this.jdbcTemplate.execute(sql);
	}

}
