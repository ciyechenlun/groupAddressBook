// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.spring.security.session;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;

/**
 * @author 李三来 <br />
 *         邮箱： li.sanlai@ustcinfo.com <br />
 *         描述：Session验证失败处理策略 <br />
 *         版本:1.0.0 <br />
 *         日期： 2013-1-12 下午1:37:12 <br />
 *         CopyRight © 2012 USTC SINOVATE SOFTWARE CO.LTD All Rights Reserved.
 */
public class RedirectInvalidSessionStrategy implements InvalidSessionStrategy {

	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(RedirectInvalidSessionStrategy.class);
	
	/**
	 * session验证失败跳转的目标地址
	 */
	private final String destinationUrl;
	
	/**
	 * 默认的跳转策略
	 */
	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	/**
	 * 是否创建新session
	 */
	private boolean createNewSession = true;

	/**
	 * 构造方法
	 * @param invalidSessionUrl
	 */
	public RedirectInvalidSessionStrategy(String invalidSessionUrl) {
		Assert.isTrue(UrlUtils.isValidRedirectUrl(invalidSessionUrl),
				"url must start with '/' or with 'http(s)'");
		this.destinationUrl = invalidSessionUrl;
	}

	/**
	 * session验证失败事件处理方法
	 */
	public void onInvalidSessionDetected(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		logger.debug("Starting new session (if required) and redirecting to '"
				+ destinationUrl + "'");
		if (createNewSession) {
			request.getSession();
		}
		redirectStrategy.sendRedirect(request, response, destinationUrl);
	}

	/**
	 * Determines whether a new session should be created before redirecting (to
	 * avoid possible looping issues where the same session ID is sent with the
	 * redirected request). Alternatively, ensure that the configured URL does
	 * not pass through the {@code SessionManagementFilter}.
	 * 
	 * @param createNewSession
	 *            defaults to {@code true}.
	 */
	public void setCreateNewSession(boolean createNewSession) {
		this.createNewSession = createNewSession;
	}
}
