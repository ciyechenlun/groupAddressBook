// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.spring.security.util;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 * @author 李三来 <br />
 *         邮箱： li.sanlai@ustcinfo.com <br />
 *         描述：AntUrlPathMatcher <br />
 *         版本:1.0.0 <br />
 *         日期： 2013-1-10 下午10:19:38 <br />
 *         CopyRight © 2012 USTC SINOVATE SOFTWARE CO.LTD All Rights Reserved.
 */
public class AntUrlPathMatcher implements UrlMatcher {

	
	/**
	 * 是否转换成小写
	 */
	private boolean requiresLowerCaseUrl = true;
	
	/**
	 * 路径匹配器
	 */
	private PathMatcher pathMatcher = new AntPathMatcher();

	/**
	 * 构造方法
	 */
	public AntUrlPathMatcher() {
		this(true);
	}

	/**
	 * 构造方法
	 * @param requiresLowerCaseUrl
	 */
	public AntUrlPathMatcher(boolean requiresLowerCaseUrl) {
		this.requiresLowerCaseUrl = requiresLowerCaseUrl;
	}

	/**
	 * 编译
	 */
	public Object compile(String path) {
		if (requiresLowerCaseUrl) {
			return path.toLowerCase();
		}

		return path;
	}

	/**
	 * 设置requiresLowerCaseUrl
	 * @param requiresLowerCaseUrl
	 */
	public void setRequiresLowerCaseUrl(boolean requiresLowerCaseUrl) {
		this.requiresLowerCaseUrl = requiresLowerCaseUrl;
	}

	/**
	 * 返回匹配结果
	 */
	public boolean pathMatchesUrl(Object path, String url) {
		return pathMatcher.match((String) path, url);
	}

	/**
	 * 获取通用Pattern
	 */
	public String getUniversalMatchPattern() {
		return "/**";
	}

	/**
	 * 返回requiresLowerCaseUrl
	 */
	public boolean requiresLowerCaseUrl() {
		return requiresLowerCaseUrl;
	}

	/**
	 * 重写toString方法
	 */
	public String toString() {
		return getClass().getName() + "[requiresLowerCase='"
				+ requiresLowerCaseUrl + "']";
	}

}
