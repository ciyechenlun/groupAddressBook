// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.spring.security.capatch;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.google.code.kaptcha.Constants;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述：验证用户名、密码和验证码的校验过滤器
 * <br />版本:1.0.0
 * <br />日期： 2013-1-10 下午8:41:03
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
public class ValidateCodeUsernamePasswordAuthenticationFilter extends
		UsernamePasswordAuthenticationFilter {

	/**
	 * 验证码在session中的key
	 */
	public static final String DEFAULT_VALIDATE_CODE_PARAMETER = "jcapatch_code";
	
	/**
	 * 提交是否只是post方式
	 */
	private boolean postOnly = true;
	
	/**
	 * 是否允许有验证码
	 */
	private boolean allowEmptyValidateCode = false;
	
	/**
	 * 验证码key值
	 */
	private String validateCodeParameter = DEFAULT_VALIDATE_CODE_PARAMETER;

	/**
	 * 鉴权
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {
		if (postOnly && !request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException(
					"Authentication method not supported: "
							+ request.getMethod());
		}
		
		HttpSession session = request.getSession(false);
		Object loginFailureMustValCode = session.getAttribute("loginFailureMustValCode");
		// check validate code
		if (!isAllowEmptyValidateCode() && loginFailureMustValCode != null && "yes".equals((String)loginFailureMustValCode))
			checkValidateCode(request, response);

		String username = obtainUsername(request);
		String password = obtainPassword(request);

		if (username == null) {
			username = "";
		}

		if (password == null) {
			password = "";
		}

		username = username.trim();

		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
				username, password);

		// Place the last username attempted into HttpSession for views
		/*HttpSession session = request.getSession(false);

		if (session != null || getAllowSessionCreation()) {
			request.getSession().setAttribute(
					SPRING_SECURITY_LAST_USERNAME_KEY,
					TextEscapeUtils.escapeEntities(username));
		}*/

		// Allow subclasses to set the "details" property
		setDetails(request, authRequest);
		return this.getAuthenticationManager().authenticate(authRequest);
	}

	/**
	 * 比较session中的验证码和用户输入的验证码是否相等
	 */
	protected void checkValidateCode(HttpServletRequest request, HttpServletResponse response) {
		String sessionValidateCode = obtainSessionValidateCode(request);
		String validateCodeParameter = obtainValidateCodeParameter(request);
		if (StringUtils.isEmpty(validateCodeParameter)
				|| !sessionValidateCode.equalsIgnoreCase(validateCodeParameter)) {
			try {
				unsuccessfulAuthentication(request, response, new BadCapatchCodeException("bad capatch code"));
				return;
			} catch (IOException e) {
				throw new RuntimeException(e);
			} catch (ServletException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * 获取验证码
	 * @param request
	 * @return
	 */
	private String obtainValidateCodeParameter(HttpServletRequest request) {
		return request.getParameter(validateCodeParameter);
	}

	/**
	 * 获取session里面的验证码
	 * @param request
	 * @return
	 */
	protected String obtainSessionValidateCode(HttpServletRequest request) {
		Object obj = request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
		return null == obj ? "" : obj.toString();
	}

	/**
	 * 返回postOnly
	 * @return
	 */
	public boolean isPostOnly() {
		return postOnly;
	}

	/**
	 * 设置postOnly
	 */
	@Override
	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}

	/**
	 * 返回allowEmptyValidateCode
	 * @return
	 */
	public boolean isAllowEmptyValidateCode() {
		return allowEmptyValidateCode;
	}

	/**
	 * 设置allowEmptyValidateCode
	 * @param allowEmptyValidateCode
	 */
	public void setAllowEmptyValidateCode(boolean allowEmptyValidateCode) {
		this.allowEmptyValidateCode = allowEmptyValidateCode;
	}

}
