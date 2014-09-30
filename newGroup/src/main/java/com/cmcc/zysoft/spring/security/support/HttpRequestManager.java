// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.spring.security.support;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author 李三来 <br />
 *         邮箱： li.sanlai@ustcinfo.com <br />
 *         描述： Http请求管理器 <br />
 *         版本:1.0.0 <br />
 *         日期： 2013-1-10 下午7:25:29 <br />
 *         CopyRight © 2012 USTC SINOVATE SOFTWARE CO.LTD All Rights Reserved.
 */
public class HttpRequestManager {
	
	/**
	 * ID种子
	 */
	private final AtomicLong idSeed = new AtomicLong(1000);

	/**
	 * Http请求管理器实例
	 */
	private final static HttpRequestManager instance = new HttpRequestManager();

	/**
	 * Http请求列表
	 */
	private final ConcurrentMap<String, HttpRequestStat> httpRequests = new ConcurrentHashMap<String, HttpRequestStat>();

	/**
	 * 构造方法
	 */
	private HttpRequestManager() {}

	/**
	 * 生成SqlId
	 * @return
	 */
	public long generateSqlId() {
		return idSeed.incrementAndGet();
	}

	/**
	 * 创建Http请求管理器实例
	 * @return
	 */
	public static final HttpRequestManager getInstance() {
		return instance;
	}

	/**
	 * 获取http请求列表
	 * @return
	 */
	public ConcurrentMap<String, HttpRequestStat> getHttpRequests() {
		return httpRequests;
	}
}
