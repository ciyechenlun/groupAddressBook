/**
 * @author AMCC
 * <br /> 邮箱:zhouyusgs@ahmobile.com
 * <br /> 描述:UserRecordDto.java
 * <br /> 版本：1.0.0
 * <br /> 日期：2013-12-28
 */
package com.cmcc.zysoft.groupaddressbook.dto;

/**
 * @author 周瑜
 *com.cmcc.zysoft.groupaddressbook.dto
 * 创建时间：2013-12-28
 */
public class UserRecordDto {
	private String employee_name;
	private String all_sport_times;
	private String all_sport_elpse_time;
	private String avg_sport_speed;
	private String all_sport_steps;
	private String avg_sport_steps;
	private String current_rank;
	
	
	/**
	 * 
	 */
	public UserRecordDto() {
		super();
	}
	/**
	 * @param employee_name
	 * @param all_sport_times
	 * @param all_sport_elpse_time
	 * @param avg_sport_speed
	 * @param all_sport_steps
	 * @param avg_sport_steps
	 * @param current_rank
	 */
	public UserRecordDto(String employee_name, String all_sport_times,
			String all_sport_elpse_time, String avg_sport_speed,
			String all_sport_steps, String avg_sport_steps, String current_rank) {
		super();
		this.employee_name = employee_name;
		this.all_sport_times = all_sport_times;
		this.all_sport_elpse_time = all_sport_elpse_time;
		this.avg_sport_speed = avg_sport_speed;
		this.all_sport_steps = all_sport_steps;
		this.avg_sport_steps = avg_sport_steps;
		this.current_rank = current_rank;
	}
	/**
	 * @return the employee_name
	 */
	public String getEmployee_name() {
		return employee_name;
	}
	/**
	 * @param employee_name the employee_name to set
	 */
	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}
	/**
	 * @return the all_sport_times
	 */
	public String getAll_sport_times() {
		return all_sport_times;
	}
	/**
	 * @param all_sport_times the all_sport_times to set
	 */
	public void setAll_sport_times(String all_sport_times) {
		this.all_sport_times = all_sport_times;
	}
	/**
	 * @return the all_sport_elpse_time
	 */
	public String getAll_sport_elpse_time() {
		return all_sport_elpse_time;
	}
	/**
	 * @param all_sport_elpse_time the all_sport_elpse_time to set
	 */
	public void setAll_sport_elpse_time(String all_sport_elpse_time) {
		this.all_sport_elpse_time = all_sport_elpse_time;
	}
	/**
	 * @return the avg_sport_speed
	 */
	public String getAvg_sport_speed() {
		return avg_sport_speed;
	}
	/**
	 * @param avg_sport_speed the avg_sport_speed to set
	 */
	public void setAvg_sport_speed(String avg_sport_speed) {
		this.avg_sport_speed = avg_sport_speed;
	}
	/**
	 * @return the all_sport_steps
	 */
	public String getAll_sport_steps() {
		return all_sport_steps;
	}
	/**
	 * @param all_sport_steps the all_sport_steps to set
	 */
	public void setAll_sport_steps(String all_sport_steps) {
		this.all_sport_steps = all_sport_steps;
	}
	/**
	 * @return the avg_sport_steps
	 */
	public String getAvg_sport_steps() {
		return avg_sport_steps;
	}
	/**
	 * @param avg_sport_steps the avg_sport_steps to set
	 */
	public void setAvg_sport_steps(String avg_sport_steps) {
		this.avg_sport_steps = avg_sport_steps;
	}
	/**
	 * @return the current_rank
	 */
	public String getCurrent_rank() {
		return current_rank;
	}
	/**
	 * @param current_rank the current_rank to set
	 */
	public void setCurrent_rank(String current_rank) {
		this.current_rank = current_rank;
	}
	
}
