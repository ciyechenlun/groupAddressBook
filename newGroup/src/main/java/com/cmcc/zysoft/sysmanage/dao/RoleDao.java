package com.cmcc.zysoft.sysmanage.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmcc.zysoft.sellmanager.model.Role;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;
import com.starit.common.dao.support.Pagination;

/**
 * RoleDao.java
 * @author zhangweihua
 * @email zhang.weihua@ustcinfo.com
 * @date 2012-12-2 下午6:06:53
 *
 */
@Repository
public class RoleDao extends HibernateBaseDaoImpl<Role, String> {
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 获取所有角色信息
	 * 
	 * @return
	 */
	public Pagination<Object> getAllRoles(int page, int rows,String companyId) {
		String rowSql = "select new Map(r.roleId as roleId,r.company.companyName as companyName ,r.roleCode as roleCode," +
				" r.description as description,r.status as status,r.displayOrder as displayOrder," +
				"r.roleName as roleName,dat.dataContent as dataContent) from Role r ,DictType typ ,DictData dat where typ.typeId=dat.dictType.typeId and typ.typeCode='role' and dat.dataCode=r.roleCode ";
		String countSql = "select count(*) from Role r ,DictType typ ,DictData dat where typ.typeId=dat.dictType.typeId and typ.typeCode='role' and dat.dataCode=r.roleCode ";
		if("0".equals(companyId)){
			rowSql += " order by r.roleCode ";
			countSql += " order by r.roleCode ";
		}else{
			rowSql += " and r.company.companyId = '" + companyId + "' order by r.roleCode ";
			countSql += " and r.company.companyId = '" + companyId + "' order by r.roleCode ";
		}
		
		int offset = (page-1)*rows;
		return this.findPageByHQL(rowSql, countSql, offset, rows);
	}
	
	/**
	 * 根据查询条件，获取角色信息列表
	 * 
	 * @param roleName 角色名称
	 * @param page
	 * @param rows
	 * @return
	 */
	public Pagination<Object> getRolesByContion(String roleName, int page, int rows,String companyId) {
		String rowSql = "select new Map(r.roleId as roleId,r.company.companyName as companyName ,r.roleCode as roleCode," +
				" r.description as description,r.status as status,r.displayOrder as displayOrder," +
				"r.roleName as roleName) from Role r ";
		String countSql = "select count(*) from Role r ";
		if("0".equals(companyId)){
			if(roleName.trim().length()==0){
				rowSql += " order by r.roleId desc ";
				countSql += " order by r.roleId desc ";
			}else{
				rowSql += " where r.roleName like '%"+ roleName.trim() +"%' order by r.roleId desc ";
				countSql += " where r.roleName like '%"+ roleName.trim() +"%' order by r.roleId desc ";
			}
		}else{
			if(roleName.trim().length()==0){
				rowSql += " order by r.roleId desc ";
				countSql += " order by r.roleId desc ";
			}else{
				rowSql += " where c.companyId = '" + companyId + "' order by r.roleId desc ";
				countSql += " where c.companyId = '" + companyId + "' order by r.roleId desc ";
			}
		}
		
		int offset = (page-1)*rows;
		return this.findPageByHQL(rowSql, countSql, offset, rows);
	}

	/**
	 * 添加角色信息
	 * 
	 * @param role 角色对象
	 */
	public void saveRole(Role role) {
		this.getHibernateTemplate().merge(role);
	}
	
	/**
	 * 保存角色信息
	 * 
	 * @param role
	 *            角色对象
	 */
	@Transactional
	public String addRole(Role role) {
		return this.save(role);
	}

	/**
	 * 修改角色信息
	 * 
	 * @param role 角色对象
	 */
	public void updateRole(Role role) {
		this.getHibernateTemplate().merge(role);
	}

	/**
	 * 根据角色id获取角色信息
	 * 
	 * @param roleId 角色id
	 * @return
	 */
	public Role getRole(String roleId) {
		Role role = this.get(roleId);
		return role;
	}

	/**
	 * 删除角色信息
	 * 
	 * @param roleId
	 */
	public void deleteRole(String roleId) {
		String sql = " delete from tb_c_role where role_id = ?'";
		jdbcTemplate.update(sql,roleId);
	}
	
	/**
	 * 根据公司id获取角色下拉框
	 * @param companyId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> roleCombo(String companyId) {
		String hql = "select new Map(role.roleId as id,role.roleName as text) from Role role where role.company.companyId = ?";
		return this.findByHQL(hql,companyId);
	}
	
	/**
	 * 根据角色类型下拉框
	 * @param companyId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> roleTypeCombo() {
		String hql = " select new Map(d.dataCode as id,d.dataContent as text) " +
				" from DictType t,DictData d" +
				" where t.typeCode='role' " +
				" and t.typeId = d.dictType.typeId and d.dataCode <> '0'";
		return this.findByHQL(hql);
	}
}
