// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sysmanage.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.sellmanager.model.Role;
import com.cmcc.zysoft.sellmanager.model.SystemUser;
import com.cmcc.zysoft.sellmanager.model.UserRole;
import com.cmcc.zysoft.sysmanage.dao.UserRoleDao;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author yandou
 */
@Service
public class UserRoleService extends BaseServiceImpl<UserRole, String> {

	@Resource
	private UserRoleDao userRoleDao;
	
	@Override
	public HibernateBaseDao<UserRole, String> getHibernateBaseDao() {
		return userRoleDao;
	}
	
	public UserRole save(Role role,SystemUser systemUser){
		UserRole userRole = new UserRole();
		userRole.setRole(role);
		userRole.setSystemUser(systemUser);
		this.getHibernateBaseDao().save(userRole);
		return userRole;
	}
	
	/**
	 * 保存用户角色关系
	 * 
	 * @param userRole
	 * @return 返回类型：String
	 */
	public String saveUserRole(UserRole userRole){
		return userRoleDao.saveUserRole(userRole);
	}

	public void updateUserRole(String userRoleId, Role role) {
		UserRole userRole = this.getEntity(userRoleId);
		userRole.setRole(role);
		this.updateEntity(userRole);
	}

}
