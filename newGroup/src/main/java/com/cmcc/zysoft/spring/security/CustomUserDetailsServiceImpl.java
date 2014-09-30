// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.spring.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.cmcc.zysoft.sellmanager.model.Role;
import com.cmcc.zysoft.sellmanager.model.SystemUser;
import com.cmcc.zysoft.spring.security.model.User;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述：自定义用户详细信息服务类
 * <br />版本:1.0.0
 * <br />日期： 2013-1-10 下午9:13:15
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
public class CustomUserDetailsServiceImpl extends HibernateDaoSupport implements
		UserDetailsService {

	/**
	 * 根据用户ID获取用户的角色列表
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Role> getRolesByUserId(String userId){
		String hql = "select r from Role as r,UserRole as ur,SystemUser as u " +
				"where r.roleId = ur.role.roleId " +
				"and u.userId = ur.systemUser.userId " +
				"and r.status = '0' " +
				"and u.userId = ?";

		List<Role>  roles = getHibernateTemplate().find(hql, userId);
		return roles;
	}
	
	/**
	 * 利用用户名登录系统
	 */
	@SuppressWarnings("unchecked")
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		//查找用户列表
		String hql = "from SystemUser as u where u.userName = ? and u.delFlag in('0','2')";
		
		try {
			List<SystemUser>  systemUsers = getHibernateTemplate().find(hql,username);
			if (systemUsers.size() < 1) {
	            throw new UsernameNotFoundException(username + "not found");
	        }
			
//			if (!"s_admin".equals(username)) {
//				throw new NoAuthorityException("noAuthority");
//			}
			SystemUser sysTUser = systemUsers.get(0);
			if(sysTUser.getDelFlag().equals("2")){
				throw new LockedException("userlock");
			}
			//创建spring security 用户
			User user = new User();
			user.setUsername(username);
			user.setCompanyId(sysTUser.getCompany().getCompanyId());
			//设置角色列表
			List<Role> roles = getRolesByUserId(sysTUser.getUserId());
			user.setRoles(roles);
			user.setEmail(sysTUser.getEmail());
			user.setEmployeeId(sysTUser.getEmployeeId());
			user.setLoginCount(sysTUser.getLoginCount());
			user.setLoginIp(sysTUser.getLoginIp());
			user.setMobile(sysTUser.getMobile());
			user.setPassSalt(sysTUser.getPassSalt());
			user.setPassword(sysTUser.getPassword());
			user.setPhoto(sysTUser.getPhoto());
			user.setRealName(sysTUser.getRealName());
			user.setSex(sysTUser.getSex());
			user.setUserId(sysTUser.getUserId());
			user.setLoginTime(sysTUser.getLoginTime());
			
			//获取角色列表
			hql = "select r from Role as r,UserRole as ur " +
					"where r.roleId = ur.role.roleId " +
					"and r.status = '0' " +
					"and r.company.companyId = ? " +
					"and ur.systemUser.userId = ? " ;
			
			roles = getHibernateTemplate().find(hql, sysTUser.getCompany().getCompanyId(), sysTUser.getUserId());
			
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			for(Role role : roles) {
				authorities.add(new com.cmcc.zysoft.spring.security.model.Role(role.getRoleCode()));
				user.setAuthorities(authorities);
			}
			
			return user;
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new UsernameNotFoundException(username + "not found");
		}
	}

}
