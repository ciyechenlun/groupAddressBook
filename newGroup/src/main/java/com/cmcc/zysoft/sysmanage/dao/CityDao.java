// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sysmanage.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmcc.zysoft.sellmanager.model.Cities;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述：
 * <br />@version:1.0.0
 * <br />日期： 2012-12-12 上午10:51:20
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Repository
public class CityDao extends HibernateBaseDaoImpl<Cities, Integer> {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 根据省份获取对应的城市列表
	 * @param provinceid 省份ID
	 * @return 城市列表
	 */
	public List<Map<String, Object>> cities(String provinceid){
		String sql = "SELECT * FROM cities WHERE provinceid = '"+provinceid+"'";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
		return list;
	}

}
