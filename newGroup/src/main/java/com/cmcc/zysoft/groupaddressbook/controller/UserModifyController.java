// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.zysoft.groupaddressbook.model.Search;
import com.cmcc.zysoft.groupaddressbook.service.AuditConfigService;
import com.cmcc.zysoft.groupaddressbook.service.UserModifyService;
import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.sellmanager.model.Company;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.starit.common.dao.support.Pagination;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：UserModifyController
 * <br />版本:1.0.0
 * <br />日期： 2013-5-29 上午11:51:51
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Controller
@RequestMapping("/pc/userModify")
public class UserModifyController extends BaseController{
	
	@Resource
	private UserModifyService userModifyService;
	
	@Resource
	private AuditConfigService auditConfigService;
	
	/**
	 * 跳转到审核设置页面.
	 * @param model
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping("/main.htm")
	public String main(Model model){
		this.auditConfigService.checkList();
		List<Map<String, Object>> list = this.auditConfigService.list();
		model.addAttribute("num", list.size());
		return "web/audit";
	}
	
	/**
	 * 跳转到审核列表.
	 * @param modelMap
	 * @param search
	 * @param companyId
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping("list.htm")
	public String main(ModelMap modelMap,HttpServletRequest request,Search search,String companyId){
		Company company;
		if("".equals(companyId) || null == companyId){
			if(request.getSession().getAttribute("selCompany")!=null)
			{
				company = (Company)request.getSession().getAttribute("selCompany");
				companyId = company.getCompanyId();
			}
			else{
				companyId = SecurityContextUtil.getCompanyId();
			}
		}
		
		Pagination<?> pagination = this.userModifyService.getModifyInfo(8, search.getPageNo(), companyId);
		modelMap.addAttribute("pagination", pagination);
		return "web/modify";
	}
	
	/**
	 * 修改审核配置表.
	 * @param value
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping("/save.htm")
	@ResponseBody
	public String main(String value){
		this.auditConfigService.updateAudit(value);
		return "SUCCESS";
	}
	
	/**
	 * 审核详情.
	 * @param userModifyId
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	@RequestMapping("/get.htm")
	@ResponseBody
	public List<Map<String, Object>> list(String userModifyId){
		return this.userModifyService.list(userModifyId);
	}
	
	/**
	 * 审核.
	 * @param userModifyId
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping("/saveModify.htm")
	@Transactional
	@ResponseBody
	public String saveModify(String userModifyId){
		return this.userModifyService.saveModify(userModifyId);
	}
	
	/**
	 * 拒绝审核.
	 * @param userModifyId
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping("/refuseModify.htm")
	@Transactional
	@ResponseBody
	public String refuseModify(String userModifyId){
		return this.userModifyService.refuseModify(userModifyId);
	}
	/**
	 * 跳转到审核页面
	 * @param userModifyId
	 * @return
	 */
	@RequestMapping("/toModifyAudit.htm")
	public String toModifyAudit(ModelMap modelmap,String userModifyId){
		modelmap.addAttribute("userModifyId", userModifyId);
		return "web/modifyAudit";
	}

}
