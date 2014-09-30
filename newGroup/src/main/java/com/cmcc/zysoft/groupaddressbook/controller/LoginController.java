// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cmcc.zysoft.framework.utils.StringUtil;
import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.sellmanager.model.Company;
import com.cmcc.zysoft.spring.security.util.Constant;
import com.cmcc.zysoft.sysmanage.service.CompanyService;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述：登录控制器
 * <br />版本:1.0.0
 * <br />日期： 2013-1-13 上午10:29:08
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Controller
@RequestMapping("/")
public class LoginController extends BaseController {
	
	//~ fields------------------------------------------------------
	
	/**
	 * 登录页面
	 */
	private static final String LOGIN_VIEW = "login";
	
	private static final String PLOGIN_VIEW = "/pedmeter/mLogin.htm";
	
	/**
	 * 404页面
	 */
	private static final String NO_PAGE_VIEW = "404";
	
	/**
	 * 属性名称：companyService 类型：CompanyService
	 */
	@Resource
	private CompanyService companyService;
	
	
	//~ methods------------------------------------------------------
	
	/**
	 * 跳转到登录页面
	 * @return
	 */
	@RequestMapping("/login.htm")
	public String loginViewFirst(HttpServletRequest request, String errorCode,String to,ModelMap modelMap){
		if(!StringUtil.isNullOrEmpty(errorCode) 
				&& !StringUtil.isNullOrEmpty(Constant.ERROR_MESSAGE_MAP.get(errorCode))){
			request.setAttribute(Constant.ERROR_MESSAGE, Constant.ERROR_MESSAGE_MAP.get(errorCode));
		}
		if (StringUtils.hasText(to)){
			modelMap.addAttribute("to", to);
		}
		else
		{
			modelMap.addAttribute("to", "");
		}
		return LOGIN_VIEW;
	}
	
	/**
	 * 跳转到登录页面
	 * @return
	 */
	@RequestMapping("/")
	public String loginViewSecond(HttpServletRequest request,String errorCode){
		if(!StringUtil.isNullOrEmpty(errorCode) 
				&& !StringUtil.isNullOrEmpty(Constant.ERROR_MESSAGE_MAP.get(errorCode))){
			request.setAttribute(Constant.ERROR_MESSAGE, Constant.ERROR_MESSAGE_MAP.get(errorCode));
		}
		return LOGIN_VIEW;
	}
	
	/**
	 * 跳转到登录页面
	 * @return
	 */
	@RequestMapping("/login")
	public String loginViewThird(HttpServletRequest request,String errorCode){
		if(!StringUtil.isNullOrEmpty(errorCode) 
				&& !StringUtil.isNullOrEmpty(Constant.ERROR_MESSAGE_MAP.get(errorCode))){
			request.setAttribute(Constant.ERROR_MESSAGE, Constant.ERROR_MESSAGE_MAP.get(errorCode));
		}
		return LOGIN_VIEW;
	}
	
	@RequestMapping("/login/test.htm")
	public String String(String company_id,String company_name)
	{
		this.companyService.test(company_id, company_name);
		return "test";
	}
	
	/**
	 * 计步器登录界面
	 * @param request
	 * @param errorCode
	 * @return
	 */
	@RequestMapping("/plogin.htm")
	public String loginPed(HttpServletRequest request,String errorCode)
	{
		return LOGIN_VIEW;
	}
	
	/**
	 * 跳转到不通公司的登录页面
	 * @return
	 */
	@RequestMapping("/login/{loginUrl}")
	public String loginViewByCompany(HttpServletRequest request,@PathVariable("loginUrl") String loginUrl,String errorCode){
		//查看地址是否带有errorCode=401参数
		if(!StringUtil.isNullOrEmpty(errorCode) 
				&& !StringUtil.isNullOrEmpty(Constant.ERROR_MESSAGE_MAP.get(errorCode))){
			request.setAttribute(Constant.ERROR_MESSAGE, Constant.ERROR_MESSAGE_MAP.get(errorCode));
		}
		//如果登录的时候带了后缀，去匹配后缀对应的公司登录地址
		if(!StringUtil.isNullOrEmpty(loginUrl)){
			Company company = companyService.getCompanyByUrl(loginUrl);
			//如果通过登录地址找不到公司，跳转到404页面
			if(company==null){
				return NO_PAGE_VIEW;
			}else{
				//如果有公司和响应的登录地址匹配，将公司信息放到session
				request.getSession().setAttribute(Constant.SESSION_COMPANY, company);
				request.getSession().setAttribute(Constant.SESSION_COMPANY_LOGO, company.getIndexLogo());
				request.getSession().setAttribute(Constant.SESSION_LOGINURL, company.getLoginUrl());
				return LOGIN_VIEW;
			}
		}
		//如果没有后缀，跳到默认的登录页面，这个登录页面默认是属于移动公司的，只有移动公司的用户才能登录,
		//安徽移动公司的登录地址只能是"/"
		else{
			Company company = companyService.getCompanyByUrl("/");
			request.getSession().setAttribute(Constant.SESSION_COMPANY, company);
			request.getSession().setAttribute(Constant.SESSION_COMPANY_LOGO, company.getIndexLogo());
			request.getSession().setAttribute(Constant.SESSION_LOGINURL, company.getLoginUrl());
			return LOGIN_VIEW;
		}
	}
	
}
