// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.dto;

/**
 * @author 杜纪亮
 * <br />邮箱：du.jiliang@ustcinfo.com
 * <br />描述：LookGroupInfoDto
 * <br />版本:1.0.0
 * <br />日期： 2013-3-4 上午11:41:19
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
public class LookGroupInfoDto {
	private int page; // 当前页
	private int rows; // 每页个数
	private String deptId;//部门ID
	private String deptName;//部门名称
	private String pDeptName;//父部门名称
	
	/**
	 * 返回 page.
	 * @return page
	 */
	public int getPage() {
		return page;
	}
	/**
	 * 设置page.
	 * @param page the page to set
	 */
	public void setPage(int page) {
		this.page = page;
	}
	/**
	 * 返回 rows.
	 * @return rows
	 */
	public int getRows() {
		return rows;
	}
	/**
	 * 设置rows.
	 * @param rows the rows to set
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}
	/**
	 * 返回 deptId.
	 * @return deptId
	 */
	public String getDeptId() {
		return deptId;
	}
	/**
	 * 设置deptId.
	 * @param deptId the deptId to set
	 */
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	/**
	 * 返回 deptName
	 * @return deptName
	 */
	public String getDeptName() {
		return deptName;
	}
	/**
	 * 设置deptName
	 * @param deptName the deptName to set
	 */
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	/**
	 * 返回 pDeptName
	 * @return pDeptName
	 */
	public String getpDeptName() {
		return pDeptName;
	}
	/**
	 * 设置pDeptName
	 * @param pDeptName the pDeptName to set
	 */
	public void setpDeptName(String pDeptName) {
		this.pDeptName = pDeptName;
	}
	
}
