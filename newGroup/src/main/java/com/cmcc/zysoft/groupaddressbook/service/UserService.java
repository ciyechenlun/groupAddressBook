// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cmcc.zysoft.framework.utils.UUIDUtil;
import com.cmcc.zysoft.groupaddressbook.dao.UserDao;
import com.cmcc.zysoft.sellmanager.model.Company;
import com.cmcc.zysoft.sellmanager.model.Role;
import com.cmcc.zysoft.sellmanager.model.SystemUser;
import com.cmcc.zysoft.sellmanager.util.MD5Tools;
import com.cmcc.zysoft.spring.security.model.User;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：UserService
 * <br />版本:1.0.0
 * <br />日期： 2013-3-14 下午3:19:57
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class UserService extends BaseServiceImpl<SystemUser, String>{
	
	@Resource
	private UserDao userDao;
	
	@Override
	public HibernateBaseDao<SystemUser, String>getHibernateBaseDao(){
		return this.userDao;
	}
	
	/**
	 * 添加用户.
	 * @param employeeId
	 * @param mobile
	 * @return 
	 * 返回类型：String
	 */
	public String addUser(String employeeId,String mobile,String companyId){
		Company company = new Company(companyId); //虚拟公司
		String salt = UUIDUtil.generateUUID();
		SystemUser systemUser = new SystemUser();
		systemUser.setEmployeeId(employeeId);
		systemUser.setUserName(mobile);
		systemUser.setDelFlag("0");
		systemUser.setCreateTime(new Date());
		systemUser.setPassSalt(salt);
		systemUser.setPassword(MD5Tools.encodePassword("111111", salt));
		systemUser.setCompany(company);
		String userId = this.userDao.save(systemUser);
		if(StringUtils.hasText(userId)){
			return userId;
		}else{
			return "";
		}
	}
	
	/**
	 * 判断当前用户是否为企业管理员
	 * @param user：当前登录用户实体
	 * @return 0：非企业管理员，1为企业管理员
	 */
	public String isManager(User user)
	{
		List<Role> roles = user.getRoles();
		if(roles.size()>0)
		{
			if(roles.get(0).getRoleId().equals("1")) //管理员
			{
				return "1";
			}
			else
			{
				return "0";
			}
		}
		else
		{
			return "0";
		}
	}

}
