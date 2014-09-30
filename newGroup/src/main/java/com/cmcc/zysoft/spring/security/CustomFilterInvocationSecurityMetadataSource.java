// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.spring.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import com.cmcc.zysoft.spring.security.util.AntUrlPathMatcher;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.cmcc.zysoft.spring.security.util.UrlMatcher;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述：自定义FilterInvocationSecurityMetadataSource
 * <br />版本:1.0.0
 * <br />日期： 2013-1-10 下午9:15:47
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
public class CustomFilterInvocationSecurityMetadataSource implements
		FilterInvocationSecurityMetadataSource {
	
	/**
	 * 日志
	 */
	private static Logger logger = LoggerFactory.getLogger(CustomFilterInvocationSecurityMetadataSource.class);
	
	/**
	 * URL匹配器
	 */
	private static UrlMatcher urlMatcher = new AntUrlPathMatcher(true);
	
	/**
	 * 是否需要将请求路径后面的"？"去掉
	 */
	private boolean stripQueryStringFromUrls;
	
	/**
	 * jdbcTemplate
	 */
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static LinkedHashMap<Object, Collection<ConfigAttribute>> httpMap = new LinkedHashMap<Object, Collection<ConfigAttribute>>();
	
	static {
		List<ConfigAttribute> attrs = new ArrayList<ConfigAttribute>();
		attrs.add(new SecurityConfig("__ROLE_USER__"));
		attrs.add(new SecurityConfig("__ROLE_ADMIN__"));
		httpMap.put(urlMatcher.compile("/**"), attrs);
	}
	
	
	/**
	 * 通过FilterInvocation对象查找适合的ConfigAttribute
	 */
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object)
			throws IllegalArgumentException {
		if ((object == null) || !this.supports(object.getClass())) {
			throw new IllegalArgumentException(
					"Object must be a FilterInvocation");
		}

		String url = ((FilterInvocation) object).getRequestUrl();

		return lookupAttributes(url);
	}

	/**
	 * 获取所有的ConfigAttribute
	 */
	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	/**
	 * 重写supports方法
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}
	
	/**
	 * 根据公司加载资源权限
	 */
	public LinkedHashMap<Object, Collection<ConfigAttribute>> loadSecurityMetadataSource() {
		LinkedHashMap<Object, Collection<ConfigAttribute>> httpMap = new LinkedHashMap<Object, Collection<ConfigAttribute>>();
		
		String companyId = SecurityContextUtil.getCurrentUser().getCompanyId();
		logger.info("#加载公司id = {} 的资源权限", companyId);

//		String sql = "SELECT m.MENU_ID, cm.META_ID, cm.META_CODE FROM sys_t_menu m, osp_t_config_meta cm " +
//				"WHERE m.META_ID = cm.META_ID AND m.TENANT_ID = ?";
		String sql = "SELECT m.MENU_ID,m.PATH FROM tb_c_menu as m WHERE m.company_id = ? and m.status = '1'";
		
		List<Map<String, Object>> menus = jdbcTemplate.queryForList(sql, companyId);
		
		for(Map<String, Object> menu : menus) {
			String pattern = (String)menu.get("PATH");
			String menuId = (String)menu.get("MENU_ID");
			
			sql = 
				"SELECT\n" +
				"  r.ROLE_CODE\n" + 
				"FROM tb_c_role AS r,tb_c_role_menu as rm,tb_c_menu as m\n" + 
				"WHERE\n" + 
				"    r.role_id = rm.role_id\n" + 
				"AND r.status = '0'\n" + 
				"AND rm.menu_id = m.menu_id\n" + 
				"AND m.company_id = r.company_id\n" + 
				"AND m.path <> ''\n" + 
				"AND m.path IS NOT NULL\n" + 
				"AND m.company_id = ? \n" + 
				"AND m.menu_id = ? ";

			List<Map<String, Object>> roles = jdbcTemplate.queryForList(sql, companyId, menuId);
			
			List<ConfigAttribute> attrs = new ArrayList<ConfigAttribute>();
			for(Map<String, Object> role : roles) {
				attrs.add(new SecurityConfig((String)role.get("ROLE_CODE")));
			}
			if(pattern!=null){
				httpMap.put(urlMatcher.compile(pattern), attrs);
			}

			if (logger.isDebugEnabled()) {
	            logger.debug("Added URL pattern: " + pattern + "; attributes: " + attrs);
	        }
		}
		
		return httpMap;
	}

	
	/**
	 * 根据URL提取ConfigAttribute
	 * @param url
	 * @param map
	 * @return
	 */
	private Collection<ConfigAttribute> extractMatchingAttributes(String url,
			Map<Object, Collection<ConfigAttribute>> map) {

		final boolean debug = logger.isDebugEnabled();

		for (Map.Entry<Object, Collection<ConfigAttribute>> entry : map.entrySet()) {
			Object p = entry.getKey();
			boolean matched = urlMatcher.pathMatchesUrl(entry.getKey(), url);

			if (debug) {
				logger.debug("Candidate is: '" + url + "'; pattern is " + p
						+ "; matched=" + matched);
			}

			if (matched) {
				return entry.getValue();
			}
		}
		return null;
	}
	
	/**
	 * 通过URL查找ConfigAttribute
	 * @param url
	 * @return
	 */
	public final Collection<ConfigAttribute> lookupAttributes(String url) {
		if (stripQueryStringFromUrls) {
			// Strip anything after a question mark symbol, as per SEC-161. See
			// also SEC-321
			int firstQuestionMarkIndex = url.indexOf("?");

			if (firstQuestionMarkIndex != -1) {
				url = url.substring(0, firstQuestionMarkIndex);
			}
		}

		if (urlMatcher.requiresLowerCaseUrl()) {
			url = url.toLowerCase();
			if (logger.isDebugEnabled()) {
				logger.debug("#转换url成小写， from: '" + url
						+ "'; to: '" + url + "'");
			}
		}

		// Obtain the map of request patterns to attributes for this method and
		// lookup the url.
		if(SecurityContextUtil.getCurrentUser() != null) {
			Map<Object, Collection<ConfigAttribute>> httpMap = loadSecurityMetadataSource();
			
			return extractMatchingAttributes(url, httpMap);
		} else {
			return extractMatchingAttributes(url, httpMap);
		}
	}

}
