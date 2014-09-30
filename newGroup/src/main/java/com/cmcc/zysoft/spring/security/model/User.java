// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.spring.security.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cmcc.zysoft.sellmanager.model.Role;

/**
 * @author 李三来 <br />
 *         邮箱： li.sanlai@ustcinfo.com <br />
 *         描述：用户 <br />
 *         版本:1.0.0 <br />
 *         日期： 2013-1-10 下午7:59:37 <br />
 *         CopyRight © 2012 USTC SINOVATE SOFTWARE CO.LTD All Rights Reserved.
 */
public class User implements UserDetails {

	/**
	 * 构造方法
	 */
	public User() {
	}

	/**
	 * 构造方法
	 * 
	 * @param userId
	 * @param companyId
	 * @param roles
	 * @param employeeId
	 * @param username
	 * @param password
	 * @param passSalt
	 * @param mobile
	 * @param email
	 * @param realName
	 * @param photo
	 * @param sex
	 * @param loginIp
	 * @param loginTime
	 * @param loginCount
	 * @param authorities
	 */
	public User(String userId, String companyId, List<Role> roles,
			String employeeId, String username, String password,
			String passSalt, String mobile, String email, String realName,
			String photo, String sex, String loginIp, Date loginTime,
			Long loginCount, List<GrantedAuthority> authorities) {
		this.userId = userId;
		this.companyId = companyId;
		this.roles = roles;
		this.employeeId = employeeId;
		this.username = username;
		this.password = password;
		this.passSalt = passSalt;
		this.mobile = mobile;
		this.email = email;
		this.realName = realName;
		this.photo = photo;
		this.sex = sex;
		this.loginIp = loginIp;
		this.loginTime = loginTime;
		this.loginCount = loginCount;
		this.authorities = authorities;
	}

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 用户所属公司ID
	 */
	private String companyId;

	/**
	 * 用户的角色ID
	 */
	private List<Role> roles;

	/**
	 * 用户所对应的员工ID
	 */
	private String employeeId;

	/**
	 * 用户登录名
	 */
	private String username;

	/**
	 * 登录密码
	 */
	private String password;

	/**
	 * 密码盐
	 */
	private String passSalt;

	/**
	 * 手机号码
	 */
	private String mobile;

	/**
	 * 电子邮箱
	 */
	private String email;

	/**
	 * 真实姓名
	 */
	private String realName;

	/**
	 * 照片地址
	 */
	private String photo;

	/**
	 * 性别
	 */
	private String sex;

	/**
	 * 登录ip
	 */
	private String loginIp;

	/**
	 * 登录时间
	 */
	private Date loginTime;

	/**
	 * 登录次数
	 */
	private Long loginCount;

	/**
	 * 角色列表
	 */
	private List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

	// ~ serialVersionUID ： long
	private static final long serialVersionUID = -8438527754900746558L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.core.userdetails.UserDetails#getAuthorities
	 * ()
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.core.userdetails.UserDetails#getPassword()
	 */
	@Override
	public String getPassword() {
		return this.password;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.core.userdetails.UserDetails#getUsername()
	 */
	@Override
	public String getUsername() {
		return this.username;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.core.userdetails.UserDetails#isAccountNonExpired
	 * ()
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.core.userdetails.UserDetails#isAccountNonLocked
	 * ()
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetails#
	 * isCredentialsNonExpired()
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.core.userdetails.UserDetails#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}

	/**
	 * 返回userId
	 * 
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 设置userId
	 * 
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 返回companyId
	 * 
	 * @return the companyId
	 */
	public String getCompanyId() {
		return companyId;
	}

	/**
	 * 设置companyId
	 * 
	 * @param companyId
	 *            the companyId to set
	 */
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	/**
	 * 返回employeeId
	 * 
	 * @return the employeeId
	 */
	public String getEmployeeId() {
		return employeeId;
	}

	/**
	 * 设置employeeId
	 * 
	 * @param employeeId
	 *            the employeeId to set
	 */
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	/**
	 * 返回passSalt
	 * 
	 * @return the passSalt
	 */
	public String getPassSalt() {
		return passSalt;
	}

	/**
	 * 设置passSalt
	 * 
	 * @param passSalt
	 *            the passSalt to set
	 */
	public void setPassSalt(String passSalt) {
		this.passSalt = passSalt;
	}

	/**
	 * 返回mobile
	 * 
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * 设置mobile
	 * 
	 * @param mobile
	 *            the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * 返回email
	 * 
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 设置email
	 * 
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 返回realName
	 * 
	 * @return the realName
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * 设置realName
	 * 
	 * @param realName
	 *            the realName to set
	 */
	public void setRealName(String realName) {
		this.realName = realName;
	}

	/**
	 * 返回photo
	 * 
	 * @return the photo
	 */
	public String getPhoto() {
		return photo;
	}

	/**
	 * 设置photo
	 * 
	 * @param photo
	 *            the photo to set
	 */
	public void setPhoto(String photo) {
		this.photo = photo;
	}

	/**
	 * 返回sex
	 * 
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * 设置sex
	 * 
	 * @param sex
	 *            the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * 返回loginIp
	 * 
	 * @return the loginIp
	 */
	public String getLoginIp() {
		return loginIp;
	}

	/**
	 * 设置loginIp
	 * 
	 * @param loginIp
	 *            the loginIp to set
	 */
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	/**
	 * 返回loginTime
	 * 
	 * @return the loginTime
	 */
	public Date getLoginTime() {
		return loginTime;
	}

	/**
	 * 设置loginTime
	 * 
	 * @param loginTime
	 *            the loginTime to set
	 */
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	/**
	 * 返回loginCount
	 * 
	 * @return the loginCount
	 */
	public Long getLoginCount() {
		return loginCount;
	}

	/**
	 * 设置loginCount
	 * 
	 * @param loginCount
	 *            the loginCount to set
	 */
	public void setLoginCount(Long loginCount) {
		this.loginCount = loginCount;
	}

	/**
	 * 设置username
	 * 
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 设置password
	 * 
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 设置authorities
	 * 
	 * @param authorities
	 *            the authorities to set
	 */
	public void setAuthorities(List<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	/**
	 * 返回roles
	 * 
	 * @return the roles
	 */
	public List<Role> getRoles() {
		return roles;
	}

	/**
	 * 设置roles
	 * 
	 * @param roles
	 *            the roles to set
	 */
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	
	/**
	 * 重写hashcode方法，解决session并发不生效问题
	 */
	@Override
	public int hashCode() {
		return this.userId.hashCode();
	}

	/**
	 * 重写equals方法，解决session并发不生效问题
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof User))
	           return false;
		User user = (User) obj;
		return user.getUserId().equals(userId);
	}

}
