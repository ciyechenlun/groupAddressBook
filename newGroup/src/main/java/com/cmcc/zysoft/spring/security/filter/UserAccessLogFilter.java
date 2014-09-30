// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.spring.security.filter;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import com.alibaba.druid.util.JMXUtils;
import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.spring.security.jmx.URIStatManager;
import com.cmcc.zysoft.spring.security.support.HttpRequestManager;
import com.cmcc.zysoft.spring.security.support.HttpRequestStat;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;

/**
 * @author 李三来 <br />
 *         邮箱： li.sanlai@ustcinfo.com <br />
 *         描述：用户访问日志过滤器 <br />
 *         版本:1.0.0 <br />
 *         日期： 2013-1-10 下午7:20:08 <br />
 *         CopyRight © 2012 USTC SINOVATE SOFTWARE CO.LTD All Rights Reserved.
 */
public class UserAccessLogFilter extends GenericFilterBean {

	/**
	 * 日志
	 */
	private final static Logger LOGGER = LoggerFactory.getLogger(UserAccessLogFilter.class);

	/**
	 * mxBean
	 */
	private static final ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();

	/**
	 * 访问用户占位符
	 */
	public static final String PLACEHOLDER_USER_CODE = "$[userCode]";

	/**
	 * 客户端访问地址占位符
	 */
	public static final String PLACEHOLDER_REMOTE_ADDR = "$[remoteAddr]";

	/**
	 * 请求URI占位符
	 */
	public static final String PLACEHOLDER_REQUEST_URI = "$[requestURI]";

	/**
	 * 访问浏览器占位符
	 */
	public static final String PLACEHOLDER_USER_AGENT = "$[userAgent]";

	/**
	 * 请求时间占位符
	 */
	public static final String PLACEHOLDER_REQUEST_METHOD = "$[requestMethod]";

	/**
	 * CPU时间占位符
	 */
	public static final String PLACEHOLDER_CPU_TIME = "$[cpuTime]";

	/**
	 * 请求参数占位符
	 */
	public static final String PLACEHOLDER_REQUEST_PARAMETER = "$[requestParameter]";

	/**
	 * 请求时间占位符
	 */
	public static final String PLACEHOLDER_REQUEST_TIME = "$[requestTime]";

	/**
	 * 响应状态码
	 */
	public static final String PLACEHOLDER_RESPONSE_STATUS = "$[responseStatus]";

	/**
	 * Pattern被使用匹配占位符
	 */
	private static final Pattern PATTERN = Pattern.compile("\\$\\[\\p{Alpha}+\\]");

	/**
	 * 消息样式字符串
	 */
	private String message = "$[userCode] - $[remoteAddr] - [$[requestMethod], cpuTime:$[cpuTime], requestTime:$[requestTime], status:$[responseStatus]]$[requestURI] - [$[requestParameter]]";

	/**
	 * jmx开关
	 */
	private boolean isJmxOption;

	/**
	 * Http请求列表
	 */
	private ConcurrentMap<String, HttpRequestStat> httpRequests = HttpRequestManager.getInstance().getHttpRequests();

	/**
	 * 请求ID
	 */
	private final AtomicLong requestId = new AtomicLong();

	/**
	 * 初始化方法
	 */
	@PostConstruct
	public void init() {
		if (isJmxOption()) {
			JMXUtils.register("com.cmcc.zysoft.sellmanager:type=URIStat",URIStatManager.getInstance());

			LOGGER.info("启动jmx监控uri统计信息");
		}
	}

	/**
	 * 覆盖执行过滤操作
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		long startTime = System.nanoTime();

		chain.doFilter(request, response);

		if (SecurityContextUtil.getCurrentUser() == null)
			return;

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;

		String requestURI = httpServletRequest.getRequestURI();
		int index = requestURI.indexOf("?");
		if (index != -1)
			requestURI = requestURI.substring(0, index);

		replacePlaceholders(httpServletRequest, (HttpServletResponse) response,
				requestURI, startTime);

	}

	/**
	 * replacePlaceholders
	 * @param request
	 * @param response
	 * @param requestURI
	 * @param startTime
	 */
	protected void replacePlaceholders(HttpServletRequest request,
			HttpServletResponse response, String requestURI, long startTime) {
		String userCode = SecurityContextUtil.getCurrentUser().getUsername();
		String remoteAddr = SecurityContextUtil.getRemoteIP(request);
		String agent = request.getHeader("user-agent");
		String requestMethod = request.getMethod();

		long endTime = System.nanoTime();
		long requestTime = endTime - startTime;

		long cupTime = mxBean.getCurrentThreadCpuTime() / (1000 * 1000);

		StringBuffer output = new StringBuffer();
		Matcher matcher = PATTERN.matcher(message);

		while (matcher.find()) {
			String match = matcher.group();
			if (PLACEHOLDER_USER_CODE.equals(match)) {
				matcher.appendReplacement(output, userCode);
			} else if (PLACEHOLDER_REMOTE_ADDR.equals(match)) {
				matcher.appendReplacement(output, remoteAddr);
			} else if (PLACEHOLDER_REQUEST_URI.equals(match)) {
				matcher.appendReplacement(output, requestURI);
			} else if (PLACEHOLDER_USER_AGENT.equals(match)) {
				matcher.appendReplacement(output, agent);
			} else if (PLACEHOLDER_REQUEST_METHOD.equals(match)) {
				matcher.appendReplacement(output, requestMethod);
			} else if (PLACEHOLDER_REQUEST_TIME.equals(match)) {
				matcher.appendReplacement(output,
						String.valueOf(requestTime / (1000 * 1000)));
			} else if (PLACEHOLDER_CPU_TIME.equals(match)) {
				matcher.appendReplacement(output, String.valueOf(cupTime));
			} else if (PLACEHOLDER_RESPONSE_STATUS.equals(match)) {
				matcher.appendReplacement(output,
						String.valueOf(response.getStatus()));
			} else if (PLACEHOLDER_REQUEST_PARAMETER.equals(match)) {
				matcher.appendReplacement(output, getRequestParameter(request));
			}
		}
		matcher.appendTail(output);

		Exception ex = (Exception) request
				.getAttribute(BaseController.EXCEPTION_MESSAGE);
		if (ex != null)
			LOGGER.info(output.toString() + "\n "
					+ ExceptionUtils.getFullStackTrace(ex));
		else
			LOGGER.info(output.toString());

		statisticsHttpRequests(requestURI, requestTime, ex);
	}

	/**
	 * 统计http请求列表
	 * @param requestURI
	 * @param requestTime
	 * @param ex
	 */
	private void statisticsHttpRequests(String requestURI, long requestTime,
			Exception ex) {
		HttpRequestStat httpRequestStat = httpRequests.get(requestURI);
		if (httpRequestStat == null) {
			synchronized (httpRequests) {
				if (!httpRequests.containsKey(requestURI)) {
					httpRequestStat = new HttpRequestStat(requestURI);
					httpRequestStat.setId(requestId.decrementAndGet());
					httpRequests.putIfAbsent(requestURI, httpRequestStat);
				}
			}
		} else {
			httpRequestStat = httpRequests.get(requestURI);
		}

		httpRequestStat.setExecuteLastStartTime(System.currentTimeMillis());
		if (ex != null)
			httpRequestStat.error(ex);
		else
			httpRequestStat.incrementExecuteSuccessCount();
		httpRequestStat.addExecuteTime(requestTime);
	}

	/**
	 * 组装所有请求参数为一个字符串。
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private String getRequestParameter(HttpServletRequest request) {
		Enumeration enumeration = request.getParameterNames();
		List<String> list = new ArrayList<String>();
		while (enumeration.hasMoreElements()) {
			String key = (String) enumeration.nextElement();
			String value = request.getParameter(key);
			list.add(key + "=" + value);
		}
		String parameter = StringUtils.join(list, ", ");
		return parameter;
	}

	/**
	 * 返回message
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * 设置message
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 返回jmx开关
	 * @return
	 */
	public boolean isJmxOption() {
		return isJmxOption;
	}

	/**
	 * 设置jmx开关
	 * @param isJmxOption
	 */
	public void setJmxOption(boolean isJmxOption) {
		this.isJmxOption = isJmxOption;
	}
}
