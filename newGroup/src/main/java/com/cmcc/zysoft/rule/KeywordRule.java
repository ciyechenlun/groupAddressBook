/**
 * 文件名:KeywordRule.java
 * 作者 ：李三来<br />
 * 邮箱 ：li.sanlai@ustcinfo.com<br />
 * 描述 ：com.cmcc.zysoft.rule
 * 版本 ：2013-5-20<br />
 * 日期 ： 2013-5-20 上午10:37:15<br />
 * 版权 ：CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.<br />
 */
package com.cmcc.zysoft.rule;

import java.util.List;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述： KeywordRule
 * <br />版本: 2013-5-20
 * <br />日期： 2013-5-20 上午10:37:15
 */
public class KeywordRule extends Rule {

	/**
	 * 构造方法
	 */
	public KeywordRule() {
		super();
	}

	/**
	 * 构造方法
	 * @param type
	 * @param values
	 */
	public KeywordRule(String type, List<String> values) {
		super(type,values);
	}

}
