/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cmcc.zysoft.groupaddressbook.util;

import java.util.Iterator;
import java.util.Map;

import org.springframework.util.StringUtils;


/**
 * 
 * @author Administrator
 */
public class GenerateUrlParam {

	public static String getUrl(Map paramMap, String url) {
		//String urlParam = "";
		StringBuffer sb = new StringBuffer();
		String[] paramValues;
		int i = 0;
		Iterator iter = paramMap.entrySet().iterator();
		while (iter.hasNext()) {
			String key = "";
			String value = "";
			Map.Entry entry = (Map.Entry) iter.next();
			key = entry.getKey().toString();
			paramValues = (String[]) entry.getValue();
			value = paramValues[0];
			if (key.equals("page.curPageNo") || key.equals("page.pageSize")) {
				continue;
			}
			if (i < (paramMap.size() - 1)) {
				//urlParam += key + "=" + value + "&";
				sb.append(key).append("=").append(value).append("&");
			} else {
				//urlParam += key + "=" + value;
				sb.append(key).append("=").append(value);
			}
			i++;
		}
		if (StringUtils.hasText((sb.toString()))) {//urlParam
			return url;
			// request.getContextPath();//getRequestURI().replaceFirst("/", "");
		}
		return url + "?" + sb.toString(); //urlParam;
	}
}
