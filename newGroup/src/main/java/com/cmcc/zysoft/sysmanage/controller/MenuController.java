// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sysmanage.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.sellmanager.model.Menu;
import com.cmcc.zysoft.sellmanager.model.Role;
import com.cmcc.zysoft.spring.security.model.User;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.cmcc.zysoft.sysmanage.service.MenuService;

/**
 * @author 李三来
 * @mail li.sanlai@ustcinfo.com
 * @date 2012-11-17 上午10:17:16
 */
@Controller
@RequestMapping("/pc/menu")
public class MenuController extends BaseController {
	
	/**
	 * 属性名称：_logger <br/>
	 * 类型：Logger
	 */
	private static Logger _logger = LoggerFactory.getLogger(MenuController.class); 
	
	/**
	 * 属性名称：menuService <br/>
	 * 类型：MenuService
	 */
	@Resource
	private MenuService menuService;
	
	/**
	 * 过滤session过期
	 * @param url
	 * @return
	 */
	@RequestMapping(value="/filter")
	@ResponseBody
	public Map<String, String> filterSessionTimeout(String menuId,String url){
		_logger.debug("#过滤{}",url);
		Map<String, String> map = new HashMap<String,String>();
		map.put("menuId", menuId);
		map.put("url", url);
		return map;
	}
	
	/**
	 * 跳转到菜单管理页面
	 * @return
	 */
	@RequestMapping(value="/main.htm")
	public String main(){
		_logger.debug("跳转到菜单管理页面");
		return "sysmanage/menu/menu";
	}
	
	/**
	 * 跳转到权限管理页面
	 * @return
	 */
	@RequestMapping(value="/Rolemain.htm")
	public String Rolemain(){
		_logger.debug("跳转到权限管理页面");
		return "sysmanage/roleMenu/roleMenu";
	}
	
	/**
	 * 跳转到添加菜单页面
	 * @return
	 */
	@RequestMapping(value="/addPage.htm")
	public String addPage(){
		_logger.debug("跳转到添加菜单页面");
		return "sysmanage/menu/add";
	}
	
	/**
	 * 跳转到修改菜单页面
	 * @return
	 */
	@RequestMapping(value="/updatePage.htm")
	public String updatePage(String menuId,Model model){
		Menu menu = this.menuService.getMenuByPid(menuId);
		model.addAttribute("menu", menu);
		_logger.debug("跳转到修改菜单页面");
		return "sysmanage/menu/update";
	}
	
	/**
	 * 跳转到查看菜单详情页面
	 * @return
	 */
	@RequestMapping(value="/detailPage.htm")
	public String detailPage(String menuId,Model model){
		Menu menu = this.menuService.getMenuByPid(menuId);
		model.addAttribute("menu", menu);
		_logger.debug("跳转到查看菜单详情页面");
		return "sysmanage/menu/detail";
	}
	
	/**
	 * 初始化顶部菜单
	 * @return
	 */
	@RequestMapping(value="topmenu.htm",method=RequestMethod.POST)
	@ResponseBody
	public List<Menu> initTopMenu(){
		_logger.debug("初始化顶部菜单");
		User user = SecurityContextUtil.getCurrentUser();
		return menuService.initTopMenu(user.getCompanyId());
	}
	
	/**
	 * 获取特定父菜单下面的子菜单
	 * @return
	 */
	@RequestMapping(value="submenu/{pid}.htm",method=RequestMethod.POST)
	@ResponseBody
	public List<Menu> getMenus(@PathVariable String pid){
		_logger.debug("获取菜单ID={}的子菜单",pid);
		//User user = SecurityContextUtil.getCurrentUser();
		List<Menu> menus = menuService.getMenusByPid(pid);
		return menus;
	}
	
	/**
	 * 获取treegrid的菜单信息
	 * @param pid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="treemenu.htm")
	@ResponseBody
	public List<Map> getMenusForTreeGrid(String id){
		return menuService.getMenuTree(id);
	}
	
	/**
	 * 添加菜单
	 * @param menu
	 * @return
	 */
	@RequestMapping(value="add.htm",method=RequestMethod.POST)
	@ResponseBody
	@Transactional
	public String addMenu(Menu menu,@RequestParam("menu_icon") MultipartFile menu_icon,HttpServletRequest request){
		_logger.debug("添加菜单");
		return menuService.addMenu(menu,menu_icon,request);
	}
	
	/**
	 * 修改菜单
	 * @param menu
	 * @return
	 */
	@RequestMapping(value="update.htm",method=RequestMethod.POST)
	@ResponseBody
	@Transactional
	public String updateMenu(Menu menu,@RequestParam("menu_icon") MultipartFile menu_icon,HttpServletRequest request){
		_logger.debug("修改菜单");
		return this.menuService.updateMenu(menu,menu_icon,request);
	}
	
	/**
	 * 删除菜单
	 * @param menu
	 * @return
	 */
	@RequestMapping(value="delete.htm",method=RequestMethod.POST)
	@ResponseBody
	@Transactional
	public String deleteMenu(String menuIds){
		_logger.debug("删除菜单");
		String[] new_menuIds = menuIds.split(",");
		for(int i=0;i<new_menuIds.length;i++){
			this.menuService.deleteMenu(new_menuIds[i], true, true);
		}
		_logger.debug("删除菜单成功");
		return "SUCCESS";
	}
	
	/**
	 * 获取菜单树,列表展示用
	 * @return
	 */
	@RequestMapping(value="tree.htm")
	@ResponseBody
	public List<Map<String, Object>> menuTree(){
		//获取当前登陆用户所属的公司
		String companyId = SecurityContextUtil.getCompanyId();
		User user = SecurityContextUtil.getCurrentUser();
		List<Role> roleList = user.getRoles();
		if(roleList.get(0).getRoleCode().equals("6")){
			_logger.debug("获取菜单树,列表展示用");
			return this.menuService.menuTree("");
		}else{
			_logger.debug("获取菜单树,列表展示用");
			return this.menuService.menuTree(companyId);
		}
	}
	
	/**
	 * 获取菜单树,添加修改用
	 * @return
	 */
	@RequestMapping(value="comboTree.htm")
	@ResponseBody
	public List<Map<String, Object>> comboTree(){
		_logger.debug("获取菜单树,添加修改用");
		String companyId = SecurityContextUtil.getCompanyId();
		User user = SecurityContextUtil.getCurrentUser();
		List<Role> roleList = user.getRoles();
		List<Map<String, Object>> list = null;
		if(roleList.get(0).getRoleCode().equals("6")){
			 list = this.menuService.menuTree("");
		}else{
			 list = this.menuService.menuTree(companyId);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", "0");
		map.put("text", "无");
		list.add(map);
		return list;
	}
	
	/**
	 * 增加或修改菜单时候从元数据中获取菜单路径
	 */
	@RequestMapping(value="path.htm")
	@ResponseBody
	public List<Map<String, Object>> getPathFromMeta(){
		_logger.debug("从元数据中获取菜单路径");
		return this.menuService.getPathFromMeta();
	}
	
	/**
	 * 通过菜单Id找到指定菜单的所属公司Id
	 */
	@RequestMapping(value="findCompanyByMenuId.htm",method=RequestMethod.POST)
	@ResponseBody
	public String findCompanyByMenuId(String menuId){
		_logger.debug("通过菜单Id找到指定菜单的所属公司Id");
		Menu menu = this.menuService.getMenuByPid(menuId);
		return menu.getCompany().getCompanyId();
	}
	
	/**
	 * 查找菜单
	 * @param deptName
	 * @return
	 */
	@RequestMapping(value="/search.htm",method=RequestMethod.POST)
	@Transactional
	@ResponseBody
	public List<Map<String, Object>> searchDept(String menuName){
		return this.menuService.searchMenu(menuName);
	}
}
