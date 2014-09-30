package com.cmcc.zysoft.sysmanage.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.sellmanager.model.Role;
import com.cmcc.zysoft.sysmanage.dao.RoleDao;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;
import com.starit.common.dao.support.Pagination;

/**
 * RoleService.java
 * 
 * @author zhangweihua
 * @email zhang.weihua@ustcinfo.com
 * @date 2012-12-6 下午6:05:12
 * 
 */
@Service
public class RoleService extends BaseServiceImpl<Role, String> {

	@Resource
	private RoleDao roleDao;

	@Override
	public HibernateBaseDao<Role, String> getHibernateBaseDao() {
		return roleDao;
	}

	/**
	 * 获取所有角色信息
	 * 
	 * @return
	 */
	public Pagination<Object> getAllRoles(int page, int rows, String companyId) {
		return this.roleDao.getAllRoles(page, rows, companyId);
	}

	/**
	 * 根据查询条件获取角色信息
	 * 
	 * @param roleName
	 *            角色名称
	 * @param page
	 * @param rows
	 * @return
	 */
	public Pagination<Object> getRolesByCondition(String roleName, int page,
			int rows, String companyId) {
		return this.roleDao.getRolesByContion(roleName, page, rows, companyId);
	}

	/**
	 * 保存角色信息
	 * 
	 * @param role
	 *            角色对象
	 */
	public void saveRole(Role role) {
		role.setStatus("0");
		this.roleDao.saveRole(role);
	}
	
	/**
	 * 保存角色信息
	 * 
	 * @param role
	 *            角色对象
	 */
	public String addRole(Role role) {
		return this.roleDao.addRole(role);
	}

	/**
	 * 修改角色信息
	 * 
	 * @param role
	 *            角色对象
	 */
	public void updateRole(Role role) {
		role.setStatus("0");
		roleDao.updateRole(role);
	}

	/**
	 * 根据角色id获取角色信息
	 * 
	 * @param roleId
	 *            角色id
	 * @return
	 */
	public Role getRole(String roleId) {
		Role role = this.roleDao.getRole(roleId);
		return role;
	}

	/**
	 * 根据角色id删除角色信息
	 * 
	 * @param roleId
	 *            角色id
	 */
	public void deleteRole(String roleId) {
		this.roleDao.deleteRole(roleId);
	}

	/**
	 * 根据公司id获取角色下拉框
	 * 
	 * @param companyId
	 * @return
	 */
	public List<Map<String, Object>> roleCombo(String companyId) {
		return this.roleDao.roleCombo(companyId);
	}

	/**
	 * 根据角色类型下拉框
	 * 
	 * @param companyId
	 * @return
	 */
	public List<Map<String, Object>> roleTypeCombo() {
		return this.roleDao.roleTypeCombo();
	}
}
