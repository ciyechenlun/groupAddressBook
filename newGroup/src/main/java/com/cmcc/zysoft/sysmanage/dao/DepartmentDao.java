// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sysmanage.dao;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.cmcc.zysoft.sellmanager.model.Department;
import com.cmcc.zysoft.spring.security.model.User;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;

/**
 * DepartmentDao.java
 * @author 李三来
 * @mail li.sanlai@ustcinfo.com
 * @date 2012-11-29 下午8:16:23
 */
@Repository
public class DepartmentDao extends HibernateBaseDaoImpl<Department, String> {
	
	private Logger _logger = LoggerFactory.getLogger(DepartmentDao.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	/**
	 * 添加部门(未使用)
	 * @param department
	 * @return 
	 */
	public String addDepartment(Department department) {
		String id = (String) this.getHibernateTemplate().save(department);
		return id;
	}
	
	/**
	 * 根据特定组织机构的ID获取其子级组织结构树(未删除的组织机构)
	 * @param pid
	 * @return
	 */
	public List<?> departmentTree(String pid){
		_logger.debug("#根据特定组织机构的ID获取其子级组织结构树");
		List<?> list = null;
		String hql = "select new Map(d.departmentId as departmentId," +
				" d.parentDepartmentId as parentDepartmentId," +
				" d.departmentName as departmentName," +
				" d.departmentMaster as departmentMaster," +
				" d.telephone as telephone," +
				" d.createTime as createTime," +
				" d.createMan as createMan," +
				" d.modifyTime as modifyTime," +
				" d.modifyMan as modifyMan," +
				" d.delFlag as delFlag," +
				" d.displayOrder as displayOrder," +
				" d.departmentId as id," +
				" d.departmentName as text," +
				" d.parentDepartmentId as _parentId," +
				" (case d.haveChildDeparment when 'N' then 'open' when 'Y' then 'closed' else 'open' end) as state," +
				" (case d.haveChildDeparment when 'N' then true when 'Y' then false else true end) as isLeaf," +
				" 'folder' as iconCls)" +
				" from Department as d " +
				" where d.delFlag='0'";
		hql += (pid == null || "0".equals(pid)) ? " and (d.parentDepartmentId = '0' or d.parentDepartmentId is null)"
				: " and d.parentDepartmentId = '" + pid + "'";
		hql += " order by d.displayOrder asc";
		_logger.debug(hql);
		list = this.getHibernateTemplate().find(hql);
		return list;
	}
	
	/**
	 * 查找特定公司id下的所有部门 (未使用)
	 * @param companyId
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<Department> getDepartmentsByComId(String companyId) {
		String hql = "from Department d where d.company.companyId = ? order by displayOrder";
		List<Department> departmentsList = this.getHibernateTemplate().find(
				hql, companyId);
		return departmentsList;
	}
	/**
	 * 查询特定部门id下的所有子部门 (未使用)
	 * @param pid
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public List<Department> getDepartmentsByPid(String pid) {
		String hql = "from Department d where d.parentDepartmentId = ? order by displayOrder";
		List<Department> departmentsList = this.getHibernateTemplate().find(
				hql, pid);
		return departmentsList;
	}

	/**
	 * 查询所有的部门
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Department> getAllDepartments(){
		String hql = "from Department as d order by d.company.companyId,d.displayOrder";
		List<Department> departments = this.getHibernateTemplate().find(hql);
		return departments;
	}
	
	/**
	 * 根据公司id,和父部门id获取下面的部门
	 * @param parentDepartmentId
	 * @param companyId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> departmentTree(String parentDepartmentId,String companyId) {
		String hql = "select new Map(dept.departmentId as id,dept.departmentName as text,dept.haveChildDeparment as haveChildDept)" +
				" from Department dept where dept.delFlag='0' and dept.parentDepartmentId = ? and dept.company.companyId = ? ";
		return this.findByHQL(hql,parentDepartmentId,companyId);
	}

	
	/**
	 * combortree 获取部门树
	 * @param parentDepartmentId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> deptTree(String parentDepartmentId,String companyId){
		String hql = "select new Map(dept.departmentId as id,dept.departmentName as text,dept.departmentType as attributes,dept.haveChildDeparment as haveChildDept) from Department dept where dept.delFlag='0' and dept.company.companyId='"+companyId+"' and dept.parentDepartmentId = ?";
		return this.findByHQL(hql,StringUtils.hasText(parentDepartmentId)?parentDepartmentId:"0");
	}
	
	/**
	 * 根据department_id,parent_department_id查找属于同一父部门下其他子部门的数量
	 * @param departmentId
	 * @param exceptId
	 */
	public int getSubDeptCount(String departmentId, String exceptId){
		String sql = "select count(*) from tb_c_department where parent_department_id = ? and department_id <> ? and del_flag <> '1'";
		return this.jdbcTemplate.queryForInt(sql,departmentId,exceptId);
	}
	
	
	/**
	 * 查询部门
	 * @param deptName
	 * @return
	 */
	public List<Map<String, Object>> searchDept(String deptName){
		String companyId = SecurityContextUtil.getCompanyId();
		String sql = "SELECT * FROM tb_c_department dept WHERE dept.del_flag='0' AND dept.company_id = '"+companyId+"' AND dept.department_name like '%"+deptName.trim()+"%' ";
		return this.jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * 通过部门Id查找对应的部门
	 * @param deptId
	 * @return
	 */
	public List<Map<String, Object>> searchDeptByDeptId(String deptId){
		String sql = "SELECT * FROM tb_c_department dept WHERE dept.del_flag='0' AND dept.department_id =? ";
		return this.jdbcTemplate.queryForList(sql,deptId);
	}
	
	/**
	 * 获取部门树   部门管理    列表展示
	 * @param parentDepartmentId
	 * @return
	 */
	public List<Map<String, Object>> tree(String parentDepartmentId){
		String companyId = SecurityContextUtil.getCompanyId();
		User user = SecurityContextUtil.getCurrentUser();
		String userCode = user.getRoles().get(0).getRoleCode();
		String sql = "SELECT dept.*,comp.company_name as companyName,dat.data_content as departmentType," +
				"(case dept.have_child_deparment when 'N' then 'open' when 'Y' then 'closed' else 'open' end) as state," +
				"(case dept.have_child_deparment when 'N' then true when 'Y' then false else true end) as isLeaf," +
				"'folder' as iconCls ";
		String empNumSql = "SELECT COUNT(*) FROM tb_c_employee emp WHERE emp.del_flag='0' AND emp.department_id=dept.department_id";//部门员工数
		String storeNumSql = "SELECT COUNT(*) FROM tb_b_store store WHERE store.del_flag='0' AND store.department_id=dept.department_id";//部门门店数
		String clientNumSql = "SELECT COUNT(*) FROM tb_b_client ent WHERE ent.del_flag='0' AND ent.department_id=dept.department_id";//部门客户数
		sql +=", (" + empNumSql +") AS empNum";
		sql +=", (" + storeNumSql +") AS storeNum";
		sql +=", (" + clientNumSql +") AS clientNum ";
		sql += " FROM tb_c_department dept,tb_c_company comp,tb_c_dict_data dat,tb_c_dict_type typ ";
		sql += " WHERE dept.del_flag='0'  AND dept.company_id=comp.company_id AND " +
				"dept.department_type=dat.data_code AND dat.type_id=typ.type_id AND typ.type_code='department_type' " +
				"and dept.parent_department_id=?";
		if(!"0".equals(userCode)){
			sql +=" and dept.company_id='"+companyId+"' ";
		}
		return this.jdbcTemplate.queryForList(sql,parentDepartmentId);
	}
	
	/**
	 * 获取部门类型列表
	 * @return
	 */
	public List<Map<String, Object>> departmentType(){
		String sql = "SELECT * FROM tb_c_dict_data dat,tb_c_dict_type typ WHERE dat.type_id=typ.type_id AND typ.type_code='department_type'";
		return this.jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * 获取部门区域列表
	 * @return
	 */
	public List<Map<String, Object>> departmentArea(){
		String sql = "SELECT * FROM tb_c_dict_data dat,tb_c_dict_type typ WHERE dat.type_id=typ.type_id AND typ.type_code='department_area'";
		return this.jdbcTemplate.queryForList(sql);
	}
	
	
	/**
	 * 找出指定部门下所有子部门及子部门的子部门..
	 * @param deptId
	 * @return
	 */
	public String allChildrenDept(String deptId,String deptIds){
		String sql = "SELECT * FROM tb_c_department dept WHERE dept.del_flag='0' AND dept.parent_department_id =? ";
		List<Map<String, Object>> list= this.jdbcTemplate.queryForList(sql,deptId);
		if(list.size()>0){
			for(Map<String, Object> map:list){
				deptIds += ","+"'"+map.get("department_id").toString()+"'";
				if(map.get("have_child_deparment").toString().equals("Y")){
					deptIds = this.allChildrenDept(map.get("department_id").toString(),deptIds);
				}
			}
		}
		return deptIds;
	}
	
	/**
	 * 找出指定部门下所有子部门及子部门的子部门..
	 * @param deptId
	 * @return
	 */
	public String allChildrenDept(String deptId){
		String deptId_new = "'"+deptId+"'";
		return this.allChildrenDept(deptId, deptId_new);
	}
	
	/**
	 * 找到指定部门的最上级部门
	 * @param deptId
	 * @return
	 */
	public String grandFather(String deptId){
		if(deptId.equals("0")){
			return deptId;
		}else{
			String sql = "SELECT * FROM tb_c_department dept WHERE dept.del_flag='0' AND dept.department_id =? ";
			List<Map<String, Object>> list= this.jdbcTemplate.queryForList(sql,deptId);
			if(list.size()>0){
				for(Map<String, Object> map:list){
					String id = map.get("parent_department_id").toString();
					if(id.equals("0")){
						return map.get("department_id").toString();
					}else{
						deptId = this.grandFather(id);
					}
				}
			}
			return deptId;
		}
	}
	
	
	/**
	 * 员工管理 左侧部门树
	 * 获取父部门;如果为超级管理员或企业管理员登陆，则获取的是所有最上级的部门;否则则获取当前部门
	 * @author li.menghua
	 * @date 2012-12-22 下午3:20:16
	 * @param parentDepartmentId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> deptTreeForCompany(String parentDepartmentId,String companyId){
		String hql = "select new Map(dept.departmentId as id," +
				"dept.departmentName as text," +
				"dept.haveChildDeparment as isLeaf," +
				"dept.haveChildDeparment as haveChildDeparment," +
				"dept.departmentType as attributes," +
				"dept.departmentType as deptType," +
				"(case dept.departmentName when 'Y' then 'closed' when 'N' then 'open' else 'open' end) as state)" +
				" from Department dept where dept.delFlag='0' and dept.company.companyId=? ";
		if(parentDepartmentId.equals("0")){
			hql += " and dept.parentDepartmentId = ?";
		}else{
			hql += " and dept.departmentId = ?";
		}
		return this.findByHQL(hql,companyId ,parentDepartmentId);
	}
	
	/**
	 * 获取指定部门的子部门
	 * @param parentDepartmentId
	 * @param companyId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> deptChildTreeForCompany(String parentDepartmentId,String companyId){
		String hql = "select new Map(dept.departmentId as id," +
				"dept.departmentName as text," +
				"dept.haveChildDeparment as isLeaf," +
				"dept.haveChildDeparment as haveChildDeparment," +
				"dept.departmentType as attributes," +
				"dept.departmentType as deptType," +
				"(case dept.departmentName when 'Y' then 'closed' when 'N' then 'open' else 'open' end) as state)" +
				" from Department dept where dept.delFlag='0' and dept.company.companyId=? and dept.parentDepartmentId = ? ";
		return this.findByHQL(hql,companyId ,parentDepartmentId);
	}
	/**
	 * 根据企业id获取所有部门
	 * @return
	 */
	public List<Map<String, Object>> getDepartbyCompany(String companyId){
		String sql = "SELECT * FROM tb_c_department where del_flag ='0' and company_id = '"+companyId+"'";
		return this.jdbcTemplate.queryForList(sql);
	}
}
