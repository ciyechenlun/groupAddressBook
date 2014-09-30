/**
 * @author AMCC
 * <br /> 邮箱:zhouyusgs@ahmobile.com
 * <br /> 描述:WalkRecordDto.java
 * <br /> 版本：1.0.0
 * <br /> 日期：2013-12-28
 */
package com.cmcc.zysoft.groupaddressbook.dto;

/**
 * @author 周瑜
 *com.cmcc.zysoft.groupaddressbook.dto
 * 创建时间：2013-12-28
 */
public class WalkRecordDto {
	
	String employeeName;
	String departmentName;
	String mobile;
	String employeeHeight;
	String employeeWeight;
	String updateDate;
	String sportDate;
	String sportStartTime;
	String sportEndTime;
	String sportElapseTime;
	String sportStep;
	String sportDistence;
	String sportSpeed;
	String sportCalories;
	String gpsStatus;
	String gpsDistence;
	String modeChangeTimes;
	String stepCalc;
	String stepError;
	/**
	 * 构造方法
	 */
	public WalkRecordDto()
	{
		super();
	}
	
	
	
	/**
	 * @param employeeName
	 * @param departmentName
	 * @param mobile
	 * @param employeeHeight
	 * @param employeeWeight
	 * @param updateDate
	 * @param sportDate
	 * @param sportStartTime
	 * @param sportEndTime
	 * @param sportElapseTime
	 * @param sportStep
	 * @param sportDistence
	 * @param sportSpeed
	 * @param sportCalories
	 * @param gpsStatus
	 * @param gpsDistence
	 * @param modeChangeTimes
	 * @param stepCalc
	 * @param stepError
	 */
	public WalkRecordDto(String employeeName, String departmentName,
			String mobile, String employeeHeight, String employeeWeight,
			String updateDate, String sportDate, String sportStartTime,
			String sportEndTime, String sportElapseTime, String sportStep,
			String sportDistence, String sportSpeed, String sportCalories,
			String gpsStatus, String gpsDistence, String modeChangeTimes,
			String stepCalc, String stepError) {
		super();
		this.employeeName = employeeName;
		this.departmentName = departmentName;
		this.mobile = mobile;
		this.employeeHeight = employeeHeight;
		this.employeeWeight = employeeWeight;
		this.updateDate = updateDate;
		this.sportDate = sportDate;
		this.sportStartTime = sportStartTime;
		this.sportEndTime = sportEndTime;
		this.sportElapseTime = sportElapseTime;
		this.sportStep = sportStep;
		this.sportDistence = sportDistence;
		this.sportSpeed = sportSpeed;
		this.sportCalories = sportCalories;
		this.gpsStatus = gpsStatus;
		this.gpsDistence = gpsDistence;
		this.modeChangeTimes = modeChangeTimes;
		this.stepCalc = stepCalc;
		this.stepError = stepError;
	}



	/**
	 * @return the employeeName
	 */
	public String getEmployeeName() {
		return employeeName;
	}
	/**
	 * @param employeeName the employeeName to set
	 */
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	/**
	 * @return the departmentName
	 */
	public String getDepartmentName() {
		return departmentName;
	}
	/**
	 * @param departmentName the departmentName to set
	 */
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/**
	 * @return the employeeHeight
	 */
	public String getEmployeeHeight() {
		return employeeHeight;
	}
	/**
	 * @param employeeHeight the employeeHeight to set
	 */
	public void setEmployeeHeight(String employeeHeight) {
		this.employeeHeight = employeeHeight;
	}
	/**
	 * @return the employeeWeight
	 */
	public String getEmployeeWeight() {
		return employeeWeight;
	}
	/**
	 * @param employeeWeight the employeeWeight to set
	 */
	public void setEmployeeWeight(String employeeWeight) {
		this.employeeWeight = employeeWeight;
	}
	/**
	 * @return the updateDate
	 */
	public String getUpdateDate() {
		return updateDate;
	}
	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	/**
	 * @return the sportDate
	 */
	public String getSportDate() {
		return sportDate;
	}
	/**
	 * @param sportDate the sportDate to set
	 */
	public void setSportDate(String sportDate) {
		this.sportDate = sportDate;
	}
	/**
	 * @return the sportStartTime
	 */
	public String getSportStartTime() {
		return sportStartTime;
	}
	/**
	 * @param sportStartTime the sportStartTime to set
	 */
	public void setSportStartTime(String sportStartTime) {
		this.sportStartTime = sportStartTime;
	}
	/**
	 * @return the sportEndTime
	 */
	public String getSportEndTime() {
		return sportEndTime;
	}
	/**
	 * @param sportEndTime the sportEndTime to set
	 */
	public void setSportEndTime(String sportEndTime) {
		this.sportEndTime = sportEndTime;
	}
	/**
	 * @return the sportElapseTime
	 */
	public String getSportElapseTime() {
		return sportElapseTime;
	}
	/**
	 * @param sportElapseTime the sportElapseTime to set
	 */
	public void setSportElapseTime(String sportElapseTime) {
		this.sportElapseTime = sportElapseTime;
	}
	/**
	 * @return the sportStep
	 */
	public String getSportStep() {
		return sportStep;
	}
	/**
	 * @param sportStep the sportStep to set
	 */
	public void setSportStep(String sportStep) {
		this.sportStep = sportStep;
	}
	/**
	 * @return the sportDistence
	 */
	public String getSportDistence() {
		return sportDistence;
	}
	/**
	 * @param sportDistence the sportDistence to set
	 */
	public void setSportDistence(String sportDistence) {
		this.sportDistence = sportDistence;
	}
	/**
	 * @return the sportSpeed
	 */
	public String getSportSpeed() {
		return sportSpeed;
	}
	/**
	 * @param sportSpeed the sportSpeed to set
	 */
	public void setSportSpeed(String sportSpeed) {
		this.sportSpeed = sportSpeed;
	}
	/**
	 * @return the sportCalories
	 */
	public String getSportCalories() {
		return sportCalories;
	}
	/**
	 * @param sportCalories the sportCalories to set
	 */
	public void setSportCalories(String sportCalories) {
		this.sportCalories = sportCalories;
	}
	/**
	 * @return the gpsStatus
	 */
	public String getGpsStatus() {
		return gpsStatus;
	}
	/**
	 * @param gpsStatus the gpsStatus to set
	 */
	public void setGpsStatus(String gpsStatus) {
		this.gpsStatus = gpsStatus;
	}
	/**
	 * @return the gpsDistence
	 */
	public String getGpsDistence() {
		return gpsDistence;
	}
	/**
	 * @param gpsDistence the gpsDistence to set
	 */
	public void setGpsDistence(String gpsDistence) {
		this.gpsDistence = gpsDistence;
	}
	/**
	 * @return the modeChangeTimes
	 */
	public String getModeChangeTimes() {
		return modeChangeTimes;
	}
	/**
	 * @param modeChangeTimes the modeChangeTimes to set
	 */
	public void setModeChangeTimes(String modeChangeTimes) {
		this.modeChangeTimes = modeChangeTimes;
	}
	/**
	 * @return the stepCalc
	 */
	public String getStepCalc() {
		return stepCalc;
	}
	/**
	 * @param stepCalc the stepCalc to set
	 */
	public void setStepCalc(String stepCalc) {
		this.stepCalc = stepCalc;
	}
	/**
	 * @return the stepError
	 */
	public String getStepError() {
		return stepError;
	}
	/**
	 * @param stepError the stepError to set
	 */
	public void setStepError(String stepError) {
		this.stepError = stepError;
	}
	
	
	
	
}
