// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sysmanage.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmcc.zysoft.sellmanager.model.Provinces;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述：ProvinceDao
 * <br />@version:1.0.0
 * <br />日期： 2012-12-12 上午10:49:21
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Repository
public class ProvinceDao extends HibernateBaseDaoImpl<Provinces, Integer> {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 获取省列表
	 * @return
	 */
	public List<Map<String, Object>> province(){
		String sql = "SELECT * FROM provinces";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
		return list;
	}
}
