/**
 * 文件名:AutoMatchThread.java
 * 作者 ：李三来<br />
 * 邮箱 ：li.sanlai@ustcinfo.com<br />
 * 描述 ：com.cmcc.zysoft.excel
 * 版本 ：2013-5-17<br />
 * 日期 ： 2013-5-17 上午11:29:01<br />
 * 版权 ：CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.<br />
 */
package com.cmcc.zysoft.excel;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import com.cmcc.zysoft.rule.Column;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述： AutoMatchThread
 * <br />版本: 2013-5-17
 * <br />日期： 2013-5-17 上午11:29:01
 */
public class AutoMatchThread extends Thread {
	
	//private Logger logger =  LoggerFactory.getLogger(AutoMatchThread.class);

	/**
	 * 线程个数计数器
	 */
	private CountDownLatch threadsSignal;
	
	/**
	 * excel索引和值得集合
	 */
	private Map<Integer, List<Object>> excelValueMap;
	
	/**
	 * 列列表
	 */
	private Column column;
	
	/**
	 * 配置过索引的列集合
	 */
	private List<Column> indexdColumuns;
	
	/**
	 * 列索引集合
	 */
	private Map<Integer, Column> columnIndexMap;

	/**
	 * 构造方法
	 */
	public AutoMatchThread() {
		super();
	}

	/**
	 * 构造方法
	 * @param threadsSignal
	 * @param excelValueMap
	 * @param column
	 * @param indexdColumuns
	 */
	public AutoMatchThread(CountDownLatch threadsSignal,
			Map<Integer, List<Object>> excelValueMap, Column column,
			List<Column> indexdColumuns) {
		super();
		this.threadsSignal = threadsSignal;
		this.excelValueMap = excelValueMap;
		this.column = column;
		this.indexdColumuns = indexdColumuns;
	}
	
	public AutoMatchThread(CountDownLatch threadsSignal,
			Map<Integer, List<Object>> excelValueMap, Column column,
			Map<Integer, Column> columnIndexMap) {
		super();
		this.threadsSignal = threadsSignal;
		this.excelValueMap = excelValueMap;
		this.column = column;
		this.columnIndexMap = columnIndexMap;
	}

	/**
	 * 返回 threadsSignal
	 * @return threadsSignal
	 */
	public CountDownLatch getThreadsSignal() {
		return threadsSignal;
	}

	/**
	 * 设置threadsSignal
	 * @param threadsSignal the threadsSignal to set
	 */
	public void setThreadsSignal(CountDownLatch threadsSignal) {
		this.threadsSignal = threadsSignal;
	}

	/**
	 * 返回 excelValueMap
	 * @return excelValueMap
	 */
	public Map<Integer, List<Object>> getExcelValueMap() {
		return excelValueMap;
	}

	/**
	 * 设置excelValueMap
	 * @param excelValueMap the excelValueMap to set
	 */
	public void setExcelValueMap(Map<Integer, List<Object>> excelValueMap) {
		this.excelValueMap = excelValueMap;
	}

	/**
	 * 返回 column
	 * @return column
	 */
	public Column getColumn() {
		return column;
	}

	/**
	 * 设置column
	 * @param column the column to set
	 */
	public void setColumn(Column column) {
		this.column = column;
	}

	/**
	 * 返回 indexdColumuns
	 * @return indexdColumuns
	 */
	public List<Column> getIndexdColumuns() {
		return indexdColumuns;
	}

	/**
	 * 设置indexdColumuns
	 * @param indexdColumuns the indexdColumuns to set
	 */
	public void setIndexdColumuns(List<Column> indexdColumuns) {
		this.indexdColumuns = indexdColumuns;
	}
	
	public Map<Integer, Column> getColumnIndexMap() {
		return columnIndexMap;
	}

	public void setColumnIndexMap(Map<Integer, Column> columnIndexMap) {
		this.columnIndexMap = columnIndexMap;
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		Column indexdColumn = column;
		Set<Integer> keyset = excelValueMap.keySet();
		// 初始化线程数
        int size = keyset.size();
		CountDownLatch threadsSignal1 = new CountDownLatch(size);
		for (Integer index : keyset) {
			Thread thread = new IndexSetterThread(threadsSignal1, excelValueMap, indexdColumn, index);
			thread.start();
		}
		try {
			threadsSignal1.await();
//			indexdColumuns.add(indexdColumn);
//			logger.debug("列名：{}，索引：{}",indexdColumn.getName(),indexdColumn.getColIndex());
			columnIndexMap.put(indexdColumn.getColIndex(), indexdColumn);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// 线程执行完成的时候计数器减1
		threadsSignal.countDown();
	}
	
}
