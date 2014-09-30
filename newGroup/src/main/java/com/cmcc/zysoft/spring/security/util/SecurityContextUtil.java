// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.spring.security.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.cmcc.zysoft.framework.common.support.ApplicationContextHolder;
import com.cmcc.zysoft.framework.utils.RandomUniqueIdGenerator;
import com.cmcc.zysoft.spring.security.model.User;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述：SecurityContext工具类
 * <br />版本:1.0.0
 * <br />日期： 2013-1-10 下午7:49:32
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
public class SecurityContextUtil {
	
	
	//~methods---------------------------------------------
	
	/**
	 * 获取当前登录用户
	 * 
	 * @return
	 */
	public static User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null) {
			Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(user instanceof User)
				return (User)user;
			else {
				User anonymousUser = new User();
				anonymousUser.setUsername((String)user);
				return anonymousUser;
			}
		} else {
			return null;
		}
	}
	
	/**
	 * 获取当前userId
	 * 
	 * @return
	 */
	public static String getUserId() {
		return SecurityContextUtil.getCurrentUser().getUserId();
	}
	
	/**
	 * 获取当前用户所属公司Id
	 * 
	 * @return
	 */
	public static String getCompanyId() {
		return SecurityContextUtil.getCurrentUser().getCompanyId();
	}
	
	/**
	 * 利用密码盐对密码加密
	 * @param rawPass
	 * @param salt
	 * @return
	 */
	public static String encodePassword(String rawPass, String salt) {
		Assert.hasText(salt);
		PasswordEncoder passwordEncoder = ApplicationContextHolder.getBean(PasswordEncoder.class);
		return passwordEncoder.encodePassword(rawPass, salt);
	}
	
	/**
	 * 生成密码Salt
	 */
	public static String generateRandomSalt() {
		return RandomUniqueIdGenerator.getNewString().toLowerCase();
	}
	
	/**
	 * 生成随机密码
	 * @param pwRandomLength
	 * @return
	 */
	public static String generateRandomPassWord(int pwRandomLength) {
		String pwd = RandomUniqueIdGenerator.getNewString(pwRandomLength);
		return pwd.toLowerCase();
	}
	
	/**
	 * 获取远程IP地址
	 * @param request
	 * @return
	 */
	public static String getRemoteIP(HttpServletRequest request) {
    	String remoteIP = request.getHeader("X-Real-IP");
		if (!StringUtils.hasText(remoteIP)) {
			remoteIP = request.getRemoteAddr();
		}
		return remoteIP;
	}
	
}
