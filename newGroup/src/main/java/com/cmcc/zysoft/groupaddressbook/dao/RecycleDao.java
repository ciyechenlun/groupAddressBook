// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.cmcc.zysoft.sellmanager.model.Employee;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;
import com.starit.common.dao.jdbc.NamedParameterJdbcTemplateExt;
import com.starit.common.dao.support.Pagination;

/**
 * @author 张军
 * <br />邮箱：zhang.jun3@ustcinfo.com
 * <br />描述：RecycleDao
 * <br />版本:1.0.0
 * <br />日期： 2013-11-21 下午12:04:17
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Repository
public class RecycleDao extends HibernateBaseDaoImpl<Employee, String> {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private NamedParameterJdbcTemplateExt namedParameterJdbcTemplateExt;
	
    
	/**
	 * 查看回收站通讯录,可查看指定部门联系人(企业通讯录).
	 * @param rows 
	 * @param page 
	 * @param deptId 
	 * @param rights 
	 * @param departmentLevel 
	 * @param selfDepartmentId 
	 * @param companyId 
	 * @return 
	 * 返回类型：Pagination<?>
	 */
    public Pagination<?> getGroupInfo(int rows, int page, String deptId, String companyId){
    	Map<String, Object> map = new HashMap<String,Object>();

		String rowSql = "SELECT * FROM ((select uc.company_id,userDept.department_id,uc.user_company_id,'' as parent_department_id,uc.employee_name as obj,uc.department_name,'成员' as type,"+
						"MAX(gv.update_date) AS time_flg" +
		    			" FROM tb_c_employee emp,tb_c_user_company uc,tb_c_user_department userDept, " +
		    			" tb_c_group_version gv,tb_c_user_company_changed ucc "+
		    			" WHERE uc.employee_id=emp.employee_id " +
		    			" AND userDept.user_company_id=uc.user_company_id " +
		    			" and uc.user_company_id= ucc.user_company_id AND ucc.group_version_num=gv.group_version_num"+
		    			" AND userDept.visible_flag='0' AND uc.company_id='"+companyId+"' "+
		    			" group by uc.user_company_id) UNION ALL "+
		    			" (select d.company_id,d.department_id,'' as user_company_id ,d.parent_department_id,d.department_name as obj,"+
		    			" '' as department_name,'部门' as type,d.modify_time AS time_flg "+
		    			" from tb_c_department d where  d.company_id='"+companyId+"' and d.del_flag=1)) AS resultT ORDER BY resultT.time_flg DESC limit :offset, :limit";
    	String countSql = "select (count(*)+(select COUNT(*) " +
    			" from tb_c_department d  " +
    			" where  d.company_id='"+companyId+"' and d.del_flag=1 ) )as count" +
    			" FROM (select uc.user_company_id from tb_c_employee emp,tb_c_user_company uc,tb_c_user_department userDept" +
    			" WHERE uc.employee_id=emp.employee_id  AND userDept.user_company_id=uc.user_company_id "+
    			" AND userDept.visible_flag='0' AND uc.company_id='"+companyId+"' group by uc.user_company_id) AS eCount ";
    	int offset = (page - 1) * rows;
    	System.out.println(rowSql);
    	return namedParameterJdbcTemplateExt.queryPage(rowSql, countSql, offset, rows, map);
    }
    
    /**
     * 回收站非企业通讯录.
     * @param rows
     * @param page
     * @param companyId
     * @return 
     * 返回类型：Pagination<?>
     */
    public Pagination<?> getOrgGroupInfo(int rows, int page, String companyId){
    	Map<String, Object> map = new HashMap<String,Object>();
    	String rowSql = "SELECT " +
    			" uc.company_id,'' as department_id,uc.user_company_id,uc.employee_name as obj,uc.department_name,'成员' as type " +
    			"FROM tb_c_employee emp,tb_c_user_company uc " +
    			"WHERE emp.del_flag = '1' " +
    			"AND uc.del_flag='1' " +
    			"AND uc.employee_id = emp.employee_id " +
    			"AND uc.company_id = '"+companyId+"' ";
    	int offset = (page - 1) * rows;
    	rowSql += " limit :offset, :limit";
    	String countSql = "SELECT " +
    			"COUNT(uc.user_company_id) " +
    			"FROM tb_c_employee emp,tb_c_user_company uc " +
    			"WHERE emp.del_flag = '1' " +
    			"AND uc.del_flag='1' " +
    			"AND uc.employee_id = emp.employee_id " +
    			"AND uc.company_id = '"+companyId+"' ";
    	return this.namedParameterJdbcTemplateExt.queryPage(rowSql, countSql, offset, rows, map);
    }
    /**
     * 部门是否被删除
     * @param deptId
     * @return
     */
    public boolean isDepartmentDelete(String deptId){
    	String sql = "select count(*) from tb_c_department where del_flag='1' and department_id='"+deptId+"'";
    	int count = this.jdbcTemplate.queryForInt(sql);
    	if(count>0){//部门已删除
    		return true;
    	}
    	return false;
    }
    /**
     * 恢复部门
     * @param deptId
     * @return
     */
    public void recycleDepartment(String deptId){
    	String sql = "update tb_c_department set del_flag='0' where department_id=?";
    	this.jdbcTemplate.update(sql,deptId);
    }
    /**
     * 恢复用户
     * @param employeeId
     * @param companyId
     * @param departmentId
     */
    public void recycleEmployee(String userCompanyId,String departmentId){
    	String sql1 = "update tb_c_user_company set del_flag='0' where user_company_id='"+userCompanyId+"'";
    	this.jdbcTemplate.execute(sql1);
    	String sql = "update tb_c_employee set del_flag='0' where employee_id="
    			+"(select employee_id from tb_c_user_company where user_company_id='"+userCompanyId+"' and del_flag='0')";
    	this.jdbcTemplate.execute(sql);
    	String sql3 = "UPDATE tb_c_system_user SET del_flag = '0' WHERE employee_id = "
    			+"(select employee_id from tb_c_user_company where user_company_id='"+userCompanyId+"' and del_flag='0')";
		this.jdbcTemplate.execute(sql3);
    	String sql2 = "update tb_c_user_department set visible_flag='1' where "
    			+" user_company_id ='"+userCompanyId+"'"
    			+" and department_id='"+departmentId+"'";
    	this.jdbcTemplate.execute(sql2);
    }
    public void delRecycleDepartment(String deptId){
    	String sql ="";
    	sql ="select employee_id from tb_c_user_company uc,tb_c_user_department ud where uc.user_company_id=ud.user_company_id"+
    			" and ud.department_id=? and employee_id not in "+
    			"(select employee_id from tb_c_user_company where user_company_id != uc.user_company_id)";
    	List<Map<String,Object>> list= this.jdbcTemplate.queryForList(sql,deptId);//查找唯一的员工
		sql ="delete from tb_c_user_company_changed where user_company_id in "
				+"(select distinct user_company_id from tb_c_user_department where department_id=?) ";
		this.jdbcTemplate.update(sql,deptId);//删除用户公司关联版本控制表中公司为当前的数据
		
		sql ="delete from tb_c_user_modify where user_company_id in "
				+"(select distinct user_company_id from tb_c_user_department where department_id=?) ";
		this.jdbcTemplate.update(sql,deptId);//删除用户审核表中公司为当前的数据
		
		sql ="delete from tb_c_user_company where user_company_id in "
				+"(select distinct user_company_id from tb_c_user_department where department_id=?) ";
		
		this.jdbcTemplate.update(sql,deptId);//删除用户公司关联表中公司为当前的数据
		sql ="delete from tb_c_user_department where department_id =? ";
		this.jdbcTemplate.update(sql,deptId);//删除用户部门关联表中部门为当前的数据
		
		sql  = "delete from tb_c_department_changed where department_id =?";
		this.jdbcTemplate.update(sql,deptId);//删除部门版本控制表中部门为当前的数据
		
		sql = "delete from tb_c_department where department_id =?";
		this.jdbcTemplate.update(sql,deptId);//删除部门版本控制表中部门为当前的数据
		for (Map<String, Object> map : list) {
			sql = "delete from tb_c_employee_medal where employee_id= ?";//删除tb_c_employee_medal表
			this.jdbcTemplate.update(sql,map.get("employee_id"));
			sql = "delete from tb_c_employee_param where employee_id= ?";//删除tb_c_employee_param表
			this.jdbcTemplate.update(sql,map.get("employee_id"));
			sql = "delete from tb_c_employee_record where employee_id= ?";//删除tb_c_employee_record表
			this.jdbcTemplate.update(sql,map.get("employee_id"));
			sql = "delete from tb_c_employee_report where employee_id= ?";//删除tb_c_employee_report表
			this.jdbcTemplate.update(sql,map.get("employee_id"));
			sql = "delete from tb_c_token where employee_id= ?";//删除员工令牌表
			this.jdbcTemplate.update(sql,map.get("employee_id"));
			sql = "delete from tb_c_device where employee_id= ?";//删除员工设备令牌表
			this.jdbcTemplate.update(sql,map.get("employee_id"));
			sql = "delete from tb_c_employee where employee_id= ?";//删除员工表
			this.jdbcTemplate.update(sql,map.get("employee_id"));
			sql = "delete from tb_c_user_role where user_id in "+
			"(select user_id from tb_c_system_user where employee_id= ?)";//删除用户角色表
			this.jdbcTemplate.update(sql,map.get("employee_id"));
			sql = "delete from tb_c_system_user where employee_id= ?";//删除用户表
			this.jdbcTemplate.update(sql,map.get("employee_id"));
		}
    }
    /**
     * 彻底删除用户
     * @param employeeId
     * @param companyId
     * @param departmentId
     */
    public void delRecycleEmployee(String userCompanyId,String departmentId){
    	String sql ="";
    	sql ="delete from tb_c_user_company_changed where user_company_id=?";
    	this.jdbcTemplate.update(sql,userCompanyId);
    	sql ="delete from tb_c_user_modify where user_company_id =? ";
    	this.jdbcTemplate.update(sql,userCompanyId);
    	sql="delete from tb_c_user_company where user_company_id=?";
    	this.jdbcTemplate.update(sql,userCompanyId);
    	sql="delete from tb_c_user_department where user_company_id=? and department_id =?";
    	this.jdbcTemplate.update(sql,userCompanyId,departmentId);
    	sql = "delete from tb_c_employee  where employee_id="
    			+"(select employee_id from tb_c_user_company where user_company_id=?)";
    	this.jdbcTemplate.update(sql,userCompanyId);
    	sql = "delete from tb_c_system_user  where employee_id="
    			+"(select employee_id from tb_c_user_company where user_company_id=?)";
    	this.jdbcTemplate.update(sql,userCompanyId);
    }
    /**
     * 检查恢复的成员是否已经重新添加了
     * @param companyId
     * @param departmentId
     * @param mobile
     * @return
     */
    public int checkInfo(String companyId,String departmentId,String mobile){
    	String rowSql = "select COUNT(*) " +
    			"FROM tb_c_employee emp,tb_c_user_company uc,tb_c_user_department userDept " +
    			"WHERE uc.employee_id=emp.employee_id " +
    			" AND userDept.user_company_id=uc.user_company_id " +
    			"AND uc.del_flag='0' " +
    			"AND emp.del_flag='0' " +
    			"AND userDept.visible_flag='1' "+
    			"AND uc.company_id='"+companyId+
    			" 'AND userDept.department_id='"+departmentId+"'"+
    			"AND uc.mobile='"+mobile+"'";
    	return this.jdbcTemplate.queryForInt(rowSql);
    }
}



