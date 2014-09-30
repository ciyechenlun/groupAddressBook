/** ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved. */
package com.cmcc.zysoft.sysmanage.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmcc.zysoft.sellmanager.model.StatisticsConfig;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;

/**
 * @author li.menghua
 * @2013-2-4
 */
@Repository
public class StatisticsConfigDao extends HibernateBaseDaoImpl<StatisticsConfig, String>{
	
	/**
	 * 属性名称：jdbcTemplate <br/>
	 * 类型：JdbcTemplate
	 */
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 获取已选的配置信息
	 * @return
	 */
	public List<Map<String, Object>> list(){
		String sql = "SELECT conf.* FROM tb_c_statistics_config conf ,tb_c_menu menu WHERE menu.menu_id=conf.menu_id AND menu.is_leaf='Y'";
		return this.jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * 找到同一个父菜单下已选子菜单的数量
	 * @param parentId
	 * @return
	 */
	public int findCount(String parentId){
		String sql = "SELECT COUNT(*) FROM tb_c_statistics_config conf,tb_c_menu menu WHERE menu.menu_id=conf.menu_id AND menu.parent_id=?";
		return this.jdbcTemplate.queryForInt(sql,parentId);
	}
	
	/**
	 * 根据指定父菜单Id查询子菜单
	 * @param menuId
	 * @return
	 */
	public List<Map<String, Object>> findSubMenu(String menuId){
		String sql = "SELECT menu.menu_id AS menuId," +
				"menu.parent_id AS parentId," +
				"menu.is_leaf AS isLeaf," +
				"menu.path AS path," +
				"menu.menu_name AS menuName " +
				"FROM tb_c_menu menu WHERE menu.status='1' AND menu.parent_id=?";
		return this.jdbcTemplate.queryForList(sql,menuId);
	}

}
