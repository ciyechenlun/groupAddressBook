// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.spring.security.support;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.management.JMException;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;

import com.alibaba.druid.util.JMXUtils;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述：Http请求状态
 * <br />版本:1.0.0
 * <br />日期： 2013-1-10 下午7:26:38
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
public class HttpRequestStat {

	private final String     uri;
	private long             id;
    private final AtomicLong executeErrorCount     = new AtomicLong();
    private Throwable        executeErrorLast;
    private long             executeErrorLastTime;
    
    //执行成功次数
    private final AtomicLong executeSuccessCount = new AtomicLong();
    //最慢消耗时间（纳秒）
    private final AtomicLong executeSpanNanoMax    = new AtomicLong();
    //最慢发生的时间（毫秒）
    private long executeSpanMaxOccurTime;
    //总共时间（纳秒）
    private final AtomicLong executeSpanNanoTotal = new AtomicLong();
    //最后一次执行的时间
    private long executeLastStartTime;
    
    /**
     * 构造方法
     * @param uri
     */
    public HttpRequestStat(String uri){
        this.uri = uri;
    }
    
    /**
     * 获取ID
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     * 设置ID
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }
    
    /**
     * 获取URI
     * @return
     */
    public String getUri() {
        return uri;
    }
    
    /**
     * 获取请求最后的开始时间
     * @return
     */
    public Date getExecuteLastStartTime() {
        if (executeLastStartTime <= 0) {
            return null;
        }

        return new Date(executeLastStartTime);
    }
    
    /**
     * 设置请求最后的开始时间
     * @param executeLastStartTime
     */
    public void setExecuteLastStartTime(long executeLastStartTime) {
        this.executeLastStartTime = executeLastStartTime;
    }
    
    /**
     * 获取executeSpanMaxOccurTime
     * @return
     */
    public Date getExecuteSpanMaxOccurTime() {
        if (executeSpanMaxOccurTime <= 0) {
            return null;
        }
        return new Date(executeSpanMaxOccurTime);
    }
    
    /**
     * 设置executeSpanMaxOccurTime
     * @return
     */
    public Date getExecuteErrorLastTime() {
        if (executeErrorLastTime <= 0) {
            return null;
        }
        return new Date(executeErrorLastTime);
    }
    
    /**
     * 增加请求成功次数
     */
    public void incrementExecuteSuccessCount() {
        executeSuccessCount.incrementAndGet();
    }

    /**
     * 获取请求成功次数
     * @return
     */
    public long getExecuteSuccessCount() {
        return executeSuccessCount.get();
    }
    
    /**
     * 获取请求的毫秒数
     * @return
     */
    public long getExecuteMillisTotal() {
        return executeSpanNanoTotal.get() / (1000 * 1000);
    }

    /**
     * 获取最大的请求耗时
     * @return
     */
    public long getExecuteMillisMax() {
        return executeSpanNanoMax.get() / (1000 * 1000);
    }

    /**
     * 获取请求的错误次数
     * @return
     */
    public long getErrorCount() {
        return executeErrorCount.get();
    }
    
    /**
     * 获取请求的次数
     * @return
     */
    public long getExecuteCount() {
        return getErrorCount() + getExecuteSuccessCount();
    }
    
    /**
     * getExecuteErrorLast方法
     * @return
     */
    public Throwable getExecuteErrorLast() {
        return executeErrorLast;
    }

    /**
     * error方法
     * @param error
     */
    public void error(Throwable error) {
        executeErrorCount.incrementAndGet();
        executeErrorLastTime = System.currentTimeMillis();
        executeErrorLast = error;

    }
    
    /**
     * addExecuteTime方法
     * @param nanoSpan
     */
    public void addExecuteTime(long nanoSpan) {
        executeSpanNanoTotal.addAndGet(nanoSpan);

        for (;;) {
            long current = executeSpanNanoMax.get();
            if (current < nanoSpan) {
                if (executeSpanNanoMax.compareAndSet(current, nanoSpan)) {
                    // 可能不准确，但是绝大多数情况下都会正确，性能换取一致性
                	executeSpanMaxOccurTime = System.currentTimeMillis();

                    break;
                } else {
                    continue;
                }
            } else {
                break;
            }
        }
    }
    
    /**
     * COMPOSITE_TYPE
     */
    private static CompositeType COMPOSITE_TYPE = null;
    
    /**
     * 获取getCompositeType
     * @return
     * @throws JMException
     */
    public static CompositeType getCompositeType() throws JMException {

        if (COMPOSITE_TYPE != null) {
            return COMPOSITE_TYPE;
        }

        OpenType<?>[] indexTypes = new OpenType<?>[] { SimpleType.LONG, SimpleType.STRING, SimpleType.LONG,
        		JMXUtils.getThrowableCompositeType(), SimpleType.DATE, SimpleType.LONG, SimpleType.LONG, SimpleType.DATE,
                SimpleType.LONG, SimpleType.DATE};

        String[] indexNames = { "ID", "URI", "ExecuteErrorCount", "ExecuteErrorLast", "ExecuteErrorLastTime" //
                , "ExecuteSuccessCount", "ExecuteSpanNanoMax", "ExecuteSpanMaxOccurTime", "ExecuteSpanNanoTotal", "ExecuteLastStartTime"};
        String[] indexDescriptions = indexNames;
        COMPOSITE_TYPE = new CompositeType("URIStatistic", "URI Statistic", indexNames, indexDescriptions, indexTypes);

        return COMPOSITE_TYPE;
    }
    
    /**
     * getData
     * @return
     * @throws JMException
     */
    public Map<String, Object> getData() throws JMException {
    	Map<String, Object> map = new HashMap<String, Object>();
        map.put("ID", getId());
        map.put("URI", getUri());
        map.put("ExecuteErrorCount", getErrorCount());
        map.put("ExecuteErrorLast", getExecuteErrorLast());
        map.put("ExecuteErrorLastTime", getExecuteErrorLastTime());
        map.put("ExecuteSuccessCount", getExecuteSuccessCount());
        map.put("ExecuteSpanNanoMax", getExecuteMillisMax());
        map.put("ExecuteSpanMaxOccurTime", getExecuteSpanMaxOccurTime());
        map.put("ExecuteSpanNanoTotal", getExecuteMillisTotal());
        map.put("ExecuteLastStartTime", getExecuteLastStartTime());
        
        return map;
    }
}
