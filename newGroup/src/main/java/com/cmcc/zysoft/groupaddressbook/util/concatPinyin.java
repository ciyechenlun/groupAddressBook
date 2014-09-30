// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.util;

import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：pinyin
 * <br />版本:1.0.0
 * <br />日期： 2013-3-28 上午11:06:08
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
public class concatPinyin {
	
	/**
	 * .
	 * @param pinyinArray 
	 * @return 
	 * 返回类型：String
	 */
	public static String concatPinyinStringArray(String[] pinyinArray)
	    {
	        StringBuffer pinyinStrBuf = new StringBuffer();
	
	        if ((null != pinyinArray) && (pinyinArray.length > 0))
	        {
	            for (int i = 0; i < pinyinArray.length; i++)
	            {
	                pinyinStrBuf.append(pinyinArray[i]);
	                pinyinStrBuf.append(System.getProperty("line.separator"));
	            }
	        }
	        String outputString = pinyinStrBuf.toString();
	        return outputString;
	    }
	
	/**
	 * 将名字转化为拼音.
	 * @param userName 
	 * @return 
	 * 返回类型：String
	 */
	public static String changeToPinyin(String userName){
		if(userName.length()==0){
			return "";
		}else{
			String result = "";
			for(int i=0;i<userName.length();i++){
				char hanzi = userName.charAt(i);//取名字中的每一个字,如果为字母或者数字,不做转换.
				if((hanzi >= '0' && hanzi <= '9') || (hanzi >= 'a' && hanzi <= 'z') || (hanzi >= 'A' && hanzi <= 'Z')){
					result += hanzi;
				}else{
					try{
						String [] pinyin = PinyinHelper.toHanyuPinyinStringArray(hanzi);//转化后的结果,音调可能不同
						if(pinyin.length==0){
							result += "";//如果没有转化成功,则用空格代替
						}else{
							String rightName = pinyin[0];//取结果中的第一个
							result += rightName.substring(0, rightName.length()-1);//截取掉末尾的音调
						}
					}catch (Exception e){
						result += "";
					}
				}
			}
			return result;
		}
	}
	
	/**
	 * 名字拼音的首字母.
	 * @param userName 
	 * @return 
	 * 返回类型：String
	 */
	public static String firstwordOfName(String userName){
		if(userName.length()==0){
			return "";
		}else{
			String result = "";
			for(int i=0;i<userName.length();i++){
				char hanzi = userName.charAt(i);//取名字中的每一个字,如果为字母或者数字,不做转换.
				if((hanzi >= '0' && hanzi <= '9') || (hanzi >= 'a' && hanzi <= 'z') || (hanzi >= 'A' && hanzi <= 'Z')){
					result += "";
				}else{
					try {
						String [] pinyin = PinyinHelper.toHanyuPinyinStringArray(hanzi);//转化后的结果,音调可能不同
						if(pinyin.length==0){
							result += "";//如果没有转化成功,则用空格代替
						}else{
							String rightName = pinyin[0];//取结果中的第一个
							result += rightName.substring(0, 1);//取首字母
						}
					}catch (Exception e){
						result += "";
					}
					
				}
			}
			return result;
		}
	}
}
