/**
 * 文件名:RulePraserThread.java
 * 作者 ：李三来<br />
 * 邮箱 ：li.sanlai@ustcinfo.com<br />
 * 描述 ：com.cmcc.zysoft.rule
 * 版本 ：2013-5-17<br />
 * 日期 ： 2013-5-17 上午9:58:19<br />
 * 版权 ：CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.<br />
 */
package com.cmcc.zysoft.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述： 解析规则的线程
 * <br />版本: 2013-5-17
 * <br />日期： 2013-5-17 上午9:58:19
 */
public class RulePraserThread extends Thread {

	/**
	 * 日志
	 */
	private static Logger _logger = LoggerFactory.getLogger(RulePraserThread.class);
	
	private static final String REGEX_TYPE = "regex";
	
	private static final String KEYWORD_TYPE = "keyword";
	
	/**
	 * 线程个数计数器
	 */
	private CountDownLatch threadsSignal;
	
	/**
	 * 列列表
	 */
	private List<Column> columns;
	
	/**
	 * 列节点
	 */
	private Node columuNode;

	/**
	 * 构造方法
	 */
	public RulePraserThread() {
		super();
	}

	/**
	 * 构造方法
	 * @param threadsSignal
	 * @param columns
	 * @param columuNode
	 */
	public RulePraserThread(CountDownLatch threadsSignal, List<Column> columns,
			Node columuNode) {
		super();
		this.threadsSignal = threadsSignal;
		this.columns = columns;
		this.columuNode = columuNode;
	}



	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		Element columuEl = (Element)columuNode;
    	String name = columuEl.attribute("name").getValue();
    	String code = columuEl.attribute("code").getValue();
    	_logger.debug("正在解析列名为{}的规则",name);
    	List<Node> ruleNodes = columuEl.selectNodes("Rules/Rule");
    	List<RegexRule> regexRules = new ArrayList<RegexRule>();
    	List<KeywordRule> keywordRules = new ArrayList<KeywordRule>();
    	for (Node ruleNode : ruleNodes) {
    		Element ruleEl = (Element)ruleNode;
    		String type = ruleEl.elementTextTrim("Type");
    		List<Node> valueNodes = ruleEl.selectNodes("Values/Value");
    		List<String> values = new ArrayList<String>();
    		for (Node valueNode : valueNodes) {
    			Element valueEl = (Element)valueNode;
    			String value = valueEl.getStringValue();
    			values.add(value);
			}
    		if(REGEX_TYPE.equalsIgnoreCase(type)){
    			regexRules.add(new RegexRule(type, values));
    		}else if(KEYWORD_TYPE.equalsIgnoreCase(type)){
    			keywordRules.add(new KeywordRule(type, values));
    		}
		}
    	
    	Column column = new Column(name, code, regexRules, keywordRules);
    	columns.add(column);
    	// 线程执行完成的时候计数器减1
		threadsSignal.countDown();
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
	 * 返回 columns
	 * @return columns
	 */
	public List<Column> getColumns() {
		return columns;
	}

	/**
	 * 设置columns
	 * @param columns the columns to set
	 */
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	/**
	 * 返回 columuNode
	 * @return columuNode
	 */
	public Node getColumuNode() {
		return columuNode;
	}

	/**
	 * 设置columuNode
	 * @param columuNode the columuNode to set
	 */
	public void setColumuNode(Node columuNode) {
		this.columuNode = columuNode;
	}
	
}
