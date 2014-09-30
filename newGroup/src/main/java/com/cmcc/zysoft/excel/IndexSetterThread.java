/**
 * 文件名:IndexSetterThread.java
 * 作者 ：李三来<br />
 * 邮箱 ：li.sanlai@ustcinfo.com<br />
 * 描述 ：com.cmcc.zysoft.excel
 * 版本 ：2013-5-17<br />
 * 日期 ： 2013-5-17 下午12:32:20<br />
 * 版权 ：CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.<br />
 */
package com.cmcc.zysoft.excel;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmcc.zysoft.rule.Column;
import com.cmcc.zysoft.rule.KeywordRule;
import com.cmcc.zysoft.rule.RegexRule;
import com.cmcc.zysoft.rule.Rule;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述： IndexSetterThread
 * <br />版本: 2013-5-17
 * <br />日期： 2013-5-17 下午12:32:20
 */
public class IndexSetterThread extends Thread {
	
	private static Logger logger = LoggerFactory.getLogger(IndexSetterThread.class);
	
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
	 * 列索引
	 */
	private int index;
	
	/**
	 * 构造方法
	 */
	public IndexSetterThread() {
	}
	
	/**
	 * 构造方法
	 * @param threadsSignal
	 * @param excelValueMap
	 * @param column
	 * @param index
	 */
	public IndexSetterThread(CountDownLatch threadsSignal,
			Map<Integer, List<Object>> excelValueMap, Column column, int index) {
		this.threadsSignal = threadsSignal;
		this.excelValueMap = excelValueMap;
		this.column = column;
		this.index = index;
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
	 * 返回 index
	 * @return index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * 设置index
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	
	/**
	 * 检查正则匹配结果
	 * @date 2013-5-17 下午1:01:22
	 * @param regex
	 * @param str
	 * @return
	 */
	private boolean checkRegex(String regex,String str){
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		boolean flag = m.matches();
		return flag;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		List<RegexRule> regexRules = column.getRegexRules();
		List<KeywordRule> keywordRules = column.getKeywordRules();
		List<Object> values = excelValueMap.get(index);
		boolean flag = false;
		int failNum = 0;
		int successNum = 0;
		boolean isAllEqual = true;//所有数据相等
		if(!regexRules.isEmpty()){
			Rule rule = regexRules.get(0);
			if(null != values&&values.size()>0){
				String eValue=values.get(0).toString();
				for (Object value : values) {
					if(!((String)value).equals(eValue)){
						isAllEqual=false;
					}
					boolean checkFlag = false;
					if(value==null||"".equals(value)){
						//failNum ++;
					}else{
						checkFlag = checkRegex(rule.getValues().get(0),(String)value);
						if(checkFlag){
							if(!keywordRules.isEmpty()){
								Rule keywordRule = keywordRules.get(0);
								List<String> ruleValues = keywordRule.getValues();
								boolean iskeyOk=false;
								if(column.getName().equals("姓名")){
									for (String ruleValue : ruleValues) {
										if(checkRegex("^"+ruleValue+".*$",(String)value)){
											successNum ++;
											iskeyOk=true;
											break;
										}
									}
								}else if(column.getName().equals("单位")||
										column.getName().equals("二级部门")||
										column.getName().equals("三级部门")||
										column.getName().equals("四级部门")||
										column.getName().equals("五级部门")||
										column.getName().equals("职位")||
										column.getName().equals("单位")||
										column.getName().equals("专业")||
										column.getName().equals("学校")){
									for (String ruleValue : ruleValues) {
										if(checkRegex("^.*"+ruleValue+"$",(String)value)){
											successNum ++;
											iskeyOk=true;
											break;
										}
									}
								}else{
									for (String ruleValue : ruleValues) {
										if(((String)value).indexOf(ruleValue)>=0){
											successNum ++;
											iskeyOk=true;
											break;
										}
									}
								}
								if(!iskeyOk){
									failNum++;
								}
							}else{
								successNum ++;
							}
						}else{
							failNum ++;
						}
					}
					//logger.debug("决策列对应关系,当前规则列={},索引号={},列值={},flag={}",new String[]{column.getName(),""+index,(String)value,(checkFlag==true?"true":"false")});
				}
			}
			//如果一列数据有80%的数据符合格式要求，则视为正确的列
			if((float)(successNum+failNum)==0||(float)successNum/(float)(successNum+failNum)<0.5){
				flag=false;
			}else{
				flag=true;
			}
			logger.debug("列名={},index={},successNum={},failNum={},flag={}",new String[]{column.getName(),""+index,""+successNum,""+failNum,""+flag});
		}
		if(column.getName().equals("单位")&&!isAllEqual&&flag){
			//column.setColIndex(index);
		}else if(column.getName().equals("单位")&&isAllEqual&&flag){
			column.setColIndex(index);
		}else if(!column.getName().equals("单位")&&isAllEqual&&flag){
			//column.setColIndex(index);
		}else{
			if(flag&&column.getColIndex()==-1){
				column.setColIndex(index);
			}
		}
		// 线程执行完成的时候计数器减1
		threadsSignal.countDown();
		
	}
}
