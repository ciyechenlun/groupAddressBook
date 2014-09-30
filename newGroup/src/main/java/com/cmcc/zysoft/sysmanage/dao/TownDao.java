// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sysmanage.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmcc.zysoft.sellmanager.model.Areas;
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
public class TownDao extends HibernateBaseDaoImpl<Areas, Integer> {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 根据城市获取区县列表
	 * @param cityid 城市ID
	 * @return 区县列表
	 */
	public List<Map<String, Object>> areas(String cityid){
		String sql = "SELECT * FROM areas WHERE cityid = '"+cityid+"'";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
		return list;
	}
	
}
