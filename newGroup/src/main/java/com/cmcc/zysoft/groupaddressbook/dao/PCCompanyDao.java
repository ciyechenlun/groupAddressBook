// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.cmcc.zysoft.sellmanager.model.Company;
import com.cmcc.zysoft.sellmanager.model.Role;
import com.cmcc.zysoft.spring.security.model.User;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;
import com.starit.common.dao.jdbc.NamedParameterJdbcTemplateExt;
import com.starit.common.dao.support.Pagination;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：PCCompanyDao
 * <br />版本:1.0.0
 * <br />日期： 2013-5-21 上午10:50:58
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Repository
public class PCCompanyDao extends HibernateBaseDaoImpl<Company, String>{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private NamedParameterJdbcTemplateExt namedParameterJdbcTemplateExt;
	
	/**
	 * 登录人所在企业通讯录.
	 * @param employee_id
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public Pagination<?> orglist(int rows, int page, String employee_id,String key){
		String sql;
		String countSql;
		Map<String, Object> map = new HashMap<String,Object>();
		if (!StringUtils.hasText(employee_id) || IsAdmin(employee_id))
		{
			sql =   "SELECT comp.*,(SELECT COUNT(1) FROM tb_c_user_company uc "+
					"WHERE  uc.del_flag='0' "+
					"AND uc.company_id=comp.company_id) as pCount "+
					"FROM tb_c_company comp " +
					"WHERE comp.del_flag='0' " +
					"AND comp.vitrue_flag='0' " +
					"AND comp.org_flag='1' ";
			countSql = "SELECT count(*) " +
					"FROM tb_c_company comp " +
					"WHERE comp.del_flag='0' " +
					"AND comp.vitrue_flag='0' " +
					"AND comp.org_flag='1' ";
			if(null !=key && !key.equals("")){
		       	 String searchSql = " AND comp.company_name LIKE :key ";
		       	 map.put("key", "%"+key+"%");
		       	 sql += searchSql;
		       	 countSql += searchSql;
		        }
			int offset = (page - 1) * rows;
	    	sql += " limit :offset, :limit";
			return this.namedParameterJdbcTemplateExt.queryPage(sql, countSql, offset, rows, map);
		}
		else
		{
			String user_id = SecurityContextUtil.getUserId();
			//用户角色
			//3->ICT管理员 //4->地市管理员//5->企业管理员
			String roleId = getUserRoleId();
			sql = 	"SELECT comp.*,(SELECT COUNT(1) FROM tb_c_user_company uc "+
					"WHERE  uc.del_flag='0' "+
					"AND uc.company_id=comp.company_id) as pCount "+
					"FROM tb_c_company comp " +
					"WHERE comp.del_flag='0' " +
					"AND comp.vitrue_flag='0' " +
					"AND comp.org_flag='1' ";
			countSql = "SELECT count(*) " +
					"FROM tb_c_company comp " +
					"WHERE comp.del_flag='0' " +
					"AND comp.vitrue_flag='0' " +
					"AND comp.org_flag='1' ";
			if(roleId.equals("3"))
			{
				//ICT管理员->分管地市
				sql += " AND comp.city IN (SELECT manage_city_code FROM tb_c_manage_city WHERE user_id='"+user_id+"')";
				countSql += " AND comp.city IN (SELECT manage_city_code FROM tb_c_manage_city WHERE user_id='"+user_id+"')";
			}
			else if(roleId.equals("4"))
			{
				//地市管理员->所在地市
				sql += " AND comp.city IN (SELECT company_city FROM tb_c_employee WHERE employee_id='"+employee_id+"')";
				countSql += " AND comp.city IN (SELECT company_city FROM tb_c_employee WHERE employee_id='"+employee_id+"')";
			}
			else if(roleId.equals("5")|| roleId.equals("6") || !StringUtils.hasText(roleId))
			{
				//企业管理员，所在企业，且为管理员
				sql += "AND (comp.company_id IN " +
						"(SELECT uc.company_id FROM tb_c_user_company uc WHERE uc.employee_id='"+employee_id+"' AND uc.del_flag='0') " +
						"OR comp.create_man='"+employee_id+"') ";
				countSql += "AND (comp.company_id IN " +
						"(SELECT uc.company_id FROM tb_c_user_company uc WHERE uc.employee_id='"+employee_id+"' AND uc.del_flag='0') " +
						"OR comp.create_man='"+employee_id+"') ";
			}
			else
			{
				sql += " AND 1=2";
				countSql += " AND 1=2";
			}
			if(null !=key && !key.equals("")){
		       	 String searchSql = " AND comp.company_name LIKE :key ";
		       	 map.put("key", "%"+key+"%");
		       	 sql += searchSql;
		       	 countSql += searchSql;
		    }
			int offset = (page - 1) * rows;
			sql += " limit :offset, :limit";
			return this.namedParameterJdbcTemplateExt.queryPage(sql, countSql, offset, rows, map);
		}
	}
	/**
	 * 登录人所在企业通讯录.
	 * @param employee_id
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> orgAllList(String employee_id){
		String sql;
		Map<String, Object> map = new HashMap<String,Object>();
		if (!StringUtils.hasText(employee_id) || IsAdmin(employee_id))
		{
			sql =   "SELECT comp.company_id,comp.company_name "+
					"FROM tb_c_company comp " +
					"WHERE comp.del_flag='0' " +
					"AND comp.vitrue_flag='0' " +
					"AND comp.org_flag='1' ";
	
			return this.jdbcTemplate.queryForList(sql);
		}
		else
		{
			String user_id = SecurityContextUtil.getUserId();
			//用户角色
			//3->ICT管理员 //4->地市管理员//5->企业管理员
			String roleId = getUserRoleId();
			sql = 	"SELECT comp.company_id,comp.company_name "+
					"FROM tb_c_company comp " +
					"WHERE comp.del_flag='0' " +
					"AND comp.vitrue_flag='0' " +
					"AND comp.org_flag='1' ";
			
			if(roleId.equals("3"))
			{
				//ICT管理员->分管地市
				sql += " AND comp.city IN (SELECT manage_city_code FROM tb_c_manage_city WHERE user_id='"+user_id+"')";
			}
			else if(roleId.equals("4"))
			{
				//地市管理员->所在地市
				sql += " AND comp.city IN (SELECT company_city FROM tb_c_employee WHERE employee_id='"+employee_id+"')";
			}
			else if(roleId.equals("5")|| roleId.equals("6") || !StringUtils.hasText(roleId))
			{
				//企业管理员，所在企业，且为管理员
				sql += "AND (comp.company_id IN " +
						"(SELECT uc.company_id FROM tb_c_user_company uc WHERE uc.employee_id='"+employee_id+"' AND uc.del_flag='0') " +
						"OR comp.create_man='"+employee_id+"') ";
			}
			else
			{
				sql += " AND 1=2";
			}
			return jdbcTemplate.queryForList(sql);
		}
	}
	/**
	 * 获取用户角色编号
	 * @return
	 */
	private String getUserRoleId(){
		List<Role> list = SecurityContextUtil.getCurrentUser().getRoles();
		List<String> roleIds = new ArrayList<String>();
		if(list.size()>0)
		{
			for(Role role : list )
			{
				roleIds.add(role.getRoleId());
			}
			if(roleIds.contains("3")){
				return "3";
			}else if(roleIds.contains("4")){
				return "4";
			}else if(roleIds.contains("5")){
				return "5";
			}else if(roleIds.contains("6")){
				return "6";
			}else{
				return "";
			}
		}
		else
		{
			return "";
		}
	}
	
	/**
	 * 判断是否超级管理员
	 * @param employeeId
	 * @return
	 */
	private boolean IsAdmin(String employeeId)
	{
		if(employeeId.equals("f85103133e37c252013e3828c40808c1") ||
				employeeId.equals("f85103133e37c252013e3828c3de08b5") ||
				employeeId.equals("f85103133e37c252013e3828c3d408b2") ||
				employeeId.equals("f85103133e37c252013e3828c3f408bb") ||
				employeeId.equals("f85103133e37c252013e3828c3fe08be") ||
				employeeId.equals("f85103133e37c252013e3828c3e808b8") ||
				employeeId.equals("f85103133e37c252013e3828c3cb08af") ||
				employeeId.equals("f8510a2d3fecf975013fed0a0b8909da"))//李铁山
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * 登录人所在的非企业通讯录或自己创建的通讯录.
	 * @param employeeId
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public Pagination<?> list(int rows, int page,String employeeId,String key){
		Map<String, Object> map = new HashMap<String,Object>();
		String sql = "SELECT * " +
				"FROM tb_c_company comp " +
				"WHERE comp.del_flag='0' " +
				"AND comp.vitrue_flag='0' " +
				"AND comp.org_flag='0' " +
				"AND (comp.company_id IN " +
				"(SELECT uc.company_id FROM tb_c_user_company uc WHERE uc.employee_id='"+employeeId+"') " +
				"OR comp.create_man='"+employeeId+"') ";
		String countSql = "SELECT count(*) " +
				"FROM tb_c_company comp " +
				"WHERE comp.del_flag='0' " +
				"AND comp.vitrue_flag='0' " +
				"AND comp.org_flag='0' " +
				"AND (comp.company_id IN " +
				"(SELECT uc.company_id FROM tb_c_user_company uc WHERE uc.employee_id='"+employeeId+"') " +
				"OR comp.create_man='"+employeeId+"') ";
		if(null !=key && !key.equals("")){
       	 String searchSql = " AND comp.company_name LIKE :key ";
       	 map.put("key", "%"+key+"%");
       	 sql += searchSql;
       	 countSql += searchSql;
        }
		int offset = (page - 1) * rows;
    	sql += " limit :offset, :limit";
		return this.namedParameterJdbcTemplateExt.queryPage(sql, countSql, offset, rows, map);
	}
	
	/**
	 * 获取所有公司列表
	 * @return 
	 * 返回类型：List<Company>
	 */
	public List<Map<String, Object>> getAllCompany(){
		String sql = "SELECT " +
				"comp.company_name AS company_name," +
				"comp.company_id AS company_id " +
				"FROM tb_c_company comp WHERE company_id!='001' AND del_flag='0' AND org_flag='1' " ;
		return  this.jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * 根据用户权限返回公司 列表
	 * @return
	 */
	public List<Map<String,Object>> getAllCompanyByUser()
	{
		String sql = "SELECT " +
				"comp.company_name AS company_name," +
				"comp.company_id AS company_id " +
				"FROM tb_c_company comp WHERE company_id!='001' AND del_flag='0' AND org_flag='1' " ;
		User user = SecurityContextUtil.getCurrentUser();
		if(!"s_admin".equals(user.getUsername()))
		{
			sql += " AND company_id= '"+user.getCompanyId() + "'";
		}
		return this.jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * 获取消息类型列表
	 * @return
	 */
	public List<Map<String, Object>> getMassageType(){
		String sql = "SELECT * FROM tb_c_dict_data dat,tb_c_dict_type typ WHERE dat.type_id=typ.type_id AND typ.type_code='message_type'";
		return this.jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * 根据企业名称返回企业列表
	 * @param company_name
	 * @return
	 */
	public Company getCompanyByCompanyName(String company_name)
	{
		String sql = "SELECT * FROM tb_c_company WHERE company_name='"+company_name+"'";
		List<Company> list = this.jdbcTemplate.queryForList(sql, Company.class);
		if(list.size()>0){
			return (Company) list.get(0);
		}
		else
		{
			return null;
		}
	}
	/**
	 * 获取所有企业所有人数
	 * @return
	 */
	public Long getAllCompanyEmployee(String key){
		String sql="SELECT COUNT(*) FROM tb_c_employee emp,tb_c_user_company uc,"+
				"tb_c_user_department userDept,tb_c_company cc "+
				"WHERE uc.employee_id=emp.employee_id "+
				"AND userDept.user_company_id=uc.user_company_id "+
				"AND uc.company_id=cc.company_id "+
				"AND uc.del_flag='0' "+
				"AND emp.del_flag='0' "+
				"AND userDept.visible_flag='1' ";
		if(null !=key && !key.equals("")){
			sql+="AND cc.company_name LIKE ?";
			return this.jdbcTemplate.queryForLong(sql,"%"+key+"%");
		}
		return this.jdbcTemplate.queryForLong(sql);
	}
	public List<Map<String,Object>> getManageCompany(String userId){
		String sql="select uc.company_id,uc.manage_flag from tb_c_user_company uc,tb_c_system_user su " +
				"where su.employee_id=uc.employee_id and su.del_flag='0' and uc.del_flag='0' " +
				"and (uc.manage_flag='1' or uc.manage_flag ='3') and su.user_id=?";
		return this.jdbcTemplate.queryForList(sql,userId);
	} 
}
