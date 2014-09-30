// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.spring.security.model;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述：用户角色
 * <br />版本:1.0.0
 * <br />日期： 2013-1-10 下午7:55:51
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
public class Role implements GrantedAuthority {

	// ~ serialVersionUID ： long
	private static final long serialVersionUID = -6113578940716066765L;

	/**
	 * 权限名称
	 */
	private String authority;

	/**
	 * 构造方法
	 * @param authority
	 */
	public Role(String authority) {
		this.authority = authority;
	}

	/**
	 * 覆盖getAuthority，获取权限名称
	 */
	@Override
	public String getAuthority() {
		return this.authority;
	}

	/**
	 * 设置权限
	 * @param authority
	 */
	public void setAuthority(String authority) {
		this.authority = authority;
	}

}
