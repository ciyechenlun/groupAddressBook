// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.rule;

import java.util.List;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述：Column
 * <br />版本:1.0.0
 * <br />日期： 2013-5-17 上午12:55:07
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
public class Column {

	/**
	 * 名称
	 */
	private String name;
	
	/**
	 * 代码
	 */
	private String code;
	
	/**
	 * 规则列表
	 */
	private List<RegexRule> regexRules;
	
	/**
	 * 规则列表
	 */
	private List<KeywordRule> keywordRules;
	
	/**
	 * 列索引号
	 */
	private int colIndex = -1;

	/**
	 * 返回name
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置name
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 返回code
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 设置code
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 返回 regexRules
	 * @return regexRules
	 */
	public List<RegexRule> getRegexRules() {
		return regexRules;
	}

	/**
	 * 设置regexRules
	 * @param regexRules the regexRules to set
	 */
	public void setRegexRules(List<RegexRule> regexRules) {
		this.regexRules = regexRules;
	}

	/**
	 * 返回 keywordRules
	 * @return keywordRules
	 */
	public List<KeywordRule> getKeywordRules() {
		return keywordRules;
	}

	/**
	 * 设置keywordRules
	 * @param keywordRules the keywordRules to set
	 */
	public void setKeywordRules(List<KeywordRule> keywordRules) {
		this.keywordRules = keywordRules;
	}

	/**
	 * 返回colIndex
	 * @return the colIndex
	 */
	public int getColIndex() {
		return colIndex;
	}

	/**
	 * 设置colIndex
	 * @param colIndex the colIndex to set
	 */
	public void setColIndex(int colIndex) {
		this.colIndex = colIndex;
	}

	/**
	 * 构造方法
	 */
	public Column() {
		super();
	}

	/**
	 * 构造方法
	 * @param name
	 * @param code
	 * @param regexRules
	 * @param keywordRules
	 */
	public Column(String name, String code, List<RegexRule> regexRules,
			List<KeywordRule> keywordRules) {
		super();
		this.name = name;
		this.code = code;
		this.regexRules = regexRules;
		this.keywordRules = keywordRules;
	}
	
}
