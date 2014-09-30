// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.mobile.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmcc.zysoft.sellmanager.model.Token;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：TokenDao
 * <br />版本:1.0.0
 * <br />日期： 2013-3-6 上午9:55:45
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Repository
public class TokenDao extends HibernateBaseDaoImpl<Token, String>{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 通过员工Id,获取登陆令牌.
	 * @param empId 
	 * @return 
	 * 返回类型：Token
	 */
	public Token getToken(String empId){
		String sql = "SELECT * FROM tb_c_token token WHERE token.employee_id=?";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,empId);
		if(list.size()>0){
			return (Token)list.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * 每次生成新的令牌之前删除以前旧的令牌.
	 * @param empId 
	 * 返回类型：void
	 */
	public void deleteToken(String empId){
		String sql = "DELETE FROM tb_c_token WHERE employee_id = '"+empId+"'";
		this.jdbcTemplate.execute(sql);
	}

}
