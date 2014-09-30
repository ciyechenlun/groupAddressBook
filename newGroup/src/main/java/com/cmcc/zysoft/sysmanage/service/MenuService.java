// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sysmanage.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cmcc.zysoft.sellmanager.model.Company;
import com.cmcc.zysoft.sellmanager.model.Menu;
import com.cmcc.zysoft.sellmanager.model.MenuElement;
import com.cmcc.zysoft.spring.security.model.User;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.cmcc.zysoft.sysmanage.dao.MenuDao;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author 李三来
 * @mail li.sanlai@ustcinfo.com
 * @date 2012-11-17 上午10:08:43
 */
@Service
public class MenuService extends BaseServiceImpl<Menu, String> {
	
	private static Logger _logger = LoggerFactory.getLogger(MenuService.class); 

	@Resource
	private MenuDao menuDao;
	
	@Override
	public HibernateBaseDao<Menu, String> getHibernateBaseDao() {
		return this.menuDao;
	}
	
	/**
	 * 初始化顶部菜单
	 * @return
	 */
	public List<Menu> initTopMenu(String companyId){
		//顶层菜单的父菜单id=0
		final String parentId = "0";
		return getMenusByPid(parentId);
	}

	/**
	 * 添加菜单信息
	 * @param menu
	 * @return
	 */
	public String addMenu(Menu menu,MultipartFile menu_icon,HttpServletRequest request){
		if(!"0".equals(menu.getParentId())){
			Menu parent_menu = menuDao.get(menu.getParentId());
			parent_menu.setIsLeaf("N");
			menuDao.update(parent_menu);
		}
		menu.setIsLeaf("Y");
		if(menu_icon.getSize() != 0){
			String photoName = menu_icon.getOriginalFilename();
			String extName = photoName.substring(photoName.lastIndexOf(".")).toLowerCase();
			try {
	        	if(extName.trim().equals(".gif")||extName.trim().equals(".jpg")||extName.trim().equals(".jpeg")||extName.trim().equals(".png")||extName.trim().equals(".bmp")){
	    	        String lastFileName = "icon_"+System.currentTimeMillis()+extName;
	    	        String path = "/resources/img/icon/";
	    	        //图片存储的相对路径   
	    	        String fileFullPath = request.getServletContext().getRealPath("")+path+lastFileName;
	    			FileCopyUtils.copy(menu_icon.getBytes(),new File(fileFullPath));
	    			menu.setIcon(lastFileName);
	            }else{
	            	_logger.debug("图片格式错误");
	            	return "ERROR PHOTO";
	            }
			} catch (Exception e) {
				return "ERROR";
			}
		}
		String menuId = this.menuDao.save(menu);
		if(StringUtils.hasText(menuId)){
			_logger.debug("添加菜单成功");
			return "SUCCESS";
		}else{
			_logger.debug("添加菜单失败");
			return "FAIL";
		}
	}
	
	/**
	 * 修改菜单信息
	 * @param menu
	 * @return
	 */
	public String updateMenu(Menu menu,MultipartFile menu_icon,HttpServletRequest request){
		if(!"0".equals(menu.getParentId())){
			Menu parent_menu = menuDao.get(menu.getParentId());
			parent_menu.setIsLeaf("N");
			menuDao.update(parent_menu);
		}
		Menu new_menu = this.menuDao.get(menu.getMenuId());
		if(!new_menu.getParentId().equals(menu.getParentId())){
			List<Menu> list = this.findByNamedParam(new String[]{"parentId","status"}, new Object[]{new_menu.getParentId(),"1"});
			if(list.size() == 1){
				Menu new_parent_menu = menuDao.get(new_menu.getParentId());
				new_parent_menu.setIsLeaf("Y");
				menuDao.update(new_parent_menu);
			}
		}
		new_menu.setPath(menu.getPath());
		new_menu.setMenuName(menu.getMenuName());
		new_menu.setParentId(menu.getParentId());
		new_menu.setStatus(menu.getStatus());
		new_menu.setDisplayOrder(menu.getDisplayOrder());
		new_menu.setComment(menu.getComment());
		if(menu_icon.getSize() != 0){
			String photoName = menu_icon.getOriginalFilename();
			String extName = photoName.substring(photoName.lastIndexOf(".")).toLowerCase();
			try {
	        	if(extName.trim().equals(".gif")||extName.trim().equals(".jpg")||extName.trim().equals(".jpeg")||extName.trim().equals(".png")||extName.trim().equals(".bmp")){
	    	        String lastFileName = "icon_"+System.currentTimeMillis()+extName;
	    	        String path = "/resources/img/icon/";
	    	        //图片存储的相对路径   
	    	        String fileFullPath = request.getServletContext().getRealPath("")+path+lastFileName;
	    			FileCopyUtils.copy(menu_icon.getBytes(),new File(fileFullPath));
	    			String old_fileFullPath = request.getServletContext().getRealPath("")+path+new_menu.getIcon();
	    			new_menu.setIcon(lastFileName);
	    			File old_file = new File(old_fileFullPath);
	    			old_file.delete();
	            }else{
	            	_logger.debug("图片格式错误");
	            	return "ERROR PHOTO";
	            }
			} catch (Exception e) {
				return "ERROR";
			}
		}
		menuDao.update(new_menu);
		_logger.debug("修改菜单成功");
		return "SUCCESS";
	}
	
	/**
	 * 删除菜单信息
	 * @param menuId
	 * @param up
	 * @param down
	 */
	public void deleteMenu(String menuId, boolean up, boolean down){
		Menu menu = menuDao.get(menuId);
		menuDao.delete(menuId);
		if("N".equals(menu.getIsLeaf()) && down){
			List<Menu> list = this.findByNamedParam(new String[]{"parentId","status"}, new Object[]{menuId,"1"});
			if(null != list && list.size() > 0){
				Menu subMenu = null;
				for(int i=0;i<list.size();i++){
					subMenu = list.get(i);
					this.deleteMenu(subMenu.getMenuId(), false, down);
				}
			}
		}
		if(up && !"0".equals(menu.getParentId())){
			List<Menu> list = this.findByNamedParam(new String[]{"parentId","status"}, new Object[]{menu.getParentId(),"1"});
			if(list.size()==0){
				Menu parent_menu = this.menuDao.get(menu.getParentId());
				parent_menu.setIsLeaf("Y");
				this.menuDao.update(parent_menu);
			}
		}
	}
	
	/**
	 * 查询特定父菜单ID下面的所有有权限的子菜单
	 * @param pid
	 * @return
	 */
	public List<Menu> getMenusByPid(final String pid){
		//获取当前登录用户信息
		User user = SecurityContextUtil.getCurrentUser();
		return menuDao.getMenusByPid(pid, user);
	}
	
	/**
	 * 查询特定菜单Id的详情
	 * @param menuId
	 * @return
	 */
	public Menu getMenuByPid(String menuId){
		return menuDao.get(menuId);
	}
	
	/**
	 * 查询所有的菜单
	 * @return
	 */
	public List<Menu> getAllMenus(){
		return menuDao.getAllMenus();
	}
	
	/**
	 * 删除菜单
	 * @param menu
	 */
	public void deleteMenu(Menu menu){
		menuDao.deleteMenu(menu);
	}
	
	/**
	 * 获取菜单树
	 * @param pid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getMenuTree(String pid){
		return menuDao.getMenuTree(pid);
	}
	
	/**
	 * 获取菜单树的父菜单
	 * @param parent_id
	 * @return
	 */
	public List<Map<String, Object>> menuTree(String companyId){
		List<Map<String, Object>> list = this.menuDao.menuTree("0",companyId);
		return childMenu(list,companyId);
	}
	
	/**
	 * 递归获取子菜单
	 * @param list
	 * @return
	 */
	public List<Map<String, Object>> childMenu(List<Map<String, Object>> list,String companyId){
		for(Map<String, Object> map : list){
			String isLeaf = map.get("isLeaf").toString();
			if("N".equals(isLeaf)){
				String parentId = map.get("id").toString();
				List<Map<String, Object>> childList = this.menuDao.menuTree(parentId,companyId);
				childMenu(childList,companyId);
				map.put("children", childList);
				map.put("state", "closed");
			}else{
				map.put("state", "open");
			}
		}
		return list;
	}
	
	/**
	 * 增加或修改菜单时候从元数据中获取菜单路径
	 * @return
	 */
	public List<Map<String, Object>> getPathFromMeta(){
		return this.menuDao.getPathFromMeta();
	}
	
	/**
	 * 查询部门
	 * @param deptName
	 * @return
	 */
	public List<Map<String, Object>> searchMenu(String menuName){
		Map<String, Object> map = null;
		List<Map<String, Object>> list = this.menuDao.searchMenu(menuName);
		List<Map<String, Object>> parentList = new ArrayList<Map<String, Object>>();
		if(null != list && list.size() > 0){
			for (int i = 0; i < list.size(); i++) {
				map = list.get(i);
				if(!"0".equals(map.get("parent_id").toString())){
					parentList.addAll(searchParent(map.get("parent_id").toString()));
				}
			}
			list.addAll(parentList);
		}
		return list;
	}
	
	/**
	 * 通过menuId找到所有的父菜单
	 * @param menuId
	 * @return
	 */
	public List<Map<String, Object>> searchParent(String menuId){
		List<Map<String, Object>> parentList = this.menuDao.searchMenuByMenuId(menuId);
		Map<String,Object> parentMap = parentList.get(0);
		if(!"0".equals(parentMap.get("parent_id").toString())){
			parentList.addAll(searchParent(parentMap.get("parent_id").toString()));
		}
		return parentList;
	}
	
	/**
	 * 菜单图标设置
	 * @param menu_icon
	 * @param request
	 * @return
	 */
	public String menuIcon(MultipartFile menu_icon,HttpServletRequest request){
		if(menu_icon.getSize() != 0){
			String photoName = menu_icon.getOriginalFilename();
			String extName = photoName.substring(photoName.lastIndexOf(".")).toLowerCase();
			try {
	        	if(extName.trim().equals(".gif")||extName.trim().equals(".jpg")||extName.trim().equals(".jpeg")||extName.trim().equals(".png")||extName.trim().equals(".bmp")){
	    	        String lastFileName = "icon_"+System.currentTimeMillis()+extName;
	    	        String path = "/resources/img/icon/";
	    	        //图片存储的相对路径   
	    	        String fileFullPath = request.getServletContext().getRealPath("")+path+lastFileName;
	    			FileCopyUtils.copy(menu_icon.getBytes(),new File(fileFullPath));
	    			return lastFileName;
	            }else{
	            	_logger.debug("图片格式错误");
	            	return "ERROR PHOTO";
	            }
			} catch (Exception e) {
				return "ERROR";
			}
		}else{
			return "";
		}
	}
	
	/**
	 * 根据公司ID获取菜单
	 * 
	 * @param companyId
	 * @return 返回类型：List<Menu>
	 */
	public List<Menu> getMenulistByCompany(String companyId){
		return menuDao.getMenulistByCompany(companyId);
	}
	
	/**
	 * 根据公司ID和父菜单ID查询有效的菜单列表
	 * 
	 * @param companyId
	 * @param pid
	 * @return 返回类型：List<Menu>
	 */
	public List<Menu> getStatusMenulistByComapnyAndPid(String companyId,String pid){
		return menuDao.getStatusMenulistByComapnyAndPid(companyId, pid);
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
	public List<Menu> getStatusMenuListByCompanyAndPid(String companyId,String pid,List<String> excludePathList){
		return menuDao.getStatusMenuListByCompanyAndPid(companyId, pid,excludePathList);
	}
	
	/**
	 * 拷贝菜单信息
	 * 
	 * @param menu
	 * @return 返回类型：Menu
	 */
	public Menu copyMenuinfoWithoutId(Menu menu){
		String menuId = null;
		Company company = null;
		String  parentId = menu.getParentId(); 
		String metaId = menu.getMetaId(); 
		String menuName = menu.getMenuName(); 
		String path = menu.getPath(); 
		String isLeaf = menu.getIsLeaf(); 
		String icon = menu.getIcon(); 
		Integer displayOrder = menu.getDisplayOrder(); 
		String status = menu.getStatus(); 
		String comment = menu.getComment(); 
		Set<MenuElement> menuElements = null;
		Menu newMenu = new Menu(menuId, company, parentId, metaId, menuName, path, isLeaf, displayOrder, icon, status, comment,menuElements);
		return newMenu;
	}
	
	/**
	 * 保存菜单
	 * 
	 * @param menu
	 * @return 返回类型：String
	 */
	public String saveMenu(Menu menu){
		return this.menuDao.saveMenu(menu);
	}
}
