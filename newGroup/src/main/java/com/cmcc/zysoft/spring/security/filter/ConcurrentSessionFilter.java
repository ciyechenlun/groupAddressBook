// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.spring.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import com.cmcc.zysoft.framework.utils.AjaxUtil;
import com.cmcc.zysoft.spring.security.ForwardRedirectStrategy;
import com.cmcc.zysoft.spring.security.model.User;
import com.cmcc.zysoft.spring.security.util.Constant;

/**
 * @author 李三来 <br />
 *         邮箱： li.sanlai@ustcinfo.com <br />
 *         描述：Session并发控制过滤器 <br />
 *         版本:1.0.0 <br />
 *         日期： 2013-1-12 下午10:01:27 <br />
 *         CopyRight © 2012 USTC SINOVATE SOFTWARE CO.LTD All Rights Reserved.
 */
public class ConcurrentSessionFilter extends GenericFilterBean {

	// ~ Instance fields
	// ================================================================================================
	
//	private static final Logger logger = LoggerFactory.getLogger(ConcurrentSessionFilter.class);

	private SessionRegistry sessionRegistry;
	private String expiredUrl;
	private LogoutHandler[] handlers = new LogoutHandler[] { new SecurityContextLogoutHandler() };
	private RedirectStrategy redirectStrategy = new ForwardRedirectStrategy();

	// ~ Methods
	// ========================================================================================================

	/**
	 * 构造方法
	 */
	public ConcurrentSessionFilter() {
	}

	public ConcurrentSessionFilter(SessionRegistry sessionRegistry) {
		this(sessionRegistry, null);
	}

	public ConcurrentSessionFilter(SessionRegistry sessionRegistry,
			String expiredUrl) {
		this.sessionRegistry = sessionRegistry;
		this.expiredUrl = expiredUrl;
	}

	@Override
	public void afterPropertiesSet() {
		Assert.notNull(sessionRegistry, "SessionRegistry required");
		Assert.isTrue(
				expiredUrl == null || UrlUtils.isValidRedirectUrl(expiredUrl),
				expiredUrl + " isn't a valid redirect URL");
	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		HttpSession session = request.getSession(false);

		if (session != null) {
			SessionInformation info = sessionRegistry
					.getSessionInformation(session.getId());

			if (info != null) {
				if (info.isExpired()) {
					// Expired - abort processing
					doLogout(request, response);

					String targetUrl = determineExpiredUrl(request, info);

					if (targetUrl != null) {
						if (!AjaxUtil.isAjaxRequest(request)){
							request.setAttribute(Constant.ERROR_MESSAGE, "用户" +((User)info.getPrincipal()).getUsername() + "已经在别处登录！");
							redirectStrategy.sendRedirect(request, response,targetUrl);
						}else {
							// 在响应头设置session状态
							response.setStatus(HttpStatus.UNAUTHORIZED.value());
						}
						return;
						
					} else {
						response.getWriter()
								.print("This session has been expired (possibly due to multiple concurrent "
										+ "logins being attempted as the same user).");
						response.flushBuffer();
					}

					return;
				} else {
					// Non-expired - update last request date/time
					sessionRegistry.refreshLastRequest(info.getSessionId());
				}
			}
		}

		chain.doFilter(request, response);
	}

	protected String determineExpiredUrl(HttpServletRequest request,
			SessionInformation info) {
		return expiredUrl;
	}

	private void doLogout(HttpServletRequest request,
			HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();

		for (LogoutHandler handler : handlers) {
			handler.logout(request, response, auth);
		}
	}

	/**
	 * @deprecated use constructor injection instead
	 */
	@Deprecated
	public void setExpiredUrl(String expiredUrl) {
		this.expiredUrl = expiredUrl;
	}

	/**
	 * @deprecated use constructor injection instead
	 */
	@Deprecated
	public void setSessionRegistry(SessionRegistry sessionRegistry) {
		this.sessionRegistry = sessionRegistry;
	}

	public void setLogoutHandlers(LogoutHandler[] handlers) {
		Assert.notNull(handlers);
		this.handlers = handlers;
	}

	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}
}
