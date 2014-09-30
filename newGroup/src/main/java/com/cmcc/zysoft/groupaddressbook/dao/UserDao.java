// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmcc.zysoft.sellmanager.model.SystemUser;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：UserDao
 * <br />版本:1.0.0
 * <br />日期： 2013-3-14 下午3:18:58
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Repository
public class UserDao extends HibernateBaseDaoImpl<SystemUser, String>{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 根据手机号码返回系统用户信息
	 * @param mobile
	 * @return
	 */
	public List<SystemUser> getSystemUserByMobile(String mobile)
	{
		String sql = "FROM SystemUser WHERE userName=?";
		return this.getHibernateTemplate().find(sql, mobile);
	}
}
