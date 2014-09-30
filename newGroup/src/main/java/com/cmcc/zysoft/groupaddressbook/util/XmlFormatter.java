/**
 * CopyRight © 2012 USTC SINOVATE SOFTWARE CO.LTD All Rights Reserved.
 */

package com.cmcc.zysoft.groupaddressbook.util;

import java.io.IOException;
import java.io.StringWriter;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.cmcc.zysoft.framework.utils.XMLUtil;

/**
 * XML格式化工具.
 * @author 袁凤建
 * <br />邮箱: yuan.fengjian@ustcinfo.com
 * <br />描述: XmlFormatter.java
 * <br />版本: 1.0.0
 * <br />日期: 2013-7-3 下午11:16:35
 * <br />CopyRight © 2012 USTC SINOVATE SOFTWARE CO.LTD All Rights Reserved.
 */

public class XmlFormatter {  
	
	/**
	 * 格式化XML报文.
	 * @param doc xml文档
	 * @return 格式化后的XML报文
	 * @throws Exception 
	 * 返回类型：String
	 */
	public static String formatXML(Document doc) throws Exception {  
        StringWriter out=null;  
        try{  
            OutputFormat formate=OutputFormat.createPrettyPrint();  
            out=new StringWriter();  
            XMLWriter writer=new XMLWriter(out,formate);  
            writer.write(doc);  
        } catch (IOException e){  
           e.printStackTrace();  
        } finally{  
            out.close();   
        }  
        return out.toString();  
    }  
	
	/**
	 * 格式化XML报文.
	 * @param unformattedXML
	 * @return String
	 * @throws Exception 
	 */
	public static String formatXML(String unformattedXML) throws Exception {
		Document document = XMLUtil.parseXML(unformattedXML);
		return XMLUtil.formatXML(document);
	}
    
	/**
	 * 报文格式化测试.
	 * @param args
	 * @throws Exception
	 */
    public static void main(String[] args) throws Exception {  
        String s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><PARAM><DBID>35</DBID><SEQUENCE>atgtca</SEQUENCE><MAXNS>10</MAXNS><MINIDENTITIES>90</MINIDENTITIES><MAXEVALUE>10</MAXEVALUE><USERNAME>admin</USERNAME><PASSWORD>111111</PASSWORD><TYPE>P</TYPE><RETURN_TYPE>2</RETURN_TYPE></PARAM>";//未格式化前的xml  
        System.out.println(XmlFormatter.formatXML(s));
    }
}