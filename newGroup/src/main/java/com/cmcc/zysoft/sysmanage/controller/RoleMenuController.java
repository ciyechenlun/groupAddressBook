// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sysmanage.controller;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.sysmanage.service.RoleMenuService;

/**
 * @author li.menghua
 * @date 2012-12-7 上午10:49:42
 */
@Controller
@RequestMapping("/pc/roleMenu")
public class RoleMenuController extends BaseController{
	
	private static Logger _logger = LoggerFactory.getLogger(RoleMenuController.class); 
	
	@Resource
	private RoleMenuService roleMenuService;
	
	
	/**
	 * 增加叶子节点菜单
	 * @param menuId
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value="save.htm",method=RequestMethod.POST)
	@Transactional
	@ResponseBody
	public String saveRight(String menuId,String roleId){
		String id = this.roleMenuService.saveRight(menuId,roleId);
		if(StringUtils.hasText(id)){
			_logger.debug("增加叶子节点菜单成功");
			return "SUCCESS";
		}else{
			_logger.debug("增加叶子节点菜单失败");
			return "FAILURE";
		}
	}
	
	/**
	 * 删除叶子节点菜单
	 * @param menuId
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value="delete.htm",method=RequestMethod.POST)
	@Transactional
	@ResponseBody
	public String deleteRight(String menuId,String roleId){
		this.roleMenuService.deleteRight(menuId,roleId);
		_logger.debug("删除叶子节点菜单成功");
		return "SUCCESS";
	}
	
	/**
	 * 增加非叶子节点菜单
	 * @param menuId
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value="saveAll.htm",method=RequestMethod.POST)
	@Transactional
	@ResponseBody
	public String saveRights(String menuId,String roleId){
		this.roleMenuService.saveRights(menuId,roleId);
		_logger.debug("增加非叶子节点菜单成功");
		return "SUCCESS";
	}
	
	/**
	 * 删除非叶子节点菜单
	 * @param menuId
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value="deleteAll.htm",method=RequestMethod.POST)
	@Transactional
	@ResponseBody
	public String deleteRights(String menuId,String roleId){
		this.roleMenuService.deleteRights(menuId,roleId);
		_logger.debug("删除非叶子节点菜单成功");
		return "SUCCESS";
	}
	
	/**
	 * 通过角色Id获取角色对应的菜单
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value="findRoleRightByRoleId.htm",method=RequestMethod.POST)
	@ResponseBody
	public List<Map<String, Object>> findRoleRightByRoleId(String roleId){
		_logger.debug("通过角色Id获取角色对应的菜单");
		return this.roleMenuService.findRoleRightByRoleId(roleId);
	}
	

}
