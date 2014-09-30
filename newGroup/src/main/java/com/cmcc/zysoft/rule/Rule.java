// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.rule;

import java.util.List;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述：Rule
 * <br />版本:1.0.0
 * <br />日期： 2013-5-17 上午12:57:42
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
public class Rule {

	/**
	 * 构造方法
	 */
	public Rule() {

	}
	
	/**
	 * 构造方法
	 * @param type
	 * @param values
	 */
	public Rule(String type, List<String> values) {
		this.type = type;
		this.values = values;
	}

	/**
	 * 类别
	 */
	private String type;
	
	/**
	 * 值
	 */
	private List<String> values;

	/**
	 * 返回type
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置type
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 返回values
	 * @return the values
	 */
	public List<String> getValues() {
		return values;
	}

	/**
	 * 设置values
	 * @param values the values to set
	 */
	public void setValues(List<String> values) {
		this.values = values;
	}
	
}
