// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.mobile.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmcc.zysoft.sellmanager.model.SystemUser;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：MloginDao
 * <br />版本:1.0.0
 * <br />日期： 2013-3-4 上午10:26:19
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Repository
public class MLoginDao extends HibernateBaseDaoImpl<SystemUser, String>{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 校验登陆用户.
	 * @param userCode 
	 * @return 
	 * 返回类型：Map<String,Object>
	 */
	public Map<String, Object> loginUser(String userCode){
		String sql = "SELECT " +
				"us.user_id AS user_id," + 
				"emp.employee_id AS empId," + 
				"emp.employee_name AS empName," + 
				"emp.email AS email, " +
				"emp.mobile AS mobile," +
				"IFNULL(emp.mobile_short,'') AS mobile_short," +
				"IFNULL(emp.telephone2,'') AS tel," +
				"IFNULL(emp.tel_short,'') AS tel_short," +
				"dept.department_name AS deptName," +
				"dept.department_id AS deptId," +
				"dept.department_level AS deptLevel," + 
				"emp.headship_id AS headshipId," +
				"(SELECT dat.data_content " +
				"FROM tb_c_dict_data dat,tb_c_dict_type typ " +
				"WHERE typ.type_id=dat.type_id AND typ.type_code='headship' AND emp.headship_id=dat.data_code) " +
				"AS headshipName " +
				"FROM tb_c_employee emp,tb_c_system_user us,tb_c_user_company uc,tb_c_user_department ud,tb_c_department dept,tb_c_headship hs "+
				"WHERE emp.employee_id=us.employee_id AND emp.employee_id=uc.employee_id AND uc.user_company_id=ud.user_company_id "+
				"AND ud.department_id=dept.department_id AND ud.headship_id=hs.headship_id "+
				//add by liyuchen 成员管理，判断成员是否被禁用
				"AND uc.forbidden_flag<>'1' "+
				//end add
				"AND us.user_name=? order by hs.headship_level limit 1";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,userCode);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * 判断用户是否为员工.
	 * @param userId 
	 * @return 
	 * 返回类型：boolean
	 */
	public boolean checkIsEmployee(String userId){
		String sql = "SELECT COUNT(*) " +
				"FROM tb_c_system_user us ,tb_c_employee emp WHERE emp.employee_id=us.employee_id AND us.user_id=?";
		int num = this.jdbcTemplate.queryForInt(sql,userId);
		if(num==0){
			return false;
		}else{
			return true;
		}
	}
	
	public boolean updateDeviceNo(String userCode,String imsi,String imei)
	{
		String updateSQL = "update tb_c_employee set qq=?,id_card=? where employee_id=?";
		try
		{
			this.jdbcTemplate.update(updateSQL,imei,imsi,userCode);
			return true;
		}
		catch(Exception ex)
		{
			return false;
		}
	}
	
	/**
	 * 
	 * @param userCode
	 * @return
	 */
	public boolean checkForbidden(String userCode){
		String sql = "SELECT COUNT(*) " +
				"FROM tb_c_system_user su,tb_c_user_company uc " +
				"WHERE su.employee_id=uc.employee_id AND su.user_name=? AND uc.forbidden_flag<>1 AND uc.del_flag='0'";
		int num = this.jdbcTemplate.queryForInt(sql,userCode);
		if(num==0){
			return false;
		}else{
			return true;
		}
	}
}
