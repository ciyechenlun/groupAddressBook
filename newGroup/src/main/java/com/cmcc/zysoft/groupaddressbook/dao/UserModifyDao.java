// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmcc.zysoft.sellmanager.model.UserModify;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;
import com.starit.common.dao.jdbc.NamedParameterJdbcTemplateExt;
import com.starit.common.dao.support.Pagination;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：UserModifyDao
 * <br />版本:1.0.0
 * <br />日期： 2013-5-29 上午11:46:46
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Repository
public class UserModifyDao extends HibernateBaseDaoImpl<UserModify, String>{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private NamedParameterJdbcTemplateExt namedParameterJdbcTemplateExt;
	
	/**
	 * 审核列表.
	 * @param rows
	 * @param page
	 * @param companyId
	 * @return 
	 * 返回类型：Pagination<?>
	 */
	public Pagination<?> getModifyInfo(int rows, int page, String companyId){
		Map<String, Object> map = new HashMap<String,Object>();
		String rowSql = "SELECT " +
				"um.user_modify_id ," +
				"um.employee_name AS employee_name2," +
				"um.user_company AS user_company2," +
				"um.department_name AS department_name2," +
				"um.headship_name AS headship_name2," +
				"um.mobile AS mobile2," +
				"um.home_telephone AS home_telephone2," +
				"um.weibo AS weibo2," +
				"um.weixin AS weixin2," +
				"um.school AS school2," +
				"um.user_major AS user_major2," +
				"um.user_grade AS user_grade2," +
				"um.user_class AS user_class2," +
				"um.student_id AS student_id2," +
				"um.native_place AS native_place2," +
				"um.address AS address2," +
				"um.home_address AS home_address2," +
				"um.remark AS remark2," +
				"um.mobile_short AS mobile_short2," +
				"um.tel_short AS tel_short2," +
				"um.telephone AS telephone2," +
				"um.email AS email2," +
				"um.qq AS qq2," +
				"um.mood AS mood2," +
				"uc.* " +
				"FROM tb_c_user_modify um,tb_c_user_company uc " +
				"WHERE uc.del_flag='0' " +
				"AND uc.user_company_id=um.user_company_id " +
				"AND um.is_audited='0' " +
				"AND uc.company_id='"+companyId+"' ";
		String countSql = "SELECT COUNT(*) " +
				"FROM tb_c_user_modify um,tb_c_user_company uc " +
				"WHERE uc.del_flag='0' " +
				"AND uc.user_company_id=um.user_company_id " +
				"AND um.is_audited='0' " +
				"AND uc.company_id='"+companyId+"' ";
		int offset = (page - 1) * rows;
    	rowSql += " limit :offset, :limit";
    	return namedParameterJdbcTemplateExt.queryPage(rowSql, countSql, offset, rows, map);
	}
	
	/**
	 * 审核详情.
	 * @param userModifyId
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> list(String userModifyId){
		String sql = "SELECT " +
				"um.user_modify_id ," +
				"um.employee_name AS employee_name2," +
				"um.user_company AS user_company2," +
				"um.department_name AS department_name2," +
				"um.headship_name AS headship_name2," +
				"um.mobile AS mobile2," +
				"um.home_telephone AS home_telephone2," +
				"um.weibo AS weibo2," +
				"um.weixin AS weixin2," +
				"um.school AS school2," +
				"um.user_major AS user_major2," +
				"um.user_grade AS user_grade2," +
				"um.user_class AS user_class2," +
				"um.student_id AS student_id2," +
				"um.native_place AS native_place2," +
				"um.address AS address2," +
				"um.home_address AS home_address2," +
				"um.remark AS remark2," +
				"um.mobile_short AS mobile_short2," +
				"um.tel_short AS tel_short2," +
				"um.telephone AS telephone22," +
				"um.email AS email2," +
				"um.qq AS qq2," +
				"um.mood AS mood2," +
				"uc.* " +
				"FROM tb_c_user_modify um,tb_c_user_company uc " +
				"WHERE uc.del_flag='0' " +
				"AND uc.user_company_id=um.user_company_id " +
				"AND um.is_audited='0' " +
				"AND um.user_modify_id='"+userModifyId+"' ";
		return this.jdbcTemplate.queryForList(sql);
	}

}
