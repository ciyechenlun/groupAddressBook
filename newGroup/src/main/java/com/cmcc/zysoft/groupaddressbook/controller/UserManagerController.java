// ~ CopyRight © 2014 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.zysoft.groupaddressbook.model.Search;
import com.cmcc.zysoft.groupaddressbook.service.UserCompanyService;
import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.sellmanager.model.Company;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
/**
 * @author 李雨辰
 * <br />邮箱：li.yuchen@ustcinfo.com
 * <br />描述：UserManagerController
 * <br />版本:1.0.0
 * <br />日期： 2014-05-07 上午11:36:50
 * <br />CopyRight © 2014 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Controller
@RequestMapping("pc/userManager")
public class UserManagerController extends BaseController{
	
	@Resource
	private UserCompanyService userCompanyService;
	/**
	 * 跳转至成员管理页面 初始化页面信息
	 * @param modelMap
	 * @param search
	 * @param request
	 * @return
	 */
	@RequestMapping("/main.htm")
	public String toUserManager(ModelMap modelMap,Search search,HttpServletRequest request){
		String companyId = SecurityContextUtil.getCompanyId();
		if(request.getSession().getAttribute("selCompany")!=null)
		{
			Company company = (Company)request.getSession().getAttribute("selCompany");
			companyId = company.getCompanyId();
		}
		//获取已被禁用的人员信息
		List<Map<String,Object>> list = this.userCompanyService.getFrobiddenInfo(companyId);
		modelMap.put("forbiddenMember", list);
		return "web/userManager";
	}
	/**
	 * 保存信息
	 * @param forbiddenMember 需要被禁用成员的userCompanyId
	 * @param companyId 企业Id
	 * @return
	 */
	@RequestMapping("/saveInfo.htm")
	@ResponseBody
	public boolean saveInfo(String forbiddenMember,Search search,HttpServletRequest request){
		String companyId = SecurityContextUtil.getCompanyId();
		if(request.getSession().getAttribute("selCompany")!=null)
		{
			Company company = (Company)request.getSession().getAttribute("selCompany");
			companyId = company.getCompanyId();
		}
		try {
			this.userCompanyService.saveInfo(forbiddenMember, companyId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
