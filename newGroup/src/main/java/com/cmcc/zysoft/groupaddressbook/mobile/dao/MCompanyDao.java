// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.mobile.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmcc.zysoft.sellmanager.model.Company;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：MCompanyDao
 * <br />版本:1.0.0
 * <br />日期： 2013-5-27 上午10:38:51
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Repository
public class MCompanyDao extends HibernateBaseDaoImpl<Company, String>{
	
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 获取群组列表.
	 * @param userCode
	 * @return 
	 * 返回类型：Map<String,Object>
	 */
	public Map<String,Object> getMyGroups(String userCode){
		String rowSql = "SELECT " +
				"comp.company_id AS group_id," +
				"comp.company_name AS group_name," +
				"comp.org_flag AS org_flag," +
				"IFNULL(comp.index_logo,'') AS index_log,IFNULL(index_pictrue,'') AS index_pictrue," +
				"temp.company_version AS company_version,temp.update_type " +
				"FROM tb_c_company comp, tb_c_user_company uc,tb_c_employee emp,tb_c_system_user us," +
				"(SELECT * FROM tb_c_company_changed cc ORDER BY cc.company_version DESC) AS temp " +
				"WHERE comp.company_id=uc.company_id " +
				"AND emp.employee_id=uc.employee_id " +
				"AND us.employee_id=emp.employee_id " +
				"AND us.user_name=? " +
				"AND temp.company_id=comp.company_id AND uc.del_flag='0' AND comp.del_flag='0' AND emp.del_flag='0' AND us.del_flag='0' " +
				"GROUP BY temp.company_id " +
				"union all select company_id AS group_id, "+
				"'' AS group_name,'' AS org_flag,'' AS index_log,'' AS index_pictrue, "+
				"company_version AS company_version ,update_type "+
				 "from tb_c_company_changed cc where cc.update_type='3' GROUP BY company_id "+
				"ORDER BY org_flag DESC,group_name ASC ";
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(rowSql,userCode);
		map.put("cmd", "mygroups");	
		map.put("code", "0");
		map.put("total", list.size());
		map.put("value", list);
		return map;
	}
	
	/**
	 * 更新我的群组.
	 * @param userCode
	 * @param versionNum
	 * @return 
	 * 返回类型：Map<String,Object>
	 */
	public Map<String,Object> updateMyGroups(String userCode, String versionNum){
		String rowSql = "SELECT " +
				"comp.company_id AS group_id," +
				"comp.company_name AS group_name," +
				"comp.org_flag AS org_flag," +
				"IFNULL(comp.index_logo,'') AS index_log,IFNULL(index_pictrue,'') AS index_pictrue," +
				"temp.company_version AS company_version," +
				"temp.update_type AS update_type " +
				"FROM tb_c_company comp, tb_c_user_company uc,tb_c_employee emp,tb_c_system_user us," +
				"(SELECT * FROM tb_c_company_changed cc ORDER BY cc.company_version DESC) AS temp " +
				"WHERE comp.company_id=uc.company_id " +
				"AND emp.employee_id=uc.employee_id " +
				"AND us.employee_id=emp.employee_id AND uc.del_flag='0' AND comp.del_flag='0' " +
				"AND us.user_name=? " +
				"AND temp.company_id=comp.company_id " +
				"AND temp.company_version > '"+versionNum+"' " +
				"GROUP BY temp.company_id ORDER " +
				"BY temp.company_version DESC ";
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(rowSql,userCode);
		map.put("cmd", "getgroupchanges");	
		map.put("code", "0");		
		map.put("total", list.size());
		map.put("value", list);
		return map;
	}

}
