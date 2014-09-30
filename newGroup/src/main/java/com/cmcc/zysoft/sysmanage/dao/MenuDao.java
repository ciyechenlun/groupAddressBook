// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sysmanage.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cmcc.zysoft.sellmanager.model.Menu;
import com.cmcc.zysoft.sellmanager.model.Role;
import com.cmcc.zysoft.spring.security.model.User;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;

/**z
 * @author 李三来
 * @mail li.sanlai@ustcinfo.com
 * @date 2012-11-17 上午9:46:56
 */
@Repository
public class MenuDao extends HibernateBaseDaoImpl<Menu, String> {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 添加菜单信息
	 * @param menu
	 * @return
	 */
	public String addMenu(Menu menu){
		String menuId = (String)this.getHibernateTemplate().save(menu);
		return menuId;
	}
	
	/**
	 * 查询特定父菜单ID下面的所有子菜单(顶部菜单)
	 * @param pid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Menu> getMenusByPid(String pid){
		String hql = "from Menu as m where m.parentId = ? order by displayOrder";
		List<Menu> Menus = this.getHibernateTemplate().find(hql,pid);
		return Menus;
	}
	
	/**
	 * 根据父菜单ID获取当前用户所能看见的菜单列表
	 * @param pid
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Menu> getMenusByPid(final String pid,User user){
		final String companyId = user.getCompanyId();
		//获取用户所拥有的角色列表
		final List<Role> roles = user.getRoles();
		
		final String hql = "select distinct m from Menu as m,RoleMenu as rm,Role as r " +
				"where m.menuId = rm.menu.menuId and r.roleId = rm.role.roleId and m.status = '1' and r.status = '0' " +
				"and m.company.companyId = r.company.companyId " +
				"and m.company.companyId = :companyId and m.parentId = :parentId " +
				"and r in (:roles) order by m.displayOrder" ;
		List<Menu> menus = this.getHibernateTemplate().executeFind(
				new HibernateCallback<List<Menu>>() {
					public List<Menu> doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createQuery(hql);
						query.setParameter("companyId", companyId);
						query.setParameter("parentId", pid);
						query.setParameterList("roles", roles);
						List<Menu> list = query.list();
						return list;
					}
				});
		return menus;
	}
	
	/**
	 * 查询所有的菜单
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Menu> getAllMenus(){
		String hql = "from Menu as m order by parentId,displayOrder";
		List<Menu> Menus = this.getHibernateTemplate().find(hql);
		return Menus;
	}
	
	/**
	 * 删除菜单
	 * @param Menu
	 */
	public void deleteMenu(Menu Menu){
		this.getHibernateTemplate().delete(Menu);
	}
	
	/**
	 * 获取菜单总数
	 * @return
	 */
	public long getMenuTotalSize(){
		long total = 0;
		String countHql = "select count(*) from Menu";
		Query query = this.getSession().createQuery(countHql);
		total = ( (Long) query.iterate().next() ).longValue();
		return total;
	}
	
	/**
	 * 构建菜单树
	 * @param pid
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> getMenuTree(String pid){
		String hql = "select new Map(menu.menuId as id," +
				"menu.menuName as text," +
				"'folder' as iconCls," +
				"(case menu.isLeaf when 'N' then 'closed' when 'Y' then 'open' else 'closed' end) as state," +
				"menu.menuId as menuId," +
				"menu.PId as pid," +
				"menu.menuName as menuName," +
				"menu.url as url," +
				"menu.isLeaf as isLeaf," +
				"menu.displayOrder as displayOrder) " +
				"from Menu menu  where 1 = 1";
		hql += (pid == null || "0".equals(pid)) ? " and (menu.PId = '0' or menu.PId is null)"
				: " and menu.PId = '" + pid + "'";
		hql += " order by menu.displayOrder asc";
		List<Map> list = this.getHibernateTemplate().find(hql);
		return list;
	}
	
	/**
	 * 构建菜单树
	 * @param parent_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> menuTree(String parentId,String companyId){
		String hql = "select new Map(menu.menuId as id," +
				"menu.menuName as text," +
				"menu.isLeaf as isLeaf," +
				"menu.isLeaf as attributes," +
				"menu.path as path," +
				"comp.companyName as companyName," +
				"(case menu.isLeaf when 'N' then 'closed' when 'Y' then 'open' else 'closed' end) as state)" +
				" from Menu menu,Company comp where menu.company.companyId=comp.companyId and menu.parentId = ? ";
		if(StringUtils.hasText(companyId)){
			hql += " and menu.company.companyId='"+companyId+"'";
		}
		hql += " order by menu.displayOrder asc";
		return this.findByHQL(hql, parentId);
	}
	
	/**
	 * 增加或修改菜单时候从元数据中获取菜单路径
	 * @return
	 */
	public List<Map<String, Object>> getPathFromMeta(){
		String sql = "SELECT meta.meta_name AS menuName,meta.meta_code AS path,meta.meta_id AS metaId" +
				" FROM tb_c_meta_data meta,tb_c_config_type typ " +
				" WHERE meta.type_id = typ.type_id AND typ.type_code='menu' ";
		return this.jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * 查询菜单
	 * @param MenuName
	 * @return
	 */
	public List<Map<String, Object>> searchMenu(String MenuName){
		String companyId = SecurityContextUtil.getCompanyId();
		String sql = "SELECT * FROM tb_c_menu menu WHERE menu.company_id='"+companyId+"' AND menu.menu_name like '%"+MenuName.trim()+"%' ";
		return this.jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * 通过菜单Id查找对应的菜单
	 * @param menuId
	 * @return
	 */
	public List<Map<String, Object>> searchMenuByMenuId(String menuId){
		String sql = "SELECT * FROM tb_c_menu menu WHERE menu.menu_id =? ";
		return this.jdbcTemplate.queryForList(sql,menuId);
	}
	
	/**
	 * 根据公司ID获取菜单
	 * 
	 * @param companyId
	 * @return 返回类型：List<Menu>
	 */
	public List<Menu> getMenulistByCompany(String companyId){
		return this.findByNamedParam(new String[]{"company.companyId","status"}, new String[]{companyId,"1"});
	}

	/**
	 * 根据公司ID和父菜单ID查询有效的菜单列表
	 * 
	 * @param companyId
	 * @param pid
	 * @return 返回类型：List<Menu>
	 */
	public List<Menu> getStatusMenulistByComapnyAndPid(String companyId,String pid){
		return this.findByNamedParamAndOrder(new String[]{"company.companyId","parentId","status"}, new String[]{companyId,pid,"1"},new Order[]{Order.asc("displayOrder")});
	}
	
	/**
	 * 根据公司ID和父菜单ID查询有效的菜单列表,
	 * 
	 * @param companyId
	 * @param pid
	 * @param excludePathList 表示要过滤掉的菜单路径
	 * @return 
	 * 返回类型：List<Menu>
	 */
	@SuppressWarnings("unchecked")
	public List<Menu> getStatusMenuListByCompanyAndPid(String companyId,String pid,List<String> excludePathList){
			final String companyID = companyId;
			final String PID = pid;
			final String status = "1";
			final List<String> excludePathLIST = excludePathList;
			
			final String hql = "select distinct m from Menu as m " +
					"where m.company.companyId = :companyId " +
					"and m.parentId = :parentId " +
					"and m.status = :status " +
					"and m.path not in (:excludePathList) order by m.displayOrder" ;
			List<Menu> menus = this.getHibernateTemplate().executeFind(
					new HibernateCallback<List<Menu>>() {
						public List<Menu> doInHibernate(Session session)
								throws HibernateException, SQLException {
							Query query = session.createQuery(hql);
							query.setParameter("companyId", companyID);
							query.setParameter("parentId", PID);
							query.setParameter("status", status);
							query.setParameterList("excludePathList", excludePathLIST);
							List<Menu> list = query.list();
							return list;
						}
					});
			return menus;
	}
	
	/**
	 * 保存菜单
	 * @param menu
	 * @return 返回类型：String
	 */
	@Transactional
	public String saveMenu(Menu menu){
		return this.save(menu);
	}
	
}
