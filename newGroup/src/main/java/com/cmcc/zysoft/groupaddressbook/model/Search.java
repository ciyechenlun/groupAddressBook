package com.cmcc.zysoft.groupaddressbook.model;

import java.io.Serializable;


@SuppressWarnings("serial")
public class Search implements Serializable {

	
	private Integer pageNo = 1;
	private Integer pageSize = 3;
	
	/**
	 * 返回 pageNo.
	 * @return pageNo
	 */
	public Integer getPageNo() {
		return pageNo;
	}
	/**
	 * 设置pageNo.
	 * @param pageNo the pageNo to set
	 */
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	/**
	 * 返回 pageSize.
	 * @return pageSize
	 */
	public Integer getPageSize() {
		return pageSize;
	}
	/**
	 * 设置pageSize.
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
}
