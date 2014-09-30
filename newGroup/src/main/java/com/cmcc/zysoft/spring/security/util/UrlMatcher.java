// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.spring.security.util;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述：URL匹配器
 * <br />版本:1.0.0
 * <br />日期： 2013-1-10 下午10:18:01
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
public interface UrlMatcher {
	/**
	 * 编译
	 * @param urlPattern
	 * @return
	 */
	Object compile(String urlPattern);

	/**
	 * 匹配
	 * @param compiledUrlPattern
	 * @param url
	 * @return
	 */
    boolean pathMatchesUrl(Object compiledUrlPattern, String url);

    /** Returns the path which matches every URL */
    String getUniversalMatchPattern();

    /**
     * Returns true if the matcher expects the URL to be converted to lower case before
     * calling {@link #pathMatchesUrl(Object, String)}.
     */
    boolean requiresLowerCaseUrl();
}
