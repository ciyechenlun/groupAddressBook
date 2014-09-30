// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmcc.zysoft.sellmanager.model.Advise;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;

@Repository
public class ReplyDao extends HibernateBaseDaoImpl<Advise, String>{

	@Resource
	private JdbcTemplate jdbcTemplate;
	//反馈人信息
	public List<Map<String, Object>> getInfo(String adviseId){
		String sql = "SELECT " +
				"emp.employee_name AS adviseMan," +
				"ad.content AS content," +
				"cmp.company_name as company_name," +
				"emp.mobile as mobile "+
				"FROM tb_c_advise ad,tb_c_employee emp,tb_c_company cmp " +
				"WHERE emp.employee_id=ad.advise_man AND cmp.company_id=ad.company_id " +
				"AND advise_id = ?";
		return this.jdbcTemplate.queryForList(sql,adviseId);
	}
	
}
