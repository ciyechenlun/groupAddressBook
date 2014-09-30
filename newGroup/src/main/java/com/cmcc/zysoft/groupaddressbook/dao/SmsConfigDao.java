/**
 * CopyRight © 2012 USTC SINOVATE SOFTWARE CO.LTD All Rights Reserved.
 */

package com.cmcc.zysoft.groupaddressbook.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmcc.zysoft.sellmanager.model.SmsConfig;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;

/**
 * @author 袁凤建
 * <br />邮箱：yuan.fengjian@ustcinfo.com
 * <br />描述：SmsConfigDao.java
 * <br />版本: 1.0.0
 * <br />日期：2013-8-2 上午10:28:19
 * <br />CopyRight © 2012 USTC SINOVATE SOFTWARE CO.LTD All Rights Reserved.
 */

@Repository
public class SmsConfigDao extends HibernateBaseDaoImpl<SmsConfig, String> {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 根据公司ID获取短信推广记录.
	 * @param companyId
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> getConfigByCompanyId(String companyId) {
		String sql = "SELECT a.* FROM tb_c_sms_config a WHERE a.company_id = ?";
		return this.jdbcTemplate.queryForList(sql, companyId);
	}
}