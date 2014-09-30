// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.spring.security.controller;

import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Properties;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cmcc.zysoft.framework.common.support.ResponseData;
import com.cmcc.zysoft.framework.utils.StringUtil;
import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.sellmanager.model.Company;
import com.cmcc.zysoft.sellmanager.model.SystemUser;
import com.cmcc.zysoft.sellmanager.util.CookieUtil;
import com.cmcc.zysoft.sellmanager.util.NetworkUtil;
import com.cmcc.zysoft.spring.security.capatch.BadCapatchCodeException;
import com.cmcc.zysoft.spring.security.model.User;
import com.cmcc.zysoft.spring.security.util.Constant;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.cmcc.zysoft.sysmanage.service.CompanyService;
import com.cmcc.zysoft.sysmanage.service.SystemUserPCService;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述：安全认证和授权控制器
 * <br />版本:1.0.0
 * <br />日期： 2013-1-10 下午9:09:32
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Controller
@RequestMapping("/security")
public class SecurityController extends BaseController {
	
	//~fields-------------------------------
	
	/**
	 * 用户名请求参数
	 */
	private static final String J_USERNAME = "j_username";
	
	/**
	 * 支持尝试登录错误次数
	 */
	private static final int MAX_FAIL_COUNT = 5;

	/**
	 * 异常key
	 */
	public static final String SPRING_SECURITY_LAST_EXCEPTION_KEY = WebAttributes.AUTHENTICATION_EXCEPTION;
	
	/**
	 * 验证码生成器
	 */
	private DefaultKaptcha captchaProducer = null;

	/**
	 * 生成验证码的字符集合
	 */
	private static final String PRODUCER_TEXT_CHAR_STRING = "0123456789";
	
	/**
	 * 无权限返回页面
	 */
	private final String ACCESS_DENIED_VIEW = "403";
	
	/**
	 * 属性名称：systemUserService 类型：SystemUserPCService
	 */
	@Resource
	private SystemUserPCService systemUserPCService;
	
	/**
	 * 属性名称：companyService <br/>
	 * 类型：CompanyService
	 */
	@Resource
	private CompanyService companyService;
	
	//~methods-------------------------------
	
	/**
	 * 构造方法
	 */
	public SecurityController() {
		captchaProducer = new DefaultKaptcha();
		Properties properties = new Properties();
		properties.put("kaptcha.border", "no");
		properties.put("kaptcha.border.color", "105,179,90");
		properties.put("kaptcha.textproducer.char.length", "4");
		properties.put("kaptcha.image.width", "90");
		properties.put("kaptcha.image.height", "40");
		properties.put("kaptcha.session.key", Constants.KAPTCHA_SESSION_KEY);
		properties.put("kaptcha.textproducer.font.size", "32");
		properties.put("kaptcha.textproducer.char.string", PRODUCER_TEXT_CHAR_STRING);
		properties.put("kaptcha.noise.impl", "com.google.code.kaptcha.impl.DefaultNoise");
		properties.put("kaptcha.background.clear.from", "236,214,11");
		properties.put("kaptcha.background.clear.to", "127,65,175");
		properties.put("kaptcha.noise.color", "204,204,255");
		Config config = new Config(properties);
		captchaProducer.setConfig(config);
	}
	
	/**
	 * Spring Security认证成功后，继续调用该方法
	 * 
	 * @return
	 */
	@RequestMapping("/loginSuccess")
	@ResponseBody
	public ResponseData loginSuccess(HttpServletRequest request,HttpServletResponse response, HttpSession httpSession) {
		//额外的处理逻辑增强：如果登录以后的用户的公司ID和保存在session里面的公司ID不一致，视为登录失败
		Company sessionCompany = httpSession.getAttribute(Constant.SESSION_COMPANY)==null?null:(Company)httpSession.getAttribute(Constant.SESSION_COMPANY);
		String loginUrl = CookieUtil.getCookieValue(request, Constant.SESSION_LOGINURL);
		sessionCompany = sessionCompany == null?companyService.getCompanyByUrl(loginUrl):sessionCompany;
		User user = SecurityContextUtil.getCurrentUser();
		httpSession.removeAttribute("loginFailureMustValCode");
		//登录成功之后在cookie里面放一个登录地址，以便注销的时候方便知道跳转到那个登录地址
		CookieUtil.addCookie(response, Constant.SESSION_LOGINURL, (String)httpSession.getAttribute(Constant.SESSION_LOGINURL),-1);
		
		String systemUserName = StringUtil.isNullOrEmpty(user.getRealName())?user.getUsername():user.getRealName();
		httpSession.setAttribute(Constant.SESSION_USER_NAME, systemUserName);
		
		//登录成功以后修改数据库中用户的登录信息
		SystemUser systemUser = systemUserPCService.getEntity(user.getUserId());
		if(systemUser!=null){
			String loginIp = NetworkUtil.getIpAddr(request);
			Date loginTime = new Date();
			Long loginCount = (systemUser.getLoginCount()==null?0:systemUser.getLoginCount())+1;
			systemUser.setLoginIp(loginIp);
			systemUser.setLoginTime(loginTime);
			systemUser.setLoginCount(loginCount);
			systemUserPCService.updateEntity(systemUser);
		} 
		
		return ResponseData.SUCCESS_NO_DATA;
		
	}

	/**
	 * Spring Security认证失败后，继续调用该方法
	 * 
	 * @return
	 */
	@RequestMapping("/loginFailure")
	@ResponseBody
	public ResponseData loginFailure(HttpServletRequest request, HttpSession httpSession) {
		httpSession.setAttribute("loginFailureMustValCode", "yes");
		AuthenticationException failed = (AuthenticationException) request.getAttribute(SPRING_SECURITY_LAST_EXCEPTION_KEY);
		//登录失败的时候，把登录名称放到ResponseData的message字段，方便aop日志组件取得登录人员信息
		String username = request.getParameter(J_USERNAME);
		
		ResponseData responseData;
		if(failed != null){
			if("noAuthority".equals(failed.getCause()==null?"":failed.getCause().getMessage())){
				responseData = new ResponseData(false, "NoAuthority",username);
				return responseData;
			}else if("userlock".equals(failed.getCause()==null?"":failed.getCause().getMessage())){
				responseData = new ResponseData(false, "userlock",username);
				return responseData;
			}
		}

		if (failed instanceof UsernameNotFoundException) {
			return new ResponseData(false, "UsernameNotFound",username);
		}else if (failed instanceof BadCredentialsException) {
			int count=1;
			Object attr = httpSession.getAttribute(username+"_fail");
			if(attr !=null){
				count = (int)httpSession.getAttribute(username+"_fail");
				if(count>=MAX_FAIL_COUNT){
					this.systemUserPCService.lockUser(username);
				}
			}
			httpSession.setAttribute(username+"_fail", count+1);
			return new ResponseData(false, "BadCredentials_"+count,username);
		} else if (failed instanceof BadCapatchCodeException) {
			return new ResponseData(false, "BadCapatch",username);
		} else {
			return ResponseData.FAILED_NO_DATA;
		}
	}
	
	/**
	 * 注销处理器
	 * @param request
	 * @param httpSession
	 * @return
	 */
	@RequestMapping("/logoutHandler")
	public String logoutHandler(HttpServletRequest request, HttpSession httpSession){
		
		String loginUrl = CookieUtil.getCookieValue(request, Constant.SESSION_LOGINURL);
		if(!"/".equals(loginUrl)&&!StringUtil.isNullOrEmpty(loginUrl)){
			return "redirect:/login/"+loginUrl;
		}else{
			return "redirect:/login.htm";
		}
	}
	
	/**
	 * session失效处理器
	 * @param request
	 * @param httpSession
	 * @return
	 */
	@RequestMapping("/sessionTimeoutHandler")
	public String sessionTimeoutHandler(HttpServletRequest request, HttpSession httpSession){
		String loginUrl = CookieUtil.getCookieValue(request, Constant.SESSION_LOGINURL);
		if(!"/".equals(loginUrl)&&!StringUtil.isNullOrEmpty(loginUrl)){
			return "redirect:/login/"+loginUrl+"?errorCode=401";
		}else{
			return "redirect:/login.htm?errorCode=401";
		}
		
	}
	
	/**
	 * 鉴权失败处理
	 * @param model
	 * @param request
	 */
	@RequestMapping("/accessDenied")
	public ModelAndView accessDenied(HttpServletRequest request) { 
	    AccessDeniedException ex = (AccessDeniedException)request.getAttribute(WebAttributes.ACCESS_DENIED_403); 
	    ModelAndView modelAndView = new ModelAndView(ACCESS_DENIED_VIEW);
	    StringWriter sw = new StringWriter(); 
	    modelAndView.addObject("errorDetails", ex.getMessage()); 
	    ex.printStackTrace(new PrintWriter(sw)); 
	    modelAndView.addObject("errorTrace", sw.toString()); 
	    return modelAndView;
	} 
	
	/**
	 * 生成验证码
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/securityCode")
	public void securityCode(HttpServletRequest request,HttpServletResponse response) throws Exception {
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control","no-store, no-cache, must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		response.setContentType("image/jpeg");

		String capText = captchaProducer.createText();
		request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);

		BufferedImage bi = captchaProducer.createImage(capText);
		ServletOutputStream out = response.getOutputStream();

		ImageIO.write(bi, "jpg", out);
		try {
			out.flush();
		} finally {
			out.close();
		}
	}
	
}
