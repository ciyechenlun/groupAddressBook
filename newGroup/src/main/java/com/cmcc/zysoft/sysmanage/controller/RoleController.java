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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.sellmanager.model.Company;
import com.cmcc.zysoft.sellmanager.model.Role;
import com.cmcc.zysoft.spring.security.model.User;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.cmcc.zysoft.sysmanage.service.CompanyService;
import com.cmcc.zysoft.sysmanage.service.RoleService;
import com.starit.common.dao.support.Pagination;

/**
 * RoleController.java
 * @author zhangweihua
 * @email zhang.weihua@ustcinfo.com
 * @date 2012-12-3 上午3:01:31
 *
 */
@Controller
@RequestMapping("/pc/role")
public class RoleController extends BaseController {
	
	private static Logger _logger = LoggerFactory.getLogger(RoleController.class);
	
	@Resource
	private RoleService roleService;
	@Resource
	private CompanyService companyService;

	/**
	 * 根据公司id获取角色下拉框
	 * @author yandou
	 * @param companyId 公司id
	 * @return
	 */
	@RequestMapping("/roleCombo/{companyId}.htm")
	@ResponseBody
	public List<Map<String,Object>> roleCombo(@PathVariable String companyId){
		_logger.debug("根据公司id获取角色下拉框");
		return this.roleService.roleCombo(companyId);
	}
	
	/**
	 * 跳转至角色信息首页
	 * 
	 * @return
	 */
	@RequestMapping(value = "/main.htm")
	public String role(ModelMap modelMap) {
		User user = SecurityContextUtil.getCurrentUser();
		modelMap.addAttribute("roleCode", user.getRoles().get(0).getRoleCode().toString());
		_logger.debug("跳转至角色信息首页");
		return "sysmanage/role/role";
	}

	/**
	 * 获取所有角色信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/all.htm", method = RequestMethod.POST)
	@ResponseBody
	public Pagination<Object> getAllRoles(int page,int rows) {
		_logger.debug("获取所有角色信息");
		String companyId = SecurityContextUtil.getCompanyId();
		return this.roleService.getAllRoles(page,rows,companyId);
	}
	
	/**
	 * 根据查询条件获取所有角色信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/allByCondition.htm", method = RequestMethod.POST)
	@ResponseBody
	public Pagination<Object> getRolesByCondition(@RequestParam("roleName") String roleName,int page,int rows) {
		_logger.debug("根据查询条件获取所有角色信息");
		String companyId = "0";	//管理员，若不是companyId=登录人员公司id
		return this.roleService.getRolesByCondition(roleName,page,rows,companyId);
	}

	/**
	 * 跳转至角色添加页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/add.htm")
	public String add(ModelMap modelMap) {
		_logger.debug("跳转至角色添加页面");
		User user = SecurityContextUtil.getCurrentUser();
		modelMap.addAttribute("roleCodeForAdmin", user.getRoles().get(0).getRoleCode().toString());
		return "sysmanage/role/addRole";
	}

	/**
	 * 添加角色信息
	 * 
	 * @param role 角色对象
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/save.htm", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public String saveRole(Role role, HttpServletRequest request) {
		_logger.debug("添加角色信息");
		try{
			String companyId = SecurityContextUtil.getCompanyId();
			Company company = companyService.getEntity(companyId);
			role.setCompany(company);
			this.roleService.saveRole(role);
		}catch(Exception e){
			_logger.error("添加角色信息失败");
			e.printStackTrace();
			return "0";
		}
		return "1";
	}

	/**
	 * 跳转至修改角色信息页面
	 * 
	 * @param roleId 角色id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/edit.htm")
	public String update(@RequestParam("roleId") String roleId, Model model,ModelMap modelMap) {
		_logger.debug("跳转至修改角色信息页面");
		Role role = new Role();
		role = this.roleService.getRole(roleId);
		model.addAttribute("role", role);
		String isAdmin = "isAdmin";	//管理员
		modelMap.addAttribute("admin", isAdmin);
		return "sysmanage/role/editRole";
	}
	
	
	/**
	 * 修改角色信息
	 * 
	 * @param role 角色对象
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/editRole.htm", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public String updateRole(Role role ,HttpServletRequest request) {
		_logger.debug("修改角色信息");
		try {
			//登录用户公司id
			Company company = new Company();
			company.setCompanyId(role.getCompany().getCompanyId());
			role.setCompany(company);
			this.roleService.updateRole(role);
		} catch (Exception e) {
			_logger.error("修改角色信息失败");
			return "0";
		}
		return "1";
	}
	
	/**
	 * 删除角色信息
	 * 
	 * @param roles 角色信息id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/delRole.htm", method = RequestMethod.GET)
	@ResponseBody
	@Transactional
	public String deleteHeadship(@RequestParam("roles") String roles ,HttpServletRequest request) {
		_logger.debug("删除角色信息");
		try {
			  String[] roleIds = roles.split(",");
				for(int j=0;j<roleIds.length;j++){
					this.roleService.deleteRole(roleIds[j]);
				}
		} catch (Exception e) {
			_logger.error("删除角色信息失败");
			return "0";
		}
		return "1";
	}
	
	/**
	 * combortree 获取角色类型树
	 * @author yandou
	 * @param companyId
	 * @return
	 */
	@RequestMapping("roleTree.htm")
	@ResponseBody
	public List<Map<String,Object>> roleTree(){
		_logger.debug("获取角色类型");
		List<Map<String, Object>> list = this.roleService.roleTypeCombo();
		User user = SecurityContextUtil.getCurrentUser();
		String roleCode = user.getRoles().get(0).getRoleCode().toString();
		if(roleCode.equals("0")){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", "0");
			map.put("text", "运营商管理员");
			list.add(map);
		}
		return list;
	}
}
