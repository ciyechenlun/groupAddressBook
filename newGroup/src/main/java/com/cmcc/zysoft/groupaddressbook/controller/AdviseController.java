// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.zysoft.groupaddressbook.model.Search;
import com.cmcc.zysoft.groupaddressbook.service.AdviseService;
import com.cmcc.zysoft.groupaddressbook.service.PCCompanyService;
import com.cmcc.zysoft.groupaddressbook.service.UserService;
import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.sellmanager.model.Company;
import com.cmcc.zysoft.sellmanager.model.Role;
import com.cmcc.zysoft.spring.security.model.User;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.starit.common.dao.support.Pagination;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：AdviseController
 * <br />版本:1.0.0
 * <br />日期： 2013-4-10 上午10:25:32
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Controller
@RequestMapping("/pc/advise")
public class AdviseController extends BaseController{
	
	@Resource
	private AdviseService adviseService;
	
	@Resource
	private UserService userService;
	
	
	
	/**
	 * 跳转到查看建议反馈页面.
	 * @param modelMap 
	 * @param search 
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping(value="/main.htm") 
	public String getAdvise(ModelMap modelMap,Search search,HttpServletRequest request){
		User user = SecurityContextUtil.getCurrentUser();
		List<Role> roles = user.getRoles();
		String manager = this.userService.isManager(user);
		boolean isAdmin = false;
		boolean isDepartmentAdmin = false;
		for(Role r : roles)
		{
			if("1".equals(r.getRoleId()) || "2".equals(r.getRoleId())
					|| "3".equals(r.getRoleId()) || "4".equals(r.getRoleId())
					|| "5".equals(r.getRoleId()))
			{
				isAdmin = true;
				break;
			}
			else if("6".equals(r.getRoleId()))
			{
				isDepartmentAdmin = true;
			}
		}
		
		if(isDepartmentAdmin)
		{
			manager = "2";
		}
		//如果角色里有roleId=1，则为系统管理员
		if(isAdmin)
		{
			manager = "1";
		}
		
		String flag = "0";
		for(Role r:roles){
			String roleId = r.getRoleId();
			//0：超级管理员，1：管理员，3：ICT管理员
			if("0".equals(roleId)||"1".equals(roleId)||"3".equals(roleId)){
				flag = "1";
				break;
			}
		}
		//top中企业选择信息加载
		Company	company = (Company)request.getSession().getAttribute("selCompany");
		if(null != company){
			modelMap.addAttribute("companyId",company.getCompanyId());
			modelMap.addAttribute("companyName",company.getCompanyName());
		}
		//使用反馈权限
		modelMap.addAttribute("flag", flag);
		//编辑企业信息权限
		modelMap.addAttribute("manager", manager);
		//将当前用户信息也送到客户端，以做权限控制
		modelMap.addAttribute("user", user);
		String companyIdM = SecurityContextUtil.getCompanyId();
		//获取反馈信息
		Pagination<?> pagination = this.adviseService.adviseList(8, search.getPageNo(), companyIdM);
		modelMap.addAttribute("pagination", pagination);
		return "web/advise";
	}
	
	/**
	 * 删除建议.
	 * @param adviseId 
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping(value="/delete.htm")
	@Transactional
	@ResponseBody
	public String delete(String adviseId){
		try{
			this.adviseService.deleteEntity(adviseId);
			return "SUCCESS";
		}catch (Exception e){
			return "ERROR";
		}
		
	}

}
