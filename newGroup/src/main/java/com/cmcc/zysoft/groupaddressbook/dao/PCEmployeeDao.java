// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmcc.zysoft.sellmanager.model.Employee;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：PCEmployeeDao
 * <br />版本:1.0.0
 * <br />日期： 2013-5-21 上午11:16:00
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Repository
public class PCEmployeeDao extends HibernateBaseDaoImpl<Employee, String>{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	/**
	 * 判断一个手机号码是否已经是用户.
	 * @param mobile
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> isUser(String mobile){
		String sql = "SELECT emp.employee_id " +
				"FROM tb_c_employee emp, tb_c_system_user us " +
				"WHERE emp.mobile='"+mobile+"' " +
				"AND emp.del_flag='0' " +
				"AND us.employee_id=emp.employee_id";
		return this.jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * 判断一个手机号码是否已经是用户.包括删除的用户
	 * @param mobile
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> isUserAll(String mobile){
		String sql = "SELECT emp.employee_id " +
				"FROM tb_c_employee emp, tb_c_system_user us " +
				"WHERE emp.mobile='"+mobile+"' " +
				"AND us.employee_id=emp.employee_id";
		return this.jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * 恢复禁用的用户
	 * @param employee_id
	 */
	public void enableUser(String employee_id)
	{
		String sql = "UPDATE tb_c_system_user SET del_flag='0' WHERE employee_id=?";
		this.jdbcTemplate.update(sql,employee_id);
		sql = "UPDATE tb_c_employee SET del_flag='0' WHERE employee_id=?";
		this.jdbcTemplate.update(sql,employee_id);
		//sql = "UPDATE tb_c_user_company SET del_flag='0' WHERE employee_id='"+employee_id+"'";
		//this.jdbcTemplate.update(sql,employee_id);
	}

	/**
	 * 根据公司ID查找该公司的所有员工.
	 * @param companyId
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> getEmpsList(String companyId) {
		String sql = 
				" SELECT emp.employee_id, emp.department_id, emp.mobile_short, emp.tel_short, emp.telephone2, emp.headship_id, " +
				" emp.employee_code, emp.employee_name, emp.employee_firstword, emp.employee_fullword, emp.parent_department_name, " +
				" IFNULL((SELECT dev.imsi FROM tb_c_device dev WHERE dev.employee_id = emp.employee_id LIMIT 1), '') AS id_card, " +
				" emp.resume AS resume, emp.age AS age, emp.sex AS sex, emp.politics_status AS politics_status, emp.native_place AS native_place, " +
				" emp.school AS school, emp.degree AS degree, emp.graduation_date AS graduation_date, emp.mobile AS mobile, " +
				" emp.backup_mobile AS backup_mobile, emp.telephone AS telephone, emp.email AS email, emp.qq AS qq, " +
				" emp.birthday AS birthday, emp.picture AS picture, emp.higher_up AS higher_up, emp.is_dept_leader AS is_dept_leader, " +
				" emp.join_date AS join_date, emp.leave_date AS leave_date, emp.leave_reason AS leave_reason,emp.comment AS comment, " +
				" emp.status AS STATUS, emp.display_order AS display_order, emp.grid_number AS grid_number, emp.del_flag AS del_flag " +
				" FROM tb_c_employee emp " +
				" WHERE emp.department_id IN ( " +
				" 		SELECT dept.department_id " +
				"		FROM tb_c_department dept " +
				"		WHERE dept.company_id = ? " +
				" ) AND emp.del_flag = '0' AND emp.employee_id IN (SELECT employee_id FROM tb_c_device)";
		return this.jdbcTemplate.queryForList(sql, companyId);
	}
	
	/**
	 * 查找ID集合empIds中的所有员工.
	 * @param empIds
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> getEmpByIds(String empIds) {
		String sql = 
				" SELECT emp.employee_id, emp.department_id, emp.mobile_short, emp.tel_short, emp.telephone2, emp.headship_id, " +
				" emp.employee_code, emp.employee_name, emp.employee_firstword, emp.employee_fullword, emp.parent_department_name, " +
				" IFNULL((SELECT dev.imsi FROM tb_c_device dev WHERE dev.employee_id = emp.employee_id LIMIT 1), '') AS id_card, " +
				" emp.resume AS resume, emp.age AS age, emp.sex AS sex, emp.politics_status AS politics_status, emp.native_place AS native_place, " +
				" emp.school AS school, emp.degree AS degree, emp.graduation_date AS graduation_date, emp.mobile AS mobile, " +
				" emp.backup_mobile AS backup_mobile, emp.telephone AS telephone, emp.email AS email, emp.qq AS qq, " +
				" emp.birthday AS birthday, emp.picture AS picture, emp.higher_up AS higher_up, emp.is_dept_leader AS is_dept_leader, " +
				" emp.join_date AS join_date, emp.leave_date AS leave_date, emp.leave_reason AS leave_reason,emp.comment AS comment, " +
				" emp.status AS STATUS, emp.display_order AS display_order, emp.grid_number AS grid_number, emp.del_flag AS del_flag " +
				" FROM tb_c_employee emp " +
				" WHERE emp.del_flag = '0' AND emp.employee_id in ( " + empIds + " ) ";
		return this.jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * 获取用户所属分公司（顶级部门）
	 * @param employee_id
	 * @return
	 */
	public String getUserParentDepartment(String employee_id)
	{
		String department_id = "";
		String sql = "SELECT department_id FROM tb_c_employee WHERE " +
				"employee_id=?";
		List<Map<String,Object>> list = this.jdbcTemplate.queryForList(sql,employee_id);
		department_id = this.getTopDepartmentId(list.get(0).get("department_id").toString());
		return department_id;
	}
	
	private String getTopDepartmentId(String department_id)
	{
		String sql = "SELECT department_id,parent_department_id FROM tb_c_department WHERE " +
				"department_id=?";
		List<Map<String,Object>> list = this.jdbcTemplate.queryForList(sql,department_id);
		if(list!=null && list.size()>0)
		{
			String parent_dpeartment_id = list.get(0).get("parent_department_id").toString();
			if("0".equals(parent_dpeartment_id))
			{
				return department_id;
			}
			else
			{
				department_id = getTopDepartmentId(parent_dpeartment_id);
			}
		}
		return department_id;
	}
}
