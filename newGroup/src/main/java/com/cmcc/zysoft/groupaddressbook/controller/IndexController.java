// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.zysoft.groupaddressbook.model.Search;
import com.cmcc.zysoft.groupaddressbook.service.NoticeService;
import com.cmcc.zysoft.groupaddressbook.service.PCCompanyService;
import com.cmcc.zysoft.groupaddressbook.service.UserCompanyService;
import com.cmcc.zysoft.groupaddressbook.service.UserService;
import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.sellmanager.model.Company;
import com.cmcc.zysoft.sellmanager.model.Role;
import com.cmcc.zysoft.spring.security.model.User;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.starit.common.dao.support.Pagination;
/**
 * @author 李三来
 * @mail li.sanlai@ustcinfo.com
 * @date 2012-11-17 上午1:29:49
 */
@Controller
@RequestMapping("/")
public class IndexController extends BaseController {
	
	@Resource
	private PCCompanyService pcCompanyService;
	
	@Resource
	private UserCompanyService userCompanyService;
	
	@Resource
	private UserService userService;
	@Resource
	private NoticeService noticeService;
	/**
	 * 日志.
	 */
	private static Logger _logger = LoggerFactory.getLogger(IndexController.class);
	
	/**
	 * 跳转到首页.
	 * @param request 
	 * @return 
	 * 返回类型：ModelAndView
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("index.htm")
	public String index(ModelMap modelMap,Search search,String companyId,HttpServletRequest request){
		User user = SecurityContextUtil.getCurrentUser();
		List<Role> role = user.getRoles();
		String manager = "";
		boolean isAdmin = false;
		boolean isCompanyAdmin = false;
		for(Role r : role)
		{
			if("1".equals(r.getRoleId()) || "0".equals(r.getRoleId())
					|| "3".equals(r.getRoleId()) || "4".equals(r.getRoleId()))
			{
				isAdmin = true;
				break;
			}
			else if("6".equals(r.getRoleId())|| "5".equals(r.getRoleId()))
			{
				isCompanyAdmin = true;
			}
		}
		String flag = "0";
		for(Role r:role){
			String roleId = r.getRoleId();
			if("0".equals(roleId)||"1".equals(roleId)||"3".equals(roleId)){
				flag = "1";
				break;
			}
		}
		
	
		
		
		modelMap.addAttribute("flag", flag);
		Pagination<?>  org_list = this.pcCompanyService.orglist(18, search.getPageNo(), user.getEmployeeId(),"");//登录人所在企业通讯录
		modelMap.addAttribute("org_list", org_list);
		List<Map<String,Object>> reList = (List<Map<String,Object>>)org_list.getResult();
		List<Map<String,Object>> manlist = null;
		if(isCompanyAdmin && !isAdmin)
		{
			manlist = this.pcCompanyService.getManageCompany(user.getUserId());
			for (Map<String,Object> reMap : reList) {
				for (Map<String, Object> map : manlist) {
					if(getMapStr(reMap,"company_id").equals(getMapStr(map,"company_id"))){
						reMap.put("manage_flag", getMapStr(map,"manage_flag"));
					}
				}
			}
		}
		//如果角色里有roleId=1，则为系统管理员
		if(isAdmin)
		{
			manager = "1";
		}
		//将当前用户信息也送到客户端，以做权限控制
		modelMap.addAttribute("user", user);
		_logger.debug("#跳转到首页");
		Company company = null;
		if("".equals(companyId) || null == companyId){
			if(request.getSession().getAttribute("selCompany")!=null)
			{
				company = (Company)request.getSession().getAttribute("selCompany");
				companyId = company.getCompanyId();
			}
		}
		else
		{
			company = this.pcCompanyService.getEntity(companyId);
			request.getSession().setAttribute("selCompany", company);
		}
		if(null !=org_list && org_list.getResult().size()==1){
			companyId = ((Map<String,Object>)org_list.getResult().get(0)).get("company_id").toString();
			modelMap.addAttribute("companyId",companyId);
			modelMap.addAttribute("companyName",((Map<String,Object>)org_list.getResult().get(0)).get("company_name"));
			modelMap.addAttribute("pageTye","index");
			company = this.pcCompanyService.getEntity(companyId);
			request.getSession().setAttribute("selCompany", company);
			List<Map<String,Object>> list= this.noticeService.getPublicRoadList(user.getUserId(), company.getCompanyId());
			if(null!=list&&list.size()>0){
				modelMap.put("publicMan", true);
			}
			if(null != manlist){
				for (Map<String, Object> map : manlist) {
					if(company.getCompanyId().equals(getMapStr(map,"company_id"))){
						if("1".equals(getMapStr(map,"manage_flag"))){
							manager="1";
						}else if("3".equals(getMapStr(map,"manage_flag"))){
							manager="2";
						}
					}
				}
			}
			modelMap.addAttribute("manager", manager);
			return "index";
		}
		if(null != company){
			modelMap.addAttribute("company",company);
			modelMap.addAttribute("companyId",company.getCompanyId());
			modelMap.addAttribute("companyName",company.getCompanyName());
			modelMap.addAttribute("pageTye","index");
			List<Map<String,Object>> list= this.noticeService.getPublicRoadList(user.getUserId(), company.getCompanyId());
			if(null!=list&&list.size()>0){
				modelMap.put("publicMan", true);
			}
			if(null != manlist){
				for (Map<String, Object> map : manlist) {
					if(company.getCompanyId().equals(getMapStr(map,"company_id"))){
						if("1".equals(getMapStr(map,"manage_flag"))){
							manager="1";
						}else if("3".equals(getMapStr(map,"manage_flag"))){
							manager="2";
						}
					}
				}
			}
			modelMap.addAttribute("manager", manager);
			return "index";
		}
		modelMap.addAttribute("pageTye","companyM");	
		modelMap.addAttribute("manager", manager);
		/*Long allCount = this.pcCompanyService.getAllCompanyEmployee("");
		modelMap.addAttribute("allCount", allCount);*/
		return "companyM";
		
		
	}
	private String getMapStr(Map<String,Object> map,String str){
		if(null == map.get(str)){
			return "";
		}
		return map.get(str).toString();
	}
	/**
	 * 跳转到首页.
	 * @param request 
	 * @param key 搜索条件
	 * @return 
	 * 返回类型：ModelAndView
	 */
	@RequestMapping("toCompanyM.htm")
	public String toCompanyM(ModelMap modelMap,Search search,HttpServletRequest request,String key,Long allCount){
		if(null == key){
			key="";
		}
		User user = SecurityContextUtil.getCurrentUser();
		List<Role> role = user.getRoles();
		String manager = "";
		boolean isAdmin = false;
		boolean isCompanyAdmin = false;
		for(Role r : role)
		{
			if("1".equals(r.getRoleId()) || "0".equals(r.getRoleId())
					|| "3".equals(r.getRoleId()) || "4".equals(r.getRoleId()))
			{
				isAdmin = true;
				break;
			}
			else if("6".equals(r.getRoleId()) || "5".equals(r.getRoleId()))
			{
				isCompanyAdmin = true;
			}
		}

		String flag = "0";
		for(Role r:role){
			String roleId = r.getRoleId();
			if("0".equals(roleId)||"1".equals(roleId)||"3".equals(roleId)){
				flag = "1";
				break;
			}
		}
		
		modelMap.addAttribute("flag", flag);
		
		Pagination<?>  org_list = this.pcCompanyService.orglist(18, search.getPageNo(), user.getEmployeeId(),key);//登录人所在企业通讯录
		modelMap.addAttribute("org_list", org_list);
		List<Map<String,Object>> reList = (List<Map<String,Object>>)org_list.getResult();
		List<Map<String,Object>> manlist = null;
		if(isCompanyAdmin && !isAdmin)
		{
			manlist = this.pcCompanyService.getManageCompany(user.getUserId());
			for (Map<String,Object> reMap : reList) {
				for (Map<String, Object> map : manlist) {
					if(getMapStr(reMap,"company_id").equals(getMapStr(map,"company_id"))){
						reMap.put("manage_flag", getMapStr(map,"manage_flag"));
					}
				}
			}
		}
		//如果角色里有roleId=1，则为系统管理员
		if(isAdmin)
		{
			manager = "1";
		}
		
		//将当前用户信息也送到客户端，以做权限控制
		modelMap.addAttribute("user", user);
		Company	company = (Company)request.getSession().getAttribute("selCompany");
		if(null != company){
			modelMap.addAttribute("companyId",company.getCompanyId());
			modelMap.addAttribute("companyName",company.getCompanyName());
		}
		modelMap.addAttribute("pageTye","companyM");	
		modelMap.addAttribute("key", key);
		modelMap.addAttribute("manager", manager);
		return "companyM";
		
	}	

	@RequestMapping("orgAllList.htm")
	@ResponseBody
	public List<Map<String,Object>> orgAllList(HttpServletRequest request){
		//String companyId = SecurityContextUtil.getCompanyId();
		User user = SecurityContextUtil.getCurrentUser();
		List<Map<String,Object>> org_list = this.pcCompanyService.orgAllList(user.getEmployeeId());//登录人所在企业通讯录
		return org_list;
	}
	@RequestMapping("toChangePwd.htm")
	public String toChangePwd(){
		return "web/changePWD";
	}
}
