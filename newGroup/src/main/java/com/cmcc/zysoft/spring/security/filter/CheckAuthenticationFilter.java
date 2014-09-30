// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.spring.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.web.filter.GenericFilterBean;

import com.cmcc.zysoft.framework.utils.AjaxUtil;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述：授权被挤出的时候的拦截器
 * <br />版本:1.0.0
 * <br />日期： 2013-1-12 下午6:21:41
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
public class CheckAuthenticationFilter extends GenericFilterBean {
	
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(CheckAuthenticationFilter.class);

	/**
	 * <p>要跳转到哪个页面的url</p>
	 * 默认跳转到"/"
	 */
	private String targetUrl = "/";
	
	/**
	 * 页面转发器
	 */
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	/**
	 * 构造方法
	 */
	public CheckAuthenticationFilter() {}
	

	/**
	 * 构造方法
	 * @param targetUrl 默认跳转到"/"
	 */
	public CheckAuthenticationFilter(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	/**
	 * 构造方法
	 * @param targetUrl 默认跳转到"/"
	 * @param redirectStrategy 页面转发器
	 */
	public CheckAuthenticationFilter(String targetUrl,
			RedirectStrategy redirectStrategy) {
		this.targetUrl = targetUrl;
		this.redirectStrategy = redirectStrategy;
	}

	/**
	 * 过滤操作
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		
		logger.debug("#检查当前的授权信息是否存在");
		//获取当前的授权信息
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth==null){
			logger.debug("#当前没有授权信息,跳转到 {}",targetUrl);
			if (!AjaxUtil.isAjaxRequest(req)){
				redirectStrategy.sendRedirect(req, res, targetUrl);
			}else {
				// 在响应头设置session状态
				res.setStatus(HttpStatus.UNAUTHORIZED.value());
			}
		}else{
			chain.doFilter(request, response);
		}
	}


	/**
	 * 返回targetUrl
	 * @return the targetUrl
	 */
	public String getTargetUrl() {
		return targetUrl;
	}


	/**
	 * 设置targetUrl
	 * @param targetUrl the targetUrl to set
	 */
	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}


	/**
	 * 返回redirectStrategy
	 * @return the redirectStrategy
	 */
	public RedirectStrategy getRedirectStrategy() {
		return redirectStrategy;
	}


	/**
	 * 设置redirectStrategy
	 * @param redirectStrategy the redirectStrategy to set
	 */
	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}

}
