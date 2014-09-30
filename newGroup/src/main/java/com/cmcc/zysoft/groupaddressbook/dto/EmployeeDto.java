// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.dto;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：employeeDto
 * <br />版本:1.0.0
 * <br />日期： 2013-3-1 下午5:36:41
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
public class EmployeeDto {
	
	String name;
	String departmentName;
	String headShip;
	String moblieLong;
	String mobileShort;
	String telLong;
	String telShort;
	String email;
	Integer displayOrder;
	
	
	/**
	 * 构造方法.  
	 */
	public EmployeeDto() {
		super();
	}
	
	/**
	 * 构造方法.
	 * @param name 姓名
	 * @param departmentName 部门名称 
	 * @param headShip  岗位
	 * @param moblieLong  手机长号
	 * @param mobileShort  手机短号
	 * @param telLong  办公长号
	 * @param telShort  办公短号
	 * @param email  邮箱
	 * @param displayOrder 显示顺序
	 */
	public EmployeeDto(String name, String departmentName, String headShip,
			String moblieLong, String mobileShort, String telLong,
			String telShort, String email, Integer displayOrder) {
		super();
		this.name = name;
		this.departmentName = departmentName;
		this.headShip = headShip;
		this.moblieLong = moblieLong;
		this.mobileShort = mobileShort;
		this.telLong = telLong;
		this.telShort = telShort;
		this.email = email;
		this.displayOrder = displayOrder;
	}
	/**
	 * 返回 name.
	 * @return name
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置name.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 返回 departmentName.
	 * @return departmentName
	 */
	public String getDepartmentName() {
		return departmentName;
	}
	/**
	 * 设置departmentName.
	 * @param departmentName the departmentName to set
	 */
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	/**
	 * 返回 headShip.
	 * @return headShip
	 */
	public String getHeadShip() {
		return headShip;
	}
	/**
	 * 设置headShip.
	 * @param headShip the headShip to set
	 */
	public void setHeadShip(String headShip) {
		this.headShip = headShip;
	}
	/**
	 * 返回 moblieLong.
	 * @return moblieLong
	 */
	public String getMoblieLong() {
		return moblieLong;
	}
	/**
	 * 设置moblieLong.
	 * @param moblieLong the moblieLong to set
	 */
	public void setMoblieLong(String moblieLong) {
		this.moblieLong = moblieLong;
	}
	/**
	 * 返回 mobileShort.
	 * @return mobileShort
	 */
	public String getMobileShort() {
		return mobileShort;
	}
	/**
	 * 设置mobileShort.
	 * @param mobileShort the mobileShort to set
	 */
	public void setMobileShort(String mobileShort) {
		this.mobileShort = mobileShort;
	}
	/**
	 * 返回 telLong.
	 * @return telLong
	 */
	public String getTelLong() {
		return telLong;
	}
	/**
	 * 设置telLong.
	 * @param telLong the telLong to set
	 */
	public void setTelLong(String telLong) {
		this.telLong = telLong;
	}
	/**
	 * 返回 telShort.
	 * @return telShort
	 */
	public String getTelShort() {
		return telShort;
	}
	/**
	 * 设置telShort.
	 * @param telShort the telShort to set
	 */
	public void setTelShort(String telShort) {
		this.telShort = telShort;
	}
	/**
	 * 返回 email.
	 * @return email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * 设置email.
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 返回 displayOrder.
	 * @return displayOrder
	 */
	public Integer getDisplayOrder() {
		return displayOrder;
	}

	/**
	 * 设置displayOrder.
	 * @param displayOrder the displayOrder to set
	 */
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

}
