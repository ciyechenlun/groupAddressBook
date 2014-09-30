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
public class UserCompanyDto {
	//"姓名","性别","主要号码","手机短号","办公固话","办公短号","单位","二级部门","三级部门","四级部门","五级部门",
	//"职位","办公地址","显示顺序","邮箱","QQ","微博账号","微信账号","学校","专业","年级","班级","学号","籍贯","家庭住址","家庭电话","生日","个性签名"
			
	String name;
	String sex;
	String moblieLong;
	String mobileShort;
	String telLong;
	String telShort;
	String companyName;
	String departmentName2;
	String departmentName3;
	String departmentName4;
	String departmentName5;
	String headShip;
	String Address;
	Integer displayOrder;
	String email;
	String qq;
	String weiBo;
	String weiXin;
	String school;
	String schoolMajor;
	String schoolGrade;
	String schoolClass;
	String studentId;
	String nativePlace;
	String homeAddress;
	String homeTel;
	String birthday;
	String mood;

	/**
	 * 构造方法.  
	 */
	public UserCompanyDto() {
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
	public UserCompanyDto(String name,String sex,String moblieLong,String mobileShort,String telLong,
			String telShort,String companyName,String departmentName2,String departmentName3,
			String departmentName4,String departmentName5,String headShip,
			String Address,Integer displayOrder,String email,String qq,
			String weiBo,String weiXin,String school,String schoolMajor,
			String schoolGrade,String schoolClass,String studentId,
			String nativePlace,String homeAddress,String homeTel,
			String birthday,String mood) {
		super();
		this.name = name;
		this.sex = sex;
		this.moblieLong = moblieLong;
		this.mobileShort = mobileShort;
		this.telLong = telLong;
		this.telShort = telShort;
		this.companyName = companyName;
		this.departmentName2 = departmentName2;
		this.departmentName3 = departmentName3;
		this.departmentName3 = departmentName4;
		this.departmentName3 = departmentName5;
		this.headShip = headShip;
		this.Address = Address;
		this.displayOrder = displayOrder;
		this.email = email;
		this.qq = qq;
		this.weiBo = weiBo;
		this.weiXin = weiXin;
		this.school = school;
		this.schoolMajor = schoolMajor;
		this.schoolGrade = schoolGrade;
		this.schoolClass = schoolClass;
		this.studentId = studentId;
		this.nativePlace = nativePlace;
		this.homeAddress = homeAddress;
		this.homeTel = homeTel;
		this.birthday = birthday;
		this.mood = mood;
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
	}public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getDepartmentName2() {
		return departmentName2;
	}

	public void setDepartmentName2(String departmentName2) {
		this.departmentName2 = departmentName2;
	}

	public String getDepartmentName3() {
		return departmentName3;
	}

	public void setDepartmentName3(String departmentName3) {
		this.departmentName3 = departmentName3;
	}

	public String getDepartmentName4() {
		return departmentName4;
	}

	public void setDepartmentName4(String departmentName4) {
		this.departmentName4 = departmentName4;
	}

	public String getDepartmentName5() {
		return departmentName5;
	}

	public void setDepartmentName5(String departmentName5) {
		this.departmentName5 = departmentName5;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getWeiBo() {
		return weiBo;
	}

	public void setWeiBo(String weiBo) {
		this.weiBo = weiBo;
	}

	public String getWeiXin() {
		return weiXin;
	}

	public void setWeiXin(String weiXin) {
		this.weiXin = weiXin;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getSchoolMajor() {
		return schoolMajor;
	}

	public void setSchoolMajor(String schoolMajor) {
		this.schoolMajor = schoolMajor;
	}

	public String getSchoolGrade() {
		return schoolGrade;
	}

	public void setSchoolGrade(String schoolGrade) {
		this.schoolGrade = schoolGrade;
	}

	public String getSchoolClass() {
		return schoolClass;
	}

	public void setSchoolClass(String schoolClass) {
		this.schoolClass = schoolClass;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getNativePlace() {
		return nativePlace;
	}

	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	public String getHomeTel() {
		return homeTel;
	}

	public void setHomeTel(String homeTel) {
		this.homeTel = homeTel;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getMood() {
		return mood;
	}

	public void setMood(String mood) {
		this.mood = mood;
	}

}
