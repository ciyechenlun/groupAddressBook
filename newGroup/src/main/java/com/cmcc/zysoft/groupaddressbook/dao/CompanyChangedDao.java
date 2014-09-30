// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.dao;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmcc.zysoft.sellmanager.model.CompanyChanged;
import com.cmcc.zysoft.sellmanager.model.CompanyChangedId;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：CompanyChangedDao
 * <br />版本:1.0.0
 * <br />日期： 2013-5-27 上午11:04:21
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Repository
public class CompanyChangedDao extends HibernateBaseDaoImpl<CompanyChanged, CompanyChangedId>{
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	public void delVersion(String companyId){
		String sql = "delete from tb_c_company_changed where company_id=? and update_type='3'";
		this.jdbcTemplate.update(sql, companyId);
	}
}
