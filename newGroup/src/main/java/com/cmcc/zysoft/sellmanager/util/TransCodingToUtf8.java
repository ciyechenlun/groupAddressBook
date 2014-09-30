package com.cmcc.zysoft.sellmanager.util;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述：UTF8编码工具类
 * <br />版本:1.0.0
 * <br />日期： 2013-1-11 下午7:49:09
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
public class TransCodingToUtf8 {
	
	/**
	 * 将字符串转换成utf8编码
	 * @param s
	 * @return
	 */
	public static String toUtf8String(String s){
        StringBuffer sb = new StringBuffer();
        for (int i=0;i<s.length();i++){
            char c = s.charAt(i);
           if (c >= 0 && c <= 255){sb.append(c);}
            else{
                byte[] b;
                try { b = Character.toString(c).getBytes("utf-8");}
               catch (Exception ex) {
                    System.out.println(ex);
                    b = new byte[0];
                 }
                for (int j = 0; j < b.length; j++) {
                   int k = b[j];
                    if (k < 0) k += 256;
                     sb.append("%" + Integer.toHexString(k).toUpperCase());
                 }
             }
         }
       return sb.toString();
     }
}
