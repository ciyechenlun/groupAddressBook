// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.spring.security.jmx;

import javax.management.JMException;
import javax.management.openmbean.TabularData;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述：JMX-URI状态管理Bean
 * <br />版本:1.0.0
 * <br />日期： 2013-1-10 下午7:43:33
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
public interface URIStatManagerMBean {
	
	/**
	 * 获取URI列表
	 * @return
	 * @throws JMException
	 */
	TabularData getUriList() throws JMException;

	/**
	 * 重置
	 */
	void reset();

	/**
	 * 获取重置次数
	 * @return
	 */
	long getResetCount();
}
