/**
 * 文件名:RuleUtil.java
 * 作者 ：李三来<br />
 * 邮箱 ：li.sanlai@ustcinfo.com<br />
 * 描述 ：com.cmcc.zysoft.rule
 * 版本 ：2013-5-17<br />
 * 日期 ： 2013-5-17 上午9:46:34<br />
 * 版权 ：CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.<br />
 */
package com.cmcc.zysoft.rule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.cmcc.zysoft.framework.utils.XMLUtil;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述： RuleUtil
 * <br />版本: 2013-5-17
 * <br />日期： 2013-5-17 上午9:46:34
 */
public class RuleUtil {
	
	private static Logger logger = LoggerFactory.getLogger(RuleUtil.class);

	/**
	 * 从规则文件解析列信息
	 * @date 2013-5-17 上午9:51:57
	 * @param ruleXml
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Column> parseColumns(String ruleFileName){
		List<Column> columns = new ArrayList<Column>();
		//读取规则文件，解析规则文件
		Document document = null;
		ClassPathResource resource = new ClassPathResource("META-INF/rule/"+ruleFileName);
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
			String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
            	buffer.append(tempString);
            }
            reader.close();
            String xml = buffer.toString();
            logger.debug("规则文件内容\n {}",xml);
            document = XMLUtil.parseXML(xml);
            List<Node> columuNodes = document.selectNodes("//Columns/Column");
            // 初始化线程数
            int size = columuNodes.size();
    		CountDownLatch threadsSignal = new CountDownLatch(size);
            for (Node columuNode : columuNodes) {
            	Thread thread = new RulePraserThread(threadsSignal, columns, columuNode);
            	thread.start();
			}
            threadsSignal.await();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally{
			if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                	e.printStackTrace();
                }
            }
		}
		
		return columns;
	}
	
	public static void main(String[] args){
		Logger logger = LoggerFactory.getLogger(RuleUtil.class);
		List<Column> columns = RuleUtil.parseColumns("cmcc.xml");
		logger.debug(""+columns.size());
	}
}
