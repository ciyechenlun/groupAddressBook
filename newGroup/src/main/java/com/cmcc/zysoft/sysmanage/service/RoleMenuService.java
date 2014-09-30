// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sysmanage.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.sellmanager.model.Menu;
import com.cmcc.zysoft.sellmanager.model.Role;
import com.cmcc.zysoft.sellmanager.model.RoleMenu;
import com.cmcc.zysoft.sysmanage.dao.MenuDao;
import com.cmcc.zysoft.sysmanage.dao.RoleMenuDao;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author li.menghua
 * @date 2012-12-7 上午10:44:09
 */
@Service
public class RoleMenuService extends BaseServiceImpl<RoleMenu, String>{
	
	@Resource
	private RoleMenuDao roleMenuDao;
	@Resource
	private MenuDao menuDao;
	
	
	@Override
	public HibernateBaseDao<RoleMenu, String> getHibernateBaseDao(){
		return this.roleMenuDao;
	}
	
	/**
	 * 保存菜单角色关系
	 * 
	 * @param roleMenu
	 * @return 返回类型：String
	 */
	public String saveRolemenu(RoleMenu roleMenu){
		return this.roleMenuDao.saveRolemenu(roleMenu);
	}
	
	/**
	 * 增加叶子节点菜单
	 * @param Id
	 * @param roleId
	 * @return
	 */
	public String saveRight(String Id,String roleId){
		RoleMenu roleMenu = new RoleMenu();
		Menu menu = new Menu();
		Role role = new Role();
		menu.setMenuId(Id);
		role.setRoleId(roleId);
		roleMenu.setMenuId(Id);
		roleMenu.setRole(role);
		List<RoleMenu> list = this.findByNamedParam(new String[]{"menu.menuId","role.roleId"} , new Object[]{Id,roleId});
		if(list.size()>0){
			return "";
		}else{
			this.getParentMenu(Id, roleId);
			return roleMenuDao.save(roleMenu);
		}
	}
	
	/**
	 * 删除叶子节点菜单
	 * @param Id
	 * @param roleId
	 */
	public void deleteRight(String Id,String roleId){
		List<RoleMenu> list = this.findByNamedParam(new String[]{"menu.menuId","role.roleId"} , new Object[]{Id,roleId});
		if(list.size()>0){
			roleMenuDao.delete(list.get(0));
			this.deleteParentMenu(Id, roleId);
		}
	}
	
	/**
	 * 增加非叶子节点菜单
	 * @param Id
	 * @param roleId
	 */
	public void saveRights(String Id,String roleId){
		this.saveRight(Id,roleId);
		List<Map<String, Object>> subMenuList = this.roleMenuDao.findSubMenu(Id);
		if(subMenuList != null && subMenuList.size()>0){
			for(Map<String, Object> map :subMenuList){
				if(map.get("isLeaf").toString().equals("Y")){
					String menuId = map.get("menuId").toString();
					this.saveRight(menuId,roleId);
				}else{
					this.saveRights(map.get("menuId").toString(),roleId);
				}
			}
		}
	}
	
	/**
	 * 删除非叶子节点菜单
	 * @param Id
	 * @param roleId
	 */
	public void deleteRights(String Id,String roleId){
		this.deleteRight(Id,roleId);
		List<Map<String, Object>> subMenuList = this.roleMenuDao.findSubMenu(Id);
		if(subMenuList != null && subMenuList.size()>0){
			for(Map<String, Object> map :subMenuList){
				if(map.get("isLeaf").toString().equals("Y")){
					String menuId = map.get("menuId").toString();
					this.deleteRight(menuId,roleId);
				}else{
					this.deleteRights(map.get("menuId").toString(),roleId);
				}
			}
		}
	}
	
	/**
	 * 通过角色Id获取角色对应的菜单
	 * @param roleId
	 * @return
	 */
	public List<Map<String, Object>> findRoleRightByRoleId(String roleId){
		return this.roleMenuDao.findRoleRightByRoleId(roleId);
	}
	
	/**
	 * 通过指定菜单Id获得上级菜单,如果存在上级菜单,则为某个角色插入菜单的时候，插入该菜单的上级菜单
	 * @param menuId
	 * @param roleId
	 */
	public void getParentMenu(String menuId,String roleId){
		Menu menu = this.menuDao.get(menuId);
		if(!"0".equals(menu.getParentId())){
			Menu parent_menu = this.menuDao.get(menu.getParentId());
			this.saveRight(parent_menu.getMenuId(), roleId);
		}
	}
	
	/**
	 * 通过指定菜单Id获得上级菜单,如果存在上级菜单,删除该菜单后,角色对应的菜单中无该上级菜单的子菜单,则删除父菜单
	 * @param menuId
	 * @param roleId
	 */
	public void deleteParentMenu(String menuId,String roleId){
		Menu menu = this.menuDao.get(menuId);
		if(!"0".equals(menu.getParentId())){
			Menu parent_menu = this.menuDao.get(menu.getParentId());
			int count = this.roleMenuDao.findRoleRightCount(parent_menu.getMenuId());
			if(count==1){
				this.deleteRight(parent_menu.getMenuId(), roleId);
			}
		}
	}

}
