/**
 * 文件名:ExcelColumns.java
 * 作者 ：李三来<br />
 * 邮箱 ：li.sanlai@ustcinfo.com<br />
 * 描述 ：com.cmcc.zysoft.excel
 * 版本 ：2013-5-22<br />
 * 日期 ： 2013-5-22 上午10:38:27<br />
 * 版权 ：CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.<br />
 */
package com.cmcc.zysoft.excel;

import java.util.List;


/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述： ExcelColumns
 * <br />版本: 2013-5-22
 * <br />日期： 2013-5-22 上午10:38:27
 */
public class ExcelColumns {

	/**
	 * EXCEL列名
	 */
	private String columnName;
	
	/**
	 * EXCEL列索引
	 */
	private int columnIndex;
	
	/**
	 * EXCEL列内容
	 */
	private List<Object> columnValues;

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public int getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}

	public List<Object> getColumnValues() {
		return columnValues;
	}

	public void setColumnValues(List<Object> columnValues) {
		this.columnValues = columnValues;
	}

	public ExcelColumns() {
		super();
	}

	public ExcelColumns(String columnName, int columnIndex,
			List<Object> columnValues) {
		super();
		this.columnName = columnName;
		this.columnIndex = columnIndex;
		this.columnValues = columnValues;
	}

	public ExcelColumns(int columnIndex, List<Object> columnValues) {
		super();
		this.columnIndex = columnIndex;
		this.columnValues = columnValues;
	}
	
}
