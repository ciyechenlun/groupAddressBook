/**
 * @author AMCC
 * <br /> 邮箱:zhouyusgs@ahmobile.com
 * <br /> 描述:CacheFileUtil.java
 * <br /> 版本：1.0.0
 * <br /> 日期：2013-10-12
 */
package com.cmcc.zysoft.groupaddressbook.util;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 周瑜
 *com.cmcc.zysoft.groupaddressbook.util
 * 创建时间：2013-10-12
 */
public class CacheFileUtil {
	
	/**
	 * 搜索指定目录下所有文本文件
	 * @param companyId
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static File[] getFilesByCompanyId(String companyId)
	{
		String fileDir = Constant.cacheFilePath;
		File file = new File(fileDir);
		String s = companyId + "_*.txt";
		s = s.replace('.','#');
		s = s.replaceAll("#","\\\\.");
		s = s.replace('*','#');
		s = s.replaceAll("#", ".*");
		s = s.replace('?', '#');
		s = s.replaceAll("#", ".?");
		s = "^" + s + "$";
		Pattern p = Pattern.compile(s);
		ArrayList list = filePattern(file,p);
		if(list!=null)
		{
			File[] rtn = new File[list.size()];
			list.toArray(rtn);
			return rtn;
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * 删除指定目录下的缓存文件
	 * @param companyId
	 */
	public static void deleteCahceFiles(String companyId)
	{
		File[] files = getFilesByCompanyId(companyId);
		if(files!=null){
			for(File file : files)
			{
				file.delete();
			}
		}
	}
	
	/**
	 * 
	 * @param file 起始文件夹
	 * @param p 匹配类型
	 * @return 其文件夹下的文件
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static ArrayList filePattern(File file,Pattern p)
	{
		if(file == null)
		{
			return null;
		}
		else if(file.isFile())
		{
			Matcher fMatcher = p.matcher(file.getName());
			if(fMatcher.matches())
			{
				ArrayList list = new ArrayList();
				list.add(file);
				return list;
			}
		}
		else if(file.isDirectory())
		{
			File[] files = file.listFiles();
			if(files!=null && files.length>0)
			{
				ArrayList list = new ArrayList();
				for(File f :files)
				{
					ArrayList rlist = filePattern(f,p);
					if(rlist!=null)
					{
						list.addAll(rlist);
					}
				}
				return list;
			}
		}
		return null;
	}
}
