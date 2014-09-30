// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.spring.security;

import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.core.userdetails.UserDetails;

import com.cmcc.zysoft.spring.security.model.User;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述：自定义密码盐
 * <br />版本:1.0.0
 * <br />日期： 2013-1-10 下午9:23:06
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
public class CustomSaltSource implements SaltSource {

	/**
	 * 返回密码盐
	 */
	@Override
	public Object getSalt(UserDetails userDetails) {
		User user = (User)userDetails;
		return user.getPassSalt();
	}

}
