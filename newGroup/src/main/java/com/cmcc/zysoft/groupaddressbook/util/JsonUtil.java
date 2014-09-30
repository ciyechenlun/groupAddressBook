/**
 * @author AMCC
 * <br /> 邮箱:zhouyusgs@ahmobile.com
 * <br /> 描述:JsonUtil.java
 * <br /> 版本：1.0.0
 * <br /> 日期：2013-10-17
 */
package com.cmcc.zysoft.groupaddressbook.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


/**
 * @author 周瑜
 *com.cmcc.zysoft.groupaddressbook.util
 * 创建时间：2013-10-17
 */
public class JsonUtil {

	/**
	 * 从一个JSON 对象字符格式中得到一个java对象，形如： {"id" : idValue, "name" : nameValue,
	 * "aBean" : {"aBeanId" : aBeanIdValue, ...}}
	 * 
	 * @param object
	 * @param clazz
	 * @return
	 */
	public static Object getDTO(String jsonString, Class clazz) {
		JSONObject jsonObject = null;
		try {
			jsonObject = JSONObject.parseObject(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSONObject.toJavaObject(jsonObject, clazz);
	}


	/**
	 * 从一个JSON数组得到一个java对象数组，形如： [{"id" : idValue, "name" : nameValue}, {"id" :
	 * idValue, "name" : nameValue}, ...]
	 * 
	 * @param object
	 * @param clazz
	 * @return
	 */
	public static Object[] getDTOArray(String jsonString, Class clazz) {
		JSONArray array = JSONArray.parseArray(jsonString);
		Object[] obj = new Object[array.size()];
		for (int i = 0; i < array.size(); i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			obj[i] = JSONObject.toJavaObject(jsonObject, clazz);
		}
		return obj;
	}


	/**
	 * 从一个JSON数组得到一个java对象集合
	 * 
	 * @param object
	 * @param clazz
	 * @return
	 */
	public static List getDTOList(String jsonString, Class clazz) {
		JSONArray array = JSONArray.parseArray(jsonString);
		List list = new ArrayList();
		for (Iterator iter = array.iterator(); iter.hasNext();) {
			JSONObject jsonObject = (JSONObject) iter.next();
			list.add(JSONObject.toJavaObject(jsonObject, clazz));
		}
		return list;
	}

	/**
	 * 从json数组中得到相应java数组 json形如：["123", "456"]
	 * 
	 * @param jsonString
	 * @return
	 */
	public static Object[] getObjectArrayFromJson(String jsonString) {
		JSONArray jsonArray = JSONArray.parseArray(jsonString);
		return jsonArray.toArray();
	}

}
