// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.spring.security.jmx;

import java.util.concurrent.atomic.AtomicLong;

import javax.management.JMException;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.TabularData;
import javax.management.openmbean.TabularDataSupport;
import javax.management.openmbean.TabularType;

import com.cmcc.zysoft.spring.security.support.HttpRequestManager;
import com.cmcc.zysoft.spring.security.support.HttpRequestStat;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述：URI状态管理器
 * <br />版本:1.0.0
 * <br />日期： 2013-1-10 下午7:45:15
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
public class URIStatManager implements URIStatManagerMBean {
	
	/**
	 * URI管理器实例
	 */
	private final static URIStatManager instance = new URIStatManager();
	
	/**
	 * 重置次数
	 */
	private final AtomicLong resetCount = new AtomicLong();
	
	/**
	 * 构造方法
	 */
	private URIStatManager() {}

	/**
	 * 获取URI列表
	 */
	@Override
	public TabularData getUriList() throws JMException {
		CompositeType rowType = HttpRequestStat.getCompositeType();
        String[] indexNames = rowType.keySet().toArray(new String[rowType.keySet().size()]);

        TabularType tabularType = new TabularType("URIListStatistic", "URIListStatistic", rowType, indexNames);
        TabularData data = new TabularDataSupport(tabularType);
        
        for (HttpRequestStat httpRequestStat : HttpRequestManager.getInstance().getHttpRequests().values()) {
            data.put(new CompositeDataSupport(HttpRequestStat.getCompositeType(), httpRequestStat.getData()));
        }
        
        return data;
	}

	/**
	 * 重置
	 */
	@Override
	public void reset() {
		resetCount.incrementAndGet();
	}

	/**
	 * 获取重置次数
	 */
	@Override
	public long getResetCount() {
		return resetCount.get();
	}
	
	/**
	 * 返回URI管理器实例
	 * @return
	 */
	public static final URIStatManager getInstance() {
        return instance;
    }

}
