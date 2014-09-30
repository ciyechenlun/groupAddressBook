// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.mobile.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.cmcc.zysoft.sellmanager.model.UserCompany;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：MUserCompanyDao
 * <br />版本:1.0.0
 * <br />日期： 2013-5-27 下午3:26:08
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Repository
public class MUserCompanyDao extends HibernateBaseDaoImpl<UserCompany, String>{
	
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 下载群组用户.若groupVersion不为空,则为更新数据.
	 * @param companyId
	 * @param groupVersion
	 * @return 
	 * 返回类型：Map<String,Object>
	 */
	public Map<String,Object> getGroupUsers(String companyId,String groupVersion){
		String rowSql = "SELECT " +
				"temp.group_version_num," +
				"temp.update_type as update_type," +
				"uc.user_company_id AS user_company_id," +
				"uc.employee_id AS employee_id," +
				"uc.employee_name AS employee_name," +
				"'' AS department_id," +
				"uc.department_name AS department_name," +
				"emp.headship_id AS headship_id," +
				"uc.headship_name AS headship_name," +
				"comp.company_id AS company_id," +
				"comp.company_name AS company_name," +
				"IFNULL(comp.company_address,'') AS company_address," +
				"IFNULL(uc.mobile,'') AS mobile," +
				"IFNULL(uc.mobile_short,'') AS mobile_short," +
				"IFNULL(uc.telephone2,'') AS tel," +
				"IFNULL(uc.tel_short,'') AS tel_short," +
				"IFNULL(uc.email,'') AS email," +
				"IFNULL(uc.telephone,'') AS home_telephone," +
				"IFNULL(uc.weibo,'') AS weibo," +
				"IFNULL(uc.weixin,'') AS weixin," +
				"IFNULL(uc.qq,'') AS qq," +
				"IFNULL(uc.school,'') AS school," +
				"IFNULL(uc.user_major,'') AS user_major," +
				"IFNULL(uc.user_grade,'') AS user_grade," +
				"IFNULL(uc.user_class,'') AS user_class," +
				"IFNULL(uc.student_id,'') AS student_id," +
				"IFNULL(uc.birthday,'') AS birthday," +
				"IFNULL(uc.native_place,'') AS native_place," +
				"IFNULL(uc.address,'') AS address," +
				"IFNULL(uc.home_address,'') AS home_address," +
				"IFNULL(uc.manage_flag,'0') AS manage_flag," +
				"uc.employee_firstword AS employee_firstword," +
				"uc.employee_fullword AS employee_fullword," +
				"IFNULL(uc.mood,'') AS mood,IFNULL(emp.picture,'') AS picture," +
				"IFNULL(uc.remark,'') AS remark,'' AS user_company,'' AS department_fax,'' AS parent_department_name,999 AS display_order,'1' AS visible_flag " +
				"FROM " +
				"tb_c_user_company uc," +
				"tb_c_company comp," +
				"tb_c_employee emp," +
				"tb_c_department dept," +
				"(SELECT  * FROM tb_c_user_company_changed ucc ORDER BY ucc.group_version_num DESC) AS temp " + 
				"WHERE uc.company_id = comp.company_id ";
				if(StringUtils.hasText(companyId)){
					String[] comps = companyId.split(",");
					String companyIds = "";
					for(int i=0;i<comps.length;i++){
						companyIds += "'"+comps[i]+"',";
					}
					companyIds += "''";
					rowSql += " AND uc.company_id IN ("+companyIds+") ";
				}
				rowSql += " AND emp.employee_id = uc.employee_id " +
				" AND emp.department_id = dept.department_id " +
				" AND temp.user_company_id = uc.user_company_id AND uc.del_flag='0' " ;
		if(StringUtils.hasText(groupVersion)){
			rowSql += " AND temp.group_version_num > '"+groupVersion+"' ";
		}
		rowSql += "GROUP BY uc.user_company_id ";
		rowSql += "ORDER BY temp.group_version_num DESC ";
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(rowSql);
		map.put("cmd", "getgroupusers");	
		map.put("code", "0");		
		map.put("total", list.size());
		map.put("value", list);
		return map;
	}

}
