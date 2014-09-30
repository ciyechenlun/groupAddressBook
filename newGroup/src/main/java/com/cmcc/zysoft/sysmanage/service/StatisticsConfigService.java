/** ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved. */
package com.cmcc.zysoft.sysmanage.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.sellmanager.model.Menu;
import com.cmcc.zysoft.sellmanager.model.StatisticsConfig;
import com.cmcc.zysoft.sysmanage.dao.StatisticsConfigDao;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author li.menghua
 * @2013-2-4
 */
@Service
public class StatisticsConfigService extends BaseServiceImpl<StatisticsConfig, String>{
	
	/**
	 * 属性名称：statisticsConfigDao <br/>
	 * 类型：StatisticsConfigDao
	 */
	@Resource
	private StatisticsConfigDao statisticsConfigDao;
	
	/**
	 * 属性名称：menuService <br/>
	 * 类型：MenuService
	 */
	@Resource
	private MenuService menuService;
	
	@Override
	public HibernateBaseDao<StatisticsConfig, String> getHibernateBaseDao(){
		return this.statisticsConfigDao;
	}
	
	/**
	 * 获取已选的配置信息
	 * @return
	 */
	public List<Map<String, Object>> list(){
		return this.statisticsConfigDao.list();
	}
	
	/**
	 * 添加叶子节点
	 * @param menuId
	 * @param menuName
	 * @param path
	 * @return
	 */
	public String save(String menuId,String menuName,String path){
		List<StatisticsConfig> list = this.findByNamedParam("menuId", menuId);
		if(list.size()>0){
			return "";
		}else{
			StatisticsConfig config = new StatisticsConfig();
			config.setMenuId(menuId);
			config.setMenuName(menuName);
			config.setModuleName(menuName);
			config.setPath(path);
			this.saveParent(menuId);
			return this.statisticsConfigDao.save(config);
		}
	}
	
	/**
	 * 删除叶子节点菜单
	 * @param menuId
	 */
	public void delete(String menuId){
		List<StatisticsConfig> list = this.findByNamedParam("menuId", menuId);
		if(list.size()>0){
			this.statisticsConfigDao.delete(list.get(0));
			this.deleteParent(menuId);
		}
	}
	
	/**
	 * 增加非叶子节点菜单
	 * @param menuId
	 * @param menuName
	 * @param path
	 */
	public void saveAll(String menuId,String menuName,String path){
		this.save(menuId, menuName, path);
		List<Map<String, Object>> subMenuList = this.statisticsConfigDao.findSubMenu(menuId);
		if(subMenuList != null && subMenuList.size()>0){
			for(Map<String, Object> map :subMenuList){
				String id = map.get("menuId").toString();
				String name = map.get("menuName").toString();
				String _path = map.get("path").toString();
				if(map.get("isLeaf").toString().equals("Y")){
					this.save(id, name, _path);
				}else{
					this.saveAll(id, name, _path);
				}
			}
		}
	}
	
	/**
	 * 删除非叶子节点菜单
	 * @param menuId
	 */
	public void deleteAll(String menuId){
		this.delete(menuId);
		List<Map<String, Object>> subMenuList = this.statisticsConfigDao.findSubMenu(menuId);
		if(subMenuList != null && subMenuList.size()>0){
			for(Map<String, Object> map :subMenuList){
				String id = map.get("menuId").toString();
				if(map.get("isLeaf").toString().equals("Y")){
					this.delete(id);
				}else{
					this.deleteAll(id);
				}
			}
		}
	}
	
	/**
	 * 保存上级菜单
	 * @param menuId
	 */
	public void saveParent(String menuId){
		Menu menu = this.menuService.getEntity(menuId);
		if(!"0".equals(menu.getParentId())){
			Menu parent_menu = this.menuService.getEntity(menu.getParentId());
			this.save(parent_menu.getMenuId(), parent_menu.getMenuName(), parent_menu.getPath());
		}
	}
	
	/**
	 * 若删除某个菜单后,父菜单下已无子菜单,则删除父菜单
	 * @param menuId
	 */
	public void deleteParent(String menuId){
		Menu menu = this.menuService.getEntity(menuId);
		if(!"0".equals(menu.getParentId())){
			Menu parent_menu = this.menuService.getEntity(menu.getParentId());
			int count = this.statisticsConfigDao.findCount(parent_menu.getMenuId());
			if(count==1){
				this.delete(parent_menu.getMenuId());
			}
		}
	}

}
