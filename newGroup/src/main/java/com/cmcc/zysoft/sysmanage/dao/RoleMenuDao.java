// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sysmanage.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmcc.zysoft.sellmanager.model.RoleMenu;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;

/**
 * @author li.menghua
 * @date 2012-12-7 上午10:14:27
 */
@Repository
public class RoleMenuDao extends HibernateBaseDaoImpl<RoleMenu, String>{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	

	/**
	 * 保存菜单角色关系
	 * 
	 * @param roleMenu
	 * @return 返回类型：String
	 */
	@Transactional
	public String saveRolemenu(RoleMenu roleMenu){
		return this.save(roleMenu);
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
				"menu.menu_name AS menuName " +
				"FROM tb_c_menu menu WHERE menu.status='1' AND menu.parent_id=?";
		return this.jdbcTemplate.queryForList(sql,menuId);
	}
	
	
	/**
	 * 通过角色Id获取角色对应的菜单
	 * @param roleId
	 * @return
	 */
	public List<Map<String, Object>> findRoleRightByRoleId(String roleId){
		String sql = "SELECT roleMenu.* " +
				"FROM tb_c_role_menu roleMenu, tb_c_menu menu " +
				"WHERE roleMenu.menu_id=menu.menu_id AND menu.is_leaf='Y' " +
				"AND roleMenu.role_id=?";
		return this.jdbcTemplate.queryForList(sql,roleId);
	}
	
	/**
	 * 通过父菜单Id查找子菜单数量
	 * @param parentId
	 * @return
	 */
	public int findRoleRightCount(String parentId){
		String sql = "SELECT COUNT(*) " +
				"FROM tb_c_role_menu roleMenu,tb_c_menu menu " +
				"WHERE roleMenu.menu_id=menu.menu_id AND menu.parent_id=?";
		return this.jdbcTemplate.queryForInt(sql,parentId);
		
		
	}

}
