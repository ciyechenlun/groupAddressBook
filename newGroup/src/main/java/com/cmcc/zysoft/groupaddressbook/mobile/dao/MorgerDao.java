/**
 * @author 徐刚强
 */
package com.cmcc.zysoft.groupaddressbook.mobile.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.cmcc.zysoft.sellmanager.model.Employee;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;

/**
 * @author gavin
 *
 */
@Repository
public class MorgerDao extends HibernateBaseDaoImpl<Employee, String>{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 下载联系人信息.
	 * @param departmentIdStr 
	 * @return 
	 * 返回类型：Map<String,Object>
	 */
	public Map<String,Object> getAllorgerList(String cmd, String userCode, int deptCode, String deptList, String token, String company_id){
		if(!StringUtils.hasText(company_id)){
			String compSQL = "SELECT " +
					"comp.company_id AS company_id," +
					"comp.company_name AS group_name," +
					"comp.org_flag AS org_flag," +
					"IFNULL(comp.index_logo,'') AS index_log,IFNULL(index_pictrue,'') AS index_pictrue," +
					"temp.company_version AS company_version " +
					"FROM tb_c_company comp, tb_c_user_company uc,tb_c_employee emp,tb_c_system_user us," +
					"(SELECT * FROM tb_c_company_changed cc ORDER BY cc.company_version DESC) AS temp " +
					"WHERE comp.company_id=uc.company_id " +
					"AND emp.employee_id=uc.employee_id " +
					"AND us.employee_id=emp.employee_id " +
					"AND us.user_id=? " +
					"AND temp.company_id=comp.company_id AND uc.del_flag='0' AND comp.del_flag='0' AND emp.del_flag='0' AND us.del_flag='0' " +
					"GROUP BY temp.company_id ORDER " +
					"BY comp.org_flag DESC,comp.company_name ASC limit 0,1 ";
			
			List<Map<String,Object>> compList = this.jdbcTemplate.queryForList(compSQL, userCode);
			if(!compList.isEmpty())
			{
				company_id = compList.get(0).get("company_id").toString();
			}
		}
		String rowSql = "Select " +
				"department_id, department_name, parent_department_id,department_firstword, " +
				"department_level, modify_time, have_child_deparment,company_id,display_order " +
				"from tb_c_department " +
				"where del_flag = 0 AND department_id!='001' AND ";
		if (StringUtils.hasText(company_id))
		{
			rowSql += "company_id='"+company_id+"'";
		}
		else{
		rowSql += "company_id IN (SELECT company_id FROM tb_c_user_company WHERE employee_id=(" +
				"SELECT employee_id FROM tb_c_system_user WHERE user_id='"+userCode+"'))";
		}
		rowSql += "  ORDER BY display_order ASC";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(rowSql);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("value", list);
		map.put("total", list.size());
		return map;	
	}
	
	/**
	 * 部门更新接口
	 * @param cmd 操作符，无具体含义
	 * @param userCode 用户编号（登录表的userId)
	 * @param deptCode 部门的版本号，服务器端根据此版本号返回更新的部门
	 * @param deptList 废除字段
	 * @param token 
	 * @param filter_company：新增字段，过滤不需要下载部门的企业
	 * @return
	 */
	public Map<String,Object> updateorgerList(String cmd, String userCode, int deptCode, String deptList, String token,String filter_company){
		String rowSql = "select  * from ( " +
				"select a.department_id, a.department_name, a.parent_department_id,a.department_firstword, " +
				"a.department_level, a.modify_time, a.have_child_deparment,a.display_order, " +
				"b.update_type, b.department_version_num,a.company_id "  +
				"from tb_c_department a, tb_c_department_changed b " +
				"where a.company_id in (select company_id from tb_c_user_company where employee_id=(select employee_id from tb_c_system_user where user_id=?)) " +
				" and a.department_id = b.department_id and b.department_version_num > ";
		rowSql += deptCode;
		if(StringUtils.hasText(filter_company))
		{
			String companyFilter = getSpliteCompanyIds(filter_company);
			if(StringUtils.hasText(companyFilter))
			{
				rowSql += " AND a.company_id NOT IN ("+companyFilter+")";
			}
		}
		rowSql += " order by b.department_version_num DESC) as tp group by department_id ";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(rowSql,userCode);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("value", list);
		map.put("total", list.size());
		return map;	
	}
	
	public int getdeptversion(){
		String rowSql = "select max(department_version_num) department_version_num from tb_c_department_version";
		int versionnum = this.jdbcTemplate.queryForObject(rowSql, Integer.class);
		return versionnum;
		
	}
	
	/**
	 * 将以逗号分隔的企业ID转成供sql语句调用的字符串
	 * @param filter_company
	 * @return
	 */
	private String getSpliteCompanyIds(String filter_company)
	{
		String retValue = "";
		// 过滤所有客户端为空的企业，拼成'company_id','company_id'直接供sql语句使用
		if(StringUtils.hasText(filter_company))
		{
			String[] companys = filter_company.split("[,]");
			for(String comp : companys)
			{
				if(StringUtils.hasText(comp))
				{
					retValue += "'" + comp + "',";
				}
			}
			if(StringUtils.hasText(retValue))
			{
				retValue = retValue.substring(0, retValue.length()-1);
			}
		}
		return retValue;
	}
	

}
