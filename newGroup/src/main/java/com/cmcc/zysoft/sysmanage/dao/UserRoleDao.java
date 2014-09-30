// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sysmanage.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmcc.zysoft.sellmanager.model.UserRole;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;

/**
 * @author yandou
 */
@Repository
public class UserRoleDao extends HibernateBaseDaoImpl<UserRole, String> {
	
	/**
	 * 保存用户角色关系
	 * 
	 * @param userRole
	 * @return 返回类型：String
	 */
	@Transactional
	public String saveUserRole(UserRole userRole){
		return this.save(userRole);
	}
	
}
