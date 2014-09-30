/**
 * 文件名:RegexRule.java
 * 作者 ：李三来<br />
 * 邮箱 ：li.sanlai@ustcinfo.com<br />
 * 描述 ：com.cmcc.zysoft.rule
 * 版本 ：2013-5-20<br />
 * 日期 ： 2013-5-20 上午10:36:30<br />
 * 版权 ：CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.<br />
 */
package com.cmcc.zysoft.rule;

import java.util.List;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述： RegexRule
 * <br />版本: 2013-5-20
 * <br />日期： 2013-5-20 上午10:36:30
 */
public class RegexRule extends Rule {
	
	/**
	 * 构造方法
	 */
	public RegexRule() {
		super();
	}

	/**
	 * 构造方法
	 * @param type
	 * @param values
	 */
	public RegexRule(String type, List<String> values) {
		super(type,values);
	}
}
