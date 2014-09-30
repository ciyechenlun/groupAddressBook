package com.cmcc.zysoft.groupaddressbook.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmcc.zysoft.sellmanager.model.UserDepartment;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;

/**
 * @author 周瑜
 * <br />邮箱： zhouyusgs#ahmobile.com
 * <br />描述：UserDepartmentDao
 * <br />版本:1.0.0
 * <br />日期： 2013-6-24 下午18:47:36
 * <br />
 */

@Repository
public class UserDepartmentDao extends HibernateBaseDaoImpl<UserDepartment, String> {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 根据部门和岗位返回信息，用来确认当前用户的部门隶属关系是否已经存在。（允许同一部门不同岗位情况存在）
	 * @param headship_id：岗位ID
	 * @param department_id：部门ID
	 * @return
	 */
	public List<Map<String,Object>> getUserDeptList(String user_company_id,String headship_id,String department_id)
	{
		String sql = "SELECT user_department_id FROM" +
				" tb_c_user_department WHERE user_company_id=? AND department_id=? and visible_flag='1'";
		return this.jdbcTemplate.queryForList(sql, user_company_id,department_id);
	}
	
	/**
	 * 查看某个人的部门隶属关系是否存在
	 * @param user_company_id
	 * @param department_id
	 * @param headship_id
	 * @return
	 */
	public UserDepartment getUserDeptInfoByUserCompanyIdAndDeptIdAndHeadShipId(String user_company_id,String department_id,String headship_id)
	{
		String sql = "FROM UserDepartment WHERE userCompanyId=? AND headshipId=? AND departmentId=?";
		List<UserDepartment> list = this.getHibernateTemplate().find(sql, user_company_id,headship_id,department_id);
		return list.size()>0?list.get(0):null;
	}
	
	/**
	 * 根据user_company_id 删除用户部门隶属关系
	 * @param user_company_id
	 */
	public void deleteUserDepartment(String user_company_id,String department_id)
	{
		String sql = "update tb_c_user_department set visible_flag='0' WHERE user_company_id=? AND department_id=?";
		this.jdbcTemplate.update(sql,user_company_id,department_id);
	}
}
