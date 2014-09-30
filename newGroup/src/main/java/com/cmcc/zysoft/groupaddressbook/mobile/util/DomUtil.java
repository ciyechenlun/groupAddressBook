package com.cmcc.zysoft.groupaddressbook.mobile.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.management.modelmbean.XMLParseException;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * XML组装和解析的工具类
 * 
 * @author <a href="mailto:seawavecau@hotmail.com">Ken Yu</a>
 * @version 1.0
 * 
 *          Create on 2009-12-7 下午03:22:34
 */
public class DomUtil {
	private static Logger log = Logger.getLogger(DomUtil.class);

	private static DocumentBuilderFactory factory;

	private static ConcurrentHashMap<String, Schema> schemaMap = new ConcurrentHashMap<String, Schema>();// 缓存<文件路径名,校验shema>的映射关系

	static {
		factory = DocumentBuilderFactory.newInstance();
	}

	/**
	 * 获取Xml文档对象
	 * 
	 * @return
	 */
	public static Document getDocument() {
		Document doc = null;
		try {
			doc = factory.newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			log.error("", e);
		}
		// doc.setXmlStandalone(true);
		return doc;
	}

	/**
	 * 创建Xml，以String的形式返回
	 * 
	 * @param document
	 *            非空的Document对象
	 * @return
	 */
	public static String createXmlString(Document document) {
		if (document == null) {
			throw new NullPointerException("document:null");
		}
		TransformerFactory tf = TransformerFactory.newInstance();
		String s = null;
		try {
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(document);
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			StreamResult result = new StreamResult(new ByteArrayOutputStream());
			transformer.transform(source, result);
			s = result.getOutputStream().toString();
		} catch (Exception e) {
			log.error("DOM转化异常:", e);
		}
		log.debug("DOM RESULT : " + s);
		return s;
	}

	/**
	 * 解析流对象成一个Document对象
	 * 
	 * @param is
	 *            InputStream
	 * @return
	 */
	public static Document parserXml(InputStream is) {
		Document document = null;
		try {
			document = factory.newDocumentBuilder().parse(is);
		} catch (Exception e) {
			log.error("parse error:", e);
		}
		return document;
	}

	/**
	 * 解析流对象成一个Document对象
	 * 
	 * @param is
	 *            InputStream
	 * @return
	 */
	public static Document parserXml(String is) {
		Document document = null;
		try {
			document = factory.newDocumentBuilder().parse(
					new InputSource(new StringReader(is)));
		} catch (Exception e) {
			log.error("parse error:", e);
		}
		return document;
	}

	/**
	 * 解析流对象成一个Document对象
	 * 
	 * @param is
	 *            InputStream
	 * @return
	 */
	public static Document parserXml(InputSource is) {
		Document document = null;
		try {
			document = factory.newDocumentBuilder().parse(is);
		} catch (Exception e) {
			log.error("parse error:", e);
		}
		return document;
	}

	/**
	 * 解析一个xml文件成一个Document对象
	 * 
	 * @param file
	 *            File
	 * @return
	 */
	public static Document parserXml(File file) {
		Document document = null;
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			document = factory.newDocumentBuilder().parse(is);
		} catch (Exception e) {
			log.error("parse error:", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					log.error("file close error:", e);
				}
			}
		}
		return document;
	}

	/**
	 * 解析一个xml文件成一个Document对象
	 * 
	 * @param filePath
	 *            String
	 * @return
	 */
	public static Document parserXmlFromFile(String filePath) {
		log.info("filePath=" + filePath);
		Document document = parserXml(new File(filePath));
		return document;
	}

	/**
	 * 用一个模板文件校验一个xml数据流
	 * 
	 * @param document
	 *            xml数据流
	 * @param file
	 *            模板文件地址
	 * @return 校验通过返回true;否则返回false
	 */
	public static boolean validateXml(InputStream document, String file) {
		SchemaFactory factory = SchemaFactory
				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		// 获取验证的schema
		Schema schema = null;
		if (schemaMap.containsKey(file)) {
			schema = schemaMap.get(file);
		} else {
			Source schemaSource = new StreamSource(
					ClassLoader.getSystemResourceAsStream(file));
			try {
				schema = factory.newSchema(schemaSource);
				schemaMap.put(file, schema);
			} catch (SAXException saxe) {
				log.error("validate XML error:", saxe);
			}
		}
		// 获取验证器，验证器的XML Schema源就是之前创建的Schema
		Validator validator = schema.newValidator();
		// 执行验证
		try {
			validator.validate(new StreamSource(document));
			return true;
		} catch (Exception e) {
			log.error("validate XML error:", e);
			return false;
		}
	}

	/**
	 * 添加一个xml element到Document中
	 * 
	 * @param doc
	 *            Document
	 * @param elementKey
	 *            String
	 * @param value
	 *            String
	 * @return
	 */
	public static Document addElementToDocument(Document doc,
			String elementKey, String value) {
		if (doc == null || elementKey == null) {
			throw new NullPointerException();
		}
		doc.appendChild(createElement(doc, elementKey, value));
		return doc;
	}

	/**
	 * 添加一个子节点到节点中
	 * 
	 * @param doc
	 *            Document
	 * @param elementKey
	 *            String
	 * @param key
	 *            String
	 * @param value
	 *            String
	 * @return
	 * @throws XMLParseException
	 */
	public static Document addElementToElement(Document doc, String elementKey,
			String key, String value) throws XMLParseException {
		if (doc == null || elementKey == null || key == null) {
			throw new NullPointerException();
		}
		NodeList nl = doc.getElementsByTagName(elementKey);
		if (nl == null || nl.getLength() < 1) {
			throw new XMLParseException("Node[" + elementKey
					+ "] is not exist.");
		}
		Element ele = (Element) doc.getElementsByTagName(elementKey).item(0);
		ele.appendChild(createElement(doc, key, value));
		return doc;
	}

	/**
	 * 添加一个属性到一个节点中
	 * 
	 * @param doc
	 *            Document
	 * @param elementKey
	 *            String
	 * @param keyValue
	 *            String
	 * @return
	 * @throws XMLParseException
	 */
	public static Document addAttributeToElement(Document doc,
			String elementKey, Map<String, String> keyValue)
			throws XMLParseException {
		if (doc == null || elementKey == null || keyValue == null) {
			throw new NullPointerException();
		}
		NodeList nl = doc.getElementsByTagName(elementKey);
		if (nl == null || nl.getLength() < 1) {
			throw new XMLParseException("Node[" + elementKey
					+ "] is not exist.");
		}
		Element ele = (Element) doc.getElementsByTagName(elementKey).item(0);
		Iterator<Entry<String, String>> kvIterator = keyValue.entrySet()
				.iterator();
		while (kvIterator.hasNext()) {
			Entry<String, String> kvEntry = kvIterator.next();
			ele.setAttribute(kvEntry.getKey(), kvEntry.getValue());
		}
		return doc;
	}

	/**
	 * 创建一个节点
	 * 
	 * @param doc
	 *            Document
	 * @param key
	 *            String
	 * @param value
	 *            String
	 * @return
	 */
	public static Element createElement(Document doc, String key, String value) {
		if (doc == null || key == null) {
			throw new NullPointerException();
		}
		Element keyEle = doc.createElement(key);
		if (value != null) {
			Text valueText = doc.createTextNode("");
			valueText.setData(value);
			keyEle.appendChild(valueText);
		}
		return keyEle;
	}

	/**
	 * 从Document中中移除一个节点 注意：如果elementKey对应多个节点，那么这些节点都会被删除
	 * 
	 * @param doc
	 *            Document
	 * @param elementKey
	 *            String
	 */
	public static void removeElementFromDocument(Document doc, String elementKey) {
		if (doc == null || elementKey == null) {
			throw new NullPointerException();
		}
		NodeList list = doc.getElementsByTagName(elementKey);
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			doc.removeChild(node);
		}
	}

	/**
	 * 从一个节点中移除一个子节点 注意：如果elementKey对应多个节点，那么这些节点中的子节点都会被删除
	 * 
	 * @param doc
	 *            Document
	 * @param elementKey
	 *            String
	 * @param subElementKey
	 *            String
	 */
	public static void removeElementFromElement(Document doc,
			String elementKey, String subElementKey) {
		if (doc == null || elementKey == null) {
			throw new NullPointerException();
		}
		NodeList list = doc.getElementsByTagName(elementKey);
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			NodeList subList = doc.getElementsByTagName(subElementKey);
			for (int j = 0; j < subList.getLength(); j++) {
				Node subNode = subList.item(j);
				node.removeChild(subNode);
			}
		}
	}

	/**
	 * 从一个节点中移除一个属性 注意：如果elementKey对应多个节点，那么这些节点下的attriKey属性都会被删除
	 * 
	 * @param doc
	 *            Document
	 * @param elementKey
	 *            String
	 * @param attriKey
	 *            String
	 */
	public static void removeAttriFromElement(Document doc, String elementKey,
			String attriKey) {
		if (doc == null || elementKey == null || attriKey == null) {
			throw new NullPointerException();
		}
		NodeList list = doc.getElementsByTagName(elementKey);
		for (int i = 0; i < list.getLength(); i++) {
			Element node = (Element) list.item(i);
			node.removeAttribute(attriKey);
		}
	}

	/**
	 * 
	 * @param doc
	 *            Document
	 * @param elementKey
	 *            String
	 * @return
	 */
	public static String getElementTextFromDocument(Document doc,
			String elementKey) {
		if (doc == null || elementKey == null) {
			throw new NullPointerException();
		}
		NodeList list = doc.getElementsByTagName(elementKey);
		Element element = (list != null && list.getLength() >= 1) ? (Element) list
				.item(0) : null;
		if (element != null) {
			return element.getFirstChild().getNodeValue();
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param doc
	 *            Document
	 * @param elementKey
	 *            String
	 * @param key
	 *            String
	 * @param value
	 *            String
	 * @param index
	 *            int
	 * @return
	 * @throws XMLParseException
	 */
	public static Document addElementToElement(Document doc, String elementKey,
			String key, String value, int index) throws XMLParseException {
		if (doc == null || elementKey == null || key == null) {
			throw new NullPointerException();
		}
		NodeList nl = doc.getElementsByTagName(elementKey);
		if (nl == null || nl.getLength() < 1) {
			throw new XMLParseException("Node[" + elementKey
					+ "] is not exist.");
		}
		Element ele = (Element) doc.getElementsByTagName(elementKey)
				.item(index);
		ele.appendChild(createElement(doc, key, value));
		return doc;
	}

}